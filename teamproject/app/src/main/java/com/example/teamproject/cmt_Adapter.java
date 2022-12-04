package com.example.teamproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class cmt_Adapter extends BaseAdapter {

    private ArrayList<Comment_item> commentList = new ArrayList<>();
    public ArrayList<Comment_item> getPostList(){
        return this.commentList;
    }
    @Override
    public int getCount() {
        return commentList.size();
    }

    @Override
    public Comment_item getItem(int position) {
        return commentList.get(position);
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
            convertView = inflater.inflate(R.layout.custom_comment, parent, false);
        }

        /* 'listview_custom'에 정의된 위젯에 대한 참조 획득 */
        TextView cmt_date_tv = (TextView) convertView.findViewById(R.id.cmt_date_tv) ;
        TextView cmt_content_tv = (TextView) convertView.findViewById(R.id.cmt_content_tv) ;
        TextView cmt_userid_tv = (TextView) convertView.findViewById(R.id.cmt_userid_tv) ;

        /* 각 리스트에 뿌려줄 아이템을 받아오는데 mMyItem 재활용 */
        Comment_item myItem = getItem(position);

        /* 각 위젯에 세팅된 아이템을 뿌려준다 */
        cmt_userid_tv.setText(myItem.getUserid());
        cmt_content_tv.setText(myItem.getContent());
        cmt_date_tv.setText(myItem.getReg_date());

        return convertView;
    }
    public void reset(){
        commentList.clear();
    }
    /* 아이템 데이터 추가를 위한 함수. 자신이 원하는대로 작성 */
    public void addItem(String reg_date, String userid, String content) {

        Comment_item myItem = new Comment_item();

        /* MyItem에 아이템을 setting한다. */
        myItem.setReg_date(reg_date);
        myItem.setContent(content);
        myItem.setUserid(userid);

        /* mItems에 MyItem을 추가한다. */
        commentList.add(myItem);

    }
    public void reverseList(){
        Collections.reverse(commentList);
    }
}
