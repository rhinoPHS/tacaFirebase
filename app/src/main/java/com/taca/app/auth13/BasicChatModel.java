package com.taca.app.auth13;

/**
 * Created by a on 2016-12-05.
 */

//기본 채팅 메시지를 담는 그릇

public class BasicChatModel {
    String userName;
    String msg;

    //getter, setter, default constructor, init constructor
    public BasicChatModel() {
    }

    public BasicChatModel(String userName, String msg) {
        this.userName = userName;
        this.msg = msg;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
