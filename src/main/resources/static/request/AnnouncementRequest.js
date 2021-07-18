class AnnouncementRequest {
    static findAll() {
        return $.ajax({
            url: connecter.baseUrlAPI + '/api/announcement',
            type: 'GET',
            async: false,
            headers: { 'Authorization': 'Token ' + connecter.getCookie('tokenCode') },
            contentType: 'application/json',
            success: function(announcementDto) {
                return announcementDto;
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

    static findOne(id) {
        return $.ajax({
            url: connecter.baseUrlAPI + '/api/announcement/' + id,
            type: 'GET',
            async: false,
            headers: { 'Authorization': 'Token ' + connecter.getCookie('tokenCode') },
            contentType: 'application/json',
            success: function(announcementDto) {
                return announcementDto;
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

    static save(announcementDto) {
        return $.ajax({
            url: connecter.baseUrlAPI + '/api/announcement',
            type: 'POST',
            async: false,
            headers: { 'Authorization': 'Token ' + connecter.getCookie('tokenCode') },
            contentType: 'application/json',
            data: JSON.stringify(announcementDto),
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

    static update(announcementDto) {
        return $.ajax({
            url: connecter.baseUrlAPI + '/api/announcement',
            type: 'PUT',
            async: false,
            headers: { 'Authorization': 'Token ' + connecter.getCookie('tokenCode') },
            contentType: 'application/json',
            data: JSON.stringify(announcementDto),
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

    static delete(id) {
        return $.ajax({
            url: connecter.baseUrlAPI + '/api/announcement/' + id,
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