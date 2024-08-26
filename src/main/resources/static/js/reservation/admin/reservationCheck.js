// 캘린더 보기
document.addEventListener('DOMContentLoaded', function () {
    var calendarEl = document.getElementById('reservationCalendar');

    if (!calendarEl) {
        console.error("달력을 표시할 요소가 존재하지 않습니다.");
        return;
    }

    var calendar = new FullCalendar.Calendar(calendarEl, {
        initialView: 'dayGridMonth',
        initialDate: new Date(),
        headerToolbar: false,
        events: [
            {
                title: '이벤트 1',
                start: '2024-08-01'
            },
            {
                title: '이벤트 2',
                start: '2024-08-01'
            }
        ]
    });
    calendar.render();

    // 달력 날짜 클릭 시 해당 달로 이동
    calendar.on('dateClick', reloadCalender);

    // 일자바꿔도 캘린더 다시 로딩
    $('#reservationDate').on('change', reloadCalender);

    function reloadCalender(info) {
        var clickedDate = info.date;
        var year = clickedDate.getFullYear();
        var month = clickedDate.getMonth();
        var firstDayOfMonth = new Date(year, month, 1);
        var lastDayOfMonth = new Date(year, month + 1, 0);

        $.ajax({
            url: '/restaurant/reservationCheck',
            type: 'post',
            data: {
                date: $('#reservationDate').val()
            },
            success: function (data) {
                calendar.removeAllEvents();
                console.log(data)
                data.forEach(function (reservationInfo) {
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
    };

    // 날짜 선택 시 캘린더 이동
    $('#reservationDate').datepicker({
        dateFormat: 'yy-mm-dd',
        onSelect: function (dateText) {
            console.log('선택된 날짜:', dateText);
            calendar.gotoDate(dateText);
        }
    });
});
