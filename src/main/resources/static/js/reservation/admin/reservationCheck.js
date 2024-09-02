$(document).ready(function () {
    var $calendarEl = $('#reservationCalendar');
    var $reservationInfo = $('#reservation-row');

    if ($calendarEl.length === 0) {
        console.error("달력을 표시할 요소가 존재하지 않습니다.");
        return;
    }

    // FullCalendar 초기화
    var calendar = new FullCalendar.Calendar($calendarEl[0], {
        initialView: 'dayGridMonth',
        initialDate: new Date(),
        headerToolbar: {
            left: 'prev,next',  // 왼쪽에 이전/다음/오늘 버튼 배치
            right: 'title',           // 중앙에 현재 월 표시

        },
    });

    // 캘린더 렌더링
    calendar.render();

    // 페이지 로드 시 모든 예약 데이터를 가져와 캘린더에 표시
    $.ajax({
        url: '/restaurant/reservationCheck',  // 예약 데이터를 가져오는 API 엔드포인트
        type: 'post',
        data: {
            date: new Date().toISOString().substring(0, 10)  // 현재 날짜를 기본으로 전달
        },
        success: function (data) {
            console.log("서버로부터 받은 이벤트 데이터:", data);
            $.each(data, function (index, reservationInfo) {
                calendar.addEvent({
                    title: '예약 ' + reservationInfo.count + '건',
                    start: reservationInfo.date
                });
            });
        },
        error: function (xhr, status, error) {
            console.error('예약 정보를 가져오는 데 실패했습니다:', error);
        }
    });


    $('#reservationDate').datepicker({
        dateFormat: 'yy-mm-dd',
        onSelect: function (dateText) {
            console.log('선택된 날짜:', dateText);
            calendar.gotoDate(dateText);  // 선택된 날짜로 캘린더 이동
            updateReservationInfo(dateText);
        }
    });


    function updateReservationInfo(dateText) {
        $reservationInfo.empty();

        $.ajax({
            url: '/restaurant/reservationInfo',
            type: 'GET',
            data: {
                date: dateText
            },
            success: function(response) {
                response.forEach(function(reservation, index) {
                    var reservationHtml = `
                <div class="col-md-10 reservation-card-container">
                    <div class="card p-4 mb-4 shadow-sm">
                        <div class="d-flex justify-content-between align-items-center">
                            <!-- 왼쪽 예약 정보 -->
                            <div>
                                <div class="d-flex align-items-center mb-3">
                                    <span class="badge badge-secondary p-2 mr-3" style="font-size: 1.1rem;">#${index + 1}</span>
                                    <h5 class="mb-0 mr-3 text-primary" style="font-size: 1.25rem;">${reservation.reservationTime}</h5>
                                </div>
                                <div class="mb-3" style="font-size: 1.15rem;">
                                    <strong>${reservation.name}</strong> <span>| <strong>${reservation.partySize}명</strong></span>
                                </div>
                                <div style="font-size: 1.15rem;">
                                    <span class="text-dark"><strong>${reservation.phone}</strong></span>
                                </div>
                            </div>

                            <!-- 오른쪽 버튼 -->
                            <div>
                                <button class="btn btn-primary mr-2 visit-complete-btn" style="font-size: 1.1rem;" data-reservation-no="${reservation.reservationNo}">방문 완료</button>
                                <button class="btn btn-danger cancel-reservation-btn" style="font-size: 1.1rem;" data-reservation-no="${reservation.reservationNo}">예약 취소</button>
                            </div>
                        </div>
                    </div>
                </div>
                `;
                    $reservationInfo.append(reservationHtml);
                });
            },
            error: function(xhr, status, error) {
                let response = JSON.parse(xhr.responseText);
                alert(response.message);
            }
        });
    }

    $(document).on('click', '.visit-complete-btn', function() {
        var reservationNo = $(this).data('reservation-no');
        var $card = $(this).closest('.reservation-card-container');

        console.log('방문 완료 - 예약번호:', reservationNo);

        $.ajax({
            url: '/restaurant/reservation/enter/' + reservationNo,
            type: 'PATCH',
            success: function(response) {
                alert(response)
                $card.remove();
            },
            error: function(xhr, status, error) {
                let response = JSON.parse(xhr.responseText);
                alert(response.message);
            }
        });
    });


    $(document).on('click', '.cancel-reservation-btn', function() {
        var reservationNo = $(this).data('reservation-no');
        var $card = $(this).closest('.reservation-card-container');

        console.log('예약 취소 - 예약번호:', reservationNo);

        $.ajax({
            url: '/restaurant/reservation/cancel/' + reservationNo,
            type: 'PATCH',
            success: function(response) {
                alert(response)
                $card.remove();
            },
            error: function(xhr, status, error) {
                let response = JSON.parse(xhr.responseText);
                alert(response.message);
            }
        });
    });
});