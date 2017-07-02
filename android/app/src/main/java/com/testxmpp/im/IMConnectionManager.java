package com.testxmpp.im;

import android.util.Log;

import com.orhanobut.logger.Logger;
import com.testxmpp.Constants;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.provider.ProviderManager;


/**
 * 时间: 2017/7/1 下午10:10
 * 作者：张林
 * Email:zhanglin01@100tal.com
 * TODO:
 */

public class IMConnectionManager {
    private static IMConnectionManager instance = null;
    public XMPPConnection connection;

    private IMConnectionManager() {
    }

    public synchronized static IMConnectionManager getInstance() {
        if (instance == null) instance = new IMConnectionManager();
        return instance;
    }

    //打開連接
    public void openConnection() {
        ConfigProvider.configure(ProviderManager.getInstance());
        ConnectionConfiguration connConfig = new ConnectionConfiguration(Constants.host, Constants.port);
        connConfig.setSASLAuthenticationEnabled(false);
        connConfig.setReconnectionAllowed(true);
        connConfig.setSendPresence(false);
        // connConfig.setSecurityMode(ConnectionConfiguration.SecurityMode.enabled);
        connConfig.setSendPresence(true);
        connection = new XMPPConnection(connConfig);
        connection.DEBUG_ENABLED = true;

        Roster.setDefaultSubscriptionMode(Roster.SubscriptionMode.manual);
        try {
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

            connection.addConnectionListener(new ConnectionListener() {

                @Override
                public void reconnectionSuccessful() {
                    // TODO Auto-generated method stub
                    Logger.e("重连成功");
                }

                @Override
                public void reconnectionFailed(Exception arg0) {
                    // TODO Auto-generated method stub
                    Logger.e("重连失败");
//                    User user = SaveUserUtil.loadAccount(context);
//                    login(user.getUser(), user.getPassword());


                }

                @Override
                public void reconnectingIn(int arg0) {
                    // TODO Auto-generated method stub
                    Logger.e("重连中");
                }

                @Override
                public void connectionClosedOnError(Exception e) {
                    // TODO Auto-generated method stub
                    Logger.e("连接出错");
                    if (e.getMessage().contains("conflict")) {
                        Logger.e("被挤掉了");
//                        disConnectServer();

                    }
                }

                @Override
                public void connectionClosed() {
                    // TODO Auto-generated method stub
                    Logger.e("连接关闭");
                }
            });

        } catch (XMPPException e) {
            Logger.e(Log.getStackTraceString(e));
        }
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
                connection.addPacketListener(new IMMessageListener(), new PacketFilter() {
                    @Override
                    public boolean accept(Packet packet) {
                        return true;
                    }
                });
                Constants.user_jid = connection.getUser();
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
        connConfig.setSendPresence(false);
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
    }

    public XMPPConnection getConnection() {
        if (connection != null && connection.isConnected())
            return connection;
        else return null;
    }
}