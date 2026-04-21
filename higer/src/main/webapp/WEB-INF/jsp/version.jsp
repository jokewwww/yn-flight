<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>APK管理</title>
    <link href="/static/css/bootstrap.css" rel="stylesheet">
    <link href="/static/css/style.css" rel="stylesheet">
    <link href="/static/css/font-awesome.css" rel="stylesheet">
    <script src="/static/js/jquery.min.js"></script>
    <script src="/static/js/_mp.js"></script>
    <script src="/static/js/bootstrap.js"></script>
    <script src="/static/js/jquery-html5Validate.js"></script>
</head>
<script type="application/javascript">
    var url = '/version/getAll';
    var success = {
        "message": "success", "delmessage": "删除成功！"
    };
    var warning = {
        "warn": "请选择一条信息！"
    };
    $(function () {
        pageInit();
        getPrintNum();
        getFlowThreshold();
        //添加或者修改
        $("#materialSubmit").click(function () {
            var isPass = $.html5Validate.isAllpass(document.getElementById('form1'));
            if (isPass) {
                $.ajax({
                    url: '/version/save',
                    type: "post",
                    data: $('#form1').serialize(),
                    success: function (data) {
                        if (data == "success") {
                            alert("成功");
                            $('#model').modal('hide');
                            _mp.clearFormData("form1");
                            goToPage(1);
                        } else if (data == "false") {
                            alert("版本号重复请重新输入")
                        }
                    },
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                        alert(errorThrown);
                    }
                });
            }
        });
        //修改回填
        $("#editMaterial").click(function () {
            initForm();
        });
        //删除
        $("#delMaterial").click(function () {
            var edit = $("input[name='checkbox']:checked");
            if (edit.length >= 1) {
                $('#delcfmModel').modal();
                return;
            }
            alert("请选择需要删除的记录！");
        });
    });

    function pageInit() {
        goToPage(1);
    }

    function goToPage(page) {
        var condition = "";
        _mp.getPageData("test", url, page, condition, pageCallBack);
    }

    function pageCallBack(datas) {
        $('#test').find('tbody').empty();
        var trHtml = "";
        if (!datas || datas.length === 0) {
            trHtml += "<tr align='center'><td colspan='100'>没有相关数据</td></tr>";
        } else {
            for (var i = 0; i < datas.length; i++) {
                _mp.cache.put(datas[i].id, datas[i]);
                let apkType = "工控机apk";
                if (datas[i].type != 0) {
                    apkType = "手持PADapk";
                }
                if (datas[i].type == 20) {
                    apkType = "工控机2.0apk";
                }
                trHtml += "<tr>";
                trHtml += "<td><input type='checkbox' name='checkbox' id='checkbox' value=" + datas[i].id + "></td>";
                trHtml += "<td><A href='javascript:void(0)' onclick='showFile(" + datas[i].id + ")'>APK上传</A></td>";
                trHtml += "<td>" + apkType + "</td>";
                trHtml += "<td>" + datas[i].ver + "</td>";
                trHtml += "<td>" + datas[i].remark + "</td>";
                let apkUrl = datas[i].apkUrl,
                    showData = '未上传APK';
                if (apkUrl != null && apkUrl != undefined && apkUrl != "") {
                    showData = "已上传APK";
                }
                trHtml += "<td>" + showData + "</td>";
                trHtml += "</tr>";
            }
        }
        $('#test').find('tbody').append(trHtml);
    }

    function initForm() {
        var edit = $("input[name='checkbox']:checked");
        if (edit.length == 1) {
            _mp.clearFormData("form1");
            var value = _mp.cache.get($(edit).val());
            _mp.bindFormData("form1", value);
            $("#materialCatName").val($("#materialCatId option:checked").text());
            $('#model').modal('show');
            return;
        }
        alert(warning.warn);
    }

    function deleteBatch() {
        var edit = $("input[name='checkbox']:checked");
        var id = new Array();
        for (var i = 0; i < edit.length; i++) {
            id[i] = $(edit[i]).val();
        }
        $.ajax({
            type: "post",
            url: 'system/resources/delete',
            data: {id: id},
            success: function (data) {
                if (data == "success") {
                    alert("成功");
                    $('#delcfmModel').modal('hide');
                    _mp.clearFormData("form1");
                    goToPage(1);
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                alert(errorThrown);
            }
        });
    }

    function showFile(id) {
        var iWidth = 1000;
        var iHeight = 350;
        var iTop = (window.screen.availHeight - 30 - iHeight) / 2;
        var iLeft = (window.screen.availWidth - 10 - iWidth) / 2;
        var win = window
            .open(
                "basefile?id=" + id,
                "弹出窗口",
                "width="
                + iWidth
                + ", height="
                + iHeight
                + ",top="
                + iTop
                + ",left="
                + iLeft
                + ",toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no, status=no,alwaysRaised=yes,depended=yes");
    }


    //---------------------------------------------打印速度配置---------------------------------------------------------------------


    function getPrintNum() {
        $.ajax({
            url: '/version/getPrintNum',
            type: "get",
            success: function (data) {
                // alert(data);

                $('#printNum').val(data);
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                alert(errorThrown);
            }
        });
    }


    function getFlowThreshold() {
        $.ajax({
            url: '/version/getFlowThreshold',
            type: "get",
            success: function (data) {
                // alert(data);

                $('#flowThreshold').val(data);
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                alert(errorThrown);
            }
        });
    }


    function updFlowThreshold() {
        $.ajax({
            type: "post",
            url: 'version/updFlowThreshold',
            data: {flowThreshold: $('#flowThreshold').val()},
            success: function (data) {
                if (data == "success") {
                    alert("成功");
                    $('#delcfmModel').modal('hide');
                    _mp.clearFormData("form1");
                    goToPage(1);
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                alert(errorThrown);
            }
        });
    }

    function updPrintNum() {
        $.ajax({
            type: "post",
            url: 'version/updPrintNum',
            data: {num: $('#printNum').val()},
            success: function (data) {
                if (data == "success") {
                    alert("成功");
                    $('#delcfmModel').modal('hide');
                    _mp.clearFormData("form1");
                    goToPage(1);
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                alert(errorThrown);
            }
        });
    }
</script>
<body>
<div class="mainbar">
    <div class="page-head">
        <h2 class="pull-left">
            APK管理
        </h2>
        <div class="clearfix"></div>
        <div class="adm_sergc">
            <div class="admserch">
            </div>
            <div class="admzsgc">
                <form>
                    <input id="addMaterial" name="" class="admzsgc_btn" type="button" value="上传APK"
                           data-toggle="modal"
                           data-target="#model"/>
                    <%-- <input id="editMaterial" name="" class="admzsgc_btn" type="button" value="修改" data-toggle="modal"/>--%>
                </form>

            </div>
            打印速度： <input type="text" id="printNum"> <input onclick="updPrintNum()" type="button" value="修改"/>&nbsp&nbsp
            开始加油的流速阈值： <input type="text" id="flowThreshold"> <input onclick="updFlowThreshold()" type="button"
                                                                              value="修改"/>
        </div>
        <div class="matter">
            <div class="container">
                <div class="row">
                    <div class="col-md-12">
                        <div class="widget">
                            <div class="widget-head">
                                <div class="pull-left">表格</div>
                                <div class="widget-icons pull-right">
                                    <a href="#" class="wminimize"><i class="icon-chevron-up"></i></a>
                                    <a href="#" class="wclose"><i class="icon-remove"></i></a>
                                </div>
                                <div class="clearfix"></div>
                            </div>
                            <div class="widget-content">
                                <div class="padd" id="test">
                                    <div id="curve-chart">
                                        <table class="table table-striped">
                                            <thead>
                                            <tr>
                                                <th><label>
                                                    <input id="selectAll" name="btn1"
                                                           class="admlist_qx" type="checkbox"/>
                                                </label></th>
                                                <th>操作按钮</th>
                                                <th>apk类型</th>
                                                <th>版本号</th>
                                                <th>备注</th>
                                                <th>状态</th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- 模态框（Modal） -->

<div class="modal fade" id="model" data-backdrop="static" tabindex="-1" role="dialog"
     aria-labelledby="myModalLabel" aria-hidden="true" align="center">
    <div class="modal-dialog">
        <div class="modal-content" style="width:562px;text-align: left;">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel" align="center">添加资源</h4>
            </div>
            <form class="i-form" id="form1">
                <input type="text" style="display: none" id="id" name="id"/>
                <div class="modal-body">
                    <table align="center">
                        <tr>
                            <td>apk类型:</td>
                            <td>
                                <select class="form-control" name="type">
                                    <option value="0">工控机apk</option>
                                    <option value="1">手持PADapk</option>
                                    <option value="20">工控机2.0apk</option>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td>版本号:</td>
                            <td>
                                <input type="text" name="ver"
                                       style="width: 250px;" required/>
                            </td>
                        </tr>
                        <tr>
                            <td>备注:</td>
                            <td>
                                <input type="text" name="remark"
                                       style="width: 250px;"/>
                            </td>
                        </tr>
                    </table>
                </div>
                <div class="modal-footer" align="center">
                    <button type="button" class="btn btn-default" data-dismiss="modal" align="center">关闭</button>
                    <button id="materialSubmit" type="button" class="btn btn-primary">提交</button>
                </div>
            </form>
        </div>
    </div>
</div>
<!-- /.modal end-->

<!-- 删除提示-->
<div class="modal fade" id="delcfmModel">
    <div class="modal-dialog">
        <div class="modal-content message_align">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-label="Close">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title">提示信息</h4>
            </div>
            <div class="modal-body">
                <p>您确认要删除吗？</p>
            </div>
            <div class="modal-footer">
                <input type="hidden" id="url"/>
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                <a onclick="deleteBatch()" class="btn btn-success"
                   data-dismiss="modal">确定</a>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>

</body>
</html>
