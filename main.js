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
            inviteName: ""

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
                        NativeModules.IMModule.sendMsg(this.state.toName + "@127.0.0.1", this.state.text)
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
                        NativeModules.IMModule.addFriend(this.state.addFriendId)
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
                                NativeModules.IMModule.createRoom(this.state.newRoomName, this.state.newRoomName, "")
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
                        NativeModules.IMModule.joinChatRoom(this.state.roomID + "@conference.127.0.0.1", "")
                            .then(msg => {
                                ToastAndroid.show(msg, 1000);

                            }).catch(error => {
                            ToastAndroid.show(error.message, 1000);
                        })
                        ;
                    }}/>
                    <Button title="离开" onPress={() => {
                        NativeModules.IMModule.leave(this.state.roomID + "@conference.127.0.0.1");
                    }}/>
                    <Button title="发送消息" onPress={() => {
                        NativeModules.IMModule.sendGroupMsg(this.state.roomID + "@conference.127.0.0.1", this.state.text)
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
                        NativeModules.IMModule.inviteUserToChatRoom(this.state.inviteName + "@127.0.0.1/Smack",
                            this.state.inviteRoomName + "@conference.127.0.0.1", ""
                            , "进来撩妹子");
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
        DeviceEventEmitter.addListener("event_inviter_chatroom", function (event) {
            Alert.alert(event.inviter + "邀请你加入聊天室"+event.roomId, event.reason, [{
                text: "拒绝", onPress: () => {
                    NativeModules.IMModule.rejectJoinChatRoom(event.roomId,
                        event.inviter, "你丫很闲啊");
                    // ToastAndroid.show("你拒绝了", 1000);
                }
            }, {
                text: "同意", onPress: () => {
                    NativeModules.IMModule.joinChatRoom(event.roomId, "")
                        .then(msg => {
                            ToastAndroid.show(msg, 1000);

                        }).catch(error => {
                        ToastAndroid.show(error.message, 1000);
                    });
                }
            }]);
        })
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