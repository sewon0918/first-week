package com.example.project1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;

import android.view.View;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;

import android.widget.TableLayout;

import com.google.android.material.appbar.AppBarLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.database.Cursor;
import java.util.ArrayList;
import android.widget.ListView;
import android.widget.SimpleAdapter;



public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    //variables for RecyclerView ?
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();

    private TableLayout tablayout;
    private AppBarLayout appBarLayout;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: started.");

        initImageBitmaps();

        TabHost tabHost1 = (TabHost) findViewById(R.id.tabHost1);
        tabHost1.setup();

        // 첫 번째 Tab. (탭 표시 텍스트:"TAB 1"), (페이지 뷰:"content1")
        TabHost.TabSpec ts1 = tabHost1.newTabSpec("Tab Spec 1");
        ts1.setContent(R.id.content1);
        ts1.setIndicator("Contacts");
        tabHost1.addTab(ts1);

        // 두 번째 Tab. (탭 표시 텍스트:"TAB 2"), (페이지 뷰:"content2")
        TabHost.TabSpec ts2 = tabHost1.newTabSpec("Tab Spec 2");
        ts2.setContent(R.id.content2);
        ts2.setIndicator("Gallery");
        tabHost1.addTab(ts2);

        // 세 번째 Tab. (탭 표시 텍스트:"TAB 3"), (페이지 뷰:"content3")
        TabHost.TabSpec ts3 = tabHost1.newTabSpec("Tab Spec 3");
        ts3.setContent(R.id.content3);
        ts3.setIndicator("Tab 3");
        tabHost1.addTab(ts3);

        tabHost1.setCurrentTab(0);
    }

    // adding images/photos and the names of corresponding contacts to each of their own lists
    private void initImageBitmaps(){
        Log.d(TAG, "initImageBitmaps: preparing bitmaps");

        mImageUrls.add("https://www.thelabradorsite.com/wp-content/uploads/2018/04/9-weeks.jpg");
        mNames.add("Labrador Puppy");

        mImageUrls.add("https://rlv.zcache.com/pembroke_welsh_corgi_puppy_postcard-rd3641220834848e6936aa22aca40087f_vgbaq_8byvr_540.jpg");
        mNames.add("Corgi Puppy");

        mImageUrls.add("https://www.warrenphotographic.co.uk/photography/bigs/40748-Cute-red-Toy-Poodle-puppy-white-background.jpg");
        mNames.add("Poodle Puppy");

        mImageUrls.add("https://i.ytimg.com/vi/wRx3Uvcktm8/maxresdefault.jpg");
        mNames.add("Pug Puppy");

        mImageUrls.add("https://i.pinimg.com/originals/3c/d2/a8/3cd2a844037b921028481f9f3f82d21f.jpg");
        mNames.add("Husky Puppy");

        mImageUrls.add("http://www.icewindshibas.com/wp-content/uploads/2017/07/red-resized-01.jpg");
        mNames.add("Shiba Inu Puppy");

        initRecyclerView();
    }

    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: init recyclerView.");
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(mNames, mImageUrls, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
