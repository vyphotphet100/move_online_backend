// script for all page
$(function() {
    // load silde bar and top bar
    $("#accordionSidebar").load("../../common/sidebar.html");
    $("#topbar").load("../../common/topbar.html", main);
});

// script for announcement
function setActiveAtSideBar() {
    var navItems = document.getElementsByClassName('nav-item');
    for (var i = 0; i < navItems.length; i++) {
        var navItem = navItems[i];
        if (navItem.innerHTML.includes('announcement/index.html')) {
            navItem.className += ' active';
            break;
        }
    }

    $.getScript('../../common/js/common.js');
}

function getPost() {
    var urlParams = new URLSearchParams(window.location.search);
    var id = urlParams.get('id');
    var announcementDto = AnnouncementRequest.findOne(id);
    document.getElementById('announcement-title').innerText = announcementDto.title;
    document.getElementById('announcement-content').innerText = announcementDto.content;
    var date = new Date(announcementDto.modifiedDate)
    document.getElementById('modifiedDateBy').innerText = 'Đăng bởi: ' + announcementDto.modifiedBy +
        ' - ' + date.toLocaleString();
}

function main() {
    setActiveAtSideBar();
    getPost();
}