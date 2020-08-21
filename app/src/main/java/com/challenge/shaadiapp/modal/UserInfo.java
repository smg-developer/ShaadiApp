package com.challenge.shaadiapp.modal;

public class UserInfo {

    public String u_name;
    public String date;
    public String description;
    public String  status;
    public String  profile_pic;


    public UserInfo(String u_name, String date, String description, String profile_pic, String status) {
        this.u_name = u_name;
        this.date = date;
        this.description = description;
        this.profile_pic = profile_pic;
        this.status = status;
    }
}
