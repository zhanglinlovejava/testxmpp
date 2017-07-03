package com.testxmpp.im.listener;

import com.testxmpp.im.event.EventRejectInvitation;
import com.testxmpp.im.event.RxBus;

import org.jivesoftware.smackx.muc.InvitationRejectionListener;

/**
 * Created by zhanglin on 2017/7/3.
 */

public class ChatRoomRejectInvitationListener implements InvitationRejectionListener {

    @Override
    public void invitationDeclined(String from, String reason) {
        RxBus.getDefault().post(new EventRejectInvitation(from, reason));
    }
}
