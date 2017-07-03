package com.testxmpp.im.modules;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.testxmpp.im.manager.ChatRoomManager;

import org.jivesoftware.smackx.muc.MultiUserChat;

/**
 * Created by zhanglin on 2017/7/3.
 */

public class ChatRoomModule extends ReactContextBaseJavaModule {
    @Override
    public String getName() {
        return "ChatRoomModule";
    }

    public ChatRoomModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }


    /**
     * 创建聊天室
     *
     * @param nick
     * @param roomName
     * @param promise
     */
    @ReactMethod
    public void createRoom(String nick, String roomName, String psw, Promise promise) {
        MultiUserChat muc = ChatRoomManager.getInstance().createRoom(nick, roomName, psw);
        if (muc == null) {
            promise.reject("101", "创建聊天室失败");
        } else {
            promise.resolve("创建聊天室成功");
        }
    }

    /**
     * 加入聊天室
     *
     * @param roomId
     * @param promise
     */
    @ReactMethod
    public void joinChatRoom(String roomId, String psw, Promise promise) {
        boolean isSuccess = ChatRoomManager.getInstance().joinChatRoom(roomId, psw);
        if (isSuccess) {
            promise.resolve("加入聊天室成功");
        } else {
            promise.reject("101", "加入聊天室失败");
        }
    }

    /**
     * 离开聊天室
     *
     * @param roomID
     */
    @ReactMethod
    public void leave(String roomID) {
        ChatRoomManager.getInstance().leaveChatRoom(roomID);
    }

    /**
     * 获取聊天室成员
     *
     * @param roomID
     */
    @ReactMethod
    public void getAllChatRoomMember(String roomID) {
    }

    /**
     * 邀请加入聊天室
     *
     * @param user
     * @param roomId
     * @param psw
     * @param inviteMsg
     */
    @ReactMethod
    public void inviteUserToChatRoom(String user, String roomId, String psw, String inviteMsg) {
        ChatRoomManager.getInstance().inviteToChatRoom(user, roomId, inviteMsg);
    }

    /**
     * 拒绝加入聊天室
     *
     * @param roomId
     * @param inviter
     * @param declineMsg
     */
    @ReactMethod
    public void rejectJoinChatRoom(String roomId, String inviter, String declineMsg) {
        ChatRoomManager.getInstance().declineChatRoomInviter(roomId, inviter, declineMsg);
    }
}
