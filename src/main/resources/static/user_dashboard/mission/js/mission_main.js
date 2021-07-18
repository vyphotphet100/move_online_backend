// script for all page
$(function() {
    // load silde bar and top bar
    $("#accordionSidebar").load("../../common/sidebar.html");
    $("#topbar").load("../../common/topbar.html", main);
});

// script for dashboard
function setActiveAtSideBar() {
    var navItems = document.getElementsByClassName('nav-item');
    for (var i = 0; i < navItems.length; i++) {
        var navItem = navItems[i];
        if (navItem.innerHTML.includes('mission/index.html')) {
            navItem.className += ' active';
            break;
        }
    }

    $.getScript('../../common/js/common.js');
}


function loadMission() {
    var missionCard =
        '<div class="card shadow mb-4">' +
        '<div class="card-header py-3">' +
        '    <h6 class="m-0 font-weight-bold text-primary"><a href="HREF">NAME</a></h6>' +
        '</div>' +
        '<div class="card-body">' +
        '    <div class="mission">' +
        '        <p class="mission description">SHORT_DESCRIPTION' +
        '        </p>' +
        '        <div style="text-align:right;">' +
        '            <p>' +
        '                <div style="display:STARDISPLAY;">' +
        '                    <strong>NUMOFSTAR</strong>' +
        '                    <span style="color: rgb(251, 255, 30);">' +
        '                        <i class="fas fa-fw fa-star"></i>' +
        '                    </span>' +
        '                </div>' +

        '               <div style="display:COINGIFTBOXDISPLAY;">' +
        '                    <strong>NUMOFCOINGIFTBOX</strong>' +
        '                    <span style="color: rgb(255, 0, 0);">' +
        '                        <i class="fas fa-fw fa-gift"></i>' +
        '                    </span>' +
        '                </div>' +

        '                <div style="display:TIMEGIFTBOXDISPLAY;">' +
        '                    <strong>NUMOFTIMEGIFTBOX</strong>' +
        '                    <span style="color: rgb(3, 219, 32);">' +
        '                        <i class="fas fa-fw fa-business-time"></i>' +
        '                    </span>' +
        '                </div>' +
        '            </p>' +
        '        </div>' +
        '        <div style="text-align:right;">' +
        '            <a class="mission href" href="HREF">Thực hiện &rarr;</a>' +
        '        </div>' +
        '    </div>' +
        '</div>' +
        '</div>';

    var missionBox = document.getElementById('missionBox');
    var missionDtos = MissionRequest.findAll();

    for (var i = 0; i < missionDtos.length; i++) {
        if (userDto.missionIds.includes(missionDtos[i].id))
            continue;

        var missionCardTmp = missionCard;
        missionCardTmp = missionCardTmp.replace('NAME', missionDtos[i].name);
        missionCardTmp = missionCardTmp.replace('SHORT_DESCRIPTION', missionDtos[i].shortDescription);

        // show star
        if (missionDtos[i].numOfStar != 0) {
            missionCardTmp = missionCardTmp.replace('STARDISPLAY', 'inline');
            missionCardTmp = missionCardTmp.replace('NUMOFSTAR', missionDtos[i].numOfStar);
        } else
            missionCardTmp = missionCardTmp.replace('STARDISPLAY', 'none');

        // show coin gift box
        if (missionDtos[i].numOfCoinGiftBox != 0) {
            missionCardTmp = missionCardTmp.replace('COINGIFTBOXDISPLAY', 'inline');
            missionCardTmp = missionCardTmp.replace('NUMOFCOINGIFTBOX', missionDtos[i].numOfCoinGiftBox);
        } else
            missionCardTmp = missionCardTmp.replace('COINGIFTBOXDISPLAY', 'none');

        // show time gift box
        if (missionDtos[i].numOfTimeGiftBox != 0) {
            missionCardTmp = missionCardTmp.replace('TIMEGIFTBOXDISPLAY', 'inline');
            missionCardTmp = missionCardTmp.replace('NUMOFTIMEGIFTBOX', missionDtos[i].numOfTimeGiftBox);
        } else
            missionCardTmp = missionCardTmp.replace('TIMEGIFTBOXDISPLAY', 'none');

        missionCardTmp = missionCardTmp.replace('HREF', 'post.html?id=' + missionDtos[i].id);
        missionCardTmp = missionCardTmp.replace('HREF', 'post.html?id=' + missionDtos[i].id);

        if (missionDtos[i].type == 'DIEMDANH') {
            missionCardTmp = missionCardTmp.replace('HREF', 'post-check-in.html?id=' + missionDtos[i].id);
            missionCardTmp = missionCardTmp.replace('HREF', 'post-check-in.html?id=' + missionDtos[i].id);
        } else if (missionDtos[i].type == 'CAPCHA') {
            missionCardTmp = missionCardTmp.replace('HREF', 'post-capcha.html?id=' + missionDtos[i].id);
            missionCardTmp = missionCardTmp.replace('HREF', 'post-capcha.html?id=' + missionDtos[i].id);
        }

        missionBox.innerHTML += missionCardTmp;
    }
}


function loadDoneMission() {
    var doneMissionCard =
        '<p class="done_mission">NAME</p>' +
        '<hr class="sidebar-divider my-0">';

    var doneMissionBox = document.getElementById('doneMissionBox');
    for (var i = 0; i < userDto.missionIds.length; i++) {
        var doneMissionCardTmp = doneMissionCard;
        doneMissionCardTmp = doneMissionCardTmp.replace('NAME',
            MissionRequest.findOne(userDto.missionIds[i]).name);
        doneMissionBox.innerHTML += doneMissionCardTmp;
    }
}

function main() {
    setActiveAtSideBar();
    loadMission();
    loadDoneMission();
}