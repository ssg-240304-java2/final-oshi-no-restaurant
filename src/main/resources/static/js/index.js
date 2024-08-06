$(document).ready(function(){
    $('.fab-button').click(function(){
        $(this).toggleClass('active');
        $('.fab-menu').toggleClass('active');
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

    $('.plus-button').on('click', function (e) {
        e.preventDefault();
        $('.bubble-menu').removeClass('hidden');
        $('.bubble-menu').css({
            // display: 'block',
            left: e.pageX,
            top: e.pageY - 130
        });
    });

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