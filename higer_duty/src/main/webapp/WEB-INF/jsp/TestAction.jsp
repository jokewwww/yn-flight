<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<meta name="renderer" content="webkit">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="X-UA-Compatible" content="IE=EDGE;IE=10;IE=9;IE=8"/>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<script type="text/javascript" src="js/jquery.min.js"></script>
<script type="text/javascript" src="js/TestAction.js"></script>
<title></title>
<style type="text/css">

.maindiv{
	width:900px;
	margin:0px;
	margin-left:30px;
	margin-top:50px;
	border:1px solid #aaa;
}

.maindiv .title{
	color:black;font-weight:700;font-size:16px;
	background1:#ccc;
}

.maindiv .body{
	padding-left:30px;
}

.maindiv .test_btn{
	margin-left:22px;
	color:blue;
	cursor: pointer;
}

.maindiv .querycond .label{
	font-size:14px;
	margin:10px;
}
.maindiv .querycond .text{
	font-size:15px;
	margin:2px;
	text-decoration: underline; 
}
</style>
</head> 
<body>
<div class="maindiv">
	<c:forEach items="${pktJson}" var="pkt" >
	<!-- 包 -->
	<div class="div_pkt">
		<div class="title" style="color:#aaa;">包名:${pkt.key}</div>
		<div class="body" style="display1:none;">
			<c:forEach items="${pkt.value}" var="cls">
			<!-- 类 -->
			<div>
				<div class="title" style="color:#888;">类名:${cls.key}</div>
				<div class="body"  style="display1:none;">
					<c:forEach items="${cls.value}" var="action" >
					<c:set var="um" value="${action.value}"></c:set>
					<!-- 方法 -->
					<div>
						<div class="title">
							<span style="color:#666;">方法名:${um.methodName}</span>
						</div>
						<div class="body" style="display:none;">
							<form>
								<div>
									<span>地址</span>
									<span id="action_url">
										<input name="url" style="width:300px;margin-left:40px;" value="${um.url}"/>
									</span>
									<span class="test_btn">测试</span>
								</div>
								<div>
									<span>调用方法</span>
									<span>
										<input type="radio" value="POST" name="methodType" checked/><label>POST</label>
										<input type="radio" value="GET" name="methodType"/><label>GET</label>
									</span>
								</div>
								<div>
									<span>参数格式</span>
									<span>
										<input type="radio" value="application/x-www-form-urlencoded" name="paramsType" checked /><label>FORM</label>
										<input type="radio" value="application/json" name="paramsType"/><label>JSON</label>
									</span>
								</div>
								<div>
									<div>参数</div>
									<textarea name="body" cols="25" rows="6">${um.params}</textarea>
								</div>
								<div>
									<div>结果</div>
									<div id="action_result" style="height:150px;border:1px solid #bbb;overflow:auto;margin-right:3px;"></div>
								</div>
							</form>
						</div>
					</div>
					</c:forEach> 
				</div>
			</div>
			</c:forEach> 
		</div>
	</div>
	</c:forEach> 
</div>
</body>
</html>   
