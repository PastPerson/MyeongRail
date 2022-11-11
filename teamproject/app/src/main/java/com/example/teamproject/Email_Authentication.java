package com.example.teamproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Email_Authentication extends AppCompatActivity {
    private Button relogin_btn;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_authentication);
        relogin_btn=findViewById(R.id.back_login);
        intent=new Intent(this,login_main.class);
        relogin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Email_Authentication.this,login_main.class));
            }
        });
    }

}