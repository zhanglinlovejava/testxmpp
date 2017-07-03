import React, {Component} from 'react';
import {
    StyleSheet,
    NativeModules,
    ToastAndroid,
    DeviceEventEmitter,
    TextInput,
    Text,
    Alert,
    Button,
    View
} from 'react-native';
export default class main extends Component {
    constructor(props) {
        super(props);
        this.state = {
            isOnline: "不在线",
            text: "",
            from: "",
            userName: "",
            psw: "",
            toName: "",
            addFriendId: "",
            roomID: "",
            newRoomName: "",
            inviteRoomName: "",
            inviteName: "",
            groupName: ""

        }
    }

    render() {
        return (
            <View>
                <Text style={{fontSize: 30, color: 'green'}}> {this.state.isOnline}{this.state.userName}</Text>
                <View style={styles.row}>
                    <TextInput
                        style={{height: 40, width: 120}}
                        onChangeText={(text) => this.setState({userName: text})}
                        value={this.state.userName}
                        placeholder={"用户名"}
                    />
                    <TextInput
                        style={{height: 40, width: 120}}
                        onChangeText={(text) => this.setState({psw: text})}
                        value={this.state.psw}
                        placeholder={"密码"}
                    />
                    <Button
                        style={styles.button}
                        title="登录xmpp"
                        onPress={() => this.receiveLoginEvent()
                        }
                    />
                </View>

                <View style={styles.row}>
                    <TextInput
                        style={{height: 40, width: 150}}
                        onChangeText={(text) => this.setState({toName: text})}
                        value={this.state.toName}
                        placeholder={"接收消息姓名"}
                    />
                    <TextInput
                        style={{height: 40, width: 120}}
                        onChangeText={(text) => this.setState({text: text})}
                        value={this.state.text}
                        placeholder={"发送的消息"}
                    />
                    <Button
                        style={styles.button}
                        title="发送消息" onPress={() => {
                        NativeModules.MessageModule.sendMsg(this.state.toName + "@127.0.0.1", this.state.text)
                            .then(msg => {
                                ToastAndroid.show(msg, 1000);
                            }).catch(error => {
                            ToastAndroid.show(error.message, 1000);
                        });
                    }
                    }/>
                </View>
                <View style={styles.row}>

                    <TextInput
                        style={{height: 40, width: 150}}
                        onChangeText={(text) => this.setState({addFriendId: text})}
                        value={this.state.addFriendId}
                        placeholder={"添加好友的id"}
                    />
                    <Button title="添加好友" onPress={() => {
                        NativeModules.FriendModule.addUser(this.state.addFriendId + "@127.0.0.1", this.state.userName)
                            .then(msg => {
                                ToastAndroid.show(msg, 1000);
                            }).catch(err => {
                            ToastAndroid.show(err.message, 1000);
                        });
                    }}/>

                    <Button title="添加好友到指定分组" onPress={() => {
                        NativeModules.FriendModule.addUserToGroup(this.state.addFriendId + "@127.0.0.1", this.state.userName, this.state.groupName)
                            .then(msg => {
                                ToastAndroid.show(msg, 1000);
                            }).catch(err => {
                            ToastAndroid.show(err.message, 1000);
                        });
                    }}/>
                </View>
                <View style={styles.row}>
                    <TextInput
                        style={{height: 40, width: 150}}
                        onChangeText={(text) => this.setState({newRoomName: text})}
                        value={this.state.newRoomName}
                        placeholder={"要创建的聊天室名称"}
                    />
                    <Button title="创建聊天室"
                            style={styles.row}
                            onPress={() => {
                                NativeModules.ChatRoomModule.createRoom(this.state.newRoomName, this.state.newRoomName, "")
                                    .then(msg => {
                                        ToastAndroid.show(msg, 1000);
                                    }).catch(error => {
                                    ToastAndroid.show(error.message, 1000);
                                })
                            }
                            }/>
                </View>
                <View style={styles.row}>

                    <TextInput
                        style={{height: 40, width: 120}}
                        onChangeText={(text) => this.setState({roomID: text})}
                        value={this.state.roomID}
                        placeholder={"要加入的聊天室名字"}
                    />
                    <Button title="加入" onPress={() => {
                        NativeModules.ChatRoomModule.joinChatRoom(this.state.roomID + "@conference.127.0.0.1", "")
                            .then(msg => {
                                ToastAndroid.show(msg, 1000);

                            }).catch(error => {
                            ToastAndroid.show(error.message, 1000);
                        })
                        ;
                    }}/>
                    <Button title="离开" onPress={() => {
                        NativeModules.ChatRoomModule.leave(this.state.roomID + "@conference.127.0.0.1");
                    }}/>
                    <Button title="发送消息" onPress={() => {
                        NativeModules.MessageModule.sendGroupMsg(this.state.roomID + "@conference.127.0.0.1", this.state.text)
                            .then(msg => {
                                ToastAndroid.show(msg, 1000);
                            }).catch(error => {
                            ToastAndroid.show(error, 1000);
                        })
                    }}/>

                </View>

                <View style={styles.row}>
                    <TextInput
                        style={{height: 40, width: 120}}
                        onChangeText={(text) => this.setState({inviteRoomName: text})}
                        value={this.state.inviteRoomName}
                        placeholder={"邀请加入的聊天室"}
                    />
                    <TextInput
                        style={{height: 40, width: 120}}
                        onChangeText={(text) => this.setState({inviteName: text})}
                        value={this.state.inviteName}
                        placeholder={"要邀请人的名字"}
                    />
                    <Button title="邀请进入聊天室" onPress={() => {
                        NativeModules.ChatRoomModule.inviteUserToChatRoom(this.state.inviteName + "@127.0.0.1/Smack",
                            this.state.inviteRoomName + "@conference.127.0.0.1", ""
                            , "进来撩妹子");
                    }}/>

                </View>
                <View style={styles.row}>
                    <Button title="获取所有好友" onPress={() => {
                        NativeModules.FriendModule.getAllEntries();
                    }}/>
                </View>
                <View style={styles.row}>
                    <TextInput
                        style={{height: 40, width: 120}}
                        onChangeText={(text) => this.setState({groupName: text})}
                        value={this.state.groupName}
                        placeholder={"分组的名字"}
                    />
                    <Button title="创建分组" onPress={() => {
                        NativeModules.FriendModule.addGroup(this.state.groupName)
                            .then(msg => {
                                ToastAndroid.show(msg, 1000);
                            }).catch(error => {
                            ToastAndroid.show(error.message, 1000);
                        });
                    }}/>
                    <Button title="删除好友" onPress={() => {
                        NativeModules.FriendModule.deleteFriend(this.state.addFriendId + "@127.0.0.1")
                            .then(msg => {
                                ToastAndroid.show(msg, 1000);
                            }).catch(error => {
                            ToastAndroid.show(error, 1000);
                        })
                    }}/>
                </View>
            </View>
        );
    }

    receiveLoginEvent() {
        NativeModules.IMModule.login(this.state.userName, this.state.psw)
            .then(msg => {
                ToastAndroid.show(msg, 1000);
                this.setState({isOnline: "在线"})
            }).catch(error => {
            ToastAndroid.show(error.message, 1000);
        })
    }

    componentWillMount() {
        DeviceEventEmitter.addListener("event_peer_new_msg", function (event) {
            var message = event.txt;
            var fromId = event.fromId;
            ToastAndroid.show("来自个人----" + message + "----" + fromId, 1000);
            // this.setState({text: event.txt, from: event.fromId});
        });
        DeviceEventEmitter.addListener("event_group_new_msg", function (event) {
            var message = event.txt;
            var fromId = event.fromId;
            ToastAndroid.show("来自聊天室---" + message + "----" + fromId, 1000);
            // this.setState({text: event.txt, from: event.fromId});
        });
        DeviceEventEmitter.addListener("event_reject_inviter_chatroom", function (event) {
            Alert.alert(event.reason, "丫的，" + event.from + "这孙子拒绝了", [{
                text: "知道了"
            }]);
        });
        DeviceEventEmitter.addListener("event_inviter_chatroom", function (event) {
            Alert.alert(event.inviter + "邀请你加入聊天室" + event.roomId, event.reason, [{
                text: "拒绝", onPress: () => {
                    NativeModules.ChatRoomModule.rejectJoinChatRoom(event.roomId,
                        event.inviter, "你丫很闲啊");
                    // ToastAndroid.show("你拒绝了", 1000);
                }
            }, {
                text: "同意", onPress: () => {
                    NativeModules.ChatRoomModule.joinChatRoom(event.roomId, "")
                        .then(msg => {
                            ToastAndroid.show(msg, 1000);

                        }).catch(error => {
                        ToastAndroid.show(error.message, 1000);
                    });
                }
            }]);
        });
        DeviceEventEmitter.addListener("chatroom_status_event", function (event) {
            var roomId = event.roomId;
            switch (event.type) {
                case "status_joined":
                    ToastAndroid.show(event.user + "--加入了房间--" + roomId, 1000);
                    break;

                case "status_left":
                    ToastAndroid.show(event.user + "--离开了房间--" + roomId, 1000);
                    break;
                case "status_kicked":
                    ToastAndroid.show(event.user + "--了被踢出了房间--" + roomId, 1000);
                    break;
            }
        });
        DeviceEventEmitter.addListener("event_friend", function (event) {
            var from = event.from;
            switch (event.type) {
                case "type_subscribe"://收到请求
                    Alert.alert("好友请求", from + "--请求加你为好友", [{
                        text: "拒绝", onPress: () => {
                            NativeModules.FriendModule.rejectFriendRequest(from);
                        }
                    }, {
                        text: "同意", onPress: () => {
                            NativeModules.FriendModule.agreeFriendRequest(from);
                        }
                    }]);
                    break;
                case "type_subscribed"://同意请求
                    ToastAndroid.show(from + "-他同意了好友请求", 1000);
                    break;

                case "type_unsubscribe"://删除
                    ToastAndroid.show(from + "-shit！！ --他拒绝了好友请求", 1000);
                    break;
                case "type_unsubscribed"://拒绝请求
                    ToastAndroid.show(from + "-shit--他把你删了。。。", 1000);
                    break;
                case "type_available"://好友上线
                    ToastAndroid.show(from + "-他上线了", 1000);
                    break;
                case "type_unavailable"://好友下线
                    ToastAndroid.show(from + "-他下线了", 1000);
                    break;
            }
        });

    }


    componentWillUnmount() {
        DeviceEventEmitter.remove();
    }

}
const styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: '#F5FCFF',

    },
    row: {
        flexDirection: 'row',
        marginTop: 10
    },
    button: {
        fontSize: 20,
        textAlign: 'center',
        margin: 10,
    },
    instructions: {
        textAlign: 'center',
        color: '#333333',
        marginBottom: 5,
    },
    text: {
        fontSize: 20,
        color: 'red'
    }
});