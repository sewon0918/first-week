package com.example.project1;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

public class fullscreenActivity extends AppCompatActivity {

    private String TAG = "fullscreenActivity";

    ViewPager viewPager;
    ViewPagerAdapterTab2 adapter;
    ArrayList gallery_photos;
    //Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_pager);
        gallery_photos = new ArrayList<>();

        // Receive Data
        //Intent intent = getIntent();
        //int current_image = intent.getExtras().getInt("current_thumbnail");
        //bundle = intent.getExtras();
        gallery_photos = (ArrayList) getIntent().getParcelableArrayListExtra("gallery_photos");
        //images = (ArrayList<Gallery_Photo>) bundle.getSerializable("Gallery_Photos");

        viewPager = findViewById(R.id.viewpager);
        adapter = new ViewPagerAdapterTab2(fullscreenActivity.this, gallery_photos);
        viewPager.setAdapter(adapter);
    }
}
