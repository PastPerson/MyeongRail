package com.example.teamproject;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
    private StationInfo si;
    private String start_point;//시작역 받는 변수
    private String transfer_point;
    ;//경유 역 받는 변수
    private String end_point;
    ;//도착역 받는 변수
    private TextView st_txt; //시작 텍스트
    private TextView tf_txt; //경유 텍스트
    private TextView ed_txt; //끝 텍스트
    private TextView st_cir;//시작 원형 모습
    private TextView ed_cir;
    private TextView sub_result;
    private Button st_btn;
    private Button tf_btn;
    private Button ed_btn;
    private ImageView ch_btn;
    private Button time_btn;
    private ArrayAdapter hourAdapter;
    private ArrayAdapter minAdapter;
    private RadioGroup radioGroup;
    private RadioButton default_rad_btn;
    private Intent intent;
    private ImageView back_btn;
    private TextView search;
    private TextView time;
    private LinearLayout layout;
    private Long now;
    int hour, min; //시간값을 저장할 시간과 분
    private String shour, smin, sec;
    private int allsec;//시간과 분을 초로 저장할 변수
    private Thread tr;
    private Thread ui;
    private ProgressDialog mProgressDialog;
    private int alltime;
    private int af_hour, af_min;
    private String[] st_list;
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
        SimpleDateFormat csec = new SimpleDateFormat("ss");
        sd = new StationDensity();
        si = new StationInfo();
        st_list = si.getStationList();
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
        sec = csec.format(date);
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
        int allhour;
        int allmin;

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
                            int allmin;
                            int allhour;
                            hour = hourOfDay;
                            min = minute;
                            allsec = hour * 3600 + min * 60;
                            type_check();
                            int i = radioGroup.getCheckedRadioButtonId();
                            if (i == R.id.radio_time) {
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

                            } else if (i == R.id.radio_distance) {
                                if (transfer_point == null) {
                                    String s_e = start_point + "-" + end_point;
                                    databaseReference.child(s_e).child("BDist").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                                            Data t = task.getResult().getValue(Data.class);
                                            get_Time(1, t);
                                            set_display(layout, t);
                                        }
                                    });
                                } else {
                                    String s_t = start_point + "-" + transfer_point;
                                    databaseReference.child(s_t).child("BDist").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                                            Data t1 = task.getResult().getValue(Data.class);
                                            String t_e = transfer_point + "-" + end_point;
                                            databaseReference.child(t_e).child("BDist").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                                    Data t2 = task.getResult().getValue(Data.class);
                                                    Data t3 = sub.datasquash(t1, t2, 1);
                                                    List<Integer> b = new ArrayList<>();
                                                    b.addAll(t1.getBetween());
                                                    b.addAll(t2.getBetween());
                                                    t3.setBetween(b);
                                                    get_Time(1, t3);
                                                    set_display(layout, t3);
                                                }
                                            });
                                        }
                                    });
                                }

                            } else if (i == R.id.radio_cost) {
                                if (transfer_point == null) {
                                    String s_e = start_point + "-" + end_point;
                                    databaseReference.child(s_e).child("BCharge").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                                            Data t = task.getResult().getValue(Data.class);
                                            get_Time(2, t);
                                            set_display(layout, t);
                                        }
                                    });
                                } else {
                                    String s_t = start_point + "-" + transfer_point;
                                    databaseReference.child(s_t).child("BCharge").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                                            Data t1 = task.getResult().getValue(Data.class);
                                            String t_e = transfer_point + "-" + end_point;
                                            databaseReference.child(t_e).child("BCharge").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                                    Data t2 = task.getResult().getValue(Data.class);
                                                    Data t3 = sub.datasquash(t1, t2, 0);

                                                    List<Integer> b = new ArrayList<>();
                                                    b.addAll(t1.getBetween());
                                                    b.addAll(t2.getBetween());
                                                    t3.setBetween(b);
                                                    get_Time(2, t3);
                                                    set_display(layout, t3);
                                                }
                                            });
                                        }
                                    });
                                }

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
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
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
                        if (transfer_point == null) {
                            String s_e = start_point + "-" + end_point;
                            databaseReference.child(s_e).child("BDist").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    Data t = task.getResult().getValue(Data.class);
                                    get_Time(1, t);
                                    set_display(layout, t);
                                }
                            });
                        } else {
                            String s_t = start_point + "-" + transfer_point;
                            databaseReference.child(s_t).child("BDist").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    Data t1 = task.getResult().getValue(Data.class);
                                    String t_e = transfer_point + "-" + end_point;
                                    databaseReference.child(t_e).child("BDist").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                                            Data t2 = task.getResult().getValue(Data.class);
                                            Data t3 = sub.datasquash(t1, t2, 1);

                                            List<Integer> b = new ArrayList<>();
                                            b.addAll(t1.getBetween());
                                            b.addAll(t2.getBetween());
                                            t3.setBetween(b);
                                            get_Time(1, t3);
                                            set_display(layout, t3);
                                        }
                                    });
                                }
                            });
                        }

                    } else if (i == R.id.radio_cost) {
                        if (transfer_point == null) {
                            String s_e = start_point + "-" + end_point;
                            databaseReference.child(s_e).child("BCharge").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    Data t = task.getResult().getValue(Data.class);
                                    get_Time(2, t);
                                    set_display(layout, t);
                                }
                            });
                        } else {
                            String s_t = start_point + "-" + transfer_point;
                            databaseReference.child(s_t).child("BCharge").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    Data t1 = task.getResult().getValue(Data.class);
                                    String t_e = transfer_point + "-" + end_point;
                                    databaseReference.child(t_e).child("BCharge").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                                            Data t2 = task.getResult().getValue(Data.class);
                                            Data t3 = sub.datasquash(t1, t2, 0);
                                            List<Integer> b = new ArrayList<>();
                                            b.addAll(t1.getBetween());
                                            b.addAll(t2.getBetween());
                                            t3.setBetween(b);
                                            get_Time(2, t3);
                                            set_display(layout, t3);
                                        }
                                    });
                                }
                            });
                        }

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
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                try {
                    switch (checkedId) {
                        case R.id.radio_time:
                            dist_btn.setEnabled(false);
                            charge_btn.setEnabled(false);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    dist_btn.setEnabled(true);
                                    charge_btn.setEnabled(true);
                                }
                            },2000);
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
//                                get_Time(0);
//                                set_display(layout, sub.getBTime());
                            } else {
                                Toast.makeText(subway_result.this, "출발역 또는 도착역이 설정되지 않았습니다", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case R.id.radio_distance:
                            default_rad_btn.setEnabled(false);
                            charge_btn.setEnabled(false);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    default_rad_btn.setEnabled(true);
                                    charge_btn.setEnabled(true);
                                }
                            },2000);
                            if (start_point != null && end_point != null) {
                                if (transfer_point == null) {
                                    String s_e = start_point + "-" + end_point;
                                    databaseReference.child(s_e).child("BDist").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                                            Data t = task.getResult().getValue(Data.class);
                                            get_Time(1, t);
                                            set_display(layout, t);
                                        }
                                    });
                                } else {
                                    String s_t = start_point + "-" + transfer_point;
                                    databaseReference.child(s_t).child("BDist").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                                            Data t1 = task.getResult().getValue(Data.class);
                                            String t_e = transfer_point + "-" + end_point;
                                            databaseReference.child(t_e).child("BDist").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                                    Data t2 = task.getResult().getValue(Data.class);
                                                    Data t3 = sub.datasquash(t1, t2, 1);
                                                    List<Integer> b = new ArrayList<>();
                                                    b.addAll(t1.getBetween());
                                                    b.addAll(t2.getBetween());
                                                    t3.setBetween(b);
                                                    get_Time(1, t3);
                                                    set_display(layout, t3);
                                                }
                                            });
                                        }
                                    });
                                }

                            } else {
                                Toast.makeText(subway_result.this, "출발역 또는 도착역이 설정되지 않았습니다", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case R.id.radio_cost:
                            default_rad_btn.setEnabled(false);
                            dist_btn.setEnabled(false);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    default_rad_btn.setEnabled(true);
                                    dist_btn.setEnabled(true);
                                }
                            },2000);
                            if (start_point != null && end_point != null) {
                                if (transfer_point == null) {
                                    String s_e = start_point + "-" + end_point;
                                    databaseReference.child(s_e).child("BCharge").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                                            Data t = task.getResult().getValue(Data.class);
                                            get_Time(2, t);
                                            set_display(layout, t);
                                        }
                                    });
                                } else {
                                    String s_t = start_point + "-" + transfer_point;
                                    databaseReference.child(s_t).child("BCharge").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                                            Data t1 = task.getResult().getValue(Data.class);
                                            String t_e = transfer_point + "-" + end_point;
                                            databaseReference.child(t_e).child("BCharge").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                                    Data t2 = task.getResult().getValue(Data.class);
                                                    Data t3 = sub.datasquash(t1, t2, 0);
                                                    List<Integer> b = new ArrayList<>();
                                                    b.addAll(t1.getBetween());
                                                    b.addAll(t2.getBetween());
                                                    t3.setBetween(b);
                                                    get_Time(2, t3);
                                                    set_display(layout, t3);
                                                }
                                            });
                                        }
                                    });
                                }
//
                            } else {
                                Toast.makeText(subway_result.this, "출발역 또는 도착역이 설정되지 않았습니다", Toast.LENGTH_SHORT).show();
                            }
                            break;
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
                    int start=util.time_index(d,allsec/10)[0];
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
                            if(st_min>=60){
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








//        try{
//            tr.join(4000);
//        }catch(InterruptedException e){
//
//   }
public void get_density(Data d, String st, int line, int updown, int index, LinearLayout lf,int now_time){
    DatabaseReference database_ref = FirebaseDatabase.getInstance().getReference("density");
    LayoutInflater layoutInflater = LayoutInflater.from(subway_result.this);
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-DD");
    String date = simpleDateFormat.format(System.currentTimeMillis());


    database_ref.child("station").child(date).child(st+"st").child("timeIndex").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
        @Override
        public void onComplete(@NonNull Task<DataSnapshot> task) {
//                int index = 0;
//                List<String> trans = d.getTrans();
//                for (int i = 0; i < d.getSum_t(); i++) {
//                    if(st.equals(d.getStart())){
//                        break;
//                    }
//                    if(st.equals(trans.get(i))){
//                        index = i + 1;
//                        break;
//                    }
//                }
//                if(st.equals(d.getEnd())){
//                    index = d.getSum_t();
//                }
//                DataUtil util = new DataUtil();
            String l = line + "line";

            String ud;
            if (updown == 0) {
                ud = "uptime";
            } else {
                ud = "downtime";
            }
//                index = util.time_index(d, now_time)[index];
            GenericTypeIndicator<ArrayList<Float>> t = new GenericTypeIndicator<ArrayList<Float>>() {
            };
            ArrayList<Float> fl = task.getResult().child(l).child(ud).getValue(t);
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
        f.removeAllViews();
        String prev_station = " ";
        if (start_point != null) {
            prev_station = start_point;
        } else if (start_point == null && transfer_point != null) {
            prev_station = transfer_point;
        }
//        f.setGravity(Gravity.CENTER);
//        f.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        //환승역 또는 경유역이
        DataUtil util = new DataUtil();
        int[][] line_time = util.getLineTime(D);

        int[] index = util.time_index(D);
        if(D!=null&&D.getSum_t()!=0){
            int line=0;
            for(int i=0;i<D.getSum_t();i++){
                if(prev_station.equals(D.getTrans().get(i))==false) {
                    int num1 = 0;
//                    for (int[] j : si.getStation(prev_station).getLine()) {
//                        int num2 = 0;
//                        for (int[] k : si.getStation(D.getTrans().get(i)).getLine()) {
//                            if (j[0] == k[0] && j[0] != -1) {
//                                int ud = 0;
//                                line = j[0];
//                                if(j[0] == 0){
//                                    if (k[1] - j[1] < 0) {
//                                        if(k[i]-j[i] < -14){
//                                            ud = 0;
//                                        }
//                                        ud = 1;
//                                    } else {
//                                        if(k[1] - j[1] > 18){
//                                            ud = 1;
//                                        }
//                                        ud = 0;
//                                    }
//                                }else if(j[0] == 5){
//                                    if (k[1] - j[1] < 0) {
//                                        if(k[i]-j[i] < -14){
//                                            ud = 0;
//                                        }
//                                        ud = 1;
//                                    } else {
//                                        if(k[1] - j[1] > 18){
//                                            ud = 1;
//                                        }
//                                        ud = 0;
//                                    }
//                                }else{
//                                    if (k[1] - j[1] < 0) {
//                                        ud = 1;
//                                    } else {
//                                        ud = 0;
//                                    }
//                                }
//
//                                    get_density(D, prev_station, line, ud, f,allsec/10);
//                            }
//                        }
//                        num1++;
//                    }
                    get_density(D, prev_station, line_time[i][0], line_time[i][1], index[i], f,allsec/10);
                    prev_station = D.getTrans().get(i);
                }
            }
        }//중간에 환승이 없는 경우
        get_density(D, prev_station, line_time[D.getSum_t()][0], line_time[D.getSum_t()][1], index[D.getSum_t()], f, allsec/10);


        if(end_point!=null&&(end_point.equals(prev_station)==false)){

            get_density(D,end_point,line_time[D.getSum_t()][0],line_time[D.getSum_t()][1], index[D.getSum_t()], f,allsec/10);
        }


    }

}

