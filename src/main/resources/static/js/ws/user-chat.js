

// 페이지 로드 시 WebSocket 연결
$(document).ready(function () {
    var userType = $('#sender').data('sender');
    var stompClient = null;
    var roomId = $('#roomId').data('roomid'); // 실제 restaurantNo를 사용하여 동적으로 설정

    connect(roomId);  // WebSocket 연결 시작


});
function updateCharCount() {
    var charCount = $('#messageInput').val().length;
    $('#charCount').text(charCount + "/1000");
}
function showMessage(message) {
    console.log("show")
    var messageElement = $('<div class="message"></div>').text(message.sender + ": " + message.message);
    $('#chatMessages').append(messageElement);
    $('#chatMessages').scrollTop($('#chatMessages')[0].scrollHeight);  // 스크롤을 최신 메시지 위치로 이동
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
        stompClient.send("/pub/chat/message", {}, JSON.stringify(chatMessage));  // 서버의 @MessageMapping에 대응

        $('#messageInput').val('');  // 입력란 초기화
    }
}
// WebSocket 및 STOMP 연결 설정
function connect(roomId) {
    var socket = new SockJS('/ws-stomp');  // Spring 서버에 정의된 WebSocket 엔드포인트와 일치해야 함
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/sub/chat/room/'+roomId, function (messageOutput) {
            var message = JSON.parse(messageOutput.body);
            showMessage(message);
        });
    });
}