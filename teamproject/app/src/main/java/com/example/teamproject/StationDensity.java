package com.example.teamproject;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


class Request{
    public ArrayList<String> getS() {
        return s;
    }

    public void setS(ArrayList<String> s) {
        this.s = s;
    }

    ArrayList<String> s = new ArrayList<>();

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    int time;

    public Request(){

    }
    public Request(String a, String b){
        s.add(a);
        s.add(b);

        Long now=System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat chour = new SimpleDateFormat("HH");
        SimpleDateFormat cmin = new SimpleDateFormat("mm");
        SimpleDateFormat csec = new SimpleDateFormat("ss");
        String shour=chour.format(date);
        String smin=cmin.format(date);
        String sec=csec.format(date);
        int allsec=Integer.parseInt(shour)*360+Integer.parseInt(smin)*6;
        time = allsec/10;
    }
    public Request(String a, String b, int c){
        s.add(a);
        s.add(b);
        time = c;
    }
    public Request(String a, String b, String c){
        s.add(a);
        s.add(b);
        s.add(c);
        Long now=System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat chour = new SimpleDateFormat("HH");
        SimpleDateFormat cmin = new SimpleDateFormat("mm");
        SimpleDateFormat csec = new SimpleDateFormat("ss");
        String shour=chour.format(date);
        String smin=cmin.format(date);
        String sec=csec.format(date);
        int allsec=Integer.parseInt(shour)*360+Integer.parseInt(smin)*6;
        time = allsec/10;
    }
    public Request(String a, String b, String c, int d){
        s.add(a);
        s.add(b);
        s.add(c);
        time = d;
    }
}
public class StationDensity {
    private DatabaseReference database_ref = FirebaseDatabase.getInstance().getReference("density");
    private DatabaseReference result_ref = FirebaseDatabase.getInstance().getReference("result");
    private Thread tr;
    public StationDensity(){
    }


    public void densityRequire(String start, String end){
        database_ref.child("request").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                String key = database_ref.child("request").push().getKey();
                Request r = new Request(start, end);
                HashMap<String,Object> h = new HashMap<>();
                h.put(key, r);
//                database_ref.child("request").updateChildren(h);
                ArrayList<String> s = r.getS();
                Dijkstra d = new Dijkstra();
                d.no_record_check(s.get(0), s.get(1));
                Data time = d.getBTime();
                Data dist = d.getBDist();
                Data charge = d.getBCharge();
                String result = s.get(0)+"-"+s.get(1);
                result_ref.child(result).child("BTime").setValue(time);
                result_ref.child(result).child("BDist").setValue(dist);
                result_ref.child(result).child("BCharge").setValue(charge);
            }
        });
    }
    public void densityRequire(String start, String end, int now_time){
        database_ref.child("request").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                String key = database_ref.child("request").push().getKey();
                Request r = new Request(start, end, now_time);
                HashMap<String,Object> h = new HashMap<>();
                h.put(key, r);
                database_ref.child("request").updateChildren(h);
            }
        });
    };

    public void densityRequire(String start, String via, String end){
        database_ref.child("request").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                String key = database_ref.child("request").push().getKey();
                Request r = new Request(start, via, end);
                HashMap<String,Object> h = new HashMap<>();
                h.put(key, r);
                database_ref.child("request").updateChildren(h);
            }
        });
    }
    public void densityRequire(String start, String via, String end, int now_time){
        database_ref.child("request").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                String key = database_ref.child("request").push().getKey();
                Request r = new Request(start, via, end, now_time);
                HashMap<String,Object> h = new HashMap<>();
                h.put(key, r);
                database_ref.child("request").updateChildren(h);
            }
        });
    }
}