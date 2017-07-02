package com.testxmpp.im;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smackx.muc.MultiUserChat;

import java.util.HashMap;
import java.util.Map;

/**
 * created by zhanglin on 2017/7/1
 */

public class MultiUserChatManager {
    private Map<String, MultiUserChat> mucs = new HashMap<>();
    private static MultiUserChatManager instance = null;

    private MultiUserChatManager() {

    }

    public synchronized static MultiUserChatManager getInstance() {
        if (instance == null) instance = new MultiUserChatManager();
        return instance;
    }

    public void addMultiUserChat(MultiUserChat muc) {
        mucs.put(muc.getRoom(), muc);
    }

    public MultiUserChat getMultiUserChat(XMPPConnection connection, String roomId) {
        MultiUserChat muc = mucs.get(roomId);
        if (muc == null) {
            muc = new MultiUserChat(connection, roomId);
            addMultiUserChat(muc);
        }
        return muc;
    }

    public void onDestroy() {
        mucs.clear();
        instance = null;
    }

    public void clearMultiUserChat() {
        mucs.clear();
    }
}
