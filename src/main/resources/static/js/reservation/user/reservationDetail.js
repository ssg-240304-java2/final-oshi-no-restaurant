document.addEventListener("DOMContentLoaded", function () {
    document.querySelectorAll('.rating-details .detail').forEach(function (detail) {
        const countText = detail.querySelector('.count').textContent;
        const nCount = parseInt(countText);
        const line = detail.querySelector('.line');
        console.log(`Setting width for count ${nCount}`);

        const maxWidth = 300;
        const widthPercentage = nCount / 30;
        const calculatedWidth = maxWidth * widthPercentage;
        line.style.width = `${calculatedWidth}px`;
    });
});

document.getElementById('arrow-button').addEventListener('click', function () {
    currentIndexes = currentIndexes.map(index => (index + 1) % images.length);
    currentIndexes.forEach((index, i) => {
        document.getElementById(`card-${i}`).querySelector('.img-placeholder').style.backgroundImage = `url(${images[index]})`;
    });
});