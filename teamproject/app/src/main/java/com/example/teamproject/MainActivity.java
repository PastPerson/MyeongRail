package com.example.teamproject;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Bundle;

import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;

import android.view.MotionEvent;
import android.view.View;

import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    enum TOUCH_MODE {
        NONE, //터치
        SINGLE, //한손가락
        MULTI //두손가락
    }
    Bitmap bitmap;
    private FirebaseAuth firebase_auth;
    
    private TOUCH_MODE touchMode;
    private Matrix matrix;      //기존 매트릭스
    private Matrix savedMatrix; //작업 후 이미지에 매핑할 매트릭스
    private PointF startPoint;  //한손가락 터치 이동 포인트
    private PointF midPoint;    //두손가락 터치 시 중신 포인트
    private float oldDistance;  //터치 시 두손가락 사이의 거리
    private double oldDegree = 0; // 두손가락의 각도
    private ImageView image_view; //이미지뷰
    private DrawerLayout drawerLayout; //메인 드로어
    private View dr_view; // 로그인 전 드로어뷰
    private TextView login_btn, logout_btn;//드로어뷰의 로그인 버튼 로그아웃버튼
    private TextView profile_id;//id표시
    private ConstraintLayout profile_box;//id박스 표시
    private FirebaseUser mFirebase_user;// 데이터베이스 유저
    private DatabaseReference mFirebaseDatabase;//데이터 베이스 레퍼런스
    private TextView list_btn; //드로어 뷰의 게시판 버튼
    private BitmapFactory.Options getBitmapSize(File imageFile){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        BitmapFactory.decodeFile(imageFile.getAbsolutePath(),options);
        return options;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebase_auth = FirebaseAuth.getInstance();
        mFirebase_user= firebase_auth.getCurrentUser();//
        mFirebaseDatabase=FirebaseDatabase.getInstance().getReference("login");//데이터베이스의 path
        profile_box=(ConstraintLayout) findViewById(R.id.profile);
        login_btn= (TextView) findViewById(R.id.menu_login);
        logout_btn = findViewById(R.id.nav_logout);
        list_btn=(TextView)findViewById(R.id.list_go);
        EditText search = findViewById(R.id.search_main);
        Button menu_button=findViewById(R.id.menu_button);
        profile_id=findViewById(R.id.profile_id);
        //image_view= findViewById(R.id.image_view);
        //image_view.setOnTouchListener(onTouch);
        //image_view.setScaleType(ImageView.ScaleType.MATRIX);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);//메인 레이아웃
        drawerLayout.setDrawerListener(listener);//메인 레이아웃
        dr_view = (View) findViewById(R.id.drawerView);//로그인 전 사이드 레이아웃
        profile_id=(TextView)findViewById(R.id.profile_id);
        /*image_view.setImageBitmap(bitmap);
        matrix = new Matrix();
        savedMatrix=new Matrix();*/
        PhotoView photoview=findViewById(R.id.photoview);
        photoview.setImageResource(R.drawable.subway);


        if(firebase_auth.getInstance().getCurrentUser() != null){
            login_btn.setVisibility(View.GONE);
            logout_btn.setVisibility(View.VISIBLE);
            profile_box.setVisibility(View.VISIBLE);
            mFirebaseDatabase.child("UserAccount").child(mFirebase_user.getUid()).child("username").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String value= dataSnapshot.getValue(String.class);
                    profile_id.setText(value);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {}
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
                startActivity(new Intent(MainActivity.this, ListActivity.class));
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, search_activity_first.class));
            }
        });
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,login_main.class));
            }
        });
        logout_btn.setOnClickListener(new View.OnClickListener() {//로그아웃
            @Override
            public void onClick(View v) {
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
                drawerLayout.openDrawer(dr_view);
            }
        });
        /*image_view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int parentWidth = ((ViewGroup)v.getParent()).getWidth();    // 부모 View 의 Width
                int parentHeight = ((ViewGroup)v.getParent()).getHeight();    // 부모 View 의 Height
                float oldXvalue=0;
                float oldYvalue=0;
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    // 뷰 누름
                     oldXvalue = event.getRawX();
                     oldYvalue = event.getY();
                    Log.d("viewTest", "oldXvalue : "+ oldXvalue + " oldYvalue : " + oldYvalue);    // View 내부에서 터치한 지점의 상대 좌표값.
                    Log.d("viewTest", "v.getX() : "+v.getX());    // View 의 좌측 상단이 되는 지점의 절대 좌표값.
                    Log.d("viewTest", "RawX : " + event.getRawX() +" RawY : " + event.getRawY());    // View 를 터치한 지점의 절대 좌표값.
                    Log.d("viewTest", "v.getHeight : " + v.getHeight() + " v.getWidth : " + v.getWidth());    // View 의 Width, Height

                }else if(event.getAction() == MotionEvent.ACTION_MOVE){
                    // 뷰 이동 중
                    v.setX(v.getX() + (event.getX()) - (v.getWidth()/2));
                    v.setY(v.getY() + (event.getY()) - (v.getHeight()/2));

                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    // 뷰에서 손을 뗌

                    /*if(v.getX() > 0){
                        v.setX(0);
                    }else if((v.getX() + v.getWidth()) < parentWidth){
                        v.setX(parentWidth - v.getWidth());
                    }

                    if(v.getY() > 0){
                        v.setY(0);
                    }else if((v.getY() + v.getHeight()) < parentHeight){
                        v.setY(parentHeight - v.getHeight());
                    }*/

              /*  }
                return true;
            }
        });*/

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

}








