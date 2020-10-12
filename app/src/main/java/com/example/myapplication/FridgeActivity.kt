package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myapplication.retrofit.API.RetrofitHelper
import com.example.myapplication.retrofit.DTO.Food
import com.example.myapplication.retrofit.DTO.FoodData
import kotlinx.android.synthetic.main.activity_fridge.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FridgeActivity : AppCompatActivity() {

    var id = ""
    var dataList = ArrayList<Food>()

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fridge)

        id = intent.getStringExtra("name")


        val arrayList = Array<String>(2) { android.Manifest.permission.INTERNET; android.Manifest.permission.RECORD_AUDIO}
        ActivityCompat.requestPermissions(this, arrayList, 1)

        if(id != "") {
           change()
        }
        itemList.setOnItemClickListener{ parent: AdapterView<*>, view: View, position: Int, l: Long ->
            val intent = Intent(this@FridgeActivity, DetailActivity::class.java)
            intent.putExtra("p_id", dataList[position].id)
            intent.putExtra("p_name",dataList[position].p_name)
            intent.putExtra("p_ex_date", dataList[position].p_ex_date)
            intent.putExtra("p_number",dataList[position].p_number)
            intent.putExtra("img_link", dataList[position].img_link)
            startActivity(intent)
        }

        itemList.onItemLongClickListener = longClickListener

        insertButton.setOnClickListener {
            val intent = Intent(this@FridgeActivity, SelectActivity::class.java)
            intent.putExtra("id",id)
            startActivity(intent)
        }

        ttsButton.setOnClickListener {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, packageName)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR")

            val mRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
            mRecognizer.setRecognitionListener(listener)
            mRecognizer.startListening(intent)

        }
    }

    private val longClickListener =
        AdapterView.OnItemLongClickListener { parent, view, position, id ->
            val intent = Intent(this@FridgeActivity, CountChangeActivity::class.java)
            intent.putExtra("p_id", dataList[position].id)
            intent.putExtra("p_name",dataList[position].p_name)
            intent.putExtra("p_ex_date", dataList[position].p_ex_date)
            intent.putExtra("p_number",dataList[position].p_number)
            intent.putExtra("img_link", dataList[position].img_link)
            startActivity(intent)
            true
        }

    private val listener = object : RecognitionListener {
        override fun onReadyForSpeech(params: Bundle?) {
            Toast.makeText(applicationContext, "음성인식을 시작합니다!", Toast.LENGTH_LONG).show()
        }

        override fun onRmsChanged(rmsdB: Float) {}

        override fun onBufferReceived(buffer: ByteArray?) {}

        override fun onPartialResults(partialResults: Bundle?) {}

        override fun onEvent(eventType: Int, params: Bundle?) {}

        override fun onBeginningOfSpeech() {}

        override fun onEndOfSpeech() {}

        override fun onError(error: Int) {
            var message = ""
            message = when(error) {
                SpeechRecognizer.ERROR_AUDIO -> "오디오 에러"
                SpeechRecognizer.ERROR_CLIENT -> "클라이언트 에러"
                SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "권한 에러"
                SpeechRecognizer.ERROR_NETWORK -> "네트워크 에러"
                SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "빠르게 말하세요"
                SpeechRecognizer.ERROR_NO_MATCH -> "매칭되는 단어가 없습니다.\n알맞는 말인지 확인 후 말해주세요!"
                SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "오류가 발생했습니다.\n잠시뒤에 말씀해주세요"
                SpeechRecognizer.ERROR_SERVER -> "서버에서 오류가 발생했습니다"
                SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "시간이 초과되었습니다."
                else -> "오디오 에러"
            }
            Toast.makeText(this@FridgeActivity, "에러 발생! : $message", Toast.LENGTH_LONG).show()
        }

        override fun onResults(results: Bundle?) {
            val matchs = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)

            var isGoDetail = false
            if (matchs != null) {
                for (s : String in matchs){
                    if(s.contains("로 할 수 있는 요리 알려 줘") || s.contains("으로 할 수 있는 요리 알려 줘") || s.contains("으로 할 수 있는 음식 알려 줘") || s.contains("로 할 수 있는 음식 알려 줘")){
                        val intent = Intent(this@FridgeActivity, DetailActivity::class.java)
                        intent.putExtra("p_name", s)
                        isGoDetail = true
                        startActivity(intent)
                    }
                }
                if(!isGoDetail){
                    Toast.makeText(this@FridgeActivity, "(재료명)으로 할 수 있는 요리 알려 줘 라고 말씀하세요", Toast.LENGTH_LONG).show()
                }
            }
        }

    }

    fun change(){
        val service = RetrofitHelper().getFridgeAPI()

        service.getTable(id).enqueue(object : Callback<FoodData> {
            override fun onFailure(call: Call<FoodData>, t: Throwable) {}

            override fun onResponse(call: Call<FoodData>, response: Response<FoodData>) {
                if (response.isSuccessful) {
                    if (response.body()!!.code == 200) {
                        dataList = response.body()!!.data
                        if (dataList.size == 0) {
                            itemList.setBackgroundColor(Color.GRAY)
                            textView3.alpha = 1f
                        } else {
                            itemList.setBackgroundDrawable(ContextCompat.getDrawable(this@FridgeActivity, R.drawable.rectangle))
                            textView3.alpha = 0f
                        }
                        val adapter =
                            ItemListAdapter(
                                this@FridgeActivity,
                                dataList,
                                R.layout.food_row,
                                id
                            )
                        itemList.adapter = adapter
                    }
                }
            }
        })
    }

    override fun onPause() {
        super.onPause()
        change()
    }

    override fun onResume() {
        super.onResume()
        change()
    }

    override fun onStop() {
        super.onStop()
        change()
    }

    override fun onRestart() {
        super.onRestart()
        change()
    }
}