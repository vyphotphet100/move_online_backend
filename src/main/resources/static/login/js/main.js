(function($) {
    "use strict";


    /*==================================================================
    [ Focus input ]*/
    $('.input100').each(function() {
        $(this).on('blur', function() {
            if ($(this).val().trim() != "") {
                $(this).addClass('has-val');
            } else {
                $(this).removeClass('has-val');
            }
        })
    })


    /*==================================================================
    [ Validate ]*/
    var input = $('.validate-input .input100');

    $('.validate-form').on('submit', function() {
        var check = true;

        for (var i = 0; i < input.length; i++) {
            if (validate(input[i]) == false) {
                showValidate(input[i]);
                check = false;
            }
        }

        return check;
    });


    $('.validate-form .input100').each(function() {
        $(this).focus(function() {
            hideValidate(this);
        });
    });

    function validate(input) {
        if ($(input).attr('type') == 'email' || $(input).attr('name') == 'email') {
            if ($(input).val().trim().match(/^([a-zA-Z0-9_\-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([a-zA-Z0-9\-]+\.)+))([a-zA-Z]{1,5}|[0-9]{1,3})(\]?)$/) == null) {
                return false;
            }
        } else {
            if ($(input).val().trim() == '') {
                return false;
            }
        }
    }

    function showValidate(input) {
        var thisAlert = $(input).parent();

        $(thisAlert).addClass('alert-validate');
    }

    function hideValidate(input) {
        var thisAlert = $(input).parent();

        $(thisAlert).removeClass('alert-validate');
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
    checkAutoLogin();

    $('.login100-form-btn').click(function() {
        document.getElementsByClassName('login100-form-btn')[0].style.cssText = 'display:none;';
        document.getElementsByClassName('loading')[0].style.cssText = 'display:block;';

        setTimeout(function() {
            var userDto = UserRequest.login($('#username').val(), $('#password').val());

            if (userDto.httpStatus != 'OK') {
                alert(userDto.message);
                document.getElementsByClassName('loading')[0].style.cssText = 'display:none;';
                document.getElementsByClassName('login100-form-btn')[0].style.cssText = 'display:block;';
            }
            if (userDto.httpStatus == 'OK') {
                if (userDto.roleCodes.includes('USER'))
                    window.location.href = connecter.basePathAfterUrl + '/user_dashboard/dashboard/index.html';
                else if (userDto.roleCodes.includes('ADMIN'))
                    window.location.href = connecter.basePathAfterUrl + '/admin_dashboard/dashboard/index.html';
            }
        }, 10);

    });

})(jQuery);