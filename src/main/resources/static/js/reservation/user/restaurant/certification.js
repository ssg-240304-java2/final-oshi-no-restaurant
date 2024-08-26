function checkInput_null(formName, fieldNames) {
    var form = document.forms[formName];
    var fields = fieldNames.split(',');

    for (var i = 0; i < fields.length; i++) {
        var field = form[fields[i]];
        if (field.value.trim() === '') {
            alert(fields[i] + ' 필드가 비어 있습니다.');
            return false;
        }
    }
    return true;
}

document.getElementById('btnCheck').addEventListener('click', function () {
    if (!checkInput_null('frm1', 'code1,code2,code3')) {
        frm1.overlap_code_ok.value = "";
    } else {
        document.frm1.code.value = frm1.code1.value + frm1.code2.value + frm1.code3.value;
        var data = {
            "b_no": [document.frm1.code.value]
        };

        $.ajax({
            url: "https://api.odcloud.kr/api/nts-businessman/v1/status?serviceKey=i7ZpttAqrc65H8cxwcHUlIkdsWQ6vFivVGJsg8uAl2KsjkP3Tl%2BlfwCaquFViG%2B9e%2B0MIAZXSLpR2pTuLw8GZg%3D%3D",
            type: "POST",
            data: JSON.stringify(data),
            dataType: "json",
            traditional: true,
            contentType: "application/json; charset=UTF-8",
            accept: "application/json",
            success: function (result) {
                console.log(result);
                if (result.match_cnt == "1") {
                    console.log("success");
                    $(document.frm1).attr({'action': '', 'type': 'post'}).submit();

                } else {
                    console.log("fail");
                    alert(result.data[0]["tax_type"]);
                }
            },
            error: function (result) {
                console.log("error");
                console.log(result.responseText);
            }
        });
    }
});


function sample6_execDaumPostcode() {
    new daum.Postcode({
        oncomplete: function (data) {
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
            if (data.userSelectedType === 'R') {
                // 법정동명이 있을 경우 추가한다. (법정리는 제외)
                // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
                if (data.bname !== '' && /[동|로|가]$/g.test(data.bname)) {
                    extraAddr += data.bname;
                }
                // 건물명이 있고, 공동주택일 경우 추가한다.
                if (data.buildingName !== '' && data.apartment === 'Y') {
                    extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                }
                // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
                if (extraAddr !== '') {
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


$('#certificationBtn').on('click', function () {

    const companyNo = $("#code1").val() + $("#code2").val() + $("#code3").val();
    const representativeName = $("#representativeName").val();
    const companyName = $("#companyName").val();
    const businessAddress = $("#sample6_postcode").val() + $("#sample6_address").val() + $("#sample6_detailAddress").val() + $("#sample6_extraAddress").val();
    const imgUrl = $("#imgUrl")[0].files[0];

    const formData = new FormData();

    const jsonData = {
        "companyNo": companyNo,
        "representativeName": representativeName,
        "companyName": companyName,
        "businessAddress": businessAddress,
    };

    formData.append('file', imgUrl);
    formData.append('jsonData', new Blob([JSON.stringify(jsonData)], {type: 'application/json'}));

    console.log(companyNo, formData);

    $.ajax({
        type: 'post',
        url: '/restaurant/certification',
        contentType: false,
        processData: false,
        data: formData,
        success: function (result) {
            console.log("success", result);
            window.location.href = '/restaurant/infoRegister';
        },
        error: function (e) {
            console.log("failed", e)
        }
    })
})
