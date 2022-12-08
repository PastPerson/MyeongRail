package com.example.teamproject;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class subway_result extends AppCompatActivity {
    private Long mLastClickTime = 0L;
    private Dijkstra sub;
    private StationDensity sd;
    private StationInfo si;
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
    private Button time_btn;
    private ArrayAdapter hourAdapter;
    private ArrayAdapter minAdapter;
    private RadioGroup radioGroup;
    private RadioButton default_rad_btn;
    private Intent intent;
    private Button back_btn;
    private TextView search;
    private TextView time;
    private LinearLayout layout;
    private Long now;
    int hour,min; //시간값을 저장할 시간과 분
    private String shour,smin,sec;
    private int allsec;//시간과 분을 초로 저장할 변수
    private Thread tr;
    private Thread ui;
    private ProgressDialog mProgressDialog;
    private int alltime;
    private int af_hour,af_min;
    private ResearchRecord rc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_subway_result);
            search=findViewById(R.id.search_result);
            now=System.currentTimeMillis();
            Date date = new Date(now);
            rc=new ResearchRecord();
            SimpleDateFormat chour = new SimpleDateFormat("HH");
            SimpleDateFormat cmin = new SimpleDateFormat("mm");
            SimpleDateFormat csec = new SimpleDateFormat("ss");
            si= new StationInfo();
            sub = new Dijkstra(subway_result.this);
            sub_result = findViewById(R.id.subway_result);
            st_txt = findViewById(R.id.startpoint_txt);//시작역 텍스트
            tf_txt = findViewById(R.id.transferpoint_txt);//경유역 텍스트
            ed_txt = findViewById(R.id.endpoint_txt);//도착역 텍스트
            ch_btn=findViewById(R.id.change_btn); //전환버튼
            st_btn = findViewById(R.id.startpoint_btn);//시작역 추가버튼
            tf_btn = findViewById(R.id.transferpoint_btn);//경유역 추가 버튼
            ed_btn = findViewById(R.id.endpoint_btn);//도착역 추가 버튼
            time_btn=findViewById(R.id.time_btn);
            time=findViewById(R.id.time);
            //st_cir = findViewById(R.id.startpoint_circle);//원형 시작역 텍스트
            //ed_cir = findViewById(R.id.endpoint_circle);//원형 도착역 텍스트
            intent=getIntent();

            default_rad_btn = findViewById(R.id.radio_time);

            radioGroup = findViewById(R.id.radio_group);
            back_btn=findViewById(R.id.result_back_button);
            layout=findViewById(R.id.result_layoutsheet);
            shour=chour.format(date);
            smin=cmin.format(date);
            sec=csec.format(date);
            allsec=Integer.parseInt(shour)*3600+Integer.parseInt(smin)*60+Integer.parseInt(sec);
            int allhour;
            int allmin;
//            Log.d("test","초기 초:"+allsec);
//        Log.d("test",chour.format(date)+" 시 "+cmin.format(date)+" 분 "+csec.format(date)+" 초 ");
        try{

                start_point=intent.getStringExtra("start_point");//시작역 받는 변수

                transfer_point=intent.getStringExtra("transfer_point");//경유 역 받는 변수

                end_point=intent.getStringExtra("end_point");//도착역 받는 변수
        }catch(NullPointerException e){}
        hour=Integer.parseInt(shour);
        min=Integer.parseInt(smin);
        point_setting(start_point, transfer_point, end_point);//존재하는 역에 따라 레이아웃 변화
        time_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog dialog = new TimePickerDialog(subway_result.this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar,new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        int allmin;
                        int allhour;
                        hour = hourOfDay;
                        min = minute;
                        allsec = hour * 3600 + min * 60;
                        type_check();
                        int i=radioGroup.getCheckedRadioButtonId();
                        if(i==R.id.radio_time) {
                            get_Time(0);
                            set_display(layout,sub.getBTime());
                        }
                        else if(i==R.id.radio_distance) {
                            get_Time(1);
                            set_display(layout,sub.getBDist());
                        }
                        else if(i==R.id.radio_cost) {
                            get_Time(2);
                            set_display(layout, sub.getBCharge());
                        }
//                            Log.d("test", "바뀌고난 초:" + allsec);
                    }


                },Integer.parseInt(shour),Integer.parseInt(smin),false);
                dialog.setTitle("출발시간");
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.show();
            }
        });
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
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                String temp;
                temp=start_point;
                start_point=end_point;
                end_point=temp;
                point_setting(start_point, transfer_point, end_point);
                //radioGroup.check(R.id.radio_time);
                type_check();
                int i=radioGroup.getCheckedRadioButtonId();
                if(i==R.id.radio_time) {
                    get_Time(0);
                    set_display(layout,sub.getBTime());
                }
                else if(i==R.id.radio_distance) {
                    get_Time(1);
                    set_display(layout,sub.getBDist());
                }

                else if(i==R.id.radio_cost) {
                    get_Time(2);
                    set_display(layout,sub.getBCharge());
                }

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
            type_check();
            try {
                if(start_point!=null&&end_point!=null) {
                    get_Time(0);
                    set_display(layout, sub.getBTime());
                }else{
                    time.setText("출발시간: "+Integer.parseInt(shour)+"시 "+Integer.parseInt(smin)+"분");
                }
            }catch(ArrayIndexOutOfBoundsException e){}
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                try {
                    switch (checkedId) {
                        case R.id.radio_time:
                            if(start_point!=null&&end_point!=null) {

                                get_Time(0);
                                set_display(layout, sub.getBTime());
                            }else{
                                Toast.makeText(subway_result.this, "출발역 또는 도착역이 설정되지 않았습니다", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case R.id.radio_distance:
                            if(start_point!=null&&end_point!=null) {

                                get_Time(1);
                                set_display(layout, sub.getBDist());
                            }else{
                                Toast.makeText(subway_result.this, "출발역 또는 도착역이 설정되지 않았습니다", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case R.id.radio_cost:
                            if(start_point!=null&&end_point!=null) {

                                get_Time(2);
                                set_display(layout, sub.getBCharge());
                            }else{
                                Toast.makeText(subway_result.this, "출발역 또는 도착역이 설정되지 않았습니다", Toast.LENGTH_SHORT).show();
                            }
                            break;
                    }
                } catch (ArrayIndexOutOfBoundsException e){
                    Toast.makeText(subway_result.this, "출발역 또는 도착역이 설정되지 않았습니다", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    //뒤로가기를 눌렀을때
    @Override
    public boolean onKeyDown(int keycode, KeyEvent event) {
        if(keycode ==KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(subway_result.this,MainActivity.class));
            return true;
        }

        return false;
    }
    //검색창으로 출발,경유,도착정보를 보내는 메소드
    public void sendstationtoSearch(){
        Intent intent=new Intent(subway_result.this,Search.class);
        if(Check_Point(start_point)){intent.putExtra("start_point",start_point);}
        if(Check_Point(transfer_point)){intent.putExtra("transfer_point",transfer_point);}
        if(Check_Point(end_point)){intent.putExtra("end_point",end_point);}

        startActivity(intent);
    }
    //메인화면으로 출발,경유,도착정보를 보내는 메소드
    public void send_station(int state){
        Intent intent=new Intent(subway_result.this,MainActivity.class);
        if(Check_Point(start_point)){intent.putExtra("start_point",start_point);}
        if(Check_Point(transfer_point)){intent.putExtra("transfer_point",transfer_point);}
        if(Check_Point(end_point)){intent.putExtra("end_point",end_point);}
        intent.putExtra("state",state);
        startActivity(intent);
    }
    //상단바의 역포인트들을 세팅하는 메소드
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
    //해당 포인트가 null인지 아닌지 체크해주는 메소드
    public Boolean Check_Point(String point){
        if(point==null)
            return false;
        else
            return true;
    }
    public void type_check(){
        try {
            if (transfer_point == null)
                sub.check(start_point, end_point,(allsec/10));
            else
                sub.check(start_point, transfer_point, end_point,(allsec/10));

            Log.d("allsec: ", "allsec: "+allsec);
        }catch (ArrayIndexOutOfBoundsException e){}
    }
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            mProgressDialog.dismiss();

            boolean retry = true;
            while (retry) {
                try {
                    tr.join();
                    retry = false;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            ;
        }
    };
    public void get_Time(int state){//최소시간,최소환승,최소금액별로 시간,거리,금액값을 얻을수 잇다..
        mProgressDialog = ProgressDialog.show(
                subway_result.this, "로딩 중..",
                "잠시만 기다려주세요..");
        tr=new Thread() {
            public void run() {
                try {
                    StringBuilder sb = new StringBuilder();
                    String str;
                    alltime = 0;
                    int distance;
                    int charge;
                    int allhour;
                    int allmin;
                    int test_min;//임시로 먼저 받을 분과 초 분이 60분이 넘는지 체크
                    int test_hour;
                    if (state == 0) {
//                        for (int i : sub.getBTime().wait_time(allsec / 10)) {
//                            Log.d("test", String.valueOf(i));
//                            time += i;
//                        }
                        alltime +=sub.getBTime().wait_time(allsec/10)[0];
                        alltime += sub.getBTime().getTime();
                        distance = sub.getBTime().getDist();
                        charge = sub.getBTime().getCharge();
                    } else if (state == 1) {
//                        for (int i : sub.getBDist().wait_time(allsec / 10)) {
//                            Log.d("test", String.valueOf(i));
//                            time += i;
//                        }
                        alltime +=sub.getBDist().wait_time(allsec/10)[0];
                        alltime += sub.getBDist().getTime();
                        distance = sub.getBDist().getDist();
                        charge = sub.getBDist().getCharge();
                    } else {
//                        for (int i : sub.getBCharge().wait_time(allsec / 10)) {
//                            time += i;
//                        }
                        alltime +=sub.getBCharge().wait_time(allsec/10)[0];
                        alltime += sub.getBCharge().getTime();
                        distance = sub.getBCharge().getDist();
                        charge = sub.getBCharge().getCharge();
                    }

                    sb.append("시간: ");//시간 출력
                    if (alltime >= 3600) {
                        sb.append(alltime / 3600 + "시간 ");
                        af_hour=alltime/3600;
                        if (alltime % 3600 != 0) {
                            sb.append((alltime % 3600) / 60 + "분 ");
                            af_min=alltime%3600/60;
                            if ((alltime % 3600) % 60 != 0) {
                                sb.append((alltime % 3600) % 60 + "초");
                            }
                        }
                    } else if (alltime >= 60) {
                        sb.append(alltime / 60 + "분 ");
                        af_min=alltime/60;
                        if (alltime % 60 != 0)
                            sb.append(alltime % 60 + "초");
                    }
                    sb.append("\n거리: ");//거리 출력
                    if (distance >= 1000) {
                        sb.append(distance / 1000 + "Km ");
                        if (distance % 1000 != 0)
                            sb.append(distance % 1000 + "m");
                    } else
                        sb.append(distance + "m");
                    sb.append("\n요금: ");//요금 출력
                    sb.append(charge + "원");
                    str = sb.toString();
                    Log.d("test", "테스트: " + str);
                        test_min=min+af_min;
                        test_hour=hour+af_hour;
                        if(test_min>=60){
                            test_min-=60;
                            test_hour+=1;
                        }
                        if(test_hour>=24){
                            test_hour-=24;


                        }
                        allhour = test_hour;
                        allmin = test_min;

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            sub_result.setText(str);

                            time.setText("출발시간: " + hour + "시 " + min + "분\n"+"도착 시간: "+allhour+"시 "+allmin+"분");
                        }
                    });


                } catch (
                        ArrayIndexOutOfBoundsException e) {
                    //Toast.makeText(subway_result.this, "출발역 또는 도착역이 설정되지 않았습니다", Toast.LENGTH_SHORT).show();
                } catch (
                        NullPointerException e) {
                }
                handler.sendMessage(handler.obtainMessage());
            }

        };

        tr.start();



//        try{
//            tr.join(4000);
//        }catch(InterruptedException e){
//
//        }

    }
    public void get_density(Data d, String st, TextView tv){
        tr= new Thread(new Runnable() {
            @Override
            public void run() {

            }
        });
        DatabaseReference database_ref = FirebaseDatabase.getInstance().getReference("density");
        int[] path = d.getPath();
        StationInfo s = new StationInfo();
        int index = 0;
        for(int i = d.getPath_cnt(); i >= 0; i--){
            if(s.getStationList()[path[i]].equals(st)){
                break;
            }
            if(d.getTrans().equals(s.getStationList()[path[i]])){
                index++;
            }
        }
        int[][] lt = d.getLineTime();
        String l = lt[index][0] + "line";
        String ud;
        if(lt[index][1] == 0){
            ud = "uptime";
        }else{
            ud = "downtime";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-DD");
        String date = simpleDateFormat.format(System.currentTimeMillis());
        database_ref.child("station").child(date).child(st).child("timeIndex").child(l).child(ud).child(String.valueOf(index)).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                Float f = task.getResult().getValue(Float.class);
                if(f>=3){
                    tv.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#DC143C")));
                }else{
                    tv.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00FF7F")));
                }
                System.out.println("ff: "+ f);
            }
        });


    }



    //결과에 따라 뷰를 동적 생성해주는 뷰
    public void set_display(LinearLayout f,Data D){
        f.removeAllViews();
        //시작역설정


        TextView start_view = new TextView(this);
        TextView density_f = new TextView(this);
        TextView end_view = new TextView(this);
        f.setGravity(Gravity.CENTER);
        f.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
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

//        Log.d("txt",start_point+transfer_point+end_point);
        String prev_station=" ";
        if(start_point!=null) {
             prev_station = start_point;
            get_density(D,start_point,start_view);
//            if (Integer.parseInt(start_point) % 2 == 0) {
//                start_view.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#DC143C")));
//
//                start_view.setTextColor(Color.parseColor("#FFFAFA"));
//            } else {
//                start_view.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00FF7F")));
//
//                start_view.setTextColor(Color.parseColor("#F0FFF0"));
//            }
             if(end_point==null){
                 end_view.setVisibility(View.GONE);
                 start_view.setVisibility(View.GONE);
             }
        }else if(transfer_point!=null){
            start_view.setVisibility(View.GONE);
            end_view.setVisibility(View.GONE);
            prev_station=transfer_point;
        }else if(start_point==null&&end_point!=null){
            start_view.setVisibility(View.GONE);
            end_view.setVisibility(View.VISIBLE);
        }

//        Log.d("text",si.getStation("122").getLine()[0][1]+" "+si.getStation("122").getLine()[0][1]+" "+si.getStation("122").getLine()[0][1]);
        //환승역 및 경유역 체크
        if(D!=null&&D.getSum_t()!=0) {
            for (int i = 0; i< D.getSum_t(); i++) {
//                Log.d("test", "경로" + D.getTrans()[i]);
                TextView density = new TextView(this);
                int num1=0;
                for(int[] j:si.getStation(prev_station).getLine()){
                    int num2=0;

                    for(int[] k:si.getStation(D.getTrans()[i]).getLine()){
//                        Log.d("test", "전역: " + j[0]+"다음역: "+k[0]);
                        if(j[0]==k[0]&&j[0]!=-1){
                            density.setText(String.valueOf(j[0]+1)+"호선");
                        }
//                        Log.d("test","전역의이름:"+prev_station+" 이번역의 이름: "+D.getTrans()[i]);

                    }

                    num1++;
                }

                prev_station=D.getTrans()[i];
                density.setTextSize(30);
                f.addView(density);
                LinearLayout.LayoutParams parms = (LinearLayout.LayoutParams) density.getLayoutParams();
                parms.setMargins(20, 0, 20, 0);
                TextView view = new TextView(this);
                get_density(D,D.getTrans()[i],view);
                view.setText(D.getTrans()[i]);
                view.setBackgroundResource(R.drawable.shape);
                f.addView(view);
//                if(Integer.parseInt(D.getTrans()[i])%2==0){
//                    view.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#DC143C")));
//
//                    view.setTextColor(Color.parseColor("#FFFAFA"));
//                }
//                else{
//                    view.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00FF7F")));
//
//                    view.setTextColor(Color.parseColor("#F0FFF0"));
//                }

                view.setTextSize(70);
                view.getLayoutParams().height = 500;
                view.getLayoutParams().width = 500;
                view.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                view.setGravity(Gravity.CENTER);

               // Log.d("txt", "전환승역" + D.getTrans()[i] + "후 환승역" + D.getTrans()[i+1] + "해당환승역" + sub.cc[i] + "다음환승역" + D.getTrans()[i+1]);
            }
        }
        //도착역 설정
        int num1=0;
        for(int[] j:si.getStation(prev_station).getLine()){
            int num2=0;
            if(end_point!=null) {
                for (int[] k : si.getStation(end_point).getLine()) {
//                    Log.d("test", "전역: " + j[num1] + "다음역: " + k[num2]);
                    if (j[0] == k[0] && j[0] != -1) {
                        density_f.setText(String.valueOf(j[0] + 1) + "호선");
                    }

                }
            }
            num1++;
        }
        density_f.setTextSize(30);
        f.addView(density_f);
        LinearLayout.LayoutParams parms_s=(LinearLayout.LayoutParams)density_f.getLayoutParams();
        parms_s.setMargins(20,0,20,0);
        //end_view=new TextView(this);
        end_view.setBackgroundResource(R.drawable.shape);
        if(end_point!=null){
            get_density(D,end_point,end_view);
        }
        f.addView(end_view);
        end_view.setText(end_point);
        end_view.setTextSize(70);
        end_view.getLayoutParams().height = 500;
        end_view.getLayoutParams().width = 500;
        end_view.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        end_view.setGravity(Gravity.CENTER);
    }

    }

