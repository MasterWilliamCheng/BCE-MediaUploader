var IS_DEBUG = true;

function recommend(videoId, type) {
    var path = $("#serverBaseUrl").val();
    $.ajax({
        url: path + "/console/recommend/save",
        data: {
            targetId: videoId,
            typeEnum: type
        },
        type: "post",
        dataType: "text",
        success: function (data) {
            if (data == 1) {
                bootbox.alert("成功", function () {
                    window.location.reload();
                });
            } else {
                bootbox.alert("出现错误");
            }
        },
        error: function () {
            bootbox.alert("出现错误");
        }
    })
}

function Trim(str) {
    return str.replace(/(^\s*)|(\s*$)/g, "");
}

function formatDateTime(date) {
    var year = 1900 + date.getYear();
    var month = date.getMonth() + 1;
    if (month < 10) month = "0" + month;
    var day = date.getDate();
    if (day < 10)day = "0" + day;
    var hour = date.getHours();
    if (hour < 10) hour = "0" + hour;
    var minute = date.getMinutes();
    if (minute < 10)minute = "0" + minute;
    var second = date.getSeconds();
    if (second < 10)second = "0" + second;
    return year + '-' + month + '-' + day + ' ' + hour + ':' + minute + ':' + second;
}

function formatDate(date) {
    var year = 1900 + date.getYear();
    var month = date.getMonth() + 1;
    if (month < 10) month = "0" + month;
    var day = date.getDate();
    if (day < 10)day = "0" + day;
    return year + '-' + month + '-' + day;
}
