package com.testxmpp.im.manager;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smackx.muc.MultiUserChat;

import java.util.HashMap;
import java.util.Map;

/**
 * created by zhanglin on 2017/7/1
 */

public class MultiUserChatManager {
    private Map<String, MultiUserChat> mucMap = new HashMap<>();
    private static MultiUserChatManager instance = null;

    private MultiUserChatManager() {

    }

    public synchronized static MultiUserChatManager getInstance() {
        if (instance == null) instance = new MultiUserChatManager();
        return instance;
    }

    public void addMultiUserChat(MultiUserChat muc) {
        mucMap.put(muc.getRoom(), muc);


    }

    public MultiUserChat getMultiUserChat(XMPPConnection connection, String roomId) {
        MultiUserChat muc = mucMap.get(roomId);
        if (muc == null) {
            muc = new MultiUserChat(connection, roomId);
            addMultiUserChat(muc);
        }
        return muc;
    }

    /**
     * 销毁聊天室成员变化和拒绝邀请的监听事件
     */
    public void onDestroy() {
        for (int i = 0; i < mucMap.size(); i++) {
            ChatRoomManager.getInstance().unRegisterChatRoomRejectListener(mucMap.get(i));
        }
        mucMap.clear();
        instance = null;
    }

    public void clearMultiUserChat() {
        mucMap.clear();
    }
}
