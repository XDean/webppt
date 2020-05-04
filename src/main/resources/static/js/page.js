let pages = [];
let currentPage = 0;

let root;
let toolBar;

let outline = false;
let outlineButton;

let lock = true;
let lockButton;

document.addEventListener("DOMContentLoaded", function () {
    pages = Array.from(document.querySelectorAll(".root > .page"));
    currentPage = (parseInt(location.hash.substr(1)) || 2) - 1;

    pages.forEach(page => page.addEventListener("click", e => onPageClick(page)));

    document.addEventListener('keydown', handleBodyKeyDown);

    document.getElementById("page-navigator-left").addEventListener('click', prevPage);
    document.getElementById("page-navigator-right").addEventListener('click', nextPage);

    root = document.getElementById("root");
    toolBar = document.getElementById("page-tool-bar");

    outlineButton = document.getElementById("page-outline-button");
    outlineButton.addEventListener("click", switchOutline);

    lockButton = document.getElementById("page-lock-button");
    lockButton.addEventListener("click", switchLock);

    updatePages();
    updateToolBars();
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

function onPageClick(page) {
    if (outline) {
        outline = false;
        currentPage = pages.indexOf(page);
        updatePages();
        updateToolBars();
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
        if (!outline) {
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
        }
    });
    toggleClass(root, outline, "outline");
    location.replace('#' + (currentPage + 1));
}

function updateToolBars() {
    toggleClass(outlineButton, outline, "active");
    toggleClass(lockButton, lock, "active");
    toggleClass(toolBar, lock, "lock");
}

function switchOutline() {
    outline = !outline;
    updatePages();
    updateToolBars();
}

function switchLock() {
    lock = !lock;
    updatePages();
    updateToolBars();
}

function toggleClass(element, condition, className) {
    element.classList.remove(className);
    if (condition) {
        element.classList.add(className);
    }
}