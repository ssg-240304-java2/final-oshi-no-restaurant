$(document).ready(function () {
    $('#serviceNo').val(new URLSearchParams(window.location.search).get('serviceNo'))
    $('#serviceType').val(new URLSearchParams(window.location.search).get('serviceType'))
    $('#restaurantNo').val(new URLSearchParams(window.location.search).get('restaurantNo'))
    const serviceNo = $('#serviceNo').val();
    const serviceType = $('#serviceType').val();
    const restaurantNo = $('#restaurantNo').val();

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
    $("#registerBtn").click(function () {

        var reviewContent = $("#review-content").val();
        var rating = $(".star-rating .fa-star.checked").last().data("value");
        var scope = 'STAR' + rating;  // Enum 이름 생성
        var reviewTags = [];
        $(".tag-btn.active").each(function() {
            reviewTags.push($(this).text());
        });
        var file = $('#file')[0].files[0];

        var reviewNo = $('#reviewNo').val();

        var jsonData = {
            "reviewContent": reviewContent,
            "scope": scope,
            "reviewTags": reviewTags,
            "reviewNo": reviewNo,
            "restaurantNo": parseInt(restaurantNo),
            "serviceNo" : parseInt(serviceNo),
            "type": serviceType
        }

        // 리뷰 데이터 객체 생성
        var requestDataReview = new FormData ();

        requestDataReview.append('file',file);
        requestDataReview.append('jsonData',new Blob([JSON.stringify(jsonData)], { type: 'application/json'}));
        // requestDataReview.append('reviewNo',reviewNo);
        // // 배열의 각 요소를 FormData에 추가
        // reviewTags.forEach(function(tag) {
        //     requestDataReview.append('reviewTags', tag);
        // });
        // requestDataReview.append('restaurantNo',restaurantNo);
        // requestDataReview.append('reviewContent',reviewContent);
        // requestDataReview.append('scope',scope);


        requestDataReview.forEach((value, key) =>{
            console.log(key, value)
        })

        // AJAX 요청 전송
        insertReview(requestDataReview);

        // 이전 페이지로 돌아가는 로직 추가

    });

    // 등록하기 버튼 클릭 이벤트
    $("#updateBtn").click(function () {
        var reviewContent = $("#review-content").val();
        var rating = $(".star-rating .fa-star.checked").last().data("value");
        var scope = 'STAR' + rating;  // Enum 이름 생성
        var reviewTags = [];
        $(".tag-btn.active").each(function() {
            reviewTags.push($(this).text());
        });
        var file = $('#file')[0].files[0];

        var reviewNo = $('#reviewNo').val();

        var jsonData = {
            "reviewContent": reviewContent,
            "scope": scope,
            "reviewTags": reviewTags,
            "reviewNo": reviewNo,
            "restaurantNo": parseInt(restaurantNo),
            "serviceNo" : parseInt(serviceNo),
            "type": serviceType
        }

        // 리뷰 데이터 객체 생성
        var requestDataReview = new FormData ();

        requestDataReview.append('file',file);
        requestDataReview.append('jsonData',new Blob([JSON.stringify(jsonData)], { type: 'application/json'}));

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
        cache: false, // 캐싱을 막기 위해 추가
        data: requestDataReview,
        // contentType: 'application/json',
        success: function (response) {
            console.log('성공');
            alert('리뷰가 성공적으로 등록되었습니다.');
            window.location.href = response;
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
