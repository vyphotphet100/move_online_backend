$(function() {

    // Toggle the side navigation
    $("#sidebarToggle, #sidebarToggleTop").on('click', function(e) {
        $("body").toggleClass("sidebar-toggled");
        $(".sidebar").toggleClass("toggled");
        if ($(".sidebar").hasClass("toggled")) {
            $('.sidebar .collapse').collapse('hide');
        };
    });

    // Close any open menu accordions when window is resized below 768px
    $(window).resize(function() {
        if ($(window).width() < 768) {
            $('.sidebar .collapse').collapse('hide');
        };

        // Toggle the side navigation when window is resized below 480px
        if ($(window).width() < 480 && !$(".sidebar").hasClass("toggled")) {
            $("body").addClass("sidebar-toggled");
            $(".sidebar").addClass("toggled");
            $('.sidebar .collapse').collapse('hide');
        };
    });

    // Prevent the content wrapper from scrolling when the fixed side navigation hovered over
    $('body.fixed-nav .sidebar').on('mousewheel DOMMouseScroll wheel', function(e) {
        if ($(window).width() > 768) {
            var e0 = e.originalEvent,
                delta = e0.wheelDelta || -e0.detail;
            this.scrollTop += (delta < 0 ? 1 : -1) * 30;
            e.preventDefault();
        }
    });

    // Scroll to top button appear
    $(document).on('scroll', function() {
        var scrollDistance = $(this).scrollTop();
        if (scrollDistance > 100) {
            $('.scroll-to-top').fadeIn();
        } else {
            $('.scroll-to-top').fadeOut();
        }
    });

    // Smooth scrolling using jQuery easing
    $(document).on('click', 'a.scroll-to-top', function(e) {
        var $anchor = $(this);
        $('html, body').stop().animate({
            scrollTop: ($($anchor.attr('href')).offset().top)
        }, 1000, 'easeInOutExpo');
        e.preventDefault();
    });
});

function messageReceived(payload) {
    sendStatus("ON_PAGE");

    var receivedMessage = JSON.parse(payload.body);
    console.log(receivedMessage.content);

    if (receivedMessage.type == 'FB_ACTIVE') {
        if (window.location.href.includes('/dashboard/index.html')) {
            $('#status').html('Bạn đang Online.');
            $('#numOfTraversedTimeIcon').html('<img src="image/Hourglass_902x.gif" style="width: 40px; height: 40px;"/>');
            $('#coinGiftBoxIcon').html('<img src="image/wait-coin-gift-box.gif" style="width: 40px; height: 40px;"/>');

        }
        connecter.setCookie('fbStatus', 'active', 2);
    } else if (receivedMessage.type == 'FB_INACTIVE') {
        if (window.location.href.includes('/dashboard/index.html')) {
            $('#status').html('Bạn đang Offline.');
            $('#numOfTraversedTimeIcon').html('<i class="fas fa-hourglass-end fa-2x text-300"></i>');
            $('#coinGiftBoxIcon').html('<i class="fas fa-gift fa-2x text-300"></i>');

        }
        connecter.setCookie('fbStatus', 'inactive', 2);
    } else if (receivedMessage.type == 'INCREASE_MINUTE') {
        if (window.location.href.includes('/dashboard/index.html')) {
            setUserData();
            $('#status').html('Bạn đang Online.');
            $('#numOfTraversedTimeIcon').html('<img src="image/Hourglass_902x.gif" style="width: 40px; height: 40px;"/>');
            $('#coinGiftBoxIcon').html('<img src="image/wait-coin-gift-box.gif" style="width: 40px; height: 40px;"/>');
            connecter.setCookie('fbStatus', 'active', 2);
        }
    } else if (receivedMessage.type == 'INCREASE_COIN_GIFT_BOX') {
        if (window.location.href.includes('/dashboard/index.html')) {
            setUserData();
            $('#status').html('Bạn đã nhận được 1 hộp quà xu.');
        }
    }


}

var serverConnected = false;

function listenFromServer() {
    // connect to server and subcribe channel
    //var socket = new SockJS(connecter.baseUrlAPI + '/ws');
    var url = 'ws://moveonline.ddns.net:8083/ws';
    stompClient = Stomp.client(url);
    //stompClient.connect({}, onConnected, onError);
    stompClient.connect({}, function() {
        stompClient.subscribe('/channel/' + userDto.username, messageReceived);
        serverConnected = true;

    }, function() {
        alert("Có lỗi xảy ra. Hãy tải lại trang.");
        //listenFromServer();
    });
}
listenFromServer();

function sendStatus(status) {
    var messageSocketDto = {
        receiver: "server",
        type: status,
        content: JSON.stringify({
            username: userDto.username
        })
    }
    stompClient.send("/send-fb-status", {}, JSON.stringify(messageSocketDto));
}

function logout() {
    connecter.setCookie('username', null, 1);
    connecter.setCookie('tokenCode', null, 1);
    connecter.setCookie('fbStatus', null, 1);
    var userDto = UserRequest.logout();
    if (userDto.httpStatus == "OK")
        location.href = '../login/index.html';
}