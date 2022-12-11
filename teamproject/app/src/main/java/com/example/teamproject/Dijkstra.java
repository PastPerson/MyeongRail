package com.example.teamproject;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

class Data{
    int time; // 출발역부터 도착역까지 시간
    int dist; // 출발역부터 도착역까지 거리
    int charge; // 출발역부터 도착역까지 요금
    String start;
    String end; // 출발역, 도착역
    List<String> trans = new ArrayList<>(); // 환승역
    List<Integer> path = new ArrayList<>();

    public List<Integer> getBetween() {
        return between;
    }

    public void setBetween(List<Integer> between) {
        this.between = between;
    }

    List<Integer> between = new ArrayList<>();
    int path_cnt;
    int sum_t; // 환승횟수

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    int type; // 0이면 시간 우선, 1이면 거리 우선, 2이면 비용 우선
//    int[][] line = null;


    public void setTime(int time) {
        this.time = time;
    }

    public void setDist(int dist) {
        this.dist = dist;
    }

    public void setCharge(int charge) {
        this.charge = charge;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public void setTrans(List<String> trans){
        this.trans = trans;
    }

    public void setPath(List<Integer> path){
        this.path = path;
    }

    public void setPath_cnt(int path_cnt) {
        this.path_cnt = path_cnt;
    }

    public void setSum_t(int sum_t) {
        this.sum_t = sum_t;
    }


    public Data(){}
    public Data(int time, int dist, int charge, String start, String end, List<String> trans, int sum_t, List<Integer> path, int path_cnt, int type){
        this.time = time;
        this.dist = dist;
        this.charge = charge;
        this.start = start;
        this.end = end;
        this.sum_t = sum_t;
        this.path_cnt = path_cnt;
        this.type = type;
        this.trans = trans;
        this.path = path;
    }

    public int getTime(){
        return this.time;
    }
    public int getDist(){
        return this.dist;
    }
    public int getCharge(){
        return this.charge;
    }
    public List<String> getTrans(){
        return trans;
    }
    public int getSum_t(){
        return sum_t;
    }
    public List<Integer> getPath(){
        return path;
    }
    public int getPath_cnt() {
        return path_cnt;
    }
    public String getStart() {
        return start;
    }
    public String getEnd() {
        return end;
    }

}

class DataUtil{
    public int[][] getLineTime(Data d){
        int sum_t = d.getSum_t();
        int path_cnt = d.getPath_cnt();
        String start = d.getStart();
        List<Integer> path = d.getPath();
        List<String> trans = d.getTrans();

        int[][] line = new int[sum_t + 1][2]; // 지나는 경로에서 사용할 시간표의 정보를 저장함
        // [n][0] 에는 몇번 노선인지 [n][1]에는 상행인지 하행인지 저장 0이면 상행선이다.
        int n = 0;
        StationInfo temp = new StationInfo();
        station s1 = temp.getst(temp.getIndexOfStation().indexOf(start));
        station s2 = temp.getst(path.get(path_cnt - 2));
        if (s1.getLine()[0][0] == s2.getLine()[0][0]) {
            line[0][0] = s1.getLine()[0][0];
            int t = s1.getLine()[0][1] - s2.getLine()[0][1];
            if (t < 0) {
                if (t == -1) {
                    line[0][1] = 0;
                } else { // 1호선과 6호선의 경우 순환선이라 마지막역과 처음역 간 이동 가능
                    line[0][1] = 1;
                }
            } else {
                if (t == 1) {
                    line[0][1] = 1;
                } else {
                    line[0][1] = 0;
                }
            }
        } else if (s1.getLine()[0][0] == s2.getLine()[1][0]) {
            line[0][0] = s1.getLine()[0][0];
            int t = s1.getLine()[0][1] - s2.getLine()[1][1];
            if (t < 0) {
                if (t == -1) {
                    line[0][1] = 0;
                } else { // 1호선과 6호선의 경우 순환선이라 마지막역과 처음역 간 이동 가능
                    line[0][1] = 1;
                }
            } else {
                if (t == 1) {
                    line[0][1] = 1;
                } else {
                    line[0][1] = 0;
                }
            }
        } else if (s1.getLine()[1][0] == s2.getLine()[0][0]) {
            line[0][0] = s1.getLine()[1][0];
            int t = s1.getLine()[1][1] - s2.getLine()[0][1];
            if (t < 0) {
                if (t == -1) {
                    line[0][1] = 0;
                } else { // 1호선과 6호선의 경우 순환선이라 마지막역과 처음역 간 이동 가능
                    line[0][1] = 1;
                }
            } else {
                if (t == 1) {
                    line[0][1] = 1;
                } else {
                    line[0][1] = 0;
                }
            }
        } else if (s1.getLine()[1][0] == s2.getLine()[1][0]) {
            line[0][0] = s1.getLine()[1][0];
            int t = s1.getLine()[1][1] - s2.getLine()[1][1];
            if (t < 0) {
                if (t == -1) {
                    line[0][1] = 0;
                } else { // 1호선과 6호선의 경우 순환선이라 마지막역과 처음역 간 이동 가능
                    line[0][1] = 1;
                }
            } else {
                if (t == 1) {
                    line[0][1] = 1;
                } else {
                    line[0][1] = 0;
                }
            }
        }
        if (sum_t == 0) {
            return line;
        }
        for (int i = path_cnt - 1; i >= 0; i--) {
            if (temp.getStationList()[path.get(i)].equals(trans.get(n))) {
                n++;
                s1 = temp.getst(path.get(i));
                s2 = temp.getst(path.get(i - 1));
                if (s1.getLine()[0][0] == s2.getLine()[0][0]) {
                    line[n][0] = s1.getLine()[0][0];
                    int t = s1.getLine()[0][1] - s2.getLine()[0][1];
                    if (t < 0) {
                        if (t == -1) {
                            line[n][1] = 0;
                        } else { // 1호선과 6호선의 경우 순환선이라 마지막역과 처음역 간 이동 가능
                            line[n][1] = 1;
                        }
                    } else {
                        if (t == 1) {
                            line[n][1] = 1;
                        } else {
                            line[n][1] = 0;
                        }
                    }
                } else if (s1.getLine()[0][0] == s2.getLine()[1][0]) {
                    line[n][0] = s1.getLine()[0][0];
                    int t = s1.getLine()[0][1] - s2.getLine()[1][1];
                    if (t < 0) {
                        if (t == -1) {
                            line[n][1] = 0;
                        } else { // 1호선과 6호선의 경우 순환선이라 마지막역과 처음역 간 이동 가능
                            line[n][1] = 1;
                        }
                    } else {
                        if (t == 1) {
                            line[n][1] = 1;
                        } else {
                            line[n][1] = 0;
                        }
                    }
                } else if (s1.getLine()[1][0] == s2.getLine()[0][0]) {
                    line[n][0] = s1.getLine()[1][0];
                    int t = s1.getLine()[1][1] - s2.getLine()[0][1];
                    if (t < 0) {
                        if (t == -1) {
                            line[n][1] = 0;
                        } else { // 1호선과 6호선의 경우 순환선이라 마지막역과 처음역 간 이동 가능
                            line[n][1] = 1;
                        }
                    } else {
                        if (t == 1) {
                            line[n][1] = 1;
                        } else {
                            line[n][1] = 0;
                        }
                    }
                } else if (s1.getLine()[1][0] == s2.getLine()[1][0]) {
                    line[n][0] = s1.getLine()[1][0];
                    int t = s1.getLine()[1][1] - s2.getLine()[1][1];
                    if (t < 0) {
                        if (t == -1) {
                            line[n][1] = 0;
                        } else { // 1호선과 6호선의 경우 순환선이라 마지막역과 처음역 간 이동 가능
                            line[n][1] = 1;
                        }
                    } else {
                        if (t == 1) {
                            line[n][1] = 1;
                        } else {
                            line[n][1] = 0;
                        }
                    }
                }
                if (n == sum_t) {
                    break;
                }
            }
        }

        return line;
    }
    public int[] wait_time(Data d, int now_time){
        int sum_t = d.getSum_t();
        String start = d.getStart();
        List<String> trans = d.getTrans();
        int[] wait = new int[sum_t+1];
        int[][] line_time = getLineTime(d);
        int now_sec = now_time;
        String[] ride_st = new String[sum_t+1];
        ride_st[0] = start;
        for(int i = 1; i < sum_t+1; i++){
            ride_st[i] = trans.get(i-1);
        }
        StationInfo s = new StationInfo();
        List<Integer> b = d.getBetween();
        for(int i = 0; i < sum_t+1; i++){
            long[] a = s.getTimeTable(s.getIndexOfStation().indexOf(ride_st[i]), line_time[i][0], line_time[i][1]);
            int n = 0;
            while(a[n] < now_sec){
                n++;
                if(n == 36){
                    n = 0;
                    break;
                }
            }
            wait[i] = (int) (a[n] - now_sec);
            now_sec += wait[i] + b.get(i)/10;
        }

        return wait;
    }
    public int[] wait_time(Data d){
        int sum_t = d.getSum_t();
        String start = d.getStart();
        List<String> trans = d.getTrans();
        Long now=System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat chour = new SimpleDateFormat("HH");
        SimpleDateFormat cmin = new SimpleDateFormat("mm");
        String shour=chour.format(date);
        String smin=cmin.format(date);
        int now_sec=Integer.parseInt(shour)*360+Integer.parseInt(smin)*6;
        int[] wait = new int[sum_t+1];
        int[][] line_time = getLineTime(d);
        String[] ride_st = new String[sum_t+1];
        ride_st[0] = start;
        for(int i = 1; i < sum_t+1; i++){
            ride_st[i] = trans.get(i-1);
        }
        StationInfo s = new StationInfo();
        List<Integer> b = d.getBetween();
        for(int i = 0; i < sum_t+1; i++){
            long[] a = s.getTimeTable(s.getIndexOfStation().indexOf(ride_st[i]), line_time[i][0], line_time[i][1]);
            int n = 0;
            while(a[n] < now_sec){
                n++;
                if(n == 36){
                    n = 0;
                    break;
                }
            }
            wait[i] = (int) (a[n] - now_sec);
            now_sec += wait[i] + b.get(i)/10;
        }

        return wait;
    }
    public int[] time_index(Data d, int now_time){
        int sum_t = d.getSum_t();
        String start = d.getStart();
        List<String> trans = d.getTrans();
        int[] t = new int[sum_t+1];
        int[][] line_time = getLineTime(d);
        int now_sec;
        String[] ride_st = new String[sum_t+1];
        ride_st[0] = start;
        for(int i = 1; i < sum_t+1; i++){
            ride_st[i] = trans.get(i-1);
        }
        StationInfo s = new StationInfo();
        List<Integer> b = d.getBetween();
        int[] wait = wait_time(d,now_time);
        now_sec = now_time;
        for(int i = 0; i < sum_t+1; i++){
//            System.out.println("nowsec: "+ now_sec);
            long[] a = s.getTimeTable(s.getIndexOfStation().indexOf(ride_st[i]), line_time[i][0], line_time[i][1]);
            int n = 0;
//            System.out.println("a time: "+a[n]);
            while(a[n] < now_sec){
                n++;
                if(n == 36){
                    n = 0;
                    break;
                }
            }
            t[i] = n;
            now_sec += wait[i] + b.get(i)/10;
        }

        return t;
    }
    public int[] time_index(Data d, int[][] line_time, int now_time){
        int sum_t = d.getSum_t();
        String start = d.getStart();
        List<String> trans = d.getTrans();
        int[] t = new int[sum_t+1];
        int now_sec;
        String[] ride_st = new String[sum_t+1];
        ride_st[0] = start;
        for(int i = 1; i < sum_t+1; i++){
            ride_st[i] = trans.get(i-1);
        }
        StationInfo s = new StationInfo();
        List<Integer> b = d.getBetween();
        int[] wait = wait_time(d,now_time);
        now_sec = now_time;
        DatabaseReference databaseRefernce = FirebaseDatabase.getInstance().getReference("time");
        for(int i = 0; i < sum_t+1; i++){
//            System.out.println("nowsec: "+ now_sec);
            long[] a = s.getTimeTable(s.getIndexOfStation().indexOf(ride_st[i]), line_time[i][0], line_time[i][1]);
            int n = 0;
//            System.out.println("a time: "+a[n]);
            while(a[n] < now_sec){
                n++;
                if(n == 36){
                    n = 0;
                    break;
                }
            }
            t[i] = n;
            now_sec += wait[i] + b.get(i)/10;
        }

        return t;
    }
    public int[] time_index(Data d){
        int sum_t = d.getSum_t();
        String start = d.getStart();
        List<String> trans = d.getTrans();
        int[] t = new int[sum_t+1];
        int[][] line_time = getLineTime(d);
        Long now=System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat chour = new SimpleDateFormat("HH");
        SimpleDateFormat cmin = new SimpleDateFormat("mm");
        SimpleDateFormat csec = new SimpleDateFormat("ss");
        String shour=chour.format(date);
        String smin=cmin.format(date);
        String sec=csec.format(date);
        int now_sec=Integer.parseInt(shour)*360+Integer.parseInt(smin)*6;
//        System.out.println("hour: "+now.getHour()+"  minute: "+now.getMinute());
        String[] ride_st = new String[sum_t+1];
        ride_st[0] = start;
        for(int i = 1; i < sum_t+1; i++){
            ride_st[i] = trans.get(i-1);
        }
        StationInfo s = new StationInfo();
        List<Integer> b = d.getBetween();
        int[] wait = wait_time(d);
//        now_sec = 4000;
//        System.out.println("a name "+ride_st[0]);
        for(int i = 0; i < sum_t+1; i++){
//            System.out.println("nowsec: "+ now_sec);
            long[] a = s.getTimeTable(s.getIndexOfStation().indexOf(ride_st[i]), line_time[i][0], line_time[i][1]);
            int n = 0;
//            System.out.println("a time: "+a[n]);
            while(a[n] < now_sec){
                n++;
                if(n == 36){
                    n = 0;
                    break;
                }
            }
            t[i] = n;
            now_sec += wait[i] + b.get(i)/10;
        }

        return t;
    }

    public int[] between_time(Data d){
        int sum_t = d.getSum_t();
        String start = d.getStart();
        String end = d.getEnd();
        int type = d.getType();
        List<String> trans = d.getTrans();
        int[] t = new int[sum_t+1];
        String[] ride_st = new String[sum_t+2];
        ride_st[0] = start;
        for(int i = 1; i < sum_t+1; i++){
            ride_st[i] = trans.get(i-1);
        }
        ride_st[sum_t+1] = end;
        Dijkstra dijk = new Dijkstra();
        for(int i = 0; i < sum_t + 1; i++) {
            if(type == 0){
                dijk.check1(ride_st[i],ride_st[i+1]);
            } else if(type == 1){
                dijk.check2(ride_st[i],ride_st[i+1]);
            } else if(type == 2){
                dijk.check3(ride_st[i],ride_st[i+1]);
            }
            t[i] = dijk.getAtime();
        }
        return t;
    }
}

public class Dijkstra {
    Context context;
    ArrayList<String> station_index = new ArrayList<>(); // 역 이름이 들어있는 ArrayList
    String[] station_list = new String[111];
    int[][] station_line;  // 각 역이 포함된 호선 정보를 담을 배열
    int[] trans_station; // 환승역 정보를 담을 배열

    public int getSum_t() {
        return sum_t;
    }

    public void setSum_t(int sum_t) {
        this.sum_t = sum_t;
    }

    public String[] getCc() {
        return cc;
    }

    int count;

    public void setCc(String[] cc) {
        this.cc = cc;
    }

    String cc[]=new String[111]; // 환승한 역이름을 저장할 배열

    int sum_t=0;
    boolean[] trans = new boolean[35]; // 환승역의 환승여부를 저장하는 배열이다.
    public int km;
    int atime;
    int charge;
    public int getAtime() {
        return atime;
    } // 소모 시간 getter

    public void setAtime(int atime) {
        this.atime = atime;
    } // 소모 시간 setter



    public void setKm(int km) {
        this.km = km;
    } // 이동 거리 setter

    public void setCharge(int charge) {
        this.charge = charge;
    } // 소모 비용 setter



    public int getKm() {
        return km;
    } // 이동 거리 getter



    public int getCharge() {
        return charge;
    } // 소모 비용 getter


    int INF = 9999; // 이동 불가능한 역들은 이동 불가임을 나타내기 위해 9999가 들어감
    int[][] time; // 이동하는데 걸리는 시간
    int[][] dist; // 이동 거리
    int[][] cost; // 이동하는데 소모하는 비용
    Data b_time; // 최소시간
    Data b_dist; // 최단거리
    Data b_charge; // 최소비용
    String[] transfer_list= new String[2];// 경유역과 전 경유역을 담을 배열 변수

    public Data getBTime(){
        return b_time;
    }
    public Data getBDist(){
        return b_dist;
    }
    public Data getBCharge(){ return b_charge; }

    StationDensity density;

    public Dijkstra(Context context){
        this.context = context;
        StationInfo data = new StationInfo(); // 지하철 역에 ;'대한 정보를 제공하는 클래스 객체
        this.station_index = data.getIndexOfStation();
        this.station_list = data.getStationList();
        this.time = data.getTime(); // 역간 이동 시 소모 시간 정보가 들어있는 배열을 받아온다.
        this.dist = data.getDist(); // 역간 이동 시 이동 거리 정보가 들어있는 배열을 받아온다.
        this.cost = data.getCost(); // 역간 이동 시 소모 비용 정보가 들어있는 배열을 받아온다.
        this.station_line = data.getStationLine(); // 역이 어느 호선에 위치했는지에 대한 정보가 들어있는 배열을 받아온다.
        this.trans_station = data.getTransStation(); // 환승역 정보가 들어있는 배열을 받아온다.
        this.density = new StationDensity();
    }

    public Dijkstra(){
        StationInfo data = new StationInfo(); // 지하철 역에 ;'대한 정보를 제공하는 클래스 객체
        this.station_index = data.getIndexOfStation();
        this.station_list = data.getStationList();
        this.time = data.getTime(); // 역간 이동 시 소모 시간 정보가 들어있는 배열을 받아온다.
        this.dist = data.getDist(); // 역간 이동 시 이동 거리 정보가 들어있는 배열을 받아온다.
        this.cost = data.getCost(); // 역간 이동 시 소모 비용 정보가 들어있는 배열을 받아온다.
        this.station_line = data.getStationLine(); // 역이 어느 호선에 위치했는지에 대한 정보가 들어있는 배열을 받아온다.
        this.trans_station = data.getTransStation(); // 환승역 정보가 들어있는 배열을 받아온다.
        this.density = new StationDensity();
    }

    void trans_station(int[] path, int path_cnt, int s, int e){ // s에서 e까지 도달하는 경로에서 지나는 환승역을 구한다.
        sum_t = 0;
        count = 0;
        if(path_cnt < 3){
            return;
        }
        for(int i = path_cnt-2; i >= 1; i--){
            int prev = path[i+1]; // 이전 역
            int now = path[i]; // 현재 역
            int next = path[i-1]; // 다음 역

            for(int j = 0; j < 35; j++){
                if(now == trans_station[j]){ // 이전역과 다음역의 호선 정보를 통해 환승여부를 판단한다.
                    if(station_line[prev][0] == station_line[now][0]){
                        if(station_line[now][0] == station_line[next][0] || station_line[now][0] == station_line[next][1]){
                            trans[j] = false;
                        }else{
                            trans[j] = true;
                            cc[count++] = station_list[trans_station[j]];
                            sum_t++;
                        }
                    } else if(station_line[prev][0] == station_line[now][1]){
                        if(station_line[now][1] == station_line[next][0] || station_line[now][1] == station_line[next][1]){
                            trans[j] = false;
                        }else{
                            trans[j] = true;
//                            System.out.println("아니 뭐임 "+ station_list[trans_station[j]]);
                            cc[count++] = station_list[trans_station[j]];
                            sum_t++;
                        }
                    } else if(station_line[prev][1] != 0 && station_line[prev][1] == station_line[now][0]){
                        if(station_line[now][0] == station_line[next][0] || station_line[now][0] == station_line[next][1]){
                            trans[j] = false;
                        }else{
                            trans[j] = true;
                            cc[count++] = station_list[trans_station[j]];
                            sum_t++;
                        }
                    }else if(station_line[prev][1] != 0 && station_line[prev][1] == station_line[now][1]){
                        if(station_line[now][1] == station_line[next][0] || station_line[now][1] == station_line[next][1]){
                            trans[j] = false;
                        }else{
                            trans[j] = true;
                            cc[count++] = station_list[trans_station[j]];
                            sum_t++;
                        }
                    }
                }
            }
        }
        setCc(cc);
        setSum_t(sum_t);
    }


    int[] pathappend(List<Integer> al, int alen, List<Integer> bl, int blen){
        int[] a = new int[al.size()];
        int[] b = new int[bl.size()];
        int[] c = new int[alen + blen];
        for(int i = 0; i < al.size(); i++){
            a[i] = al.get(i);
        }
        for(int i = 0; i < bl.size(); i++){
            b[i] = bl.get(i);
        }
        System.arraycopy(a, 0, c, 0, alen);
        System.arraycopy(b, 1, c, alen, blen-1);
        return c;
    }

    Data datasquash(Data a, Data b, int type){
        int time = a.getTime()+b.getTime();
        int dist = a.getDist()+b.getDist();
        int charge = a.getCharge()+b.getCharge();
        String start = a.getStart();
        String end = b.getEnd();
        int[] path = pathappend(b.getPath(),b.getPath_cnt(), a.getPath(), a.getPath_cnt());
        int path_cnt = a.getPath_cnt()+b.getPath_cnt()-1;
//        trans_station(path, path_cnt, station_index.indexOf(start), station_index.indexOf(end));
        String[] trans = new String[a.getSum_t()+b.getSum_t()+1];
        for(int i = 0; i < a.getSum_t()+b.getSum_t()+1; i++){
            if(i < a.getSum_t()){
                trans[i] = a.getTrans().get(i);
            } else if(i == a.getSum_t()){
                trans[i] = a.getEnd();
            } else{
                trans[i] = b.getTrans().get(i-a.getSum_t()-1);
            }
        }
        int sum_t = a.getSum_t()+b.getSum_t()+1;

        int[] trim_path = new int[path_cnt];
        for(int n = 0; n < path_cnt; n++){
            trim_path[n] = path[n];
        }
        return new Data(time, dist, charge, start, end, Arrays.asList(trans), sum_t, Arrays.asList(Arrays.stream(trim_path).boxed().toArray(Integer[]::new)), path_cnt, type);
    }

    void check1(String start, String finish){
        int i, j, k = 0, min; /* i, j, k = for문을 위해 생성
				  							  s = 시작노드 입력값
				  							  e = 끝 노드 입력값
				  							  min = 최소값을 찾기 위함*/
        int s = station_index.indexOf(start);
        int e = station_index.indexOf(finish);
        int[] v = new int[111]; //이동하는 최단 거리 확인
        int[] distance = new int[111];//지나간 노드 확인
        int[] distance1 = new int[111];//지나간 노드 확인
        int[] distance2 = new int[111];//지나간 노드 확인
        int[] via = new int[111]; //지나간 노드를 오름차순으로 정렬해서 저장
        for (j = 0; j < 111; j++) {
            v[j] = 0;//초기화
            distance1[j] = INF; //j의 거리를 아직 연결되지 않음을 설정
        }
        distance1[s] = 0;

        for (i = 0; i < 111; i++) {
            min = INF;  //최소값을 아직 연결되지 않음을 설정
            for (j = 0; j < 111; j++) {
                if (v[j] == 0 && distance1[j] < min)
                //이웃한 노드 중 방문하지 않은 노드일 경우
                {
                    k = j;//최단거리 노드번호를 k에 저장
                    min = distance1[j];//min에 최단거리를 장
                }
            }


            v[k] = 1;
            if (min == INF)
                break;

            for (j = 0; j < 111; j++) {
                if (distance1[j] > distance1[k] + time[k][j]) {
                    distance[j] = distance[k] + dist[k][j];
                    distance1[j] = distance1[k] + time[k][j];
                    distance2[j] = distance2[k] + cost[k][j];
                    via[j] = k;
                }
            }
        }
        km = distance[e];
        atime = distance1[e];
        charge = distance2[e];
        int path[] = new int[111];
        int path_cnt = 0;
        k = e;
        while (true) {
            path[path_cnt++] = k;
            if (k == s)
                break;
            k = via[k];
        }
        // System.out.print(" 경로 :");
        int[] trim_path = new int[path_cnt];
        for(int n = 0; n < path_cnt; n++){
            trim_path[n] = path[n];
        }
        trans_station(trim_path, path_cnt, s, e);
        String[] t = new String[sum_t];
        for(int n = 0; n < sum_t; n++){
            t[n] = cc[n];
        }
        b_time = new Data(atime, km, charge, start, finish, Arrays.asList(t), sum_t, Arrays.asList(Arrays.stream(trim_path).boxed().toArray(Integer[]::new)), path_cnt, 0);
    }

    void check2(String start, String finish){
        int i, j, k = 0, min; /* i, j, k = for문을 위해 생성
				  							  s = 시작노드 입력값
				  							  e = 끝 노드 입력값
				  							  min = 최소값을 찾기 위함*/
        int s = station_index.indexOf(start);
        int e = station_index.indexOf(finish);
        int[] v = new int[111]; //이동하는 최단 거리 확인
        int[] distance = new int[111];//지나간 노드 확인
        int[] distance1 = new int[111];//지나간 노드 확인
        int[] distance2 = new int[111];//지나간 노드 확인
        int[] via = new int[111]; //지나간 노드를 오름차순으로 정렬해서 저장
        for (j = 0; j < 111; j++) {
            v[j] = 0;//초기화
            distance[j] = INF; //j의 거리를 아직 연결되지 않음을 설정
        }
        distance[s] = 0;

        for (i = 0; i < 111; i++) {
            min = INF;  //최소값을 아직 연결되지 않음을 설정
            for (j = 0; j < 111; j++) {
                if (v[j] == 0 && distance[j] < min)
                //이웃한 노드 중 방문하지 않은 노드일 경우
                {
                    k = j;//최단거리 노드번호를 k에 저장
                    min = distance[j];//min에 최단거리를 장
                }
            }

            v[k] = 1;
            if (min == INF)
                break;

            for (j = 0; j < 111; j++) {
                if (distance[j] > distance[k] + dist[k][j]) {
                    distance[j] = distance[k] + dist[k][j];
                    distance1[j] = distance1[k] + time[k][j];
                    distance2[j] = distance2[k] + cost[k][j];
                    via[j] = k;
                }
            }
        }

        km = distance[e];
        atime = distance1[e];
        charge = distance2[e];
        int path[] = new int[111];
        int path_cnt = 0;
        k = e;
        while (true) {
            path[path_cnt++] = k;
            if (k == s)
                break;
            k = via[k];
        }
        // System.out.print(" 경로 :");
        int[] trim_path = new int[path_cnt];
        for(int n = 0; n < path_cnt; n++){
            trim_path[n] = path[n];
        }
        trans_station(trim_path, path_cnt, s, e);
        String[] t = new String[sum_t];
        for(int n = 0; n < sum_t; n++){
            t[n] = cc[n];
        }
        b_dist = new Data(atime, km, charge, start, finish, Arrays.asList(t), sum_t, Arrays.asList(Arrays.stream(trim_path).boxed().toArray(Integer[]::new)), path_cnt, 1);
    }
    void check3(String start, String finish) {
        int i, j, k = 0, min; /* i, j, k = for문을 위해 생성
				  							  s = 시작노드 입력값
				  							  e = 끝 노드 입력값
				  							  min = 최소값을 찾기 위함*/
        int s = station_index.indexOf(start);
        int e = station_index.indexOf(finish);
        int[] v = new int[111]; //이동하는 최단 거리 확인
        int[] distance = new int[111];//지나간 노드 확인
        int[] distance1 = new int[111];//지나간 노드 확인
        int[] distance2 = new int[111];//지나간 노드 확인
        int[] via = new int[111]; //지나간 노드를 오름차순으로 정렬해서 저장
        for (j = 0; j < 111; j++) {
            v[j] = 0;//초기화
            distance2[j] = INF; //j의 거리를 아직 연결되지 않음을 설정
        }
        distance2[s] = 0;

        for (i = 0; i < 111; i++) {
            min = INF;  //최소값을 아직 연결되지 않음을 설정
            for (j = 0; j < 111; j++) {
                if (v[j] == 0 && distance2[j] < min)
                //이웃한 노드 중 방문하지 않은 노드일 경우
                {
                    k = j;//최단거리 노드번호를 k에 저장
                    min = distance2[j];//min에 최단거리를 장
                }
            }

            v[k] = 1;
            if (min == INF)
                break;

            for (j = 0; j < 111; j++) {
                if (distance2[j] > distance2[k] + cost[k][j]) {
                    distance[j] = distance[k] + dist[k][j];
                    distance1[j] = distance1[k] + time[k][j];
                    distance2[j] = distance2[k] + cost[k][j];
                    via[j] = k;
                }
            }
        }


        km = distance[e];
        atime = distance1[e];
        charge = distance2[e];
        int path[] = new int[111];
        int path_cnt = 0;
        k = e;
        while (true) {
            path[path_cnt++] = k;
            if (k == s)
                break;
            k = via[k];
        }
        int[] trim_path = new int[path_cnt];
        for(int n = 0; n < path_cnt; n++){
            trim_path[n] = path[n];
        }
        trans_station(trim_path, path_cnt, s, e);
        String[] t = new String[sum_t];
        for(int n = 0; n < sum_t; n++){
            t[n] = cc[n];
        }
        b_charge = new Data(atime, km, charge, start, finish, Arrays.asList(t), sum_t, Arrays.asList(Arrays.stream(trim_path).boxed().toArray(Integer[]::new)), path_cnt, 2);
    }
}