// 팝업을 나타나게 하는 함수
function showPopup() {
    $('#badgePopup').toggleClass('show');
}

// 페이지 로드 시 팝업과 뱃지 업데이트
$('#showBadge').on('click', function() {
    showPopup();
});
