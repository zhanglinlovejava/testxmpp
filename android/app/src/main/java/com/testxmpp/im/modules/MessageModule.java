package com.testxmpp.im.modules;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.testxmpp.im.manager.MessageManger;

/**
 * Created by zhanglin on 2017/7/3.
 */

public class MessageModule extends ReactContextBaseJavaModule {
    public MessageModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "MessageModule";
    }


    /**
     * 发送消息
     *
     * @param toID
     * @param msg
     * @param promise
     */
    @ReactMethod
    public void sendMsg(String toID, String msg, Promise promise) {
        boolean isSuccess = MessageManger.getInstance().sendMsg(toID, msg);
        if (isSuccess)
            promise.resolve("发送成功");
        else promise.reject("101", "发送失败");
    }

    /**
     * 发送聊天室消息
     *
     * @param roomId
     * @param msg
     * @param promise
     */
    @ReactMethod
    public void sendGroupMsg(String roomId, String msg, Promise promise) {
        boolean isSuccess = MessageManger.getInstance().sendChatRoomMsg(roomId, msg);
        if (isSuccess) promise.resolve("发送成功");
        else promise.reject("101", "发送失败");

    }
}
