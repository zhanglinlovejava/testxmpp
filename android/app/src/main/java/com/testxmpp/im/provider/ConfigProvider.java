package com.testxmpp.im.provider;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.ServiceDiscoveryManager;
import org.jivesoftware.smackx.bytestreams.socks5.provider.BytestreamsProvider;
import org.jivesoftware.smackx.packet.ChatStateExtension;
import org.jivesoftware.smackx.packet.OfflineMessageInfo;
import org.jivesoftware.smackx.packet.OfflineMessageRequest;
import org.jivesoftware.smackx.provider.AdHocCommandDataProvider;
import org.jivesoftware.smackx.provider.DataFormProvider;
import org.jivesoftware.smackx.provider.DiscoverInfoProvider;
import org.jivesoftware.smackx.provider.DiscoverItemsProvider;
import org.jivesoftware.smackx.provider.MUCAdminProvider;
import org.jivesoftware.smackx.provider.MUCOwnerProvider;
import org.jivesoftware.smackx.provider.MUCUserProvider;
import org.jivesoftware.smackx.provider.StreamInitiationProvider;
import org.jivesoftware.smackx.provider.VCardProvider;
import org.jivesoftware.smackx.provider.XHTMLExtensionProvider;

/**
 * 时间: 2017/7/1 下午11:28
 * 作者：张林
 * Email:zhanglin01@100tal.com
 * TODO:
 */

public class ConfigProvider {
    public static void configure(ProviderManager pm) {
        // Service Discovery # Items
        pm.addIQProvider("query", "http://jabber.org/protocol/disco#items", new DiscoverItemsProvider());
        // Service Discovery # Info
        pm.addIQProvider("query", "http://jabber.org/protocol/disco#info", new DiscoverInfoProvider());

        // Service Discovery # Items
        pm.addIQProvider("query", "http://jabber.org/protocol/disco#items", new DiscoverItemsProvider());
        // Service Discovery # Info
        pm.addIQProvider("query", "http://jabber.org/protocol/disco#info", new DiscoverInfoProvider());

        //Offline Message Requests
        pm.addIQProvider("offline", "http://jabber.org/protocol/offline", new OfflineMessageRequest.Provider());
        //Offline Message Indicator
        pm.addExtensionProvider("offline", "http://jabber.org/protocol/offline", new OfflineMessageInfo.Provider());

        //vCard
        pm.addIQProvider("vCard", "vcard-temp", new VCardProvider());

        //FileTransfer
        pm.addIQProvider("si", "http://jabber.org/protocol/si", new StreamInitiationProvider());
        pm.addIQProvider("query", "http://jabber.org/protocol/bytestreams", new BytestreamsProvider());
//        pm.addIQProvider("open","http://jabber.org/protocol/ibb", new IBBProviders.Open());
//        pm.addIQProvider("close","http://jabber.org/protocol/ibb", new IBBProviders.Close());
//        pm.addExtensionProvider("data","http://jabber.org/protocol/ibb", new IBBProviders.Data());
        //Data Forms
        pm.addExtensionProvider("x", "jabber:x:data", new DataFormProvider());
        //Html
        pm.addExtensionProvider("html", "http://jabber.org/protocol/xhtml-im", new XHTMLExtensionProvider());
        //Ad-Hoc Command
        pm.addIQProvider("command", "http://jabber.org/protocol/commands", new AdHocCommandDataProvider());
        // Chat State
        ChatStateExtension.Provider chatState = new ChatStateExtension.Provider();
        pm.addExtensionProvider("active", "http://jabber.org/protocol/chatstates", chatState);
        pm.addExtensionProvider("composing", "http://jabber.org/protocol/chatstates",
                chatState);
        pm.addExtensionProvider("paused", "http://jabber.org/protocol/chatstates", chatState);
        pm.addExtensionProvider("inactive", "http://jabber.org/protocol/chatstates", chatState);
        pm.addExtensionProvider("gone", "http://jabber.org/protocol/chatstates", chatState);
        //MUC User,Admin,Owner
        pm.addExtensionProvider("x", "http://jabber.org/protocol/muc#user", new MUCUserProvider());
        pm.addIQProvider("query", "http://jabber.org/protocol/muc#admin", new MUCAdminProvider());
        pm.addIQProvider("query", "http://jabber.org/protocol/muc#owner", new MUCOwnerProvider());
    }

    public static void initFeatures(XMPPConnection xmppConnection) {
        ServiceDiscoveryManager.setIdentityName("Android_IM");
        ServiceDiscoveryManager.setIdentityType("phone");
        ServiceDiscoveryManager sdm = ServiceDiscoveryManager.getInstanceFor(xmppConnection);
        if (sdm == null) {
            sdm = new ServiceDiscoveryManager(xmppConnection);
        }
        sdm.addFeature("http://jabber.org/protocol/disco#info");
        sdm.addFeature("http://jabber.org/protocol/caps");
        sdm.addFeature("urn:xmpp:avatar:metadata");
        sdm.addFeature("urn:xmpp:avatar:metadata+notify");
        sdm.addFeature("urn:xmpp:avatar:data");
        sdm.addFeature("http://jabber.org/protocol/nick");
        sdm.addFeature("http://jabber.org/protocol/nick+notify");
        sdm.addFeature("http://jabber.org/protocol/xhtml-im");
        sdm.addFeature("http://jabber.org/protocol/muc");
        sdm.addFeature("http://jabber.org/protocol/commands");
        sdm.addFeature("http://jabber.org/protocol/si/profile/file-transfer");
        sdm.addFeature("http://jabber.org/protocol/si");
        sdm.addFeature("http://jabber.org/protocol/bytestreams");
        sdm.addFeature("http://jabber.org/protocol/ibb");
        sdm.addFeature("http://jabber.org/protocol/feature-neg");
        sdm.addFeature("jabber:iq:privacy");
    }

}
