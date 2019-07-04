package com.example.project1;

<<<<<<< HEAD
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentUris;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;

import android.view.View;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;

import android.widget.TableLayout;
=======
import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.PersistableBundle;
import android.provider.ContactsContract;
import android.provider.MediaStore.Images;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
>>>>>>> com

import com.google.android.material.appbar.AppBarLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
<<<<<<< HEAD
import android.database.Cursor;

import java.io.InputStream;
import java.util.ArrayList;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import android.widget.Button;
import android.app.Activity;
import java.io.Serializable;
import android.content.ContentResolver;
import android.util.Log;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    //variables for RecyclerView ?
    private ArrayList<String> Names = new ArrayList<>();
    private ArrayList<String> Numbers = new ArrayList<>();
    private ArrayList<Bitmap> Photos = new ArrayList<>();
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
=======

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    String[] permission_list = {
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    //variables for Tab1
    private ArrayList<String> Names = new ArrayList<>();
    private ArrayList<String> Numbers = new ArrayList<>();
    private ArrayList<Bitmap> Photos = new ArrayList<>();
    private ArrayList<Bitmap> Gallery = new ArrayList<>();
    private ArrayList<String> imageList = new ArrayList<>();
    private String imageEncoded;
    private SwipeController swipeController = null;
    private final int ADD_CONTACT = 3;
    //variables for Tab2
    private TableLayout tablayout;
    private AppBarLayout appBarLayout;
    //variables for Tab3
    private Button[][] buttons = new Button[10][10];
    private boolean player1Turn = true;
    private int roundCount;
    private int player1Points;
    private int player2Points;
    private int tiePoints;
    private TextView textViewPlayer1;
    private TextView textViewPlayer2;
    private TextView textViewTie;
    private TextView timer;
    private final int PICK_IMAGE_REQUEST = 1;
    private final int TAKE_PICTURE = 2;
    //private CountDownTimer countDownTimer;
    private CountDownTimer  countDownTimer = new CountDownTimer(20000, 1000) {
        public void onTick(long millisUntilFinished) {
            timer.setText(String.format(Locale.getDefault(), "%d sec left.", millisUntilFinished / 1000L));
        }
        public void onFinish() {
            timer.setText("Done.");
            if (player1Turn){
                player2Win();
            }else{
                player1Win();
            }
        }
    };
>>>>>>> com

    public JSONArray getContactList(){
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = new String[]{
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.Contacts.PHOTO_ID,
                ContactsContract.Contacts._ID
        };
<<<<<<< HEAD

        String[] selectionArgs = null;
        String sortOrder = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC";
        Cursor cursor = getContentResolver().query(uri, projection, null, selectionArgs, sortOrder);

        JSONArray jArray = new JSONArray();

=======
        String[] selectionArgs = null;
        String sortOrder = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC";
        Cursor cursor = getContentResolver().query(uri, projection, null, selectionArgs, sortOrder);
        JSONArray jArray = new JSONArray();
>>>>>>> com
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
<<<<<<< HEAD
//        Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, id);
//        InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(cr, uri);
//        if (input != null)
//            return resizingBitmap(BitmapFactory.decodeStream(input));
//        else
//            Log.d("PHOTO", "first try failed to load photo");
=======
>>>>>>> com
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
<<<<<<< HEAD
=======

>>>>>>> com
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

<<<<<<< HEAD
    // adding images/photos and the names of corresponding contacts to each of their own lists

//    private void initImageBitmaps(ArrayList<ContactItem> contactItems) {
//        Log.d(TAG, "initImageBitmaps: preparing bitmaps");
//        ContentResolver cr = getContentResolver();
//        for (int j = 0; j < contactItems.size(); j++) {
//            Names.add(contactItems.get(j).getUser_name());
//            Numbers.add(contactItems.get(j).getUser_number());
//            Photos.add(loadContactPhoto(cr, contactItems.get(j).getPerson_id(), contactItems.get(j).getPhoto_id()));
//        }
//        initRecyclerView(Names, Numbers, Photos);
//    }

=======
>>>>>>> com
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
<<<<<<< HEAD
        initRecyclerView(Names, Numbers, Photos);
    }

    private void initRecyclerView(ArrayList<String> Names, ArrayList<String> Numbers, ArrayList<Bitmap> Photos) {
        Log.d(TAG, "initRecyclerView: init recyclerView.");
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(Names, Numbers, Photos,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
=======
        initTab1RecyclerView(Names, Numbers, Photos);
    }

    private void initTab1RecyclerView(ArrayList<String> Names, ArrayList<String> Numbers, ArrayList<Bitmap> Photos) {
        Log.d(TAG, "initTab1RecyclerView: init recyclerView.");
        final RecyclerView recyclerViewtab1 = findViewById(R.id.recycler_view_tab1);
        final RecyclerViewAdapterTab1 adapterTab1 = new RecyclerViewAdapterTab1(Names, Numbers, Photos,this);
        recyclerViewtab1.setAdapter(adapterTab1);
        recyclerViewtab1.setLayoutManager(new LinearLayoutManager(this));
        swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onRightClicked(int position) {
                // 아답타에게 알린다
                adapterTab1.Names.remove(position);
                adapterTab1.Numbers.remove(position);
                adapterTab1.Photos.remove(position);
                adapterTab1.notifyItemRemoved(position);
                adapterTab1.notifyItemRangeChanged(position, adapterTab1.getItemCount());
            }
        });
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(recyclerViewtab1);
        recyclerViewtab1.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });
    }

    private void initGalleryInfo(ArrayList<Bitmap> Gallery) {
        Log.d(TAG, "initGalleryInfo: preparing gallery info");
        initTab2RecyclerView(Gallery);
    }

    private void initTab2RecyclerView(ArrayList<Bitmap> Gallery) {
        Log.d(TAG, "initTab2RecyclerView: init recyclerView for tab2.");
        RecyclerView recyclerViewtab2 = findViewById(R.id.recycler_view_tab2);
        RecyclerViewAdapterTab2 adapterTab2 = new RecyclerViewAdapterTab2(this, Gallery);
        recyclerViewtab2.setAdapter(adapterTab2);
        recyclerViewtab2.setLayoutManager(new GridLayoutManager(this, 3));
>>>>>>> com
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
<<<<<<< HEAD
        //Log.d(TAG, "onCreate: started.");

        TabHost tabHost1 = (TabHost) findViewById(R.id.tabHost1);
        tabHost1.setup();

        JSONArray jArray = getContactList();
        initContactInfo(jArray);


=======
        checkPermission();
    }

    public void initial(){
        TabHost tabHost1 = (TabHost) findViewById(R.id.tabHost1);
        tabHost1.setup();

>>>>>>> com
        // 첫 번째 Tab. (탭 표시 텍스트:"TAB 1"), (페이지 뷰:"content1")
        TabHost.TabSpec ts1 = tabHost1.newTabSpec("Tab Spec 1");
        ts1.setContent(R.id.content1);
        ts1.setIndicator("Contacts");
        tabHost1.addTab(ts1);

<<<<<<< HEAD
=======
        JSONArray jArray = getContactList();
        initContactInfo(jArray);
        Button addContact = (Button)findViewById(R.id.button_addContact);

        addContact.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, AddContact.class);
                startActivityForResult(intent, ADD_CONTACT);
            }
        });

>>>>>>> com
        // 두 번째 Tab. (탭 표시 텍스트:"TAB 2"), (페이지 뷰:"content2")
        TabHost.TabSpec ts2 = tabHost1.newTabSpec("Tab Spec 2");
        ts2.setContent(R.id.content2);
        ts2.setIndicator("Gallery");
        tabHost1.addTab(ts2);
<<<<<<< HEAD
=======
        Button gallery = (Button)findViewById(R.id.button_gallery);
        Button camera = (Button)findViewById(R.id.button_camera);
        gallery.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent();
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                // Show only images, no videos or anything else
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                // Always show the chooser (if there are multiple options available)
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

        camera.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view){
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, TAKE_PICTURE);
            }
        });
>>>>>>> com

        // 세 번째 Tab. (탭 표시 텍스트:"TAB 3"), (페이지 뷰:"content3")
        TabHost.TabSpec ts3 = tabHost1.newTabSpec("Tab Spec 3");
        ts3.setContent(R.id.content3);
<<<<<<< HEAD
        ts3.setIndicator("Tab 3");
        tabHost1.addTab(ts3);

        tabHost1.setCurrentTab(0);

    }

=======
        ts3.setIndicator("Game");
        tabHost1.addTab(ts3);

        textViewPlayer1 = findViewById(R.id.text_view_p1);
        textViewPlayer2 = findViewById(R.id.text_view_p2);
        textViewTie = findViewById(R.id.text_view_tie);
        timer = findViewById(R.id.text_view_timer);

        for (int i=0; i<10; i++){
            for (int j=0; j<10; j++){
                String buttonID ="button_"+i+j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[i][j] = findViewById(resID);
                buttons[i][j].setOnClickListener(this);
            }
        }
        Button buttonReset = findViewById(R.id.button_reset);
        buttonReset.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                resetGame();
            }
        });
        tabHost1.setCurrentTab(0);
    }
    public void checkPermission(){
        //현재 안드로이드 버전이 6.0미만이면 메서드를 종료한다.
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return;
        List<String> PermissionRqList = new ArrayList<>();
        for(String permission : permission_list){
            //권한 허용 여부를 확인한다.
            int chk = checkCallingOrSelfPermission(permission);

            if(chk == PackageManager.PERMISSION_DENIED){
                //권한 허용을여부를 확인하는 창을 띄운다
                PermissionRqList.add(permission);
                //requestPermissions(permission_list,0);
            }
        }
        if(!PermissionRqList.isEmpty()){
            requestPermissions(PermissionRqList.toArray(new String[PermissionRqList.size()]),0);
        }
        else{
            initial();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==0)
        {
            if(grantResults.length > 0) {
                for (int i = 0; i < grantResults.length; i++) {
                    //허용됐다면
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(getApplicationContext(), "앱권한설정하세요", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
            } else {
                Toast.makeText(getApplicationContext(), "앱권한설정하세요", Toast.LENGTH_LONG).show();
                finish();
            }
            initial();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Gallery.clear();
        try{
            switch(requestCode){
                case PICK_IMAGE_REQUEST:
                    if (resultCode == RESULT_OK && data != null) {
                        if(data.getClipData() != null){
                            int count = data.getClipData().getItemCount();
                            for (int i=0; i<count; i++){
                                Uri uri = data.getClipData().getItemAt(i).getUri();
                                Bitmap bm = Images.Media.getBitmap(getContentResolver(), uri);
                                Gallery.add(bm);
                            }
                        }
                        else if(data.getData() != null){
                            InputStream in = getContentResolver().openInputStream(data.getData());
                            Bitmap img = BitmapFactory.decodeStream(in);
                            in.close();
                            Gallery.add(img);
                        }
                        initGalleryInfo(Gallery);
                    }else{
                        Toast.makeText(MainActivity.this, "사진 선택을 취소하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case TAKE_PICTURE:
                    if (resultCode == RESULT_OK && data.hasExtra("data")) {
                        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                        if (bitmap != null) {
                            Gallery.add(bitmap);
                        }
                        initGalleryInfo(Gallery);
                        //num.setText(String.valueOf(Gallery.size()));
                    }else{
                        Toast.makeText(MainActivity.this, "사진 찍기를 취소하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case ADD_CONTACT:
                    if (resultCode == RESULT_OK ) {
                        Intent contact = getIntent();
                        String str_name = data.getStringExtra("str_name");
                        String str_number = data.getStringExtra("str_number");
                        String str_photo = data.getStringExtra("str_photo");
                        byte[] decodedByteArray = Base64.decode(str_photo, Base64.NO_WRAP);
                        Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
                        Names.add(str_name);
                        Numbers.add(str_number);
                        Photos.add(decodedBitmap);
                        initTab1RecyclerView(Names, Numbers, Photos);
                    }else{
                        Toast.makeText(MainActivity.this, "연락처 추가를 취소하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }catch(Exception e){
            Toast.makeText(this, "Oops! 로딩에 오류가 있습니다.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v){
        countDownTimer.start();
        if (!((Button) v).getText().toString().equals("")){
            return;
        }
        if (player1Turn){
            ((Button) v).setText("O");
        }else{
            ((Button) v).setText("X");
        }
        roundCount++;
        if (checkForWin()){
            if (player1Turn){
                player1Wins();
            }else{
                player2Wins();
            }
        }else if (roundCount == 100){
            draw();
        }else{
            updatePointsText();
            player1Turn = !player1Turn;
            if (player1Turn){
                textViewPlayer1.setTextColor(Color.RED);
                textViewPlayer2.setTextColor(Color.BLACK);
            }else{
                textViewPlayer1.setTextColor(Color.BLACK);
                textViewPlayer2.setTextColor(Color.RED);
            }
        }

    }
    private Boolean checkForWin(){
        String[][] field = new String[10][10];
        for (int i=0; i<10; i++){
            for (int j=0; j<10; j++){
                field[i][j] = buttons[i][j].getText().toString();
            }
        }
        for (int i=0; i<10; i++){
            for (int j=0; j<6; j++){
                if (field[i][j].equals(field[i][j+1]) && field[i][j].equals(field[i][j+2]) && field[i][j].equals(field[i][j+3]) && field[i][j].equals(field[i][j+4]) && !field[i][j].equals("")){
                    return true;
                }
            }
        }
        for (int i=0; i<10; i++){
            for (int j=0; j<6; j++){
                if (field[j][i].equals(field[j+1][i]) && field[j][i].equals(field[j+2][i]) && field[j][i].equals(field[j+3][i]) && field[j][i].equals(field[j+4][i]) && !field[j][i].equals("")){
                    return true;
                }
            }
        }
        for (int i=0; i<6; i++){
            for (int j=0; j<6; j++){
                if (field[i][j].equals(field[i+1][j+1]) && field[i][j].equals(field[i+2][j+2]) && field[i][j].equals(field[i+3][j+3]) && field[i][j].equals(field[i+4][j+4]) && !field[i][j].equals("")){
                    return true;
                }
            }
        }
        for (int i=0; i<6; i++){
            for (int j=4; j<10; j++){
                if (field[i][j].equals(field[i+1][j-1]) && field[i][j].equals(field[i+2][j-2]) && field[i][j].equals(field[i+3][j-3]) && field[i][j].equals(field[i+4][j-4]) && !field[i][j].equals("")){
                    return true;
                }
            }
        }
        return false;
    }
    private void player1Wins(){
        countDownTimer.cancel();
        player1Points++;
        Toast toast = Toast.makeText(this, "Player 1 wins!", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL,0,0);
        toast.show();
        updatePointsText();
        timer.setText("timer: ");
        for (int i=0; i<10; i++){
            for (int j=0; j<10; j++){
                buttons[i][j].setEnabled(false);
            }
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                resetBoard();
                for (int i=0; i<10; i++){
                    for (int j=0; j<10; j++){
                        buttons[i][j].setEnabled(true);
                    }
                }
            }},2000);
    }
    private void player2Wins(){
        countDownTimer.cancel();
        player2Points++;
        Toast toast2 = Toast.makeText(this, "Player 2 wins!", Toast.LENGTH_SHORT);
        toast2.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL,0,0);
        toast2.show();
        updatePointsText();
        timer.setText("timer: ");
        for (int i=0; i<10; i++){
            for (int j=0; j<10; j++){
                buttons[i][j].setEnabled(false);
            }
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                resetBoard();
                for (int i=0; i<10; i++){
                    for (int j=0; j<10; j++){
                        buttons[i][j].setEnabled(true);
                    }
                }
            }},2000);
    }
    private void player1Win(){
        countDownTimer.cancel();
        player1Points++;
        Toast toast = Toast.makeText(this, "Time is up! Player 1 wins", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL,0,0);
        toast.show();
        updatePointsText();
        timer.setText("timer: ");
        for (int i=0; i<10; i++){
            for (int j=0; j<10; j++){
                buttons[i][j].setEnabled(false);
            }
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                resetBoard();
                for (int i=0; i<10; i++){
                    for (int j=0; j<10; j++){
                        buttons[i][j].setEnabled(true);
                    }
                }
            }},2000);
    }
    private void player2Win(){
        countDownTimer.cancel();
        player2Points++;
        Toast toast2 = Toast.makeText(this, "Time is up! Player 2 wins", Toast.LENGTH_SHORT);
        toast2.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL,0,0);
        toast2.show();
        updatePointsText();
        timer.setText("timer: ");
        for (int i=0; i<10; i++){
            for (int j=0; j<10; j++){
                buttons[i][j].setEnabled(false);
            }
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                resetBoard();
                for (int i=0; i<10; i++){
                    for (int j=0; j<10; j++){
                        buttons[i][j].setEnabled(true);
                    }
                }
            }},2000);
    }
    private void draw(){
        tiePoints++;
        Toast.makeText(this, "Again!", Toast.LENGTH_SHORT).show();
        updatePointsText();
        resetBoard();
    }
    private void updatePointsText(){
        textViewPlayer1.setText("Player1: " + player1Points);
        textViewPlayer2.setText("Player2: " + player2Points);
        textViewTie.setText("Tie: " + tiePoints);
    }
    private void resetBoard(){
        for (int i=0 ; i<10 ; i++){
            for (int j=0; j<10; j++){
                buttons[i][j].setText("");
            }
        }
        roundCount=0;
        player1Turn = true;
        textViewPlayer1.setTextColor(Color.RED);
        textViewPlayer2.setTextColor(Color.BLACK);
    }
    private void resetGame(){
        countDownTimer.cancel();
        player1Points = 0;
        player2Points = 0;
        tiePoints = 0;
        timer.setText("timer: ");
        updatePointsText();
        resetBoard();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState);
        outState.putInt("roundCount", roundCount);
        outState.putInt("player1Points", player1Points);
        outState.putInt("player2Points", player2Points);
        outState.putInt("tie2Points", tiePoints);
        outState.putBoolean("player1Turn", player1Turn);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        roundCount=savedInstanceState.getInt("roundCount");
        player1Points=savedInstanceState.getInt("player1Points");
        player2Points=savedInstanceState.getInt("player2Points");
        tiePoints=savedInstanceState.getInt("tiePoints");
        player1Turn=savedInstanceState.getBoolean("player1Turn");
    }
    //////////////////////////////////////
>>>>>>> com
}

