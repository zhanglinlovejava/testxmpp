package com.testxmpp.im;

import android.util.Log;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.FormField;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.packet.VCard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * 时间: 2017/7/2 上午1:37
 * 作者：张林
 * Email:zhanglin01@100tal.com
 * TODO:
 */

public class IMManager {
    private static IMManager instance = null;
    private ChatManager chatManager;

    private IMManager() {

    }

    public synchronized static IMManager getInstance() {
        if (instance == null) instance = new IMManager();
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
     * 创建聊天室
     *
     * @param roomName 房间名称
     */
    public MultiUserChat createRoom(String user, String roomName, String password) {
        XMPPConnection connection = IMConnectionManager.getInstance().getConnection();
        if (connection == null)
            return null;

        MultiUserChat muc = null;
        try {
            // 创建一个MultiUserChat
            muc = new MultiUserChat(connection, roomName + "@conference."
                    + connection.getServiceName());
            // 创建聊天室
            muc.create(roomName);
            // 获得聊天室的配置表单
            Form form = muc.getConfigurationForm();
            // 根据原始表单创建一个要提交的新表单。
            Form submitForm = form.createAnswerForm();
            // 向要提交的表单添加默认答复
            for (Iterator<FormField> fields = form.getFields(); fields
                    .hasNext(); ) {
                FormField field = (FormField) fields.next();
                if (!FormField.TYPE_HIDDEN.equals(field.getType())
                        && field.getVariable() != null) {
                    // 设置默认值作为答复
                    submitForm.setDefaultAnswer(field.getVariable());
                }
            }
            // 设置聊天室的新拥有者
            List<String> owners = new ArrayList<String>();
            owners.add(connection.getUser());// 用户JID
            submitForm.setAnswer("muc#roomconfig_roomowners", owners);
            // 设置聊天室是持久聊天室，即将要被保存下来
            submitForm.setAnswer("muc#roomconfig_persistentroom", true);
            // 房间仅对成员开放
            submitForm.setAnswer("muc#roomconfig_membersonly", false);
            // 允许占有者邀请其他人
            submitForm.setAnswer("muc#roomconfig_allowinvites", true);
            if (!password.equals("")) {
                // 进入是否需要密码
                submitForm.setAnswer("muc#roomconfig_passwordprotectedroom",
                        true);
                // 设置进入密码
                submitForm.setAnswer("muc#roomconfig_roomsecret", password);
            }
            // 能够发现占有者真实 JID 的角色
            // submitForm.setAnswer("muc#roomconfig_whois", "anyone");
            // 登录房间对话
            submitForm.setAnswer("muc#roomconfig_enablelogging", true);
            // 仅允许注册的昵称登录
            submitForm.setAnswer("x-muc#roomconfig_reservednick", true);
            // 允许使用者修改昵称
            submitForm.setAnswer("x-muc#roomconfig_canchangenick", false);
            // 允许用户注册房间
            submitForm.setAnswer("x-muc#roomconfig_registration", false);
            // 发送已完成的表单（有默认值）到服务器来配置聊天室
            muc.sendConfigurationForm(submitForm);
        } catch (XMPPException e) {
            e.printStackTrace();
            return null;
        }
        return muc;
    }


    /**
     * 获取某个组里面的所有好友
     *
     * @param groupName
     * @return
     */
    public List<RosterEntry> getEntriesByGroup(String groupName) {
        XMPPConnection connection = IMConnectionManager.getInstance().getConnection();
        if (connection == null)
            return null;
        List<RosterEntry> Entrieslist = new ArrayList<RosterEntry>();
        RosterGroup rosterGroup = connection.getRoster().getGroup(
                groupName);
        Collection<RosterEntry> rosterEntry = rosterGroup.getEntries();
        Iterator<RosterEntry> i = rosterEntry.iterator();
        while (i.hasNext()) {
            Entrieslist.add(i.next());
        }
        return Entrieslist;
    }

    /**
     * 获取所有好友信息
     *
     * @return
     */
    public List<RosterEntry> getAllEntries() {
        XMPPConnection connection = IMConnectionManager.getInstance().getConnection();
        if (connection == null)
            return null;
        List<RosterEntry> Entrieslist = new ArrayList<RosterEntry>();
        Collection<RosterEntry> rosterEntry = connection.getRoster()
                .getEntries();
        Iterator<RosterEntry> i = rosterEntry.iterator();
        while (i.hasNext()) {
            Entrieslist.add(i.next());
        }
        return Entrieslist;
    }

    /**
     * 获取用户VCard信息
     *
     * @param user
     * @return
     * @throws XMPPException
     */
    public VCard getUserVCard(String user) {
        XMPPConnection connection = IMConnectionManager.getInstance().getConnection();
        if (connection == null)
            return null;
        VCard vcard = new VCard();
        try {
            vcard.load(connection, user);
        } catch (XMPPException e) {
            e.printStackTrace();
        }
        return vcard;
    }

    /**
     * 添加一个分组
     *
     * @param groupName
     * @return
     */
    public boolean addGroup(String groupName) {
        XMPPConnection connection = IMConnectionManager.getInstance().getConnection();
        if (connection == null)
            return false;
        try {
            connection.getRoster().createGroup(groupName);
            Log.e("addGroup", groupName + "創建成功");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean joinChatRoom(String roomId, String psw) {
        XMPPConnection connection = IMConnectionManager.getInstance().getConnection();
        if (connection == null)
            return false;
        try {
            MultiUserChat muc = MultiUserChatManager.getInstance().getMultiUserChat(connection, roomId);
            DiscussionHistory history = new DiscussionHistory();
            history.setMaxStanzas(0);
            muc.join(connection.getUser(), psw, history, SmackConfiguration.getPacketReplyTimeout());
            return true;
        } catch (XMPPException e) {
            e.printStackTrace();
            return false;
        }

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

    /**
     * 离开聊天室
     *
     * @param roomId
     */
    public void leaveChatRoom(String roomId) {
        XMPPConnection connection = IMConnectionManager.getInstance().getConnection();
        if (connection == null)
            return;
        MultiUserChat multiUserChat = MultiUserChatManager.getInstance().getMultiUserChat(connection, roomId);
        if (multiUserChat.isJoined())
            multiUserChat.leave();
    }

    /**
     * 邀请加入聊天室
     * @param user
     * @param roomId
     * @param inviteMsg
     */
    public void inviteToChatRoom(String user, String roomId, String inviteMsg) {
        XMPPConnection connection = IMConnectionManager.getInstance().getConnection();
        if (connection == null)
            return;
        MultiUserChat multiUserChat = MultiUserChatManager.getInstance().getMultiUserChat(connection, roomId);
        multiUserChat.invite(user, inviteMsg);
    }
}