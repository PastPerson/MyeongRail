package com.example.teamproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth firebase_auth;
    private DatabaseReference database_ref;
<<<<<<< Updated upstream
    private EditText email, pwd;
=======
    private EditText email, pwd, name;
>>>>>>> Stashed changes
    private Button btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebase_auth = FirebaseAuth.getInstance();
        database_ref = FirebaseDatabase.getInstance().getReference("login");

        email = findViewById(R.id.user_id);
        pwd = findViewById(R.id.user_pwd);
<<<<<<< Updated upstream
=======
        name = findViewById(R.id.user_name);
>>>>>>> Stashed changes

        btn_register = findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 회원가입 처리 시작
<<<<<<< Updated upstream
=======
                String str_name = name.getText().toString();
>>>>>>> Stashed changes
                String str_email = email.getText().toString();
                String str_pwd = pwd.getText().toString();

                // Firebase Auth 진행
                firebase_auth.createUserWithEmailAndPassword(str_email, str_pwd).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
<<<<<<< Updated upstream
                        if(task.isSuccessful()){
                            FirebaseUser firebase_user = firebase_auth.getCurrentUser();
                            UserAccount account = new UserAccount();
                            account.setIdToken(firebase_user.getUid());
                            account.setEmailId(firebase_user.getEmail());
                            account.setPassword(str_pwd);

                            // setValue -> database에 삽입
                            database_ref.child("UserAccount").child(firebase_user.getUid()).setValue(account);

                            Toast.makeText(RegisterActivity.this, "회원가입에 성공하셨습니다", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this, login_main.class);
                            //startActivity(intent);
                            finish();
                        } else{
                            Toast.makeText(RegisterActivity.this, "회원가입에 실패하셨습니다", Toast.LENGTH_SHORT).show();
=======
                        if (str_pwd.length() <= 7) {
                            Toast.makeText(RegisterActivity.this, "비밀번호를 8자 이상으로 설정해 주세요.", Toast.LENGTH_SHORT).show();
                        } else {
                            if (task.isSuccessful()) {
                                FirebaseUser firebase_user = firebase_auth.getCurrentUser();
                                UserAccount account = new UserAccount();
                                account.setIdToken(firebase_user.getUid());
                                account.setUsername(str_name);
                                account.setEmailId(firebase_user.getEmail());
                                account.setPassword(str_pwd);

                                // setValue -> database에 삽입
                                database_ref.child("UserAccount").child(firebase_user.getUid()).setValue(account);

                                //이메일 인증
                                firebase_user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(RegisterActivity.this, "Email sent", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                Toast.makeText(RegisterActivity.this, "회원가입에 성공하셨습니다", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this, Email_Authentication.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(RegisterActivity.this, "회원가입에 실패하셨습니다", Toast.LENGTH_SHORT).show();
                            }
>>>>>>> Stashed changes
                        }
                    }
                });
            }
        });
    }
}