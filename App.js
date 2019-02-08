import React, {Component} from 'react';
import {StyleSheet, View, Text,Button, TextInput, Keyboard,PermissionsAndroid,ToastAndroid,TouchableOpacity } from 'react-native';
import {VoiceUtils} from './Utils/VoiceUtils'

export default class App extends Component {
    constructor(props) {
        super(props)
        this.state = {
            text: "interaction"
        }

    }

    onBtnPressOne = async () => {
        // alert("111")
        VoiceUtils.init(0)
        VoiceUtils.speak(this.state.text);
    }
    onBtnPress = async () => {
        // alert("111")
        VoiceUtils.init(1)
        VoiceUtils.speak(this.state.text);
    }

    render() {
        return (
            <View style={styles.container}>
                <TouchableOpacity style={styles.button_view}
                                  onPress={this.requestReadPermission.bind(this)}>
                    <Text style={styles.button_text}>申请读写权限</Text>
                </TouchableOpacity>
                <View style={{flexDirection: "row",justifyContent: "space-between",flexWrap: "wrap",width: 250}}>
                    <Button title={`女声Read`} onPress={this.onBtnPressOne.bind(this)}/>
                    <Button title={`男声Read`} onPress={this.onBtnPress.bind(this)}/>

                </View>
                <TextInput
                    style={styles.textInput}
                    multiline={true}
                    onChangeText={text => this.setState({text})}
                    value={this.state.text}
                    onSubmitEditing={Keyboard.dismiss}
                />
            </View>
        );
    }
    show(data) {
        ToastAndroid.show(data,ToastAndroid.SHORT)
    }
    async requestReadPermission() {
        try {
            //返回string类型
            const granted = await PermissionsAndroid.request(
                PermissionsAndroid.PERMISSIONS.WRITE_EXTERNAL_STORAGE,
                {
                    //第一次请求拒绝后提示用户你为什么要这个权限
                    'title': '我要读写权限',
                    'message': '没权限我不能工作，同意就好了'
                }
            )
            if (granted === PermissionsAndroid.RESULTS.GRANTED) {
                this.show("你已获取了读写权限")
            } else {
                this.show("获取读写权限失败")
            }
        } catch (err) {
            this.show(err.toString())
        }
    }
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: '#F5FCFF',
    },
    button_view: {
        padding:5,
        margin:5,
        borderRadius: 4,
        backgroundColor: '#8d4dfc',
        alignItems: 'center',
    },
    welcome: {
        fontSize: 20,
        textAlign: 'center',
        margin: 10,
    },
    instructions: {
        textAlign: 'center',
        color: '#333333',
        marginBottom: 5,
    },
    textInput: {
        borderColor: "gray",
        borderWidth: 1,
        height: 200,
        width: "100%"
    }
});
