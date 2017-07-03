package com.testxmpp.im.event;

/**
 * Created by zhanglin on 2017/7/3.
 */

public class EventChatRoomStatus {
    public String type;
    public String user;
    public String roomId;

    public EventChatRoomStatus(String type, String user, String roomId) {
        this.type = type;
        this.user = user;
        this.roomId = roomId;
    }
}
