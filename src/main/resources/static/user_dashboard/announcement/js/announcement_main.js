// script for all page
$(function() {
    // load silde bar and top bar
    $("#accordionSidebar").load("../../common/sidebar.html");
    $("#topbar").load("../../common/topbar.html", main);
});




function loadAnnouncement() {
    var announcementCard =
        '<div class="card shadow mb-4">' +
        '<div class="card-header py-3">' +
        '<h6 class="m-0 font-weight-bold text-primary"><a href="HREF">TITLE</a></h6>' +
        '</div>' +
        '<div class="card-body">' +
        '<div class="announcement">' +
        '<p>SHORT_DESCRIPTION' +
        '</p>' +
        '<div style="text-align:right;">' +
        '<a href="HREF">Xem tin &rarr;</a>' +
        '</div>' +
        '<hr class="sidebar-divider my-0">' +
        '<br>' +
        '</div>' +
        '</div>' +
        '</div>';

    var announcementBox = document.getElementById('announcementBox');
    var announcementDtos = AnnouncementRequest.findAll();
    for (var i = announcementDtos.length - 1; i >= 0; i--) {
        var announcementCardTmp = announcementCard;
        announcementCardTmp = announcementCardTmp.replace('TITLE', announcementDtos[i].title);
        announcementCardTmp = announcementCardTmp.replace('SHORT_DESCRIPTION', announcementDtos[i].shortDescription);
        announcementCardTmp = announcementCardTmp.replace('HREF', 'post.html?id=' + announcementDtos[i].id);
        announcementCardTmp = announcementCardTmp.replace('HREF', 'post.html?id=' + announcementDtos[i].id);
        announcementBox.innerHTML += announcementCardTmp;
    }
    $('#loading-gif').attr('style', 'display: none;');
}

function main() {
    $.getScript('../../common/js/common.js');
    loadAnnouncement();
}