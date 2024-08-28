var userType = /*[[${userType}]]*/ 'user';
var stompClient = null;
var roomId = /*[[${roomId}]]*/ '123'; // 실제 restaurantNo를 사용하여 동적으로 설정

function connect(roomId) {
    var socket = new SockJS('/ws-stomp');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe(`/sub/chat/room/${roomId}`, function (messageOutput) {
            var message = JSON.parse(messageOutput.body);
            showMessage(message);
        });
    });
}

function sendMessage(roomId) {
    var messageContent = $('#messageInput').val().trim();
    if (messageContent && stompClient) {
        var chatMessage = {
            sender: 'userType',
            message: messageContent,
            roomId: roomId,
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
    connect(roomId);
});
