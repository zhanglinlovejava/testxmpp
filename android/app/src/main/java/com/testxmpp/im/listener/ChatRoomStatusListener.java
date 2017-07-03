package com.testxmpp.im.listener;

import com.orhanobut.logger.Logger;
import com.testxmpp.Constants;
import com.testxmpp.im.event.EventChatRoomStatus;
import com.testxmpp.im.event.RxBus;

import org.jivesoftware.smackx.muc.ParticipantStatusListener;

/**
 * Created by zhanglin on 2017/7/3.
 */

public class ChatRoomStatusListener implements ParticipantStatusListener {
    private String roomId;

    public ChatRoomStatusListener(String roomId) {
        this.roomId = roomId;
    }

    @Override
    public void joined(String s) {
        RxBus.getDefault().post(new EventChatRoomStatus(Constants.STATUS_JOINED, s, roomId));
    }

    @Override
    public void left(String s) {
        RxBus.getDefault().post(new EventChatRoomStatus(Constants.STATUS_LEFT, s, roomId));
    }

    @Override
    public void kicked(String s, String s1, String s2) {
        RxBus.getDefault().post(new EventChatRoomStatus(Constants.STATUS_KICKED, s, roomId));
    }

    @Override
    public void voiceGranted(String s) {
        Logger.e(s + "--被禁言了");
    }

    @Override
    public void voiceRevoked(String s) {
        Logger.e(s + "---解除禁言了");
    }

    @Override
    public void banned(String s, String s1, String s2) {
        Logger.e(s + "---banned-禁止加入房间---" + s1 + "--" + s2);
    }

    @Override
    public void membershipGranted(String s) {
        Logger.e(s + "--membershipGranted--授予成员权限-");
    }

    @Override
    public void membershipRevoked(String s) {
        Logger.e(s + "--membershipRevoked--移除成员权限");
    }

    @Override
    public void moderatorGranted(String s) {
        Logger.e(s + "--moderatorGranted--授予主持人权限");
    }

    @Override
    public void moderatorRevoked(String s) {
        Logger.e(s + "--moderatorRevoked--移除主持人权限");
    }

    @Override
    public void ownershipGranted(String s) {
        Logger.e(s + "--ownershipGranted--授予所有者权限");
    }

    @Override
    public void ownershipRevoked(String s) {
        Logger.e(s + "--ownershipRevoked--移除所有者权限");
    }

    @Override
    public void adminGranted(String s) {
        Logger.e(s + "--adminGranted--");
    }

    @Override
    public void adminRevoked(String s) {
        Logger.e(s + "--adminRevoked");
    }

    @Override
    public void nicknameChanged(String s, String s1) {
        Logger.e(s + "--nicknameChanged");
    }
}
