package com.example.project1;

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

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.material.appbar.AppBarLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
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


    //FACEBOOK====================================================
    public static CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private int created;

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
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission();

        //FACEBOOK====================================
        callbackManager = CallbackManager.Factory.create();

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();


        if(isLoggedIn) {
            Log.d(TAG, "true");
        }
        else {
            Log.d(TAG, "false");
        }

        if(isLoggedIn)
        {
            initial();
        }
        else {
            final LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
            loginButton.bringToFront();
            loginButton.setReadPermissions(Arrays.asList(
                    "public_profile", "email"));

            // Defining the AccessTokenTracker
            accessTokenTracker = new AccessTokenTracker() {
                // This method is invoked everytime access token changes
                @Override
                protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

                    if(currentAccessToken!= null) {
                        Log.d(TAG, "onCurrentAccessTokenChanged");
                        useLoginInformation(currentAccessToken);
                    }

                }
            };


            LoginManager.getInstance().registerCallback(callbackManager,
                    new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {
                            // App code,,
                            Log.d(TAG, "facebook:onSuccess1");

                            AccessToken accessToken = loginResult.getAccessToken();
                            if (accessToken != null) {
                                useLoginInformation(accessToken);
                            }
                            if(created == 0) {
                                initial();
                                created ++;
                            }
                        }

                        @Override
                        public void onCancel() {
                            // App code
                            Log.d(TAG, "facebook:onCancel1");
                        }

                        @Override
                        public void onError(FacebookException exception) {
                            // App code
                        }
                    });

            // Callback registration
            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    Log.d(TAG, "facebook:onSuccess2");
                    AccessToken accessToken = loginResult.getAccessToken();
                    if(created == 0) {
                        initial();
                        created ++;
                    }
                }

                @Override
                public void onCancel() {
                    // App code
                    Log.d(TAG, "facebook:onCancel2");
                }

                @Override
                public void onError(FacebookException exception) {
                    // App code
                }
            });
        }
    }

    public void onStart() {
        Log.d(TAG, "onStart");
        super.onStart();

//        accessTokenTracker.startTracking();
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null) {
            useLoginInformation(accessToken);
        }
    }

    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    public void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart");
    }

    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
//        accessTokenTracker.stopTracking();

    }

    private void useLoginInformation(AccessToken accessToken) {
        /**
         Creating the GraphRequest to fetch user details
         1st Param - AccessToken
         2nd Param - Callback (which will be invoked once the request is successful)
         **/
        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            //OnCompleted is invoked once the GraphRequest is successful
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    String name = object.getString("name");
                    String email = object.getString("email");
                    String image = object.getJSONObject("picture").getJSONObject("data").getString("url");
//                    displayName.setText(name);
//                    emailID.setText(email);
                    Log.d("name: ", name);
                    Log.d("email: ", email);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        // We set parameters to the GraphRequest using a Bundle.
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,picture.width(200)");
        request.setParameters(parameters);
        // Initiate the GraphRequest
        request.executeAsync();
    }

    public void initial(){
        TabHost tabHost1 = (TabHost) findViewById(R.id.tabHost1);
        tabHost1.setup();

        // 첫 번째 Tab. (탭 표시 텍스트:"TAB 1"), (페이지 뷰:"content1")
        TabHost.TabSpec ts1 = tabHost1.newTabSpec("Tab Spec 1");
        ts1.setContent(R.id.content1);
        ts1.setIndicator("Contacts");
        tabHost1.addTab(ts1);

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

        // 두 번째 Tab. (탭 표시 텍스트:"TAB 2"), (페이지 뷰:"content2")
        TabHost.TabSpec ts2 = tabHost1.newTabSpec("Tab Spec 2");
        ts2.setContent(R.id.content2);
        ts2.setIndicator("Gallery");
        tabHost1.addTab(ts2);
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

        // 세 번째 Tab. (탭 표시 텍스트:"TAB 3"), (페이지 뷰:"content3")
        TabHost.TabSpec ts3 = tabHost1.newTabSpec("Tab Spec 3");
        ts3.setContent(R.id.content3);
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
//            initial();
//            FACEBOOK==========================

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
//            initial();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        //FACEBOOK============================================
        callbackManager.onActivityResult(requestCode, resultCode, data);


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
}
