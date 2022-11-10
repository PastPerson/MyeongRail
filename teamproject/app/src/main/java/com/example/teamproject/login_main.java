package com.example.teamproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class login_main extends AppCompatActivity {

    private FirebaseAuth firebase_auth;
    private DatabaseReference database_reference;
    private EditText id, pw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);

        firebase_auth = FirebaseAuth.getInstance(); //파이어베이스 인스터스값
        database_reference = FirebaseDatabase.getInstance().getReference("login");

        id = findViewById(R.id.user_id);
        pw = findViewById(R.id.user_pwd);

        Button btn_login = findViewById(R.id.login);
        btn_login.setOnClickListener(new View.OnClickListener() {//로그인 버튼을 눌렀을때 이벤트
            @Override
            public void onClick(View v) {
                try {
                    String str_id = id.getText().toString();
                    String str_pwd = pw.getText().toString();

                    firebase_auth.signInWithEmailAndPassword(str_id, str_pwd).addOnCompleteListener(login_main.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                if (firebase_auth.getCurrentUser().isEmailVerified()) {//이메일 인증을 진행한 계정만 로그인이 가능함
                                    // 로그인 성공!!
                                    Intent intent = new Intent(login_main.this, MainActivity.class);
                                    intent.putExtra("ID", str_id);
                                    startActivity(intent);
                                    finish();
                                } else {//계정은 존재하나 이메일 인증이 되지 않은 경우
                                    Toast.makeText(login_main.this, "이메일 인증을 해주시기 바랍니다.", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(login_main.this, "로그인 실패..", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }catch(IllegalArgumentException e){
                    Toast.makeText(login_main.this, "아이디와 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button btn_register = findViewById(R.id.register);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login_main.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}