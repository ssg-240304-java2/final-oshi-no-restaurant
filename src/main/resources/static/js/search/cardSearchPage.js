$(document).ready(function () {
    // Toggle action buttons on clicking the plus button
    $(document).on('click', '.toggle-actions', function () {
        $(this).closest('.card').toggleClass('show-actions');
    });

    let currentPage = 1;
    let isRequestInProgress = false;

    window.onscroll = function () {
        if ((window.innerHeight + window.scrollY) >= document.body.offsetHeight) {
            // 스크롤이 페이지 하단에 도달했을 때 다음 페이지를 요청
            loadMoreRestaurants();
        }
    };

    function loadMoreRestaurants() {
        const keyword = new URLSearchParams(window.location.search).get('query');  // 예: 검색어가 있는 경우

        if(isRequestInProgress){
            return;
        }

        isRequestInProgress = true;
        $.ajax({
            url: `/search/card`,  // 서버의 엔드포인트
            type: 'post',
            dataType: 'json',
            data: {
                keyword: keyword,
                page: currentPage
            },
            success: function(data) {
                console.log(data);
                appendRestaurants(data);
                currentPage++;  // 페이지 번호 증가
            },
            error: function(xhr, status, error) {
                console.error('Error fetching data:', error);
            },
            complete: function () {
                isRequestInProgress = false;
            }
        });
    }

    function appendRestaurants(restaurants) {
        const container = $('#restaurant-container');
        restaurants.forEach(function(restaurant) {
            const restaurantHtml = `
            <div class="col-md-4 d-flex align-items-stretch">
                <div class="card h-100">
                    <img src="${restaurant.imgUrl}" class="card-img-top" alt="Restaurant Image">
                    <div class="card-body">
                        <h5 class="card-title">${restaurant.restaurantName}</h5>
                        <p class="card-text rating">평점: ${restaurant.rating} ★</p>
                        <p class="card-text">카테고리: ${restaurant.foodType}</p>
                        <p class="card-text">주소: ${restaurant.restaurantAddress}</p>
                        <button class="btn btn-custom btn-sm toggle-actions">+</button>
                        <div class="action-buttons">
                            <button class="btn btn-info btn-sm" onclick="location.href='/reservation/' + ${restaurant.restaurantNo}">예약</button>
                        <button class="btn btn-warning btn-sm" onclick="location.href='/users/waitingForm/' + ${restaurant.restaurantNo}">웨이팅</button>
                        </div>
                    </div>
                </div>
            </div>`;
            container.append(restaurantHtml);
        });
    }
});