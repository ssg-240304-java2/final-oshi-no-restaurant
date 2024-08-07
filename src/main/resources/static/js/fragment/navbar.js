$(function () {
    $('.btn-notify').on('click', function () {
        $('#notificationsDropdown').toggleClass('show');

        if($('#notificationsDropdown').hasClass('show')){
            $('#notificationsDropdown').prev().css({display: 'block'});
        }
        else{
            $('#notificationsDropdown').prev().css({display: 'none'});
        }
    });

    $('.btn-user').on('click', function () {
        $('#userMenuDropdown').toggleClass('show');

        if($('#userMenuDropdown').hasClass('show')){
            $('#userMenuDropdown').prev().css({display: 'block'});
        }
        else{
            $('#userMenuDropdown').prev().css({display: 'none'});
        }
    });
});
