<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2019/3/5
  Time: 13:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<script src="/static/js/jquery.min.js"></script>
<script src="/static/js/_mp.js"></script>

<SCRIPT language=JavaScript>
    var data = "";
    var id = 0;

    //退出hger控件

    function save() {
        if (data) {
            $.ajax({
                type: "post",
                url: '/aiImg/update',
                data: {imgdata: data, id: id},
                success: function (data) {
                    if (data == "success") {
                        alert("成功");
                        window.close();
                    } else {
                        alert("添加失败")
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    alert(errorThrown);
                }
            });
        }
    }

    $(function () {
        if ('${id}') {
            id = '${id}';
        }
        if ('${imgbase}') {
            data = '${imgbase}';
            document.getElementById("zpDiv").src = "data:image/gif;base64,${imgbase}";

            //document.getElementById("zpDiv").innerHTML = '<img id="zpImg" width="130" height="230" src="data:image/gif;base64,'+data+'" />';
        }

    })


    function getMedia() {
        let constraints = {
            video: {width: 130, height: 230},
            audio: false
        };
        //获得video摄像头区域
        let video = document.getElementById("video");
        //这里介绍新的方法，返回一个 Promise对象
        // 这个Promise对象返回成功后的回调函数带一个 MediaStream 对象作为其参数
        // then()是Promise对象里的方法
        // then()方法是异步执行，当then()前的方法执行完后再执行then()内部的程序
        // 避免数据没有获取到
        let promise = navigator.mediaDevices.getUserMedia(constraints);
        promise.then(function (MediaStream) {
            video.srcObject = MediaStream;
            video.play();
        });
    }

    function takePhoto() {
        //获得Canvas对象
        let video = document.getElementById("video");
        let canvas = document.getElementById("canvas");

        let ctx = canvas.getContext('2d');
        ctx.drawImage(video, 0, 0, 130, 230);

        data = canvas.toDataURL("image/png");
        document.getElementById('zpDiv').src = canvas.toDataURL("image/png");
    }

</SCRIPT>

<body>
<table style="border: 1px solid red">
    <tr>
        <TD width=46 height=2>
            摄像头
        </TD>
        <TD width=46 height=2>

            拍照
        </TD>

        <TD width=46 height=2>

            原始照片
        </TD>


    </tr>
    <tr>
        <TD width=46 height=2>
            <video id="video" width="130" height="230" autoplay="autoplay"></video>

        </TD>
        <TD width=46 height=2>

            <canvas id="canvas" width="130" height="230"></canvas>


        </TD>

        <TD width=46 height=2>

            <img id='zpDiv' width="130" height="230">
        </TD>


    </tr>

</table>


<TD width=58 height=10>
    <input onMouseUp=getMedia() type=button value=打开 name=B242>
</TD>


<TD width=58 height=10>
    <input onMouseUp=takePhoto() type=button value=拍照 name=B244>
</TD>
<TD width=58 height=10>
    <input onMouseUp=save() type=button value=保存 name=B244>
</TD>


</body>
</html>
