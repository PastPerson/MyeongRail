package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

class ResearchRecord{
    public String getStation() {
        return name;
    }

    public void setStation(String station) {
        this.name = station;
    }
    public HashMap<String, HashMap<String, ArrayList<Float>>> getTimeIndex(){
        return timeIndex;
    }

    String name;

    public void setTimeIndex(HashMap<String, HashMap<String, ArrayList<Float>>> timeIndex) {
        this.timeIndex = timeIndex;
    }

    HashMap<String, HashMap<String, ArrayList<Float>>> timeIndex = new HashMap<>();


    public ResearchRecord(){
        this.name = "";
    }

    public ResearchRecord(String name){
        this.name = name;
        StationInfo s = new StationInfo();
        int index = s.getIndexOfStation().indexOf(name);
        HashMap<String, ArrayList<Float>> t1 = new HashMap<>();
        ArrayList<Float> f = new ArrayList<>();
        station[] st = s.getst();
        for(int i = 0; i < 36; i++){
            f.add((float) 0);
        }
        timeIndex.put(String.valueOf(st[index].getLine()[0][0])+"line", t1);
        if(st[index].getLine()[1][0] != -1){
            timeIndex.put(String.valueOf(st[index].getLine()[1][0])+"line", t1);
        }
        t1.put("uptime", f);
        t1.put("downtime", f);
    }
    public void addDensity(int line, int ud, int index, float f){
        float nf;
        if (ud == 0) {
            nf = timeIndex.get(String.valueOf(line)+"line").get("uptime").get(index) + f;
            timeIndex.get(String.valueOf(line)+"line").get("uptime").set(index, nf);
        } else {
            nf = timeIndex.get(String.valueOf(line)+"line").get("downtime").get(index) + f;
            timeIndex.get(String.valueOf(line)+"line").get("downtime").set(index, nf);
        }
    }
}

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
    private ResearchRecord change_value = null;
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
                database_ref.child("request").updateChildren(h);
                ArrayList<String> s = r.getS();
                Dijkstra d = new Dijkstra();
                d.no_record_check(s.get(0), s.get(1));
                DataUtil util = new DataUtil();
                List<Integer> between;
                Data time = d.getBTime();
                between = util.between_time(time);
                time.setBetween(between);
                Data dist = d.getBDist();
                between = util.between_time(dist);
                dist.setBetween(between);
                Data charge = d.getBCharge();
                between = util.between_time(charge);
                charge.setBetween(between);
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

    public void RequestReceive(){
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName){
//                System.out.println("density request occurred!");
                initRecord();
                Request r = snapshot.getValue(Request.class);
                ArrayList<String> s = r.getS();
                Dijkstra d = new Dijkstra();
                if(s.size() == 2){
                    d.no_record_check(s.get(0), s.get(1));
                }
                Data time = d.getBTime();
                Data dist = d.getBDist();
                Data charge = d.getBCharge();
                addRecord(time,dist,charge,r.getTime());
                database_ref.child("request").child(snapshot.getKey()).removeValue();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                System.out.println("removed well");
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        database_ref.child("request").addChildEventListener(childEventListener);
    }

    public void addRecord(Data a, Data b, Data c) {
        tr=new Thread(new Runnable() {
            @Override
            public void run() {
                database_ref.child("station").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        int[] path = new int[111];
                        StationInfo s = new StationInfo();
                        station[] st = s.getst();
                        String[] st_list = s.getStationList();
                        DataUtil util = new DataUtil();
                        int[][] line_time = util.getLineTime(a);
                        int[] index = util.time_index(a);
                        int n = 0;
                        for(int i = a.getPath_cnt() - 1; i >= 0; i--){
                            st[a.getPath().get(i)].addValue(line_time[n][0], line_time[n][1], index[n], (float) 0.7);

                            if(n < a.getSum_t() && st_list[a.getPath().get(i)].equals(a.getTrans().get(n))) {
                                n++;
                                st[a.getPath().get(i)].addValue(line_time[n][0], line_time[n][1], index[n], (float) 0.7);
                            }
                        }
                        line_time = util.getLineTime(b);
                        index = util.time_index(b);
                        n = 0;
                        for(int i = b.getPath_cnt() - 1; i >= 0; i--){
                            st[b.getPath().get(i)].addValue(line_time[n][0], line_time[n][1], index[n], (float) 0.2);
                            if(n < b.getSum_t() && st_list[b.getPath().get(i)].equals(b.getTrans().get(n))) {
                                n++;
                                st[b.getPath().get(i)].addValue(line_time[n][0], line_time[n][1], index[n], (float) 0.2);
                            }
                        }
                        line_time = util.getLineTime(c);
                        index = util.time_index(c);
                        n = 0;
                        for(int i = c.getPath_cnt() - 1; i >= 0; i--){
                            st[c.getPath().get(i)].addValue(line_time[n][0], line_time[n][1], index[n], (float) 0.1);
                            if( n < c.getSum_t() && st_list[c.getPath().get(i)].equals(c.getTrans().get(n))) {
                                n++;
                                st[c.getPath().get(i)].addValue(line_time[n][0], line_time[n][1], index[n], (float) 0.1);

                            }
                        }

                        for(int i = 0; i < 111; i++){
                            if(i < a.getPath_cnt()){
                                path[a.getPath().get(i)] = 1;
                            }
                            if(i < b.getPath_cnt()){
                                path[b.getPath().get(i)] = 1;
                            }
                            if(i < c.getPath_cnt()){
                                path[c.getPath().get(i)] = 1;
                            }
                        }

                        for(int i = 0; i < 111; i++){
                            if(path[i] == 1){
                                System.out.println("역 이름: "+st_list[i]);
                                ResearchRecord r = task.getResult().child(st_list[i]).getValue(ResearchRecord.class);
                                if(r == null){
                                    r = new ResearchRecord(st_list[i]);

                                }
                                for(int line : st[i].getValue().keySet()){
                                    for(int ud : st[i].getValue().get(line).keySet()){
                                        for(int t_index : st[i].getValue().get(line).get(ud).keySet()){
                                            r.addDensity(line, ud, t_index, st[i].getValue().get(line).get(ud).get(t_index));
                                        }
                                    }
                                }
                                database_ref.child("station").child(st_list[i]).setValue(r);
                            }
                        }
                    }
                });
            }
        });
        tr.start();
    }


    public void addRecord(Data a, Data b, Data c, int now_time) {
        tr=new Thread(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-DD");
                String date = simpleDateFormat.format(System.currentTimeMillis());
                database_ref.child("station").child(date).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        int[] path = new int[111];
                        StationInfo s = new StationInfo();
                        station[] st = s.getst();
                        String[] st_list = s.getStationList();
                        DataUtil util = new DataUtil();
                        int[][] line_time = util.getLineTime(a);
                        int[] index = util.time_index(a,now_time);
                        int n = 0;
                        for(int i = a.getPath_cnt() - 1; i >= 0; i--){
                            st[a.getPath().get(i)].addValue(line_time[n][0], line_time[n][1], index[n], (float) 0.7);
                            if(n < a.getSum_t() && st_list[a.getPath().get(i)].equals(a.getTrans().get(n))) {
                                n++;
                                st[a.getPath().get(i)].addValue(line_time[n][0], line_time[n][1], index[n], (float) 0.7);
                            }
                        }
                        line_time = util.getLineTime(b);
                        index = util.time_index(b, now_time);
                        n = 0;
                        for(int i = b.getPath_cnt() - 1; i >= 0; i--){
                            st[b.getPath().get(i)].addValue(line_time[n][0], line_time[n][1], index[n], (float) 0.2);
                            if(n < b.getSum_t() && st_list[b.getPath().get(i)].equals(b.getTrans().get(n))) {
                                n++;
                                st[b.getPath().get(i)].addValue(line_time[n][0], line_time[n][1], index[n], (float) 0.2);
                            }
                        }
                        line_time = util.getLineTime(c);
                        index = util.time_index(c,now_time);
                        n = 0;
                        for(int i = c.getPath_cnt() - 1; i >= 0; i--){
                            st[c.getPath().get(i)].addValue(line_time[n][0], line_time[n][1], index[n], (float) 0.1);
                            if( n < c.getSum_t() && st_list[c.getPath().get(i)].equals(c.getTrans().get(n))) {
                                n++;
                                st[c.getPath().get(i)].addValue(line_time[n][0], line_time[n][1], index[n], (float) 0.1);
                            }
                        }

                        for(int i = 0; i < 111; i++){
                            if(i < a.getPath_cnt()){
                                path[a.getPath().get(i)] = 1;
                            }
                            if(i < b.getPath_cnt()){
                                path[b.getPath().get(i)] = 1;
                            }
                            if(i < c.getPath_cnt()){
                                path[c.getPath().get(i)] = 1;
                            }
                        }

                        for(int i = 0; i < 111; i++){
                            if(path[i] == 1){
                                ResearchRecord r = task.getResult().child(st_list[i]+"st").getValue(ResearchRecord.class);
                                if(r == null){
                                    r = new ResearchRecord(st_list[i]);

                                }
                                for(int line : st[i].getValue().keySet()){
                                    for(int ud : st[i].getValue().get(line).keySet()){
                                        for(int t_index : st[i].getValue().get(line).get(ud).keySet()){
                                            r.addDensity(line, ud, t_index, st[i].getValue().get(line).get(ud).get(t_index));
                                        }
                                    }
                                }
                                database_ref.child("station").child(date).child(st_list[i]+"st").setValue(r);
                            }
                        }
                    }
                });
            }
        });

        tr.start();
    }

    public void initRecord() {
        tr=new Thread(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-DD");
                String date = simpleDateFormat.format(System.currentTimeMillis());
                database_ref.child("station").child(date).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        Object o = task.getResult().getValue();
                        if(o == null) {
                            StationInfo s = new StationInfo();
                            for (int i = 0; i < 111; i++) {
                                database_ref.child("station").child(date).child(s.getStationList()[i]+"st").setValue(new ResearchRecord(s.getStationList()[i]));
                            }
                        } else return;
                    }
                });
            }
        });

        tr.start();
    }
}
