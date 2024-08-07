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

    // 아이디 중복 확인 (예제용)
    $('#userId').next('.btn-duplicate-check').on('click', function () {
        const userId = $('#userId').val();
        if (userId === "") {
            alert("아이디를 입력해주세요.");
            return;
        }

        // 여기에 중복 검사 로직 추가 (예: AJAX 요청)
        // 예제에서는 간단히 중복 검사 실패로 처리
        const isDuplicate = false; // 중복 검사 결과 예제 (false로 설정)

        if (isDuplicate) {
            alert("아이디가 이미 사용 중입니다.");
        } else {
            alert("사용 가능한 아이디입니다.");
        }
    });

    // 닉네임 중복 검사 (예제용)
    $('#nickname').next('.btn-duplicate-check').on('click', function () {
        const nickname = $('#nickname').val();
        if (nickname === "") {
            alert("닉네임을 입력해주세요.");
            return;
        }

        // 여기에 중복 검사 로직 추가 (예: AJAX 요청)
        // 예제에서는 간단히 중복 검사 실패로 처리
        const isDuplicate = false; // 중복 검사 결과 예제 (false로 설정)

        if (isDuplicate) {
            alert("닉네임이 이미 사용 중입니다.");
        } else {
            alert("사용 가능한 닉네임입니다.");
        }
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
});