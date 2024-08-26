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
        headerToolbar: false

    });
    calendar.render();

    // 달력 날짜 클릭 시 해당 달로 이동
    calendar.on('dateClick', function (info) {
        var clickedDate = info.date;
        var year = clickedDate.getFullYear();
        var month = clickedDate.getMonth();
        var firstDayOfMonth = new Date(year, month, 1);
        var lastDayOfMonth = new Date(year, month + 1, 0);

        $.ajax({
            url: '/restaurant/reservationCheck',
            data: {
                start: firstDayOfMonth.toISOString().split('T')[0],
                end: lastDayOfMonth.toISOString().split('T')[0]
            },
            success: function (data) {
                calendar.removeAllEvents();
                data.forEach(function (reservation) {
                    calendar.addEvent({
                        title: '예약 ' + reservation.count + '건',
                        start: reservation.date
                    });
                });
            },
            error: function (xhr, status, error) {
                console.error('예약 정보를 가져오는 데 실패했습니다:', error);
            }
        });
    });

    // 날짜 선택 시 캘린더 이동
    $('#reservationDate').datepicker({
        dateFormat: 'yy-mm-dd',
        onSelect: function (dateText) {
            console.log('선택된 날짜:', dateText);
            calendar.gotoDate(dateText);
        }
    });
});
