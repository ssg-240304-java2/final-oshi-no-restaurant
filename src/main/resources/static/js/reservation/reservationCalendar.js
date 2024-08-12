// 식당 선택 이벤트 처리 식당의 정보를 가져오는
$('#restaurant-select').on('change', function() {
    var restaurantNo = 1;  // $(this).val();  // 선택된 restaurant_no 가져오기
    console.log("선택된 식당 번호:", restaurantNo);

    if (restaurantNo) {
        $.ajax({
            url: '/reservation/' + restaurantNo,  // 해당 restaurant_no의 데이터 요청
            method: 'GET',   // HTTP GET 메서드
            success: function(response) {
                console.log('서버 응답:', response);

                // 인원 선택 버튼 초기화 및 삭제
                $('.people .btn').remove();
                // 시간 선택 버튼 초기화 및 삭제
                $('.time-selection .btn').remove();

                // 받은 데이터로 버튼 생성 및 추가
                if (response.reservationPeople && response.reservationPeople.length > 0) {
                    response.reservationPeople.forEach(function(people) {
                        $('.people').append('<button type="button" class="btn btn-custom">' + people + '</button>');
                    });
                }

                if (response.reservationTime && response.reservationTime.length > 0) {
                    response.reservationTime.forEach(function(time) {
                        $('.time-selection').append('<button type="button" class="btn btn-custom">' + time + '</button>');
                    });
                }
            },
            error: function(jqXHR, textStatus, errorThrown) {
                alert('해당 식당의 예약 정보를 불러오는데 실패했습니다. 다시 시도해주세요.');
                console.error('AJAX 오류:', textStatus, errorThrown);
            }
        });
    } else {
        console.log("식당이 선택되지 않았습니다.");
    }
});







$(document).ready(function() {
    console.log("Document is ready!");
    var today = new Date();
    var reservationDate, partySize, reservationTime;

    // Pikaday 달력 초기화
    var picker = new Pikaday({
        field: document.createElement('div'),  // 동적으로 div 요소 생성
        container: document.getElementById('datepicker-container'),
        bound: false,  // 달력을 고정된 위치에 표시
        format: 'YYYY-MM-DD',
        i18n: {
            previousMonth: '이전달',
            nextMonth: '다음달',
            months: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
            weekdays: ['일', '월', '화', '수', '목', '금', '토'],
            weekdaysShort: ['일', '월', '화', '수', '목', '금', '토']
        },
        onSelect: function() {
            console.log("onSelect called");
            var selectedDate = this.getDate();  // 선택된 날짜를 JavaScript Date 객체로 반환

            // UTC 시간대 기준으로 설정
            reservationDate = new Date(Date.UTC(
                selectedDate.getFullYear(),
                selectedDate.getMonth(),
                selectedDate.getDate()
            )).toISOString().split('T')[0];  // 'YYYY-MM-DD' 형식으로 변환

            console.log("선택된 날짜:", reservationDate);
        },
        disableDayFn: function(date) {
            return date < today.setHours(0, 0, 0, 0);  // 오늘 이전의 날짜는 비활성화
        }
    });







       // 이벤트 핸들러: 동적으로 생성된 요소에 대한 이벤트 처리
    $(document).on('click', '.times .btn', function(){
        $('.times .btn').removeClass('selected');
        $(this).addClass('selected');
        reservationTime = $(this).text();  // 버튼의 텍스트 값 사용
        console.log("선택된 시간:", reservationTime);
    });


    // 인원 선택 버튼 처리
    $(document).on('click', '.people .btn', function(){
        $('.people .btn').removeClass('selected');
        $(this).addClass('selected');
        partySize = $(this).text();  // 버튼의 텍스트 값 사용
        console.log("선택된 인원수:", partySize);
    });



    $('#reserve-button').on('click', function() {
        console.log("예약 버튼 클릭됨");

        // 각 변수의 값을 확인
        console.log("선택된 날짜:", reservationDate);
        console.log("선택된 인원수:", partySize);
        console.log("선택된 시간:", reservationTime);

        // 선택된 날짜, 인원수, 시간이 모두 있는지 확인
        if (reservationDate && partySize && reservationTime) {
            console.log("모든 값이 설정됨, AJAX 요청 준비 중...");


            // 수정 필요
            var requestData = {
                memberNo: 1,  // 수정 필요
                restaurantNo:1   ,    // $('#restaurant-select').val(),  // 선택된 식당 번호
                partySize: partySize,  // 선택된 인원수
                reservationDate: reservationDate,  // 선택된 날짜
                reservationTime: reservationTime// 선택된 시간
            };

            console.log(requestData); /////////// 이거 확인!

            // 서버로 AJAX 요청을 보냅니다.
            $.ajax({
                url: '/reservation',  // 서버의 엔드포인트 URL
                method: 'POST',   // HTTP 메서드
                contentType: 'application/json',  // 데이터 타입 설정
                data: JSON.stringify(requestData), // 데이터를 JSON으로 변환하여 전송
                success: function(response) {
                    alert('쩝쩝 예약 성공!');
                    console.log('서버 응답:', response);
                },
                error: function(jqXHR, textStatus, errorThrown) {
                    alert('예약에 실패했습니다. 다시 시도해주세요.');
                    console.error('AJAX 오류:', textStatus, errorThrown);
                }
            });
        } else {
            console.log("예약 데이터를 모두 선택하지 않았습니다.");
            alert('날짜, 인원수, 시간 모두를 선택해주세요.');
        }
    });

});
