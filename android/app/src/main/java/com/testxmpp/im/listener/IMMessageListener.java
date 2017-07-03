package com.testxmpp.im.listener;

import android.text.TextUtils;

import com.testxmpp.Constants;
import com.testxmpp.im.event.EventFriendEvent;
import com.testxmpp.im.event.EventNewMsg;
import com.testxmpp.im.event.RxBus;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;

/**
 * Created by zhanglin on 2017/7/3.
 */

public class IMMessageListener implements PacketListener {

    @Override
    public void processPacket(Packet packet) {
        if (packet instanceof Presence) {
            Presence presence = (Presence) packet;
            // Presence.Type有7中状态
            String from = presence.getFrom();
            if (from.contains(Constants.user_jid)) return;
            if (presence.getType().equals(Presence.Type.subscribe)) {// 好友申请
                RxBus.getDefault().post(new EventFriendEvent(Constants.TYPE_SUBSCRIBE, from));
            } else if (presence.getType().equals(Presence.Type.subscribed)) {// 同意添加好友
                RxBus.getDefault().post(new EventFriendEvent(Constants.TYPE_SUBSCRIBED, from));
            } else if (presence.getType().equals(Presence.Type.unsubscribe)) {// 删除好友
                RxBus.getDefault().post(new EventFriendEvent(Constants.TYPE_UNSUBSCRIBE, from));
            } else if (presence.getType().equals(Presence.Type.unsubscribed)) {// 拒绝对方的添加请求
                RxBus.getDefault().post(new EventFriendEvent(Constants.TYPE_UNSUBSCRIBED, from));
            } else if (presence.getType().equals(Presence.Type.unavailable)) {// 好友下线
                RxBus.getDefault().post(new EventFriendEvent(Constants.TYPE_UNAVAILABLE, from));
            } else if (presence.getType().equals(Presence.Type.available)) {// 好友上线
                RxBus.getDefault().post(new EventFriendEvent(Constants.TYPE_AVAILABLE, from));
//                //0.在线 1.Q我吧 2.忙碌 3.勿扰 4.离开 5.隐身 6.离线
//                if (presence.getMode() == Presence.Mode.chat) {//Q我吧
//                    Logger.e(from + "\t 的状态改为了 Q我吧");
//                } else if (presence.getMode() == Presence.Mode.dnd) {//忙碌
//                    Logger.e(from + "\t 的状态改为了 忙碌了");
//                } else if (presence.getMode() == Presence.Mode.xa) {//忙碌
//                    Logger.e(from + "\t 的状态改为了 勿扰了");
//                } else if (presence.getMode() == Presence.Mode.away) {//离开
//                    Logger.e(from + "\t 的状态改为了 离开了");
//                } else {
//                    Logger.e(from + "\t 的状态改为了 上线了");
//                }
            }
        } else if (packet instanceof Message) {
            Message msg = (Message) packet;
            if (!TextUtils.isEmpty(msg.getBody())) {
                if (msg.getType() == Message.Type.chat) {
                    RxBus.getDefault().post(new EventNewMsg(msg));
                } else if (msg.getType() == Message.Type.groupchat) {
                    if (!msg.getFrom().contains(Constants.user_jid)) {//去除自己发送的
                        RxBus.getDefault().post(new EventNewMsg(msg));
                    }
                }
            } else {
//                Logger.e(packet.toXML());
            }
        }
    }
}
