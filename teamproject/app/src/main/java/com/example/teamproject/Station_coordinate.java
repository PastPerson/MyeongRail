package com.example.teamproject;

import android.util.Log;

public class Station_coordinate {

    //각역의 좌표를 저장할 배열{역번호,좌측상단x좌표,좌측상단y좌표,우측하단x좌표,우측하단y좌표}
    final int [][]station={{101,495,3369,600,3490},{102,474,3673,579,3778}};
    public int Check_Coor(int x,int y){
        for(int i[] : station){
            if((i[1]<=x&&i[3]>=x)&&(i[2]<=y&&i[4]>=y))
                return i[0];
        }
        return 0;
    }


}
