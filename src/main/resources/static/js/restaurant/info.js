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

// 식당 정보 관련 메서드
function handleRestaurantInfo(actionUrl) {
    const storeName = $("#storeName").val();
    const storePhone = $("#storePhone").val();
    const businessAddress = $("#sample6_postcode").val() + $("#sample6_address").val() + $("#sample6_detailAddress").val() + $("#sample6_extraAddress").val();
    const postCode = $("#sample6_postcode").val();
    const address = $("#sample6_address").val();
    const detailAddress = $("#sample6_detailAddress").val();
    const extraAddress = $("#sample6_extraAddress").val();
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
    const storeImage = $("#storeImage")[0].files[0];

    const formData = new FormData();

    const newRestaurant = {
        "restaurantName": storeName,
        "contact": storePhone,
        "restaurantAddress": businessAddress,
        "postCode": postCode,
        "address": address,
        "detailAddress": detailAddress,
        "extraAddress": extraAddress,
        "foodTypes": foodType,
        "openingTime": openingHoursStart,
        "closingTime": openingHoursEnd,
        "hashTags": tagType,
        "description": storeIntro
    };

    const updateInfo = {
        "restaurantName": storeName,
        "contact": storePhone,
        "restaurantAddress": businessAddress,
        "postCode": postCode,
        "address": address,
        "detailAddress": detailAddress,
        "extraAddress": extraAddress,
        "foodTypes": foodType,
        "openingTime": openingHoursStart,
        "closingTime": openingHoursEnd,
        "hashTags": tagType,
        "description": storeIntro
    };

    formData.append('file', storeImage);
    formData.append('newRestaurant', new Blob([JSON.stringify(newRestaurant)], {type: 'application/json'}));
    formData.append('updateInfo', new Blob([JSON.stringify(updateInfo)], {type: 'application/json'}));

    console.log(storeName, foodType, tagType);
    console.log(formData);

    $.ajax({
        type:'post',
        url: actionUrl,
        contentType: false,
        processData: false,
        data: formData,
        success: function (result) {
            console.log("success", result);
            window.location.href = '/restaurant/main';
        },
        error: function (e) {
            console.log("failed", e)
        }
    })

}

// 데이트픽
$(document).ready(function () {
    if ($('#reservationDate').length) {
        $("#reservationDate").datepicker({
            dateFormat: "yy-mm-dd"
        });
    }
    if ($('#waitingDate').length) {
        $("#waitingDate").datepicker({
            dateFormat: "yy-mm-dd"
        });
    }
});

// 식당 사진 미리보기
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

// 회원가입 시 식당 정보 등록
$('#infoRegisterBtn').on('click', function () {
    handleRestaurantInfo('/restaurant/infoRegister');
});

// 식당 정보 수정
$('#updateBtn').on('click', function () {
    handleRestaurantInfo('/restaurant/infoUpdate');
});

// 예약 정보 등록
$('#reservationBtn').on('click', function () {
    const reservationDate = $('#reservationDate').val();
    const reservationTime = $('#reservationTime').val();
    const reservationPeople = $('#reservationPeople').val();

    console.log(reservationDate, reservationTime, reservationPeople)

    $.ajax({
        type: 'post',
        url: '/restaurant/reservationSetting',
        contentType: 'application/json',
        data: JSON.stringify({
            "reservationDate": reservationDate,
            "reservationTime": reservationTime,
            "reservationPeople": reservationPeople
        }),
        success: function (reservation) {
            alert("등록되었습니다.");
            console.log("success", reservation);

            const listItem = `
                <li class="list-group-item d-flex justify-content-between align-items-center">
                    <span>${reservation.reservationTime} - ${reservation.reservationPeople}명</span>
                    <button type="button" class="btn btn-danger btn-sm reservationRmvBtn" data-id="${reservation.reservationNo}">삭제</button>
                </li>`;
            $('#reservationList').append(listItem);
            $('#reservationList').css({"display": "block"});
        },
        error: function (e) {
            alert("등록에 실패했습니다.");
            console.log(e, "failed");
        }
    })
})

// 예약 정보 조회
$('#reservationDate').on('change', function () {
    const selectedDate = $(this).val();
    const reservationList = $('#reservationList')

    if (!selectedDate) {
        reservationList.hide();
        reservationList.empty();
        return;
    }

    $.ajax({
        type: 'GET',
        url: '/restaurant/reservationSetting/' + selectedDate,
        contentType: 'application/json',
        success: function (reservations) {
            console.log("success", reservations);

            reservationList.empty();

            if (reservations.length > 0) {
                reservations.forEach(function (reservSettings) {
                    const listItem = `
                    <li class="list-group-item d-flex justify-content-between align-items-center">
                        <span>${reservSettings.reservationTime} - ${reservSettings.reservationPeople}명</span>
                        <button type="button" class="btn btn-danger btn-sm reservationRmvBtn" data-id="${reservSettings.reservationNo}">삭제</button>
                    </li>`;
                    reservationList.append(listItem);
                });
                reservationList.show();
            } else {
                reservationList.hide();
            }
        },
        error: function (e) {
            console.log("failed", e);
            reservationList.hide();
        }
    });
});

// 예약 정보 삭제
$(document).on('click', '.reservationRmvBtn', function () {
    const reservationNo = $(this).data('id');
    const selectedDate = $('#reservationDate').val();

    console.log(reservationNo);

    if (confirm('정말 삭제하시겠습니까?')) {
        $.ajax({
            type: 'DELETE',
            url: '/restaurant/deleteReservationSetting/' + reservationNo,
            success: function (response) {
                console.log(response)
                alert('삭제되었습니다.');

                $.ajax({
                    type: 'GET',
                    url: '/restaurant/reservationSetting/' + selectedDate,
                    contentType: 'application/json',
                    success: function (reservations) {
                        console.log("success", reservations);

                        const reservationList = $('#reservationList');
                        reservationList.empty();

                        if (reservations.length > 0) {
                            reservations.forEach(function (reservSettings) {
                                const listItem = `
                                <li class="list-group-item d-flex justify-content-between align-items-center">
                                    <span>${reservSettings.reservationTime} - ${reservSettings.reservationPeople}명</span>
                                    <button type="button" class="btn btn-danger btn-sm reservationRmvBtn" data-id="${reservSettings.reservationNo}">삭제</button>
                                </li>`;
                                reservationList.append(listItem);
                            });
                            reservationList.show();
                        } else {
                            reservationList.hide();
                        }
                    },
                    error: function (e) {
                        console.log("failed", e);
                        $('#reservationList').hide();
                    }
                });
            },
            error: function (error) {
                console.log(error);
                alert('삭제에 실패했습니다.');
            }
        });
    }
});

// 웨이팅 메서드
function handleWaitingSetting(actionUrl) {
    const waitingDate = $('#waitingDate').val();
    const waitingStartTime = $('#waitingStartTime').val();
    const waitingEndTime = $('#waitingEndTime').val();
    const waitingPeople = $('#waitingPeople').val();
    const isWaitingOn = $('#waitingToggle').prop('checked');

    console.log(waitingDate, waitingStartTime, waitingEndTime, waitingPeople, isWaitingOn);

    if (isWaitingOn && waitingDate) {
        $.ajax({
            type: 'post',
            url: actionUrl,
            contentType: 'application/json',
            data: JSON.stringify({
                "waitingDate": waitingDate,
                "startTime": waitingStartTime,
                "endTime": waitingEndTime,
                "waitingPeople": waitingPeople,
                "waitingStatus": isWaitingOn ? "Y" : "N"
            }),
            success: function (response) {
                alert("등록되었습니다.");
                console.log('success', response);
            },
            error: function (error) {
                console.log('error', error);
                alert("등록에 실패했습니다.");
            }
        });
    } else if (!isWaitingOn && waitingDate) {
        // off일 때 삭제
        $.ajax({
            type: 'DELETE',
            url: '/restaurant/deleteWaitingSetting/' + waitingDate,
            success: function (response) {
                alert("삭제되었습니다.");
                console.log('deleted', response);

                $('#waitingStartTime').val('00:00');
                $('#waitingEndTime').val('00:00');
                $('#waitingPeople').val('');
                $('#waitingToggle').prop('checked', false);
            },
            error: function (error) {
                console.log('error', error);
                alert("삭제에 실패했습니다.");
            }
        });
    } else {
        alert("웨이팅 정보 등록을 위해 날짜를 선택하고 ON으로 설정해주세요.")
    }
}

// 날짜에 따른 웨이팅 정보 조회
$(document).ready(function () {
    $('#waitingDate').on('change', function () {
        let dateText = $('#waitingDate').val();

        $.ajax({
            url: '/restaurant/waitingSetting/' + dateText,
            method: 'GET',
            success: function (data) {
                if (data) {
                    $('#waitingStartTime').val(data.startTime);
                    $('#waitingEndTime').val(data.endTime);
                    $('#waitingPeople').val(data.waitingPeople);
                    $('#waitingToggle').prop('checked', data.waitingStatus);
                } else {
                    $('#waitingStartTime').val('00:00');
                    $('#waitingEndTime').val('00:00');
                    $('#waitingPeople').val('');
                    $('#waitingToggle').prop('checked', false);
                }
            },
            error: function (error) {
                console.log('Error fetching waiting setting: ', error);
                $('#waitingStartTime').val('00:00');
                $('#waitingEndTime').val('00:00');
                $('#waitingPeople').val('');
                $('#waitingToggle').prop('checked', false);
            }
        });
    });
    // 웨이팅 정보 등록 및 수정
    $('#waitingSaveBtn').on('click', function () {
        const actionUrl = $(this).data('action-url') || '/restaurant/waitingSetting';
        handleWaitingSetting(actionUrl);
    });
});





