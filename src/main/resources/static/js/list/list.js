var map;
var markers = [];
var infoWindows = [];
var currentActiveRestaurantItem = null;

$(document).ready(function () {
    map = new naver.maps.Map('map', {
        center: new naver.maps.LatLng(37.5666805, 126.9784147),
        zoom: 10
    });

    $('#follow-list').click(function () {
        $('.content-section').removeClass('active');
        $('#following-list').addClass('active');
        $('#follow-list').addClass('active');
        $('#follow-list').css('color', '#fb3f4a');
        $('#favorite-list').css('color', 'black');
        $('#favorite-list').removeClass('active');
        $('#total-list-count').hide(); // 팔로잉 리스트 클릭 시 전체 리스트 개수 숨기기
    });

    $('#favorite-list').click(function () {
        $('.content-section').removeClass('active');
        $('#favorite-content').addClass('active');
        $('#favorite-list').addClass('active');
        $('#favorite-list').css('color', '#fb3f4a');
        $('#follow-list').css('color', 'black');
        $('#follow-list').removeClass('active');
        $('#total-list-count').show(); // 내 쩝쩝 리스트 클릭 시 전체 리스트 개수 표시
    });
});

function showRestaurantList(area) {
    $('.content-section').removeClass('active');
    $('#restaurant-list').addClass('active');
    $('#favorite-list').addClass('active');
    $('#follow-list').removeClass('active');
    $('.btn-custom').removeClass('active');
    $(`.btn-custom:contains(${area})`).addClass('active');
    $('#total-list-count').hide(); // 전체 리스트 개수 숨기기

    clearMarkers();

    // 예제 맛집 리스트
    var restaurantList = [
        {name: '밍글스', address: '서울특별시 강남구 논현동 논현로122길 102호', image: 'restaurant1.jpg', lat: 37.5665, lng: 126.9780},
        {name: '정식당', address: '서울특별시 강남구 삼성동 113-23', image: 'restaurant2.jpg', lat: 37.5655, lng: 126.9770},
        {name: '백반집', address: '서울특별시 종로구 종로3가 34-6', image: 'restaurant3.jpg', lat: 37.5645, lng: 126.9760}
    ];

    $('#restaurant-list').empty(); // 기존 리스트 제거

    $('#restaurant-list').append(
        '<div class="delete-btn-container">' +
        '<button class="delete-btn" onclick="toggleDeleteCheckboxes()">식당 삭제</button>' +
        '</div>'
    ); // 삭제 버튼을 식당 정보 박스 위로 이동

    restaurantList.forEach(function (restaurant) {
        $('#restaurant-list').append(
            '<div class="restaurant-item" onclick="showRestaurantInfo(this, \'' + restaurant.name + '\', \'' + restaurant.address + '\', \'' + restaurant.image + '\', ' + restaurant.lat + ', ' + restaurant.lng + ')">' +
            '<input type="checkbox" class="delete-checkbox">' +
            '<img src="' + restaurant.image + '" alt="Restaurant">' +
            '<div class="restaurant-info">' +
            '<h5>' + restaurant.name + '</h5>' +
            '<p>' + restaurant.address + '</p>' +
            '<p class="rating">4.5 ★</p>' +
            '</div>' +
            '</div>'
        );

        // 마커 추가
        var marker = new naver.maps.Marker({
            position: new naver.maps.LatLng(restaurant.lat, restaurant.lng),
            map: map,
            icon: {
                url: 'https://maps.google.com/mapfiles/ms/icons/blue-dot.png'
            }
        });
        markers.push(marker);

        // 마커 클릭 시 인포윈도우 표시
        marker.restaurantInfo = restaurant; // 마커에 식당 정보 저장

        naver.maps.Event.addListener(marker, 'click', function () {
            showRestaurantInfoWindow(marker.restaurantInfo.name, marker.restaurantInfo.address, marker.restaurantInfo.image, marker.restaurantInfo.lat, marker.restaurantInfo.lng);
            changeMarkerColor(marker);
        });
    });
}

function clearMarkers() {
    for (var i = 0; i < markers.length; i++) {
        markers[i].setMap(null);
    }
    markers = [];
}

function toggleDeleteCheckboxes() {
    $('.restaurant-item').toggleClass('show-checkbox');
    if ($('.restaurant-item').hasClass('show-checkbox')) {
        $('.delete-btn').text('삭제 확인');
    } else {
        $('.delete-btn').text('식당 삭제');
    }
}

function showRestaurantInfo(element, name, address, image, lat, lng) {
    if ($(element).hasClass('active')) {
        $(element).removeClass('active');
        hideRestaurantInfo();
        return;
    }

    $('.restaurant-item').removeClass('active');
    $(element).addClass('active');

    showRestaurantInfoWindow(name, address, image, lat, lng);

    var marker = markers.find(m => m.getPosition().lat() === lat && m.getPosition().lng() === lng);
    if (marker) {
        changeMarkerColor(marker);
    }
}

function showRestaurantInfoWindow(name, address, image, lat, lng) {
    var infoWindow = $('#restaurant-info-window');
    infoWindow.html('<h5>' + name + '</h5><p>' + address + '</p><img src="' + image + '" alt="Restaurant" style="width:100px; height:100px; margin-top:10px;">');

    var coord = new naver.maps.LatLng(lat, lng);
    var newCenter = new naver.maps.LatLng(lat, lng - 0.018);
    map.setCenter(newCenter);
    map.setZoom(15);

    // 마커의 우측에 정보 표시
    var proj = map.getProjection();
    var point = proj.fromCoordToOffset(coord);

    infoWindow.css({
        display: 'block',
        top: point.y - 50 + 'px', // 정보 창이 마커 바로 위에 나타나도록 조정
        left: point.x + 50 + 'px' // 정보 창이 마커의 우측에 나타나도록 조정
    });
}

function hideRestaurantInfo() {
    $('#restaurant-info-window').hide();
}

function showFollowerList(follower) {
    $('.content-section').removeClass('active');
    $('#follower-restaurant-items').addClass('active');

    $('#follower-restaurant-items').empty(); // 기존 리스트 제거

    $.ajax({
        url: '/list/member',
        type: 'post',
        data: {memberNo: follower},
        success: function (result) {
            appendLists(result);
        },
        error: function (e) {
            console.log(e);
        }
    })
}

function appendLists(data) {
    const listContainer = $('#follower-restaurant-items');
    listContainer.empty();

    data.forEach(list => {
        let innerHTML = `
        <button type="button" class="btn btn-custom" onclick="showFollowerRestaurantList(${list.listNo})">${list.listName}</button>
        `
        listContainer.append(innerHTML);
    });
}

function showFollowerRestaurantList(listNo) {
    $('.content-section').removeClass('active');
    $('#follower-restaurant-list').addClass('active');

    clearMarkers();

    // 예제 맛집 리스트
    // var restaurantList = [
    //     {name: '밍글스', address: '서울특별시 강남구 논현동 논현로122길 102호', image: 'restaurant1.jpg', lat: 37.5665, lng: 126.9780},
    //     {name: '정식당', address: '서울특별시 강남구 삼성동 113-23', image: 'restaurant2.jpg', lat: 37.5655, lng: 126.9770},
    //     {name: '백반집', address: '서울특별시 종로구 종로3가 34-6', image: 'restaurant3.jpg', lat: 37.5645, lng: 126.9760}
    // ];
    $.ajax({
        url: '/zzupList/list',
        type: 'get',
        data: {listNo: listNo},
        success: function (result) {
            console.log(result);
            appendRestaurants(result);
        },
        error: function (e) {
            console.log(e);
        }
    })

}

function appendRestaurants(data) {
    const appendContainer = $('#follower-restaurant-list');
    appendContainer.empty();

    data.forEach(function (restaurant) {
        $('#follower-restaurant-list').append(
            `<div class="restaurant-item" onclick="showFollowerRestaurantInfo(this,'${restaurant.restaurantName}','${restaurant.address}','${restaurant.imgUrl}','${restaurant.xcoordinate}','${restaurant.ycoordinate}')">
            <input type="checkbox" class="delete-checkbox">
            <img src="${restaurant.imgUrl}" alt="Restaurant">
            <div class="restaurant-info">
            <h5>${restaurant.restaurantName}</h5>
            <p>${restaurant.address}</p>
            <p class="rating">${restaurant.rating} ★</p>
            </div>
            </div>`
        );

        // 마커 추가
        var marker = new naver.maps.Marker({
            position: new naver.maps.LatLng(restaurant.xcoordinate, restaurant.ycoordinate),
            map: map,
            icon: {
                url: 'https://maps.google.com/mapfiles/ms/icons/blue-dot.png'
            }
        });
        markers.push(marker);

        // 마커의 infoWindow 생성 및 마커에 연관지음
        // var infoWindow = new naver.maps.InfoWindow({
        //     content: `<div style="width:150px;text-align:center;padding:10px;">
        //                     <b>${restaurant.restaurantName}</b><br/>
        //                     ${restaurant.address}
        //                   </div>`
        // });
        // infoWindows.push(infoWindow);

        // 마커에 infoWindow를 연결하여 클릭 시 열리도록 설정
        naver.maps.Event.addListener(marker, 'click', function () {
            infoWindows.forEach(iw => iw.close()); // 다른 정보 창 닫기
            if (infoWindow.getMap()) {
                infoWindow.close();
                marker.setIcon({url: 'https://maps.google.com/mapfiles/ms/icons/blue-dot.png'})
            } else {
                infoWindow.open(map, marker);
                marker.setIcon({url: 'https://maps.google.com/mapfiles/ms/icons/red-dot.png'})
            }
        });

        // 마커 객체에 infoWindow 속성을 추가
        marker.infoWindow = infoWindow;
    });
}

function showFollowerRestaurantInfo(element, name, address, image, lat, lng) {
    if ($(element).hasClass('active')) {
        $(element).removeClass('active');
        hideRestaurantInfo();
        return;
    }

    $('.restaurant-item').removeClass('active');
    $(element).addClass('active');

    showRestaurantInfoWindow(name, address, image, lat, lng);

    // 해당 위치의 마커 찾기
    var marker = markers.find(m => m.getPosition().lat() === lat && m.getPosition().lng() === lng);

    if (marker) {
        changeMarkerColor(marker);

        // 기존 열려있는 모든 infoWindow 닫기
        infoWindows.forEach(iw => iw.close());

        // 마커의 infoWindow 열기
        marker.infoWindow.open(map, marker);
    }
}


function changeMarkerColor(marker) {
    markers.forEach(function (m) {
        m.setIcon({
            url: 'https://maps.google.com/mapfiles/ms/icons/blue-dot.png'
        });
    });
    marker.setIcon({
        url: 'https://maps.google.com/mapfiles/ms/icons/red-dot.png'
    });
}

function toggleSidebar() {
    $('.container-fluid').toggleClass('collapsed');
    if ($('.toggle-btn').text() === '<') {
        $('.toggle-btn').text('>').css({left: '0px'});
    } else {
        $('.toggle-btn').text('<').css({left: '50%'});
    }
}

$(document).on('click', function (event) {
    if (!$(event.target).closest('.restaurant-item').length && !$(event.target).closest('.restaurant-info-window').length) {
        hideRestaurantInfo();
    }
});