var mapOptions = {
    center: new naver.maps.LatLng(37.3595704, 127.105399),
    zoom: 10
};
var map = new naver.maps.Map('map', mapOptions);

$(document).ready(function() {
    // 지도와 식당 정보 토글 기능
    $('.toggle-button').click(function() {
        var info = $('#restaurantInfo');
        var mapArea = $('#mapArea');
        var button = $(this);

        if (info.width() > 0) {
            info.css('width', '0');
            mapArea.css('width', '100%');
            button.text('▶');
        } else {
            info.css('width', '50%');
            mapArea.css('width', '50%');
            button.text('◀');
        }

        // 지도가 새로 확장되거나 축소될 때 다시 렌더링되도록 강제 업데이트
        setTimeout(function() {
            naver.maps.Event.trigger(map, 'resize');
        }, 300);
    });

    // 지도가 움직일 때 현 지도에서 찾기 버튼 표시
    naver.maps.Event.addListener(map, 'dragend', onMapMove);

    // 현 지도에서 찾기 버튼 클릭 시
    $('#findButton').click(function() {
        // 현재 지도 위치를 기반으로 서버에 요청
        $.ajax({
            url: '/find',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                // 지도 위치 정보를 담아 보낼 수 있도록
            }),
            success: function(data) {
                // 받아온 데이터를 처리
                console.log(data);
            }
        });
    });

    // 카테고리 버튼 클릭 시 선택 상태 토글
    $('.category-buttons button').click(function() {
        $(this).toggleClass('selected btn-primary text-white');
    });

    function onMapMove() {
        $('#findButton').show();
    }
});