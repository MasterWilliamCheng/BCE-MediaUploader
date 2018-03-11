<%--
  Created by IntelliJ IDEA.
  User: snh007
  Date: 2015/9/11
  Time: 18:37
  To change this template use File | Settings | File Templates.

上传组件使用方法:
    <button type="button" class="btn btn-success" id="uploadButton">上传</button>

    $("#uploadButton").click(function(){
      UPLOAD_PATH="/mediasource/album/";
      showUploadModal("CoverImage");
    });

    $uploadSaveBtn.click(function(){
      switch ($pathsType.val()){
        case "CoverImage":
          alert($paths.val());
          break;
      }

    });
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!--引入CSS-->
<link rel="stylesheet" type="text/css"
      href="${pageContext.request.contextPath}/resources/plugins/webuploader/webuploader.css">
<!--引入JS-->
<script type="text/javascript"
        src="${pageContext.request.contextPath}/resources/plugins/webuploader/webuploader.js"></script>
<!-- Modal -->
<div class="modal" id="uploadModal">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">×</span></button>
                <h4 class="modal-title">文件上传</h4>
            </div>

            <div class="modal-body">
                <div id="uploader" class="wu-example">
                    <!--用来存放文件信息-->
                    <div id="uploadFileList" class="uploader-list"></div>
                    <div class="btns">
                        <div id="picker">选择文件</div>
                        <button id="uploadBtn" class="btn btn-default">开始上传</button>
                    </div>
                    <input type="hidden" id="pathsType" value="">
                    <input type="hidden" id="paths" value="">
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-info pull-left" data-dismiss="modal" id="uploadSaveBtn">保存
                </button>
            </div>

        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>
<script>
    var state = 'pending',//状态
            uploadFileSize= 0,//上传文件大小
            $btn = $("#uploadBtn"),//上传按钮
            uploader,//上传组件
            $paths = $("#paths"),//文件路径列表
            $uploadSaveBtn = $("#uploadSaveBtn"),
            $pathsType = $("#pathsType"),//上传文件类型
            $list = $("#uploadFileList");//文件列表
    $(function () {
        uploader = WebUploader.create({
            // 文件接收服务端。
            server: '${pageContext.request.contextPath}/console/upload/uploadFile',

            // 选择文件的按钮。可选。
            // 内部根据当前运行是创建，可能是input元素，也可能是flash.
            pick: '#picker',

            // 不压缩image, 默认如果是jpeg，文件上传前会压缩一把再上传！
            resize: false,

            formData: {
                path: UPLOAD_PATH
            }
        });
        // 当有文件被添加进队列的时候
        uploader.on('fileQueued', function (file) {
            $list.append('<div id="' + file.id + '" class="item">' +
            '<h4 class="info">' + file.name + '</h4>' +
            '<p class="state">等待上传...</p>' +
            '</div>');
        });

        // 文件上传过程中创建进度条实时显示。
        uploader.on('uploadProgress', function (file, percentage) {
            var $li = $('#' + file.id),
                    $percent = $li.find('.progress .progress-bar');

            // 避免重复创建
            if (!$percent.length) {
                $percent = $('<div class="progress progress-striped active">' +
                '<div class="progress-bar" role="progressbar" style="width: 0%">' +
                '</div>' +
                '</div>').appendTo($li).find('.progress-bar');
            }

            $li.find('p.state').text('上传中');

            $percent.css('width', percentage * 100 + '%');
        });

        uploader.on('uploadSuccess', function (file, response) {
            if (response.path == '') {
                $('#' + file.id).find('p.state').text('上传出错');
            } else {
                $('#' + file.id).find('p.state').text('已上传');
                var pathsValue = $paths.val();
                if (pathsValue == "") {
                    $paths.val(response.path);
                } else {
                    $paths.val(pathsValue + "," + response.path);
                }
                uploadFileSize=response.size;

            }

        });

        uploader.on('uploadError', function (file) {
            $('#' + file.id).find('p.state').text('上传出错');
        });

        uploader.on('uploadComplete', function (file) {
            $('#' + file.id).find('.progress').fadeOut();
        });

        uploader.on('all', function (type) {
            if (type === 'startUpload') {
                state = 'uploading';
            } else if (type === 'stopUpload') {
                state = 'paused';
            } else if (type === 'uploadFinished') {
                state = 'done';
            }

            if (state === 'uploading') {
                $btn.text('暂停上传');
            } else {
                $btn.text('开始上传');
            }
        });

        $btn.click(function () {
            if (state === 'uploading') {
                uploader.stop();
            } else {
                uploader.upload();
            }
        });

    });

    /**
     *初始化路径标签
     */
    function emptyPaths() {
        $paths.val("");
        $pathsType.val("");
    }

    /**
     *显示Modal
     */
    function showUploadModal(type) {
        emptyPaths();
        $list.html("");
        $('#uploadModal').modal("show");
        $pathsType.val(type);
    }
</script>