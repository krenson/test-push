function readCookie(name) {
    var nameEQ = name + "=";
    var ca = document.cookie.split(';');
    for(var i=0;i < ca.length;i++) {
        var c = ca[i];
        while (c.charAt(0) ===' ') c = c.substring(1,c.length);
        if (c.indexOf(nameEQ) === 0) return c.substring(nameEQ.length,c.length);
    }
    return null;
}
function createCookie(nom, valeur, jours) {
    if (jours) {
        var date = new Date();
        date.setTime(date.getTime()+(jours*24*60*60*1000));
        var expire = "; expire="+date.toGMTString();
    }
    else var expire = "";
    document.cookie = nom+"="+valeur+expire+"; path=/";
}
function checkDate(dateStart, dateEnd) {
    var today = new Date();
    if (typeof dateEnd === "undefined" ) {
        return true;
    }
    if (typeof dateStart === "undefined" ) {
        return (Date.parse(today) <= dateEnd.getTime());
    } else {
        if (Date.parse(today) >= dateStart.getTime() && Date.parse(today) <= dateEnd.getTime()) {
            return true;
        } else {
            return false;
        }
    }
}
function isIE11() {
    // true on IE11, false on Edge and other IEs/browsers.
    let isIE11 = !!window.MSInputMethodContext && !!document.documentMode,
        ua = window.navigator.userAgent;

    if (ua.indexOf("AppleWebKit") > 0) {
        return false;
    } else if (ua.indexOf("Lunascape") > 0) {
        return false;
    } else if (ua.indexOf("Sleipnir") > 0) {
        return false;
    }

    array = /(msie|rv:?)\s?([\d\.]+)/.exec(ua);
    version = (array) ? array[2] : '';

    return (parseInt(version) === 11) ? true : false;
}