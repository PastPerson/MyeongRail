package com.example.teamproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

public class subway_result extends AppCompatActivity {
    private Dijkstraaa sub;
    private String start_point="101";
    private String transfer_point="0";
    private String end_point="103";
    private TextView st_txt;
    private TextView tf_txt;
    private TextView ed_txt;
    private TextView st_cir;
    private TextView ed_cir;
    private TextView sub_result;
    private Button st_btn;
    private Button tf_btn;
    private Button ed_btn;
    private Spinner h_spin;
    private Spinner m_spin;
    private ArrayAdapter hourAdapter;
    private ArrayAdapter minAdapter;
    private RadioGroup radioGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subway_result);
        sub=new Dijkstraaa(subway_result.this);
        sub_result=findViewById(R.id.subway_result);
        st_txt=findViewById(R.id.startpoint_txt);
        tf_txt=findViewById(R.id.transferpoint_txt);
        ed_txt=findViewById(R.id.endpoint_txt);
        st_btn=findViewById(R.id.startpoint_btn);
        tf_btn=findViewById(R.id.transferpoint_btn);
        ed_btn=findViewById(R.id.endpoint_btn);
        st_cir=findViewById(R.id.startpoint_circle);
        ed_cir=findViewById(R.id.endpoint_circle);
        point_setting(start_point,transfer_point,end_point);
        h_spin=findViewById(R.id.spinner_hour);
        m_spin=findViewById(R.id.spinner_min);
        hourAdapter=ArrayAdapter.createFromResource(this,R.array.hour, android.R.layout.simple_spinner_item);
        hourAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        h_spin.setAdapter(hourAdapter);
        minAdapter=ArrayAdapter.createFromResource(this,R.array.min, android.R.layout.simple_spinner_item);
        minAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        m_spin.setAdapter(minAdapter);
        radioGroup=findViewById(R.id.radio_group);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.radio_time:
                        sub.check(start_point,end_point);
                        sub_result.setText(Integer.toString(sub.getAtime()/60)+"분"+Integer.toString(sub.getAtime()%60)+"초");
                        break;
                    case R.id.radio_distance:
                        sub.check(start_point,end_point);
                        sub_result.setText(Integer.toString(sub.getKm()));
                        break;
                    case R.id.radio_cost:
                        sub.check(start_point,end_point);
                        sub_result.setText(Integer.toString(sub.getCharge()));
                        break;
                }
            }
        });
    }



    public void point_setting(String st_pt, String tf_pt, String ed_pt){
        if(Check_Point(st_pt)){
            st_txt.setText((st_pt));
            st_cir.setText((st_pt));
            st_txt.setVisibility(View.VISIBLE);
            st_btn.setVisibility(View.GONE);
        }else{

            st_btn.setVisibility(View.VISIBLE);
            st_txt.setVisibility(View.GONE);
        }
        if(Check_Point(tf_pt)){
            tf_txt.setText(tf_pt);
            tf_txt.setVisibility(View.VISIBLE);
            tf_btn.setVisibility(View.GONE);

        }else{
            tf_btn.setVisibility(View.VISIBLE);
            tf_txt.setVisibility(View.GONE);

        }
        if(Check_Point(ed_pt)){
            ed_txt.setText(ed_pt);
            ed_cir.setText(ed_pt);
            ed_txt.setVisibility(View.VISIBLE);
            ed_btn.setVisibility(View.GONE);
        }else {

            ed_btn.setVisibility(View.VISIBLE);
            ed_txt.setVisibility(View.GONE);
        }
    }
    public Boolean Check_Point(String point){
        if(point=="0")
            return false;
        else
            return true;
    }

}