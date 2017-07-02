package com.testxmpp.im.event;

import org.jivesoftware.smack.packet.Message;

/**
 * Created by zhanglin on 2017/6/30.
 */

public class EventInviterChatRoom {
    public Message message;
    public String reason;
    public String psw;
    public EventInviterChatRoom(Message message, String reason, String psw) {
        this.message = message;
        this.reason = reason;
        this.psw = psw;
    }
}
