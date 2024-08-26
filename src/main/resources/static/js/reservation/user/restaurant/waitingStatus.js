// 화면에 웨이팅 정보 표시
function fetchWaiting() {
    $.ajax({
        url: '/admin/waiting/' + restaurantNo,
        method: 'GET',
        success: function(response) {

            let container = $('#waiting-card-container');
            container.empty();

            response.forEach(function(waiting) {

                let createdAt = new Date(waiting.createdAt);
                let now = new Date(); // --> 9시간 +

                console.log(now);

                let minutesElapsed = Math.floor((now - createdAt) / (1000 * 60));

                const waitingCard = `
                    <div class="waiting-card" data-waiting-no="${waiting.waitingNo}">
                        <div class="row align-items-center">
                            <div class="col-sm-2-no col-data waiting-no">
                                <p>#${waiting.sequenceNumber}</p>
                            </div>
                            <div class="col-sm-2-name col-data">
                                <p>${waiting.name}</p>
                            </div>
                            <div class="col-sm-2-people col-data">
                                <p>${waiting.partySize}명</p>
                            </div>
                            <div class="col-sm-3-phone col-data">
                                <p>${waiting.phone}</p>
                            </div>
                            <div class="col-sm-3-time col-data">
                                <p>${minutesElapsed}분째 웨이팅 중</p>
                            </div>
                        </div>
                        <div class="row actions">
                            <div class="col text-right">
                                <button class="btn btn-primary call-button">호출하기</button>
                                <button class="btn btn-secondary complete-button">입장 완료</button>
                                <button class="btn btn-outline-secondary cancel-button">웨이팅 취소</button>
                            </div>
                        </div>
                    </div>
                    `
                container.append(waitingCard);
            });
        },
        error: function(error) {
            console.error('웨이팅 리스트를 기져오는 중 오류가 발생했습니다!', error);
        }
    });
}

$(document).ready(function() {

    fetchWaiting();

    // 카카오톡 알림 전송 로직 필요
    $(document).on('click', '.call-button', function() {
        let card = $(this).closest('.waiting-card')
        let waitingNo = card.data('waiting-no');

        $.ajax({
            url: '/admin/waiting/sendOne/' + waitingNo,
            method: 'POST',
            success: function(response) {
                alert("호출 완료!");
            },
            error: function(error) {
                alert("메세지 전송에 실패했습니다.");
            }
        });
    });




    // 입장 처리 비동기 요청
    $(document).on('click', '.complete-button', function() {
        let card = $(this).closest('.waiting-card')
        let waitingNo = card.data('waiting-no');

        $.ajax({
            url: '/admin/waiting/visit/' + waitingNo,
            method: 'PATCH',
            success: function(response) {
                alert(response.message)
                card.remove();
            },
            error: function(xhr, status, error) {
                let response = JSON.parse(xhr.responseText);
                alert(response.message);
                card.remove();
            }
        });
    });



    // 취소 처리 비동기 요청
    $(document).on('click', '.cancel-button', function() {
        let card = $(this).closest('.waiting-card')
        let waitingNo = card.data('waiting-no');

        $.ajax({
            url: '/admin/waiting/cancel/' + waitingNo,
            method: 'PATCH',
            success: function(response) {
                alert(response.message)
                card.remove();
            },
            error: function(xhr, status, error) {
                let response = JSON.parse(xhr.responseText);
                alert(response.message);
                card.remove();
            }
        });
    });

    // SSE 연결을 설정하여 서버에서 전송하는 실시간 이벤트를 수신
    const eventSource = new EventSource(`/sse/waiting/${restaurantNo}`);

    eventSource.onopen = function() {
        console.log('sse connection open');
    }
    eventSource.onerror = function(event) {
        console.error('SSE connection error:', event);
    };

    eventSource.addEventListener('newWaiting', function(event) {
        console.log('New Waiting event received');
        fetchWaiting();
    });
});