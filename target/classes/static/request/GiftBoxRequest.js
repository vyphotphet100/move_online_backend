class GiftBoxRequest {
    static openCoinGiftBox() {
        return $.ajax({
            url: connecter.baseUrlAPI + '/api/gift_box/coin_gift_box?option=open',
            type: 'GET',
            async: false,
            headers: { 'Authorization': 'Token ' + connecter.getCookie('tokenCode') },
            contentType: 'application/json',
            success: function(coinGiftBoxDto) {
                return coinGiftBoxDto;
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

    static openTimeGiftBox() {
        return $.ajax({
            url: connecter.baseUrlAPI + '/api/gift_box/time_gift_box?option=open',
            type: 'GET',
            async: false,
            headers: { 'Authorization': 'Token ' + connecter.getCookie('tokenCode') },
            contentType: 'application/json',
            success: function(timeGiftBoxDto) {
                return timeGiftBoxDto;
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