var stompClient = null;

// WebSocket 및 STOMP 연결 설정
function connect() {
    var socket = new SockJS('/ws-stomp');  // Spring 서버에 정의된 WebSocket 엔드포인트와 일치해야 함
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);

        // 특정 채팅방 구독 (예: roomId = "123")
        stompClient.subscribe('/sub/chat/room/123', function (messageOutput) {
            var message = JSON.parse(messageOutput.body);
            showMessage(message);
        });
    });
}

// 메시지 전송 함수
function sendMessage() {
    var messageContent = $('#messageInput').val().trim();
    if (messageContent && stompClient) {
        var chatMessage = {
            sender: 'restaurant',  // 실제 사용자 정보로 교체
            message: messageContent,
            roomId: '123',  // 실제 roomId로 교체
            type: 'TALK'  // 메시지 타입 설정 (예: CHAT)
        };
        stompClient.send("/pub/chat/message", {}, JSON.stringify(chatMessage));  // 서버의 @MessageMapping에 대응

        $('#messageInput').val('');  // 입력란 초기화
    }
}

// 메시지를 화면에 표시하는 함수
function showMessage(message) {
    console.log("show")
    var messageElement = $('<div class="message"></div>').text(message.sender + ": " + message.message);
    $('#chatMessages').append(messageElement);
    $('#chatMessages').scrollTop($('#chatMessages')[0].scrollHeight);  // 스크롤을 최신 메시지 위치로 이동
}

// 문자 수를 업데이트하는 함수
function updateCharCount() {
    var charCount = $('#messageInput').val().length;
    $('#charCount').text(charCount + "/1000");
}

// 페이지 로드 시 WebSocket 연결
$(document).ready(function () {
    connect();  // WebSocket 연결 시작
});