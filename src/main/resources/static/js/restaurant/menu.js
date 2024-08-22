// 메뉴 등록
$('#menuBtn').on('click', function () {

    console.log("🍎메뉴 버튼 클릭됨")

    const menuName = $('#menuName').val();
    const description = $('#menuDescription').val();
    const imgUrl = $("#imgUrl")[0].files[0];

    const formData = new FormData();

    const newMenu = {
        "menuName" : menuName,
        "description" : description,
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

