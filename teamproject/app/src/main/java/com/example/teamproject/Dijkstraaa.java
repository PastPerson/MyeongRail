package com.example.teamproject;

import android.content.Context;

import java.util.ArrayList;


public class Dijkstraaa {
    Context context;
    ArrayList<String> station_index = new ArrayList<>();

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

    String cc[]=new String[40];

    int sum_t=0;
    // t1~ t35까지 환승 역을 뜻함
    boolean t1; // 101번 역
    boolean t2; // 104번 역
    boolean t3; // 107번 역
    boolean t4; // 109번 역
    boolean t5; // 112번 역
    boolean t6; // 113번 역
    boolean t7; // 115번 역
    boolean t8; // 116번 역
    boolean t9; // 119번 역
    boolean t10; // 121번 역
    boolean t11; // 122번 역
    boolean t12; // 123번 역
    boolean t13; // 202번 역
    boolean t14; // 207번 역
    boolean t15; // 209번 역
    boolean t16; // 211번 역
    boolean t17; // 214번 역
    boolean t18; // 216번 역
    boolean t19; // 303번 역
    boolean t20; // 307번 역
    boolean t21; // 403번 역
    boolean t22; // 406번 역
    boolean t23; // 409번 역
    boolean t24; // 412번 역
    boolean t25; // 416번 역
    boolean t26; // 417번 역
    boolean t27; // 503번 역
    boolean t28; // 601번 역
    boolean t29; // 605번 역
    boolean t30; // 608번 역
    boolean t31; // 614번 역
    boolean t32; // 618번 역
    boolean t33; // 621번 역
    boolean t34; // 702번 역
    boolean t35; // 705번 역
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


    public Dijkstraaa(Context context){
        this.context = context;
        DataInput data = new DataInput(); // 지하철 역에 대한 정보를 제공하는 클래스 객체
        this.station_index = data.getIndexOfStation();
        this.time = data.getTime(); // 역간 이동 시 소모 시간 정보가 들어있는 배열을 받아온다.
        this.dist = data.getDist(); // 역간 이동 시 이동 거리 정보가 들어있는 배열을 받아온다.
        this.cost = data.getCost(); // 역간 이동 시 소모 비용 정보가 들어있는 배열을 받아온다.
    }

    void trans_station(int[] path, int path_cnt, int s, int e){ // s에서 e까지 도달하는 경로에서 지나는 환승역을 구한다.

        for(int i = path_cnt - 1; i >= 1; i--){
            if(path[i] == 0){
                t1=true;
            }
            if(path[i] ==3){
                t2=true;
            }
            if(path[i] ==6){
                t3=true;
            }
            if(path[i] ==8){
                t4=true;
            }
            if(path[i] ==11){
                t5=true;
            }
            if(path[i] ==12){
                t6=true;
            }
            if(path[i] ==14){
                t7=true;
            }
            if(path[i] ==15){
                t8=true;
            }
            if(path[i] ==18){
                t9=true;
            }
            if(path[i] ==20){
                t10=true;
            }
            if(path[i] ==21){
                t11=true;
            }
            if(path[i] ==22){
                t12=true;
            }
            if(path[i] ==24){
                t13=true;
            }
            if(path[i] ==29){
                t14=true;
            }
            if(path[i] ==31){
                t15=true;
            }
            if(path[i] ==33){
                t16=true;
            }
            if(path[i] ==36){
                t17=true;
            }
            if(path[i] ==38){
                t18=true;
            }
            if(path[i] ==42){
                t19=true;
            }
            if(path[i] ==46){
                t20=true;
            }
            if(path[i] ==50){
                t21=true;
            }
            if(path[i] ==53){
                t22=true;
            }
            if(path[i] ==56){
                t23=true;
            }
            if(path[i] ==59){
                t24=true;
            }
            if(path[i] ==63){
                t25=true;
            }
            if(path[i] ==64){
                t26=true;
            }
            if(path[i] ==67){
                t27=true;
            }
            if(path[i] ==72){
                t28=true;
            }
            if(path[i] ==76){
                t29=true;
            }
            if(path[i] ==79){
                t30=true;
            }
            if(path[i] ==85){
                t31=true;
            }
            if(path[i] ==89){
                t32=true;
            }
            if(path[i] ==92){
                t33=true;
            }
            if(path[i] ==95){
                t34=true;
            }
            if(path[i] ==98){
                t35=true;
            }
        }
        for(int first =0 ;first<=22;first++){
            if(s==first){
                for (int last = 0 ; last <=22; last++){
                    if(e==last){
                        t1=false;
                        t2=false;
                        t3=false;
                        t4=false;
                        t5=false;
                        t6=false;
                        t7=false;
                        t8=false;
                        t9=false;
                        t10=false;
                        t11=false;
                        t12=false;
                    }
                }

            }
        }
        for(int first =23 ;first<=39;first++){
            if(s== first ||s==0){
                for (int last = 23 ; last <=39; last++) {
                    if (e == last ||e==0 ) {
                        t1 = false;
                        t13 = false;
                        t14 = false;
                        t15 = false;
                        t16 = false;
                        t17 = false;
                        t18 = false;
                    }
                }
            }
        }

        for(int first =40 ;first<=46;first++){
            if(s== first || s ==6|| s==22 || s == 29){
                for (int last = 40 ; last <=46; last++) {
                    if (e == last || e ==6 || e==22 || s == 29) {
                        t14 = false;
                        t19 = false;
                        t12 = false;
                        t20 = false;
                        t3 = false;
                    }
                }
            }
        }
        for(int first =47 ;first<=63;first++){
            if(s== first || s==3 || s == 14 || s == 38 || s == 46 ){
                for (int last = 47 ; last <=63; last++) {
                    if (e == last || e ==3 || e == 14 || e == 38 || e == 46) {
                        t2 = false;
                        t20 = false;
                        t22 = false;
                        t23 = false;
                        t7 = false;
                        t24 = false;
                        t25 = false;
                        t26 = false;
                        t18 = false;
                    }
                }
            }
        }
        for(int first =64 ;first<=70;first++){
            if(s== first || s==8 || s==21 || s==31 || s==50){
                for (int last = 64; last <=70; last++) {
                    if (e == last || e==8 || e==21 || e==31 || e==50) {
                        t15 = false;
                        t27 = false;
                        t11 = false;
                        t21 = false;
                        t4 = false;
                    }
                }
            }
        }
        for(int first =71 ;first<=92;first++){
            if(s== first || s==15 || s==20 || s==59 || s==64){
                for (int last = 71 ; last <=92; last++) {
                    if (e == last || e==15 || e==20 || e==59 || e==64) {
                        t28 = false;
                        t10 = false;
                        t29 = false;
                        t8 = false;
                        t30 = false;
                        t24 = false;
                        t31 = false;
                        t26 = false;
                        t32 = false;
                        t33 = false;
                    }
                }
            }
        }
        for(int first =93 ;first<=99;first++){
            if(s== first || s==24 || s==42 || s==67 || s==63 || s==72 || s==85){
                for (int last = 93 ; last <=99; last++) {
                    if (e == last || e==24 || e==42 || e==67 || e==63 || e==72 || e==85) {
                        t13 = false;
                        t19 = false;
                        t27 = false;
                        t29 = false;
                        t34 = false;
                        t35 = false;
                        t25 = false;
                        t31 = false;
                    }
                }
            }
        }
        for(int first =100 ;first<=105;first++){
            if(s== first || s==12 || s==36 || s==56 || s==79 || s== 89|| s==98){
                for (int last = 100 ; last <=105; last++) {
                    if (e == last || e==12 || e==36 || e==56 || e==79 || e==89 || e==98) {
                        t6 = false;
                        t23 = false;
                        t30 = false;
                        t35 = false;
                        t33 = false;
                        t17 = false;
                    }
                }
            }
        }for(int first =106 ;first<=110;first++){
            if(s== first || s==11 || s==18 || s==33 || s==53 || s==76 || s==92 || s==95){
                for (int last = 94 ; last <=100; last++) {
                    if (e == last || e==11 || e==18 || e==33 || e==53 || e==76 || e==92 || e==95) {
                        t5 = false;
                        t22 = false;
                        t9 = false;
                        t34 = false;
                        t33 = false;
                        t16 = false;
                    }
                }
            }
        }
        for (int i = path_cnt - 1; i >= 1; i--) {
            // System.out.printf("%d -> ", path[i] + 1);
            if(path[i]==0&& t1){
                cc[count]= "T1";
                count++;
            }
            if(path[i]==3&& t2){
                cc[count]= "T2";
                count++;
            }
            if(path[i]==6&& t3){
                cc[count]= "T3";
                count++;
            }
            if(path[i]==8&& t4){
                cc[count]= "T4";
                count++;
            }
            if(path[i]==11&& t5){
                cc[count]= "T5";
                count++;
            }
            if(path[i]==12&& t6){
                cc[count]= "T6";
                count++;
            }
            if(path[i]==14&& t7){
                cc[count]= "T7";
                count++;
            }
            if(path[i]==15&& t8){
                cc[count]= "T8";
                count++;
            }
            if(path[i]==18&& t9){
                cc[count]= "T9";
                count++;
            }
            if(path[i]==20&& t10){
                cc[count]= "T10";
                count++;
            }
            if(path[i]==21&& t11){
                cc[count]= "T11";
                count++;
            }
            if(path[i]==22&& t12){
                cc[count]= "T12";
                count++;
            }
            if(path[i]==24&& t13){
                cc[count]= "T13";
                count++;
            }
            if(path[i]==29&& t14){
                cc[count]= "T14";
                count++;
            }
            if(path[i]==31&& t15){
                cc[count]= "T15";
                count++;
            }
            if(path[i]==33&& t16){
                cc[count]= "T16";
                count++;
            }
            if(path[i]==36&& t17){
                cc[count]= "T17";
                count++;
            }
            if(path[i]==38&& t18){
                cc[count]= "T18";
                count++;
            }
            if(path[i]==42&& t19){
                cc[count]= "T19";
                count++;
            }
            if(path[i]==46&& t20){
                cc[count]= "T20";
                count++;
            }
            if(path[i]==50&& t21){
                cc[count]= "T21";
                count++;
            }
            if(path[i]==53&& t22){
                cc[count]= "T22";
                count++;
            }
            if(path[i]==56&& t23){
                cc[count]= "T23";
                count++;
            }
            if(path[i]==59&& t24){
                cc[count]= "T24";
                count++;
            }
            if(path[i]==63&& t25){
                cc[count]= "T25";
                count++;
            }
            if(path[i]==64&& t26){
                cc[count]= "T26";
                count++;
            }
            if(path[i]==67&& t27){
                cc[count]= "T27";
                count++;
            }
            if(path[i]==72&& t28){
                cc[count]= "T28";
                count++;
            }
            if(path[i]==76&& t29){
                cc[count]= "T29";
                count++;
            }
            if(path[i]==79&& t30){
                cc[count]= "T30";
                count++;
            }
            if(path[i]==85&& t31){
                cc[count]= "T31";
                count++;
            }
            if(path[i]==89&& t32){
                cc[count]= "T32";
                count++;
            }
            if(path[i]==92&& t33){
                cc[count]= "T33";
                count++;
            }
            if(path[i]==95&& t34){
                cc[count]= "T34";
                count++;
            }
            if(path[i]==98&& t35){
                cc[count]= "T35";
                count++;
            }
        }
        //System.out.printf("%d .\n", path[i] + 1);
        setCc(cc);

        if (t1){
            sum_t++;
        }
        if (t2){
            sum_t++;
        }
        if (t3){
            sum_t++;
        }
        if (t4){
            sum_t++;
        }
        if (t5){
            sum_t++;
        }
        if (t6){
            sum_t++;
        }
        if (t7){
            sum_t++;
        }
        if (t8){
            sum_t++;
        }
        if (t9){
            sum_t++;
        }
        if (t10){
            sum_t++;
        }
        if (t11){
            sum_t++;
        }
        if (t12){
            sum_t++;
        }
        if (t13){
            sum_t++;
        }
        if (t14){
            sum_t++;
        }
        if (t15){
            sum_t++;
        }
        if (t16){
            sum_t++;
        }
        if (t17){
            sum_t++;
        }
        if (t18){
            sum_t++;
        }
        if (t19){
            sum_t++;
        }
        if (t20){
            sum_t++;
        }
        if (t21){
            sum_t++;
        }
        if (t22){
            sum_t++;
        }
        if (t23){
            sum_t++;
        }
        if (t24){
            sum_t++;
        }
        if (t25){
            sum_t++;
        }
        if (t26){
            sum_t++;
        }
        if (t27){
            sum_t++;
        }
        if (t28){
            sum_t++;
        }
        if (t29){
            sum_t++;
        }
        if (t30){
            sum_t++;
        }
        if (t31){
            sum_t++;
        }
        if (t32){
            sum_t++;
        }
        if (t33){
            sum_t++;
        }
        if (t34){
            sum_t++;
        }
        if (t35){
            sum_t++;
        }

        setSum_t(sum_t);
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

    void check2(int start, int finish){
        int i, j, k = 0, min,min2,min3; /* i, j, k = for문을 위해 생성
				  							  s = 시작노드 입력값
				  							  e = 끝 노드 입력값
				  							  min = 최소값을 찾기 위함*/
        int s = start;
        int e = finish;
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
    void check3(int start, int finish) {
        int i, j, k = 0, min, min2, min3; /* i, j, k = for문을 위해 생성
				  							  s = 시작노드 입력값
				  							  e = 끝 노드 입력값
				  							  min = 최소값을 찾기 위함*/
        int s = start;
        int e = finish;
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