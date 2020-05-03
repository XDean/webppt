var pages = [];
var currentPage = 0;

document.addEventListener("DOMContentLoaded", function () {
    pages = document.querySelectorAll(".root > .page");

    document.getElementById("page-navigator-left").addEventListener('click', prevPage);
    document.getElementById("page-navigator-right").addEventListener('click', nextPage);

    updatePages();
});

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
}