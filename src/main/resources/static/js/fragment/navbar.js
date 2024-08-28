$(function () {
    $('.btn-notify').on('click', function () {
        $('#notificationsDropdown').toggleClass('show');

        if ($('#notificationsDropdown').hasClass('show')) {
            $('#notificationsDropdown').prev().css({display: 'block'});

            $.ajax({
                url: '/notification',
                type: 'get',
                success: function (data) {
                    const container = $('#notificationContainer');
                    container.empty();
                    data.forEach(function (noti) {
                        const notiHtml = `
                            <div class="notification">
                                <div class="notification-header">
                                    <img src="/images/navbar/${noti.img}.png" alt="알림 아이콘">
                                    <div class="notification-title-date">
                                        <div class="notification-title">${noti.referenceName}</div>
                                        <div class="notification-date">${noti.date}</div>
                                    </div>
                                </div>
                                <div class="notification-text">
                                    ${noti.message}
                                </div>
                            </div>
                            `;
                        container.append(notiHtml);
                    });

                    if(!container.children().length > 0){
                        const notiHtml = `
                            <div class="notification">
                                <div class="notification-text">
                                    알림이 없습니다.
                                </div>
                            </div>
                            `;
                        container.append(notiHtml);
                    }

                },
                error: function (e) {
                    console.log(e);
                }
            });
        } else {
            $('#notificationsDropdown').prev().css({display: 'none'});
        }
    });

    $('.btn-user').on('click', function () {
        $('#userMenuDropdown').toggleClass('show');

        if ($('#userMenuDropdown').hasClass('show')) {
            $('#userMenuDropdown').prev().css({display: 'block'});
        } else {
            $('#userMenuDropdown').prev().css({display: 'none'});
        }
    });
});
