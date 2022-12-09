package com.example.teamproject;

import android.os.Bundle;
import android.content.Intent;

import android.app.Activity;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class FirstScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_firstscreen);
        try{
            Thread.sleep(3000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();

    }


}
