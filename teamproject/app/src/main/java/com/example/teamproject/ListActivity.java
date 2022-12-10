package com.example.teamproject;

import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {
    private Button write_btn;
    private Spinner post_spn;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference my_ref = database.getReference("post");
    ArrayList<Post_item> postList = new ArrayList<>();
    String firebase_path;
    boolean first_time = true;


    @Override
    public void onBackPressed() {
        startActivity(new Intent(ListActivity.this,MainActivity.class));
        finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        write_btn = (Button) findViewById(R.id.reg_button);
        post_spn = findViewById(R.id.post_spn);
        post_spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView)parent.getChildAt(2)).setTextColor(Color.WHITE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ListView post_listView = findViewById(R.id.post_lv);

        LayoutInflater layoutInflater = LayoutInflater.from(ListActivity.this);
        onList(post_listView);
        post_spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Log.i("posttype : ",""+position);
                firebase_path = ("post"+position);
                switch (position){
                    case 0:
                        my_ref =database.getReference("post0");
                        break;
                    case 1 :
                        my_ref = database.getReference("post1");
                        break;
                    case 2 :
                        my_ref = database.getReference("post2");
                        break;
                    case 3 :
                        my_ref = database.getReference("post3");
                        break;
                    case 4 :
                        my_ref = database.getReference("post4");
                        break;
                    case 5 :
                        my_ref = database.getReference("post5");
                        break;
                    case 6 :
                        my_ref = database.getReference("post6");
                        break;
                    case 7 :
                        my_ref = database.getReference("post7");
                        break;
                    case 8 :
                        my_ref = database.getReference("post8");
                        break;
                    case 9 :
                        my_ref = database.getReference("post9");
                        break;
                    default:
                        break;
                }
             onList(post_listView);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        write_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ListActivity.this, PostWriteActivity.class));
            }

        });
        post_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(ListActivity.this, PostViewActivity.class);
                intent.putExtra("Post_item", postList.get(position));
                intent.putExtra("firebase_path", firebase_path);
                startActivity(intent);
            }
        });
    }

    private void onList(ListView post_listView) {
        MyAdapter myAdapter = new MyAdapter();
        if(!first_time){
            post_listView.removeAllViewsInLayout();
        }
        my_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myAdapter.resetItem();
                for(DataSnapshot messageData : snapshot.getChildren()){
                    String crt_dt = messageData.getKey().toString();
                    String content = messageData.child("content").getValue(String.class).toString();
                    String userid = messageData.child("userid").getValue(String.class).toString();
                    String title = messageData.child("title").getValue(String.class).toString();
                    int count = messageData.child("count").getValue(int.class);
                    myAdapter.addItem(crt_dt, userid, content, title,count);
                    first_time = false;
                    //Log.w("add Item :" ,"crt: "+crt_dt+" userid: "+userid+" content: "+content+" title: " +title );
                }
                myAdapter.reverseList();
                postList = myAdapter.getPostList();
                post_listView.setAdapter(myAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

}