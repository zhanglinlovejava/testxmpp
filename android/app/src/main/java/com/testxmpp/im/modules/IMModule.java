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
import com.testxmpp.im.event.EventChatRoomStatus;
import com.testxmpp.im.event.EventFriendEvent;
import com.testxmpp.im.event.EventInviterChatRoom;
import com.testxmpp.im.event.EventNewMsg;
import com.testxmpp.im.event.EventRejectInvitation;
import com.testxmpp.im.event.RxBus;
import com.testxmpp.im.manager.ChatRoomManager;
import com.testxmpp.im.manager.IMConnectionManager;
import com.testxmpp.im.manager.MultiUserChatManager;

import org.jivesoftware.smack.packet.Message;

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

    /**
     * 登录
     *
     * @param username
     * @param psw
     * @param promise
     */
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

    /**
     * 注册监听
     */
    public void registerMsgObserver() {
        unRegisterReceiver();
        ChatRoomManager.getInstance().registerChatRoomInviterListener();
        if (subscriptions==null)subscriptions = new CompositeSubscription();
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
                            sendDeviceEmitter(map, "event_peer_new_msg");
                        } else if (msg.getType() == Message.Type.groupchat) {
                            sendDeviceEmitter(map, "event_group_new_msg");
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
                        sendDeviceEmitter(map, "event_inviter_chatroom");
                    }
                }));
        subscriptions.add(RxBus.getDefault().toObserverable(EventRejectInvitation.class)
                .subscribe(new Action1<EventRejectInvitation>() {
                    @Override
                    public void call(EventRejectInvitation event) {
                        WritableMap map = Arguments.createMap();
                        map.putString("from", event.from);
                        map.putString("reason", event.reason);
                        sendDeviceEmitter(map, "event_reject_inviter_chatroom");
                    }
                }));
        subscriptions.add(RxBus.getDefault().toObserverable(EventChatRoomStatus.class)
                .subscribe(new Action1<EventChatRoomStatus>() {
                    @Override
                    public void call(EventChatRoomStatus event) {
                        WritableMap map = Arguments.createMap();
                        map.putString("type", event.type);
                        map.putString("roomId", event.roomId);
                        map.putString("user", event.user);
                        sendDeviceEmitter(map, "chatroom_status_event");
                    }
                }));
        subscriptions.add(RxBus.getDefault().toObserverable(EventFriendEvent.class)
                .subscribe(new Action1<EventFriendEvent>() {
                    @Override
                    public void call(EventFriendEvent event) {
                        WritableMap map = Arguments.createMap();
                        map.putString("type", event.type);
                        map.putString("from", event.from);
                        sendDeviceEmitter(map, "event_friend");
                    }
                }));
    }

    /**
     * 注销监听
     */
    public void unRegisterReceiver() {
        if (subscriptions != null && !subscriptions.isUnsubscribed()) {
            subscriptions.unsubscribe();
            subscriptions.clear();
            subscriptions = null;
        }
        ChatRoomManager.getInstance().unRegisterChatRoomInvitationListener();
        MultiUserChatManager.getInstance().onDestroy();
    }

    private void sendDeviceEmitter(WritableMap map, String eventName) {
        mContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, map);
    }
}
