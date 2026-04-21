<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="author" content="Jophy"/>
    <title>文件上传</title>
    <script src="/static/js/jquery.min.js"></script>
    <script type="text/javascript">
        var inf = "";
        var name = "";

        function subimtBtn() {
            var form = $("form[name=imagefrm]");
            var options = {
                url: '/uploadfiles',
                type: 'post',
                async: false,
                success: function (data) {
                    var jsondata = eval("(" + data + ")");
                    if (jsondata.error == "0") {
                        var url = jsondata.url;
                        alert(url)
                        $("#img").attr("src", url);
                    } else {
                        var message = jsondata.message;
                        alert(message);
                    }
                }
            };
            form.ajaxSubmit(options);
            //$("#fileForm").submit();
        }

        function c(th, s) {
            name = s;
            if (s == 1) {
                inf = document.getElementById('s1');
            }

            var fN = '';
            //判断并获取文件名
            if (fN = th.value.match(/[^\\\/]+\.[a-zA-Z0-9]+$/)) {
                //如果获取到文件名，则将文件名在后面的<A href="https://www.baidu.com/s?wd=span%E6%A0%87%E7%AD%BE&tn=44039180_cpr&fenlei=mv6quAkxTZn0IZRqIHckPjm4nH00T1YkujmYnj6YPHPhPWbdPhfY0ZwV5Hcvrjm3rH6sPfKWUMw85HfYnjn4nH6sgvPsT6KdThsqpZwYTjCEQLGCpyw9Uz4Bmy-bIi4WUvYETgN-TLwGUv3EPHR4PWDvPjfk" target="_blank" class="baidu-highlight">span标签</A>中显示出来。
                //这里你可以自行修改要显示的样式等。
                inf.innerHTML = "<font color='green'>√ " + fN + "</font>";
            } else {
                inf.innerHTML = "<font color='red'>× 获取文件名失败</font>";
            }
            $("#whimg").attr('src', getObjectURL(th.files[0]));
        }

        function getObjectURL(file) {
            var url = null;
            if (window.createObjectURL != undefined) {
                url = window.createObjectURL(file)
            } else if (window.URL != undefined) {
                url = window.URL.createObjectURL(file)
            } else if (window.webkitURL != undefined) {
                url = window.webkitURL.createObjectURL(file)
            }
            return url
        };

        $(function () {
            if ('${mes}') {
                alert('${mes}');
                window.close();
            }
        })
    </script>
    <meta name="keywords" content=""/>
    <meta name="description" content=""/>
    <link href="/static/css/style.css" rel="stylesheet">
    <style type="text/css">
        .scimg_cp {
            overflow: hidden;
        }

        li {
            list-style: none;
            float: left;
            margin: 5px;
            width: 27%;
            position: relative;
            overflow: hidden;
        }

        img {
            width: 150px;
            height: 150px;
            border: 1px solid #ddd;
            float: left;
        }

        input {
            font-size: 10px;
        }

        .sc_btn {
            float: right;
            margin-top: 5px;
        }

        .sc {
            float: left;
            margin-top: 5px;
        }

        .sc_btn a input {
            font-size: 10px;
        }
    </style>
</head>
<body>


<div class="modal fade" id="nmo">
    <div class="modal-dialog" style="width: 900px;">
        <div class="modal-content" align="center">
            <div class="modal-header">
                <h4 class="modal-title" id="myModalLabel">上传APK</h4>
            </div>
            <form enctype="multipart/form-data" name="imagefrm"
                  action="/uploadfiles" method="post">
                <input type="hidden" id="upgradeid" name="upgradeid" value="${id}">
                <div class="">
                    <ul class="scimg_cp">
                        <li class="">
                            <div class="sc_btn">
                                <a href="javascript:void(0);"> <input type="file"
                                                                      name="file1" class="s1">
                                </a>
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
            </form>
        </div>
    </div>
</div>
</body>
</html>