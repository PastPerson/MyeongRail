package com.example.teamproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class stationtimetable extends AppCompatActivity {
    private FirebaseUser mFirebase_user;// 데이터베이스 유저
    private DatabaseReference mFirebaseDatabase;//데이터 베이스 레퍼런스
    private DatabaseReference mstation_name;
    private FirebaseAuth firebase_auth;
    private TextView station_name;
    private String name;
    private ListView lst1;
    private TextView first_line;
    private TextView last_line;
    private Button re_btn;
    ArrayList<String> fuptimelist=new ArrayList<>();
    ArrayList<String> fdowntimelist=new ArrayList<>();
    ArrayList<String> luptimelist=new ArrayList<>();
    ArrayList<String> ldowntimelist=new ArrayList<>();
    private LinearLayout lastsheet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stationtimetable);
        Intent intent=getIntent();
        name=intent.getStringExtra("name");
        station_name=findViewById(R.id.station_name);
        station_name.setText(name);
        re_btn=findViewById(R.id.table_backbtn);
        first_line=findViewById(R.id.first_line_name);
        last_line=findViewById(R.id.last_line_name);
        lastsheet=findViewById(R.id.last_line_sheet);
        ListView fdowntime_listView=findViewById(R.id.fdowntime_list);
        ListView fuptime_listView=findViewById(R.id.fuptime_list);
        ListView ldowntime_listView=findViewById(R.id.ldowntime_list);
        ListView luptime_listView=findViewById(R.id.luptime_list);
        LayoutInflater layoutinflater = LayoutInflater.from(stationtimetable.this);
        List<String> fup_data=new ArrayList<>();
        List<String> lup_data=new ArrayList<>();
        List<String> fdw_data=new ArrayList<>();
        List<String> ldw_data=new ArrayList<>();

        ArrayAdapter<String> adapter_fup=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,fup_data);
        ArrayAdapter<String> adapter_fdw=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,fdw_data);
        ArrayAdapter<String> adapter_lup=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,lup_data);
        ArrayAdapter<String> adapter_ldw=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,ldw_data);
        fuptime_listView.setAdapter(adapter_fup);
        fdowntime_listView.setAdapter(adapter_fdw);
        luptime_listView.setAdapter(adapter_lup);
        ldowntime_listView.setAdapter(adapter_ldw);
        mFirebaseDatabase= FirebaseDatabase.getInstance().getReference("time");//데이터베이스의 path
        mstation_name=mFirebaseDatabase.child(name);
        lastsheet.setVisibility(View.GONE);

        re_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(stationtimetable.this,MainActivity.class));
            }
        });

        mstation_name.addListenerForSingleValueEvent((new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int count=0;
                for(DataSnapshot s:snapshot.getChildren()){
                    Log.d("test",String.valueOf(count));
                    Log.d("name",s.getKey().toString());
                    DatabaseReference station_line=mstation_name.child(s.getKey().toString());
                    if(count==0) {
                        first_line.setText(String.valueOf(Integer.parseInt(s.getKey().toString())+1)+"호선");
                        station_line.child("uptime").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                fuptimelist.clear();
                                String str;
                                int time;
                                for (DataSnapshot s : snapshot.getChildren()) {
                                    Log.d("type", s.getValue().toString());
                                    time=Integer.parseInt(s.getValue().toString());
                                    str = String.valueOf(time*10/3600)+" : ";
                                    if((time*10%3600)/60==0){
                                        str+="00";
                                    }else{

                                        str+= String.valueOf((time*10%3600)/60);
                                    }
                                    fup_data.add(str);
                                }
                                fuptime_listView.setAdapter(adapter_fup);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        station_line.child("downtime").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                fdowntimelist.clear();
                                String str;
                                int time;
                                for (DataSnapshot s : snapshot.getChildren()) {
                                    Log.d("type", s.getValue().toString());
                                    time=Integer.parseInt(s.getValue().toString());
                                    str = String.valueOf(time*10/3600)+" : ";
                                    if((time*10%3600)/60==0){
                                        str+="00";
                                    }else{
                                        str+= String.valueOf((time*10%3600)/60);
                                    }
                                    fdw_data.add(str);
                                }
                                fdowntime_listView.setAdapter(adapter_fdw);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }else if(count==1){
                        lastsheet.setVisibility(View.VISIBLE);
                        last_line.setText(String.valueOf(Integer.parseInt(s.getKey().toString())+1)+"호선");
                        station_line.child("uptime").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                luptimelist.clear();
                                String str;
                                int time;
                                for (DataSnapshot s : snapshot.getChildren()) {
                                    Log.d("type", s.getValue().toString());
                                    time=Integer.parseInt(s.getValue().toString());
                                    str = String.valueOf(time*10/3600)+" : ";
                                    if((time*10%3600)/60==0){
                                        str+="00";
                                    }else{

                                        str+= String.valueOf((time*10%3600)/60);
                                    }
                                    lup_data.add(str);
                                }
                                luptime_listView.setAdapter(adapter_lup);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        station_line.child("downtime").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ldowntimelist.clear();
                                String str;
                                int time;
                                for (DataSnapshot s : snapshot.getChildren()) {
                                    Log.d("type", s.getValue().toString());
                                    time=Integer.parseInt(s.getValue().toString());
                                    str = String.valueOf(time*10/3600)+" : ";
                                    if((time*10%3600)/60==0){
                                        str+="00";
                                    }else{
                                        str+= String.valueOf((time*10%3600)/60);
                                    }
                                    ldw_data.add(str);
                                }
                                ldowntime_listView.setAdapter(adapter_ldw);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                    count++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        }));

        adapter_fup.notifyDataSetChanged();
        //역의 이름

    }
}