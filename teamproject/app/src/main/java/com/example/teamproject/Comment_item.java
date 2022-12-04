package com.example.teamproject;

public class Comment_item {
    private String userid;
    private String content;
    private String reg_date;
    private int count = 0;
    public void setUserid(String userid){this.userid = userid;}
    public String getUserid() {return this.userid;}
    public void setContent(String content){this.content = content;}
    public String getContent() {return this.content;}
    public void setReg_date(String reg_date){this.reg_date = reg_date;}
    public String getReg_date(){return this.reg_date;}
    public void addcount() {this.count++;}
    public int getCount(){return this.count;}

}
