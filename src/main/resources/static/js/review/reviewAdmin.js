function openReportModal(reviewId) {
    document.getElementById('reportReviewId').value = reviewId;
    $('#reportModal').modal('show');
}

function submitReport() {
    const reviewId = document.getElementById('reportReviewId').value;
    const reason = document.getElementById('reportReason').value;

    if(!reason) {
        alert('신고 사유를 입력해주세요.')
        return;
    }

    $.ajax({
        type: 'post',
        url: '/restaurant/report',
        data: {
            reviewId: reviewId,
            reason: reason
        },
        success: function (response){
            alert('신고가 접수되었습니다.');
            $('#reportModal').modal('hide');
            console.log("success", response);
        },
        error: function (error) {
            alert('신고 중 오류가 발생했습니다.');
            console.log("failed", error);
        }
    })
}