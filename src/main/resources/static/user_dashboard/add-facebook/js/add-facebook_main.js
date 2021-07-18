var userDto = null;

function loadUserInfo() {
    userDto = UserRequest.getCurrentUser();
    $('#input-step-1').val('Xác nhận tham gia Move online: ' + userDto.username);
}

function checkTime() {
    if (userDto.facebookLink != null) {
        var d = new Date();
        if (d.getDay() != 0 || d.getHours() != 8) {
            alert("Hệ thống chỉ cho phép thay đổi tài khoản Facebook làm việc vào khoảng thời gian 8AM - 9AM ngày Chủ nhật.");
            location.href = '../profile/index.html';
            return;
        }
    }

}

function main() {
    loadUserInfo();
    checkTime();
}
main();

var checkUserDto = null;
$('#check').click(function() {
    $('.step-3').attr('style', $('.step-3').attr('style').replace('display:none;', ''));

    setTimeout(function() {
        checkUserDto = UserRequest.checkFacebookAccByPostLink($('#input-step-2').val());
        if (checkUserDto.httpStatus == 'OK') {
            $('#facebook-name').text(checkUserDto.listResult[1]);
            $('#avatar').attr('src', checkUserDto.listResult[2]);
            $('#confirm').prop('disabled', false);
        }
    }, 100);
    // console.log(UserRequest.addFacebookAccByPostLink('https://www.facebook.com/vy.caodinh.52/posts/342505827254991', "https://www.facebook.com/vy.caodinh.52/", "https://scontent.fdad1-1.fna.fbcdn.net/v/t1.6435-1/cp0/e15/q65/c6.0.120.120a/p120x120/79603752_100740981431478_7119497691482030080_n.jpg?_nc_cat=104&ccb=1-3&_nc_sid=dbb9e7&_nc_ohc=u-xeFMISbIIAX8W-Up9&_nc_ht=scontent.fdad1-1.fna&tp=5&oh=85f79aedfcefbb96ad70de119cd301f7&oe=60EAD763"));
});

$('#confirm').click(function() {
    $('#loading-gif').attr('style', 'display:inline-block;');

    setTimeout(function() {
        var confirmUserDto = UserRequest.saveFacebookAccByPostLink($('#input-step-2').val(), checkUserDto.listResult[0], checkUserDto.listResult[2], checkUserDto.listResult[1]);
        if (confirmUserDto.httpStatus == 'OK') {
            $('#loading-gif').attr('style', 'display:none;');
            $('#announcement-content').text(confirmUserDto.message);
            $('#announcement').modal('show');
        }
    }, 100);
});

$('#ok-btn').click(function() {
    location.href = '../profile/index.html';
})