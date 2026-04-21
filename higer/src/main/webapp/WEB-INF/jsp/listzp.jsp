<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <script src="/static/js/jquery.min.js"></script>

    <script src="/static/js/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="/static/js/json2.js"></script>

    <link id="easyuiTheme" rel="stylesheet" type="text/css" href="/static/css/easyui.css">
    <link rel="stylesheet" type="text/css" href="/static/css/icon.css">
    <link rel="stylesheet" type="text/css" href="/static/css/myui.css"/>
    <script type="text/javascript">
        function closePro() {
            $.messager.progress('close');
        }

        var data = null;
        var id;

        function subimtBtn() {


            var url = '/aiImg/save';
            if (id != null) {
                url = '/aiImg/update';
            }
            document.getElementById("form1").action = url;
            var form = $("form[name=imagefrm]");

            var options = {
                url: url,
                type: 'post',
                success: function (data) {
                    //alert(data);
                    var jsondata = eval("(" + data + ")");
                    // alert(jsondata);
                    if (jsondata.error == "0") {
                        var url = jsondata.url;
                        alert(url)
                        $("#img").attr("src", url);
                    } else {
                        var message = jsondata.mes;
                        alert(message);
                    }
                }
            };
            form.ajaxSubmit(options);
            //$("#fileForm").submit();
        }

        function saveZw() {
            var code = {name: $("#name").val(), pass: $("#cardNo").val()}
            var logoCode = JSON.stringify(code);
            /*            alert(logoCode);
                        alert(id);
                        alert(data);*/
            if (id != null) {
                $.ajax({
                    type: "post",
                    url: '/aiImg/update',
                    data: {imgdata: data, id: id, logoCode: logoCode},
                    success: function (data) {
                        if (data == "success") {
                            alert("成功");
                            $('#addZwWin').window('close');
                            $('#kcsbList').datagrid('clearSelections');
                            $('#kcsbList').datagrid('reload');//重新载入数据

                        } else {
                            alert("添加失败")
                        }
                        $("#name").val('');
                        $("#cardNo").val('');
                        data = null;
                        document.getElementById("zpDiv").innerHTML = '<img id="zpImg" width="140" height="190" src="" />';
                    },
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                        alert(errorThrown);
                        $("#name").val('');
                        $("#cardNo").val('');
                        data = null;
                        document.getElementById("zpDiv").innerHTML = '<img id="zpImg" width="140" height="190" src="" />';
                    }

                });
            } else {
                $.ajax({
                    url: '/aiImg/save',
                    type: "post",

                    data: {imgdata: data, logoCode: logoCode},
                    success: function (data) {
                        if (data == "success") {
                            alert("成功");
                            $('#addZwWin').window('close');
                            $('#kcsbList').datagrid('clearSelections');
                            $('#kcsbList').datagrid('reload');//重新载入数据
                            $("#name").val('');
                            $("#cardNo").val('');
                            data = null;
                            document.getElementById("zpDiv").innerHTML = '<img id="zpImg" width="140" height="190" src="" />';

                        } else if (data == "false") {
                            $("#name").val('');
                            $("#cardNo").val('');
                            data = null;
                            document.getElementById("zpDiv").innerHTML = '<img id="zpImg" width="140" height="190" src="" />';
                        }
                    },
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                        alert(errorThrown);
                    }
                });
            }
            return;


        }

        function delDrvmes() {
            var selected = $('#kcsbList').datagrid('getSelected');
            if (selected == null) {
                $.messager.alert("信息提示", "请选择要删除的信息!", "error");
                return false;
            } else {
                $.messager.confirm("提示", "你确定要删除此条信息吗?", function (r) {
                    if (r) {
                        $.ajax({
                            type: "post",
                            url: '/aiImg/delete',
                            data: {id: selected.id},
                            success: function (data) {
                                if (data == "success") {
                                    alert("成功");
                                    $('#kcsbList').datagrid('clearSelections');
                                    $('#kcsbList').datagrid('reload');//重新载入数据
                                    /*
                                                        $('#delcfmModel').modal('hide');
                                                        _mp.clearFormData("form1");
                                                        goToPage(1);*/
                                }
                            },
                            error: function (XMLHttpRequest, textStatus, errorThrown) {
                                alert(errorThrown);
                            }
                        });

                    }
                });
            }
        }

        $(document).ready(function () {

            $("#kcsbList").datagrid({
                url: '/aiImg/getAll', //请求的数据源
                singleSelect: true,
                fit: true,
                collapsible: false,
                method: 'post',
                remoteSort: false,
                rownumbers: true,
                fitColumns: true,
                idField: 'id',
                striped: true, //行背景交换
                nowap: true, //列内容多时自动折至第二行

                columns: [[//显示的列
                    {field: 'id', title: '序号', width: 100, align: 'center'},
                    {field: 'logoCode', title: '标识', width: 100, sortable: true, align: 'center'},
                    {field: 'imgData', title: '照片', width: 100, sortable: true, align: 'center'},
                    {field: 'imgDataBase', hidden: 'true', align: 'center'},

                ]],
                onLoadSuccess: closePro,
                onLoadError: closePro,
                toolbar: '#kcsbtb',
                onDblClickRow: function (rowIndex, rowData) {
                    var code = JSON.parse(rowData.logoCode)
                    $("#name").val(code.name);
                    $("#cardNo").val(code.pass);
                    id = rowData.id;

                    $("#upgradeid").val(id);


                    data = rowData.imgDataBase;
                    $('#addZwWin').show();
                    $('#addZwWin').window('open');
                    if (rowData.imgDataBase != '' && rowData.imgDataBase != null) {
                        $('#openBtn').val('打开');
                        $("#mjzpYkrq").val(rowData.ykrq);
                        document.getElementById("zpDiv").innerHTML = '<img id="zpImg" width="140" height="190" src="data:image/gif;base64,' + rowData.imgDataBase + '" />';
                        CloseCam();

                    } else {
                        $('#openBtn').val('关闭');
                        //  document.getElementById("zpDiv").src = "data:image/gif;base64,"+rowData.imgDataBase;
                        document.getElementById("zpDiv").innerHTML = '<img id="zpImg" width="140" height="190" src="" />';
                        OpenCam();


                    }

                },
                rowStyler: function (index, row) {
                    if (row.imgData == '无照片') {
                        return 'background-color:#FFD2D2;';
                    }

                }
            });

        });

        function add() {
            $("#name").val('');
            $("#cardNo").val('');
            document.getElementById("zpDiv").innerHTML = '<img id="zpImg" width="140" height="190" src="" />';
            $('#addZwWin').show();
            $('#addZwWin').window('open');
            id = null;
            $("#upgradeid").val(id);
            ocm = false;
            OpenCam();
            $('#openBtn').val('关闭');
            document.getElementById("catchPic").disabled = false; //使不可用
        }

        function searchKcsb() {
            $('#kcsbList').datagrid('clearSelections');
            $('#kcsbList').datagrid('loadData', {total: 0, rows: []});//清空数据
            $('#kcsbList').datagrid('load', {
                "kcsb.ksxm": $('#ksxm').val(),
                "kcsb.syzjcx": $('#syzjcx').val()
            });
        }


    </script>
</head>
<body>
<div class="easyui-layout" data-options="fit : true,border : false">
    <div data-options="region:'center',border:false" style="padding: 10px;">
        <table id="kcsbList"></table>
    </div>
    <div id="kcsbtb" style="padding: 5px; height: auto;text-align: center;">

        <a href="javascript:void(0)" class="easyui-linkbutton" onclick="delDrvmes()" iconCls="icon-remove" plain="true">删除</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" onclick="add()" plain="true">添加</a>


    </div>

    <div id="addZwWin" class="easyui-window" title="添加"
         data-options="iconCls:'icon-save',minimizable:false,maximizable:false,collapsible:false,draggable:false,resizable:false,closed:true,modal:false"
         style="width:700px;height:420px;padding:10px;display: none;">
        <%-- <form enctype="multipart/form-data" name="imagefrm"
               action="/aiImg/update" method="post">

             <div class="">
                 <ul class="scimg_cp">
                     <li class="">
                         <div class="sc_btn">
                             <A href="javascript:void(0);"> <input type="file"
                                                                   name="file1" class="s1">
                             </A>
                         </div>
                         <div class="sc">
                             <span id="s1"></span>
                         </div>
                     </li>
                 </ul>
             </div>
             <div class="modal-footer">
                 <button type="button" class="btn btn-default"
                         onclick="window.close();">关闭
                 </button>
                 <button type="submit" class="btn btn-primary" onclick="subimtBtn();">提交</button>
             </div>
         </form>--%>
        <form enctype="multipart/form-data" name="imagefrm" id="form1"
              action="" method="post">
            <input type="hidden" id="upgradeid" name="id">
            <table class="edit_table" style="height: 350px;">
                <tr>
                    <td class="edit_table_left" width="60">用户名：</td>
                    <td>
                        <input id="name" name="userName" class="easyui-validatebox"
                               data-options="required:true,validType:'unnormal'"
                               style="border: 1px solid #D3D3D3;width:140px;height:25px;"></input>
                    </td>
                    <td class="edit_table_left" width="40" rowspan="3">照片：</td>
                    <td rowspan="3">
                        <div id="zpDiv"><img src="images/no-img.jpg" style=" border: 1px solid #95B8E7;" width="120"
                                             height="160" id="zpImg"></img></div>
                    </td>

                </tr>
                <tr>
                    <td class="edit_table_left">密码：</td>
                    <td>
                        <input id="cardNo" name="userPassWord" class="easyui-validatebox" data-options="required:true"
                               style="border: 1px solid #D3D3D3;width:140px;height:25px;" maxlength="30"/>
                    </td>
                </tr>
                <tr>
                    <td class="edit_table_left">上传文件：</td>
                    <td height="30" align="left">
                        <input type="file" id="file1" name="file1" class="easyui-validatebox"
                               data-options="required:true" style="border: 1px solid #D3D3D3;width:200px; "/>
                    </td>
                </tr>
                <tr>

                    <td colspan="4" height="38">
                        <%--    &lt;%&ndash; <input type="button" id="openBtn" onFocus="this.blur()" class="buttonStyle" onclick="pz()" value="选择照片" />&ndash;%&gt;
                           &lt;%&ndash;  <input type="button" id="catchPic" onFocus="this.blur()" class="buttonStyle" onclick="saveZP()" value="拍照" />&ndash;%&gt;
                            &lt;%&ndash; <input id="saveBtn" type="button" onFocus="this.blur()"  class="buttonStyle"  onclick="subimtBtn()" value="保存"/>
         &ndash;%&gt;--%>
                        <button type="submit" class="btn btn-primary" onclick="subimtBtn();">提交</button>

                    </td>
                </tr>
            </table>
        </form>
    </div>
</div>
</body>
</html>