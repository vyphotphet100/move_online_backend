window.onload = fadeout;
var intervalID = null;

function fadeout() {
    intervalID = setInterval(hide, 100);
}

function hide() {
    var loadingDiv = document.getElementById('loading-div');
    opacity =
        Number(window.getComputedStyle(loadingDiv).getPropertyValue("opacity"))

    if (opacity > 0) {
        opacity = opacity - 0.1;
        loadingDiv.style.opacity = opacity;
    } else {
        clearInterval(intervalID);
        loadingDiv.style.cssText = 'display:none;';
    }
}