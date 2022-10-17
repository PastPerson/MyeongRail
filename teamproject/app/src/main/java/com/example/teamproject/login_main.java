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

        firebase_auth = FirebaseAuth.getInstance();
        database_reference = FirebaseDatabase.getInstance().getReference("login");

        id = findViewById(R.id.user_id);
        pw = findViewById(R.id.user_pwd);

        Button btn_login = findViewById(R.id.login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_id = id.getText().toString();
                String str_pwd = pw.getText().toString();

                firebase_auth.signInWithEmailAndPassword(str_id, str_pwd).addOnCompleteListener(login_main.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            // 로그인 성공!!
                            Intent intent = new Intent(login_main.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else{
                            Toast.makeText(login_main.this, "로그인 실패..", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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