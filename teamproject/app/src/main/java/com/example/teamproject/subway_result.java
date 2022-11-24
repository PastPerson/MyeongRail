package com.example.teamproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class subway_result extends AppCompatActivity {

    private Dijkstra sub;

    private String start_point;//시작역 받는 변수
    private String transfer_point;;//경유 역 받는 변수
    private String end_point;;//도착역 받는 변수
    private TextView st_txt; //시작 텍스트
    private TextView tf_txt; //경유 텍스트
    private TextView ed_txt; //
    private TextView st_cir;
    private TextView ed_cir;
    private TextView sub_result;
    private Button st_btn;
    private Button tf_btn;
    private Button ed_btn;
    private Button ch_btn;
    private Spinner h_spin;
    private Spinner m_spin;
    private ArrayAdapter hourAdapter;
    private ArrayAdapter minAdapter;
    private RadioGroup radioGroup;
    private RadioButton default_rad_btn;
    private Intent intent;
    private Button back_btn;
    private TextView search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_subway_result);
            search=findViewById(R.id.search_result);
            sub = new Dijkstra(subway_result.this);
            sub_result = findViewById(R.id.subway_result);
            st_txt = findViewById(R.id.startpoint_txt);//시작역 텍스트
            tf_txt = findViewById(R.id.transferpoint_txt);//경유역 텍스트
            ed_txt = findViewById(R.id.endpoint_txt);//도착역 텍스트
            ch_btn=findViewById(R.id.change_btn);
            st_btn = findViewById(R.id.startpoint_btn);//시작역 추가버튼
            tf_btn = findViewById(R.id.transferpoint_btn);//경유역 추가 버튼
            ed_btn = findViewById(R.id.endpoint_btn);//도착역 추가 버튼
            st_cir = findViewById(R.id.startpoint_circle);//원형 시작역 텍스트
            ed_cir = findViewById(R.id.endpoint_circle);//원형 도착역 텍스트
            intent=getIntent();
            h_spin = findViewById(R.id.spinner_hour);//시 설정
            m_spin = findViewById(R.id.spinner_min);//분 설정
            default_rad_btn = findViewById(R.id.radio_time);
            hourAdapter = ArrayAdapter.createFromResource(this, R.array.hour, android.R.layout.simple_spinner_item);
            hourAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            h_spin.setAdapter(hourAdapter);
            minAdapter = ArrayAdapter.createFromResource(this, R.array.min, android.R.layout.simple_spinner_item);
            minAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            m_spin.setAdapter(minAdapter);
            radioGroup = findViewById(R.id.radio_group);
            back_btn=findViewById(R.id.result_back_button);
        try{

                start_point=intent.getStringExtra("start_point");//시작역 받는 변수

                transfer_point=intent.getStringExtra("transfer_point");//경유 역 받는 변수

                end_point=intent.getStringExtra("end_point");//도착역 받는 변수
        }catch(NullPointerException e){}

        point_setting(start_point, transfer_point, end_point);//존재하는 역에 따라 레이아웃 변화

       back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(subway_result.this,MainActivity.class));//뒤로가기니까 정보 안가지고 감
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendstationtoSearch();
            }
        });
        ch_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp;
                temp=start_point;
                start_point=end_point;
                end_point=temp;
                point_setting(start_point, transfer_point, end_point);
                radioGroup.check(R.id.radio_time);
            }
        });
        st_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               send_station(1);

            }
        });
        tf_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_station(2);
            }
        });
        ed_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_station(3);
            }
        });
        if(default_rad_btn.isChecked()){//기존 라디오버튼이 체크되어있을 때
            try {
                sub.check(start_point, end_point);
                sub_result.setText(Integer.toString(sub.getBTime().time));
            }catch(ArrayIndexOutOfBoundsException e){}
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                try {
                    switch (checkedId) {
                        case R.id.radio_time:
                            sub_result.setText(get_Time());
                        case R.id.radio_distance:
                            sub.check(start_point, end_point);
                            int distance=sub.getKm();
                            if(distance>=1000)
                                sub_result.setText(Integer.toString(sub.getKm()/1000)+"km"+Integer.toString(sub.getKm()%1000)+"m");
                            else
                                sub_result.setText(Integer.toString(sub.getKm())+"m");
                            break;
                        case R.id.radio_cost:
                            sub.check(start_point, end_point);
                            sub_result.setText(Integer.toString(sub.getCharge())+"원");
                            break;
                    }
                } catch (ArrayIndexOutOfBoundsException e){
                    Toast.makeText(subway_result.this, "출발역 또는 도착역이 설정되지 않았습니다", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void sendstationtoSearch(){
        Intent intent=new Intent(subway_result.this,Search.class);
        if(Check_Point(start_point)){intent.putExtra("start_point",start_point);}
        if(Check_Point(transfer_point)){intent.putExtra("transfer_point",transfer_point);}
        if(Check_Point(end_point)){intent.putExtra("end_point",end_point);}

        startActivity(intent);
    }

    public void send_station(int state){
        Intent intent=new Intent(subway_result.this,MainActivity.class);
        if(Check_Point(start_point)){intent.putExtra("start_point",start_point);}
        if(Check_Point(transfer_point)){intent.putExtra("transfer_point",transfer_point);}
        if(Check_Point(end_point)){intent.putExtra("end_point",end_point);}
        intent.putExtra("state",state);
        startActivity(intent);
    }

    public void point_setting(String st_pt, String tf_pt, String ed_pt){
        if(Check_Point(st_pt)){
            st_txt.setText((st_pt));
            st_cir.setText((st_pt));
            st_txt.setVisibility(View.VISIBLE);
            st_btn.setVisibility(View.GONE);
        }else{
            st_btn.setVisibility(View.VISIBLE);
            st_txt.setVisibility(View.GONE);
        }
        if(Check_Point(tf_pt)){
            tf_txt.setText(tf_pt);
            tf_txt.setVisibility(View.VISIBLE);
            tf_btn.setVisibility(View.GONE);

        }else{
            tf_btn.setVisibility(View.VISIBLE);
            tf_txt.setVisibility(View.GONE);

        }
        if(Check_Point(ed_pt)){
            ed_txt.setText(ed_pt);
            ed_cir.setText(ed_pt);
            ed_txt.setVisibility(View.VISIBLE);
            ed_btn.setVisibility(View.GONE);
        }else {
            ed_btn.setVisibility(View.VISIBLE);
            ed_txt.setVisibility(View.GONE);
        }
    }
    public Boolean Check_Point(String point){
        if(point==null)
            return false;
        else
            return true;
    }
    public String get_Time(){
        try {
            StringBuilder sb = new StringBuilder();
            int time;
            int distance;
            int charge;
            if (transfer_point == null)
                sub.check(start_point, end_point);
            else
                sub.check(start_point, transfer_point, end_point);
            time = sub.getBTime().time;
            distance = sub.getBTime().dist;
            charge = sub.getBTime().charge;
            String str;
            sb.append("시간: ");//시간 출력
            if (time >= 3600) {
                sb.append(time / 3600 + "시간 ");
                if (time % 3600 != 0) {
                    sb.append((time % 3600) / 60 + "분 ");
                    if ((time % 3600) % 60 != 0) {
                        sb.append((time % 3600) % 60 + "초");
                    }
                }
            } else if (time >= 60) {
                sb.append(time / 60 + "분 ");
                if (time % 60 != 0)
                    sb.append(time % 60 + "초");
            }
            sb.append("\n거리: ");//거리 출력
            if (distance >= 1000) {
                sb.append(distance / 1000 + "Km ");
                if (distance % 1000 != 0)
                    sb.append(distance % 1000 + "m");
            } else
                sb.append(distance + "m");
            sb.append("\n요금: ");//요금 출력
            sb.append(charge);
            str = sb.toString();
            Log.d("test","테스트: "+str);
            return str;
        }catch (ArrayIndexOutOfBoundsException e){
            Toast.makeText(subway_result.this, "출발역 또는 도착역이 설정되지 않았습니다", Toast.LENGTH_SHORT).show();
        }
        return null;
    }
}