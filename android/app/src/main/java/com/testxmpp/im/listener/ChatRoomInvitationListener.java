package com.testxmpp.im.listener;

import com.testxmpp.im.event.EventInviterChatRoom;
import com.testxmpp.im.event.RxBus;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.muc.InvitationListener;

/**
 * Created by zhanglin on 2017/7/3.
 */

public class ChatRoomInvitationListener implements InvitationListener {
    @Override
    public void invitationReceived(Connection connection, String roomID, String inviter, String reason, String psw, Message message) {
        RxBus.getDefault().post(new EventInviterChatRoom(message, reason, psw, roomID, inviter));
    }
}
