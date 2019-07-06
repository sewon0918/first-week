package com.example.project1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;

public class Login extends AppCompatActivity {
    public static Context mContext;
    //public static CallbackManager callbackManager;
    private String name = ((MainActivity)MainActivity.context).name;
    private String image = ((MainActivity)MainActivity.context).image;
    private int created = ((MainActivity)MainActivity.context).created;
    public int afterlogout=0;
    private CallbackManager callbackManager = ((MainActivity)MainActivity.context).callbackManager;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        mContext = this;

        final LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.bringToFront();
        loginButton.setReadPermissions("public_profile");
        loginButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, MainActivity.class);
                //afterlogout++;
                startActivity(intent);
            }
        });
    }
}
