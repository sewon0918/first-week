package com.example.project1;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore.Images;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.material.appbar.AppBarLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    String[] permission_list = {
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    public static String name;
    private String email;
    public String image;
    public Bitmap imageBitmap;
    public static Context context;

    //variables for Tab1
    private ArrayList<String> Names = new ArrayList<>();
    private ArrayList<String> Numbers = new ArrayList<>();
    private ArrayList<Bitmap> Photos = new ArrayList<>();
    private ArrayList<String> PhotosStr = new ArrayList<>();
    private ArrayList<PersonInfo> PersonInfo = new ArrayList<>();
    private ArrayList<Bitmap> Gallery = new ArrayList<>();
    private ArrayList<String> GalleryName = new ArrayList<>();
    private String BoardName = new String();
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

    RetroClient retroClient= RetroClient.getInstance(this).createBaseApi();
    //RetroClient retroClient2= RetroClient.getInstance(this).createBaseApi();


    //FACEBOOK====================================================
    public static CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    public int created=0;
    public int contentcreated = 0;

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
//        else
//            Log.d("PHOTO", "second try failed to load photo");
        return null;
    }

    @Override
    public void onClick(View view) {

    }

    public Bitmap resizingBitmap(Bitmap oBitmap){
        if (oBitmap == null)
            return null;
        float width = oBitmap.getWidth();
        float height = oBitmap.getHeight();
        float resizing_size = 300;
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
        for (int i=0; i<Photos.size(); i++){
            if (Photos.get(i)!=null){
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                Bitmap bitmap = Photos.get(i);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] bytes = stream.toByteArray();
                PhotosStr.add(Base64.encodeToString(bytes, Base64.NO_WRAP));
            }else{
                PhotosStr.add(null);
            }
        }
        for (int i=0; i<Names.size(); i++){
            PersonInfo personinfo = new PersonInfo();
            String namestr = Names.get(i);
            String numstr = Numbers.get(i);
            String photostr = PhotosStr.get(i);
            personinfo.setName(namestr);
            personinfo.setNum(numstr);
            personinfo.setId(name);
            if (photostr != null){
                personinfo.setPhoto(photostr);
            }
            //RetroClient retroClient = RetroClient.getInstance(this).createBaseApi();
            retroClient.addContact(personinfo, new RetroCallback() {
                @Override
                public void onError(Throwable t) {
                    Log.e("error", "initcontacterror");
                }
                @Override
                public void onSuccess(int code, Object receivedData) {
                    initTab1RecyclerView(Names, Numbers, PhotosStr);
                }
                @Override
                public void onFailure(int code) {
                    Log.e("error", "ddddd");
                }
            });
        }
    }
    private void initContactInfoById(String Id) {
        Names.clear();
        Numbers.clear();
        PhotosStr.clear();
        //RetroClient retroClient = RetroClient.getInstance(this).createBaseApi();
        retroClient.getAllContact(Id, new RetroCallback() {
            @Override
            public void onError(Throwable t) {
                Log.e("error", "initcontactbyiderror");
            }
            @Override
            public void onSuccess(int code, Object receivedData) {
                List<PersonInfo> data = (List<PersonInfo>) receivedData;
                for (int i=0; i<data.size(); i++){
                    Names.add(data.get(i).getName());
                    Numbers.add(data.get(i).getNum());
                    PhotosStr.add(data.get(i).getPhoto());
                }
                initTab1RecyclerView(Names, Numbers, PhotosStr);
            }
            @Override
            public void onFailure(int code) {
                Log.e("error", "ddddd");
            }
        });
    }

    private void initTab1RecyclerView(final ArrayList<String> Names, final ArrayList<String> Numbers, final ArrayList<String> PhotosStr) {
        Log.d(TAG, "initTab1RecyclerView: init recyclerView.");
        final RecyclerView recyclerViewtab1 = findViewById(R.id.recycler_view_tab1);
        final RecyclerViewAdapterTab1 adapterTab1 = new RecyclerViewAdapterTab1(Names, Numbers, PhotosStr,this);
        recyclerViewtab1.setAdapter(adapterTab1);
        recyclerViewtab1.setLayoutManager(new LinearLayoutManager(this));
        swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onRightClicked(final int position) {
                //RetroClient retroClient = RetroClient.getInstance(this).createBaseApi();
                String deletename = Names.get(position);
//                Names.remove(position);
//                Numbers.remove(position);
//                PhotosStr.remove(position);
                retroClient.deleteContact(name, deletename, new RetroCallback() {
                    @Override
                    public void onError(Throwable t) {
                        Log.e("error", "deletecintacterror");
                    }
                    @Override
                    public void onSuccess(int code, Object receivedData) {
                        // 아답타에게 알린다
                        adapterTab1.Names.remove(position);
                        adapterTab1.Numbers.remove(position);
                        adapterTab1.PhotosStr.remove(position);
                        adapterTab1.notifyItemRemoved(position);
                        adapterTab1.notifyItemRangeChanged(position, adapterTab1.getItemCount());
                    }
                    @Override
                    public void onFailure(int code) {
                        Log.e("error", "ddddd");
                    }
                });
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

    private void initGalleryInfo(ArrayList<Bitmap> Gallery, ArrayList<String> GalleryName) {
        Log.d(TAG, "initGalleryInfo: preparing gallery info");
        initTab2RecyclerView(Gallery);
    }

    private void initGalleryInfoById(String Id) {
        Gallery.clear();
        GalleryName.clear();
        retroClient.getAllGallery(Id, new RetroCallback() {
            @Override
            public void onError(Throwable t) {
                Log.e("error", "initgallerybyiderror");
            }
            @Override
            public void onSuccess(int code, Object receivedData) {
                List<GalleryInfo> data = (List<GalleryInfo>) receivedData;
                for (int i=0; i<data.size(); i++){
                    String gallerystr = data.get(i).getGallery();
                    byte[] decodedByteArray = Base64.decode(gallerystr, Base64.NO_WRAP);
                    Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
                    Gallery.add(decodedBitmap);
                    GalleryName.add(data.get(i).getName());
                }
                initTab2RecyclerView(Gallery);
            }
            @Override
            public void onFailure(int code) {
                Log.e("error", "ddddd");
            }
        });
    }
    //Context context = this;
    private void initTab2RecyclerView(final ArrayList<Bitmap> Gallery) {
        Log.d(TAG, "initTab2RecyclerView: init recyclerView for tab2.");
        final RecyclerView recyclerViewtab2 = findViewById(R.id.recycler_view_tab2);
        final RecyclerViewAdapterTab2 adapterTab2 = new RecyclerViewAdapterTab2(this, Gallery);
        recyclerViewtab2.setAdapter(adapterTab2);
        recyclerViewtab2.setLayoutManager(new GridLayoutManager(this, 3));

        recyclerViewtab2.addOnItemTouchListener(new RecyclerViewOnItemClickListener(this, recyclerViewtab2,
                new RecyclerViewOnItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, final int position) {
                        String str = "";
                        for (int i=0; i<GalleryName.size();i++){
                            str=str+String.valueOf(GalleryName.get(i));
                        }
                        Log.d(TAG, "click"+ str);
                        if(position >= GalleryName.size())
                            Log.d(TAG, "out of bound");
                        final String removedgallery = GalleryName.get(position);
                        Log.d(TAG, "longclick"+removedgallery);
                        AlertDialog.Builder alt_bld = new AlertDialog.Builder(v.getContext());
                        alt_bld.setMessage("Do you want to delete the photo?").setCancelable(
                                false).setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Log.d(TAG, "deleting : " + String.valueOf(position));

                                        GalleryName.remove(position);
                                        adapterTab2.mData.remove(position);
                                        adapterTab2.notifyItemRemoved(position);
                                        adapterTab2.notifyItemRangeChanged(position, adapterTab2.getItemCount());

                                        Log.d(TAG, "after deleting : " + String.valueOf(position));

                                        retroClient.deleteGallery(name, removedgallery, new RetroCallback() {
                                            @Override
                                            public void onError(Throwable t) {
                                                Log.e("error", "aaaaaaaaaa");
                                            }
                                            @Override
                                            public void onSuccess(int code, Object receivedData) {
//                                                GalleryName.remove(position);
//                                                adapterTab2.mData.remove(position);
//                                                //adapterTab2.mName.remove(position);
//                                                adapterTab2.notifyItemRemoved(position);
//                                                adapterTab2.notifyItemRangeChanged(position, adapterTab2.getItemCount());

                                            }
                                            @Override
                                            public void onFailure(int code) {
                                                Log.e("error", "ddddd");
                                            }
                                        });
                                    }
                                }).setNegativeButton("No",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = alt_bld.create();
                        // Title for AlertDialog
                        alert.setTitle("DELETE");
                        // Icon for AlertDialog
                        alert.show();
                    }

                    @Override
                    public void onItemLongClick(View v, final int position) {
                    }

                })
        );
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        //        //name="";
        context=this;
        checkPermission();

        //FACEBOOK====================================
        callbackManager = CallbackManager.Factory.create();


        // Defining the AccessTokenTracker
        accessTokenTracker = new AccessTokenTracker() {
            // This method is invoked everytime access token changes
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                Log.d(TAG, "onCurrentAccessTokenChanged");
                if(currentAccessToken!= null) {
                    Log.d(TAG, "onCurrentAccessTokenChanged : about to call useinfo");
                }
                //==========================================================
                boolean insideLogin = currentAccessToken != null && !currentAccessToken.isExpired();
//                if (((Login)Login.mContext).afterlogout !=0){
//                    insideLogin = true;
//                }
                if(insideLogin) {
                    //==============================
                    //이 부분이 로그인한 직후 실행됨
                    Log.d(TAG, "just logged in");
                    useLoginInformation(currentAccessToken);
                    initial();
                }
                else {
                    //==============================
                    //이 부분이 로그아웃한 직후 실행됨
                    Log.d(TAG, "just logged out");
                    //Intent intent=new Intent(MainActivity.this,Login.class);
                    //startActivity(intent);
                    setContentView(R.layout.login);
                    //useLoginInformation(currentAccessToken);
                    Names.clear();
                    Numbers.clear();
                    PhotosStr.clear();
                    //setContentView(R.layout.activity_main);
                }
                //==========================================================
            }
        };
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        if(isLoggedIn)
        {
            Log.d(TAG, "true");
            Log.d(TAG, "onCreate, isLoggedin");
            useLoginInformation(accessToken);
            //Log.d(TAG, "onCreate, isLoggedin" + name);
            initial();
        }
        else {
            setContentView(R.layout.login);
            Log.d(TAG, "false");
            Log.d(TAG, "onCreate, not isLoggedin");
            final LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
            loginButton.bringToFront();
            loginButton.setReadPermissions("public_profile");
//            loginButton.setOnClickListener(new Button.OnClickListener(){
//                @Override
//                public void onClick(View v){
//                    LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
//                }
//            });

            //accessTokenTracker.startTracking();
            // Callback registration
            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    Log.d(TAG, "facebook:onSuccess2");
                    AccessToken accessToken = loginResult.getAccessToken();
//                    AccessToken accessToken = AccessToken.getCurrentAccessToken();
                    useLoginInformation(accessToken);
                    Log.d(TAG, "onSuccess2 : " + name);
                    if(created == 0) {
                        initial();
                        created ++;
                    }
                    //created=0;
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


//        AccessToken accessToken = AccessToken.getCurrentAccessToken();
//        if (accessToken != null) {
//            Log.d(TAG, "onStart - about to call info");
//            useLoginInformation(accessToken);
//            Log.d(TAG, "onStart - info, name : " + name);
//        }
    }

    public void onResume() {
        accessTokenTracker.startTracking();
        super.onResume();
//        initial();
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
        accessTokenTracker.stopTracking();
    }

    void useLoginInformation(AccessToken accessToken) {
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
                    TextView profile_email = (TextView) findViewById(R.id.profile_email);
                    TextView profile_email2 = (TextView) findViewById(R.id.profile_email2);
                    name = object.getString("name");
                    if (object.has("email")){
                        email = object.getString("email");
                        profile_email.setText(email);
                        profile_email2.setText(email);
                    }else{
                        profile_email.setText("email을 알 수 없습니다");
                        profile_email2.setText("email을 알 수 없습니다");
                    }
                    TextView profile_name = (TextView) findViewById(R.id.profile_name);
                    profile_name.setText(name);
                    TextView profile_name2 = (TextView) findViewById(R.id.profile_name2);
                    profile_name2.setText(name);
                    image = object.getJSONObject("picture").getJSONObject("data").getString("url");
                    Thread mThread = new Thread(){
                        @Override
                        public void run(){
                            URL url = null;
                            try {
                                url = new URL(image);
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            }
                            HttpURLConnection connection = null;
                            try {
                                connection = (HttpURLConnection) url.openConnection();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            try {
                                connection.connect();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            InputStream input = null;
                            try {
                                input = connection.getInputStream();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            imageBitmap = BitmapFactory.decodeStream(input);

                        }
                    };
                    mThread.start();
                    try{
                        mThread.join();
                        ImageView profile_picture = (ImageView) findViewById(R.id.profile_picture);
                        ImageView profile_picture2 = (ImageView) findViewById(R.id.profile_picture2);
                        profile_picture.setImageBitmap(imageBitmap);
                        profile_picture2.setImageBitmap(imageBitmap);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    initContactInfoById(name);
                    initGalleryInfoById(name);


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d(TAG, "useinfo exception catch");
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
        setContentView(R.layout.activity_main);

        Log.d(TAG, "initial들어감");
        Names.clear();
        Numbers.clear();
        PhotosStr.clear();
        Gallery.clear();
        GalleryName.clear();
       // if (accessToken != null) {
                //Log.d("name (inside initial)", name);
        //        //}
        //useLoginInformation(accessToken);
        TabHost tabHost1 = (TabHost) findViewById(R.id.tabHost1);
        tabHost1.setup();

        // 첫 번째 Tab. (탭 표시 텍스트:"TAB 1"), (페이지 뷰:"content1")
        TabHost.TabSpec ts1 = tabHost1.newTabSpec("Tab Spec 1");
        ts1.setContent(R.id.content1);
        ts1.setIndicator("Contacts");
        tabHost1.addTab(ts1);

        //initContactInfoById(name);
        Button setContact = (Button)findViewById(R.id.button_setContact);
        setContact.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                JSONArray jArray = getContactList();
                initContactInfo(jArray);
            }
        });

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


        //initGalleryInfoById(name);

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

        ImageView img1 = (ImageView) findViewById(R.id.imageView);
        ImageView img2 = (ImageView) findViewById(R.id.imageView2);
//        ImageView phone1 = (ImageView) findViewById(R.id.image_phone);
//        ImageView phone2 = (ImageView) findViewById(R.id.image_phone2);
//        ImageView phone3 = (ImageView) findViewById(R.id.image_phone3);

        TextView single = (TextView) findViewById(R.id.textView4);
//        TextView multi = (TextView) findViewById(R.id.MULTIPLAY);

        img1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(v.getContext(), omokPage.class);
                startActivity(intent);
            }
        });

        img2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(v.getContext(), omokPage2.class);
                startActivity(intent);
            }
        });
        /*

        */
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
                                Bitmap smallbm = resizingBitmap(bm);
                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                smallbm.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                                byte[] bytes = stream.toByteArray();
                                String addedgallery = Base64.encodeToString(bytes, Base64.NO_WRAP);

                                Gallery.add(smallbm);
                                double random = Math.random();
                                GalleryName.add(String.valueOf(random));
                                GalleryInfo galleryinfo = new GalleryInfo();
                                galleryinfo.setGallery(addedgallery);
                                galleryinfo.setId(name);
                                galleryinfo.setName(String.valueOf(random));

                                retroClient.addGallery(galleryinfo, new RetroCallback() {
                                    @Override
                                    public void onError(Throwable t) {
                                        Log.e("error", "aaaaaaaaaa");
                                    }
                                    @Override
                                    public void onSuccess(int code, Object receivedData) {
                                        initGalleryInfo(Gallery, GalleryName);
                                    }
                                    @Override
                                    public void onFailure(int code) {
                                        Log.e("error", "ddddd");
                                    }
                                });
                            }
                        }
                        else if(data.getData() != null){
                            InputStream in = getContentResolver().openInputStream(data.getData());
                            Bitmap img = BitmapFactory.decodeStream(in);

                            Bitmap smallimg = resizingBitmap(img);
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            smallimg.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                            byte[] bytes = stream.toByteArray();
                            String addedgallery = Base64.encodeToString(bytes, Base64.NO_WRAP);
                            Gallery.add(smallimg);
                            double random2 = Math.random();
                            GalleryName.add(String.valueOf(random2));
                            in.close();
                            GalleryInfo galleryinfo = new GalleryInfo();
                            galleryinfo.setGallery(addedgallery);
                            galleryinfo.setId(name);
                            galleryinfo.setName(String.valueOf(random2));
                            retroClient.addGallery(galleryinfo, new RetroCallback() {
                                @Override
                                public void onError(Throwable t) {
                                    Log.e("error", "aaaaaaaaaa");
                                }
                                @Override
                                public void onSuccess(int code, Object receivedData) {
                                    initGalleryInfo(Gallery, GalleryName);
                                }
                                @Override
                                public void onFailure(int code) {
                                    Log.e("error", "ddddd");
                                }
                            });
                        }
                    }else{
                        Toast.makeText(MainActivity.this, "사진 선택을 취소하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case TAKE_PICTURE:
                    if (resultCode == RESULT_OK && data.hasExtra("data")) {
                        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                        //if (bitmap != null) {
                        Gallery.add(bitmap);
                        //}
                        double random3 = Math.random();
                        GalleryName.add(String.valueOf(random3));
                        //Bitmap smallbitmap = resizingBitmap(bitmap);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        byte[] bytes = stream.toByteArray();
                        String addedgallery = Base64.encodeToString(bytes, Base64.NO_WRAP);
                        GalleryInfo galleryinfo = new GalleryInfo();
                        galleryinfo.setGallery(addedgallery);
                        galleryinfo.setId(name);
                        galleryinfo.setName(String.valueOf(random3));
                        retroClient.addGallery(galleryinfo, new RetroCallback() {
                            @Override
                            public void onError(Throwable t) {
                                Log.e("error", "aaaaaaaaaa");
                            }
                            @Override
                            public void onSuccess(int code, Object receivedData) {
                                initGalleryInfo(Gallery, GalleryName);
                            }
                            @Override
                            public void onFailure(int code) {
                                Log.e("error", "ddddd");
                            }
                        });
                        //initGalleryInfo(Gallery);
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
                        PhotosStr.add(str_photo);
                        initTab1RecyclerView(Names, Numbers, PhotosStr);
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


    //////////////////////////////////////
}
