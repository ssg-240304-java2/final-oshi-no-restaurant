$(document).ready(function() {
    console.log("Document is ready!");
    var today = new Date();
    var partySize = 1;
    var reservationDate = today.toISOString().split('T')[0];
    var reservationTime;
    var memberNo = $('#reservation-info').data('memberno');

    // URL에서 restaurantNo 추출
    var url = window.location.pathname;
    var restaurantNo = url.substring(url.lastIndexOf('/') + 1);

    console.log("MemberNo:", memberNo);
    console.log("RestaurantNo:", restaurantNo);

    // 서버에서 예약 가능한 날짜 데이터를 가져옴
    $.ajax({
        url: `/reservation/${restaurantNo}/available-dates`,
        method: 'POST',
        success: function(data) {
            var availableDates = data.map(date => {
                var d = new Date(date);
                d.setHours(0, 0, 0, 0); // 시간을 00:00:00으로 설정
                return d;
            });
            console.log("예약 가능한 날짜들:", availableDates);

            // Pikaday 초기화 (AJAX 성공 후)
            initializeDatePicker(availableDates);
        },
        error: function(jqXHR, textStatus, errorThrown) {
            console.error('예약 가능한 날짜를 가져오는 데 실패했습니다.', textStatus, errorThrown);
        }
    });

    function initializeDatePicker(availableDates) {
        var picker = new Pikaday({
            field: document.createElement('div'),
            container: document.getElementById('datepicker-container'),
            bound: false,
            format: 'YYYY-MM-DD',
            defaultDate: today,
            setDefaultDate: true,
            i18n: {
                previousMonth: '이전달',
                nextMonth: '다음달',
                months: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
                weekdays: ['일', '월', '화', '수', '목', '금', '토'],
                weekdaysShort: ['일', '월', '화', '수', '목', '금', '토']
            },
            onSelect: function() {
                console.log("onSelect called");
                var selectedDate = this.getDate();

                // 선택된 날짜를 UTC 기준으로 설정
                reservationDate = new Date(Date.UTC(
                    selectedDate.getFullYear(),
                    selectedDate.getMonth(),
                    selectedDate.getDate()
                )).toISOString().split('T')[0];

                console.log("선택된 날짜:", reservationDate);

                // 날짜 선택 후 시간대 업데이트
                updateAvailableTimeSlots();
            },
            disableDayFn: function(date) {
                var todayMidnight = new Date();
                todayMidnight.setHours(0, 0, 0, 0);

                var inputDate = new Date(Date.UTC(date.getFullYear(), date.getMonth(), date.getDate()));
                inputDate.setHours(0, 0, 0, 0);

                if (inputDate < todayMidnight) {
                    return true;
                }

                var isAvailable = availableDates.some(d => {
                    return d.getTime() === inputDate.getTime();
                });

                return !isAvailable;
            }
        });

        // 기본으로 오늘 날짜의 시간대 업데이트
        updateAvailableTimeSlots();
    }

    function updateAvailableTimeSlots() {
        if (reservationDate && partySize) {
            $.ajax({
                url: `/reservation/${restaurantNo}/available-times`,
                method: 'GET',
                data: {
                    date: reservationDate,
                    partySize: partySize
                },
                success: function(timeSlots) {
                    var timesContainer = $('.times');
                    timesContainer.empty();

                    timeSlots.forEach(function(slot) {
                        var button = $('<button class="btn">').text(slot.time);

                        if (!slot.isAvailable) {
                            button.addClass('disabled').css('color', 'gray');
                        } else {
                            button.on('click', function() {
                                $('.times .btn').removeClass('selected');
                                $(this).addClass('selected');
                                reservationTime = $(this).text();
                                console.log("선택된 시간:", reservationTime);
                            });
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

    // 인원 선택 버튼 처리
    $(document).on('click', '.people .btn', function(){
        $('.people .btn').removeClass('selected');
        $(this).addClass('selected');
        partySize = parseInt($(this).text());
        console.log("선택된 인원수:", partySize);

        // 인원수 선택 후 시간대 업데이트
        updateAvailableTimeSlots();
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

            var requestData = {
                memberNo: memberNo,
                restaurantNo: restaurantNo,
                partySize: partySize,
                reservationDate: reservationDate,
                reservationTime: reservationTime
            };

            console.log(requestData);

            // 서버로 예약 요청을 보냅니다.
            $.ajax({
                url: '/reservation',
                method: 'POST',
                contentType: 'application/json',
                data: JSON.stringify(requestData),
                success: function(response) {
                    console.log('서버 응답:', response);

                    // 중복 예약 시 서버에서 409 응답을 받으면 alert 창을 띄움
                    if (response.httpCode === 409) {
                        console.log('409 응답 감지됨');
                        if (response.message === '이미 동일한 시간에 예약이 존재합니다.') {
                            alert('이미 동일한 시간에 예약이 존재합니다.');
                        } else if (response.message === '동시에 다른 사용자가 동일한 예약하려고 시도했습니다. 다시 시도해주세요.') {
                            alert('다른 사용자가 동일한 예약하려고 시도 중입니다. 잠시 후 다시 시도해주세요.');
                        } else {
                            alert('예약 중 오류가 발생했습니다. 다시 시도해주세요.');
                        }

                    } else if (response.httpCode === 200) {
                        console.log('예약 성공, 모달 창 띄우기');
                        fetchModalContent(requestData.restaurantNo);
                        $('#reservationModal').modal('show');
                        subtractPartySize(requestData.restaurantNo, partySize, reservationTime);
                    } else {
                        console.error('예상치 못한 서버 응답 코드:', response.httpCode);
                    }
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


    // 모달 콘텐츠를 불러오기 위한 함수
    function fetchModalContent(restaurantNo) {
        $.ajax({
            url: `/reservation/${restaurantNo}/modal/data`,
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ "restaurantNo": restaurantNo }),
            success: function(data) {
                $('#modalRestaurantName').text(data.restaurantName);
                $('#modalPartySize').text(data.partySize + "명");
                $('#modalReservationDate').text(data.reservationDate);
                $('#modalReservationTime').text(data.reservationTime);

                $('#reservationModal').modal('show');
                console.log('모달 불러오기 성공');

                setTimeout(function() {
                    window.location.href = '/';
                }, 5000);
            },
            error: function(jqXHR, textStatus, errorThrown) {
                console.error('모달을 불러오는 데 실패했습니다.', textStatus, errorThrown);
            }
        });
    }

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
