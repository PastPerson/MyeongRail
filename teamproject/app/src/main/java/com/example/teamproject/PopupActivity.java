package com.example.teamproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.Nullable;



public class PopupActivity extends Activity {
    private TextView start_text;
    private TextView transfer_text;
    private TextView end_text;
    private TextView station;
    private TextView info;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_activity);
        start_text=(TextView)findViewById(R.id.start_pop);
        transfer_text=(TextView) findViewById(R.id.transfer_pop);
        end_text=(TextView) findViewById(R.id.end_pop);
        station=(TextView)findViewById(R.id.station_pop);
        info=(TextView)findViewById(R.id.std_info);
        //데이터 가져오기
        Intent intent = getIntent();
        String data = intent.getStringExtra("data");
        station.setText(data);

        start_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PopupActivity.this,subway_result.class);
                intent.putExtra("start_point", station.getText());
                Log.d("viewTest", "이름"+station.getText() );
                startActivity(intent);
            }
        });
        transfer_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PopupActivity.this,subway_result.class);
                intent.putExtra("transfer_point", station.getText());

                startActivity(intent);
            }
        });
        end_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PopupActivity.this,subway_result.class);
                intent.putExtra("end_point", station.getText());

                startActivity(intent);
            }
        });
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PopupActivity.this,stationtimetable.class);
                intent.putExtra("name",station.getText());
                startActivity(intent);
            }
        });
    }
    public void mOnClose(View v){

        finish();
    }


}
