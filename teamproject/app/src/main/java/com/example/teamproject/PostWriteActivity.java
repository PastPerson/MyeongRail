package com.example.teamproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

public class PostWriteActivity extends AppCompatActivity {

    private DatabaseReference database_ref;
    private DatabaseReference my_ref;
    // 사용할 컴포넌트 선언
    EditText title_et, content_et;
    Button reg_button;
    Spinner lines_spn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_post);

// 컴포넌트 초기화
        title_et = findViewById(R.id.title_et);
        content_et = findViewById(R.id.content_et);
        reg_button = findViewById(R.id.reg_button);
        lines_spn = findViewById(R.id.lines_select_spn);

        title_et.setText("");
        content_et.setText("");
        my_ref = FirebaseDatabase.getInstance().getReference("login");
// 버튼 이벤트 추가

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        reg_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 게시물 등록 함수
                String select_lines = lines_spn.getSelectedItem().toString();
                my_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.child("UserAccount").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("isManager").getValue(Boolean.class)){
                            database_ref =  FirebaseDatabase.getInstance().getReference("post0");
                            Post_item myItem = new Post_item();
                            SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
                            SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
                            String userid = snapshot.child("UserAccount").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("username").getValue(String.class).toString();
                            String reg_date = (date.format(new Date()) + "   " + time.format(new Date())).toString();
                            myItem.setTitle(title_et.getText().toString());
                            myItem.setContent(content_et.getText().toString());
                            myItem.setUserid(userid);
                            myItem.setReg_date(reg_date);
                            database_ref.child(reg_date).setValue(myItem);
                            database_ref.child(reg_date).getDatabase().getReference("Comments");
                        }
                        else{
                            if (select_lines.equals("----게시판을 선택하세요----")) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(PostWriteActivity.this);
                                builder.setTitle("").setMessage("게시판을 선택하세요");

                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {
                                    }
                                });
                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();

                            }

                            else if(title_et.getText().toString().isEmpty()){
                                AlertDialog.Builder builder = new AlertDialog.Builder(PostWriteActivity.this);
                                builder.setTitle("").setMessage("제목을 입력하세요");

                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                                    @Override
                                    public void onClick(DialogInterface dialog, int id){}
                                });
                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();
                            }
                            else if(content_et.getText().toString().isEmpty()){
                                AlertDialog.Builder builder = new AlertDialog.Builder(PostWriteActivity.this);
                                builder.setTitle("").setMessage("내용을 입력하세요");

                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                                    @Override
                                    public void onClick(DialogInterface dialog, int id){}
                                });
                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();
                            }
                            else {
                                switch (select_lines) {
                                    case "1호선":
                                        database_ref = FirebaseDatabase.getInstance().getReference("post1");
                                        break;
                                    case "2호선":
                                        database_ref = FirebaseDatabase.getInstance().getReference("post2");
                                        break;
                                    case "3호선":
                                        database_ref = FirebaseDatabase.getInstance().getReference("post3");
                                        break;
                                    case "4호선":
                                        database_ref = FirebaseDatabase.getInstance().getReference("post4");
                                        break;
                                    case "5호선":
                                        database_ref = FirebaseDatabase.getInstance().getReference("post5");
                                        break;
                                    case "6호선":
                                        database_ref = FirebaseDatabase.getInstance().getReference("post6");
                                        break;
                                    case "7호선":
                                        database_ref = FirebaseDatabase.getInstance().getReference("post7");
                                        break;
                                    case "8호선":
                                        database_ref = FirebaseDatabase.getInstance().getReference("post8");
                                        break;
                                    case "9호선":
                                        database_ref = FirebaseDatabase.getInstance().getReference("post9");
                                        break;
                                    default:
                                        break;
                                }

                                Post_item myItem = new Post_item();
                                my_ref.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
                                        SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
                                        String userid = snapshot.child("UserAccount").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("username").getValue(String.class).toString();
                                        String reg_date = (date.format(new Date()) + "   " + time.format(new Date())).toString();
                                        myItem.setTitle(title_et.getText().toString());
                                        myItem.setContent(content_et.getText().toString());
                                        myItem.setUserid(userid);
                                        myItem.setReg_date(reg_date);
                                        database_ref.child(reg_date).setValue(myItem);
                                        database_ref.child(reg_date).getDatabase().getReference("Comments");
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                    }
                                });
                                startActivity(new Intent(PostWriteActivity.this, ListActivity.class));
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
    }
}

