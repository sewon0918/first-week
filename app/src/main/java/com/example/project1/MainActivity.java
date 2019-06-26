package com.example.project1;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
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

    private TableLayout tablayout;
    private AppBarLayout appBarLayout;
    ArrayList<User> userList = new ArrayList<>();
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabHost tabHost1 = (TabHost) findViewById(R.id.tabHost1);
        tabHost1.setup();

        listView =(ListView) findViewById(R.id.Listview);

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
    class User {
        String name;
        String number;
    }




    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK)
        {
            Cursor cursor = getContentResolver().query(data.getData(),
                    new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                            ContactsContract.CommonDataKinds.Phone.NUMBER}, null, null, null);
            cursor.moveToFirst();
            String name = cursor.getString(0);        //0은 이름을 얻어옵니다.
            String number = cursor.getString(1);   //1은 번호를 받아옵니다.
            User user1 = new User();
            user1.name = name;
            user1.number = number;
            userList.add(user1);
            try {
                //JSONArray jArray = new JSONArray();//배열이 필요할때
                for (int i = 0; i < userList.size(); i++) {
                    JSONObject sObject = new JSONObject();//배열 내에 들어갈 json
                    sObject.put("name", userList.get(i).name);
                    sObject.put("number", userList.get(i).number);
                    //jArray.put(sObject);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //simpleAdapter 생성
            SimpleAdapter simpleAdapter = new SimpleAdapter(this,userList,android.R.layout.simple_list_item_2,new String[]{"name","number"},new int[]{android.R.id.text1,android.R.id.text2});
            listView.setAdapter(simpleAdapter);

            cursor.close();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onClick01(View v) {
//        Intent intent = new Intent(Intent.ACTION_PICK);
//        intent.setData(ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
//        startActivityForResult(intent, 0);
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, 0);
        setResult(1);
        onActivityResult(0, 1, intent);

    }
    public void onClick02(View v) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:010-1234-5678"));
        startActivity(intent);
    }

}
