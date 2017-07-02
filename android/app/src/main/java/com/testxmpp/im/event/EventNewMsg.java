package com.testxmpp.im.event;

import org.jivesoftware.smack.packet.Message;

/**
 * Created by zhanglin on 2017/6/30.
 */

public class EventNewMsg {
    public Message message;

    public EventNewMsg(Message message) {
        this.message = message;
    }
}
