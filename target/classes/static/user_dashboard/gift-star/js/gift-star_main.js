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
        if (navItem.innerHTML.includes('gift-star/index.html')) {
            navItem.className += ' active';
            break;
        }
    }

    $.getScript('../../common/js/common.js');
}

function setUserData() {
    //var userDto = UserRequest.getCurrentUser();
    document.getElementById('numOfCoinGiftBox').innerHTML = userDto.numOfCoinGiftBox + ' hộp';
    document.getElementById('numOfTimeGiftBox').innerHTML = userDto.numOfTimeGiftBox + ' hộp';
    document.getElementById('numOfStar').innerHTML = userDto.numOfStar + ' sao';
}

function main() {
    setActiveAtSideBar();
    setUserData();
}

function exchangeTimeGiftBox() {
    var resDto = UserRequest.exchangeTimeGiftBox();
    if (resDto.httpStatus == "OK")
        alert(resDto.message);
    userDto = UserRequest.getCurrentUser();
    setUserData();
}

function exchangeCoinGiftBox() {
    var resDto = UserRequest.exchangeCoinGiftBox();
    if (resDto.httpStatus == "OK")
        alert(resDto.message);
    userDto = UserRequest.getCurrentUser();
    setUserData();
}

function exchangeShirt() {
    alert("Không đủ số sao để đổi áo phong Move Online.");
}