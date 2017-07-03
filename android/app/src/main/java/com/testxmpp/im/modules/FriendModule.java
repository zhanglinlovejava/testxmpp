package com.testxmpp.im.modules;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.orhanobut.logger.Logger;
import com.testxmpp.im.manager.FriendManager;

import org.jivesoftware.smack.RosterEntry;

import java.util.List;

/**
 * Created by zhanglin on 2017/7/3.
 */

public class FriendModule extends ReactContextBaseJavaModule {
    public FriendModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "FriendModule";
    }


    /**
     * 添加好友
     *
     * @param targetId
     */
    @ReactMethod
    public void addUser(String targetId, String name, Promise promise) {
        boolean isSuccess = FriendManager.getInstance().addUser(targetId, name);
        if (isSuccess) promise.resolve("请求发送成功");
        else promise.reject("101", "请求发送失败");
    }

    /**
     * 添加好友到指定分组
     *
     * @param targetId
     */
    @ReactMethod
    public void addUserToGroup(String targetId, String name, String groupName, Promise promise) {
        boolean isSuccess = FriendManager.getInstance().addUserToGroup(targetId, name, groupName);
        if (isSuccess) promise.resolve("请求发送成功");
        else promise.reject("101", "请求发送失败");
    }

    /**
     * 获取所有好友列表
     */
    @ReactMethod
    public void getAllEntries() {
        List<RosterEntry> list = FriendManager.getInstance().getAllEntries();
        if (list == null) return;
        for (RosterEntry entry : list) {
            Logger.e(entry.getUser() + "---好友");
        }
    }

    /**
     * 添加一个分组
     *
     * @param groupName
     */
    @ReactMethod
    public void addGroup(String groupName, Promise promise) {
        boolean isSuccess = FriendManager.getInstance().addGroup(groupName);
        if (isSuccess) promise.resolve("创建分组成功");
        else promise.reject("101", "创建分组失败");
    }

    /**
     * 同意好友请求
     *
     * @param from
     */
    @ReactMethod
    public void agreeFriendRequest(String from) {
        FriendManager.getInstance().agreeFriendRequest(from);
    }

    /**
     * 拒绝好友请求
     *
     * @param from
     */
    @ReactMethod
    public void rejectFriendRequest(String from) {
        FriendManager.getInstance().rejectFriendRequest(from);
    }

    @ReactMethod
    public void deleteFriend(String user, Promise promise) {
        boolean isSuccess = FriendManager.getInstance().deleteFriend(user);
        if (isSuccess) promise.resolve("删除成功");
        else promise.reject("101", "删除失败");
    }
}
