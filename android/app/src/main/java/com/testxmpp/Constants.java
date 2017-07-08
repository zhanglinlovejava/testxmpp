package com.testxmpp;


/**
 * created by zhanglin on 2017/7/2
 */
public class Constants {
    public static final String host = "10.0.115.12";
    public static final String server_name = "127.0.0.1";
    public static final int port = 5222;
    public static String user_jid = "";


    //chartoom status type
    public static final String STATUS_JOINED = "status_joined";
    public static final String STATUS_LEFT = "status_left";
    public static final String STATUS_KICKED = "status_kicked";
    public static final String STATUS_VOICE_GRANTED = "status_voice_granted";
    public static final String STATUS_VOICE_REVOKED = "status_voice_revoked";
    public static final String STATUS_BANNED = "status_banned";
    public static final String STATUS_MEMBER_SHIP_GRANTED = "status_member_ship_granted";
    public static final String STATUS_MEMBER_SHIP_REVOKED = "status_member_ship_revoked";
    public static final String STATUS_MODERATOR_GRANTED = "status_moderator_granted";
    public static final String STATUS_MODERATOR_REVOKED = "status_moderator_revoked";
    public static final String STATUS_OWNERSHIP_GRANTED = "status_ownership_granted";
    public static final String STATUS_OWNERSHIP_REVOKED = "status_ownership_revoked";
 

    //add friend type
    public static final String TYPE_SUBSCRIBE = "type_subscribe";//收到好友请求
    public static final String TYPE_UNSUBSCRIBE = "type_unsubscribe";//删除好友
    public static final String TYPE_SUBSCRIBED = "type_subscribed";//同意好友请求
    public static final String TYPE_UNSUBSCRIBED = "type_unsubscribed";//拒绝对方的请求
    public static final String TYPE_AVAILABLE = "type_available";//对方上线
    public static final String TYPE_UNAVAILABLE = "type_unavailable";//对方下线
}
