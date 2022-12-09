package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StationDensity density = new StationDensity();
        density.RequestReceive();
        StationInfo s = new StationInfo();
        String[] st_list = s.getStationList();
        int n = 110;
        for(int i = 0; i < 111; i++){
//            for(int j = 0; j < 10; j++){
//                if(i != n+j){
//                    density.densityRequire(st_list[i], st_list[n+j]);
//                }
//            }
//            if(!st_list[i].equals(st_list[n])){
//                density.densityRequire(st_list[i],st_list[n]);
//            }
//            if(!st_list[i].equals(st_list[n+1])){
//                density.densityRequire(st_list[i],st_list[n+1]);
//            }
//            if(!st_list[i].equals(st_list[n+2])){
//                density.densityRequire(st_list[i],st_list[n+2]);
//            }
        }
//        System.out.println("done");
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("result");
//        databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                int n = 0;
//            }
//        });

        setContentView(R.layout.activity_main);
    }
}