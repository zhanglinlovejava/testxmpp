package com.testxmpp.im.event;

/**
 * Created by zhanglin on 2017/7/3.
 */

public class EventFriendEvent {
    public String type;
    public String from;

    public EventFriendEvent(String type, String from) {
        this.type = type;
        this.from = from;
    }
}
