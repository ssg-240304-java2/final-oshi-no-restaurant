$(document).ready(function () {
    $(".tag-btn").click(function () {
        $(this).toggleClass("active");
    });

    $(".star-rating .fa-star").click(function () {
        var rating = $(this).data("value");
        $(".star-rating .fa-star").each(function () {
            if ($(this).data("value") <= rating) {
                $(this).addClass("checked");
            } else {
                $(this).removeClass("checked");
            }
        });
    });
});