package com.testxmpp.im;

import android.text.TextUtils;

import com.orhanobut.logger.Logger;
import com.testxmpp.Constants;
import com.testxmpp.im.event.EventNewMsg;
import com.testxmpp.im.event.RxBus;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;

/**
 * 时间: 2017/7/2 上午1:52
 * 作者：张林
 * Email:zhanglin01@100tal.com
 * TODO:
 */

public class IMMessageListener implements PacketListener {

    @Override
    public void processPacket(Packet packet) {
        if (packet instanceof Presence) {
            Presence presence = (Presence) packet;
            String s1[] = new String[0], s2[];
            // Presence.Type有7中状态
            if (presence.getType().equals(Presence.Type.subscribe)) {// 好友申请
                s1 = presence.getFrom().toUpperCase().split("@");// 发送方
                s2 = presence.getTo().toUpperCase().split("@");// 接收方
                Logger.e(s1[0] + "\t好友申请加为好友\t type="
                        + presence.getType().toString());

            } else if (presence.getType().equals(Presence.Type.subscribed)) {// 同意添加好友
                Logger.e(s1[0] + "\t同意添加好友\t type="
                        + presence.getType().toString());

            } else if (presence.getType().equals(Presence.Type.unsubscribe)) {// 删除好友

                Logger.e(s1[0] + "\t 删除好友");


            } else if (presence.getType().equals(Presence.Type.unsubscribed)) {// 拒绝对放的添加请求

                Logger.e(s1[0] + "\t 拒绝添加好友");

            } else if (presence.getType().equals(Presence.Type.unavailable)) {// 好友下线
                Logger.e(s1[0] + "\t 下线了");
            } else if (presence.getType().equals(Presence.Type.available)) {// 好友上线
                //0.在线 1.Q我吧 2.忙碌 3.勿扰 4.离开 5.隐身 6.离线
                if (presence.getMode() == Presence.Mode.chat) {//Q我吧
                    Logger.e(s1[0] + "\t 的状态改为了 Q我吧");
                } else if (presence.getMode() == Presence.Mode.dnd) {//忙碌
                    Logger.e(s1[0] + "\t 的状态改为了 忙碌了");
                } else if (presence.getMode() == Presence.Mode.xa) {//忙碌
                    Logger.e(s1[0] + "\t 的状态改为了 勿扰了");
                } else if (presence.getMode() == Presence.Mode.away) {//离开
                    Logger.e(s1[0] + "\t 的状态改为了 离开了");
                } else {
                    Logger.e(s1[0] + "\t 的状态改为了 上线了");
                }


            }

        } else if (packet instanceof Message) {
            Logger.e(packet.toXML());
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
                Logger.e(packet.toXML());
            }
        }
    }
}
