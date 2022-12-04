package com.example.teamproject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class MyAdapter extends BaseAdapter {

    private ArrayList<Post_item> postList = new ArrayList<>();
    public ArrayList<Post_item> getPostList(){
        return this.postList;
    }
    @Override
    public int getCount() {
        return postList.size();
    }

    @Override
    public Post_item getItem(int position) {
        return postList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();

        /* 'listview_custom' Layout을 inflate하여 convertView 참조 획득 */
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_post, parent, false);
        }

        /* 'listview_custom'에 정의된 위젯에 대한 참조 획득 */
        TextView cnt_date_tv = (TextView) convertView.findViewById(R.id.cnt_date_tv) ;
        TextView cnt_title_tv = (TextView) convertView.findViewById(R.id.cnt_title_tv) ;
        TextView cnt_content_tv = (TextView) convertView.findViewById(R.id.cnt_content_tv) ;
        TextView cnt_userid_tv = (TextView) convertView.findViewById(R.id.cnt_userid_tv) ;

        /* 각 리스트에 뿌려줄 아이템을 받아오는데 mMyItem 재활용 */
        Post_item myItem = getItem(position);

        /* 각 위젯에 세팅된 아이템을 뿌려준다 */
        cnt_userid_tv.setText(myItem.getUserid());
        cnt_content_tv.setText(myItem.getContent());
        cnt_title_tv.setText(myItem.getTitle());
        cnt_date_tv.setText(myItem.getReg_date());

        return convertView;
    }
    public void resetItem(){
        postList.clear();
    }
    /* 아이템 데이터 추가를 위한 함수. 자신이 원하는대로 작성 */
    public void addItem(String reg_date, String userid, String content, String title,int cnt) {

        Post_item myItem = new Post_item();

        /* MyItem에 아이템을 setting한다. */
        myItem.setReg_date(reg_date);
        myItem.setContent(content);
        myItem.setTitle(title);
        myItem.setUserid(userid);
//        myItem.setcount(cnt);
        /* mItems에 MyItem을 추가한다. */
        postList.add(myItem);

    }
    public void reverseList(){
        Collections.reverse(postList);
    }
}
