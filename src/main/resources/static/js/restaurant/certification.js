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

function code_check() {
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
                    document.frm1.submit();
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
}