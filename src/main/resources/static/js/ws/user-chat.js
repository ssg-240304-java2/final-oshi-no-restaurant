

// 페이지 로드 시 WebSocket 연결
$(document).ready(function () {
    var userType = $('#sender').val();
    var stompClient = null;
    var roomId = $('#roomId').val();

    connect(roomId);  // WebSocket 연결 시작

});

function updateCharCount() {
    var charCount = $('#messageInput').val().length;
    $('#charCount').text(charCount + "/1000");
}
function showMessage(message) {
    console.log("show")
    var messageElement = $('<div class="message"></div>').text(message.email + ": " + message.message);
    $('#chatMessages').append(messageElement);
    $('#chatMessages').scrollTop($('#chatMessages')[0].scrollHeight);
}
function sendMessage(roomId) {
    let sender = $('#sender').val();
    let memberNo = $('#memberNo').val();

    var messageContent = $('#messageInput').val().trim();
    if (messageContent && stompClient) {
        var chatMessage = {
            sender: sender,
            message: messageContent,
            roomId: roomId,
            type: 'TALK',
            memberNo: memberNo
        };
        stompClient.send("/pub/chat.sendMessage", {}, JSON.stringify(chatMessage));

        $('#messageInput').val('');
    }
}
// WebSocket 및 STOMP 연결 설정
function connect(roomId) {
    var socket = new SockJS('/ws-stomp');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/sub/chat/room/'+roomId, function (messageOutput) {
            var message = JSON.parse(messageOutput.body);
            showMessage(message);
        });
    });
}

