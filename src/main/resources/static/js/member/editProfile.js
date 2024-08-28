$(document).ready(function () {

    // 프로필 사진 변경
    $('#photoInput').on('change', function (event) {
        const file = event.target.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = function (e) {
                $('#profileImage').attr('src', e.target.result);
            };
            reader.readAsDataURL(file);
        }
    });

    // 비밀번호 확인 검증
    $('#passwordConfirm').on('input', function () {
        const password = $('#password').val();
        const confirmPassword = $(this).val();
        const feedback = $('#passwordFeedback');
        const success = $('#passwordSuccess');

        if (confirmPassword !== '' && password !== '') {
            if (confirmPassword === password) {
                $(this).addClass('is-valid').removeClass('is-invalid');
                feedback.hide();
                success.show();
            } else {
                $(this).addClass('is-invalid').removeClass('is-valid');
                feedback.show();
                success.hide();
            }
        } else {
            $(this).removeClass('is-valid is-invalid');
            feedback.hide();
            success.hide();
        }
    });

    // 전화번호 유효성 검사
    $('#phone').on('input', function () {
        const phoneValue = $(this).val();
        const phoneFeedback = $('#phoneFeedback');

        // 전화번호가 11자리 숫자인지 확인
        const phonePattern = /^010\d{8}$/;

        if (phonePattern.test(phoneValue)) {
            $(this).addClass('is-valid').removeClass('is-invalid');
            phoneFeedback.hide();
        } else {
            $(this).addClass('is-invalid').removeClass('is-valid');
            phoneFeedback.show();
        }
    });

    // 닉네임 중복 검사 (예제용)
    $('#nickname').next('.btn-duplicate-check').on('click', function () {
        const nickname = $('#nickname').val();
        if (nickname === "") {
            alert("닉네임을 입력해주세요.");
            return;
        }

        $.ajax({
            type: 'post',
            url: '/signUp/checkDupNickname',
            contentType: 'application/json',
            dataType: 'json',
            data: JSON.stringify({"nickname": nickname}),
            success: function (result) {
                if (result === true) {
                    $('.nickname-before').addClass('hidden');
                    $('.nickname-after').removeClass('hidden');
                    $('#nickname').removeClass('is-valid').addClass('is-invalid');
                } else {
                    $('.nickname-before').addClass('hidden');
                    $('.nickname-after').addClass('hidden');
                    $('#nickname').removeClass('is-invalid').addClass('is-valid');
                }
            },
            error: function (e) {
                console.log(e);
            }
        })
    });

    $('#profileForm').on('submit', function (event) {
        event.preventDefault();
        var formData = new FormData(this); // FormData 객체 생성

        // 이 부분에서 url에 회원 번호도 붙여야 하는지는 생각해봐야 한다.

        $.ajax({
            url: '/myInfo/profile', // 서버의 PATCH 요청을 받을 URL
            type: 'PATCH',
            data: formData,
            processData: false, // jQuery가 데이터를 처리하지 않도록 설정
            contentType: false, // Content-Type을 false로 설정하여 multipart/form-data로 전송
            success: function (response) {
                alert('프로필이 성공적으로 업데이트되었습니다.');
            },
            error: function (error) {
                alert('프로필 업데이트에 실패했습니다.');
            }
        });
    });

    $('#btn-change-password').on('click', function () {

        $(this).addClass('hidden');
        $('#password-form').removeClass('hidden');
        $('#passwordConfirm-form').removeClass('hidden');

    });


    $('#password').on('input', function () {
        const passwordRegex = /^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*?_]).{8,16}$/;

        if (passwordRegex.test($(this).val())) {
            $(this).removeClass('is-invalid');
            $('#password-feedback').addClass('hidden');
            $(this).addClass('is-valid');
        } else {
            $(this).removeClass('is-valid');
            $('#password-feedback').removeClass('hidden');
            $(this).addClass('is-invalid');
        }
    });

    $('#confirmPassword').on('input', function () {

        if ($(this).val() === $('#password').val()) {
            $(this).removeClass('is-invalid');
            $('#confirmPassword-feedback').addClass('hidden');
            $(this).addClass('is-valid');
        } else {
            $(this).removeClass('is-valid');
            $('#confirmPassword-feedback').removeClass('hidden');
            $(this).addClass('is-invalid');
        }
    });

    $('#btn-submit').on('click', function () {

        // PWD 체크
        const password = $('#password').val();
        if( $('#btn-change-password').hasClass('hidden') ){
            if(password === ""){
                alert('비밀번호를 입력해 주세요');
                return;
            }

            if( !$('#password').hasClass('is-valid') || !$('#confirmPassword').hasClass('is-valid') ){
                alert('유효하지않은 비밀번호 입니다.');
                return;
            }
        }

        // 이름 체크
        const name = $('#name').val();
        if (name === ""){
            alert('이름을 입력해주세요.');
            return;
        }

        // 닉네임 체크
        const nickname = $('#nickname').val();
        if ( nickname !== $('#nickname').data('initial-value') ){
            if( nickname === "" ){
                alert('닉네임을 입력해주세요.');
                return;
            }

            if( $('#nickname').hasClass('is-invalid') || !$('#nickname').hasClass('is-valid') ){
                alert('닉네임 중복체크 후 변경가능합니다.')
                return;
            }
        }

        // 생년월일 체크
        const birthday = $('#birthDate').val();
        if(birthday !== $('#birthDate').data('initial-value') ){

            if( birthday === "" ){
                alert('생년월일을 입력해주세요.');
                return;
            }

            if( $('#birthDate').hasClass('is-invalid') ){
                alert('유효한 생년월일을 입력해주세요.')
                return;
            }
        }

        // 성별 체크안함
        const gender = $('input[name="gender"]:checked').val();

        // 전화번호 체크
        const phone = $('#phone').val();
        if( phone === "" ){
            alert('전화번호가 유효하지 않습니다.')
            return;
        }

        // 자기소개 체크안함
        const introduce = $('#bio').val()

        // 이미지 변경확인
        const imgUrl = $('#profileImage').attr('src') !== $('#profileImage').data('initial-value')
            ?$('#photoInput')[0].files[0]:$('#profileImage').data('initial-value');


        const formData = new FormData();

        const jsonData = {
            "password": password,
            "name": name,
            "nickname": nickname,
            "birthday": birthday,
            "gender": gender,
            "phone": phone,
            "introduction": introduce
        }

        // formData에 데이터들 적재
        formData.append('file',imgUrl);
        formData.append('jsonData',new Blob([JSON.stringify(jsonData)], { type: 'application/json'}));

        console.log(formData);

        $.ajax({
            type: 'put',
            url: '/myInfo',
            contentType: false,
            processData: false,
            data: formData,
            success: function (result) {
                console.log(result);

                window.location.href = '/myPage';
            },
            error: function (e) {
                console.log(e)
            }
        })


    })
});