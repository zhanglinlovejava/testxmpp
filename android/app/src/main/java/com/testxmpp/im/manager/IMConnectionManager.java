package com.testxmpp.im.manager;


import com.orhanobut.logger.Logger;
import com.testxmpp.Constants;
import com.testxmpp.im.listener.IMMessageListener;
import com.testxmpp.im.provider.ConfigProvider;
import com.testxmpp.im.utils.BackgroundExecutor;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.provider.ProviderManager;


/**
 * created by zhanglin on 2017/7/2
 */

public class IMConnectionManager {
    private static IMConnectionManager instance = null;
    public XMPPConnection connection;
    private IMMessageListener imMessageListener;

    private IMConnectionManager() {
    }

    public synchronized static IMConnectionManager getInstance() {
        if (instance == null) instance = new IMConnectionManager();
        return instance;
    }

    /**
     * 登录
     *
     * @param name
     * @param pwd
     * @return
     */
    public boolean login(String name, String pwd) {

        try {
            initConnection();
            if (connection != null && connection.isConnected()) {
                // SASLAuthentication.supportSASLMechanism("PLAIN", 0);
                connection.login(name.toLowerCase(), pwd);
                connection.sendPacket(new Presence(Presence.Type.available));
                Constants.user_jid = connection.getUser();
                if (imMessageListener == null) {
                    imMessageListener = new IMMessageListener();
                }
                connection.addPacketListener(imMessageListener, new PacketFilter() {
                    @Override
                    public boolean accept(Packet packet) {
                        if (packet instanceof Presence || packet instanceof Message)
                            return true;
                        return false;
                    }
                });
                return true;
            }
        } catch (XMPPException e) {
            Logger.e(e.getMessage());
        }
        return false;
    }

    private void initConnection() throws XMPPException {
        ConfigProvider.configure(ProviderManager.getInstance());
        ConnectionConfiguration connConfig = new ConnectionConfiguration(Constants.host, Constants.port, Constants.server_name);
//            connConfig.setSASLAuthenticationEnabled(false);
        connConfig.setReconnectionAllowed(true);
        // connConfig.setSecurityMode(ConnectionConfiguration.SecurityMode.enabled);
        connConfig.setSendPresence(true);
        connection = new XMPPConnection(connConfig);
        XMPPConnection.DEBUG_ENABLED = true;
        Roster.setDefaultSubscriptionMode(Roster.SubscriptionMode.manual);
        if (connection.isConnected()) {// 首先判断是否还连接着服务器，需要先断开
            try {
                connection.disconnect();
            } catch (Exception e) {
                Logger.e("conn.disconnect() failed: " + e);
            }
        }
        SmackConfiguration.setPacketReplyTimeout(30000);// 设置超时时间
        SmackConfiguration.setKeepAliveInterval(-1);
        SmackConfiguration.setDefaultPingInterval(0);
        connection.connect();
        ConfigProvider.initFeatures(connection);
        connection.getRoster().setSubscriptionMode(Roster.SubscriptionMode.manual);
        connection.addConnectionListener(new ConnectionListener() {
            @Override
            public void reconnectionSuccessful() {
                Logger.e("重连成功");
            }

            @Override
            public void reconnectionFailed(Exception arg0) {
                Logger.e("重连失败");


            }

            @Override
            public void reconnectingIn(int arg0) {
                Logger.e("重连中");
            }

            @Override
            public void connectionClosedOnError(Exception e) {
                Logger.e("连接关闭" + e.getMessage());
                reLogin();
            }

            @Override
            public void connectionClosed() {
                Logger.e("连接关闭");
            }
        });

    }

    public void reLogin() {
        BackgroundExecutor.execute(new Runnable() {
            @Override
            public void run() {
                disconnect();
//                EventBus.getDefault().post(new EventBusEvent.ReloginEvent(conflict));//// TODO: 2017/7/3
            }
        });
    }


    public void disconnect() {
        BackgroundExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    if (connection!=null&&connection.isConnected())
                        connection.disconnect();
                } catch (Exception e) {
                    Logger.e(e);
                }
            }
        });

    }

    public XMPPConnection getConnection() {
        if (connection != null && connection.isConnected())
            return connection;
        else return null;
    }

}