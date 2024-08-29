

// 페이지 로드 시 WebSocket 연결
$(document).ready(function () {
    var userType = $('#sender').val();
    var stompClient = null;
    var roomId = $('#roomId').val(); // 실제 restaurantNo를 사용하여 동적으로 설정

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
function sendMessage() {

    let roomId = $('#roomId').val()
    let sender = $('#sender').val()
    let restaurantNo = $('#restaurantNo').val()

    var messageContent = $('#messageInput').val().trim();
    if (messageContent && stompClient) {
        var chatMessage = {
            sender: sender,
            message: messageContent,
            roomId: roomId,
            type: 'TALK'
        };
        stompClient.send("/pub/chat.sendMessage", {}, JSON.stringify(chatMessage));  // 서버의 @MessageMapping에 대응

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

    const messageContainer = $('#chatMessages')
    messageContainer.empty();

    $.ajax({
        url: '/chat/messages',
        type: 'get',
        data: {roomId: roomId},
        success: function (data){
            data.forEach(msg => {
                let innerhtml = `
                    <div class="message">
                        ${msg.email}: ${msg.message}
                    </div>
                    `;

                messageContainer.append(innerhtml);
            })

        }
    });
    $('#roomId').val(roomId);
    $('#sender').val(restaurantNo);

}