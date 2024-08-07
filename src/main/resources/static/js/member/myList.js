function toggleBadge(element) {
    if (element.classList.contains('badge-public')) {
        element.classList.remove('badge-public');
        element.classList.add('badge-private');
        element.textContent = '비공개';
    } else {
        element.classList.remove('badge-private');
        element.classList.add('badge-public');
        element.textContent = '공개';
    }
}