class UserDTOtmp {
    constructor() {
        this.listRequest = [];
    }
}

class connecter {

    static baseUrlAPI = "";
    static baseUrl = 'http://localhost:80';
    static basePathAfterUrl = "";

    static authorization() {
        var userDtoTmp = new UserDTOtmp();
        userDtoTmp.listRequest.push(document.URL);
        return $.ajax({
            url: connecter.baseUrlAPI + '/authorization',
            type: 'POST',
            async: false,
            headers: { 'Authorization': 'Token ' + connecter.getCookie('tokenCode') },
            contentType: 'application/json',
            data: JSON.stringify(userDtoTmp),
            dataType: 'json',
            success: function(result) {
                return result;
            },
            error: function(error) {
                connecter.logout();
                window.location.href = connecter.basePathAfterUrl + "/Login/index.html";
            }
        }).responseJSON;

    }

    static logout() {
        $.ajax({
            url: connecter.baseUrlAPI + '/log_out',
            type: 'GET',
            async: false,
            contentType: 'application/json',
        });
        connecter.setCookie('username', null, 1);
        connecter.setCookie('tokenCode', null, 1);
    }

    static setCookie(cname, cvalue, exMinutes) {
        var d = new Date();
        d.setTime(d.getTime() + (exMinutes * 60 * 1000));
        var expires = "expires=" + d.toUTCString();
        document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
    }

    static getCookie(cname) {
        var name = cname + "=";
        var ca = document.cookie.split(';');
        for (var i = 0; i < ca.length; i++) {
            var c = ca[i];
            while (c.charAt(0) == ' ') {
                c = c.substring(1);
            }
            if (c.indexOf(name) == 0) {
                return c.substring(name.length, c.length);
            }
        }
        return "";
    }
}