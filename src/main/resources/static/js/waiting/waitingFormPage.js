$(document).ready(function() {
    let partySize = 0;

    // let memberNo = $[[{memberNo}]];
    // let restaurantNo = $[[{restaurantNo}]];

    const maxPeople = 8; // 최대 인원수 설정

    const $partySizeSpan = $('#partySize');
    const $decreaseButton = $('#decrease');
    const $increaseButton = $('#increase');
    const $registerButton = $('#register');

    $decreaseButton.on('click', function() {
        if (partySize > 0) {
            partySize--;
            updatePartySize();
        }
    });

    $increaseButton.on('click', function() {
        if (partySize < maxPeople) {
            partySize++;
            updatePartySize();
        }
    });

    $registerButton.on('click', function() {
        if (partySize === 0) {
            alert('0명은 등록할 수 없습니다!');
            return;
        }

        // 서버로 데이터 전송
        $.ajax({
            url: '/users/waiting',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                restaurantNo: restaurantNo,
                memberNo: memberNo,
                partySize: partySize }),
            success: function(data) {

                alert(data.message);

                let waitingNo = data.waitingNo;
                console.log(waitingNo);

                // 중첩 ajax로 웨이팅 등록 시 메세지 전송 요청
                $.ajax({
                    url: '/users/waiting/sendOne/' + waitingNo,
                    type: 'POST',
                    success: function(response) {
                        window.location.href = '../../'; // 인덱스 페이지로 이동
                    },
                    error: function(error) {
                        alert("메세지 전송에 실패했습니다.");
                    }
                });
            },
            error: function(xhr, status, error) {
                let response = JSON.parse(xhr.responseText);
                alert(response.message);
            }
        });
    });

    function updatePartySize() {
        $partySizeSpan.text(partySize);
        $registerButton.prop('disabled', partySize === 0);
    }
});