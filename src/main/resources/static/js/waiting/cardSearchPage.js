$(document).ready(function() {
    // Toggle action buttons on clicking the plus button
    $('.toggle-actions').click(function() {
        $(this).closest('.card').toggleClass('show-actions');
    });
});