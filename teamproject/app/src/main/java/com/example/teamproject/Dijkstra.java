package com.example.teamproject;

import android.content.Context;

import java.util.ArrayList;
import java.time.LocalTime;

class Data{
    int time; // 출발역부터 도착역까지 시간
    int dist; // 출발역부터 도착역까지 거리
    int charge; // 출발역부터 도착역까지 요금
    String start;
    String end; // 출발역, 도착역
    String[] trans = new String[111]; // 환승역
    int[] path = new int[111];

    int path_cnt;
    int sum_t; // 환승횟수
    int type; // 0이면 시간 우선, 1이면 거리 우선, 2이면 비용 우선


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

    public void setTrans(String[] trans) {
        this.trans = trans;
    }

    public void setPath(int[] path) {
        this.path = path;
    }

    public void setPath_cnt(int path_cnt) {
        this.path_cnt = path_cnt;
    }

    public void setSum_t(int sum_t) {
        this.sum_t = sum_t;
    }



    public Data(int time, int dist, int charge, String start, String end, String[] trans, int sum_t, int[] path, int path_cnt, int type){
        this.time = time;
        this.dist = dist;
        this.charge = charge;
        this.start = start;
        this.end = end;
        this.trans = trans;
        this.sum_t = sum_t;
        this.path = path;
        this.path_cnt = path_cnt;
        this.type = type;
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
    public String[] getTrans(){
        return trans;
    }
    public int getSum_t(){
        return sum_t;
    }
    public int[] getPath(){
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

    public int[][] getLineTime(){
        int[][] linetime = new int[sum_t+1][2]; // 지나는 경로에서 사용할 시간표의 정보를 저장함
                                                // [n][0] 에는 몇번 노선인지 [n][1]에는 상행인지 하행인지 저장 0이면 상행선이다.
        int n = 0;
        StationInfo temp = new StationInfo();
//        station[] st = temp.getst();
//        int a = temp.getIndexOfStation().indexOf(start);
//        int b = path_cnt-2;
//        if(st[a].getLine()[0][0] == st[b].getLine()[0][0]){
//            linetime[0][0] = st[a].getLine()[0][0];
//            int t = st[a].getLine()[0][1] - st[b].getLine()[0][1];
//            if(t < 0){
//                if(t == -1){
//                    linetime[0][1] = 0;
//                } else{ // 1호선과 6호선의 경우 순환선이라 마지막역과 처음역 간 이동 가능
//                    linetime[0][1] = 1;
//                }
//            } else{
//                if(t == 1){
//                    linetime[0][1] = 1;
//                } else{
//                    linetime[0][1] = 0;
//                }
//            }
//        } else if(st[a].getLine()[0][0] == st[b].getLine()[1][0]){
//            linetime[0][0] = st[a].getLine()[0][0];
//            int t = st[a].getLine()[0][1] - st[b].getLine()[1][1];
//            if(t < 0){
//                if(t == -1){
//                    linetime[0][1] = 0;
//                } else{ // 1호선과 6호선의 경우 순환선이라 마지막역과 처음역 간 이동 가능
//                    linetime[0][1] = 1;
//                }
//            } else{
//                if(t == 1){
//                    linetime[0][1] = 1;
//                } else{
//                    linetime[0][1] = 0;
//                }
//            }
//        } else if(st[a].getLine()[1][0] == st[b].getLine()[0][0]){
//            linetime[0][0] = st[a].getLine()[1][0];
//            int t = st[a].getLine()[1][1] - st[b].getLine()[0][1];
//            if(t < 0){
//                if(t == -1){
//                    linetime[0][1] = 0;
//                } else{ // 1호선과 6호선의 경우 순환선이라 마지막역과 처음역 간 이동 가능
//                    linetime[0][1] = 1;
//                }
//            } else{
//                if(t == 1){
//                    linetime[0][1] = 1;
//                } else{
//                    linetime[0][1] = 0;
//                }
//            }
//        } else if(st[a].getLine()[1][0] == st[b].getLine()[1][0]){
//            linetime[0][0] = st[a].getLine()[1][0];
//            int t = st[a].getLine()[1][1] - st[b].getLine()[1][1];
//            if(t < 0){
//                if(t == -1){
//                    linetime[0][1] = 0;
//                } else{ // 1호선과 6호선의 경우 순환선이라 마지막역과 처음역 간 이동 가능
//                    linetime[0][1] = 1;
//                }
//            } else{
//                if(t == 1){
//                    linetime[0][1] = 1;
//                } else{
//                    linetime[0][1] = 0;
//                }
//            }
//        }
//        for(int i = sum_t-1; i >= 0; i--) {
//            if (temp.getStationList()[path[i]] == trans[n]) {
//                n++;
//                a = temp.getIndexOfStation().indexOf(path[i]);
//                b = temp.getIndexOfStation().indexOf(path[i-1]);
//                if(st[a].getLine()[0][0] == st[b].getLine()[0][0]){
//                    linetime[0][0] = st[a].getLine()[0][0];
//                    int t = st[a].getLine()[0][1] - st[b].getLine()[0][1];
//                    if(t < 0){
//                        if(t == -1){
//                            linetime[0][1] = 0;
//                        } else{ // 1호선과 6호선의 경우 순환선이라 마지막역과 처음역 간 이동 가능
//                            linetime[0][1] = 1;
//                        }
//                    } else{
//                        if(t == 1){
//                            linetime[0][1] = 1;
//                        } else{
//                            linetime[0][1] = 0;
//                        }
//                    }
//                } else if(st[a].getLine()[0][0] == st[b].getLine()[1][0]){
//                    linetime[0][0] = st[a].getLine()[0][0];
//                    int t = st[a].getLine()[0][1] - st[b].getLine()[1][1];
//                    if(t < 0){
//                        if(t == -1){
//                            linetime[0][1] = 0;
//                        } else{ // 1호선과 6호선의 경우 순환선이라 마지막역과 처음역 간 이동 가능
//                            linetime[0][1] = 1;
//                        }
//                    } else{
//                        if(t == 1){
//                            linetime[0][1] = 1;
//                        } else{
//                            linetime[0][1] = 0;
//                        }
//                    }
//                } else if(st[a].getLine()[1][0] == st[b].getLine()[0][0]){
//                    linetime[0][0] = st[a].getLine()[1][0];
//                    int t = st[a].getLine()[1][1] - st[b].getLine()[0][1];
//                    if(t < 0){
//                        if(t == -1){
//                            linetime[0][1] = 0;
//                        } else{ // 1호선과 6호선의 경우 순환선이라 마지막역과 처음역 간 이동 가능
//                            linetime[0][1] = 1;
//                        }
//                    } else{
//                        if(t == 1){
//                            linetime[0][1] = 1;
//                        } else{
//                            linetime[0][1] = 0;
//                        }
//                    }
//                } else if(st[a].getLine()[1][0] == st[b].getLine()[1][0]){
//                    linetime[0][0] = st[a].getLine()[1][0];
//                    int t = st[a].getLine()[1][1] - st[b].getLine()[1][1];
//                    if(t < 0){
//                        if(t == -1){
//                            linetime[0][1] = 0;
//                        } else{ // 1호선과 6호선의 경우 순환선이라 마지막역과 처음역 간 이동 가능
//                            linetime[0][1] = 1;
//                        }
//                    } else{
//                        if(t == 1){
//                            linetime[0][1] = 1;
//                        } else{
//                            linetime[0][1] = 0;
//                        }
//                    }
//                }
//            }
//        }
        station s1 = temp.getst(temp.getIndexOfStation().indexOf(start));
        station s2 = temp.getst(path[path_cnt - 2]);
        if(s1.getLine()[0][0] == s2.getLine()[0][0]){
            linetime[0][0] = s1.getLine()[0][0];
            int t = s1.getLine()[0][1] - s2.getLine()[0][1];
            if(t < 0){
                if(t == -1){
                    linetime[0][1] = 0;
                } else{ // 1호선과 6호선의 경우 순환선이라 마지막역과 처음역 간 이동 가능
                    linetime[0][1] = 1;
                }
            } else{
                if(t == 1){
                    linetime[0][1] = 1;
                } else{
                    linetime[0][1] = 0;
                }
            }
        } else if(s1.getLine()[0][0] == s2.getLine()[1][0]){
            linetime[0][0] = s1.getLine()[0][0];
            int t = s1.getLine()[0][1] - s2.getLine()[1][1];
            if(t < 0){
                if(t == -1){
                    linetime[0][1] = 0;
                } else{ // 1호선과 6호선의 경우 순환선이라 마지막역과 처음역 간 이동 가능
                    linetime[0][1] = 1;
                }
            } else{
                if(t == 1){
                    linetime[0][1] = 1;
                } else{
                    linetime[0][1] = 0;
                }
            }
        } else if(s1.getLine()[1][0] == s2.getLine()[0][0]){
            linetime[0][0] = s1.getLine()[1][0];
            int t = s1.getLine()[1][1] - s2.getLine()[0][1];
            if(t < 0){
                if(t == -1){
                    linetime[0][1] = 0;
                } else{ // 1호선과 6호선의 경우 순환선이라 마지막역과 처음역 간 이동 가능
                    linetime[0][1] = 1;
                }
            } else{
                if(t == 1){
                    linetime[0][1] = 1;
                } else{
                    linetime[0][1] = 0;
                }
            }
        } else if(s1.getLine()[1][0] == s2.getLine()[1][0]){
            linetime[0][0] = s1.getLine()[1][0];
            int t = s1.getLine()[1][1] - s2.getLine()[1][1];
            if(t < 0){
                if(t == -1){
                    linetime[0][1] = 0;
                } else{ // 1호선과 6호선의 경우 순환선이라 마지막역과 처음역 간 이동 가능
                    linetime[0][1] = 1;
                }
            } else{
                if(t == 1){
                    linetime[0][1] = 1;
                } else{
                    linetime[0][1] = 0;
                }
            }
        }
        if(sum_t == 0){
            return linetime;
        }
        for(int i = path_cnt-1; i >= 0; i--){
            if(temp.getStationList()[path[i]].equals(trans[n])){
                n++;
                s1 = temp.getst(path[i]);
                s2 = temp.getst(path[i-1]);
                if(s1.getLine()[0][0] == s2.getLine()[0][0]){
                    linetime[n][0] = s1.getLine()[0][0];
                    int t = s1.getLine()[0][1] - s2.getLine()[0][1];
                    if(t < 0){
                        if(t == -1){
                            linetime[n][1] = 0;
                        } else{ // 1호선과 6호선의 경우 순환선이라 마지막역과 처음역 간 이동 가능
                            linetime[n][1] = 1;
                        }
                    } else{
                        if(t == 1){
                            linetime[n][1] = 1;
                        } else{
                            linetime[n][1] = 0;
                        }
                    }
                } else if(s1.getLine()[0][0] == s2.getLine()[1][0]){
                    linetime[n][0] = s1.getLine()[0][0];
                    int t = s1.getLine()[0][1] - s2.getLine()[1][1];
                    if(t < 0){
                        if(t == -1){
                            linetime[n][1] = 0;
                        } else{ // 1호선과 6호선의 경우 순환선이라 마지막역과 처음역 간 이동 가능
                            linetime[n][1] = 1;
                        }
                    } else{
                        if(t == 1){
                            linetime[n][1] = 1;
                        } else{
                            linetime[n][1] = 0;
                        }
                    }
                } else if(s1.getLine()[1][0] == s2.getLine()[0][0]){
                    linetime[n][0] = s1.getLine()[1][0];
                    int t = s1.getLine()[1][1] - s2.getLine()[0][1];
                    if(t < 0){
                        if(t == -1){
                            linetime[n][1] = 0;
                        } else{ // 1호선과 6호선의 경우 순환선이라 마지막역과 처음역 간 이동 가능
                            linetime[n][1] = 1;
                        }
                    } else{
                        if(t == 1){
                            linetime[n][1] = 1;
                        } else{
                            linetime[n][1] = 0;
                        }
                    }
                } else if(s1.getLine()[1][0] == s2.getLine()[1][0]){
                    linetime[n][0] = s1.getLine()[1][0];
                    int t = s1.getLine()[1][1] - s2.getLine()[1][1];
                    if(t < 0){
                        if(t == -1){
                            linetime[n][1] = 0;
                        } else{ // 1호선과 6호선의 경우 순환선이라 마지막역과 처음역 간 이동 가능
                            linetime[n][1] = 1;
                        }
                    } else{
                        if(t == 1){
                            linetime[n][1] = 1;
                        } else{
                            linetime[n][1] = 0;
                        }
                    }
                }
                if(n == sum_t){
                    break;
                }
            }
        }
        return linetime;
    }

    public int[] wait_time(){
        LocalTime now = LocalTime.now();
        int[] wait = new int[sum_t+1];
        int[][] line_time = getLineTime();
        int now_sec;
        if(now.getHour()+9 > 23){
            now_sec = (now.getHour()+9-24)*360 + now.getMinute()*6;
        }else{
            now_sec = now.getHour()*360 + now.getMinute()*6;
        }
        String[] ride_st = new String[sum_t+1];
        ride_st[0] = start;
        for(int i = 1; i < sum_t+1; i++){
            ride_st[i] = trans[i-1];
        }
        StationInfo s = new StationInfo();
        int[] b = between_time();
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
            now_sec += wait[i] + between_time()[i]/10;
        }

        return wait;
    }
    public int[] time_index(int now_time){
        int[] t = new int[sum_t+1];
        int[][] line_time = getLineTime();
        int now_sec;
        String[] ride_st = new String[sum_t+1];
        ride_st[0] = start;
        for(int i = 1; i < sum_t+1; i++){
            ride_st[i] = trans[i-1];
        }
        StationInfo s = new StationInfo();
        int[] b = between_time();
        int[] wait = wait_time();
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
            now_sec += wait[i] + between_time()[i]/10;
        }

        return t;
    }
    public int[] time_index(){
        int[] t = new int[sum_t+1];
        int[][] line_time = getLineTime();
        LocalTime now = LocalTime.now();
        int now_sec;
        if(now.getHour()+9 > 23){
            now_sec = (now.getHour()+9-24)*360 + now.getMinute()*6;
        }else{
            now_sec = now.getHour()*360 + now.getMinute()*6;
        }
//        System.out.println("hour: "+now.getHour()+"  minute: "+now.getMinute());
        String[] ride_st = new String[sum_t+1];
        ride_st[0] = start;
        for(int i = 1; i < sum_t+1; i++){
            ride_st[i] = trans[i-1];
        }
        StationInfo s = new StationInfo();
        int[] b = between_time();
        int[] wait = wait_time();
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
            now_sec += wait[i] + between_time()[i]/10;
        }

        return t;
    }

    public int[] between_time(){
        int[] t = new int[sum_t+1];
        String[] ride_st = new String[sum_t+2];
        ride_st[0] = start;
        for(int i = 1; i < sum_t+1; i++){
            ride_st[i] = trans[i-1];
        }
        ride_st[sum_t+1] = end;
        Dijkstra d = new Dijkstra();
        for(int i = 0; i < sum_t + 1; i++) {
            if(type == 0){
                d.check1(ride_st[i],ride_st[i+1]);
            } else if(type == 1){
                d.check2(ride_st[i],ride_st[i+1]);
            } else if(type == 2){
                d.check3(ride_st[i],ride_st[i+1]);
            }
            t[i] = d.getAtime();
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
//        for(int i = 0; i < sum_t; i++){
//            System.out.println(i+"번째 환승역:  "+cc[i]);
//        }
        setCc(cc);
        setSum_t(sum_t);
    }

    void check(String start, String finish, int now_time){
        int i, j; /* i, j, = for문을 위해 생성
				  	 s = 시작노드 입력값
				  	 e = 끝 노드 입력값*/
        int[] k = new int[3];
        int[] min = new int[3];//min = 최소값을 찾기 위함
        int s = station_index.indexOf(start); // 역이름에 맞는 인덱스 반환
        int e = station_index.indexOf(finish);// 역이름에 맞는 인덱스 반환
        int[][] v = new int[3][111]; //이동하는 최단 거리 확인
        int[][] distance = new int[3][111];//지나간 노드 확인
        int[][] distance1 = new int[3][111];//지나간 노드 확인
        int[][] distance2 = new int[3][111];//지나간 노드 확인
        int[][] via = new int[3][111]; //지나간 노드를 오름차순으로 정렬해서 저장
        for (j = 0; j < 111; j++) {
            v[0][j] = 0;//초기화
            v[1][j] = 0;//초기화
            v[2][j] = 0;//초기화
            distance1[0][j] = INF; //j의 거리를 아직 연결되지 않음을 설정
            distance[1][j] = INF; //j의 거리를 아직 연결되지 않음을 설정
            distance2[2][j] = INF; //j의 거리를 아직 연결되지 않음을 설정
        }
        distance1[0][s] = 0;
        distance[1][s] = 0;
        distance2[2][s] = 0;

        for (i = 0; i < 111; i++) {
            min[0] = INF;  //최소값을 아직 연결되지 않음을 설정
            min[1] = INF;
            min[2] = INF;
            for (j = 0; j < 111; j++) {
                if (v[0][j] == 0 && distance1[0][j] < min[0])
                //이웃한 노드 중 방문하지 않은 노드일 경우
                {
                    k[0] = j;//최단거리 노드번호를 k에 저장
                    min[0] = distance1[0][j];//min에 최단거리를 장
                }
                if (v[1][j] == 0 && distance[1][j] < min[1])
                //이웃한 노드 중 방문하지 않은 노드일 경우
                {
                    k[1] = j;//최단거리 노드번호를 k에 저장
                    min[1] = distance[1][j];//min에 최단거리를 장
                }
                if (v[2][j] == 0 && distance2[2][j] < min[2])
                //이웃한 노드 중 방문하지 않은 노드일 경우
                {
                    k[2] = j;//최단거리 노드번호를 k에 저장
                    min[2] = distance2[2][j];//min에 최단거리를 장
                }
            }

            v[0][k[0]] = 1;
            v[1][k[1]] = 1;
            v[2][k[2]] = 1;
            if (min[0] == INF)
                break;
            if (min[1] == INF)
                break;

            for (j = 0; j < 111; j++) {
                if (distance1[0][j] > distance1[0][k[0]] + time[k[0]][j]) {
                    distance[0][j] = distance[0][k[0]] + dist[k[0]][j];
                    distance1[0][j] = distance1[0][k[0]] + time[k[0]][j];
                    distance2[0][j] = distance2[0][k[0]] + cost[k[0]][j];
                    via[0][j] = k[0];
                }
            }
            for (j = 0; j < 111; j++) {
                if (distance[1][j] > distance[1][k[1]] + dist[k[1]][j]) {
                    distance[1][j] = distance[1][k[1]] + dist[k[1]][j];
                    distance1[1][j] = distance1[1][k[1]] + time[k[1]][j];
                    distance2[1][j] = distance2[1][k[1]] + cost[k[1]][j];
                    via[1][j] = k[1];
                }
            }
            for (j = 0; j < 111; j++) {
                if (distance2[2][j] > distance2[2][k[2]] + cost[k[2]][j]) {
                    distance[2][j] = distance[2][k[2]] + dist[k[2]][j];
                    distance1[2][j] = distance1[2][k[2]] + time[k[2]][j];
                    distance2[2][j] = distance2[2][k[2]] + cost[k[2]][j];
                    via[2][j] = k[2];
                }
            }
        }

        km = distance[0][e];
        setKm(km);
        atime = distance1[0][e];
        setAtime(atime);
        charge = distance2[0][e];
        setCharge(charge);
        int path[] = new int[111];
        int path_cnt = 0;
        k[0] = e;
        while (true) {
            path[path_cnt++] = k[0];
            if (k[0] == s)
                break;
            k[0] = via[0][k[0]];
        }
        path[path_cnt] = -1;
        trans_station(path, path_cnt, s, e);
        String[] t = new String[sum_t];
        for(int n = 0; n < sum_t; n++){
            t[n] = cc[n];
        }
        b_time = new Data(atime, km, charge, start, finish, t, sum_t, path, path_cnt, 0);
        atime = distance1[1][e];
        km = distance[1][e];
        charge = distance2[1][e];
        path = new int[111];
        path_cnt = 0;
        k[1] = e;
        while (true) {
            path[path_cnt++] = k[1];
            if (k[1] == s)
                break;
            k[1] = via[1][k[1]];
        }
        path[path_cnt] = -1;
        trans_station(path, path_cnt, s, e);
        t = new String[sum_t];
        for(int n = 0; n < sum_t; n++){
            t[n] = cc[n];
        }
        b_dist = new Data(atime, km, charge, start, finish, t, sum_t, path, path_cnt, 1);

        atime = distance1[2][e];
        km = distance[2][e];
        charge = distance2[2][e];
        path = new int[111];
        path_cnt = 0;
        k[2] = e;
        while (true) {
            path[path_cnt++] = k[2];
            if (k[2] == s)
                break;
            k[2] = via[2][k[2]];
        }
        path[path_cnt] = -1;

        trans_station(path, path_cnt, s, e);
        t = new String[sum_t];
        for(int n = 0; n < sum_t; n++){
            t[n] = cc[n];
        }
        b_charge = new Data(atime, km, charge, start, finish, cc, sum_t, path, path_cnt, 2);
        density.addRecord(b_time, b_dist, b_charge, now_time);
    }
    void check(String start, String finish){
        int i, j; /* i, j, = for문을 위해 생성
				  	 s = 시작노드 입력값
				  	 e = 끝 노드 입력값*/
        int[] k = new int[3];
        int[] min = new int[3];//min = 최소값을 찾기 위함
        int s = station_index.indexOf(start); // 역이름에 맞는 인덱스 반환
        int e = station_index.indexOf(finish);// 역이름에 맞는 인덱스 반환
        int[][] v = new int[3][111]; //이동하는 최단 거리 확인
        int[][] distance = new int[3][111];//지나간 노드 확인
        int[][] distance1 = new int[3][111];//지나간 노드 확인
        int[][] distance2 = new int[3][111];//지나간 노드 확인
        int[][] via = new int[3][111]; //지나간 노드를 오름차순으로 정렬해서 저장
        for (j = 0; j < 111; j++) {
            v[0][j] = 0;//초기화
            v[1][j] = 0;//초기화
            v[2][j] = 0;//초기화
            distance1[0][j] = INF; //j의 거리를 아직 연결되지 않음을 설정
            distance[1][j] = INF; //j의 거리를 아직 연결되지 않음을 설정
            distance2[2][j] = INF; //j의 거리를 아직 연결되지 않음을 설정
        }
        distance1[0][s] = 0;
        distance[1][s] = 0;
        distance2[2][s] = 0;

        for (i = 0; i < 111; i++) {
            min[0] = INF;  //최소값을 아직 연결되지 않음을 설정
            min[1] = INF;
            min[2] = INF;
            for (j = 0; j < 111; j++) {
                if (v[0][j] == 0 && distance1[0][j] < min[0])
                //이웃한 노드 중 방문하지 않은 노드일 경우
                {
                    k[0] = j;//최단거리 노드번호를 k에 저장
                    min[0] = distance1[0][j];//min에 최단거리를 장
                }
                if (v[1][j] == 0 && distance[1][j] < min[1])
                //이웃한 노드 중 방문하지 않은 노드일 경우
                {
                    k[1] = j;//최단거리 노드번호를 k에 저장
                    min[1] = distance[1][j];//min에 최단거리를 장
                }
                if (v[2][j] == 0 && distance2[2][j] < min[2])
                //이웃한 노드 중 방문하지 않은 노드일 경우
                {
                    k[2] = j;//최단거리 노드번호를 k에 저장
                    min[2] = distance2[2][j];//min에 최단거리를 장
                }
            }

            v[0][k[0]] = 1;
            v[1][k[1]] = 1;
            v[2][k[2]] = 1;
            if (min[0] == INF)
                break;
            if (min[1] == INF)
                break;

            for (j = 0; j < 111; j++) {
                if (distance1[0][j] > distance1[0][k[0]] + time[k[0]][j]) {
                    distance[0][j] = distance[0][k[0]] + dist[k[0]][j];
                    distance1[0][j] = distance1[0][k[0]] + time[k[0]][j];
                    distance2[0][j] = distance2[0][k[0]] + cost[k[0]][j];
                    via[0][j] = k[0];
                }
            }
            for (j = 0; j < 111; j++) {
                if (distance[1][j] > distance[1][k[1]] + dist[k[1]][j]) {
                    distance[1][j] = distance[1][k[1]] + dist[k[1]][j];
                    distance1[1][j] = distance1[1][k[1]] + time[k[1]][j];
                    distance2[1][j] = distance2[1][k[1]] + cost[k[1]][j];
                    via[1][j] = k[1];
                }
            }
            for (j = 0; j < 111; j++) {
                if (distance2[2][j] > distance2[2][k[2]] + cost[k[2]][j]) {
                    distance[2][j] = distance[2][k[2]] + dist[k[2]][j];
                    distance1[2][j] = distance1[2][k[2]] + time[k[2]][j];
                    distance2[2][j] = distance2[2][k[2]] + cost[k[2]][j];
                    via[2][j] = k[2];
                }
            }
        }

        km = distance[0][e];
        setKm(km);
        atime = distance1[0][e];
        setAtime(atime);
        charge = distance2[0][e];
        setCharge(charge);
        int path[] = new int[111];
        int path_cnt = 0;
        k[0] = e;
        while (true) {
            path[path_cnt++] = k[0];
            if (k[0] == s)
                break;
            k[0] = via[0][k[0]];
        }
        path[path_cnt] = -1;
        trans_station(path, path_cnt, s, e);
        String[] t = new String[sum_t];
        for(int n = 0; n < sum_t; n++){
            t[n] = cc[n];
        }
        b_time = new Data(atime, km, charge, start, finish, t, sum_t, path, path_cnt, 0);
        atime = distance1[1][e];
        km = distance[1][e];
        charge = distance2[1][e];
        path = new int[111];
        path_cnt = 0;
        k[1] = e;
        while (true) {
            path[path_cnt++] = k[1];
            if (k[1] == s)
                break;
            k[1] = via[1][k[1]];
        }
        path[path_cnt] = -1;
        trans_station(path, path_cnt, s, e);
        t = new String[sum_t];
        for(int n = 0; n < sum_t; n++){
            t[n] = cc[n];
        }
        b_dist = new Data(atime, km, charge, start, finish, t, sum_t, path, path_cnt, 1);

        atime = distance1[2][e];
        km = distance[2][e];
        charge = distance2[2][e];
        path = new int[111];
        path_cnt = 0;
        k[2] = e;
        while (true) {
            path[path_cnt++] = k[2];
            if (k[2] == s)
                break;
            k[2] = via[2][k[2]];
        }
        path[path_cnt] = -1;

        trans_station(path, path_cnt, s, e);
        t = new String[sum_t];
        for(int n = 0; n < sum_t; n++){
            t[n] = cc[n];
        }
        b_charge = new Data(atime, km, charge, start, finish, cc, sum_t, path, path_cnt, 2);
        density.addRecord(b_time, b_dist, b_charge);
//        System.out.println("what " + b_time.getSum_t());
//        for(int t = 0; t < b_time.getSum_t(); t++){
//            System.out.println( b_time.getTrans()[t]);
//        }
//        System.out.println("where " + b_time.getPath_cnt());
//        for(int t = 0; t < b_time.getPath_cnt(); t++){
//            System.out.println(b_time.getPath()[t]);
//        }
    }

    void no_record_check(String start, String finish){
        int i, j; /* i, j, = for문을 위해 생성
				  	 s = 시작노드 입력값
				  	 e = 끝 노드 입력값*/
        int[] k = new int[3];
        int[] min = new int[3];//min = 최소값을 찾기 위함
        int s = station_index.indexOf(start); // 역이름에 맞는 인덱스 반환
        int e = station_index.indexOf(finish);// 역이름에 맞는 인덱스 반환
        int[][] v = new int[3][111]; //이동하는 최단 거리 확인
        int[][] distance = new int[3][111];//지나간 노드 확인
        int[][] distance1 = new int[3][111];//지나간 노드 확인
        int[][] distance2 = new int[3][111];//지나간 노드 확인
        int[][] via = new int[3][111]; //지나간 노드를 오름차순으로 정렬해서 저장
        for (j = 0; j < 111; j++) {
            v[0][j] = 0;//초기화
            v[1][j] = 0;//초기화
            v[2][j] = 0;//초기화
            distance1[0][j] = INF; //j의 거리를 아직 연결되지 않음을 설정
            distance[1][j] = INF; //j의 거리를 아직 연결되지 않음을 설정
            distance2[2][j] = INF; //j의 거리를 아직 연결되지 않음을 설정
        }
        distance1[0][s] = 0;
        distance[1][s] = 0;
        distance2[2][s] = 0;

        for (i = 0; i < 111; i++) {
            min[0] = INF;  //최소값을 아직 연결되지 않음을 설정
            min[1] = INF;
            min[2] = INF;
            for (j = 0; j < 111; j++) {
                if (v[0][j] == 0 && distance1[0][j] < min[0])
                //이웃한 노드 중 방문하지 않은 노드일 경우
                {
                    k[0] = j;//최단거리 노드번호를 k에 저장
                    min[0] = distance1[0][j];//min에 최단거리를 장
                }
                if (v[1][j] == 0 && distance[1][j] < min[1])
                //이웃한 노드 중 방문하지 않은 노드일 경우
                {
                    k[1] = j;//최단거리 노드번호를 k에 저장
                    min[1] = distance[1][j];//min에 최단거리를 장
                }
                if (v[2][j] == 0 && distance2[2][j] < min[2])
                //이웃한 노드 중 방문하지 않은 노드일 경우
                {
                    k[2] = j;//최단거리 노드번호를 k에 저장
                    min[2] = distance2[2][j];//min에 최단거리를 장
                }
            }

            v[0][k[0]] = 1;
            v[1][k[1]] = 1;
            v[2][k[2]] = 1;
            if (min[0] == INF)
                break;
            if (min[1] == INF)
                break;

            for (j = 0; j < 111; j++) {
                if (distance1[0][j] > distance1[0][k[0]] + time[k[0]][j]) {
                    distance[0][j] = distance[0][k[0]] + dist[k[0]][j];
                    distance1[0][j] = distance1[0][k[0]] + time[k[0]][j];
                    distance2[0][j] = distance2[0][k[0]] + cost[k[0]][j];
                    via[0][j] = k[0];
                }
            }
            for (j = 0; j < 111; j++) {
                if (distance[1][j] > distance[1][k[1]] + dist[k[1]][j]) {
                    distance[1][j] = distance[1][k[1]] + dist[k[1]][j];
                    distance1[1][j] = distance1[1][k[1]] + time[k[1]][j];
                    distance2[1][j] = distance2[1][k[1]] + cost[k[1]][j];
                    via[1][j] = k[1];
                }
            }
            for (j = 0; j < 111; j++) {
                if (distance2[2][j] > distance2[2][k[2]] + cost[k[2]][j]) {
                    distance[2][j] = distance[2][k[2]] + dist[k[2]][j];
                    distance1[2][j] = distance1[2][k[2]] + time[k[2]][j];
                    distance2[2][j] = distance2[2][k[2]] + cost[k[2]][j];
                    via[2][j] = k[2];
                }
            }
        }

        km = distance[0][e];
        setKm(km);
        atime = distance1[0][e];
        setAtime(atime);
        charge = distance2[0][e];
        setCharge(charge);
        int path[] = new int[111];
        int path_cnt = 0;
        k[0] = e;
        while (true) {
            path[path_cnt++] = k[0];
            if (k[0] == s)
                break;
            k[0] = via[0][k[0]];
        }
        path[path_cnt] = -1;
        trans_station(path, path_cnt, s, e);
        String[] t = new String[sum_t];
        for(int n = 0; n < sum_t; n++){
            t[n] = cc[n];
        }
        b_time = new Data(atime, km, charge, start, finish, t, sum_t, path, path_cnt, 0);
        atime = distance1[1][e];
        km = distance[1][e];
        charge = distance2[1][e];
        path = new int[111];
        path_cnt = 0;
        k[1] = e;
        while (true) {
            path[path_cnt++] = k[1];
            if (k[1] == s)
                break;
            k[1] = via[1][k[1]];
        }
        path[path_cnt] = -1;
        trans_station(path, path_cnt, s, e);
        t = new String[sum_t];
        for(int n = 0; n < sum_t; n++){
            t[n] = cc[n];
        }
        b_dist = new Data(atime, km, charge, start, finish, t, sum_t, path, path_cnt, 1);

        atime = distance1[2][e];
        km = distance[2][e];
        charge = distance2[2][e];
        path = new int[111];
        path_cnt = 0;
        k[2] = e;
        while (true) {
            path[path_cnt++] = k[2];
            if (k[2] == s)
                break;
            k[2] = via[2][k[2]];
        }
        path[path_cnt] = -1;

        trans_station(path, path_cnt, s, e);
        t = new String[sum_t];
        for(int n = 0; n < sum_t; n++){
            t[n] = cc[n];
        }
        b_charge = new Data(atime, km, charge, start, finish, cc, sum_t, path, path_cnt, 2);
//        System.out.println("what " + b_time.getSum_t());
//        for(int t = 0; t < b_time.getSum_t(); t++){
//            System.out.println( b_time.getTrans()[t]);
//        }
//        System.out.println("where " + b_time.getPath_cnt());
//        for(int t = 0; t < b_time.getPath_cnt(); t++){
//            System.out.println(b_time.getPath()[t]);
//        }
    }
    void check(String start, String via, String end){
        int s = station_index.indexOf(start);
        int e = station_index.indexOf(end);

        check(start, via);
        for(int i=0; this.cc[i]!=null;i++){
            if(this.cc[i+1]==null){
                transfer_list[0]=this.cc[i];
                transfer_list[1]="0";
            }
        }
        Data first_time = getBTime();
        Data first_dist = getBDist();
        Data  first_charge = getBCharge();
        check(via, end);
        if(transfer_list[0]==null){
            transfer_list[0]="0";
            transfer_list[1]=this.cc[0];
        }
        b_time = datasquash(first_time, getBTime(), 0);
        b_dist = datasquash(first_dist, getBDist(), 1);
        b_charge = datasquash(first_charge, getBCharge(), 2);
        System.out.println("환승 몇번: " + b_time.getSum_t());
        for(int i = 0; i < b_time.getSum_t(); i++){
            System.out.println(b_time.getTrans()[i]);
        }
    }
    void check(String start, String via, String end, int now_time){
        int s = station_index.indexOf(start);
        int e = station_index.indexOf(end);

        no_record_check(start, via);
        for(int i=0; this.cc[i]!=null;i++){
            if(this.cc[i+1]==null){
                transfer_list[0]=this.cc[i];
                transfer_list[1]="0";
            }
        }
        Data first_time = getBTime();
        Data first_dist = getBDist();
        Data  first_charge = getBCharge();
        no_record_check(via, end);
        if(transfer_list[0]==null){
            transfer_list[0]="0";
            transfer_list[1]=this.cc[0];
        }
        b_time = datasquash(first_time, getBTime(), 0);
        b_dist = datasquash(first_dist, getBDist(), 1);
        b_charge = datasquash(first_charge, getBCharge(), 2);
        System.out.println("환승 몇번: " + b_time.getSum_t());
        for(int i = 0; i < b_time.getSum_t(); i++){
            System.out.println(b_time.getTrans()[i]);
        }
        density.addRecord(b_time, b_dist, b_charge, now_time);
    }
    int[] pathappend(int[] a, int alen, int[] b, int blen){
        int[] c = new int[alen + blen];
        System.arraycopy(a, 0, c, 0, alen);
        System. arraycopy(b, 1, c, alen, blen-1);
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
                trans[i] = a.getTrans()[i];
            } else if(i == a.getSum_t()){
                trans[i] = a.getEnd();
            } else{
              trans[i] = b.getTrans()[i-a.getSum_t()-1];
            }
        }
        int sum_t = a.getSum_t()+b.getSum_t()+1;
        return new Data(time, dist, charge, start, end, trans, sum_t, path, path_cnt, type);
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
        setKm(km);
        atime = distance1[e];
        setAtime(atime);
        charge = distance2[e];
        setCharge(charge);
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
        trans_station(path, path_cnt, s, e);
        b_time = new Data(atime, km, charge, start, finish, cc, sum_t, path, path_cnt, 0);
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
        setKm(km);
        atime = distance1[e];
        setAtime(atime);
        charge = distance2[e];
        setCharge(charge);
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

        trans_station(path, path_cnt, s, e);
        b_dist = new Data(atime, km, charge, start, finish, cc, sum_t, path, path_cnt, 1);
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
        setKm(km);
        atime = distance1[e];
        setAtime(atime);
        charge = distance2[e];
        setCharge(charge);
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

        trans_station(path, path_cnt, s, e);
        b_charge = new Data(atime, km, charge, start, finish, cc, sum_t, path, path_cnt, 2);
    }
}