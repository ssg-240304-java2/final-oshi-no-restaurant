function filterData() {
    const filter = document.getElementById('selectMenu').value;
    const items = document.querySelectorAll('.reservation-item');
    items.forEach(item => {
        if (filter === '전체 내역' || item.getAttribute('data-type') === filter) {
            item.style.display = 'block';
        } else {
            item.style.display = 'none';
        }
    });
}
document.addEventListener('DOMContentLoaded', function() {
    filterData();  // Initialize with the default value
});

$('.btn-outline-danger').on('click', function () {

    console.log($(this).data('restaurantno'));
    console.log($(this).data('servicetype'));
    console.log($(this).data('serviceno'));

    const restaurantNo = $(this).data('restaurantno');
    const serviceType = $(this).data('servicetype');
    const serviceNo = $(this).data('serviceno');
    window.location.href = '/reviewPage?restaurantNo='+restaurantNo+'&serviceType='+serviceType+'&serviceNo='+serviceNo;
});