$(document).ready(function () {
    $(".tag-btn").click(function () {
        $(this).toggleClass("active");
    });

    $(".star-rating .fa-star").click(function () {
        var rating = $(this).data("value");
        $(".star-rating .fa-star").each(function () {
            if ($(this).data("value") <= rating) {
                $(this).addClass("checked");
            } else {
                $(this).removeClass("checked");
            }
        });
    });

    // 등록하기 버튼 클릭 이벤트
    $(".btn.btn-register").click(function () {
        var reviewContent = $("#review-content").val();
        var rating = $(".star-rating .fa-star.checked").last().data("value");
        var scope = 'STAR' + rating;  // Enum 이름 생성

        // 바꿔야함 @@@
        // var memberNo = 1;  // 임시 값
        // var reviewNo = 123;  // 임시 값
        var restaurantNo = window.location.href.split('/')[window.location.href.split('/').length-1];  // 임시 값


        // 선택된 태그 수집
        var reviewTags = [];
        $(".tag-btn.active").each(function() {
            reviewTags.push($(this).text());
        });

        var jsonData = {
            // memberNo: memberNo,
            // reviewNo: reviewNo,
            reviewTags: reviewTags, // 배열로 전송
            restaurantNo: restaurantNo,
            reviewContent: reviewContent,
            scope: scope  // 서버로 Enum 이름 전송
        };
        var file = $('#file')[0].files[0];

        // 리뷰 데이터 객체 생성
        var requestDataReview = new FormData ();

        requestDataReview.append('jsonData',new Blob([JSON.stringify(jsonData)], { type: 'application/json'}));
        requestDataReview.append('file',file);

        console.log(requestDataReview);

        // AJAX 요청 전송
        insertReview(requestDataReview);

        // 이전 페이지로 돌아가는 로직 추가

    });
});

function insertReview(requestDataReview) {
    $.ajax({
        url: '/reviewPage',
        method: 'POST',
        contentType: false,
        processData: false,
        data: requestDataReview,
        // contentType: 'application/json',
        success: function (response) {
            console.log('성공');
            alert('리뷰가 성공적으로 등록되었습니다.');
        },
        error: function (jqXHR, textStatus, errorThrown) {
            alert('리뷰 등록에 실패했습니다. 다시 시도해주세요.');
            console.log('AJAX 오류:', textStatus, errorThrown);
        }
    });
}

document.addEventListener("DOMContentLoaded", function() {
    const tagButtons = document.querySelectorAll(".tag-btn");
    const selectedTagsInput = document.getElementById("selectedTags");

    tagButtons.forEach(button => {
        button.addEventListener("click", function() {
            button.classList.toggle("selected"); // 선택된 버튼에 'selected' 클래스 토글

            // 선택된 버튼들을 가져와서 숨겨진 입력 필드를 업데이트
            const selectedTags = Array.from(document.querySelectorAll(".tag-btn.selected"))
                .map(btn => btn.getAttribute("data-tag"));

            // selectedTagsInput.value = selectedTags.join(",");
            console.log(selectedTags);
        });
    });
});
