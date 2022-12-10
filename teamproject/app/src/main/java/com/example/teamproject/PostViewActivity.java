package com.example.teamproject;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PostViewActivity extends AppCompatActivity {
    private Button cmt_reg_button;
    private Button reportButton;
    private Button deleteButton;
    private DatabaseReference my_ref;
    private DatabaseReference database_ref;
    EditText comment_et;
    ArrayList<Comment_item> commentList = new ArrayList<>();
    Post_item myItem;
    ListView cmt_listView;
    boolean first_time = true;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);
        cmt_reg_button = findViewById(R.id.reg_button);
        reportButton = findViewById(R.id.post_report_btn);
        deleteButton = findViewById(R.id.post_delete_btn);
        Intent intent=getIntent();
        myItem = (Post_item) intent.getSerializableExtra("Post_item");
        TextView title = findViewById(R.id.title_tv);
        TextView date = findViewById(R.id.date_tv);
        TextView content = findViewById(R.id.content_tv);
        comment_et = findViewById(R.id.comment_et);
        title.setText(myItem.getTitle());
        date.setText(myItem.getReg_date());
        content.setText(myItem.getContent());
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        String path = intent.getStringExtra("firebase_path");
        cmt_listView = findViewById(R.id.comment_ls);
        database_ref = FirebaseDatabase.getInstance().getReference(path);
        my_ref = FirebaseDatabase.getInstance().getReference("login");
        my_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(myItem.getUserid().toString().equals(snapshot.child("UserAccount").child(FirebaseAuth.getInstance()
                        .getCurrentUser().getUid()).child("username").getValue(String.class).toString())){
                    deleteButton.setVisibility(View.VISIBLE);
                }else{
                    deleteButton.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PostViewActivity.this);
                builder.setTitle("").setMessage("삭제하시겠습니까?");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(PostViewActivity.this, ListActivity.class));
                                database_ref.child(myItem.getReg_date()).removeValue();
                            }
                        });
                builder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        cmt_reg_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(comment_et.getText().toString().equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(PostViewActivity.this);
                    builder.setTitle("").setMessage("댓글 내용을 입력하세요.");

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
                else {
                    Comment_item mycomItem = new Comment_item();
                    my_ref = FirebaseDatabase.getInstance().getReference("login");
                    my_ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
                            SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
                            String userid = snapshot.child("UserAccount").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("username").getValue(String.class).toString();
                            String reg_date = (date.format(new Date()) + "   " + time.format(new Date())).toString();
                            mycomItem.setContent(comment_et.getText().toString());
                            mycomItem.setUserid(userid);
                            mycomItem.setReg_date(reg_date);
                            //Log.w("test : ", myItem.getUserid()+myItem.getContent()+myItem.getReg_date());
                            database_ref.child(myItem.getReg_date()).child("comments").child(reg_date).setValue(mycomItem);
                            comment_et.setText("");

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(PostViewActivity.this);
                    builder.setTitle("").setMessage("신고하시겠습니까?");

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int id){
                            if (!path.equals("post0")) {
                                database_ref.child(myItem.getReg_date()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        int count = (int) snapshot.child("count").getValue(Integer.class);
                                        if (count > 3) {
                                            return;
                                        }
                                        my_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot sapshot) {
                                                String userid = sapshot.child("UserAccount").child(FirebaseAuth.getInstance()
                                                        .getCurrentUser().getUid()).child("username").getValue(String.class).toString();
                                                for (DataSnapshot messageData : snapshot.child("reporter").getChildren()) {
                                                    if (messageData.getValue().toString().equals(userid)) {
                                                        return;
                                                    }
                                                }
                                                if (count < 3) {
                                                    int count = (int) snapshot.child("count").getValue(Integer.class) + 1;
                                                    database_ref.child(myItem.getReg_date()).child("count").setValue(count);
                                                    database_ref.child(myItem.getReg_date()).child("reporter").child("" + count).setValue(userid);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                        if (count >= 3) {
                                            //나가고
                                            startActivity(new Intent(PostViewActivity.this, ListActivity.class));
                                            //삭제
                                            database_ref.child(myItem.getReg_date()).removeValue();
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();


            }
        });
//
        onList(cmt_listView);
    }
    public void onList(ListView cmt_listView){
        if(!first_time){
            cmt_listView.removeAllViews();
        }
        cmt_Adapter cmt_adapter = new cmt_Adapter();
        DatabaseReference comments_ref = database_ref.child(myItem.getReg_date()).child("comments");
        comments_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cmt_adapter.reset();
                commentList.clear();
                for(DataSnapshot messageData : snapshot.getChildren()) {
                    String reg_date = messageData.getKey().toString();
                    String content = messageData.child("content").getValue(String.class).toString();
                    String userid = messageData.child("userid").getValue(String.class).toString();
                    cmt_adapter.addItem(reg_date, userid, content);
                    first_time = false;
                }
                commentList = cmt_adapter.getPostList();
                cmt_listView.setAdapter(cmt_adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
