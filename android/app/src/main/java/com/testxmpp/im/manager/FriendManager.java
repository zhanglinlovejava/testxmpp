package com.testxmpp.im.manager;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.XMPPConnection;

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
        XMPPConnection connection = IMConnectionManager.getInstance().getConnection();
        if (connection == null)
            return null;
        List<RosterEntry> Entrieslist = new ArrayList<RosterEntry>();
        Collection<RosterEntry> rosterEntry = connection.getRoster()
                .getEntries();
        Iterator<RosterEntry> i = rosterEntry.iterator();
        while (i.hasNext()) {
            Entrieslist.add(i.next());
        }
        return Entrieslist;
    }

    /**
     * 获取所有组
     *
     * @param roster
     * @return 所有组集合
     */
    public List<RosterGroup> getGroups(Roster roster) {
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
     * @param roster
     * @param groupName
     * @return
     */
    public static boolean addGroup(Roster roster, String groupName) {
        try {
            roster.createGroup(groupName);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
