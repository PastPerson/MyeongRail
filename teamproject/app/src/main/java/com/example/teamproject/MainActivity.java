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

import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;


import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.EventListener;
import java.util.GregorianCalendar;
import java.util.List;


public class MainActivity extends AppCompatActivity {


    private FirebaseAuth firebase_auth;
    


    private DrawerLayout drawerLayout; //메인 드로어
    private View dr_view; // 로그인 전 드로어뷰
    private TextView login_btn, logout_btn;//드로어뷰의 로그인 버튼 로그아웃버튼
    private TextView profile_id;//id표시
    private ConstraintLayout profile_box;//id박스 표시
    private FirebaseUser mFirebase_user;// 데이터베이스 유저
    private DatabaseReference mFirebaseDatabase;//데이터 베이스 레퍼런스
    private TextView list_btn; //드로어 뷰의 게시판 버튼
    private Bitmap myBitmap;
    private PhotoView photoview;
    private Station_coordinate st;

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
        st=new Station_coordinate();
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);//메인 레이아웃
        drawerLayout.setDrawerListener(listener);//메인 레이아웃
        dr_view = (View) findViewById(R.id.drawerView);//로그인 전 사이드 레이아웃
        profile_id=(TextView)findViewById(R.id.profile_id);
        myBitmap=BitmapFactory.decodeResource(getResources(),R.drawable.subway);
        photoview=findViewById(R.id.photoview);
        photoview.setImageResource(R.drawable.subway);



        photoview.setOnPhotoTapListener(new OnPhotoTapListener() {


            @Override
            public void onPhotoTap(ImageView view, float x, float y) {
                int old_x=(int)(myBitmap.getWidth()*x);
                int old_y=(int)(myBitmap.getHeight()*y);
                int station=st.Check_Coor(old_x,old_y);

                Intent istate=getIntent();
                int state=istate.getIntExtra("state",0);
                //데이터 전달
                Log.d("viewTest", "좌표 : "+old_x+", "+old_y);
                Log.d("viewTest","크기: "+myBitmap.getWidth()+" "+myBitmap.getHeight());
                if(station!=0) {
                    switch (state) {
                        case (0):
                            Intent intent = new Intent(MainActivity.this, PopupActivity.class);
                            intent.putExtra("data", Integer.toString(station));
                            startActivityForResult(intent, 1);
                            break;
                        case (1):
                            intent=new Intent(MainActivity.this,subway_result.class);
                            intent.putExtra("start_data",Integer.toString(station));
                            startActivity(intent);
                            break;
                        case(2):
                            intent=new Intent(MainActivity.this,subway_result.class);
                            intent.putExtra("transfer_data",Integer.toString(station));
                            startActivity(intent);
                            break;
                        case(3):
                            intent=new Intent(MainActivity.this,subway_result.class);
                            intent.putExtra("end_data",Integer.toString(station));
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








