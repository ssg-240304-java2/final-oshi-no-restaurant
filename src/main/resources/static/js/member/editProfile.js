$(document).ready(function () {



    const authButton = document.getElementById('emailCheckBtn');
    let timer = null;
    let remainingTime = 180; // 3 minutes in seconds
    let endTime = null;      // 타이머가 종료될 시간
    let isRunning = false;   // 타이머가 현재 실행 중인지 여부

    function updateButtonText() {
        const currentTime = Math.max(Math.floor((endTime - Date.now()) / 1000), 0);
        authButton.textContent = `${Math.floor(currentTime / 60)}:${String(currentTime % 60).padStart(2, '0')}`;
        if (currentTime <= 0) {
            clearInterval(timer);
            authButton.textContent = '인증메일발송';
            isRunning = false;
        }
    }

    function startTimer() {
        if (isRunning) return; // 이미 실행 중이면 중복 실행 방지

        endTime = Date.now() + remainingTime * 1000; // 종료 시간을 설정
        isRunning = true;

        timer = setInterval(updateButtonText, 1000); // 1초마다 타이머 업데이트

        updateButtonText(); // 즉시 한 번 업데이트
    }

    function pauseTimer() {
        if (isRunning) {
            clearInterval(timer);
            isRunning = false;
        }
    }

    authButton.addEventListener('click', handleMouseClick);

    function handleMouseClick() {
        if (!isRunning || remainingTime <= 0) { // 타이머가 멈췄거나 종료되었을 때만 재시작
            remainingTime = 180;
            startTimer();
        }

        $('#verifForm').removeClass('hidden');

        const email = $('#email').val();
        $.ajax({
            url: 'signUp/sendEmailVerifCode',
            type: 'post',
            contentType: 'application/json',
            dataType: 'json',
            data: JSON.stringify({"email": email}),
            success: function (result) {

            },
            error: function (e) {
                console.log(e)
            }
        })
    }

    authButton.addEventListener('mouseenter', handleMouseEnter);

    function handleMouseEnter() {
        $('#emailCheckBtn').css({backgroundColor: '#FA4A54', color: '#FFF'})
        if (isRunning) {
            authButton.textContent = '인증메일발송';
            pauseTimer(); // 타이머 일시 정지
        }
    }

    authButton.addEventListener('mouseleave', handleMouseLeave);

    function handleMouseLeave() {
        $('#emailCheckBtn').css({backgroundColor: '#FFF', color: '#000'})
        if (!isRunning && remainingTime > 0) {
            remainingTime = Math.max(Math.round((endTime - Date.now()) / 1000), 0); // 남은 시간 갱신
            startTimer(); // 마우스를 치우면 타이머 재개
        }
    }

    $('#authCodeCheckBtn').on('click', function () {

        const verifCode = $('#authCode').val();
        const email = $('#email').val();

        $.ajax({
            url: 'signUp/checkEmailVerifCode',
            type: 'post',
            contentType: 'application/json',
            dataType: 'json',
            data: JSON.stringify({"reqVerifCode": verifCode, "email": email}),
            success: function (result) {

                if (result === true) {
                    clearInterval(timer);
                    $('#authCode').removeClass('is-invalid')
                        .addClass('is-valid')
                        .attr('disabled', true);
                    $('.verifCode-feedback').addClass('hidden');
                    $('#authCodeCheckBtn').attr('disabled', true).text('인증 성공');
                    $('#emailCheckBtn').attr('disabled', true).text('인증 성공');
                    $('#email').attr('disabled', true);

                    document.getElementById('emailCheckBtn').removeEventListener('mouseenter', handleMouseEnter);
                    document.getElementById('emailCheckBtn').removeEventListener('mouseleave', handleMouseLeave);
                } else {
                    $('#authCode').removeClass('is-valid').addClass('is-invalid');
                    $('.verifCode-feedback').removeClass('hidden');
                }

            }
        })
    })

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
        const account = $('#userId').val();
        if (account === "") {
            alert("아이디를 입력해주세요.");
            return;
        }

        $.ajax({
            type: 'post',
            url: '/signUp/checkDupAccount',
            contentType: 'application/json',  // 요청 데이터의 MIME 타입을 지정
            dataType: 'json',  // 서버에서 반환되는 데이터 형식
            data: JSON.stringify({"account": account}),
            success: function (result) {
                if (result === true) {
                    $('.account-before').addClass('hidden');
                    $('.account-after').removeClass('hidden');
                    $('#userId').removeClass('is-valid').addClass('is-invalid');
                } else {
                    $('.account-before').addClass('hidden');
                    $('.account-after').addClass('hidden');
                    $('#userId').removeClass('is-invalid').addClass('is-valid');
                }
            },
            error: function (e) {
                console.log('failed')
                console.log(e);
            }
        });

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

    $('#email').on('input', function () {

        const emailRegex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/i;
        if (emailRegex.test($(this).val())) {
            $(this).removeClass('is-invalid');
            $('.email-feedback').addClass('hidden');
            $(this).addClass('is-valid');
            $('#emailCheckBtn').css({disable: 'true'});
        } else {
            $(this).removeClass('is-valid');
            $('.email-feedback').removeClass('hidden');
            $(this).addClass('is-invalid');
            $('#emailCheckBtn').css({disable: 'false'});
        }
    });

    $('#btn-submit').on('click', function () {

        // ID 체크
        // const account = $('#userId').val();
        // if( account !== $('#userId').data('initial-value') ){
        //
        //     if( account === "" ){
        //         alert('아이디를 입력해주세요.');
        //         $('.account-before').removeClass('hidden');
        //         $('#userId').addClass('is-invalid');
        //         return;
        //     }
        //
        //     if( !$('#userId').hasClass('is-valid') ){
        //         alert('아이디 변경 시 중복확인 필수 입니다 !');
        //         return;
        //     }
        // }

        // PWD 체크
        const password = $('#password').val();
        if( !$('#password').hasClass('hidden') ){
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
        const gender = $('#gender').val()

        // 이메일 체크
        // const email = $('#email').val()
        // if( email !== $('#email').data('initial-value') ){
        //
        //     if( $('#email').hasClass('is-invalid') || $('#authCode').hasClass('is-invalid') ){
        //         alert('이메일 인증 후 변경가능합니다.')
        //         return;
        //     }
        // }

        // 전화번호 체크
        const phone = $('#phone').val();
        if( phone === "" ){
            alert('전화번호가 유효하지 않습니다.')
            return;
        }

        // 자기소개 체크안함
        const introduce = $('#bio').text()

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