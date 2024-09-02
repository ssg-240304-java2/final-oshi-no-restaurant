// 메뉴 등록
$('#menuBtn').on('click', function () {

    console.log("🍎메뉴 버튼 클릭됨")

    const menuName = $('#menuName').val();
    const description = $('#menuDescription').val();
    const imgUrl = $("#imgUrl")[0].files[0];

    const formData = new FormData();

    const newMenu = {
        "menuName": menuName,
        "description": description,
    };

    formData.append('file', imgUrl);
    formData.append('newMenu', new Blob([JSON.stringify(newMenu)], {type: 'application/json'}));

    console.log(menuName, description, imgUrl);

    $.ajax({
        type: 'post',
        url: '/restaurant/menuRegister',
        contentType: false,
        processData: false,
        data: formData,
        success: function (result) {
            console.log("success", result);
            window.location.href = '/restaurant/menu';
        },
        error: function (e) {
            console.log("failed", e);
        }
    })
})

// 메뉴 사진 미리보기
function previewImage(event) {
    const image = document.getElementById('imagePreview');
    const file = event.target.files[0];

    if (file) {
        const reader = new FileReader();
        reader.onload = function (e) {
            image.src = e.target.result;
            image.style.display = 'block';
        };
        reader.readAsDataURL(file);
    } else {
        image.src = '';
        image.style.display = 'none';
    }
}

// 메뉴 수정 버튼 클릭 시 데이터 로드
$(document).on('click', '.updateBtn', function () {
    const menuId = $(this).attr("data-id");

    // 해당 메뉴의 데이터를 서버에서 가져오기 (이 부분은 예시로, 실제로는 AJAX 요청으로 서버에서 데이터를 가져와야 함)
    $.ajax({
        type: 'get',
        url: '/restaurant/getMenuDetails',  // 이 URL은 예시이며, 실제로 서버에서 메뉴 상세 정보를 제공하는 엔드포인트로 변경해야 함
        data: { menuId: menuId },
        success: function (menu) {
            // 가져온 데이터를 폼에 로드
            $('#menuId').val(menu.menuNo);
            $('#menuName').val(menu.menuName);
            $('#menuDescription').val(menu.description);
            $('#imagePreview').attr('src', menu.imgUrl).show();

            $('#menuBtn').hide();
            $('#updateMenuBtn').show();
        },
        error: function (e) {
            console.log("메뉴 데이터를 불러오지 못했습니다.", e);
        }
    });
});

// 메뉴 수정 요청
$('#updateMenuBtn').on('click', function () {

    const menuId = $('#menuId').val();
    const menuName = $('#menuName').val();
    const description = $('#menuDescription').val();
    const imgUrl = $("#imgUrl")[0].files[0];

    const formData = new FormData();
    const updatedMenu = {
        "menuNo": menuId,
        "menuName": menuName,
        "description": description
    };

    formData.append('file', imgUrl);
    formData.append('updatedMenu', new Blob([JSON.stringify(updatedMenu)], {type: 'application/json'}));

    $.ajax({
        type: 'post',
        url: '/restaurant/updateMenu',
        contentType: false,
        processData: false,
        data: formData,
        success: function (result) {
            console.log("메뉴 수정 완료", result);
            window.location.href = '/restaurant/menu';
        },
        error: function (e) {
            console.log("메뉴 수정 실패", e);
        }
    });
});


