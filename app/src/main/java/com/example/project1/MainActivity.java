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
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import android.widget.Button;
import android.app.Activity;
import java.io.Serializable;




public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    //variables for RecyclerView ?
//    private ArrayList<String> mNames = new ArrayList<>();
//    private ArrayList<String> mImageUrls = new ArrayList<>();
    private ArrayList<String> Names = new ArrayList<>();
    private ArrayList<String> Numbers = new ArrayList<>();
    private TableLayout tablayout;
    private AppBarLayout appBarLayout;


    public class ContactItem implements Serializable{
        private String user_number, user_name;
//        private long photo_id=0, person_id=0;
        private int id;
        public ContactItem(){}
//        public long getPhoto_id(){
//            return photo_id;
//        }
//        public long getPerson_id(){
//            return person_id;
//        }
//        public void setPhoto_id(long id){
//            this.photo_id=id;
//        }
//        public void setPerson_id(long id){
//            this.person_id=id;
//        }
        public String getUser_number(){
            return user_number;
        }
        public String getUser_name(){
            return user_name;
        }
        public void setId(int id){
            this.id=id;
        }
        public int getId(){
            return id;
        }
        public void setUser_number(String string){
            this.user_number=string;
        }
        public void setUser_name(String string){
            this.user_name=string;
        }
        @Override
        public String toString(){
            return this.user_number;
        }
        @Override
        public int hashCode(){
            return getNumberChanged().hashCode();
        }
        public String getNumberChanged(){
            return user_number.replace("-","");
        }
        @Override
        public boolean equals(Object o){
            if (o instanceof ContactItem)
                return getNumberChanged().equals(((ContactItem)o).getNumberChanged());
            return false;
        }
    }

    public ArrayList<ContactItem> getContactList(){
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = new String[]{
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
//                ContactsContract.Contacts.PHOTO_ID,
//                ContactsContract.Contacts._ID
        };
        String[] selectionArgs = null;
        //String sortOrder = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + "COLLATE LOCALIZED ASC";
        Cursor cursor = getContentResolver().query(uri, projection, null, selectionArgs, null);
        LinkedHashSet<ContactItem> hashlist = new LinkedHashSet<>();
        if (cursor.moveToFirst()){
            do {
//                long photo_id = cursor.getLong(2);
//                long person_id = cursor.getLong(3);
                ContactItem contactItem = new ContactItem();
                contactItem.setUser_number(cursor.getString(0));
                contactItem.setUser_name(cursor.getString(1));
//                contactItem.setPhoto_id(photo_id);
//                contactItem.setPerson_id(person_id);
                hashlist.add(contactItem);
            }while (cursor.moveToNext());
        }
        ArrayList<ContactItem> contactItems = new ArrayList<>(hashlist);
        for (int i=0 ; i< contactItems.size(); i++){
            contactItems.get(i).setId(i);
        }
        return contactItems;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Log.d(TAG, "onCreate: started.");

        TabHost tabHost1 = (TabHost) findViewById(R.id.tabHost1);
        tabHost1.setup();

        ArrayList<ContactItem> contactItems = getContactList();
        initImageBitmaps(contactItems);


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
    private void initImageBitmaps(ArrayList<ContactItem> contactItems){
        Log.d(TAG, "initImageBitmaps: preparing bitmaps");

        for (int j=0 ; j< contactItems.size(); j++){
            Names.add(contactItems.get(j).getUser_name());
            Numbers.add(contactItems.get(j).getUser_number());
        }
        initRecyclerView( Names, Numbers);
    }

    private void initRecyclerView(ArrayList<String> Names, ArrayList<String> Numbers){
        Log.d(TAG, "initRecyclerView: init recyclerView.");
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(Names, Numbers, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    }


//    public void onClick02(View v) {
//        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:010-1234-5678"));
//        startActivity(intent);
//    }


