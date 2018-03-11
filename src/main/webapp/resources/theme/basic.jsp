<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="th" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html>
<!--
This is a starter template page. Use this page to start your new project from
scratch. This page gets rid of all links and provides the needed markup only.
-->
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no"
          name="viewport">
    <title>内部媒体资源管理平台${pageContext.request.contextPath}</title>

    <c:set var="CONTEXT_PATH" value="${pageContext.request.contextPath}" scope="page"/>
    <c:set var="USER_NAME" value="${pageContext.request.session.getAttribute('dataUser').staffname}" scope="page"/>
    <%--<c:set var="USER_HEADIMGURL" value="${pageContext.request.session.getAttribute('dataUser').headimgurl}" scope="page"/>--%>
    <c:set var="USER_GROUPID" value="${pageContext.request.session.getAttribute('dataUser').groupId}" scope="page"/>

    <link rel="stylesheet" href="${CONTEXT_PATH}/resources/css/bootstrap.min.css"/>
    <link rel="stylesheet"
          href="${CONTEXT_PATH}/resources/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${CONTEXT_PATH}/resources/css/ionicons.min.css"/>
    <link rel="stylesheet" href="${CONTEXT_PATH}/resources/css/AdminLTE.min.css"/>
    <link rel="stylesheet" href="${CONTEXT_PATH}/resources/css/skins/skin-blue.min.css"/>
    <link rel="stylesheet"
          href="${CONTEXT_PATH}/resources/plugins/datatables/dataTables.bootstrap.css"/>
    <link rel="stylesheet"
          href="${CONTEXT_PATH}/resources/plugins/datatables/buttons.dataTables.min.css"/>
    <link rel="stylesheet"
          href="${CONTEXT_PATH}/resources/plugins/daterangepicker/daterangepicker-bs3.css"/>
    <link rel="stylesheet" href="${CONTEXT_PATH}/resources/plugins/iCheck/all.css"/>
    <link rel="stylesheet"
          href="${CONTEXT_PATH}/resources/plugins/colorpicker/bootstrap-colorpicker.min.css"/>
    <link rel="stylesheet"
          href="${CONTEXT_PATH}/resources/plugins/timepicker/bootstrap-timepicker.min.css"/>
    <link rel="stylesheet" href="${CONTEXT_PATH}/resources/plugins/select2/select2.min.css"/>
    <link rel="stylesheet"
          href="${CONTEXT_PATH}/resources/plugins/jQuery-confirm/jquery-confirm.min.css"/>
    <link rel="stylesheet" href="${CONTEXT_PATH}/resources/css/snh/common.css"/>

    <script src="${CONTEXT_PATH}/resources/plugins/vue/vue.js"></script>
    <script src="${CONTEXT_PATH}/resources/plugins/jQuery/jQuery-2.1.4.min.js"></script>
    <script src="${CONTEXT_PATH}/resources/js/bootstrap.js"></script>
    <script src="${CONTEXT_PATH}/resources/js/app.min.js"></script>
    <script src="${CONTEXT_PATH}/resources/plugins/datatables/jquery.dataTables.js"></script>
    <script src="${CONTEXT_PATH}/resources/plugins/datatables/dataTables.bootstrap.js"></script>
    <script src="${CONTEXT_PATH}/resources/plugins/datatables/dataTables.buttons.min.js"></script>
    <script src="${CONTEXT_PATH}/resources/plugins/slimScroll/jquery.slimscroll.min.js"></script>
    <script src="${CONTEXT_PATH}/resources/plugins/fastclick/fastclick.min.js"></script>
    <script src="${CONTEXT_PATH}/resources/js/moment.min.js"></script>
    <script src="${CONTEXT_PATH}/resources/plugins/daterangepicker/daterangepicker.js"></script>
    <script src="${CONTEXT_PATH}/resources/plugins/colorpicker/bootstrap-colorpicker.min.js"></script>
    <script src="${CONTEXT_PATH}/resources/plugins/timepicker/bootstrap-timepicker.min.js"></script>
    <script src="${CONTEXT_PATH}/resources/plugins/select2/select2.full.min.js"></script>
    <script src="${CONTEXT_PATH}/resources/plugins/My97DatePicker/WdatePicker.js"></script>
    <script src="${CONTEXT_PATH}/resources/js/jquery.bootstrap.min.js"></script>
    <script src="${CONTEXT_PATH}/resources/plugins/input-mask/jquery.inputmask.js"></script>
    <script src="${CONTEXT_PATH}/resources/plugins/input-mask/jquery.inputmask.date.extensions.js"></script>
    <script src="${CONTEXT_PATH}/resources/plugins/input-mask/jquery.inputmask.extensions.js"></script>
    <script src="${CONTEXT_PATH}/resources/plugins/template/template-web.js"></script>
    <script src="${CONTEXT_PATH}/resources/plugins/bootbox.js"></script>
    <script src="${CONTEXT_PATH}/resources/plugins/jQuery-confirm/jquery-confirm.min.js"></script>
    <script src="${CONTEXT_PATH}/resources/js/snh/common.js"></script>



    <sitemesh:write property='head'/>


    <!--[if lt IE 9]>
    <script type="text/javascript" src="${CONTEXT_PATH}/resources/js/html5shiv.min.js"></script>
    <script type="text/javascript" src="${CONTEXT_PATH}/resources/js/respond.min.js"></script>
    <![endif]-->

</head>

<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">
    <header class="main-header">



        <nav class="navbar navbar-static-top" role="navigation">
            <a href="#" class="sidebar-toggle" data-toggle="offcanvas" role="button">
                <span class="sr-only">Toggle navigation</span>
            </a>

            <div class="navbar-custom-menu">
                <ul class="nav navbar-nav">
                    <li class="dropdown user user-menu">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                            <%--<img src="${USER_HEADIMGURL}" class="user-image">--%>
                            <span class="hidden-xs">欢迎回来，【${USER_GROUPID}】${USER_NAME}</span>
                        </a>
                        <ul class="dropdown-menu">
                            <li class="user-footer">
                                <div class="pull-right">
                                    <form action="${CONTEXT_PATH}/logout" method="POST">
                                        <sec:csrfInput/>
                                        <button type="submit" class="btn btn-default btn-flat">退出
                                        </button>
                                    </form>
                                </div>
                            </li>
                        </ul>
                    </li>

                </ul>
            </div>
        </nav>
    </header>

    <aside class="main-sidebar">

        <section class="sidebar">
            <c:import url="/menu"/>
        </section>
    </aside>

    <sitemesh:write property='body'/>

    <footer class="main-footer">
        <div class="pull-right hidden-xs"></div>
        <strong>Copyright &copy; 2015
            <a href="http://www.snh48.com">SNH48</a>.
        </strong>
        All rights reserved.
    </footer>

    <div class="control-sidebar-bg"></div>
</div>

</body>
<script>
    function loadingEvent() {
        var dialog = bootbox.dialog({
            size: 'small',
            message: '<p class="text-center">操作中，请稍后...</p>',
            closeButton: false
        });
//        dialog.modal('hide');
    }
    function search(type){
        var owner = $("#searchOwner").val();
        var name = $("#searchName").val();
        var lKeyCode = (navigator.appname=="Netscape")?event.which:event.keyCode;
        if ( lKeyCode == 13 ){
            if(type == 1){
                window.location.href="/media/console/video_list?searchName="+name+"&searchOwner="+owner;
            }else if(type == 3){
                window.location.href="/media/console/pic_list?searchName="+name+"&searchOwner="+owner;
            }else{
                return false;
            }
        }
        else{
            return false;
        }
    }
</script>
</html>
