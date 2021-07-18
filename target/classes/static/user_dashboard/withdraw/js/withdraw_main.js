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
        if (navItem.innerHTML.includes('withdraw/index.html')) {
            navItem.className += ' active';
            break;
        }
    }

    $.getScript('../../common/js/common.js');
}

function setUserInfo() {
    document.getElementById('numOfCoin').innerHTML = userDto.accountBalance + ' xu';
    var phoneNumber = userDto.momoPhoneNumber;
    if (phoneNumber == null) {
        alert('Bạn chưa bổ sung thông tin thanh toán. Xin mời bổ sung thông tin thanh toán.');
        location.href = "../billing-info/index.html";
        return;
    }
    var momoDto = MomoRequest.findOne(phoneNumber);
    document.getElementById('phoneNumber').value = momoDto.phoneNumber;
    document.getElementById('name').value = momoDto.name;
}

function main() {
    setActiveAtSideBar();
    setUserInfo();
}

function setNumOfRequestedCoin(e) {
    document.getElementById('numOfRequestedCoin').value = e.innerText;
}

$('.btn-do-withdraw-request').click(function() {
    var withdrawRequestDto = new WithdrawRequestDTO();
    withdrawRequestDto.username = userDto.username;
    withdrawRequestDto.type = "MOMO";
    withdrawRequestDto.status = "UNPAID";
    withdrawRequestDto.amountOfMoney = document.getElementById('numOfRequestedCoin').value.replace(' xu', '');
    withdrawRequestDto.amountOfMoney = withdrawRequestDto.amountOfMoney.replace(',', '');

    var resDto = WithdrawRequestRequest.save(withdrawRequestDto);
    if (resDto.httpStatus == "OK") {
        document.getElementById('alert-save-success').innerText = resDto.message;
        document.getElementById('alert-save-success').style.cssText = "display : block;";
        userDto = UserRequest.getCurrentUser();
        setUserInfo();
    }

});