// script for all page
$(function() {
    $("#topbar").load("../../common/topbar.html", autorun);
});

function autorun() {
    $.getScript('../../common/js/common.js', checkStartWork);
}

function checkStartWork() {
    var url = new URL(document.URL);
    var id = url.searchParams.get("id");
    var tokenCode = connecter.getCookie('tokenCode');

    if (id.trim() != '123456' || // CONFIRM
        //!document.referrer.includes('facebook.com') ||
        tokenCode == null ||
        userDto.facebookLink == null || userDto.facebookLink == '') {
        alert('Xác nhận bắt đầu làm việc thất bại.');
        location.href = '../dashboard/index.html';
        return;
    }

    var interval = setInterval(function() {
        if (serverConnected) {
            sendStatus('FB_ACTIVE');
            alert('Xác nhận bắt đầu làm việc thành công.');
            clearInterval(interval);
            window.close();
        }

    }, 100);

}