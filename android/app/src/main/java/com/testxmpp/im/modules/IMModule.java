package com.testxmpp.im.modules;


import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.testxmpp.im.event.EventInviterChatRoom;
import com.testxmpp.im.event.EventNewMsg;
import com.testxmpp.im.event.RxBus;
import com.testxmpp.im.manager.ChatRoomManager;
import com.testxmpp.im.manager.IMConnectionManager;
import com.testxmpp.im.manager.MessageManger;

import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.muc.MultiUserChat;

import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by zhanglin on 2017/6/27.
 */

public class IMModule extends ReactContextBaseJavaModule implements LifecycleEventListener {
    private ReactContext mContext;
    private CompositeSubscription subscriptions;

    @Override
    public String getName() {
        return "IMModule";
    }

    public IMModule(ReactApplicationContext reactContext) {
        super(reactContext);
        mContext = reactContext;
    }

    private void registerMsgObserver() {
        ChatRoomManager.getInstance().registerChatRoomInviterListener();
        if (subscriptions == null) subscriptions = new CompositeSubscription();
        subscriptions.add(RxBus.getDefault().toObserverable(EventNewMsg.class)
                .subscribe(new Action1<EventNewMsg>() {
                    @Override
                    public void call(EventNewMsg event) {
                        Message msg = event.message;
                        if (msg == null) return;
                        WritableMap map = Arguments.createMap();
                        map.putString("fromId", msg.getFrom());
                        map.putString("txt", msg.getBody());
                        if (msg.getType() == Message.Type.chat) {
                            mContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                                    .emit("event_peer_new_msg", map);
                        } else if (msg.getType() == Message.Type.groupchat) {
                            mContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                                    .emit("event_group_new_msg", map);
                        }
                    }
                }));
        subscriptions.add(RxBus.getDefault().toObserverable(EventInviterChatRoom.class)
                .subscribe(new Action1<EventInviterChatRoom>() {
                    @Override
                    public void call(EventInviterChatRoom event) {
                        WritableMap map = Arguments.createMap();
                        map.putString("inviter", event.inivter);
                        map.putString("reason", event.reason);
                        map.putString("psw", event.psw);
                        map.putString("roomId", event.roomId);
                        mContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                                .emit("event_inviter_chatroom", map);
                    }
                }));
    }

    @ReactMethod
    public void login(String username, String psw, final Promise promise) {
        boolean isSucess = IMConnectionManager.getInstance().login(username, psw);
        if (isSucess) {
            promise.resolve("登陆成功");
            registerMsgObserver();
        } else {
            promise.reject("101", "登录失败");
        }
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


    /**
     * 发送消息
     *
     * @param toID
     * @param msg
     * @param promise
     */
    @ReactMethod
    public void sendMsg(String toID, String msg ,Promise promise) {
        boolean isSuccess = MessageManger.getInstance().sendMsg(toID, msg);
        if (isSuccess)
            promise.resolve("发送成功");
        else promise.reject("101", "发送失败");
    }

    @ReactMethod
    public void sendGroupMsg(String roomId, String msg, Promise promise) {
        boolean isSuccess = MessageManger.getInstance().sendChatRoomMsg(roomId, msg);
        if (isSuccess) promise.resolve("发送成功");
        else promise.reject("101", "发送失败");

    }




    /**
     * 添加好友
     *
     * @param targetId
     */
    @ReactMethod
    public void addFriend(String targetId) {
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


    @Override
    public void onHostResume() {

    }

    @Override
    public void onHostPause() {

    }

    @Override
    public void onHostDestroy() {
        unRegisterReceiver();
    }

    private void unRegisterReceiver() {
        if (subscriptions != null && !subscriptions.isUnsubscribed()) {
            subscriptions.unsubscribe();
            subscriptions.clear();
        }
    }
}
