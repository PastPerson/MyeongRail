package com.example.teamproject;

import java.io.Serializable;
import java.util.ArrayList;

public class Post_item implements Serializable {
    private String userid;
    private String title;
    private String content;
    private String reg_date;
    private String reportUser0;
    private String reportUser1;
    private ArrayList<Comment_item> commentList = new ArrayList<>();
    private int count = 0;

    public void setReportUser0(String str){this.reportUser0 = str;}
    public void setReportUser1(String str){this.reportUser1 = str;}
    public String getReportUser1(){return this.reportUser1;}
    public String getReportUser0(){return this.reportUser0;}
    public void setUserid(String userid){this.userid = userid;}
    public String getUserid() {return this.userid;}
    public void setTitle(String title){this.title = title;}
    public String getTitle() {return this.title;}
    public void setContent(String content){this.content = content;}
    public String getContent() {return this.content;}
    public void setReg_date(String reg_date){this.reg_date = reg_date;}
    public String getReg_date(){return this.reg_date;}
    public void addcount() {this.count++;}
    public int getCount(){return this.count;}
    public void addComment(Comment_item item){
        commentList.add(item);
    }
    public Comment_item getComment(int position){
        return commentList.get(position);
    }
}
