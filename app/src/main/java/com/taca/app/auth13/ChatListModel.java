package com.taca.app.auth13;

/**
 * Created by a on 2016-12-06.
 * 채팅 목록 자료구조
 */

public class ChatListModel {
    String channel;     //채팅 채널값
    String profile;     //상대방 프로필 사진 url
    String lastMsg;     //마지막 메세지
    int unReadMsgCnt;   //읽지 않은 메시지 수
    long lastMsgTm;     //마지막 세시지 수신 시간
    int status;         //상대방 상태 값(1:익명, 2:친구, 3:차단)

    public ChatListModel() {
    }

    public ChatListModel(String channel, String profile, String lastMsg, int unReadMsgCnt, long lastMsgTm, int status) {
        this.channel = channel;
        this.profile = profile;
        this.lastMsg = lastMsg;
        this.unReadMsgCnt = unReadMsgCnt;
        this.lastMsgTm = lastMsgTm;
        this.status = status;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getLastMsg() {
        return lastMsg;
    }

    public void setLastMsg(String lastMsg) {
        this.lastMsg = lastMsg;
    }

    public int getUnReadMsgCnt() {
        return unReadMsgCnt;
    }

    public void setUnReadMsgCnt(int unReadMsgCnt) {
        this.unReadMsgCnt = unReadMsgCnt;
    }

    public long getLastMsgTm() {
        return lastMsgTm;
    }

    public void setLastMsgTm(long lastMsgTm) {
        this.lastMsgTm = lastMsgTm;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
