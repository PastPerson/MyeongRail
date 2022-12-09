package com.example.teamproject;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

class SubwayListAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<Subway_Item> subways;
    private int layout;
    private int num;

//    public SubwayListAdapter(Context context, int layout, ArrayList<Subway_Item> subways){
//        this.inflater=(LayoutInflater)context.getSystemService(
//                Context.LAYOUT_INFLATER_SERVICE);
//        this.subways=subways;
//        this.layout =layout;
//    }

    public  void additem(String name, String line,int density){
        Subway_Item sb=new Subway_Item(name,line,density);
        subways.add(sb);

    }

    @Override
    public int getCount(){
        return subways.size();
    }
    @Override
    public Subway_Item getItem(int pos){
        return subways.get(pos);
    }
    @Override
    public long getItemId(int pos){
        return pos;
    }
    @Override
    public View getView(int pos, View convertView, ViewGroup parent){
        if(convertView==null){
            convertView=inflater.inflate(layout,parent,false);
        }
        Subway_Item subway= subways.get(pos);
        TextView name= convertView.findViewById(R.id.station_item_number);
        TextView line= convertView.findViewById(R.id.station_item_line);
        TextView density=convertView.findViewById(R.id.density_status);
        TextView name_color=convertView.findViewById(R.id.station_item_color);
        name.setText(subway.getName());
        line.setText(subway.getLine());
        if(subway.getDensity()==1){
            density.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#DC143C")));
        }else{
            density.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00FF7F")));
        }
        num=Integer.parseInt(subway.getLine());
        if(num==1){
            name_color.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#09B802")));//1호선색상
            line.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#09B802")));//1호선색상
        }else if(num==2){
            name_color.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#0200c7")));//2호선색상
            line.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#0200c7")));//2호선색상
        }else if(num==3){
            name_color.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#803901")));//3호선색상
            line.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#803901")));//3호선색상
        }else if(num==4){
            name_color.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F20303")));//4호선색상
            line.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F20303")));//4호선색상
        }else if(num==5){
            name_color.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#7C85FF")));//5호선색상
            line.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#7C85FF")));//5호선색상
        }else if(num==6){
            name_color.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F2EB11")));//6호선색상
            line.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F2EB11")));//6호선색상
        }else if(num==7){
            name_color.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CFD11")));//7호선색상;
            line.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CFD11")));//7호선색상;
        }else if(num==8){
            name_color.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#03DAF2")));//8호선색상
            line.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#03DAF2")));//8호선색상
        }else if (num==9){
            name_color.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#A206F2")));//9호선색상
            line.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#A206F2")));//9호선색상
        }else if(subway.getLine()==null){
            line.setText(" ");
        }
        return convertView;
    }

}
