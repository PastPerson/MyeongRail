package com.example.teamproject;


import java.util.ArrayList;

public class DataInput {
    ArrayList<Integer> station_index = new ArrayList<>();
    int[] station_info = { // 역간 이동시 소모 비용 정보가 들어있는 배열, 5개 단위로 0,1은 이동하는 두 역, 2,3,4는 시간, 거리, 비용을 의미함
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
    int[][] station_set = new int[139][2]; // [i][0,1] 에 0과 1에 서로 이동하는 역이 저장된다. 예를 들어 101에서 102역으로 이동가능 하다면 101과 102가 들어있음

    public DataInput(){
        int st_num = 101;
        for (int i = 0; i < 23; i++) {
            station_index.add(st_num);
            st_num++;
        }
        st_num = 201;
        for (int i = 0; i < 17; i++) {
            station_index.add(st_num);
            st_num++;
        }
        st_num = 301;
        for (int i = 0; i < 8; i++) {
            station_index.add(st_num);
            st_num++;
        }
        st_num = 401;
        for (int i = 0; i < 17; i++) {
            station_index.add(st_num);
            st_num++;
        }
        st_num = 501;
        for (int i = 0; i < 7; i++) {
            station_index.add(st_num);
            st_num++;
        }
        st_num = 601;
        for (int i = 0; i < 22; i++) {
            station_index.add(st_num);
            st_num++;
        }
        st_num = 701;
        for (int i = 0; i < 7; i++) {
            station_index.add(st_num);
            st_num++;
        }
        st_num = 801;
        for (int i = 0; i < 6; i++) {
            station_index.add(st_num);
            st_num++;
        }
        st_num = 901;
        for (int i = 0; i < 4; i++) {
            station_index.add(st_num);
            st_num++;
        }


        int n = 0;
        int index = 0;
        for (int i = 0; i < 139 * 5; i++) {
            if (n == 5) {
                index++;
                n = 0;
            }
            if (n < 2) {
                station_set[index][n] = station_info[i];
            } else {
                station_cost[index][n - 2] = station_info[i];
            }
            n++;
        }
    }

    public int[][] getTime(){ // 역간 이동 시간 배열을 반환한다.
        int[][] list = new int[111][111];
        for(int i = 0; i < 111; i++) {
            for(int j = 0; j < 111; j++) {
                if(i != j) {
                    list[i][j] = 9999;
                }else {
                    list[i][j] = 0;
                }
            }
        }
        int index = 0;
        for(int i = 0; i < 139; i++) {
            int a = station_index.indexOf(station_set[i][0]);
            int b = station_index.indexOf(station_set[i][1]);
            list[a][b] = station_cost[index][0];
            list[b][a] = station_cost[index][0];
            index++;
        }
        return list;
    }

    public int[][] getDist(){ // 역간 이동 거리 배열을 반환한다.
        int[][] list = new int[111][111];
        for(int i = 0; i < 111; i++) {
            for(int j = 0; j < 111; j++) {
                if(i != j) {
                    list[i][j] = 9999;
                }else {
                    list[i][j] = 0;
                }
            }
        }
        int index = 0;
        for(int i = 0; i < 139; i++) {
            int a = station_index.indexOf(station_set[i][0]);
            int b = station_index.indexOf(station_set[i][1]);
            list[a][b] = station_cost[index][1];
            list[b][a] = station_cost[index][1];
            index++;
        }
        return list;
    }

    public int[][] getCost(){ // 역간 이동 비용 배열을 반환한다.
        int[][] list = new int[111][111];
        for(int i = 0; i < 111; i++) {
            for(int j = 0; j < 111; j++) {
                if(i != j) {
                    list[i][j] = 9999;
                }else {
                    list[i][j] = 0;
                }
            }
        }
        int index = 0;
        for(int i = 0; i < 139; i++) {
            int a = station_index.indexOf(station_set[i][0]);
            int b = station_index.indexOf(station_set[i][1]);
            list[a][b] = station_cost[index][2];
            list[b][a] = station_cost[index][2];
            index++;
        }
        return list;
    }

    public int getIndexOfStation(int s){
        return station_index.indexOf(s); // 101을 매개변수로 받으면 0을 반환, 역에 맞는 인덱스를 반환해준다.
    }
}