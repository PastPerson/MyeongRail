package com.example.teamproject;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

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
//    HashMap<Integer, HashMap<String, ArrayList<Float>>> last_index = new HashMap<>();


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
        timeIndex.put(String.valueOf(st[index].getLine()[0][0]), t1);
//        t1.put("uptime", f);
//        t1.put("downtime", f);
        if(st[index].getLine()[1][0] != -1){
            timeIndex.put(String.valueOf(st[index].getLine()[1][0]), t1);
        }
        t1.put("uptime", f);
        t1.put("downtime", f);
//        HashMap<String, ArrayList<Float>> t1 = time_index.get(String.valueOf(s.getStationLine()[index][0]));
        ArrayList<Float> t3 = timeIndex.get(String.valueOf(s.getStationLine()[index][0])).get("uptime");
    }
    public void addDensity(int line, int ud, int index, float f){
        float nf;
//        System.out.println("\n이름 "+ name +"\n");
//        for(String l : timeIndex.keySet()){
//            System.out.println("line: "+l);
//
//        }
//        HashMap<String, ArrayList<Float>> t = timeIndex.get(String.valueOf(line));
//        for(String s : t.keySet()){
//            System.out.println("key: "+ s);
//        }
        if (ud == 0) {
//            ArrayList<Float> t1 = time_index.get(String.valueOf(line)).get("uptime");
            nf = timeIndex.get(String.valueOf(line)).get("uptime").get(index) + f;
            timeIndex.get(String.valueOf(line)).get("uptime").set(index, nf);
        } else {
            nf = timeIndex.get(String.valueOf(line)).get("downtime").get(index) + f;
            timeIndex.get(String.valueOf(line)).get("downtime").set(index, nf);
        }
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
                int[] path = new int[111];
                StationInfo s = new StationInfo();
                station[] st = s.getst();
                String[] st_list = s.getStationList();
                int[][] line_time = a.getLineTime();
                int[] index = a.time_index();
                int n = 0;
//                for(int i = 0; i < a.getSum_t(); i++){
//                    System.out.println("a환승역 "+a.getTrans()[i]);
//                }
//                for(int i = 0; i < b.getSum_t(); i++){
//                    System.out.println("b환승역 "+b.getTrans()[i]);
//                }
//                for(int i = 0; i < c.getSum_t(); i++){
//                    System.out.println("c환승역 "+c.getTrans()[i]);
//                }
                for(int i = a.getPath_cnt() - 1; i >= 0; i--){
//                    System.out.println("a: "+st_list[a.getPath()[i]]);
                    st[a.getPath()[i]].addValue(line_time[n][0], line_time[n][1], index[n], (float) 0.7);

                    if(n < a.getSum_t() && st_list[a.getPath()[i]].equals(a.getTrans()[n])) {
//                        System.out.println("aaa");
                        n++;
                        st[a.getPath()[i]].addValue(line_time[n][0], line_time[n][1], index[n], (float) 0.7);
                    }
                }
                line_time = b.getLineTime();
                index = b.time_index();
                n = 0;
                for(int i = b.getPath_cnt() - 1; i >= 0; i--){
//                    System.out.println("b: "+s.getStationList()[b.getPath()[i]]);
                    st[b.getPath()[i]].addValue(line_time[n][0], line_time[n][1], index[n], (float) 0.2);
                    if(n < b.getSum_t() && st_list[b.getPath()[i]].equals(b.getTrans()[n])) {
                        n++;
                        st[b.getPath()[i]].addValue(line_time[n][0], line_time[n][1], index[n], (float) 0.2);
                    }
                }
                line_time = c.getLineTime();
                index = c.time_index();
                n = 0;
                for(int i = c.getPath_cnt() - 1; i >= 0; i--){
//                    System.out.println("c: "+s.getStationList()[c.getPath()[i]]);

                    st[c.getPath()[i]].addValue(line_time[n][0], line_time[n][1], index[n], (float) 0.1);
                    if( n < b.getSum_t() && st_list[c.getPath()[i]].equals(c.getTrans()[n])) {
                        n++;
                        st[c.getPath()[i]].addValue(line_time[n][0], line_time[n][1], index[n], (float) 0.1);

                    }
                }

                for(int i = 0; i < 111; i++){
                    if(i < a.getPath_cnt()){
                        path[a.getPath()[i]] = 1;
                    }
                    if(i < b.getPath_cnt()){
                        path[b.getPath()[i]] = 1;
                    }
                    if(i < c.getPath_cnt()){
                        path[c.getPath()[i]] = 1;
                    }
                }

                for(int i = 0; i < 111; i++){
                    if(path[i] == 1){
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

}
