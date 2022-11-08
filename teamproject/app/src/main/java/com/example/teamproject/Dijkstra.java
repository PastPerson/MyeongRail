package com.example.teamproject;

import android.content.Context;

import java.util.ArrayList;


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


    public Dijkstra(Context context){
        this.context = context;
        StationInfo data = new StationInfo(); // 지하철 역에 대한 정보를 제공하는 클래스 객체
        this.station_index = data.getIndexOfStation();
        this.station_list = data.getStationList();
        this.time = data.getTime(); // 역간 이동 시 소모 시간 정보가 들어있는 배열을 받아온다.
        this.dist = data.getDist(); // 역간 이동 시 이동 거리 정보가 들어있는 배열을 받아온다.
        this.cost = data.getCost(); // 역간 이동 시 소모 비용 정보가 들어있는 배열을 받아온다.
        this.station_line = data.getStationLine(); // 역이 어느 호선에 위치했는지에 대한 정보가 들어있는 배열을 받아온다.
        this.trans_station = data.getTransStation(); // 환승역 정보가 들어있는 배열을 받아온다.
    }

    void trans_station(int[] path, int path_cnt, int s, int e){ // s에서 e까지 도달하는 경로에서 지나는 환승역을 구한다.
        count = 0;
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

        //System.out.printf("%d .\n", path[i] + 1);
        setCc(cc);
        setSum_t(sum_t);
//        System.out.println("환승횟수 "+sum_t);
//        for(int i = 0; i < count; i++){
//            System.out.println("환승 " + cc[i]);
//        }
    }


    void check(String start, String finish){
        int i, j, k = 0, min,min2,min3; /* i, j, k = for문을 위해 생성
				  							  s = 시작노드 입력값
				  							  e = 끝 노드 입력값
				  							  min = 최소값을 찾기 위함*/
        int s = station_index.indexOf(start); // 역이름에 맞는 인덱스 반환
        int e = station_index.indexOf(finish);// 역이름에 맞는 인덱스 반환
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
                    min2 = distance1[j];
                    min3 = distance2[j];

                }
            }


            v[k] = 1;
            if (min == INF)
                break;

            for (j = 0; j < 111; j++) {
                if (distance[j] > distance[k] + time[k][j]) {
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
    }

    void check2(String start, String finish){
        int i, j, k = 0, min,min2,min3; /* i, j, k = for문을 위해 생성
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
            min2 = INF;  //최소값을 아직 연결되지 않음을 설정
            for (j = 0; j < 111; j++) {
                if (v[j] == 0 && distance[j] < min2)
                //이웃한 노드 중 방문하지 않은 노드일 경우
                {
                    k = j;//최단거리 노드번호를 k에 저장
                    min = distance[j];//min에 최단거리를 장
                    min2 = distance1[j];
                    min3 = distance2[j];

                }
            }


            v[k] = 1;
            if (min2 == INF)
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
    }
    void check3(String start, String finish) {
        int i, j, k = 0, min, min2, min3; /* i, j, k = for문을 위해 생성
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
            min3 = INF;  //최소값을 아직 연결되지 않음을 설정
            for (j = 0; j < 111; j++) {
                if (v[j] == 0 && distance[j] < min3)
                //이웃한 노드 중 방문하지 않은 노드일 경우
                {
                    k = j;//최단거리 노드번호를 k에 저장
                    min = distance[j];//min에 최단거리를 장
                    min2 = distance1[j];
                    min3 = distance2[j];

                }
            }


            v[k] = 1;
            if (min3 == INF)
                break;

            for (j = 0; j < 111; j++) {
                if (distance[j] > distance[k] + cost[k][j]) {
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
    }
}