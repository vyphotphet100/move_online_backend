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
        if (navItem.innerHTML.includes('billing-info/index.html')) {
            navItem.className += ' active';
            break;
        }
    }

    $.getScript('../../common/js/common.js');
}

function setUserInfo() {
    var phoneNumber = userDto.momoPhoneNumber;
    if (phoneNumber != null) {
        var momoDto = MomoRequest.findOne(phoneNumber);
        document.getElementById('phoneNumber').value = momoDto.phoneNumber;
        document.getElementById('name').value = momoDto.name;
    }
    $('#loading-gif').attr('style', 'display:none;');
}

function main() {
    setActiveAtSideBar();
    setUserInfo();
}

$('.btn-momo-info-form').click(function(e) {
    document.getElementById("username").value = userDto.username;
    e.preventDefault();
    var data = {};
    var formData = $('.momo-info-form').serializeArray();
    $.each(formData, function(i, v) {
        data["" + v.name + ""] = v.value;
    });

    if (data['name'].trim() == '' || data['phoneNumber'].trim() == '') {
        alert('Chưa nhập đủ thông tin. Mời kiểm tra lại.');
        return;
    }

    var momoDto = MomoRequest.save(data);
    if (momoDto.httpStatus == "OK") {
        document.getElementById('alert-save-success').innerText = momoDto.message;
        document.getElementById('alert-save-success').style.cssText = "display : block;";
    }

});