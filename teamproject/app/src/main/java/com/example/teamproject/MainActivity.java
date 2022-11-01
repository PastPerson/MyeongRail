package com.example.teamproject;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Bundle;

import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;

import android.view.MotionEvent;
import android.view.View;

import android.view.inputmethod.EditorInfo;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



public class MainActivity extends AppCompatActivity {

    enum TOUCH_MODE {
        NONE, //터치
        SINGLE, //한손가락
        MULTI //두손가락
    }
    
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
    private TextView login_btn, logout_btn;
    private TextView profile_id;
    private ConstraintLayout profile_box;
    private String E_id;
    private FirebaseUser mFirebase_user;// 데이터베이스 유저
    private DatabaseReference mFirebaseDatabase;//데이터 베이스 레퍼런스
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
        matrix = new Matrix();
        savedMatrix=new Matrix();
        EditText search = findViewById(R.id.search_main);
        Button menu_button=findViewById(R.id.menu_button);
        profile_id=findViewById(R.id.profile_id);
        image_view= findViewById(R.id.image_view);
        image_view.setOnTouchListener(onTouch);
        image_view.setScaleType(ImageView.ScaleType.MATRIX);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);//메인 레이아웃
        drawerLayout.setDrawerListener(listener);//메인 레이아웃
        dr_view = (View) findViewById(R.id.drawerView);//로그인 전 사이드 레이아웃
        profile_id=(TextView)findViewById(R.id.profile_id);

        if(firebase_auth.getInstance().getCurrentUser() != null){
            login_btn.setVisibility(View.GONE);
            logout_btn.setVisibility(View.VISIBLE);
            profile_box.setVisibility(View.VISIBLE);
            Intent getintent=getIntent();
            final String[] profile_name = new String[1];
            mFirebaseDatabase.child("UserAccount").child(mFirebase_user.getUid()).child("emailId").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String value= dataSnapshot.getValue(String.class);
                    profile_id.setText(value);
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
                Dijkstraaa d = new Dijkstraaa(MainActivity.this);
                d.check(0, 1);
                int k = d.getKm();
                int t = d.getAtime();
                int c = d.getCharge();
                String s = k + " km";
                System.out.println(s +" "+ t + "초 " + c + "원");
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
    private View.OnTouchListener onTouch = new View.OnTouchListener() { //터치 이벤트
        @Override
        public boolean onTouch(View v, MotionEvent event) { // 터치 이벤트 함수
            if (v.equals(image_view)) {
                int action = event.getAction();
                switch (action & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN: //한손가락 터치를 했을 때
                        touchMode = TOUCH_MODE.SINGLE;
                        downSingleEvent(event);
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        if (event.getPointerCount() == 2) { // 두손가락 터치를 했을 때
                            touchMode = TOUCH_MODE.MULTI;
                            downMultiEvent(event);
                        }
                        break;
                    case MotionEvent.ACTION_MOVE: //한손가락 드래그를 했을 때
                        if (touchMode == TOUCH_MODE.SINGLE) {

                            moveSingleEvent(event);
                        } else if (touchMode == TOUCH_MODE.MULTI) {//두 손가락 드래그를 했을 때
                            moveMultiEvent(event);
                        }
                        break;
                    case MotionEvent.ACTION_UP:// 한손가락을 떘을때
                    case MotionEvent.ACTION_POINTER_UP://두손가락을 땠을 때
                        touchMode = TOUCH_MODE.NONE;
                        break;
                }
            }
            return true;
        }
    };
    public static int getScreenWidth(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();  // in Activity
        /* getActivity().getWindowManager().getDefaultDisplay() */ // in Fragment
        Point size = new Point();
        display.getRealSize(size); // or getSize(size)
        int width = size.x;
        return width;
    }
    public static int getScreenHeight(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();  // in Activity
        /* getActivity().getWindowManager().getDefaultDisplay() */ // in Fragment
        Point size = new Point();
        display.getRealSize(size); // or getSize(size)
        int height = size.y;
        return height;
    }
    private PointF getMidPoint(MotionEvent e) {//두 손가락 사이의 중간점을 구하는 함수
        float x = (e.getX(0) + e.getX(1)) / 2;
        float y = (e.getY(0) + e.getY(1)) / 2;

        return new PointF(x, y);
    }

    private float getDistance(MotionEvent e) {//두 손가락 사이의 거리를 구하는 함수
        float x = e.getX(0) - e.getX(1);
        float y = e.getY(0) - e.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }
    private void downSingleEvent(MotionEvent event) {//한손가락을 눌렀을 때 시작지점 저장
        savedMatrix.set(matrix);
        startPoint = new PointF(event.getX(), event.getY());
    }
    private void downMultiEvent(MotionEvent event) {//두 손가락으로 눌렀을 때 두 손가락의 중심점과 각도를 구한다.
        oldDistance = getDistance(event);
        if (oldDistance > 5f) {
            savedMatrix.set(matrix);
            midPoint = getMidPoint(event);
            double radian = Math.atan2(event.getY() - midPoint.y, event.getX() - midPoint.x);
            oldDegree = (radian * 180) / Math.PI;
        }
    }
    private void moveSingleEvent(MotionEvent event) { //한손가락 터치
        matrix.set(savedMatrix);
        matrix.postTranslate(event.getX() - startPoint.x, event.getY() - startPoint.y);
        image_view.setImageMatrix(matrix);
    }
    private void moveMultiEvent(MotionEvent event) { //두손가락 드래그
        float newDistance = getDistance(event);
        if (newDistance > 5f) {
            matrix.set(savedMatrix);
            float scale = newDistance / oldDistance;
            matrix.postScale(scale, scale, midPoint.x, midPoint.y);
            image_view.setImageMatrix(matrix);
        }
    }
}








