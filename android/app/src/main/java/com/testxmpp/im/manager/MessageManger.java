package com.testxmpp.im.manager;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.muc.MultiUserChat;

/**
 * created by zhanglin on 2017/7/2
 */

public class MessageManger {

    private static MessageManger instance = null;
    private ChatManager chatManager;
    private MessageManger() {
    }

    public synchronized static MessageManger getInstance() {
        if (instance == null) instance = new MessageManger();
        return instance;
    }

    /**
     * 发送消息
     *
     * @param to
     * @param msg
     * @return
     */
    public boolean sendMsg(String to, String msg) {
        try {
            XMPPConnection connection = IMConnectionManager.getInstance().getConnection();
            if (connection != null) {
                if (chatManager == null)
                    chatManager = connection.getChatManager();
                Chat chat = chatManager.createChat(to, null);
                chat.sendMessage(msg);
                return true;
            } else return false;
        } catch (XMPPException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 发送聊天室文本信息
     *
     * @param roomId
     * @param msg
     * @return
     */
    public boolean sendChatRoomMsg(String roomId, String msg) {
        XMPPConnection connection = IMConnectionManager.getInstance().getConnection();
        if (connection == null)
            return false;
        try {
            MultiUserChat muc = MultiUserChatManager.getInstance().getMultiUserChat(connection, roomId);
            muc.sendMessage(msg);
            return true;
        } catch (XMPPException e) {
            e.printStackTrace();
            return false;
        }
    }
}
