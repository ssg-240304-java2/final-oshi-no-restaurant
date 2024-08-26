document.getElementById('userIdCheckBtn').addEventListener('click', function () {
    this.classList.add('clicked');
});
document.getElementById('nicknameCheckBtn').addEventListener('click', function () {
    this.classList.add('clicked');
});
document.getElementById('emailCheckBtn').addEventListener('click', function () {
    this.classList.add('clicked');
});
document.getElementById('authCodeCheckBtn').addEventListener('click', function () {
    this.classList.add('clicked');
});

$('#userIdCheckBtn').on('click', function () {
    const account = $("#userId").val();

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

$('#password').on('input', function () {
    const passwordRegex = /^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*?_]).{8,16}$/;

    if (passwordRegex.test($(this).val())) {
        $(this).removeClass('is-invalid');
        $(this).next().addClass('hidden');
        $(this).addClass('is-valid');
    } else {
        $(this).removeClass('is-valid');
        $(this).next().removeClass('hidden');
        $(this).addClass('is-invalid');
    }
});

$('#confirmPassword').on('input', function () {

    if ($(this).val() === $('#password').val()) {
        $(this).removeClass('is-invalid');
        $(this).next().addClass('hidden');
        $(this).addClass('is-valid');
    } else {
        $(this).removeClass('is-valid');
        $(this).next().removeClass('hidden');
        $(this).addClass('is-invalid');
    }
});

$('#nicknameCheckBtn').on('click', function () {

    const nickname = $('#nickname').val();

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

$('#phone').on('input', function () {

    const phoneRegex = /^(01[016789]{1})[0-9]{3,4}[0-9]{4}$/;
    if (phoneRegex.test($(this).val())) {
        $(this).removeClass('is-invalid');
        $(this).next().addClass('hidden');
        $(this).addClass('is-valid');
    } else {
        $(this).removeClass('is-valid');
        $(this).next().removeClass('hidden');
        $(this).addClass('is-invalid');
    }
});

document.addEventListener("DOMContentLoaded", function () {
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
            url: '/signUp/sendEmailVerifCode',
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
            url: '/signUp/checkEmailVerifCode',
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
});

$('#signUpBtn').on('click', function () {

    if (
        // 유효값 체크
        !$('#userId').hasClass('is-valid') ||
        !$('#password').hasClass('is-valid') ||
        !$('#confirmPassword').hasClass('is-valid') ||
        !$('#phone').hasClass('is-valid') ||
        !$('#nickname').hasClass('is-valid') ||
        !$('#email').hasClass('is-valid') ||
        !$('#authCode').hasClass('is-valid') ||
        // null값 체크
        $('#userId').val() == "" ||
        $('#password').val() == "" ||
        $('#confirmPassword').val() == "" ||
        $('#phone').val() == "" ||
        $('#fullName').val() == "" ||
        $('#birthDate').val() == "" ||
        $("input:radio[name='gender']:checked").val() == "" ||
        $('#nickname').val() == "" ||
        $('#email').val() == "" ||
        $('#authCode').val() == ""
    ) {
        alert("모든 정보를 입력하고 검증받아야 회원가입 가능합니다.");
    } else {
        const account = $('#userId').val();
        const password = $('#password').val();
        const name = $('#fullName').val();
        const phone = $('#phone').val();
        const birthday = $('#birthDate').val();
        const gender = $("input:radio[name='gender']:checked").val();
        const nickname = $('#nickname').val();
        const email = $('#email').val();

        $.ajax({
            type: 'post',
            url: '/signUp',
            contentType: 'application/json',
            dataType: 'text',
            data: JSON.stringify({
                "account": account,
                "password": password,
                "name": name,
                "phone": phone,
                "birthday": birthday,
                "gender": gender,
                "nickname": nickname,
                "email": email
            }),
            success: function (result) {
                window.location.href = result;
            },
            error: function (e) {
                console.log("error");
                console.log(e);
            }
        })
    }
});


