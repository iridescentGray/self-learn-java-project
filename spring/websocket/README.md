
打开http://www.easyswoole.com/wstool.html 的三个Tab：

ws://127.0.0.1:8080/?accessToken=苹果
ws://127.0.0.1:8080/?accessToken=番茄
ws://127.0.0.1:8080/?accessToken=土豆

在输入框发送消息，即可在其他两端同步看到
{
    type: "SEND_TO_ONE_REQUEST",
    body: {
        toUser: "番茄",
        msgId: "eaef4a3c-35dd-46ee-b548-f9c4eb6396fe",
        content: "我是一条单聊消息"
    }
}