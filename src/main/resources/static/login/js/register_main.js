function checkReferrerCode() {
    var url = new URL(location.href);
    var referrerCode = url.searchParams.get("referrer");

    if (referrerCode != null && referrerCode.trim() != '') {
        $('#referrer-code').val(referrerCode);
        $('#referrer-part').attr('style', 'display:none;');
    }
}

function checkAutoLogin() {
    if (connecter.getCookie('tokenCode') != 'null' &&
        connecter.getCookie('tokenCode') != null &&
        connecter.getCookie('tokenCode') != '' &&
        connecter.getCookie('username') != 'null' &&
        connecter.getCookie('username') != null &&
        connecter.getCookie('username') != '') {
        var args = ['alert=true;'];
        var userDto = UserRequest.getCurrentUser(args);
        if (userDto != null && userDto.username == connecter.getCookie('username')) {
            location.href = '../user_dashboard/dashboard/index.html';
        }
    }
}

function main() {
    checkReferrerCode();
    checkAutoLogin();
}
main();

$('#register-btn').click(function() {
    $('#register-btn').attr('style', 'display:none;');
    $('#loading-gif').attr('style', 'display:block;');

    setTimeout(function() {
        if ($('#password').val().trim() != $('#re-password').val().trim()) {
            alert("Xác nhận mật khẩu chưa chính xác.");
            $('#register-btn').attr('style', 'display:block;');
            $('#loading-gif').attr('style', 'display:none;');
            return;
        }

        if ($('#username').val().trim() == '' ||
            $('#password').val().trim() == '' ||
            $('#re-password').val().trim() == '' ||
            $('#fullname').val().trim() == '') {
            alert('Vui lòng nhập đầy đủ thông tin.');
            $('#register-btn').attr('style', 'display:block;');
            $('#loading-gif').attr('style', 'display:none;');
            return;
        }

        var userDto = new UserDTO();
        userDto.username = $('#username').val().trim();
        userDto.password = $('#password').val().trim();
        userDto.fullname = $('#fullname').val().trim();
        userDto.referrerUsername = $('#referrer-code').val().trim();
        userDto = UserRequest.save(userDto);

        if (userDto.httpStatus == 'OK') {
            alert(userDto.message);
            location.href = 'index.html';
        } else {
            $('#register-btn').attr('style', 'display:block;');
            $('#loading-gif').attr('style', 'display:none;');
        }
    }, 100);




});