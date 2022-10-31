package com.example.teamproject;

import android.os.Bundle;
import android.content.Intent;

import android.app.Activity;

public class FirstScreen extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            Thread.sleep(2000);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();

    }
}
