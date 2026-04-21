<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>排版测试</title>
<script src="js/jquery.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/vue@2.6.11"></script>
<link href="css/main.css" rel="stylesheet">
</head> 
<body>
<div id="div_btns">
	<template v-for="(btn,index) in btns">
		<span class="btn" :class="{btn_active:btn.active}" @click="onTabClick(index)">{{btn.value}}</span>
	</template>
</div>
<div id="vue_group"></div>
<script src="js/duty.js"></script>
</body>
</html>