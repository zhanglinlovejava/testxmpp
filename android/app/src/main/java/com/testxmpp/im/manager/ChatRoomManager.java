package com.testxmpp.im.manager;

import com.testxmpp.im.listener.ChatRoomInvitationListener;
import com.testxmpp.im.listener.ChatRoomRejectInvitationListener;
import com.testxmpp.im.listener.ChatRoomStatusListener;

import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.FormField;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.MultiUserChat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * created by zhanglin on 2017/7/2
 */
public class ChatRoomManager {
    private static ChatRoomManager instance = null;
    private ChatRoomInvitationListener chatRoomInvitationListener;
    private ChatRoomRejectInvitationListener chatRoomRejectInvitationListener;
    private ChatRoomStatusListener chatRoomStatusListener;

    private ChatRoomManager() {

    }

    public synchronized static ChatRoomManager getInstance() {
        if (instance == null) instance = new ChatRoomManager();
        return instance;
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
            submitForm.setAnswer("muc#roomconfig_whois", "anyone");
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

            //注册聊天室邀请被拒绝监听
            registerChatRoomRejectInvitationListener(muc);
            //注册聊天室成员变化监听
            registerChatRoomStatusListener(muc);
        } catch (XMPPException e) {
            e.printStackTrace();
            return null;
        }
        return muc;
    }


    /**
     * 加入聊天室
     *
     * @param roomId
     * @param psw
     * @return
     */
    public boolean joinChatRoom(String roomId, String psw) {
        XMPPConnection connection = IMConnectionManager.getInstance().getConnection();
        if (connection == null)
            return false;
        try {
            MultiUserChat muc = MultiUserChatManager.getInstance().getMultiUserChat(connection, roomId);
            DiscussionHistory history = new DiscussionHistory();
            history.setMaxStanzas(0);
            muc.join(connection.getUser(), psw, history, SmackConfiguration.getPacketReplyTimeout());
            //注册聊天室邀请被拒绝监听
            registerChatRoomRejectInvitationListener(muc);
            //注册聊天室成员变化监听
            registerChatRoomStatusListener(muc);
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
        if (multiUserChat.isJoined()) {
            multiUserChat.leave();
            unRegisterChatRoomStatusListener(multiUserChat);
        }
    }

    /**
     * 邀请加入聊天室
     *
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

    /**
     * 注册聊天室拒绝邀请监听
     *
     * @param muc
     */
    public void registerChatRoomRejectInvitationListener(MultiUserChat muc) {
        if (muc == null) return;
        if (chatRoomRejectInvitationListener == null)
            chatRoomRejectInvitationListener = new ChatRoomRejectInvitationListener();
        muc.addInvitationRejectionListener(chatRoomRejectInvitationListener);
    }

    /**
     * 注销聊天室拒绝监听
     */

    public void unRegisterChatRoomRejectListener(MultiUserChat muc) {
        if (muc == null) return;
        if (chatRoomRejectInvitationListener != null) {
            muc.removeInvitationRejectionListener(chatRoomRejectInvitationListener);
        }
    }

    /**
     * 注册聊天室成员变化监听
     *
     * @param muc
     */
    public void registerChatRoomStatusListener(MultiUserChat muc) {
        if (muc == null) return;
        if (chatRoomStatusListener == null)
            chatRoomStatusListener = new ChatRoomStatusListener(muc.getRoom());
        muc.addParticipantStatusListener(chatRoomStatusListener);
    }

    public void unRegisterChatRoomStatusListener(MultiUserChat muc) {
        if (muc == null) return;
        if (chatRoomStatusListener != null) {
            muc.removeParticipantStatusListener(chatRoomStatusListener);
        }
    }

    /**
     * 注册会议室邀请监听
     */
    public void registerChatRoomInviterListener() {
        XMPPConnection connection = IMConnectionManager.getInstance().getConnection();
        if (connection == null)
            return;
        if (chatRoomInvitationListener == null) {
            chatRoomInvitationListener = new ChatRoomInvitationListener();
        }
        MultiUserChat.addInvitationListener(connection, chatRoomInvitationListener);

    }

    /**
     * 注销会议室邀请监听事件
     */
    public void unRegisterChatRoomInvitationListener() {
        XMPPConnection connection = IMConnectionManager.getInstance().getConnection();
        if (connection == null)
            return;
        if (chatRoomInvitationListener != null) {
            try {
                MultiUserChat.removeInvitationListener(connection, chatRoomInvitationListener);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 拒绝接受聊天室的邀请
     *
     * @param roomId
     * @param inviter
     * @param declineMsg
     */
    public void declineChatRoomInviter(String roomId, String inviter, String declineMsg) {
        XMPPConnection connection = IMConnectionManager.getInstance().getConnection();
        if (connection == null)
            return;
        MultiUserChat.decline(connection, roomId, inviter, declineMsg);
    }
}