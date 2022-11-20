package com.example.teamproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class SearchAdapter extends BaseAdapter {

    private Context context;
    private List<String> list;
    private LayoutInflater inflate;
    private ViewHolder viewHolder;

    public SearchAdapter(List<String> list, Context context) {
        this.list = list;
        this.context = context;
        this.inflate = LayoutInflater.from(context);
    }
    @Override
    public int getCount(){
        return list.size();
    }

    @Override
    public Object getItem (int i){
        return null;
    }

    @Override
    public long getItemId(int i){
        return 0;
    }

    @Override
    public View getView(int position, View converView, ViewGroup viewGroup){
        if(converView == null){
            converView = inflate.inflate(R.layout.row_listview,null);

            viewHolder = new ViewHolder();
            viewHolder.label = (TextView) converView.findViewById(R.id.label);
            converView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) converView.getTag();
        }

        viewHolder.label.setText(list.get(position));

        return converView;
    }

    class ViewHolder{
        public TextView label;
    }
}
