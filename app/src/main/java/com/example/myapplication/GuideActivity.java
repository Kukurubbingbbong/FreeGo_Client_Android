package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class GuideActivity extends FragmentActivity {

    private ViewPager mPager;

    Button btnSkip;

    TabLayout tabLayout;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        MyAdapter adapter = new MyAdapter(getSupportFragmentManager());

        btnSkip = findViewById(R.id.btnSkip);
        btnSkip.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), FridgeActivity.class);
            intent.putExtra("name", getIntent().getStringExtra("name"));
            startActivity(intent);
            finish();
        });
        mPager = findViewById(R.id.pager);
        mPager.setCurrentItem(0);
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(mPager,true);

        mPager.setAdapter(adapter);


    }

    class MyAdapter extends FragmentStatePagerAdapter{

        public MyAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0 : return new GuideFragment1();
                case 1 : return new GuideFragment2();
                default : return new GuideFragment3();
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            super.destroyItem(container, position, object);
        }
    }

}
