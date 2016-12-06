package com.taca.app.auth13;

/**
 * Created by a on 2016-12-06.
 */

public class ExChatModel {
    String uid, msg;    //아이디, 메세지
    int isRead;         // 1:않읽음(나를 제외한 사람수), 0:읽음
    long createTime;    // 생성시간
    int type;            // 1:텍스트 2:이미지(URL) 3:사운드(URL) 4:점표,...

    public ExChatModel() {
    }

    public ExChatModel(String uid, String msg, int isRead, long createTime, int type) {
        this.uid = uid;
        this.msg = msg;
        this.isRead = isRead;
        this.createTime = createTime;
        this.type = type;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
