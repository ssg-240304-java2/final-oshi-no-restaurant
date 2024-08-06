$(document).ready(function() {
    var today = new Date();

    var picker = new Pikaday({
        field: document.createElement('div'),
        container: document.getElementById('datepicker-container'),
        bound: false,
        format: 'YYYY-MM-DD',
        i18n: {
            previousMonth: '이전달',
            nextMonth: '다음달',
            months: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
            weekdays: ['일', '월', '화', '수', '목', '금', '토'],
            weekdaysShort: ['일', '월', '화', '수', '목', '금', '토']
        },
        onSelect: function() {
            var selectedDate = this.getMoment().format('YYYY-MM-DD');
            console.log(selectedDate);

            // 선택된 날짜의 버튼에 is-selected 클래스를 추가합니다.
            $('.pika-single .pika-button').removeClass('is-selected');
            $('td[data-day="' + this.getDate().getDate() + '"] button').addClass('is-selected');
        },
        theme: 'calendar', // Add a custom class for additional styling
        disableDayFn: function(date) {
            return date < today.setHours(0, 0, 0, 0);
        }
    });

    $('.people .btn').on('click', function(){
        $('.people .btn').removeClass('selected');
        $(this).addClass('selected');
    });

    $('.time-selection .btn').on('click', function(){
        $('.time-selection .btn').removeClass('selected');
        $(this).addClass('selected');
    });

    $('#reserve-button').on('click', function(){
        $(this).toggleClass('selected');
    });
});