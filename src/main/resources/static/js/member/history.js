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