package com.example.project1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentUris;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;

import android.net.Uri;
import android.provider.ContactsContract;

import android.widget.TableLayout;

import com.google.android.material.appbar.AppBarLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.database.Cursor;

import java.util.ArrayList;

import android.content.ContentResolver;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    //variables for Tab1
    private ArrayList<String> Names = new ArrayList<>();
    private ArrayList<String> Numbers = new ArrayList<>();
    private ArrayList<Bitmap> Photos = new ArrayList<>();

    //variables for Tab2
    private ArrayList<Gallery_Photo> tab2_gallery_photos = new ArrayList<>();

    private TableLayout tablayout;
    private AppBarLayout appBarLayout;


//    public class ContactItem implements Serializable {
//        private String user_number, user_name;
//        private long photo_id=0, person_id=0;
//        private int id;
//
//        public ContactItem() {
//        }
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
//        public String getUser_number() {
//            return user_number;
//        }
//
//        public String getUser_name() {
//            return user_name;
//        }
//
//        public void setId(int id) {
//            this.id = id;
//        }
//
//        public int getId() {
//            return id;
//        }
//
//        public void setUser_number(String string) {
//            this.user_number = string;
//        }
//
//        public void setUser_name(String string) {
//            this.user_name = string;
//        }
//
//        @Override
//        public String toString() {
//            return this.user_number;
//        }
//
//        @Override
//        public int hashCode() {
//            return getNumberChanged().hashCode();
//        }
//
//        public String getNumberChanged() {
//            return user_number.replace("-", "");
//        }
//
//        @Override
//        public boolean equals(Object o) {
//            if (o instanceof ContactItem)
//                return getNumberChanged().equals(((ContactItem) o).getNumberChanged());
//            return false;
//        }
//    }


    /*public ArrayList<ContactItem> getContactList(){
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = new String[]{
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.Contacts.PHOTO_ID,
                ContactsContract.Contacts._ID
        };

        String[] selectionArgs = null;
        String sortOrder = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC";
        Cursor cursor = getContentResolver().query(uri, projection, null, selectionArgs, sortOrder);
        LinkedHashSet<ContactItem> hashlist = new LinkedHashSet<>();
        if (cursor.moveToFirst()) {
            do {
                long photo_id = cursor.getLong(2);
                long person_id = cursor.getLong(3);
                ContactItem contactItem = new ContactItem();
                contactItem.setUser_number(cursor.getString(0));
                contactItem.setUser_name(cursor.getString(1));
                contactItem.setPhoto_id(photo_id);
                contactItem.setPerson_id(person_id);
                hashlist.add(contactItem);
            } while (cursor.moveToNext());
        }

        ArrayList<ContactItem> contactItems = new ArrayList<>(hashlist);


        // this is just for setting id for each contact..
        for (int i=0 ; i< contactItems.size(); i++){
            contactItems.get(i).setId(i);
        }
        return contactItems;
    }*/

    public JSONArray getContactList(){
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = new String[]{
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.Contacts.PHOTO_ID,
                ContactsContract.Contacts._ID
        };

        String[] selectionArgs = null;
        String sortOrder = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC";
        Cursor cursor = getContentResolver().query(uri, projection, null, selectionArgs, sortOrder);

        JSONArray jArray = new JSONArray();

        if (cursor.moveToFirst()){
            do {
                try {
                    JSONObject sObject = new JSONObject();
                    sObject.put("number", cursor.getString(0));
                    sObject.put("name", cursor.getString(1));
                    sObject.put("photo_id", cursor.getLong(2));
                    sObject.put("person_id", cursor.getLong(3));
                    jArray.put(sObject);
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }while (cursor.moveToNext());
        }
        return jArray;
    }

    public Bitmap loadContactPhoto(ContentResolver cr, long id, long photo_id){
//        Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, id);
//        InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(cr, uri);
//        if (input != null)
//            return resizingBitmap(BitmapFactory.decodeStream(input));
//        else
//            Log.d("PHOTO", "first try failed to load photo");
        byte[] photoBytes = null;
        Uri photoUri = ContentUris.withAppendedId(ContactsContract.Data.CONTENT_URI, photo_id);
        String sortOrder = ContactsContract.CommonDataKinds.Photo.PHOTO + " ASC";
        Cursor c = cr.query(photoUri, new String[]{ContactsContract.CommonDataKinds.Photo.PHOTO}, null, null, sortOrder);
        try{
            if (c.moveToFirst())
                photoBytes = c.getBlob(0);
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            c.close();
        }
        if (photoBytes != null)
            return resizingBitmap(BitmapFactory.decodeByteArray(photoBytes, 0, photoBytes.length));
        else
            Log.d("PHOTO", "second try failed to load photo");
        return null;
    }
    public Bitmap resizingBitmap(Bitmap oBitmap){
        if (oBitmap == null)
            return null;
        float width = oBitmap.getWidth();
        float height = oBitmap.getHeight();
        float resizing_size = 120;
        Bitmap rBitmap = null;
        if (width > resizing_size){
            float mWidth = (float) (width/100);
            float fScale = (float) (resizing_size / mWidth);
            width *= (fScale/100);
            height *= (fScale/100);
        }else if (height > resizing_size){
            float mHeight = (float) (height/100);
            float fScale = (float) (resizing_size / mHeight);
            width *= (fScale/100);
            height *= (fScale/100);
        }
        //Log.d("rBitmap : " + width + "," + height);
        rBitmap = Bitmap.createScaledBitmap(oBitmap, (int)width, (int)height, true);
        return rBitmap;
    }

    // adding images/photos and the names of corresponding contacts to each of their own lists

//    private void initImageBitmaps(ArrayList<ContactItem> contactItems) {
//        Log.d(TAG, "initImageBitmaps: preparing bitmaps");
//        ContentResolver cr = getContentResolver();
//        for (int j = 0; j < contactItems.size(); j++) {
//            Names.add(contactItems.get(j).getUser_name());
//            Numbers.add(contactItems.get(j).getUser_number());
//            Photos.add(loadContactPhoto(cr, contactItems.get(j).getPerson_id(), contactItems.get(j).getPhoto_id()));
//        }
//        initTab1RecyclerView(Names, Numbers, Photos);
//    }

    private void initContactInfo(JSONArray jArray) {
        Log.d(TAG, "initContactInfo: preparing contact info");

        ContentResolver cr = getContentResolver();

        for (int j=0 ; j< jArray.length(); j++){
            try {
                Names.add(jArray.getJSONObject(j).getString("name"));
                Numbers.add(jArray.getJSONObject(j).getString("number"));
                Photos.add(loadContactPhoto(cr,
                        jArray.getJSONObject(j).getLong("person_id"),
                        jArray.getJSONObject(j).getLong("photo_id")));
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
        initTab1RecyclerView(Names, Numbers, Photos);
    }

    private void initTab1RecyclerView(ArrayList<String> Names, ArrayList<String> Numbers, ArrayList<Bitmap> Photos) {
        Log.d(TAG, "initTab1RecyclerView: init recyclerView.");
        RecyclerView recyclerViewtab1 = findViewById(R.id.recycler_view_tab1);
        RecyclerViewAdapterTab1 adapterTab1 = new RecyclerViewAdapterTab1(Names, Numbers, Photos,this);
        recyclerViewtab1.setAdapter(adapterTab1);
        recyclerViewtab1.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initGalleryInfo() {
        Log.d(TAG, "initGalleryInfo: preparing gallery info");

        tab2_gallery_photos.add(new Gallery_Photo("blues_shop_silk", R.drawable.blue_shop_silk_flower));
        tab2_gallery_photos.add(new Gallery_Photo("german_shepherd", R.drawable.german_shepherd));
        tab2_gallery_photos.add(new Gallery_Photo("sycamore_yes", R.drawable.sycamore_yes));
        tab2_gallery_photos.add(new Gallery_Photo("blue_eye_doggy", R.drawable.blue_eye_doggy));
        tab2_gallery_photos.add(new Gallery_Photo("blue_butterfly", R.drawable.bluebutterfly));
        tab2_gallery_photos.add(new Gallery_Photo("chihuahua", R.drawable.chihuahua));
        tab2_gallery_photos.add(new Gallery_Photo("daylily_flower_and_buds_sharp", R.drawable.daylily_flower));
        tab2_gallery_photos.add(new Gallery_Photo("flowervase", R.drawable.flowervase));
        tab2_gallery_photos.add(new Gallery_Photo("puppy_development", R.drawable.puppy_development));
        tab2_gallery_photos.add(new Gallery_Photo("rosebear", R.drawable.rosebear));
        tab2_gallery_photos.add(new Gallery_Photo("treefaces", R.drawable.treefaces));

        initTab2RecyclerView(tab2_gallery_photos);
    }

    private void initTab2RecyclerView(ArrayList<Gallery_Photo> tab2_gallery_photos) {
        Log.d(TAG, "initTab2RecyclerView: init recyclerView for tab2.");
        RecyclerView recyclerViewtab2 = findViewById(R.id.recycler_view_tab2);
        RecyclerViewAdapterTab2 adapterTab2 = new RecyclerViewAdapterTab2(this, tab2_gallery_photos);
        recyclerViewtab2.setAdapter(adapterTab2);
        recyclerViewtab2.setLayoutManager(new GridLayoutManager(this, 3));

        /*recyclerViewtab2.setItemAnimator(new DefaultItemAnimator());
        recyclerViewtab2.addOnItemTouchListener(new adapterTab2.RecyclerTouchListener(getApplicationContext(), recyclerViewtab2, new ));*/
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: started.");

        TabHost tabHost1 = (TabHost) findViewById(R.id.tabHost1);
        tabHost1.setup();

        // 첫 번째 Tab. (탭 표시 텍스트:"TAB 1"), (페이지 뷰:"content1")
        TabHost.TabSpec ts1 = tabHost1.newTabSpec("Tab Spec 1");
        ts1.setContent(R.id.content1);
        ts1.setIndicator("Contacts");
        tabHost1.addTab(ts1);

        JSONArray jArray = getContactList();
        initContactInfo(jArray);

        // 두 번째 Tab. (탭 표시 텍스트:"TAB 2"), (페이지 뷰:"content2")
        TabHost.TabSpec ts2 = tabHost1.newTabSpec("Tab Spec 2");
        ts2.setContent(R.id.content2);
        ts2.setIndicator("Gallery");
        tabHost1.addTab(ts2);

        initGalleryInfo();

        // 세 번째 Tab. (탭 표시 텍스트:"TAB 3"), (페이지 뷰:"content3")
        TabHost.TabSpec ts3 = tabHost1.newTabSpec("Tab Spec 3");
        ts3.setContent(R.id.content3);
        ts3.setIndicator("Tab 3");
        tabHost1.addTab(ts3);

        tabHost1.setCurrentTab(0);

    }

}

