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
                    mapOptions = {
                        center: new naver.maps.LatLng(latitude,longitude),
                        zoom: 16
                    }
                    map = new naver.maps.Map('map',mapOptions)
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
            const query = new URLSearchParams(window.location.search).get('query');

            $.ajax({
                type: 'get',
                url: `/search/map/coordinate?latitude=${latitude}&longitude=${longitude}&query=${query}` + encodeURIComponent(query),
                success: function ( data ){
                    console.log(data);
                    appendRestaurants(data);
                },
                error: function (e) {
                    console.log(e)
                }
            });
        }
    });

    // 카테고리 버튼 클릭 시 선택 상태 토글
    $('.category-buttons button').click(function() {
        $(this).toggleClass('selected btn-primary text-white');
    });

    function onMapMove() {
        $('#findButton').show();
    }

    document.getElementById('searchInput').addEventListener('keypress', search);
    $('#searchButton').on('click',search);

    function search (event){
        if (event.key === 'Enter' || $(this).hasClass('btn')) {
            event.preventDefault(); // 기본 동작(폼 제출) 방지

            const query = $('#searchInput').val(); // 입력된 값을 가져오고, 앞뒤 공백 제거

                $.ajax({
                    type: 'get',
                    url: '/search/map/coordinate',
                    data: {
                        latitude: latitude,
                        longitude: longitude,
                        query: query
                    },
                    success: function ( data ){
                        console.log(data);
                        appendRestaurants(data);
                    },
                    error: function (e) {
                        console.log(e)
                    }
                })

        }
    }

    function appendRestaurants(restaurants) {
        const container = $('#restaurantInfo');
        document.getElementById('restaurantInfo').replaceChildren();
        restaurants.forEach(function(restaurant) {
            const restaurantHtml = `
            <div class="restaurant-card" id="${restaurant.restaurantNo}">
            <h3>${restaurant.restaurantName}</h3>
            <img src="${restaurant.imgUrl}">
            <p>${restaurant.restaurantAddress}</p>
            <p>${restaurant.foodType}</p>
            <p>${restaurant.rating}</p>
            <button class="favorite-button btn btn-outline-secondary mt-2">관심 식당 등록</button>
        </div>`;
            container.append(restaurantHtml);

            // 위치 생성
            var restaurantPosition = new naver.maps.LatLng(restaurant.xcoordinate, restaurant.ycoordinate);

            // 마커 생성
            var marker = new naver.maps.Marker({
                map: map,
                position: restaurantPosition,
                icon: {
                    url: 'https://maps.google.com/mapfiles/ms/icons/red-dot.png'
                }
            });
            // 정보 창 생성
            var infoWindow = new naver.maps.InfoWindow({
                content: `<div style="width:150px;text-align:center;padding:10px;">
                                <b>${restaurant.restaurantName}</b><br/>
                                ${restaurant.restaurantAddress}
                              </div>`
            });

            // 마커 클릭 시 정보 창 표시 및 닫기 기능 추가
            naver.maps.Event.addListener(marker, 'click', function() {
                if (infoWindow.getMap()) {
                    // 정보 창이 이미 열려 있다면 닫음
                    infoWindow.close();
                    marker.setIcon({ url: 'https://maps.google.com/mapfiles/ms/icons/red-dot.png'})
                } else {
                    // 정보 창이 열려 있지 않다면 열음
                    infoWindow.open(map, marker);
                    marker.setIcon({ url: 'https://maps.google.com/mapfiles/ms/icons/blue-dot.png'})
                }
            });


        });
    }

    $('.restaurant-card').on('click', function () {

        window.location.href = ''+$(this).attr('id');
    });
});

$(document).ready(function(){
    $('.categoryBtn').on('click', function(){
        $(this).toggleClass('active'); // 클릭한 버튼에 active 클래스 추가/제거
    });
});