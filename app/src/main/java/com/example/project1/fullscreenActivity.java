package com.example.project1;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

public class fullscreenActivity extends AppCompatActivity {

    ViewPager viewPager;
    ViewPagerAdapterTab2 adapter;
    ArrayList<Gallery_Photo> images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_pager);

        // Receive Data
        Bundle bundle = getIntent().getExtras();
        images = (ArrayList<Gallery_Photo>) bundle.getSerializable("Gallery_Photos");

        viewPager = findViewById(R.id.viewpager);
        adapter = new ViewPagerAdapterTab2(fullscreenActivity.this, images);
        viewPager.setAdapter(adapter);
    }
}
