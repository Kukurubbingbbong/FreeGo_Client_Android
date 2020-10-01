package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.example.myapplication.retrofit.API.FridgeAPI;
import com.example.myapplication.retrofit.API.RetrofitHelper;
import com.example.myapplication.retrofit.DTO.BarCodeFoodInfo;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BarcodeActivity extends AppCompatActivity {

    private CaptureManager capture;
    private DecoratedBarcodeView barcodeScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);

        barcodeScannerView = findViewById(R.id.barcodeView);
        capture = new CaptureManager(this, barcodeScannerView);
        capture.initializeFromIntent(this.getIntent(),savedInstanceState);

        capture.decode();

        barcodeScannerView.decodeContinuous(result -> readBarcode(result.toString()));

    }

    @Override
    protected void onResume() {
        super.onResume();
        capture.onResume();
    }

    @Override
    protected void onPause(){
        super.onPause();
        capture.onPause();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        capture.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(@NotNull Bundle outState){
        super.onSaveInstanceState(outState);
        capture.onSaveInstanceState(outState);
    }


    public void readBarcode(String barcode){
        if(barcode.length() == 13) {
            ProgressBar progressBar = findViewById(R.id.barcodeProgress);
            progressBar.setAlpha(1f);
            FridgeAPI fridgeAPI = new RetrofitHelper().getFridgeAPI();
            Intent getInt = getIntent();
            fridgeAPI.barCodeShow(barcode).enqueue(new Callback<BarCodeFoodInfo>() {
                @Override
                public void onResponse(Call<BarCodeFoodInfo> call, Response<BarCodeFoodInfo> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            if(response.body().getCode() == 200) {
                                if (response.body().getData() != null) {
                                    Intent intent = new Intent(getApplicationContext(), AddFoodActivity.class);
                                    intent.putExtra("data", response.body().getData());
                                    intent.putExtra("barcode", barcode);
                                    intent.putExtra("id", getInt.getStringExtra("id"));
                                    startActivity(intent);
                                    finish();
                                }

                            } else {
                                progressBar.setAlpha(0f);
                                Toast.makeText(getApplicationContext(), "등록되지 않은 식품입니다. 직접 입력해주세요", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<BarCodeFoodInfo> call, Throwable t) {}
            });
        }
    }

}