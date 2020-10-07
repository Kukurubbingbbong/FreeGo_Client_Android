package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.embeddedtest.retrofit.DTO.DeleteFoodBody;
import com.example.myapplication.retrofit.API.FridgeAPI;
import com.example.myapplication.retrofit.API.RetrofitHelper;
import com.example.myapplication.retrofit.DTO.FirstData;
import com.example.myapplication.retrofit.DTO.Food;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ItemListAdapter extends BaseAdapter {

    String id;
    Context context;
    ArrayList<Food> food;
    int resource;
    FridgeAPI fridge = new RetrofitHelper().getFridgeAPI();

    public ItemListAdapter(Context context, ArrayList<Food> food, int resource, String id){
        this.context = context;
        this.food = food;
        this.resource = resource;
        this.id = id;
    }


    @Override
    public int getCount() {
        return food.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(resource, null);
        }

        ImageView foodImage = convertView.findViewById(R.id.foodImage);
        TextView foodTitle = convertView.findViewById(R.id.foodTitle);
        TextView foodDate = convertView.findViewById(R.id.foodDate);
        TextView btnDelete = convertView.findViewById(R.id.btnDelete);
        TextView foodCount = convertView.findViewById(R.id.textCount);

        Glide.with(context).load(food.get(position).getImg_link()).placeholder(R.drawable.loading).error(R.drawable.noimage).into(foodImage);
        foodTitle.setText(food.get(position).getP_name());
        foodCount.setText(food.get(position).getP_number() + "개");
        String[] date = food.get(position).getP_ex_date().split(" ");
        Date current_date = new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String ex_month = date_check(date[2]);
        String ex_date_String = date[3] + "-" + ex_month + "-" + date[1];
        Date ex_date;
        boolean date_big = true;
        long date_second = 0;
        try{
            ex_date = format.parse(ex_date_String);

            if(ex_date.getTime() >= current_date.getTime()){
                date_second = ex_date.getTime() - current_date.getTime();
                date_second = date_second / (24*60*60*1000) + 1;
                date_big = true;
            } else if(ex_date.getTime() < current_date.getTime()){
                date_second = current_date.getTime() - ex_date.getTime();
                date_second = date_second / (24*60*60*1000) + 1;
                date_big = false;
            }
            date_second = Math.abs(date_second);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(date_big){
            foodDate.setText("D-" + (date_second));
        } else {
            foodDate.setText("D+" + (date_second));
        }

        btnDelete.setOnClickListener(v -> {
            DeleteDialog dialog = new DeleteDialog(context, new DialogClickListener() {
                @Override
                public void onAccept() {

                    fridge.deleteFood(new DeleteFoodBody(id, food.get(position).getP_name())).enqueue(new Callback<FirstData>() {
                        @Override
                        public void onResponse(Call<FirstData> call, Response<FirstData> response) {
                            Toast.makeText(context, "삭제 완료", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(Call<FirstData> call, Throwable t) {}
                    });

                    food.remove(position);
                    notifyDataSetChanged();
                }

                @Override
                public void onCancel() {}
            });
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();
        });


        return convertView;
    }

    private String date_check(String month){
        switch (month){
            case "Jan" :
                return "01";
            case "Feb" :
                return "02";
            case "Mar" :
                return "03";
            case "Apr" :
                return "04";
            case "May" :
                return "05";
            case "Jun" :
                return "06";
            case "Jul" :
                return "07";
            case "Aug":
                return "08";
            case "Sep" :
                return "09";
            case "Oct" :
                return "10";
            case "Nov" :
                return "11";
            case "Dec" :
                return "12";
        }
        return null;
    }
}
