package com.example.teamproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class subway_result extends AppCompatActivity {

    private Dijkstra sub;

    private String start_point;//시작역 받는 변수
    private String transfer_point;;//경유 역 받는 변수
    private String end_point;;//도착역 받는 변수
    private TextView st_txt; //시작 텍스트
    private TextView tf_txt; //경유 텍스트
    private TextView ed_txt; //끝 텍스트
    private TextView st_cir;//시작 원형 모습
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
    private LinearLayout layout;
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
            ch_btn=findViewById(R.id.change_btn); //전환버튼
            st_btn = findViewById(R.id.startpoint_btn);//시작역 추가버튼
            tf_btn = findViewById(R.id.transferpoint_btn);//경유역 추가 버튼
            ed_btn = findViewById(R.id.endpoint_btn);//도착역 추가 버튼
            //st_cir = findViewById(R.id.startpoint_circle);//원형 시작역 텍스트
            //ed_cir = findViewById(R.id.endpoint_circle);//원형 도착역 텍스트
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
            layout=findViewById(R.id.result_layoutsheet);

        try{

                start_point=intent.getStringExtra("start_point");//시작역 받는 변수

                transfer_point=intent.getStringExtra("transfer_point");//경유 역 받는 변수

                end_point=intent.getStringExtra("end_point");//도착역 받는 변수
        }catch(NullPointerException e){}

        point_setting(start_point, transfer_point, end_point);//존재하는 역에 따라 레이아웃 변화

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.removeAllViews();
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
                set_display(layout,sub.getBTime());
            }
        });
        //상단 역들을 클릭할 경우 설정할 수있다.
        st_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {send_station(1);}
        });
        st_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {send_station(1);}
        });
        tf_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {send_station(2);}
        });
        tf_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_station(2);
            }
        });
        ed_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {send_station(3);}
        });
        ed_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_station(3);
            }
        });
        if(default_rad_btn.isChecked()){//기존 라디오버튼이 체크되어있을 때
            try {
                get_Time(0);
                set_display(layout,sub.getBTime());
            }catch(ArrayIndexOutOfBoundsException e){}
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                try {
                    switch (checkedId) {
                        case R.id.radio_time:

                            get_Time(0);
                            set_display(layout,sub.getBTime());
                            break;
                        case R.id.radio_distance:

                            get_Time(1);
                            set_display(layout,sub.getBDist());
                            break;
                        case R.id.radio_cost:

                            get_Time(2);
                            set_display(layout,sub.getBCharge());
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
           // st_cir.setText((st_pt));
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
           // ed_cir.setText(ed_pt);
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
    public void get_Time(int state){//시간을 얻을수 잇다..
        try {
            StringBuilder sb = new StringBuilder();
            int time;
            int distance;
            int charge;
            if (transfer_point == null)
                sub.check(start_point, end_point);
            else
                sub.check(start_point, transfer_point, end_point);
            if(state==0) {
                time = sub.getBTime().time;
                distance = sub.getBTime().dist;
                charge = sub.getBTime().charge;
            }else if(state==1){
                time = sub.getBDist().time;
                distance = sub.getBDist().dist;
                charge = sub.getBDist().charge;
            }else{
                time = sub.getBCharge().time;
                distance = sub.getBCharge().dist;
                charge = sub.getBCharge().charge;
            }
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
            sb.append(charge+"원");
            str = sb.toString();
            Log.d("test","테스트: "+str);

            sub_result.setText(str);
        }catch (ArrayIndexOutOfBoundsException e){
            //Toast.makeText(subway_result.this, "출발역 또는 도착역이 설정되지 않았습니다", Toast.LENGTH_SHORT).show();
        }

    }

    //결과에 따라 뷰를 동적 생성해주는 뷰
    public void set_display(LinearLayout f,Data D){
        f.removeAllViews();
        TextView start_view = new TextView(this);
        TextView density_f = new TextView(this);
        TextView density_t;
        TextView end_view = new TextView(this);
        TextView trans_view;
                f.setGravity(Gravity.CENTER);
                f.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                //if(sub.sum_t==0) {

                start_view.setBackgroundResource(R.drawable.shape);//
                f.addView(start_view);
                start_view.setText(start_point);
                start_view.setTextSize(70);

                start_view.getLayoutParams().height = 500;
                start_view.getLayoutParams().width = 500;
                start_view.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                start_view.setGravity(Gravity.CENTER);
                LinearLayout.LayoutParams parms_f=(LinearLayout.LayoutParams) start_view.getLayoutParams();
                parms_f.setMargins(50,0,0,0);
                //환승역 및 경유역 체크

                try {//중간에 환승역이 있는경우
                    Log.d("test", "환승역 개수" + sub.cc.length+", "+sub.sum_t);
                    if((sub.cc[0]==null&&transfer_point!=null)||(transfer_point!=null&&sub.transfer_list[1].equals(sub.cc[0]))||(transfer_point!=null&&sub.transfer_list[0].equals(sub.cc[0]))){//환승역이 없고 경유역이 있는 경우
                        density_t = new TextView(this);
                        density_t.setText("밀집도: 평균");
                        f.addView(density_t);
                        LinearLayout.LayoutParams parms_s = (LinearLayout.LayoutParams) density_t.getLayoutParams();
                        parms_s.setMargins(20, 0, 20, 0);
                        trans_view=new TextView(this);
                        trans_view.setBackgroundResource(R.drawable.shape);
                        f.addView(trans_view);
                        trans_view.setText(transfer_point);
                        trans_view.setTextSize(70);
                        trans_view.getLayoutParams().height = 500;
                        trans_view.getLayoutParams().width = 500;
                        trans_view.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        trans_view.setGravity(Gravity.CENTER);
                    }
                } catch (NullPointerException e) {
                    Log.d("error",e.toString());
                }
                    //Log.d("txt",sub.trans[0]);
                    for (int i = 0; sub.cc[i]!=null; i++) {
                        Log.d("test","경로"+sub.cc[i]);
                        TextView density = new TextView(this);
                        density.setText("밀집도: 평균");
                        f.addView(density);
                        LinearLayout.LayoutParams parms = (LinearLayout.LayoutParams) density.getLayoutParams();
                        parms.setMargins(20, 0, 20, 0);
                        TextView view = new TextView(this);
                        view.setText(sub.cc[i]);
                        view.setBackgroundResource(R.drawable.shape);
                        f.addView(view);
                        view.setTextSize(70);
                        view.getLayoutParams().height = 500;
                        view.getLayoutParams().width = 500;
                        view.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        view.setGravity(Gravity.CENTER);
                        Log.d("txt","전환승역"+sub.transfer_list[0]+"후 환승역"+sub.transfer_list[1]+"해당환승역"+sub.cc[i]+"다음환승역"+sub.cc[i+1]);
                        try {
                            if (transfer_point.equals(sub.cc[i+1])!=true&&((transfer_point != null &&(sub.transfer_list[0].equals(sub.cc[i]))) ||
                                    (transfer_point != null && sub.transfer_list[1].equals(sub.cc[i + 1])))) {//전 환승역과 후 환승역이 같다면 거기에 경유역을 생성한다.
                                Log.d("txt", "전환승역" + sub.transfer_list[0] + "후 환승역" + sub.transfer_list[1]);
                                density_t = new TextView(this);
                                density_t.setText("밀집도: 평균");
                                f.addView(density_t);
                                LinearLayout.LayoutParams parms_s = (LinearLayout.LayoutParams) density_t.getLayoutParams();
                                parms_s.setMargins(20, 0, 20, 0);
                                trans_view=new TextView(this);
                                trans_view.setBackgroundResource(R.drawable.shape);
                                f.addView(trans_view);
                                trans_view.setText(transfer_point);
                                trans_view.setTextSize(70);
                                trans_view.getLayoutParams().height = 500;
                                trans_view.getLayoutParams().width = 500;
                                trans_view.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                trans_view.setGravity(Gravity.CENTER);
                            }
                        }catch (NullPointerException e){
                            Log.d("error",e.toString());
                        }


                    }



                density_f.setText("밀집도: 평균");
                f.addView(density_f);
                LinearLayout.LayoutParams parms_s=(LinearLayout.LayoutParams)density_f.getLayoutParams();
                parms_s.setMargins(20,0,20,0);

                end_view.setBackgroundResource(R.drawable.shape);
                f.addView(end_view);
                end_view.setText(end_point);
                end_view.setTextSize(70);
                end_view.getLayoutParams().height = 500;
                end_view.getLayoutParams().width = 500;
                end_view.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                end_view.setGravity(Gravity.CENTER);
               // }
                //else {
                    /*start_view.setVisibility(View.GONE);
                    density_f.setVisibility(View.GONE);
                    end_view.setVisibility(View.GONE);*/

               // }
            }

    }

