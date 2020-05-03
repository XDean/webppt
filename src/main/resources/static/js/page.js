let pages = [];
let currentPage = 0;

document.addEventListener("DOMContentLoaded", function () {
    pages = document.querySelectorAll(".root > .page");
    currentPage = (parseInt(location.hash.substr(1)) || 2) - 1;

    document.addEventListener('keydown', handleBodyKeyDown);

    document.getElementById("page-navigator-left").addEventListener('click', prevPage);
    document.getElementById("page-navigator-right").addEventListener('click', nextPage);

    updatePages();
});

function handleBodyKeyDown(event) {
    switch (event.keyCode) {
        case 37:// left arrow
        case 8: // backspace
        case 33:// PgDn
        case 38:// up arrow
            prevPage();
            event.preventDefault();
            break;
        case 39:// right arrow
        case 32:// space
        case 34:// PgDn
        case 40:// down arrow
            nextPage();
            event.preventDefault();
            break;
    }
}

function prevPage() {
    if (currentPage > 0) {
        currentPage--;
        updatePages();
    }
}

function nextPage() {
    if (currentPage < pages.length - 1) {
        currentPage++;
        updatePages();
    }
}

function updatePages() {
    pages.forEach(function (page, index) {
        page.classList.remove("prev", "current", "next", "far-prev", "far-next");
        if (index === currentPage) {
            page.classList.add("current");
        } else if (index === currentPage - 1) {
            page.classList.add("prev");
        } else if (index === currentPage + 1) {
            page.classList.add("next");
        } else if (index < currentPage - 1) {
            page.classList.add("far-prev");
        } else if (index > currentPage + 1) {
            page.classList.add("far-next")
        }
    });
    location.replace('#' + (currentPage + 1));
}