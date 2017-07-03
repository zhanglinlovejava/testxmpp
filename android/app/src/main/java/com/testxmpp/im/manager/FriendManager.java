package com.testxmpp.im.manager;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * created by zhanglin on 2017/7/2
 */

public class FriendManager {

    private static FriendManager instance = null;

    private FriendManager() {
    }

    public synchronized static FriendManager getInstance() {
        if (instance == null) instance = new FriendManager();
        return instance;
    }


    /**
     * 获取某个组里面的所有好友
     *
     * @param groupName
     * @return
     */
    public List<RosterEntry> getEntriesByGroup(String groupName) {
        XMPPConnection connection = IMConnectionManager.getInstance().getConnection();
        if (connection == null)
            return null;
        List<RosterEntry> Entrieslist = new ArrayList<RosterEntry>();
        RosterGroup rosterGroup = connection.getRoster().getGroup(
                groupName);
        Collection<RosterEntry> rosterEntry = rosterGroup.getEntries();
        Iterator<RosterEntry> i = rosterEntry.iterator();
        while (i.hasNext()) {
            Entrieslist.add(i.next());
        }
        return Entrieslist;
    }

    /**
     * 获取所有好友信息
     *
     * @return
     */
    public List<RosterEntry> getAllEntries() {
        Roster roster = getRoster();
        if (roster == null) return null;
        List<RosterEntry> Entrieslist = new ArrayList<RosterEntry>();
        Collection<RosterEntry> rosterEntry = roster.getEntries();
        Iterator<RosterEntry> i = rosterEntry.iterator();
        while (i.hasNext()) {
            Entrieslist.add(i.next());
        }
        return Entrieslist;
    }

    private Roster getRoster() {
        XMPPConnection connection = IMConnectionManager.getInstance().getConnection();
        if (connection == null)
            return null;
        return connection.getRoster();
    }

    /**
     * 获取所有组
     *
     * @return 所有组集合
     */
    public List<RosterGroup> getGroups() {
        Roster roster = getRoster();
        if (roster == null) return null;
        List<RosterGroup> grouplist = new ArrayList<RosterGroup>();
        Collection<RosterGroup> rosterGroup = roster.getGroups();
        Iterator<RosterGroup> i = rosterGroup.iterator();
        while (i.hasNext()) {
            grouplist.add(i.next());
        }
        return grouplist;
    }

    /**
     * 添加一个分组
     *
     * @param groupName
     * @return
     */
    public boolean addGroup(String groupName) {
        try {
            getRoster().createGroup(groupName);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 添加好友 无分组
     *
     * @param userName
     * @param name
     * @return
     */
    public boolean addUser(String userName, String name) {
        try {
            getRoster().createEntry(userName, name, null);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 添加好友 有分组
     *
     * @param userName
     * @param name
     * @param groupName
     * @return
     */
    public boolean addUserToGroup(String userName, String name,
                                  String groupName) {
        try {
            getRoster().createEntry(userName, name, new String[]{groupName});
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 发送同意好友请求的packet
     *
     * @param from
     * @return
     */
    public void agreeFriendRequest(String from) {
        XMPPConnection connection = IMConnectionManager.getInstance().getConnection();
        if (connection == null)
            return;
        Presence presenceRes = new Presence(Presence.Type.subscribed);
        presenceRes.setTo(from);
        connection.sendPacket(presenceRes);
    }

    /**
     * 发送拒绝好友请求的packet
     *
     * @param from
     */
    public void rejectFriendRequest(String from) {
        XMPPConnection connection = IMConnectionManager.getInstance().getConnection();
        if (connection == null)
            return;
        Presence presenceRes = new Presence(Presence.Type.unsubscribe);
        presenceRes.setTo(from);
        connection.sendPacket(presenceRes);
    }

    /**
     * 删除好友
     *
     * @param user
     * @return
     */
    public boolean deleteFriend(String user) {
        Roster roster = getRoster();
        if (roster == null) return false;
        try {
            RosterEntry entry = roster.getEntry(user);
            if (entry != null) {
                roster.removeEntry(entry);
                return true;
            } else {
                return false;
            }

        } catch (XMPPException e) {
            e.printStackTrace();
            return false;
        }
    }
}
