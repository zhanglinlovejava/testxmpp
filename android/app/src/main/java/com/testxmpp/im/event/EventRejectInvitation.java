package com.testxmpp.im.event;

/**
 * Created by zhanglin on 2017/7/3.
 */

public class EventRejectInvitation {
    public String from;
    public String reason;

    public EventRejectInvitation(String from, String reason) {
        this.from = from;
        this.reason = reason;
    }
}
