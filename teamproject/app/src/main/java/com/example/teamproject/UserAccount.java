package com.example.teamproject;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

/*
 사용자 계정 정보 모델 클래스
 */
public class UserAccount {
    private String emailId;     // 이메일 아이디
    private String userName;    // 유저 이름
    private String password;    // 비밀번호
    private String idToken;     // Firebase Uid(고유 토큰정보)
    private boolean isManager;  // true일시 관리자 계정

    public UserAccount(){isManager = false;}

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getUsername() {return userName;}

    public void setUsername(String userName){this.userName = userName;}

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public boolean getIsManager(){return isManager;}

    public void accountToDatabase(DatabaseReference databaseRef){
        databaseRef.child("UserAccount").child(this.userName).setValue(this);
    }
    public boolean isLogin(){
        FirebaseAuth fb = FirebaseAuth.getInstance();
        if(fb.getCurrentUser() == null){return false;}
        else{return true;}
    }
    //이메일 인증
    public void sendVeriftEmail(FirebaseUser fbuser){
        fbuser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    //Toast.makeText(RegisterActivity.this, "Email sent", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //해당 유저가 작성한 게시글 불러오는 기능
}