<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>油单管理管理</title>
    <link href="/static/css/bootstrap.css" rel="stylesheet">
    <link href="/static/css/style.css" rel="stylesheet">
    <link href="/static/css/font-awesome.css" rel="stylesheet">
    <script src="/static/js/jquery.min.js"></script>
    <script src="/static/js/_mp.js"></script>
    <script src="/static/js/bootstrap.js"></script>
    <script src="/static/js/jquery-html5Validate.js"></script>
</head>
<script type="application/javascript">

    var websocket = null;
    var logos = "";
    if ('WebSocket' in window) {//120.27.24.213   192.168.1.101
        websocket = new WebSocket("ws://192.168.1.101:8989/websocket");
    } else {
        console.log('当前浏览器 Not support websocket')
    }

    //连接发生错误的回调方法
    websocket.onerror = function () {
        //alert("error");
        setMessageInnerHTML("WebSocket连接发生错误");
    };

    //连接成功建立的回调方法
    websocket.onopen = function () {
        //alert("success");
        console.log("success");
        //setMessageInnerHTML("链接成功");
    }

    //接收到消息的回调方法
    websocket.onmessage = function (event) {
        var datas = JSON.parse(event.data);
        console.log(datas);

        for (var i = 0; i < datas.length; i++) {
            var date = JSON.parse(datas[i].data);
            //判断 不包含  如果包含 就
            if (logos.indexOf(datas[i].hp) == -1 && logos != "") {
                $("#admserch1").css("display", "block");//display属性设置为block（显示）
                $("#hp1").text(date.hp);
                $("#jd1").text(date.jd);
                $("#wd1").text(date.wd);
                $("#gd1").text(date.jd);
                if (date.num_data) {
                    var numDatas = (date.num_data + "").split(",");
                    $("#numDataOne1").text(numDatas[0]);
                    $("#numDataTwo1").text(numDatas[1]);
                }
                if (datas[i].state == 0) {
                    $("#state1").text("离线");
                } else {
                    $("#state1").text("在线");
                }
            } else {
                if (logos == "") {
                    logos += datas[i].hp;
                }
                $("#hp").text(date.hp);
                $("#jd").text(date.jd);
                $("#wd").text(date.wd);
                $("#gd").text(date.jd);
                if (date.num_data) {
                    var numDatas = (date.num_data + "").split(",");
                    $("#numDataOne").text(numDatas[0]);
                    $("#numDataTwo").text(numDatas[1]);
                }
                if (datas[i].state == 0) {
                    $("#state").text("离线");
                } else {
                    $("#state").text("在线");
                }
            }
        }
        // pageInit();
    };
    //连接关闭的回调方法
    websocket.onclose = function () {
        console.log("close");
        //setMessageInnerHTML("WebSocket连接关闭");
    }

    //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
    window.onbeforeunload = function () {
        closeWebSocket();
    }

    //将消息显示在网页上
    function setMessageInnerHTML(innerHTML) {
        // document.getElementById('message').innerHTML += innerHTML + '<br/>';
    }

    //关闭WebSocket连接
    function closeWebSocket() {
        websocket.close();
    }

    var url = 'system/testaa/getAll';
    var success = {
        "message": "success", "delmessage": "删除成功！"
    };
    var warning = {
        "warn": "请选择一条信息！"
    };
    $(function () {
        $("#admserch1").css("display", "none");
        pageInit();
        //修改回填
        $("#editMaterial").click(function () {
            initForm();
        });
        $("#refreshData").click(function () {
            pageInit();
        })
    });


    function pageInit() {
        goToPage(1);
    }

    function goToPage(page) {
        var condition = "";
        _mp.getPageData("test", url, page, condition, pageCallBack);
    }

    function pageCallBack(datas) {
        $('#testaa').find('tbody').empty();
        var trHtml = "";
        if (!datas || datas.length === 0) {
            trHtml += "<tr align='center'><td colspan='100'>没有相关数据</td></tr>";
        } else {
            for (var i = 0; i < datas.length; i++) {
                _mp.cache.put(datas[i].id, datas[i]);
                trHtml += "<tr>";
                trHtml += "<td><input type='checkbox' name='checkbox' id='checkbox' value=" + datas[i].id + "></td>";
                trHtml += "<td>" + datas[i].signName + "</td>";
                trHtml += "<td>" + datas[i].deliveryNo + "</td>";
                trHtml += "</tr>";
            }
        }
        $('#testaa').find('tbody').append(trHtml);
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

</script>
<body>
<div class="mainbar">
    <div class="page-head">
        <h2 class="pull-left">
            <i class="icon-home"></i>油单记录
        </h2>
        <div class="clearfix"></div>
        <div class="adm_sergc">
            <div class="admserch">
                车牌:<span id="hp"></span>&nbsp;&nbsp; 经度:<span id="jd"></span>&nbsp;&nbsp;纬度:<span
                    id="wd"></span>高度:<span id="gd"></span>&nbsp;&nbsp;油量:<span
                    id="numDataOne"></span>&nbsp;&nbsp;流速:<span id="numDataTwo"></span>&nbsp;&nbsp;状态:<span
                    id="state"></span>
            </div>
            <div class="admserch" id="admserch1">
                车牌:<span id="hp1"></span>&nbsp;&nbsp; 经度:<span id="jd1"></span>&nbsp;&nbsp;纬度:<span
                    id="wd1"></span>高度:<span id="gd1"></span>&nbsp;&nbsp;油量:<span
                    id="numDataOne1"></span>&nbsp;&nbsp;流速:<span id="numDataTwo1"></span>&nbsp;&nbsp;状态:<span
                    id="state1"></span>
            </div>
            <div class="admzsgc">
                <form>
                    <input id="editMaterial" name="" class="admzsgc_btn" type="button" value="查看"
                           data-toggle="modal"/>
                    <input id="refreshData" name="" class="admzsgc_btn" type="button" value="刷新油单"/>
                </form>
            </div>
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
                                                <th>加油员</th>
                                                <th>油单编号</th>
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
                <h4 class="modal-title" id="myModalLabel" align="center">油单查看</h4>
            </div>
            <form class="i-form" id="form1">
                <input type="text" style="display: none" id="id" name="id"/>
                <div class="modal-body">
                    <table align="center">
                        <tr>
                            <td>日期:</td>
                            <td>
                                <input type="text" name="date"
                                       style="width: 250px;" required/>
                            </td>
                        </tr>
                        <tr>
                            <td>油单类型:</td>
                            <td>
                                <input type="text" name="deliveryType"
                                       style="width: 250px;" required/>
                            </td>
                        </tr>
                        <tr>
                            <td>油单号:</td>
                            <td>
                                <input type="text" name="deliveryNo"
                                       style="width: 250px;" required/>
                            </td>
                        </tr>
                        <tr>
                            <td>机场:</td>
                            <td>
                                <input type="text" name="airport"
                                       style="width: 250px;" required/>
                            </td>
                        </tr>
                        <tr>
                            <td>所属单位:</td>
                            <td>
                                <input type="text" name="delivered"
                                       style="width: 250px;" required/>
                            </td>
                        </tr>
                        <tr>
                            <td>航班号:</td>
                            <td>
                                <input type="text" name="filghtNo"
                                       style="width: 250px;" required/>
                            </td>
                        </tr>
                        <tr>
                            <td>飞机号:</td>
                            <td>
                                <input type="text" name="aircraftNo"
                                       style="width: 250px;" required/>
                            </td>
                        </tr>
                        <tr>
                            <td>飞机类型:</td>
                            <td>
                                <input type="text" name="aircraftType"
                                       style="width: 250px;" required/>
                            </td>
                        </tr>
                        <tr>
                            <td>起始:</td>
                            <td>
                                <input type="text" name="departure"
                                       style="width: 250px;" required/>
                            </td>
                        </tr>
                        <tr>
                            <td>经停（备降）:</td>
                            <td>
                                <input type="text" name="transitStop"
                                       style="width: 250px;" required/>
                            </td>
                        </tr>
                        <tr>
                            <td>终点:</td>
                            <td>
                                <input type="text" name="destination"
                                       style="width: 250px;" required/>
                            </td>
                        </tr>
                        <tr>
                            <td>化验单号码:</td>
                            <td>
                                <input type="text" name="testbillNo"
                                       style="width: 250px;" required/>
                            </td>
                        </tr>
                        <tr>
                            <td>油品名称与标准:</td>
                            <td>
                                <input type="text" name="descriptionAndGrade"
                                       style="width: 250px;" required/>
                            </td>
                        </tr>
                        <tr>
                            <td>摄氏度:</td>
                            <td>
                                <input type="text" name="temperature"
                                       style="width: 250px;" required/>
                            </td>
                        </tr>
                        <tr>
                            <td>实际密度g/cm3:</td>
                            <td>
                                <input type="text" name="actualDensity"
                                       style="width: 250px;" required/>
                            </td>
                        </tr>
                        <tr>
                            <td>计量表开始读数 升:</td>
                            <td>
                                <input type="text" name="meterStart"
                                       style="width: 250px;" required/>
                            </td>
                        </tr>
                        <tr>
                            <td>计量表结束读数 升:</td>
                            <td>
                                <input type="text" name="meterFinish"
                                       style="width: 250px;" required/>
                            </td>
                        </tr>
                        <tr>
                            <td>加油数量 小写 升:</td>
                            <td>
                                <input type="text" name="figures"
                                       style="width: 250px;" required/>
                            </td>
                        </tr>
                        <tr>
                            <td>加油数量 大写 升:</td>
                            <td>
                                <input type="text" name="figuresInWords"
                                       style="width: 250px;" required/>
                            </td>
                        </tr>
                        <tr>
                            <td>加油数量 千克:</td>
                            <td>
                                <input type="text" name="quantity"
                                       style="width: 250px;" required/>
                            </td>
                        </tr>
                        <tr>
                            <td>加油地井编号:</td>
                            <td>
                                <input type="text" name="hydrantPitNo"
                                       style="width: 250px;" required/>
                            </td>
                        </tr>
                        <tr>
                            <td>加油车车号:</td>
                            <td>
                                <input type="text" name="vehicleTypeAndNo"
                                       style="width: 250px;" required/>
                            </td>
                        </tr>
                        <tr>
                            <td>加油开始时间:</td>
                            <td>
                                <input type="text" name="timeStart"
                                       style="width: 250px;" required/>
                            </td>
                        </tr>
                        <tr>
                            <td>加油结束时间:</td>
                            <td>
                                <input type="text" name="timeFinish"
                                       style="width: 250px;" required/>
                            </td>
                        </tr>
                        <tr>
                            <td>加油员:</td>
                            <td>
                                <input type="text" name="signName"
                                       style="width: 250px;" required/>
                            </td>
                        </tr>
                    </table>
                </div>
                <div class="modal-footer" align="center">
                    <button type="button" class="btn btn-default" data-dismiss="modal" align="center">关闭</button>
                    <%--<button id="materialSubmit" type="button" class="btn btn-primary">提交</button>--%>
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
