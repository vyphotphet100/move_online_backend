class UserRequest {
    static findAll() {
        return $.ajax({
            url: connecter.baseUrlAPI + '/api/user',
            type: 'GET',
            async: false,
            headers: { 'Authorization': 'Token ' + connecter.getCookie('tokenCode') },
            contentType: 'application/json',
            success: function(userDto) {
                return userDto;
            },
            error: function(error) {
                alert(error.responseJSON.message);
                if (error.responseJSON.message.toLowerCase().includes('access') &&
                    error.responseJSON.message.toLowerCase().includes('denied')) {
                    connecter.logout();
                    window.location.href = connecter.basePathAfterUrl + "/login/index.html";
                }
                return error;
            }
        }).responseJSON.listResult;
    }

    static findOne(username) {
        return $.ajax({
            url: connecter.baseUrlAPI + '/api/user/' + username,
            type: 'GET',
            async: false,
            headers: { 'Authorization': 'Token ' + connecter.getCookie('tokenCode') },
            contentType: 'application/json',
            success: function(userDto) {
                return userDto;
            },
            error: function(error) {
                alert(error.responseJSON.message);
                if (error.responseJSON.message.toLowerCase().includes('access') &&
                    error.responseJSON.message.toLowerCase().includes('denied')) {
                    connecter.logout();
                    window.location.href = connecter.basePathAfterUrl + "/login/index.html";
                }
                return error;
            }
        }).responseJSON;
    }

    static save(userDto) {
        userDto.authorities = null;
        return $.ajax({
            url: connecter.baseUrlAPI + '/api/user',
            type: 'POST',
            async: false,
            headers: { 'Authorization': 'Token ' + connecter.getCookie('tokenCode') },
            contentType: 'application/json',
            data: JSON.stringify(userDto),
            dataType: 'json',
            success: function(result) {
                return result;
            },
            error: function(error) {
                alert(error.responseJSON.message);
                if (error.responseJSON.message.toLowerCase().includes('access') &&
                    error.responseJSON.message.toLowerCase().includes('denied')) {
                    connecter.logout();
                    window.location.href = connecter.basePathAfterUrl + "/login/index.html";
                }
                return error;
            }
        }).responseJSON;
    }

    static update(userDto) {
        userDto.authorities = null;
        return $.ajax({
            url: connecter.baseUrlAPI + '/api/user',
            type: 'PUT',
            async: false,
            headers: { 'Authorization': 'Token ' + connecter.getCookie('tokenCode') },
            contentType: 'application/json',
            data: JSON.stringify(userDto),
            dataType: 'json',
            success: function(result) {
                return result;
            },
            error: function(error) {
                alert(error.responseJSON.message);
                if (error.responseJSON.message.toLowerCase().includes('access') &&
                    error.responseJSON.message.toLowerCase().includes('denied')) {
                    connecter.logout();
                    window.location.href = connecter.basePathAfterUrl + "/login/index.html";
                }
                return error;
            }
        }).responseJSON;
    }

    static delete(username) {
        return $.ajax({
            url: connecter.baseUrlAPI + '/api/user/' + username,
            type: 'DELETE',
            async: false,
            headers: {
                'Authorization': 'Token ' + connecter.getCookie('tokenCode')
            },
            contentType: 'application/json',
            success: function(result) {
                return result;
            },
            error: function(error) {
                alert(error.responseJSON.message);
                if (error.responseJSON.message.toLowerCase().includes('access') &&
                    error.responseJSON.message.toLowerCase().includes('denied')) {
                    connecter.logout();
                    window.location.href = connecter.basePathAfterUrl + "/login/index.html";
                }
                return error;
            }
        }).responseJSON;
    }

    static logout() {
        return $.ajax({
            url: connecter.baseUrlAPI + '/api/user/logout',
            type: 'GET',
            async: false,
            headers: { 'Authorization': 'Token ' + connecter.getCookie('tokenCode') },
            contentType: 'application/json',
            success: function(userDto) {
                return userDto;
            },
            error: function(error) {
                alert(error.responseJSON.message);
                if (error.responseJSON.message.toLowerCase().includes('access') &&
                    error.responseJSON.message.toLowerCase().includes('denied')) {
                    connecter.logout();
                    window.location.href = connecter.basePathAfterUrl + "/login/index.html";
                }
                return error;
            }
        }).responseJSON;
    }

    static getCurrentUser(args) {
        return $.ajax({
            url: connecter.baseUrlAPI + '/api/user/option/current_user',
            type: 'GET',
            async: false,
            headers: { 'Authorization': 'Token ' + connecter.getCookie('tokenCode') },
            contentType: 'application/json',
            success: function(userDto) {
                return userDto;
            },
            error: function(error) {
                if (args == null || !args.includes('alert=false;'))
                    alert(error.responseJSON.message);
                if (error.responseJSON.message.toLowerCase().includes('access') &&
                    error.responseJSON.message.toLowerCase().includes('denied')) {
                    connecter.logout();
                    window.location.href = connecter.basePathAfterUrl + "/login/index.html";
                }
                return error;
            }
        }).responseJSON;
    }

    static login(username, password) {
        var userDto = new UserDTO();
        userDto.username = username;
        userDto.password = password;
        return $.ajax({
            url: connecter.baseUrlAPI + '/login',
            type: 'POST',
            async: false,
            contentType: 'application/json',
            data: JSON.stringify(userDto),
            dataType: 'json',
            success: function(resDto) {
                connecter.setCookie('username', resDto.username, 100000);
                connecter.setCookie('tokenCode', resDto.tokenCode, 100000);
                return resDto;
                //alert(resDto.message);
            },
            error: function(error) {
                connecter.setCookie('username', null, 1);
                connecter.setCookie('tokenCode', null, 1);
                return error;
                //alert(error.responseJSON['message']);
            }
        }).responseJSON;
    }

    static logout() {
        return $.ajax({
            url: connecter.baseUrlAPI + '/log_out',
            type: 'GET',
            async: false,
            contentType: 'application/json',
            success: function(userDto) {
                connecter.setCookie('username', null, 1);
                connecter.setCookie('tokenCode', null, 1);
                return userDto;
            },
            error: function(error) {
                return error;
            }
        }).responseJSON;
    }

    static checkIn() {
        return $.ajax({
            url: connecter.baseUrlAPI + '/api/user/check_in',
            type: 'POST',
            async: false,
            headers: { 'Authorization': 'Token ' + connecter.getCookie('tokenCode') },
            contentType: 'application/json',
            success: function(result) {
                return result;
            },
            error: function(error) {
                alert(error.responseJSON.message);
                if (error.responseJSON.message.toLowerCase().includes('access') &&
                    error.responseJSON.message.toLowerCase().includes('denied')) {
                    connecter.logout();
                    window.location.href = connecter.basePathAfterUrl + "/login/index.html";
                }
                return error;
            }
        }).responseJSON;
    }

    static exchangeTimeGiftBox() {
        return $.ajax({
            url: connecter.baseUrlAPI + '/api/user/exchange_time_gift_box',
            type: 'GET',
            async: false,
            headers: { 'Authorization': 'Token ' + connecter.getCookie('tokenCode') },
            contentType: 'application/json',
            success: function(result) {
                return result;
            },
            error: function(error) {
                alert(error.responseJSON.message);
                if (error.responseJSON.message.toLowerCase().includes('access') &&
                    error.responseJSON.message.toLowerCase().includes('denied')) {
                    connecter.logout();
                    window.location.href = connecter.basePathAfterUrl + "/login/index.html";
                }
                return error;
            }
        }).responseJSON;
    }

    static exchangeCoinGiftBox() {
        return $.ajax({
            url: connecter.baseUrlAPI + '/api/user/exchange_coin_gift_box',
            type: 'GET',
            async: false,
            headers: { 'Authorization': 'Token ' + connecter.getCookie('tokenCode') },
            contentType: 'application/json',
            success: function(result) {
                return result;
            },
            error: function(error) {
                alert(error.responseJSON.message);
                if (error.responseJSON.message.toLowerCase().includes('access') &&
                    error.responseJSON.message.toLowerCase().includes('denied')) {
                    connecter.logout();
                    window.location.href = connecter.basePathAfterUrl + "/login/index.html";
                }
                return error;
            }
        }).responseJSON;
    }

    static getReferredUser() {
        return $.ajax({
            url: connecter.baseUrlAPI + '/api/user/referred_user',
            type: 'GET',
            async: false,
            headers: { 'Authorization': 'Token ' + connecter.getCookie('tokenCode') },
            contentType: 'application/json',
            success: function(userDto) {
                return userDto;
            },
            error: function(error) {
                alert(error.responseJSON.message);
                if (error.responseJSON.message.toLowerCase().includes('access') &&
                    error.responseJSON.message.toLowerCase().includes('denied')) {
                    connecter.logout();
                    window.location.href = connecter.basePathAfterUrl + "/login/index.html";
                }
                return error;
            }
        }).responseJSON.listResult;
    }

    static changePassword(username, oldPassword, newPassword) {
        var userDto = new UserDTO();
        userDto.username = username;
        userDto.listRequest.push(oldPassword);
        userDto.listRequest.push(newPassword);
        return $.ajax({
            url: connecter.baseUrlAPI + '/api/user/change_password',
            type: 'POST',
            async: false,
            headers: { 'Authorization': 'Token ' + connecter.getCookie('tokenCode') },
            contentType: 'application/json',
            data: JSON.stringify(userDto),
            dataType: 'json',
            success: function(resDto) {
                return resDto;
            },
            error: function(error) {
                alert(error.responseJSON.message);
                if (error.responseJSON.message.toLowerCase().includes('access') &&
                    error.responseJSON.message.toLowerCase().includes('denied')) {
                    connecter.logout();
                    window.location.href = connecter.basePathAfterUrl + "/login/index.html";
                }
                return error;
            }
        }).responseJSON;
    }

    static checkFacebookAccByPostLink(postLink) {
        var userDto = new UserDTO();
        userDto.listRequest.push(postLink);
        return $.ajax({
            url: connecter.baseUrlAPI + '/api/user/check_facebook',
            type: 'POST',
            async: false,
            headers: { 'Authorization': 'Token ' + connecter.getCookie('tokenCode') },
            contentType: 'application/json',
            data: JSON.stringify(userDto),
            dataType: 'json',
            success: function(resDto) {
                return resDto;
            },
            error: function(error) {
                alert(error.responseJSON.message);
                if (error.responseJSON.message.toLowerCase().includes('access') &&
                    error.responseJSON.message.toLowerCase().includes('denied')) {
                    connecter.logout();
                    window.location.href = connecter.basePathAfterUrl + "/login/index.html";
                }
                return error;
            }
        }).responseJSON;
    }

    static saveFacebookAccByPostLink(postLink, profileLink, pictureLink, facebookName) {
        var userDto = new UserDTO();
        userDto.listRequest.push(postLink);
        userDto.listRequest.push(profileLink);
        userDto.listRequest.push(pictureLink);
        userDto.listRequest.push(facebookName);
        return $.ajax({
            url: connecter.baseUrlAPI + '/api/user/save_facebook',
            type: 'POST',
            async: false,
            headers: { 'Authorization': 'Token ' + connecter.getCookie('tokenCode') },
            contentType: 'application/json',
            data: JSON.stringify(userDto),
            dataType: 'json',
            success: function(resDto) {
                return resDto;
            },
            error: function(error) {
                alert(error.responseJSON.message);
                if (error.responseJSON.message.toLowerCase().includes('access') &&
                    error.responseJSON.message.toLowerCase().includes('denied')) {
                    connecter.logout();
                    window.location.href = connecter.basePathAfterUrl + "/login/index.html";
                }
                return error;
            }
        }).responseJSON;
    }

    static saveReferrerUser(referrerCode) {
        var userDto = new UserDTO();
        userDto.listRequest.push(referrerCode);
        return $.ajax({
            url: connecter.baseUrlAPI + '/api/user/save_referrer_user',
            type: 'POST',
            async: false,
            headers: { 'Authorization': 'Token ' + connecter.getCookie('tokenCode') },
            contentType: 'application/json',
            data: JSON.stringify(userDto),
            dataType: 'json',
            success: function(resDto) {
                return resDto;
            },
            error: function(error) {
                alert(error.responseJSON.message);
                if (error.responseJSON.message.toLowerCase().includes('access') &&
                    error.responseJSON.message.toLowerCase().includes('denied')) {
                    connecter.logout();
                    window.location.href = connecter.basePathAfterUrl + "/login/index.html";
                }
                return error;
            }
        }).responseJSON;
    }

    static checkReferrerExist(username) {
        var userDto = new UserDTO();
        userDto.username = username;
        return $.ajax({
            url: connecter.baseUrlAPI + '/api/user/check_referrer_exist',
            type: 'OPTIONS',
            async: false,
            headers: { 'Authorization': 'Token ' + connecter.getCookie('tokenCode') },
            contentType: 'application/json',
            data: JSON.stringify(userDto),
            dataType: 'json',
            success: function(resDto) {
                return resDto;
            },
            error: function(error) {
                alert(error.responseJSON.message);
                if (error.responseJSON.message.toLowerCase().includes('access') &&
                    error.responseJSON.message.toLowerCase().includes('denied')) {
                    connecter.logout();
                    window.location.href = connecter.basePathAfterUrl + "/login/index.html";
                }
                return error;
            }
        }).responseJSON;
    }
}