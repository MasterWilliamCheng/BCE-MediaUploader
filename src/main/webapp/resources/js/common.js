void function (c) {
    c.initMenu = function () {
        var url = location.href;
        var pattern = /\/console\/[A-Za-z]+/;
        if (url.indexOf('count') > 0) {
            pattern = /\/console\/([A-Za-z]+\/)+/;
        }
        var re = new RegExp(pattern);
        var arr = url.match(re);
        $('.menu-element').each(function () {
            url = $(this).attr('href');
            if (url.indexOf(arr[0]) > 0) {
                $(this).attr('style', 'color:#d9534f');
                var element = $(this).parent().parent().parent();
                element.addClass('open');
                element.children('ul').slideDown(200);
                element.siblings('li').children('ul').slideUp(200);
                element.siblings('li').removeClass('open');
                element.siblings('li').find('li').removeClass('open');
                element.siblings('li').find('ul').slideUp(200);
            }
        });
    };

    //dataTables汉化
    c.oLanguage = {
        "sLengthMenu": "每页显示 _MENU_ 条记录",
        "sZeroRecords": "没有检索到数据",
        "sInfo": "第 _START_ 到第 _END_ 条数据；总共有 _TOTAL_ 条记录",
        "sInfoEmtpy": "没有数据",
        "sProcessing": "正在加载数据...",
        "oPaginate": {
            "sFirst": "首页",
            "sPrevious": "前页",
            "sNext": "后页",
            "sLast": "尾页"
        }
    };
    c.language = {
        "lengthMenu": "每页显示 _MENU_ 条记录",
        "zeroRecords": "没有检索到数据",
        "info": "第 _START_ 到第 _END_ 条数据；总共有 _TOTAL_ 条记录",
        "infoEmpty": "没有数据",
        "processing": "正在加载数据...",
        "paginate": {
            "sFirst": "首页",
            "sPrevious": "<span class='glyphicon glyphicon-menu-left'></span>",
            "sNext": "<span class='glyphicon glyphicon-menu-right'></span>",
            "sLast": "尾页"
        },
        "search": "搜索 "
    };

    c.dateLocale = {
        format: 'YYYY-MM-DD',
        separator: ' - ',
        applyLabel: '确认',
        cancelLabel: '取消',
        weekLabel: '周',
        customRangeLabel: '自定义范围'
    };

    c.formatDate = function (str) {
        var date = new Date(str);
        var year = date.getFullYear();
        var month = c.addZero(date.getMonth() + 1);
        var day = c.addZero(date.getDate());
        var hours = c.addZero(date.getHours());
        var minutes = c.addZero(date.getMinutes());
        var seconds = c.addZero(date.getSeconds());
        return year + "-" + month + "-" + day + " " + hours + ":" + minutes + ":" + seconds;
    };

    c.formatLocaleDatetime = function (date) {
        var year = date.getFullYear();
        var month = c.addZero(date.getMonth() + 1);
        var day = c.addZero(date.getDate());
        var hours = c.addZero(date.getHours());
        var minutes = c.addZero(date.getMinutes());
        var seconds = c.addZero(date.getSeconds());
        return year + "-" + month + "-" + day + "T" + hours + ":" + minutes + ":" + seconds;
    };

    c.formatYYYYMM = function (date) {
        var year = date.getFullYear();
        var month = c.addZero(date.getMonth() + 1);
        return year + "-" + month;
    };

    c.formatYYYYMMDD = function (date) {
        var year = date.getFullYear();
        var month = c.addZero(date.getMonth() + 1);
        var day = c.addZero(date.getDate());
        return year + "-" + month + "-" + day;
    };

    c.formatYYYYMMDD_zhCN = function (date) {
        var year = date.getFullYear();
        var month = c.addZero(date.getMonth() + 1);
        var day = c.addZero(date.getDate());
        return year + "年" + month + "月" + day + '日';
    };


    c.addZero = function (num) {
        num = Number(num) || 0;
        return num < 10 ? '0' + num : num;
    };


    c.createPage = function (divId, page, size, pages, href, search) {
        page = Number(page);
        pages = Number(pages);
        if (search != null && search.trim().length > 0) {
            search = '&search=' + encodeURIComponent(search);
        } else {
            search = '';
        }
        var arr = [];
        if (page > 3) {
            arr.push(1);
            arr.push('...');
        }
        for (var i = page - 2; i < page + 3; i++) {
            if (i > 0 && i <= pages) {
                arr.push(i);
            }
        }
        if (pages - page > 2) {
            arr.push('...');
            arr.push(pages);
        }

        var pre = page > 1 ? page - 1 : 1;
        var html = '<nav><ul class="pagination">' +
            '<li><a href="' + href + '?page=' + pre + '&size=' + size + search + '" aria-label="Previous"><span aria-hidden="true">&laquo;</span> </a></li>';

        arr.forEach(function (p) {
            if (p == page) {
                html += '<li class="active"><a href="' + href + '?page=' + p + '&size=' + size + search + '">' + p + '</a></li>';
            } else if (p == '...') {
                html += '<li><a>' + '...' + '</a></li>';
            } else {
                html += '<li><a href="' + href + '?page=' + p + '&size=' + size + search + '">' + p + '</a></li>';
            }
        });
        var next = page < pages ? page + 1 : pages;
        html += '<li> <a href="' + href + '?page=' + next + '&size=' + size + search + '" aria-label="Next"> <span aria-hidden="true">&raquo;</span> </a> </li> </ul></nav>';
        $('#' + divId).html(html);
    };

    c.getUrlParam = function (name) {
        return decodeURIComponent((new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(location.search) || [, ""])[1].replace(/\+/g, '%20')) || null;
    };

    c.getCookie = function (key) {
        var strCookies = document.cookie;
        var arr = strCookies.split(';')
        for (var i = 0; i < arr.length; i++) {
            var name = arr[i].split('=')[0];
            var value = arr[i].split('=')[1];
            if (name == key)
                return value;
        }
        return null;
    };

    // Returns a random integer between min (included) and max (excluded)
    // Using Math.round() will give you a non-uniform distribution!
    c.getRandomInt = function (min, max) {
        return Math.floor(Math.random() * (max - min)) + min;
    };

    c.randomColor = function () {
        var arr = ['success', 'warning', 'danger', 'primary', 'info'];
        return arr[c.getRandomInt(0, 4)];
    };

    c.copyTextToClipboard = function (text) {
        var textArea = document.createElement("textarea");
        // Place in top-left corner of screen regardless of scroll position.
        textArea.style.position = 'fixed';
        textArea.style.top = 0;
        textArea.style.left = 0;
        // Ensure it has a small width and height. Setting to 1px / 1em
        // doesn't work as this gives a negative w/h on some browsers.
        textArea.style.width = '2em';
        textArea.style.height = '2em';
        // We don't need padding, reducing the size if it does flash render.
        textArea.style.padding = 0;
        // Clean up any borders.
        textArea.style.border = 'none';
        textArea.style.outline = 'none';
        textArea.style.boxShadow = 'none';
        // Avoid flash of white box if rendered for any reason.
        textArea.style.background = 'transparent';
        textArea.value = text;
        document.body.appendChild(textArea);
        textArea.select();
        try {
            var successful = document.execCommand('copy');
            var msg = successful ? 'successful' : 'unsuccessful';
            console.log('Copying text command was ' + msg);
            bootbox.alert("已成功复制到剪切板");
        } catch (err) {
            console.log('Oops, unable to copy');
            window.prompt("复制到剪切板:请按Ctrl+C, Enter", text);
        }
        document.body.removeChild(textArea);
    };

    c.showLoading = function () {
        $('#loading').show();
    };

    c.hideLoading = function () {
        $('#loading').hide();
    };

    c.upload = function (fileId, type, callback) {
        var file = $('#' + fileId)[0].files[0];
        if (file.size == 0) {
            return;
        }
        var url = $('#commonUploadUrl').val();
        var form = new FormData();
        form.append("media", file);
        form.append("type", type);
        common.showLoading();
        $.ajax({
            "crossDomain": true,
            "url": url,
            "method": "POST",
            "processData": false,
            "contentType": false,
            "mimeType": "multipart/form-data",
            "data": form,
            success: function (response) {
                common.hideLoading();
                callback(JSON.parse(response));
            }, error: function (err) {
                common.hideLoading();
                bootbox.alert("未知错误");
            }
        });
    };

    c.checkImageUrl = function (url) {
        return (url.match(/\.(jpeg|jpg|gif|png)$/) != null);
    };

    c.checkVideoUrl = function (url) {
        return (url.match(/\.(mp4)$/) != null);
    };

    c.isUrl = function (url) {
        var expression = /https?:\/\/(www\.)?[-a-zA-Z0-9@:%._\+~#=]{2,256}\.[a-z]{2,6}\b([-a-zA-Z0-9@:%_\+.~#?&//=]*)/g;
        var regex = new RegExp(expression);
        if (url.match(regex)) {
            return true;
        } else {
            return false;
        }
    };

    c.replaceUrlParam = function (url, paramName, paramValue) {
        var pattern = new RegExp('\\b(' + paramName + '=).*?(&|$)')
        if (url.search(pattern) >= 0) {
            return url.replace(pattern, '$1' + paramValue + '$2');
        }
        return url + (url.indexOf('?') > 0 ? '&' : '?') + paramName + '=' + paramValue
    }
}(window.common = {});
