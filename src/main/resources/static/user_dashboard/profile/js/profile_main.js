var userDto = null;

function loadUserInfo() {
    userDto = UserRequest.getCurrentUser();
    $('#fullname-1').text(userDto.fullname);
    $('#username').text(userDto.username);
    $('#fullname-2').val(userDto.fullname);
    $('#email').val(userDto.email);
    $('#phone-number').val(userDto.phoneNumber);
    $('#address').val(userDto.address);
    $('#facebook-name').val(userDto.facebookName);
    if (userDto.picture != null && userDto.picture != "")
        $('#avatar').attr('src', userDto.picture);
    else
        $('#avatar').attr('src', '../../common/img/undraw_profile.svg');
    if (userDto.referrerUsername != null) {
        $('#referrer-code').attr('readonly', true);
        $('#referrer-code').val(userDto.referrerUsername);
        $('#confirm-referrer-code').attr('style', 'display:none;');
    }

}

function main() {
    loadUserInfo();
}
main();

// change password
$('#change-password-save').click(function() {
    // check confirm pass
    if ($('#change-password-new-password').val() != $('#change-password-new-password-confirm').val()) {
        alert('Xác nhận mật khẩu chưa khớp. Hãy kiểm tra lại.');
        return;
    }

    // check 
    if ($('#change-password-new-password').val() == "" || $('#change-password-new-password').val() == null) {
        alert("Mật khẩu mới không được phép để trống.");
        return;
    }

    var resDto = UserRequest.changePassword(userDto.username, $('#change-password-current-password').val(), $('#change-password-new-password').val())

    if (resDto.httpStatus == 'OK') {
        alert(resDto.message);
        location.reload();
    }

});

$('#save').click(function() {
    userDto.fullname = $('#fullname-2').val();
    userDto.email = $('#email').val();
    userDto.phoneNumber = $('#phone-number').val();
    userDto.address = $('#address').val();
    userDto = UserRequest.update(userDto);

    if (userDto.httpStatus == "OK") {
        $('#announcement-content').text(userDto.message);
        $("#announcement").modal("show");
    }
});

$('#ok-btn').click(function() {
    location.reload();
});

$('#change-facebook').click(function() {
    location.href = "../add-facebook/index.html";
});

$('#confirm-referrer-code').click(function() {
    if ($('#referrer-code').val().trim() == '' || $('#referrer-code').val() == null) {
        alert('Mã người giới thiệu không được bỏ trống.');
        return;
    }

    var userDto = UserRequest.saveReferrerUser($('#referrer-code').val());
    if (userDto.httpStatus == 'OK') {
        $('#confirm-referrer-code').attr('style', 'display: none;');
        $('#announcement-content').text(userDto.message);
        $("#announcement").modal("show");
    }
});