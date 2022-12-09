package com.example.myapplication;


import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.ArrayList;
import java.util.HashMap;


class station {
    String name;
    int index;
    int[][] line = new int[2][2];
    long[][] uptime = new long[2][36];
    long[][] downtime = new long[2][36];
    HashMap<Integer, HashMap<Integer, HashMap<Integer, Float>>> value = new HashMap<>(); // 밀집도를 위한 변수

    public station() {
    }

    public station(String name) {
        this.name = name;
        StationInfo s = new StationInfo();
        index = s.getIndexOfStation().indexOf(name);
        init_line();
        init_time();
    }

    public station(int index) {
        StationInfo s = new StationInfo();
        name = s.getStationList()[index];
        this.index = index;
        init_line();
        init_time();
    }

    public station(String name, int index) {
        this.name = name;
        this.index = index;
        init_line();
        init_time();
    }
    public void init_line(){
        line[0][0] = -1;
        line[1][0] = -1;
    }

    public void init_time() {
        for (int i = 0; i < 36; i++) {
            uptime[0][i] = -1;
            uptime[1][i] = -1;
            downtime[0][i] = -1;
            downtime[1][i] = -1;
        }
    }
    public void setLine(int l, int n){
        if(line[0][0] == -1){
            line[0][0] = l;
            line[0][1] = n;
        } else{
            line[1][0] = l;
            line[1][1] = n;
        }
    }
    public int[][] getLine(){
        return line;
    }

    public void setUptime(int line, long[] up){
        if (this.line[0][0] == line) {
            uptime[0] = up;
        } else {
            uptime[1] = up;
        }
    }
    public void setUptime(int line, int k, long time){
        if(this.line[0][0] == line){
            uptime[0][k] = time;
        } else{
            uptime[1][k] = time;
        }
    }
    public void setDowntime(int line, long[] down){
        if (this.line[0][0] == line) {
            downtime[0] = down;
        } else {
            downtime[1] = down;
        }
    }
    public void setDowntime(int line, int k, long time){
        if(this.line[0][0] == line){
            downtime[0][k] = time;
        } else{
            downtime[1][k] = time;
        }
    }
    public long[] getUptime(int line){
        if(this.line[0][0] == line){
            return uptime[0];
        }
        else return uptime[1];
    }
    public long[] getDowntime(int line){
        if(this.line[0][0] == line){
            return downtime[0];
        }
        else return downtime[1];
    }

    public void addValue(int line, int ud, int index, float v){
        if(value.containsKey(line)){
            HashMap<Integer, HashMap<Integer, Float>> t1 = value.get(line);
            if(t1.containsKey(ud)){
                HashMap<Integer, Float> t2 = t1.get(ud);
                if(t2.containsKey(index)){
                    float f = t2.get(index);
                    value.get(line).get(ud).put(index,f+v);
                } else{
                    value.get(line).get(ud).put(index,v);

                }
            } else{
                HashMap<Integer, Float> t2 = new HashMap<>();
                value.get(line).put(ud, t2);
                t2.put(index, v);
            }
        } else{
            HashMap<Integer, Float> t1 = new HashMap<>();
            HashMap<Integer, HashMap<Integer, Float>> t2 = new HashMap<>();
            value.put(line, t2);
            t2.put(ud, t1);
            t1.put(index, v);
        }
    }

    public HashMap<Integer, HashMap<Integer, HashMap<Integer, Float>>> getValue() {
        return value;
    }
}
public class StationInfo {
    ArrayList<String> station_index = new ArrayList<>();
    String[] station_list = new String[111];
    static int[] station_info = { // 역간 이동시 소모 비용 정보가 들어있는 배열, 5개 단위로 0,1은 이동하는 두 역, 2,3,4는 시간, 거리, 비용을 의미함
            101, 102, 200, 500, 200,
            102, 103, 300, 400, 300,
            103, 104, 1000, 600, 500,
            104, 105, 500, 200, 340,
            105, 106, 150, 600, 450,
            106, 107, 320, 200, 120,
            107, 108, 400, 700, 650,
            108, 109, 800, 350, 200,
            109, 110, 900, 250, 430,
            110, 111, 500, 650, 120,
            111, 112, 1000, 400, 890,
            112, 113, 2000, 500, 800,
            113, 114, 500, 500, 700,
            114, 115, 220, 400, 540,
            115, 116, 230, 600, 330,
            116, 117, 300, 200, 280,
            117, 118, 500, 600, 800,
            118, 119, 480, 200, 1000,
            119, 120, 500, 700, 2000,
            120, 121, 400, 350, 700,
            121, 122, 900, 250, 650,
            122, 123, 300, 650, 440,
            123, 101, 480, 400, 200,
            101, 201, 1000, 500, 300,
            201, 202, 250, 500, 500,
            202, 203, 480, 400, 340,
            203, 204, 400, 600, 450,
            204, 205, 250, 200, 120,
            205, 206, 500, 600, 650,
            206, 207, 320, 200, 200,
            207, 208, 250, 700, 430,
            208, 209, 300, 350, 120,
            209, 210, 150, 250, 890,
            210, 211, 900, 650, 800,
            211, 212, 320, 400, 700,
            212, 213, 150, 500, 540,
            213, 214, 500, 500, 330,
            214, 215, 210, 400, 280,
            215, 216, 150, 600, 800,
            216, 217, 500, 200, 1000,
            207, 301, 300, 600, 2000,
            301, 302, 300, 200, 700,
            302, 303, 480, 700, 650,
            303, 304, 400, 350, 440,
            304, 123, 250, 250, 200,
            123, 305, 300, 650, 300,
            305, 306, 250, 400, 500,
            306, 307, 900, 500, 340,
            307, 308, 480, 500, 450,
            308, 107, 400, 400, 120,
            104, 401, 1000, 600, 650,
            401, 307, 150, 200, 200,
            307, 402, 300, 600, 430,
            402, 403, 210, 200, 120,
            403, 404, 320, 700, 890,
            404, 405, 210, 350, 800,
            405, 406, 500, 250, 700,
            406, 407, 300, 650, 540,
            407, 115, 320, 400, 330,
            115, 408, 480, 500, 280,
            408, 409, 300, 340, 800,
            409, 410, 480, 500, 1000,
            410, 411, 300, 400, 2000,
            411, 412, 900, 600, 700,
            412, 413, 400, 200, 650,
            413, 414, 430, 600, 440,
            414, 415, 150, 200, 200,
            415, 416, 1000, 700, 300,
            416, 417, 500, 350, 500,
            417, 216, 900, 250, 340,
            209, 501, 320, 650, 450,
            501, 502, 320, 400, 120,
            502, 503, 430, 500, 650,
            503, 504, 210, 500, 200,
            504, 122, 320, 400, 430,
            122, 505, 480, 600, 120,
            505, 506, 300, 200, 890,
            506, 403, 320, 600, 800,
            403, 507, 300, 200, 700,
            507, 109, 1000, 700, 540,
            601, 602, 150, 350, 330,
            602, 121, 700, 250, 280,
            121, 603, 500, 650, 800,
            603, 604, 300, 400, 1000,
            604, 605, 430, 200, 2000,
            605, 606, 480, 300, 700,
            606, 116, 320, 400, 650,
            116, 607, 250, 200, 440,
            607, 608, 500, 600, 200,
            608, 609, 700, 200, 300,
            609, 412, 320, 700, 500,
            412, 610, 1000, 350, 340,
            610, 611, 700, 250, 450,
            611, 612, 700, 650, 120,
            612, 613, 150, 400, 650,
            613, 614, 430, 200, 200,
            614, 615, 500, 300, 430,
            615, 616, 700, 400, 120,
            616, 417, 480, 200, 890,
            417, 617, 320, 600, 800,
            617, 618, 300, 200, 700,
            618, 619, 250, 700, 540,
            619, 620, 700, 350, 330,
            620, 621, 320, 250, 280,
            621, 622, 480, 650, 800,
            622, 601, 150, 400, 1000,
            202, 303, 1000, 200, 2000,
            303, 503, 700, 300, 700,
            503, 601, 500, 400, 650,
            601, 701, 430, 200, 440,
            701, 702, 150, 600, 200,
            702, 703, 600, 200, 300,
            703, 704, 700, 700, 500,
            704, 705, 250, 350, 340,
            705, 706, 600, 250, 450,
            706, 416, 300, 650, 120,
            416, 707, 430, 400, 650,
            707, 614, 480, 200, 200,
            113, 801, 600, 300, 430,
            801, 802, 1000, 400, 120,
            802, 803, 700, 200, 890,
            803, 409, 600, 600, 800,
            409, 608, 500, 200, 700,
            608, 804, 700, 700, 540,
            804, 805, 150, 350, 330,
            805, 806, 210, 250, 280,
            806, 705, 600, 650, 800,
            705, 618, 250, 400, 1000,
            618, 214, 700, 200, 2000,
            112, 901, 600, 300, 700,
            901, 406, 300, 400, 650,
            406, 605, 210, 200, 440,
            605, 902, 480, 600, 280,
            902, 119, 430, 200, 800,
            119, 903, 1000, 700, 1000,
            903, 702, 150, 350, 2000,
            702, 904, 500, 250, 700,
            904, 621, 250, 650, 650,
            621, 211, 300, 400, 440
    };
    int[][] station_cost = new int[139][3]; // [i][0,1,2]에 0은 시간, 1은 거리, 2는 비용이 저장된다.
    String[][] station_set = new String[139][2]; // [i][0,1] 에 0과 1에 서로 이동하는 역이 저장된다. 예를 들어 101에서 102역으로 이동가능 하다면 101과 102가 들어있음
    int[][] station_line = new int[111][2]; // 각 역이 어떤 호선에 포함돼있는지 저장하는 배열이다. 환승역이 아닐경우 두번째 요소는 0을 가진다.
    static int[] trans_station = {
            0, 3, 6, 8, 11, 12, 14, 15, 18, 20, 21, 22,
            24, 29, 31, 33, 36, 38, 42, 46, 50, 53, 56, 59,
            63, 64, 67, 72, 76, 79, 85, 89, 92, 95, 98
    }; // 35개의 환승역이 존재한다. 첫 번째 배열은 환승역의 이름, 두 번째는 환승역의 인덱스이다.
    static int[] line_num = {23, 18, 11, 21, 11, 26, 13, 12, 11}; // 노선에 존재하는 역 개수 저장.
//    boolean[] is_cycle = {true, false, false, false, false, true, false, false, false}; // 각 노선이 순환선인지를 표시하는 배열. 순환선이면 true
    station[] st = new station[111];


    Line line = new Line();
    startTime time = new startTime();

    class startTime{
        // 각 노선의 종점 시간표
        long[][] uptime = new long[9][36]; // 상행선 시간표
        long[][] downtime = new long[9][36]; // 하행선 시간표

        public startTime(){
            for(int i = 0; i < 9; i++){
                long time = 2160;
                for(int j = 0; j < 36; j++){
                    uptime[i][j] = time;
                    downtime[i][j] = time;
                    time += 180;
                }
            }
        }

        public long[][] getUptime(){
            return uptime;
        }
        public long[] getUptime(int line){
            return uptime[line];
        }
        public long[][] getDowntime(){
            return downtime;
        }
        public long[] getDowntime(int line){
            return downtime[line];
        }
    }
    class Line {
        int[] line_num = {23, 18, 11, 21, 11, 26, 13, 12, 11};
        int[][] line_st = new int[9][];

        public Line() {
            for (int i = 0; i < 9; i++) {
                line_st[i] = new int[line_num[i]];
            }
            for (int i = 0; i < 9; i++) {
                if (i == 0) {
                    for (int j = 0; j < 26; j++) {
                        if (j < 23) {
                            line_st[i][j] = j;
                        }
                    }
                }
                if (i == 1) {
                    int temp = 23;
                    for (int j = 0; j < 26; j++) {
                        if (j == 0) {
                            line_st[i][j] = 0;
                        } else if (j < 18) {
                            line_st[i][j] = temp++;
                        }
                    }
                }
                if (i == 2) {
                    int temp = 40;
                    for (int j = 0; j < 26; j++) {
                        if (j == 0) {
                            line_st[i][j] = 29;
                        } else if (j == 5) {
                            line_st[i][j] = 22;
                        } else if (j == 10) {
                            line_st[i][j] = 6;
                        } else if (j < 11) {
                            line_st[i][j] = temp++;
                        }
                    }
                }
                if (i == 3) {
                    int temp = 48;
                    for (int j = 0; j < 26; j++) {
                        if (j == 0) {
                            line_st[i][j] = 3;
                        } else if (j == 2) {
                            line_st[i][j] = 46;
                        } else if (j == 9) {
                            line_st[i][j] = 14;
                        } else if (j == 20) {
                            line_st[i][j] = 38;
                        } else if (j < 21) {
                            line_st[i][j] = temp++;
                        }
                    }
                }
                if (i == 4) {
                    int temp = 65;
                    for (int j = 0; j < 26; j++) {
                        if (j == 0) {
                            line_st[i][j] = 31;
                        } else if (j == 5) {
                            line_st[i][j] = 21;
                        } else if (j == 8) {
                            line_st[i][j] = 50;
                        } else if (j == 10) {
                            line_st[i][j] = 8;
                        } else if (j < 11) {
                            line_st[i][j] = temp++;
                        }
                    }
                }
                if (i == 5) {
                    int temp = 72;
                    for (int j = 0; j < 26; j++) {
                        if (j == 2) {
                            line_st[i][j] = 20;
                        } else if (j == 7) {
                            line_st[i][j] = 15;
                        } else if (j == 11) {
                            line_st[i][j] = 59;
                        } else if (j == 19) {
                            line_st[i][j] = 64;
                        } else if (j < 26) {
                            line_st[i][j] = temp++;
                        }
                    }
                }
                if (i == 6) {
                    int temp = 94;
                    for (int j = 0; j < 26; j++) {
                        if (j == 0) {
                            line_st[i][j] = 24;
                        } else if (j == 1) {
                            line_st[i][j] = 42;
                        } else if (j == 2) {
                            line_st[i][j] = 67;
                        } else if (j == 3) {
                            line_st[i][j] = 72;
                        } else if (j == 10) {
                            line_st[i][j] = 63;
                        } else if (j == 12) {
                            line_st[i][j] = 85;
                        } else if (j < 13) {
                            line_st[i][j] = temp++;
                        }
                    }
                }
                if (i == 7) {
                    int temp = 101;
                    for (int j = 0; j < 26; j++) {
                        if (j == 0) {
                            line_st[i][j] = 12;
                        } else if (j == 4) {
                            line_st[i][j] = 56;
                        } else if (j == 5) {
                            line_st[i][j] = 79;
                        } else if (j == 9) {
                            line_st[i][j] = 98;
                        } else if (j == 10) {
                            line_st[i][j] = 89;
                        } else if (j == 11) {
                            line_st[i][j] = 36;
                        } else if (j < 12) {
                            line_st[i][j] = temp++;
                        }
                    }
                }
                if (i == 8) {
                    int temp = 107;
                    for (int j = 0; j < 26; j++) {
                        if (j == 0) {
                            line_st[i][j] = 11;
                        } else if (j == 2) {
                            line_st[i][j] = 53;
                        } else if (j == 3) {
                            line_st[i][j] = 76;
                        } else if (j == 5) {
                            line_st[i][j] = 18;
                        } else if (j == 7) {
                            line_st[i][j] = 95;
                        } else if (j == 9) {
                            line_st[i][j] = 92;
                        } else if (j == 10) {
                            line_st[i][j] = 33;
                        } else if (j < 11) {
                            line_st[i][j] = temp++;
                        }
                    }
                }
            }
        }

        public int[] getLine_num() {
            return line_num;
        }

        public int[][] getLine_st() {
            return line_st;
        }
    }



    public StationInfo(){
        int st_num = 101;
        int n = 0;
        for (int i = 0; i < 23; i++, n++) {
            station_index.add(String.valueOf(st_num));
            station_list[n] = String.valueOf(st_num);
            station_line[n][0] = 0; // 1호선에 포함됨
            station_line[n][1] = -1; // 기본적으로 하나의 호선에만 포함되므로 0 저장
            st[n] = new station(String.valueOf(st_num), n);
            st_num++;
        }
        st_num = 201;
        for (int i = 0; i < 17; i++, n++) {
            station_index.add(String.valueOf(st_num));
            station_list[n] = String.valueOf(st_num);
            station_line[n][0] = 1; // 2호선에 포함됨
            station_line[n][1] = -1;
            st[n] = new station(String.valueOf(st_num), n);
            st_num++;
        }
        st_num = 301;
        for (int i = 0; i < 8; i++, n++) {
            station_index.add(String.valueOf(st_num));
            station_list[n] = String.valueOf(st_num);
            station_line[n][0] = 2; // 3호선에 포함됨
            station_line[n][1] = -1;
            st[n] = new station(String.valueOf(st_num), n);
            st_num++;
        }
        st_num = 401;
        for (int i = 0; i < 17; i++, n++) {
            station_index.add(String.valueOf(st_num));
            station_list[n] = String.valueOf(st_num);
            station_line[n][0] = 3; // 4호선에 포함됨
            station_line[n][1] = -1;
            st[n] = new station(String.valueOf(st_num), n);
            st_num++;
        }
        st_num = 501;
        for (int i = 0; i < 7; i++, n++) {
            station_index.add(String.valueOf(st_num));
            station_list[n] = String.valueOf(st_num);
            station_line[n][0] = 4; // 5호선에 포함됨
            station_line[n][1] = -1;
            st[n] = new station(String.valueOf(st_num), n);
            st_num++;
        }
        st_num = 601;
        for (int i = 0; i < 22; i++, n++) {
            station_index.add(String.valueOf(st_num));
            station_list[n] = String.valueOf(st_num);
            station_line[n][0] = 5; // 6호선에 포함됨
            station_line[n][1] = -1;
            st[n] = new station(String.valueOf(st_num), n);
            st_num++;
        }
        st_num = 701;
        for (int i = 0; i < 7; i++, n++) {
            station_index.add(String.valueOf(st_num));
            station_list[n] = String.valueOf(st_num);
            station_line[n][0] = 6; // 7호선에 포함됨
            station_line[n][1] = -1;
            st[n] = new station(String.valueOf(st_num), n);
            st_num++;
        }
        st_num = 801;
        for (int i = 0; i < 6; i++, n++) {
            station_index.add(String.valueOf(st_num));
            station_list[n] = String.valueOf(st_num);
            station_line[n][0] = 7; // 8호선에 포함됨
            station_line[n][1] = -1;
            st[n] = new station(String.valueOf(st_num), n);
            st_num++;
        }
        st_num = 901;
        for (int i = 0; i < 4; i++, n++) {
            station_index.add(String.valueOf(st_num));
            station_list[n] = String.valueOf(st_num);
            station_line[n][0] = 8; // 9호선에 포함됨
            station_line[n][1] = -1;
            st[n] = new station(String.valueOf(st_num), n);
            st_num++;
        }

        for(int i = 0; i < 9; i++){
            for(int j = 0; j < line_num[i]; j++){
                st[line.getLine_st()[i][j]].setLine(i, j);
            }
        }


        station_line[0][1] = 1; // 환승역이 속하는 다른 호선을 기록한다.
        station_line[3][1] = 3;
        station_line[6][1] = 2;
        station_line[8][1] = 4;
        station_line[11][1] = 8;
        station_line[12][1] = 7;
        station_line[14][1] = 3;
        station_line[15][1] = 5;
        station_line[18][1] = 8;
        station_line[20][1] = 5;
        station_line[21][1] = 4;
        station_line[22][1] = 2;
        station_line[24][1] = 6;
        station_line[29][1] = 2;
        station_line[31][1] = 4;
        station_line[33][1] = 8;
        station_line[36][1] = 7;
        station_line[38][1] = 3;
        station_line[42][1] = 6;
        station_line[46][1] = 3;
        station_line[50][1] = 4;
        station_line[53][1] = 8;
        station_line[56][1] = 7;
        station_line[59][1] = 5;
        station_line[63][1] = 6;
        station_line[64][1] = 5;
        station_line[67][1] = 6;
        station_line[72][1] = 6;
        station_line[76][1] = 8;
        station_line[79][1] = 7;
        station_line[85][1] = 6;
        station_line[89][1] = 7;
        station_line[92][1] = 8;
        station_line[95][1] = 8;
        station_line[98][1] = 7;


        n = 0;
        int index = 0;
        for (int i = 0; i < 139 * 5; i++) {
            if (n == 5) {
                index++;
                n = 0;
            }
            if (n < 2) {
                station_set[index][n] = String.valueOf(station_info[i]); // 서로 이동가능한 역을 저장한다.
            } else {
                station_cost[index][n - 2] = station_info[i]; // 서로 이동할 때 소모하는 비용들을 저장한다.
            }
            n++;
        }

    }
    station temp = new station();
    public void changeStation(int i, station s){
        st[i] = s;
    }
    public station getStation(String s){
        int t = station_index.indexOf(s);
        if(st[t].getUptime(st[t].getLine()[0][0])[0] == -1) {
            DatabaseReference database_ref = FirebaseDatabase.getInstance().getReference("time");
            database_ref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    Object o = task.getResult().child(s).getValue(Object.class);
                    GenericTypeIndicator<HashMap<String, ArrayList<Integer>>> T = new GenericTypeIndicator<HashMap<String, ArrayList<Integer>>>() {
                        @Override
                        public int hashCode() {
                            return super.hashCode();
                        }
                    };
                    if(o == null) {
                        setStationTime();
                        for (int i = 0; i < 111; i++) {
                            for (int j = 0; j < 36; j++) {
                                String index = String.valueOf(j);
                                int line1 = st[i].getLine()[0][0];
                                int line2 = st[i].getLine()[1][0];
                                database_ref.child(station_list[i]).child(String.valueOf(line1)).child("uptime").child(index).setValue(st[i].getUptime(line1)[j]);
                                database_ref.child(station_list[i]).child(String.valueOf(line1)).child("downtime").child(index).setValue(st[i].getDowntime(line1)[j]);
                                if (line2 != -1) {
                                    database_ref.child(station_list[i]).child(String.valueOf(line2)).child("uptime").child(index).setValue(st[i].getUptime(line2)[j]);
                                    database_ref.child(station_list[i]).child(String.valueOf(line2)).child("downtime").child(index).setValue(st[i].getDowntime(line2)[j]);
                                }
                            }
                        }
                    }else{
                        HashMap<String, ArrayList<Integer>> h = task.getResult().child(s).child(String.valueOf(st[t].getLine()[0][0])).getValue(T);
                        for(int i = 0; i < 36; i++){
                            st[t].setUptime(st[t].getLine()[0][0], i, h.get("uptime").get(i));
                            st[t].setDowntime(st[t].getLine()[0][0], i, h.get("downtime").get(i));
                            if(st[t].getLine()[1][0] != -1) {
                                st[t].setUptime(st[t].getLine()[1][0], i, h.get("uptime").get(i));
                                st[t].setDowntime(st[t].getLine()[0][0], i, h.get("downtime").get(i));
                            }
                        }
                    }
                }
            });
        }

        return st[t];
    }

    public station getStation(int line, String s){
        int t = station_index.indexOf(s);
        if(st[t].getUptime(line)[0] == -1){
            setStationTime(line);
        }
        return st[t];
    }

    public void setStationTime(int n){

        st[line.getLine_st()[n][0]].setUptime(n,time.getUptime(n));
        st[line.getLine_st()[n][line_num[n]-1]].setDowntime(n,time.getDowntime(n));
        for(int j = 1; j < line.getLine_num()[n]; j++){
            int a = line.getLine_st()[n][j];
            int b = line.getLine_st()[n][j-1];
            int c = line.getLine_st()[n][line.getLine_num()[n]-j-1];
            int d = line.getLine_st()[n][line.getLine_num()[n]-j];
            long[] bu = st[b].getUptime(n);
            long[] dd = st[d].getDowntime(n);
            for(int k = 0; k < 36; k++){
                st[a].setUptime(n, k, bu[k]+getTime()[b][a]/10);
                st[c].setDowntime(n, k, dd[k]+getTime()[d][c]/10);
            }
        }
    }
    public void setStationTime(){
        int[] line_num = line.getLine_num();
        for(int i = 0; i < 9; i++){
            st[line.getLine_st()[i][0]].setUptime(i,time.getUptime(i));
            st[line.getLine_st()[i][line_num[i]-1]].setDowntime(i,time.getDowntime(i));
        }
        for(int i = 0; i < 9; i++){
            int n = line.getLine_num()[i];
            for(int j = 1; j < n; j++){
                int a = line.getLine_st()[i][j];
                int b = line.getLine_st()[i][j-1];
                int c = line.getLine_st()[i][n-j-1];
                int d = line.getLine_st()[i][n-j];
                long[] bu = st[b].getUptime(i);
                long[] dd = st[d].getDowntime(i);
                for(int k = 0; k < 36; k++){
                    st[a].setUptime(i, k, bu[k]+(getTime()[b][a])/10);
                    st[c].setDowntime(i, k, dd[k]+(getTime()[d][c])/10);
                }
            }
        }
    }

    public long[] getTimeTable(int station, int line, int ud){
        if(st[station].getUptime(line)[0] == -1){
            st[station] = getStation(line, station_list[station]);
        }
        if(ud == 0){
            return st[station].getUptime(line);
        }else{
            return st[station].getDowntime(line);
        }
    }

    public int[][] getTime(){ // 역간 이동 시간 배열을 반환한다.
        int[][] list = new int[111][111];
        for(int i = 0; i < 111; i++) {
            for(int j = 0; j < 111; j++) {
                if(i != j) {
                    list[i][j] = 9999; // 이동할 수 없는 역의 경우 소모값을 9999로 설정하기 위해 9999로 초기화
                }else {
                    list[i][j] = 0; // 자기 자신의 경우 소모값을 0으로 설정
                }
            }
        }
        int index = 0;
        for(int i = 0; i < 139; i++, index++) {
            int a = station_index.indexOf(station_set[i][0]);
            int b = station_index.indexOf(station_set[i][1]);
            list[a][b] = station_cost[index][0]; // 이동 가능한 역의 소모값을 저장한다.
            list[b][a] = station_cost[index][0]; // 이동 가능한 역의 소모값을 저장한다.
        }
        return list;
    }

    public int[][] getDist(){ // 역간 이동 거리 배열을 반환한다.
        int[][] list = new int[111][111];
        for(int i = 0; i < 111; i++) {
            for(int j = 0; j < 111; j++) {
                if(i != j) {
                    list[i][j] = 9999; // 이동할 수 없는 역의 경우 소모값을 9999로 설정하기 위해 9999로 초기화
                }else {
                    list[i][j] = 0; // 자기 자신의 경우 소모값을 0으로 설정
                }
            }
        }
        int index = 0;
        for(int i = 0; i < 139; i++) {
            int a = station_index.indexOf(station_set[i][0]);
            int b = station_index.indexOf(station_set[i][1]);
            list[a][b] = station_cost[index][1]; // 이동 가능한 역의 소모값을 저장한다.
            list[b][a] = station_cost[index][1]; // 이동 가능한 역의 소모값을 저장한다.
            index++;
        }
        return list;
    }

    public int[][] getCost(){ // 역간 이동 비용 배열을 반환한다.
        int[][] list = new int[111][111];
        for(int i = 0; i < 111; i++) {
            for(int j = 0; j < 111; j++) {
                if(i != j) {
                    list[i][j] = 9999; // 이동할 수 없는 역의 경우 소모값을 9999로 설정하기 위해 9999로 초기화
                }else {
                    list[i][j] = 0; // 자기 자신의 경우 소모값을 0으로 설정
                }
            }
        }
        int index = 0;
        for(int i = 0; i < 139; i++) {
            int a = station_index.indexOf(station_set[i][0]);
            int b = station_index.indexOf(station_set[i][1]);
            list[a][b] = station_cost[index][2]; // 이동 가능한 역의 소모값을 저장한다.
            list[b][a] = station_cost[index][2]; // 이동 가능한 역의 소모값을 저장한다.
            index++;
        }
        return list;
    }

    public ArrayList<String> getIndexOfStation(){
        return station_index; // 기차 역의 이름이 순서대로 들어있는 ArrayList반환
    }
    public String[] getStationList(){
        return station_list;
    } // 기차 역의 이름이 순서대로 들어있는 String 배열 반환

    public int[] getTransStation(){
        return trans_station;
    } // 환승역에 대한 정보가 들어있는 배열 반환

    public int[][] getStationLine(){
        return station_line;
    } // 각 역이 어느 호선에 포함됐는지에 대한 정보가 들어있는 배열 반환


    public Line getLine() {
        return line;
    }

    public station getst(int n){
        return st[n];
    }
    public station[] getst(){
        return st;
    }
}
