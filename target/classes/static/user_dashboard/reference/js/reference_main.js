// script for all page
$(function() {
    // load silde bar and top bar
    $("#accordionSidebar").load("../../common/sidebar.html");
    $("#topbar").load("../../common/topbar.html", main);
});

// script for dashboard
function setActiveAtSideBar() {
    var navItems = document.getElementsByClassName('nav-item');
    for (var i = 0; i < navItems.length; i++) {
        var navItem = navItems[i];
        if (navItem.innerHTML.includes('reference/index.html')) {
            navItem.className += ' active';
            break;
        }
    }

    $.getScript('../../common/js/common.js');
}

function setUserInfo() {
    document.getElementById('referenceCode').value = userDto.username;
    document.getElementById('referenceLink').value = "http://localhost/move_online/login/register.html?referrer=" + userDto.username;
}

function getReferredUser() {
    var rowStr =
        '<tr>' +
        '    <th scope="row">ORDER</th>' +
        '    <td>USERNAME</td>' +
        '    <td>COMMISSION</td>' +
        '</tr>';
    var referredUsers = UserRequest.getReferredUser();
    if (referredUsers != null) {
        for (var i = 0; i < referredUsers.length; i++) {
            var rowStrTmp = rowStr;
            rowStrTmp = rowStrTmp.replace('ORDER', i + 1);
            rowStrTmp = rowStrTmp.replace('USERNAME', referredUsers[i].username);
            rowStrTmp = rowStrTmp.replace('COMMISSION', referredUsers[i].commission);
            document.getElementsByClassName('table body')[0].innerHTML += rowStrTmp;
        }
    }
}

function main() {
    setActiveAtSideBar();
    setUserInfo();
    getReferredUser();
}