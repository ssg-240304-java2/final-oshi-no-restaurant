document.getElementById('more-button').addEventListener('click', function() {
    this.classList.toggle('active');
    if (this.classList.contains('active')) {
        this.style.backgroundColor = '#FA4A54';
        this.style.color = '#fff';
    } else {
        this.style.backgroundColor = '#fff';
        this.style.color = '#FA4A54';
    }
});

// // 예시: 식당 번호를 URL에서 가져온다고 가정
// const restaurantNo = new URLSearchParams(window.location.search).get('restaurantNo');
//
// // 식당 정보를 서버에서 가져오는 함수
// async function fetchRestaurantDetails(restaurantNo) {
//     const response = await fetch(`/api/restaurants/${restaurantNo}`);
//     const data = await response.json();
//     return data;
// }
//
// // 식당 정보를 가져와서 이미지 설정
// fetchRestaurantDetails(restaurantNo).then(data => {
//     console.log("Image URL:", data.imgUrl);  // 콘솔에 이미지 URL 출력
//     document.getElementById("dynamic-img").src = data.imgUrl;
// }).catch(error => {
//     console.error("Error fetching restaurant details:", error);
// });



const images = [
    'https://via.placeholder.com/150?text=Image+1',
    'https://via.placeholder.com/150?text=Image+2',
    'https://via.placeholder.com/150?text=Image+3',
    'https://via.placeholder.com/150?text=Image+4',
    'https://via.placeholder.com/150?text=Image+5',
    'https://via.placeholder.com/150?text=Image+6',
    'https://via.placeholder.com/150?text=Image+7',
    'https://via.placeholder.com/150?text=Image+8'
];
let currentIndexes = [0, 1, 2, 3];

document.getElementById('arrow-button').addEventListener('click', function() {
    currentIndexes = currentIndexes.map(index => (index + 1) % images.length);
    currentIndexes.forEach((index, i) => {
        document.getElementById(`card-${i}`).querySelector('.img-placeholder').style.backgroundImage = `url(${images[index]})`;
    });
});