function sample6_execDaumPostcode() {
    new daum.Postcode({
        oncomplete: function(data) {
            // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

            // 각 주소의 노출 규칙에 따라 주소를 조합한다.
            // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
            var addr = ''; // 주소 변수
            var extraAddr = ''; // 참고항목 변수

            //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
            if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                addr = data.roadAddress;
            } else { // 사용자가 지번 주소를 선택했을 경우(J)
                addr = data.jibunAddress;
            }

            // 사용자가 선택한 주소가 도로명 타입일때 참고항목을 조합한다.
            if(data.userSelectedType === 'R'){
                // 법정동명이 있을 경우 추가한다. (법정리는 제외)
                // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
                if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                    extraAddr += data.bname;
                }
                // 건물명이 있고, 공동주택일 경우 추가한다.
                if(data.buildingName !== '' && data.apartment === 'Y'){
                    extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                }
                // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
                if(extraAddr !== ''){
                    extraAddr = ' (' + extraAddr + ')';
                }
                // 조합된 참고항목을 해당 필드에 넣는다.
                document.getElementById("sample6_extraAddress").value = extraAddr;

            } else {
                document.getElementById("sample6_extraAddress").value = '';
            }

            // 우편번호와 주소 정보를 해당 필드에 넣는다.
            document.getElementById('sample6_postcode').value = data.zonecode;
            document.getElementById("sample6_address").value = addr;
            // 커서를 상세주소 필드로 이동한다.
            document.getElementById("sample6_detailAddress").focus();
        }
    }).open();
}

// 식당 정보 관련 메서드
function handleRestaurantInfo(actionUrl) {
    const storeName = $("#storeName").val();
    const storePhone = $("#storePhone").val();
    const businessAddress = $("#sample6_postcode").val() + $("#sample6_address").val() + $("#sample6_detailAddress").val() + $("#sample6_extraAddress").val();

    const foodType = [];
    $("input[name='foodType']:checked").each(function () {
        foodType.push($(this).val());
    });

    const openingHoursStart = $("#openingHoursStart").val();
    const openingHoursEnd = $("#openingHoursEnd").val();

    const tagType = [];
    $("input[name='tagType']:checked").each(function () {
        tagType.push($(this).val());
    });

    const storeIntro = $("#storeIntro").val();

    console.log(storeName, foodType, tagType);

    $.ajax({
        type: 'post',
        url: actionUrl,
        contentType: 'application/json',
        dataType: 'text',
        data: JSON.stringify({
            "restaurantName": storeName,
            "contact": storePhone,
            "restaurantAddress": businessAddress,
            "foodType": foodType,
            "openingTime": openingHoursStart,
            "closingTime": openingHoursEnd,
            "hashTag": tagType,
            "description": storeIntro
        }),
        success: function (result) {
            console.log("success");
            window.location.href = result;
        },
        error: function (e) {
            console.log("failed");
            console.log(e);
        }
    });
}

// 데이트픽
$(document).ready(function() {
    if($('#reservationDate').length) {
        $("#reservationDate").datepicker({
            dateFormat: "yy-mm-dd"
        });
    }
    if($('#waitingDate').length){
        $("#waitingDate").datepicker({
            dateFormat: "yy-mm-dd"
        });
    }
});

// 회원가입 시 식당 정보 등록
$('#infoRegisterBtn').on('click', function (){
    handleRestaurantInfo('/restaurant/infoRegister');
});

// 식당 정보 수정 및 웨이팅, 예약 정보 입력
$('#updateBtn').on('click', function (){
    handleRestaurantInfo('/restaurant/infoUpdate');
});



