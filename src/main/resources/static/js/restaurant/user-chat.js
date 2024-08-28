var stompClient = null;

function connect() {
    var socket = new SockJS('/ws-stomp');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/sub/chat/room/123', function (messageOutput) {
            var message = JSON.parse(messageOutput.body);
            showMessage(message);
        });
    });
}

function sendMessage() {
    var messageContent = $('#messageInput').val().trim();
    if (messageContent && stompClient) {
        var chatMessage = {
            sender: 'user',  // 유저에서 전송하는 메시지이므로 'user'로 설정
            message: messageContent,
            roomId: '123',
            type: 'TALK'
        };
        stompClient.send("/pub/chat/message", {}, JSON.stringify(chatMessage));
        $('#messageInput').val('');  // 입력란 초기화
    }
}

function showMessage(message) {
    var messageElement = $('<div class="message"></div>').text(message.sender + ": " + message.message);
    $('#chatMessages').append(messageElement);
    $('#chatMessages').scrollTop($('#chatMessages')[0].scrollHeight);  // 스크롤을 최신 메시지 위치로 이동
}

function updateCharCount() {
    var charCount = $('#messageInput').val().length;
    $('#charCount').text(charCount + "/1000");
}

$(document).ready(function () {
    connect();
});
