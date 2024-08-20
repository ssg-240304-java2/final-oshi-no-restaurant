var mapOptions = {
    center: new naver.maps.LatLng(37.3595704, 127.105399),
    zoom: 10
};
var map = new naver.maps.Map('map', mapOptions);


$(document).ready(function() {
    let latitude;
    let longitude;
    // 현재 위치 가져오기
    getGeolocation();

    function getGeolocation() {
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(
                function(position) {
                    // 성공적으로 위치를 가져온 경우
                    latitude = position.coords.latitude;
                    longitude = position.coords.longitude;

                    console.log('위도: ' + latitude);
                    console.log('경도: ' + longitude);
                },
                function(error) {
                    // 위치 정보를 가져오는데 실패한 경우
                    switch(error.code) {
                        case error.PERMISSION_DENIED:
                            console.error("사용자가 위치 정보 제공을 거부했습니다.");
                            alert("사용자가 위치 정보 제공을 거부했습니다.");
                            return;
                        case error.POSITION_UNAVAILABLE:
                            console.error("위치 정보를 사용할 수 없습니다.");
                            return;
                        case error.TIMEOUT:
                            console.error("위치 정보를 가져오는 데 시간이 초과되었습니다.");
                            return;
                        case error.UNKNOWN_ERROR:
                            console.error("알 수 없는 오류가 발생했습니다.");
                            return;
                    }
                }
            );
        } else {
            console.error("이 브라우저에서는 Geolocation을 지원하지 않습니다.");
            return;
        }
    }

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
        getGeolocation();

        if(latitude === undefined || longitude === undefined){
            alert('위치좌표제공에 동의해주세요.')
        } else {
            window.location.href=`/search/map?latitude=${latitude}&longitude=${longitude}`
        }
    });

    // 카테고리 버튼 클릭 시 선택 상태 토글
    $('.category-buttons button').click(function() {
        $(this).toggleClass('selected btn-primary text-white');
    });

    function onMapMove() {
        $('#findButton').show();
    }
});

