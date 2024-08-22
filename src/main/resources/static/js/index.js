$(document).ready(function(){

    // 모든 div 요소를 가져오기
    const allDivs = document.querySelectorAll('div');

    // popular로 시작하는 클래스명을 찾기
    const popularClasses = Array.from(allDivs).filter(div => {
        return Array.from(div.classList).some(className => className.startsWith('popular'));
    });
    const busiestClasses = Array.from(allDivs).filter(div => {
        return Array.from(div.classList).some(className => className.startsWith('popular'));
    });
    const directClasses = Array.from(allDivs).filter(div => {
        return Array.from(div.classList).some(className => className.startsWith('popular'));
    });

    // popular로 시작하는 클래스명의 개수를 계산
    var popularCount = popularClasses.length;
    var popularIndex = 0;
    var busiestCount = busiestClasses.length;
    var busiestIndex = 0;
    var directCount = directClasses.length;
    var directIndex = 0;

    var baseLeft = 10; // 첫 번째 요소의 left 값 (10%)
    var increment = 30; // 증가량 (30%)

    moveIndex(popularIndex, popularCount,'popular');
    moveIndex(busiestIndex, busiestCount, 'busiest');
    moveIndex(directIndex, directCount, 'direct');

    $('#prev-popular').click(function (){
        if (popularIndex <= 0 ) return;
        popularIndex -= 1;
        moveIndex(popularIndex, popularCount, 'popular');
    });
    $('#next-popular').click(function (){
        if (popularIndex >= popularCount - 3) return;
        popularIndex += 1;
        moveIndex(popularIndex, popularCount, 'popular');
    });
    $('#prev-busiest').click(function (){
        if (busiestIndex <= 0 ) return;
        busiestIndex -= 1;
        moveIndex(busiestIndex, busiestCount, 'busiest');
    });
    $('#next-busiest').click(function (){
        if (busiestIndex >= busiestCount - 3) return;
        busiestIndex += 1;
        moveIndex(busiestIndex, busiestCount, 'busiest');
    });
    $('#prev-direct').click(function (){
        if (directIndex <= 0 ) return;
        directIndex -= 1;
        moveIndex(directIndex, directCount, 'direct');
    });
    $('#next-direct').click(function (){
        if (directIndex >= directCount - 3) return;
        directIndex += 1;
        moveIndex(directIndex, directCount, 'direct');
    });

    function moveIndex(index, count, str) {
        // popular로 시작하는 클래스명에 따라 반복문을 실행
        for (let i = 0; i < count; i++) {
            var currentLeft = baseLeft + ((i - index) * increment);

            $(`.${str}${i}`).css({
                'transition': 'left 0.3s ease',
                'left': currentLeft + '%' // 계산된 left 값 적용
            });

            if( i >= index && i <= index + 2){
                $(`.${str}${i}`).css({
                    'box-shadow': '0 2px 10px rgba(0, 0, 0, 0.1)'
                });
            }else {
                $(`.${str}${i}`).css({
                    'box-shadow': 'none'
                });
            }
        }

    }

    $('.fab-button').click(function(){
        $(this).toggleClass('active');
        $('.fab-menu').toggleClass('active');
    });

    document.getElementById('searchInput').addEventListener('keypress', function(event) {
        if (event.key === 'Enter') {
            event.preventDefault(); // 기본 동작(폼 제출) 방지

            const query = event.target.value.trim(); // 입력된 값을 가져오고, 앞뒤 공백 제거

            if (query) { // 입력된 값이 있으면
                window.location.href = '/search/card?query=' + encodeURIComponent(query);
            }
        }
    });

    $('.fab-menu-item-container').click(function (){
        const index = $(this).index();

        switch (index){
            case 0:
                window.location.href = '/myPage/review';
                break;
            case 1:
                window.location.href = '/myPage/list';
                break;
            case 2:
                window.location.href = '/myPage/history';
                break;
            default:
                alert("잘못된 접근 입니다 !!")
                window.location.href = '/';
        }
    });

    var bubbleNo = -1;

    $('.plus-button').on('click', function (e) {
        bubbleNo = $(this).parent().attr('no');

        e.preventDefault();
        $('.bubble-menu')
            .removeClass('hidden')
            .css({
                // display: 'block',
                left: e.pageX,
                top: e.pageY - 130
            });
    });

    $('.bubble-option').on('click', function () {
        if($(this).text() === "예약"){
            window.location.href = '/reservation/'+bubbleNo;
        }
        else {
            window.location.href = '/users/waitingForm/'+bubbleNo;
        }
    })

    // 메뉴에서 다른 곳을 클릭하면 메뉴 숨기기
    $(document).on('click', function () {
        // 클릭한 요소가 .plus-button 요소가 아닐 때만 실행
        if (!$(event.target).closest('.plus-button').length) {
            $('.bubble-menu').addClass('hidden');
        }

        // 클릭한 요소가 .plus-button 요소가 아닐 때만 실행
        if (!$(event.target).closest('.fab-button').length) {
            $('.fab-button').removeClass('active');
            $('.fab-menu').removeClass('active');
        }

        // 클릭한 요소가 #notificationsDropdown 요소가 아닐 때만 실행
        if (!$(event.target).closest('.btn-notify').length) {
            $('#notificationsDropdown').removeClass('show').prev().css({display: 'none'});
            // $('#notificationsDropdown').prev().css({display: 'none'});
        }
        // 클릭한 요소가 #userMenuDropdown 요소가 아닐 때만 실행
        if (!$(event.target).closest('.btn-user').length) {
            $('#userMenuDropdown').removeClass('show').prev().css({display: 'none'});
            // $('#userMenuDropdown').prev().css({display: 'none'});
        }
    });

});

function toggleHeart(element) {
    var emptyHeart = element.querySelector('.empty-heart');
    var fullHeart = element.querySelector('.full-heart');

    if (fullHeart.style.display === 'none') {
        fullHeart.style.display = 'block';
        emptyHeart.style.display = 'none';
    } else {
        fullHeart.style.display = 'none';
        emptyHeart.style.display = 'block';
    }
}