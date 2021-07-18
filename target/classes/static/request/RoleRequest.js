class RoleRequest {
    static findAll() {
        return $.ajax({
            url: connecter.baseUrlAPI + '/api/role',
            type: 'GET',
            async: false,
            headers: { 'Authorization': 'Token ' + connecter.getCookie('tokenCode') },
            contentType: 'application/json',
            success: function(roleDto) {
                return roleDto;
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

    static findOne(code) {
        return $.ajax({
            url: connecter.baseUrlAPI + '/api/role/' + code,
            type: 'GET',
            async: false,
            headers: { 'Authorization': 'Token ' + connecter.getCookie('tokenCode') },
            contentType: 'application/json',
            success: function(roleDto) {
                return roleDto;
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

    static save(roleDto) {
        return $.ajax({
            url: connecter.baseUrlAPI + '/api/role',
            type: 'POST',
            async: false,
            headers: { 'Authorization': 'Token ' + connecter.getCookie('tokenCode') },
            contentType: 'application/json',
            data: JSON.stringify(roleDto),
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

    static update(roleDto) {
        return $.ajax({
            url: connecter.baseUrlAPI + '/api/role',
            type: 'PUT',
            async: false,
            headers: { 'Authorization': 'Token ' + connecter.getCookie('tokenCode') },
            contentType: 'application/json',
            data: JSON.stringify(roleDto),
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

    static delete(code) {
        return $.ajax({
            url: connecter.baseUrlAPI + '/api/role/' + code,
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

}