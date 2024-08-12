document.addEventListener('DOMContentLoaded', function () {
    var calendarEl = document.getElementById('calendar');
    var calendar = new FullCalendar.Calendar(calendarEl, {
        initialView: 'dayGridMonth',
        initialDate: '2024-06-19',
        headerToolbar: false,
        events: [
            {
                title: '예약 5건',
                start: '2024-06-03'
            },
            {
                title: '예약 5건',
                start: '2024-06-09'
            },
            {
                title: '예약 5건',
                start: '2024-06-12'
            },
            {
                title: '예약 5건',
                start: '2024-06-25'
            }
        ]
    });
    calendar.render();

    // 날짜 선택 시 캘린더 이동
    document.getElementById('datePicker').addEventListener('change', function () {
        var selectedDate = this.value;
        console.log('selectedDate', selectedDate);
        calendar.gotoDate(selectedDate);
    });
});