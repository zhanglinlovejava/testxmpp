package com.testxmpp;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;
import com.testxmpp.im.modules.ChatRoomModule;
import com.testxmpp.im.modules.FriendModule;
import com.testxmpp.im.modules.IMModule;
import com.testxmpp.im.modules.MessageModule;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by zhanglin on 2017/6/27.
 */

public class CustomPackage implements ReactPackage {
    @Override
    public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
        return Arrays.<NativeModule>asList(
                new IMModule(reactContext),
                new FriendModule(reactContext),
                new MessageModule(reactContext),
                new ChatRoomModule(reactContext)
        );
    }

    @Override
    public List<Class<? extends JavaScriptModule>> createJSModules() {
        return Collections.emptyList();
    }

    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
        return Collections.emptyList();
    }
}
