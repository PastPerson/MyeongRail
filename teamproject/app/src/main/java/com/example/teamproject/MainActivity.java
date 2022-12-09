package com.example.teamproject;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;

import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {


    private FirebaseAuth firebase_auth;
    


    private DrawerLayout drawerLayout; //메인 드로어
    private View dr_view; // 로그인 전 드로어뷰
    private TextView login_btn, logout_btn;//드로어뷰의 로그인 버튼 로그아웃버튼
    private TextView profile_id;//id표시
    private TextView profile_box;//id박스 표시
    private FirebaseUser mFirebase_user;// 데이터베이스 유저
    private DatabaseReference mFirebaseDatabase;//데이터 베이스 레퍼런스
    private TextView list_btn; //드로어 뷰의 게시판 버튼
    private Bitmap myBitmap;
    private PhotoView photoview;
    private Station_coordinate st;
    private String start_point=null;
    private String transfer_point=null;
    private String end_point=null;
    private final long finishtimeed = 1000;
    private Long mLastClickTime = 0L;
    private long presstime = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebase_auth = FirebaseAuth.getInstance();
        mFirebase_user= firebase_auth.getCurrentUser();//
        mFirebaseDatabase=FirebaseDatabase.getInstance().getReference("login");//데이터베이스의 path
        profile_box=(TextView) findViewById(R.id.profile_id);
        login_btn= (TextView) findViewById(R.id.menu_login);
        logout_btn = findViewById(R.id.nav_logout);
        list_btn=(TextView)findViewById(R.id.list_go);
        TextView search = findViewById(R.id.search_main);
        Button menu_button=findViewById(R.id.menu_button);
        profile_id=findViewById(R.id.profile_id);
        st=new Station_coordinate();
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);//메인 레이아웃
        drawerLayout.setDrawerListener(listener);//메인 레이아웃
        dr_view = (View) findViewById(R.id.drawerView);//로그인 전 사이드 레이아웃
        profile_id=(TextView)findViewById(R.id.profile_id);
        myBitmap=BitmapFactory.decodeResource(getResources(),R.drawable.subway);
        photoview=findViewById(R.id.photoview);
        photoview.setImageResource(R.drawable.subway);
        Intent get_intent=getIntent();
        try{
            if(start_point==null)
                start_point=get_intent.getStringExtra("start_point");//시작역 받는 변수
            if(transfer_point==null)
                transfer_point=get_intent.getStringExtra("transfer_point");//경유 역 받는 변수
            if(end_point==null)
                end_point=get_intent.getStringExtra("end_point");//도착역 받는 변수
        }catch(NullPointerException e){}

        photoview.setOnPhotoTapListener(new OnPhotoTapListener() {


            @Override
            public void onPhotoTap(ImageView view, float x, float y) {
                int old_x=(int)(8225*x);
                int old_y=(int)(5957*y);
                int station=st.Check_Coor(old_x,old_y);
                Intent istate=getIntent();
                int state=istate.getIntExtra("state",0);
                //데이터 전달
                Log.d("viewTest", "좌표 : "+old_x+", "+old_y);
               // Log.d("viewTest","크기: "+myBitmap.getWidth()+" "+myBitmap.getHeight());
                if(station!=0) {
                    switch (state) {
                        case (0):
                            Intent intent = new Intent(MainActivity.this, PopupActivity.class);
                            intent.putExtra("data", Integer.toString(station));
                            startActivityForResult(intent, 1);
                            break;
                        case (1):
                            intent=new Intent(MainActivity.this,subway_result.class);
                            start_point=Integer.toString(station);
                            if(start_point.equals(transfer_point)) {transfer_point=null;}
                            else if(start_point.equals(end_point)){end_point=null;}
                            intent.putExtra("start_point",start_point);
                            if(transfer_point!=null){intent.putExtra("transfer_point",transfer_point);}
                            if(end_point!=null){intent.putExtra("end_point",end_point);}
                            startActivity(intent);
                            break;
                        case(2):
                            intent=new Intent(MainActivity.this,subway_result.class);
                            transfer_point=Integer.toString(station);
                            if(transfer_point.equals(start_point)) {start_point=null;}
                            else if(transfer_point.equals(end_point)){end_point=null;}
                            intent.putExtra("transfer_point",transfer_point);
                            if(start_point!=null){intent.putExtra("start_point",start_point);}
                            if(end_point!=null){intent.putExtra("end_point",end_point);}
                            startActivity(intent);
                            break;
                        case(3):
                            intent=new Intent(MainActivity.this,subway_result.class);
                            end_point=Integer.toString(station);
                            if(end_point.equals(start_point)) {start_point=null;}
                            else if(end_point.equals(transfer_point)){transfer_point=null;}
                            intent.putExtra("end_point",end_point);
                            intent.putExtra("start_point",start_point);
                          intent.putExtra("transfer_point",transfer_point);
                            startActivity(intent);
                            break;
                    }
                }
            }
        });

        if(firebase_auth.getInstance().getCurrentUser() != null){
            login_btn.setVisibility(View.GONE);
            logout_btn.setVisibility(View.VISIBLE);
            profile_box.setVisibility(View.VISIBLE);

            mFirebaseDatabase.child("UserAccount").child(mFirebase_user.getUid()).child("username").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String value= dataSnapshot.getValue(String.class);
//                    if(value.equals("abc")){
//                        StationDensity d = new StationDensity();
//                        d.RequestReceive();
//                    }
                    System.out.println("abc");
                    System.out.println(value);
                    profile_id.setText(value+"님 환영합니다.");
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {}
            });
            mFirebaseDatabase.child("UserAccount").child(mFirebase_user.getUid()).child("isManager").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if(String.valueOf(snapshot.getValue()).equals("true")){
                        StationDensity d = new StationDensity();
                        d.RequestReceive();

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else {
            login_btn.setVisibility(View.VISIBLE);
            logout_btn.setVisibility(View.GONE);
            profile_box.setVisibility(View.GONE);

        }

        list_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                if(firebase_auth.getInstance().getCurrentUser() != null) {
                    startActivity(new Intent(MainActivity.this, ListActivity.class));
                }else{

                    Toast.makeText(MainActivity.this, "로그인읗하고 이용해주세요", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this,login_main.class));

                }
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MainActivity.this,Search.class);
                intent.putExtra("start_point",start_point);
                intent.putExtra("transfer_point",transfer_point);
                intent.putExtra("end_point",end_point);
                startActivity(intent);

            }});
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                startActivity(new Intent(MainActivity.this,login_main.class));
            }
        });
        logout_btn.setOnClickListener(new View.OnClickListener() {//로그아웃
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                firebase_auth.signOut();
                logout_btn.setVisibility(View.GONE);
                login_btn.setVisibility(View.VISIBLE);
                profile_box.setVisibility(View.GONE);
                profile_id.setText("");
                Toast.makeText(MainActivity.this, "로그아웃됨", Toast.LENGTH_SHORT).show();
            }
        });
        menu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                drawerLayout.openDrawer(dr_view);
            }
        });
    }

    DrawerLayout.DrawerListener listener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
        }

        @Override
        public void onDrawerOpened(@NonNull View drawerView) {
        }

        @Override
        public void onDrawerClosed(@NonNull View drawerView) {
        }

        @Override
        public void onDrawerStateChanged(int newState) {
        }
    };

    @Override
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - presstime;

        if (0 <= intervalTime && finishtimeed >= intervalTime)
        {
            moveTaskToBack(true);						// 태스크를 백그라운드로 이동
            finishAndRemoveTask();						// 액티비티 종료 + 태스크 리스트에서 지우기
            android.os.Process.killProcess(android.os.Process.myPid());
        }
        else
        {
            presstime = tempTime;
            Toast.makeText(getApplicationContext(), "한번더 누르시면 앱이 종료됩니다", Toast.LENGTH_SHORT).show();
        }
    }
}








