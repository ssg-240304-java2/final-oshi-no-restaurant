$('#followButton').on('click', function (){
    let memberNo = new URL(window.location.href).pathname.split('/').at(-1);

    $.ajax({
        type: 'post',
        url: '/follow',
        contentType: 'application/json',
        data: JSON.stringify({
            "memberNo": memberNo
        }),
        success: function (result) {
            console.log('followToggle');

            if(result === true){
                $('#followButton').addClass('following').text('팔로잉');
            }
            else {
                $('#followButton').removeClass('following').text('팔로우');
            }
        },
        error: function (e){
            console.log(e);
        }
    })
});