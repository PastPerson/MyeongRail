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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
import com.google.firebase.database.GenericTypeIndicator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class subway_result extends AppCompatActivity {
    private Long mLastClickTime = 0L;
    private Dijkstra sub;
    private StationDensity sd;
    private String start_point;//시작역 받는 변수
    private String transfer_point;
    //경유 역 받는 변수
    private String end_point;
    //도착역 받는 변수
    private TextView st_txt; //시작 텍스트
    private TextView tf_txt; //경유 텍스트
    private TextView ed_txt; //끝 텍스트
    private Button st_btn;
    private Button tf_btn;
    private Button ed_btn;
    private ImageView ch_btn;
    private RadioGroup radioGroup;
    private RadioButton default_rad_btn;
    private Intent intent;
    private ImageView back_btn;
    private LinearLayout layout;
    private Long now;
    int hour, min; //시간값을 저장할 시간과 분
    private String shour, smin;
    private int allsec;//시간과 분을 초로 저장할 변수
    private Thread tr;
    private ProgressDialog mProgressDialog;
    private int alltime;
    private int af_hour, af_min;
    private TextView start_time, end_time;
    private TextView s_time, dis, chag, st_tb;
    private RadioButton dist_btn;
    private RadioButton charge_btn;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("result");

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subway_result);

        now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat chour = new SimpleDateFormat("HH");
        SimpleDateFormat cmin = new SimpleDateFormat("mm");
        sd = new StationDensity();
        sub = new Dijkstra(subway_result.this);

        st_txt = findViewById(R.id.startpoint_txt);//시작역 텍스트
        tf_txt = findViewById(R.id.transferpoint_txt);//경유역 텍스트
        ed_txt = findViewById(R.id.endpoint_txt);//도착역 텍스트
        ch_btn = findViewById(R.id.change_btn); //전환버튼
        st_btn = findViewById(R.id.startpoint_btn);//시작역 추가버튼
        tf_btn = findViewById(R.id.transferpoint_btn);//경유역 추가 버튼
        ed_btn = findViewById(R.id.endpoint_btn);//도착역 추가 버튼

        intent = getIntent();
        start_time = findViewById(R.id.start_time);
        end_time = findViewById(R.id.end_time);
        default_rad_btn = findViewById(R.id.radio_time);
        s_time = findViewById(R.id.spendtime);
        dis = findViewById(R.id.distance);
        chag = findViewById(R.id.charge);
        st_tb = findViewById(R.id.start_timetable);
        dist_btn=findViewById(R.id.radio_distance);
        charge_btn=findViewById(R.id.radio_cost);

        radioGroup = findViewById(R.id.radio_group);
        back_btn = findViewById(R.id.result_back_button);
        layout = findViewById(R.id.result_layoutsheet);
        shour = chour.format(date);
        smin = cmin.format(date);
        allsec = Integer.parseInt(shour) * 3600 + Integer.parseInt(smin) * 60;
        dist_btn.setEnabled(false);
        charge_btn.setEnabled(false);
        ch_btn.setEnabled(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ch_btn.setEnabled(true);
                dist_btn.setEnabled(true);
                charge_btn.setEnabled(true);
            }
        },2000);

        try {
            start_point = intent.getStringExtra("start_point");//시작역 받는 변수

            transfer_point = intent.getStringExtra("transfer_point");//경유 역 받는 변수

            end_point = intent.getStringExtra("end_point");//도착역 받는 변수
        } catch (NullPointerException e) {
        }
        hour = Integer.parseInt(shour);
        min = Integer.parseInt(smin);
        point_setting(start_point, transfer_point, end_point);//존재하는 역에 따라 레이아웃 변화
        start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog dialog = new TimePickerDialog(subway_result.this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (start_point != null && end_point != null) {
                            hour = hourOfDay;
                            min = minute;
                            allsec = hour * 3600 + min * 60;
                            type_check();
                            int i = radioGroup.getCheckedRadioButtonId();
                            if(transfer_point == null) {
                                String s_e = start_point + "-" + end_point;
                                databaseReference.child(s_e).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                        Data b_time = task.getResult().child("BTime").getValue(Data.class);
                                        Data b_dist = task.getResult().child("BDist").getValue(Data.class);
                                        Data b_cost = task.getResult().child("BCharge").getValue(Data.class);

                                        if (i == R.id.radio_time) {
                                            get_Time(0, b_time);
                                            set_display(layout, b_time);
                                        } else if (i == R.id.radio_distance) {
                                            get_Time(1, b_dist);
                                            set_display(layout, b_dist);
                                        } else if (i == R.id.radio_cost) {
                                            get_Time(2, b_cost);
                                            set_display(layout, b_cost);
                                        }
                                    }
                                });
                            }else{
                                String s_t = start_point + "-" + transfer_point;
                                String t_e = transfer_point + "-" + end_point;
                                databaseReference.child(s_t).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                        Data t1 = task.getResult().child("BTime").getValue(Data.class);
                                        Data d1 = task.getResult().child("BDist").getValue(Data.class);
                                        Data c1 = task.getResult().child("BCharge").getValue(Data.class);
                                        databaseReference.child(t_e).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                                Data t2 = task.getResult().child("BTime").getValue(Data.class);
                                                Data d2 = task.getResult().child("BDist").getValue(Data.class);
                                                Data c2 = task.getResult().child("BCharge").getValue(Data.class);
                                                List<Integer> b1 = new ArrayList<>();
                                                List<Integer> b2 = new ArrayList<>();
                                                List<Integer> b3 = new ArrayList<>();
                                                b1.addAll(t1.getBetween());
                                                b1.addAll(t2.getBetween());
                                                b2.addAll(d1.getBetween());
                                                b2.addAll(d2.getBetween());
                                                b3.addAll(c1.getBetween());
                                                b3.addAll(c2.getBetween());
                                                Data b_time = sub.datasquash(t1,t2, 0);
                                                b_time.setBetween(b1);
                                                Data b_dist = sub.datasquash(d1,d2, 1);
                                                b_dist.setBetween(b2);
                                                Data b_cost = sub.datasquash(c1,c2, 2);
                                                b_cost.setBetween(b3);
                                                if (i == R.id.radio_time) {
                                                    get_Time(0, b_time);
                                                    set_display(layout, b_time);
                                                } else if (i == R.id.radio_distance) {
                                                    get_Time(1, b_dist);
                                                    set_display(layout, b_dist);
                                                } else if (i == R.id.radio_cost) {
                                                    get_Time(2, b_cost);
                                                    set_display(layout, b_cost);
                                                }
                                            }
                                        });
                                    }
                                });
                            }
                        }
                    }

                }, Integer.parseInt(shour), Integer.parseInt(smin), false);

                dialog.setTitle("출발시간");
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.show();
            }

        });
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.removeAllViews();
                startActivity(new Intent(subway_result.this, MainActivity.class));//뒤로가기니까 정보 안가지고 감
                finish();
            }
        });


        ch_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public synchronized void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1500) {
                    System.out.println("return");
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                if(start_point!=null&end_point!=null) {
                    String temp;
                    temp = start_point;
                    start_point = end_point;
                    end_point = temp;
                    point_setting(start_point, transfer_point, end_point);
                    //radioGroup.check(R.id.radio_time);
                    type_check();
                    int i = radioGroup.getCheckedRadioButtonId();
                    if(transfer_point == null) {
                        String s_e = start_point + "-" + end_point;
                        databaseReference.child(s_e).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                Data b_time = task.getResult().child("BTime").getValue(Data.class);
                                Data b_dist = task.getResult().child("BDist").getValue(Data.class);
                                Data b_cost = task.getResult().child("BCharge").getValue(Data.class);
                                if (i == R.id.radio_time) {
                                    dist_btn.setEnabled(false);
                                    charge_btn.setEnabled(false);
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            dist_btn.setEnabled(true);
                                            charge_btn.setEnabled(true);
                                        }
                                    },2000);
                                    get_Time(0, b_time);
                                    set_display(layout, b_time);
                                } else if (i == R.id.radio_distance) {
                                    default_rad_btn.setEnabled(false);
                                    charge_btn.setEnabled(false);
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            default_rad_btn.setEnabled(true);
                                            charge_btn.setEnabled(true);
                                        }
                                    },2000);
                                    get_Time(1, b_dist);
                                    set_display(layout, b_dist);
                                } else if (i == R.id.radio_cost) {
                                    default_rad_btn.setEnabled(false);
                                    dist_btn.setEnabled(false);
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            default_rad_btn.setEnabled(true);
                                            dist_btn.setEnabled(true);
                                        }
                                    },2000);
                                    get_Time(2, b_cost);
                                    set_display(layout, b_cost);
                                }
                            }
                        });
                    }else{
                        String s_t = start_point + "-" + transfer_point;
                        String t_e = transfer_point + "-" + end_point;
                        databaseReference.child(s_t).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                Data t1 = task.getResult().child("BTime").getValue(Data.class);
                                Data d1 = task.getResult().child("BDist").getValue(Data.class);
                                Data c1 = task.getResult().child("BCharge").getValue(Data.class);
                                databaseReference.child(t_e).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                        Data t2 = task.getResult().child("BTime").getValue(Data.class);
                                        Data d2 = task.getResult().child("BDist").getValue(Data.class);
                                        Data c2 = task.getResult().child("BCharge").getValue(Data.class);
                                        List<Integer> b1 = new ArrayList<>();
                                        List<Integer> b2 = new ArrayList<>();
                                        List<Integer> b3 = new ArrayList<>();
                                        b1.addAll(t1.getBetween());
                                        b1.addAll(t2.getBetween());
                                        b2.addAll(d1.getBetween());
                                        b2.addAll(d2.getBetween());
                                        b3.addAll(c1.getBetween());
                                        b3.addAll(c2.getBetween());
                                        Data b_time = sub.datasquash(t1,t2, 0);
                                        b_time.setBetween(b1);
                                        Data b_dist = sub.datasquash(d1,d2, 1);
                                        b_dist.setBetween(b2);
                                        Data b_cost = sub.datasquash(c1,c2, 2);
                                        b_cost.setBetween(b3);
                                        if (i == R.id.radio_time) {
                                            dist_btn.setEnabled(false);
                                            charge_btn.setEnabled(false);
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    dist_btn.setEnabled(true);
                                                    charge_btn.setEnabled(true);
                                                }
                                            },2000);
                                            get_Time(0, b_time);
                                            set_display(layout, b_time);
                                        } else if (i == R.id.radio_distance) {
                                            default_rad_btn.setEnabled(false);
                                            charge_btn.setEnabled(false);
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    default_rad_btn.setEnabled(true);
                                                    charge_btn.setEnabled(true);
                                                }
                                            },2000);
                                            get_Time(1, b_dist);
                                            set_display(layout, b_dist);
                                        } else if (i == R.id.radio_cost) {
                                            default_rad_btn.setEnabled(false);
                                            dist_btn.setEnabled(false);
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    default_rad_btn.setEnabled(true);
                                                    dist_btn.setEnabled(true);
                                                }
                                            },2000);
                                            get_Time(2, b_cost);
                                            set_display(layout, b_cost);
                                        }
                                    }
                                });
                            }
                        });
                    }
                }
            }
        });
        //상단 역들을 클릭할 경우 설정할 수있다.
        st_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_station(1);
            }
        });
        st_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_station(1);
            }
        });
        tf_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_station(2);
            }
        });
        tf_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_station(2);
            }
        });
        ed_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_station(3);
            }
        });
        ed_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_station(3);
            }
        });
        if (default_rad_btn.isChecked()) {//기존 라디오버튼이 체크되어있을 때
            type_check();
            try {
                if (start_point != null && end_point != null) {
                    if (transfer_point == null) {
                        String s_e = start_point + "-" + end_point;
                        databaseReference.child(s_e).child("BTime").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                Data t = task.getResult().getValue(Data.class);
                                get_Time(0, t);
                                set_display(layout, t);
                            }
                        });
                    } else {
                        String s_t = start_point + "-" + transfer_point;
                        databaseReference.child(s_t).child("BTime").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                Data t1 = task.getResult().getValue(Data.class);
                                String t_e = transfer_point + "-" + end_point;
                                databaseReference.child(t_e).child("BTime").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                        Data t2 = task.getResult().getValue(Data.class);
                                        Data t3 = sub.datasquash(t1, t2, 0);
                                        List<Integer> b = new ArrayList<>();
                                        b.addAll(t1.getBetween());
                                        b.addAll(t2.getBetween());
                                        t3.setBetween(b);
                                        get_Time(0, t3);
                                        set_display(layout, t3);
                                    }
                                });
                            }
                        });
                    }

                } else {

                }
            } catch (ArrayIndexOutOfBoundsException e) {
            }
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int i) {
                try {
                    if(transfer_point == null) {
                        String s_e = start_point + "-" + end_point;
                        databaseReference.child(s_e).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                Data b_time = task.getResult().child("BTime").getValue(Data.class);
                                Data b_dist = task.getResult().child("BDist").getValue(Data.class);
                                Data b_cost = task.getResult().child("BCharge").getValue(Data.class);
                                if (i == R.id.radio_time) {
                                    dist_btn.setEnabled(false);
                                    charge_btn.setEnabled(false);
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            dist_btn.setEnabled(true);
                                            charge_btn.setEnabled(true);
                                        }
                                    },2000);
                                    get_Time(0, b_time);
                                    set_display(layout, b_time);
                                } else if (i == R.id.radio_distance) {
                                    default_rad_btn.setEnabled(false);
                                    charge_btn.setEnabled(false);
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            default_rad_btn.setEnabled(true);
                                            charge_btn.setEnabled(true);
                                        }
                                    },2000);
                                    get_Time(1, b_dist);
                                    set_display(layout, b_dist);
                                } else if (i == R.id.radio_cost) {
                                    default_rad_btn.setEnabled(false);
                                    dist_btn.setEnabled(false);
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            default_rad_btn.setEnabled(true);
                                            dist_btn.setEnabled(true);
                                        }
                                    },2000);
                                    get_Time(2, b_cost);
                                    set_display(layout, b_cost);
                                }
                            }
                        });
                    }else{
                        String s_t = start_point + "-" + transfer_point;
                        String t_e = transfer_point + "-" + end_point;
                        databaseReference.child(s_t).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                Data t1 = task.getResult().child("BTime").getValue(Data.class);
                                Data d1 = task.getResult().child("BDist").getValue(Data.class);
                                Data c1 = task.getResult().child("BCharge").getValue(Data.class);
                                databaseReference.child(t_e).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                        Data t2 = task.getResult().child("BTime").getValue(Data.class);
                                        Data d2 = task.getResult().child("BDist").getValue(Data.class);
                                        Data c2 = task.getResult().child("BCharge").getValue(Data.class);
                                        List<Integer> b1 = new ArrayList<>();
                                        List<Integer> b2 = new ArrayList<>();
                                        List<Integer> b3 = new ArrayList<>();
                                        b1.addAll(t1.getBetween());
                                        b1.addAll(t2.getBetween());
                                        b2.addAll(d1.getBetween());
                                        b2.addAll(d2.getBetween());
                                        b3.addAll(c1.getBetween());
                                        b3.addAll(c2.getBetween());
                                        Data b_time = sub.datasquash(t1,t2, 0);
                                        b_time.setBetween(b1);
                                        Data b_dist = sub.datasquash(d1,d2, 1);
                                        b_dist.setBetween(b2);
                                        Data b_cost = sub.datasquash(c1,c2, 2);
                                        b_cost.setBetween(b3);
                                        if (i == R.id.radio_time) {
                                            dist_btn.setEnabled(false);
                                            charge_btn.setEnabled(false);
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    dist_btn.setEnabled(true);
                                                    charge_btn.setEnabled(true);
                                                }
                                            },2000);
                                            get_Time(0, b_time);
                                            set_display(layout, b_time);
                                        } else if (i == R.id.radio_distance) {
                                            default_rad_btn.setEnabled(false);
                                            charge_btn.setEnabled(false);
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    default_rad_btn.setEnabled(true);
                                                    charge_btn.setEnabled(true);
                                                }
                                            },2000);
                                            get_Time(1, b_dist);
                                            set_display(layout, b_dist);
                                        } else if (i == R.id.radio_cost) {
                                            default_rad_btn.setEnabled(false);
                                            dist_btn.setEnabled(false);
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    default_rad_btn.setEnabled(true);
                                                    dist_btn.setEnabled(true);
                                                }
                                            },2000);
                                            get_Time(2, b_cost);
                                            set_display(layout, b_cost);
                                        }
                                    }
                                });
                            }
                        });
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    Toast.makeText(subway_result.this, "출발역 또는 도착역이 설정되지 않았습니다", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //뒤로가기를 눌렀을때
    @Override
    public boolean onKeyDown(int keycode, KeyEvent event) {
        if (keycode == KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(subway_result.this, MainActivity.class));
            finish();
            return true;
        }

        return false;
    }

    //검색창으로 출발,경유,도착정보를 보내는 메소드
    public void sendstationtoSearch() {
        Intent intent = new Intent(subway_result.this, Search.class);
        if (Check_Point(start_point)) {
            intent.putExtra("start_point", start_point);
        }
        if (Check_Point(transfer_point)) {
            intent.putExtra("transfer_point", transfer_point);
        }
        if (Check_Point(end_point)) {
            intent.putExtra("end_point", end_point);
        }

        startActivity(intent);
    }

    //메인화면으로 출발,경유,도착정보를 보내는 메소드
    public void send_station(int state) {
        Intent intent = new Intent(subway_result.this, MainActivity.class);
        if (Check_Point(start_point)) {
            intent.putExtra("start_point", start_point);
        }
        if (Check_Point(transfer_point)) {
            intent.putExtra("transfer_point", transfer_point);
        }
        if (Check_Point(end_point)) {
            intent.putExtra("end_point", end_point);
        }
        intent.putExtra("state", state);
        startActivity(intent);
        finish();
    }

    //상단바의 역포인트들을 세팅하는 메소드
    public void point_setting(String st_pt, String tf_pt, String ed_pt) {
        if (Check_Point(st_pt)) {
            st_txt.setText((st_pt));
            // st_cir.setText((st_pt));
            st_txt.setVisibility(View.VISIBLE);
            st_btn.setVisibility(View.GONE);
        } else {
            st_btn.setVisibility(View.VISIBLE);
            st_txt.setVisibility(View.GONE);
        }
        if (Check_Point(tf_pt)) {
            tf_txt.setText(tf_pt);
            tf_txt.setVisibility(View.VISIBLE);
            tf_btn.setVisibility(View.GONE);

        } else {
            tf_btn.setVisibility(View.VISIBLE);
            tf_txt.setVisibility(View.GONE);

        }
        if (Check_Point(ed_pt)) {
            ed_txt.setText(ed_pt);
            // ed_cir.setText(ed_pt);
            ed_txt.setVisibility(View.VISIBLE);
            ed_btn.setVisibility(View.GONE);
        } else {
            ed_btn.setVisibility(View.VISIBLE);
            ed_txt.setVisibility(View.GONE);
        }
    }

    //해당 포인트가 null인지 아닌지 체크해주는 메소드
    public Boolean Check_Point(String point) {
        if (point == null)
            return false;
        else
            return true;
    }

    public void type_check() {
        try {

            if(transfer_point == null){
                sd.densityRequire(start_point, end_point, allsec/10);
            } else{
                sd.densityRequire(start_point, transfer_point, end_point, allsec/10);
            }

        } catch (ArrayIndexOutOfBoundsException e) {
        }
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

    public void get_Time(int state, Data d) {//최소시간,최소환승,최소금액별로 시간,거리,금액값을 얻을수 잇다..
        mProgressDialog = ProgressDialog.show(
                subway_result.this, "로딩 중..",
                "잠시만 기다려주세요..");
        tr = new Thread() {
            public void run() {
                try {
                    DataUtil util = new DataUtil();
                    StringBuilder sb_t = new StringBuilder();
                    StringBuilder sb_d = new StringBuilder();
                    StringBuilder sb_c = new StringBuilder();
                    String str_t, str_d, str_c;
                    alltime = 0;
                    int distance;
                    int charge;
                    int wait=util.wait_time(d, allsec/10)[0];
                    int allhour;
                    int allmin;
                    int test_min;//임시로 먼저 받을 분과 초 분이 60분이 넘는지 체크
                    int test_hour;
                    if (state == 0) {
                        alltime += d.getTime();
                        distance = d.getDist();
                        charge = d.getCharge();

                    } else if (state == 1) {
                        alltime += d.getTime();
                        distance = d.getDist();
                        charge = d.getCharge();
                    } else {

                        alltime += d.getTime();
                        distance = d.getDist();
                        charge = d.getCharge();
                    }


                    if (alltime >= 3600) {
                        sb_t.append(alltime / 3600 + "시간 ");
                        af_hour = alltime / 3600;
                        if (alltime % 3600 != 0) {
                            sb_t.append((alltime % 3600) / 60 + "분 ");
                            af_min = alltime % 3600 / 60;
                            if ((alltime % 3600) % 60 != 0) {
                                sb_t.append((alltime % 3600) % 60 + "초");
                            }
                        }
                    } else if (alltime >= 60) {
                        sb_t.append(alltime / 60 + "분 ");
                        af_min = alltime / 60;
                        if (alltime % 60 != 0)
                            sb_t.append(alltime % 60 + "초");
                    }
                    str_t = sb_t.toString();

                    if (distance >= 1000) {
                        sb_d.append(distance / 1000 + "Km ");
                        if (distance % 1000 != 0)
                            sb_d.append(distance % 1000 + "m");
                    } else
                        sb_d.append(distance + "m");
                    str_d = sb_d.toString();

                    sb_c.append(charge + "원");
                    str_c = sb_c.toString();

                    test_min = min + af_min+wait/6;
                    test_hour = hour + af_hour;
                    if (test_min >= 60) {
                        while(test_min>=60) {
                            test_min -= 60;
                            test_hour += 1;
                        }

                    }
                    if (test_hour >= 24) {
                        test_hour -= 24;


                    }
                    allhour = test_hour;
                    allmin = test_min;

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            s_time.setText(str_t);
                            dis.setText(str_d);
                            chag.setText(str_c);

                            start_time.setText(hour + "시" + min + "분");
                            end_time.setText(allhour + "시 " + allmin + "분");
                            int station_time=wait/6;
                            int st_hour=hour;
                            int st_min=min+station_time;
                            while(st_min>=60){
                                st_min-=60;
                                st_hour+=1;
                            }
                            st_tb.setText(st_hour+"시 "+st_min+"분");

                        }
                    });


                } catch (
                        ArrayIndexOutOfBoundsException e) {

                } catch (
                        NullPointerException e) {
                }
                handler.sendMessage(handler.obtainMessage());
            }


        };
        tr.start();
    }



public void get_density(Data d, String st, int line, int updown, int index, LinearLayout lf){
    DatabaseReference database_ref = FirebaseDatabase.getInstance().getReference("density");
    LayoutInflater layoutInflater = LayoutInflater.from(subway_result.this);
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-DD");
    String date = simpleDateFormat.format(System.currentTimeMillis());
    database_ref.child("station").child(date).child(st+"st").child("timeIndex").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
        @Override
        public void onComplete(@NonNull Task<DataSnapshot> task) {
            String l = line + "line";
            String ud;
            if (updown == 0) {
                ud = "uptime";
            } else {
                ud = "downtime";
            }
            GenericTypeIndicator<ArrayList<Float>> t = new GenericTypeIndicator<ArrayList<Float>>() {
            };
            ArrayList<Float> fl = task.getResult().child(l).child(ud).getValue(t);
            System.out.println("station: "+st);
            System.out.println("index : "+index);
            System.out.println("line : " +line);
            float f = fl.get(index);
            int line1 = line+1;
            if(st.equals(d.getEnd())){
                line1 = 0;
            }
            View customView = layoutInflater.inflate(R.layout.subway_item, null);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            ((TextView) customView.findViewById(R.id.station_item_number)).setText(st);
            ((TextView) customView.findViewById(R.id.station_item_line)).setText(String.valueOf(line1)+"호선");

            ((TextView) customView.findViewById(R.id.density_status)).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#DC143C")));
            if(f>=3){
                ((TextView) customView.findViewById(R.id.density_status)).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#DC143C")));
            }else{
                ((TextView) customView.findViewById(R.id.density_status)).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00FF7F")));

            }
            if(line1==1){
                ((ImageView)customView.findViewById(R.id.station_item_color)).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#09B802")));//1호선색상
                ((TextView) customView.findViewById(R.id.station_item_line)).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#09B802")));//1호선색상
            }else if(line1==2){
                ((ImageView)customView.findViewById(R.id.station_item_color)).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#0200c7")));//2호선색상
                ((TextView) customView.findViewById(R.id.station_item_line)).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#0200c7")));//2호선색상
            }else if(line1==3){
                ((ImageView)customView.findViewById(R.id.station_item_color)).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#803901")));//3호선색상
                ((TextView) customView.findViewById(R.id.station_item_line)).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#803901")));//3호선색상
            }else if(line1==4){
                ((ImageView)customView.findViewById(R.id.station_item_color)).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F20303")));//4호선색상
                ((TextView) customView.findViewById(R.id.station_item_line)).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F20303")));//4호선색상
            }else if(line1==5){
                ((ImageView)customView.findViewById(R.id.station_item_color)).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#7C85FF")));//5호선색상
                ((TextView) customView.findViewById(R.id.station_item_line)).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#7C85FF")));//5호선색상
            }else if(line1==6){
                ((ImageView)customView.findViewById(R.id.station_item_color)).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F2EB11")));//6호선색상
                ((TextView) customView.findViewById(R.id.station_item_line)).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F2EB11")));//6호선색상
            }else if(line1==7){
                ((ImageView)customView.findViewById(R.id.station_item_color)).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CFD11")));//7호선색상;
                ((TextView) customView.findViewById(R.id.station_item_line)).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CFD11")));//7호선색상;
            }else if(line1==8){
                ((ImageView)customView.findViewById(R.id.station_item_color)).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#03DAF2")));//8호선색상
                ((TextView) customView.findViewById(R.id.station_item_line)).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#03DAF2")));//8호선색상
            }else if (line1==9){
                ((ImageView)customView.findViewById(R.id.station_item_color)).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#A206F2")));//9호선색상
                ((TextView) customView.findViewById(R.id.station_item_line)).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#A206F2")));//9호선색상
            }else if(line1==0){
                ((TextView) customView.findViewById(R.id.station_item_line)).setText(" ");
                ((TextView) customView.findViewById(R.id.station_item_line)).setVisibility(View.GONE);

            }
            customView.setLayoutParams(params);


            ((FrameLayout)customView.findViewById(R.id.fram_sheet)).setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            lf.addView(customView);


        }

    });
}


    //결과에 따라 뷰를 동적 생성해주는 뷰
    public void set_display(LinearLayout f, Data D) {
        //환승역 또는 경유역이
        DataUtil util = new DataUtil();
        int[][] line_time = util.getLineTime(D);

        int sum_t = D.getSum_t();
        String start = D.getStart();
        List<String> trans = D.getTrans();
        int[] t = new int[sum_t+1];
        String[] ride_st = new String[sum_t+1];
        ride_st[0] = start;
        for(int i = 1; i < sum_t+1; i++){
            ride_st[i] = trans.get(i-1);
        }
        StationInfo s = new StationInfo();
        List<Integer> b = D.getBetween();
        int[] wait = util.wait_time(D,allsec/10);
        DatabaseReference databaseRefernce = FirebaseDatabase.getInstance().getReference("time");
        databaseRefernce.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                GenericTypeIndicator<ArrayList<Integer>> generic = new GenericTypeIndicator<ArrayList<Integer>>() {};
                f.removeAllViews();
                int now_sec = allsec/10;
                String prev_station = " ";
                if (start_point != null) {
                    prev_station = start_point;
                } else if (start_point == null && transfer_point != null) {
                    prev_station = transfer_point;
                }
                StationInfo info = new StationInfo();
                for(int i = 0; i < sum_t+1; i++){
                    ArrayList<Integer> a;
                    if(line_time[i][1] == 0) {
                        a = task.getResult().child(ride_st[i]).child(String.valueOf(line_time[i][0])).child("uptime").getValue(generic);
                    } else{
                        a = task.getResult().child(ride_st[i]).child(String.valueOf(line_time[i][0])).child("downtime").getValue(generic);
                    }
                    int n = 0;
                    while(a.get(n) < allsec/10){
                        n++;
                        if(n == 36){
                            n = 0;
                            break;
                        }
                    }
                    t[i] = n;
                    now_sec += wait[i] + b.get(i)/10;
                }

                if(D!=null&&D.getSum_t()!=0){
                    int line=0;
                    for(int i=0;i<D.getSum_t();i++){
                        if(prev_station.equals(D.getTrans().get(i))==false) {
                            get_density(D, prev_station, line_time[i][0], line_time[i][1], t[i], f);
                            prev_station = D.getTrans().get(i);
                        }
                    }
                }//중간에 환승이 없는 경우
                get_density(D, prev_station, line_time[D.getSum_t()][0], line_time[D.getSum_t()][1], t[D.getSum_t()], f);

                if(end_point!=null&&(end_point.equals(prev_station)==false)){
                    get_density(D,end_point,line_time[D.getSum_t()][0],line_time[D.getSum_t()][1], t[D.getSum_t()], f);
                }
            }
        });

    }

}

