package com.example.teamproject;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalTime;

class ResearchRecord{
    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }


    public float getA_search() {
        return a_search;
    }

    public void setA_search(float a_search) {
        this.a_search = a_search;
    }


    public float getB_search() {
        return b_search;
    }

    public void setB_search(float b_search) {
        this.b_search = b_search;
    }

    public float getC_search() {
        return c_search;
    }

    public void setC_search(float c_search) {
        this.c_search = c_search;
    }

    public int getAb() {
        return ab;
    }

    public void setAb(int ab) {
        this.ab = ab;
    }

    String station;
    float a_search; // 0~30분 검색결과
    float b_search; // 30~60분 검색결과
    float c_search; // 사용할 검색결과 넣음
    int ab; // a_search 쓸지 b_search 쓸지 0이면 a 1이면 b 사용
    public ResearchRecord(){
        this.station = "";
        this.a_search = 0;
        this.b_search = 0;
        this.c_search = 0;
        this.ab = 0;
    }
    public ResearchRecord(String station){
        this.station = station;
        this.a_search = 0;
        this.b_search = 0;
        this.c_search = 0;
        this.ab = 0;
    }
}
public class StationDensity {
    private DatabaseReference database_ref = FirebaseDatabase.getInstance().getReference("density");
    private ResearchRecord change_value = null;
    public StationDensity(){
//        database_ref = FirebaseDatabase.getInstance().getReference("station_density");
//        database_ref.setValue("101");
    }

    public void addRecord(Data a, Data b, Data c) {
        database_ref.child("station").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                float[] path = new float[111];
                StationInfo s = new StationInfo();
                String[] st_list = s.getStationList();
                for(int i = 0; i < 111; i++){
                    if(i < a.getPath_cnt()){
                        path[a.getPath()[i]] += (float) 0.7;
                    }
                    if(i < b.getPath_cnt()){
                        path[b.getPath()[i]] += (float) 0.2;
                    }
                    if(i < c.getPath_cnt()){
                        path[c.getPath()[i]] += (float) 0.1;
                    }
                }
                for(int i = 0; i < 111; i++){
                    if(path[i] != 0){
                        ResearchRecord r = task.getResult().child(st_list[i]).getValue(ResearchRecord.class);
                        if(r == null){
                            database_ref.child("station").child(st_list[i]).setValue(new ResearchRecord(st_list[i]));
                        } else{
                            int lastAB = r.getAb();
                            LocalTime now = LocalTime.now();
                            if (now.getMinute() < 30) {
                                r.setAb(1);
//                                if(lastAB == 0){
//                                    r.setA_search(0);
//                                    r.setC_search(r.getA_search());
//                                }
                            } else {
                                r.setAb(0);
//                                if(lastAB == 1){
//                                    r.setB_search(0);
//                                    r.setC_search(r.getB_search());
//                                }
                            }

                            if (r.getAb() == 0) {
                                r.setA_search(r.getA_search() + path[i]);
                                    System.out.println("fi call " + r.getA_search() + " path " + st_list[i]);
                            } else {
                                r.setB_search(r.getB_search() + path[i]);
                                    System.out.println("fi call " + r.getB_search() + "path " + st_list[i]);
                            }
                            database_ref.child("station").child(st_list[i]).setValue(r);
                        }
                    }
                }
            }
        });
    }

}
