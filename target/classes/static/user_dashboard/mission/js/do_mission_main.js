function doMission() {
    // get mission id 
    var missionId = connecter.getCookie('missionId');
    if (missionId == null) {
        alert('Có lỗi xảy ra.');
        location.href = 'index.html';
        return;
    }

    var missionDto = MissionRequest.findOne(missionId);
    if (missionDto.httpStatus == 'OK' &&
        missionDto.type == 'CAPCHA' &&
        document.referrer != 'http://olalink.co/') {

        alert('Bạn chưa xác nhận capcha.');
        location.href = 'index.html';
        return;
    }

    missionDto = MissionRequest.doMission(missionId);
    alert(missionDto.message);
    location.href = 'index.html';
}


function main() {
    doMission();
}
main();