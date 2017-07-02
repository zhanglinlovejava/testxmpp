package com.testxmpp.im.event;

import org.jivesoftware.smack.packet.Message;

/**
 * Created by zhanglin on 2017/6/30.
 */

public class EventInviterChatRoom {
    public Message message;
    public String reason;
    public String psw;
    public String roomId;
    public String inivter;

    public EventInviterChatRoom(Message message, String reason, String psw, String roomId, String inivter) {
        this.message = message;
        this.reason = reason;
        this.psw = psw;
        this.roomId = roomId;
        this.inivter = inivter;
    }

}
