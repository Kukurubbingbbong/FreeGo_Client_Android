package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.myapplication.retrofit.DTO.Recipe;

import java.util.ArrayList;

public class RecipeAdapter extends BaseAdapter{

    Context context;
    ArrayList<String> recipes;

    RecipeAdapter(Context context, ArrayList<String> recipes){
        this.context = context;
        this.recipes = recipes;
    }

    @Override
    public int getCount() {
        return recipes.size();
    }

    @Override
    public Object getItem(int i) {
        return recipes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        @SuppressLint("ViewHolder") View convertView = LayoutInflater.from(context).inflate(R.layout.recipe_row,viewGroup, false);


        return convertView;
    }
}
