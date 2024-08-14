$(document).ready(function() {
    console.log("Document is ready!");
    var today = new Date();
    var partySize = 1; // 기본 인원수를 1명으로 설정
    var reservationDate = today.toISOString().split('T')[0]; // 오늘 날짜를 기본값으로 설정
    var reservationTime;

    // Pikaday 달력 초기화
    var picker = new Pikaday({
        field: document.createElement('div'),  // 동적으로 div 요소 생성
        container: document.getElementById('datepicker-container'),
        bound: false,  // 달력을 고정된 위치에 표시
        format: 'YYYY-MM-DD',
        defaultDate: today,  // 기본 날짜를 오늘로 설정
        setDefaultDate: true,  // 기본 날짜를 선택된 상태로 유지
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

            // 날짜 선택 후 시간대 업데이트
            updateAvailableTimeSlots();
        },
        disableDayFn: function(date) {
            var todayMidnight = new Date(); // 현재 날짜 가져오기
            todayMidnight.setHours(0, 0, 0, 0); // 시간을 자정으로 설정
            return date < todayMidnight; // 오늘 이전의 날짜는 비활성화
        }
    });

    // 기본으로 오늘 날짜의 시간대 업데이트
    updateAvailableTimeSlots();

    // 기본으로 1명이 선택된 상태로 설정
    $('.people .btn').eq(0).addClass('selected');

    // 인원 선택 버튼 처리
    $(document).on('click', '.people .btn', function(){
        $('.people .btn').removeClass('selected');
        $(this).addClass('selected');
        partySize = parseInt($(this).text());  // 버튼의 텍스트 값을 정수로 변환하여 사용
        console.log("선택된 인원수:", partySize);

        // 인원수 선택 후 시간대 업데이트
        updateAvailableTimeSlots();
    });

    // 시간대 업데이트 함수
    function updateAvailableTimeSlots() {
        if (reservationDate && partySize) {
            var restaurantNo = 1; // 레스토랑 번호를 여기에 지정합니다.

            $.ajax({
                url: `/reservation/${restaurantNo}/available-times`,
                method: 'GET',
                data: {
                    date: reservationDate,
                    partySize: partySize
                },
                success: function(timeSlots) {
                    var timesContainer = $('.times');  // 시간대 버튼들을 담을 컨테이너
                    timesContainer.empty();  // 기존의 시간대 버튼들을 제거합니다.

                    timeSlots.forEach(function(slot) {
                        var button = $('<button class="btn">').text(slot.time);

                        if (!slot.isAvailable) {
                            button.addClass('disabled').css('color', 'gray');  // 예약 불가능한 시간대는 회색으로 표시
                        }

                        timesContainer.append(button);
                    });

                    console.log("시간대 업데이트 완료:", timeSlots);
                },
                error: function(jqXHR, textStatus, errorThrown) {
                    console.error('시간대 정보를 가져오는 데 실패했습니다.', textStatus, errorThrown);
                }
            });
        }
    }

    // 이벤트 핸들러: 동적으로 생성된 요소에 대한 이벤트 처리
    $(document).on('click', '.times .btn', function(){
        if (!$(this).hasClass('disabled')) {  // 버튼이 비활성화되지 않은 경우에만 실행
            $('.times .btn').removeClass('selected');
            $(this).addClass('selected');
            reservationTime = $(this).text();  // 버튼의 텍스트 값 사용
            console.log("선택된 시간:", reservationTime);
        }
    });

    // 예약 버튼 클릭 이벤트
    $('#reserve-button').on('click', function() {
        console.log("예약 버튼 클릭됨");

        // 각 변수의 값을 확인
        console.log("선택된 날짜:", reservationDate);
        console.log("선택된 인원수:", partySize);
        console.log("선택된 시간:", reservationTime);

        // 선택된 날짜, 인원수, 시간이 모두 있는지 확인
        if (reservationDate && partySize > 0 && reservationTime) {
            console.log("모든 값이 설정됨, AJAX 요청 준비 중...");

            // 예약 데이터 생성
            var requestData = {
                memberNo: 1,  // 수정 필요
                restaurantNo: 1,  // $('#restaurant-select').val(),  // 선택된 식당 번호
                partySize: partySize,  // 선택된 인원수
                reservationDate: reservationDate,  // 선택된 날짜
                reservationTime: reservationTime  // 선택된 시간
            };

            console.log(requestData); // 확인용 로그

            // 서버로 예약 요청을 보냅니다.
            $.ajax({
                url: '/reservation',  // 서버의 엔드포인트 URL
                method: 'POST',   // HTTP 메서드
                contentType: 'application/json',  // 데이터 타입 설정
                data: JSON.stringify(requestData), // 데이터를 JSON으로 변환하여 전송
                success: function(response) {
                    alert('예약 성공!');
                    console.log('서버 응답:', response);

                    // 예약 성공 후 인원수 차감 처리
                    subtractPartySize(requestData.restaurantNo, partySize, reservationTime);
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





    // 인원수 차감 처리 함수
    function subtractPartySize(reservationNo, partySize, time) {
        $.ajax({
            url: `/reservation/${reservationNo}/subtract?partySize=${partySize}&time=${encodeURIComponent(time)}`,
            method: 'PUT',
            success: function(response) {
                console.log('인원수가 성공적으로 차감되었습니다.');
            },
            error: function(jqXHR, textStatus, errorThrown) {
                console.error('인원수 차감에 실패했습니다.', textStatus, errorThrown);
            }
        });
    }

});
