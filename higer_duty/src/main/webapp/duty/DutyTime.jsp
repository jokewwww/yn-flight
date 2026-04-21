<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div id="vue_frame">
	<div style="margin:3px;">
		<input type="text" v-model="editJson.startTime" placeholder="开始时间" style="width:80px;"/>
		<input type="text"  v-model="editJson.endTime" placeholder="结束时间"  style="width:80px;"/>
		<input type="button" value="保存" @click="add"/>
	</div>
	<table>
		<tr>
			<th>序号</th>
			<th>开始时间</th>
			<th>结束时间</th>
			<th>操作</th>
		</tr>
		<template v-for="(json,index) in dataList">
		 	<tr>
		 		<td>{{json.timeId}}</td>
				<td>{{json.startTimeStr}}</td>
				<td>{{json.endTimeStr}}</td>
				<td>
					<span class="span_btn" @click="edit(index)">编辑</span>
					<span class="span_btn" @click="deleteRow(index,json.timeId)">删除</span>
				</td>
		 	</tr>
		</template>
	</table>
</div>
<script type="text/javascript">
	var app1 = new Vue({
	  el: '#vue_frame',
	  data: {
	    message: 'I am DutyTime.',
	    dataList:[],
	    editJson:{}
	  },
	  mounted:function(){
		  console.log("load DutyTime");
		  this.search();
	  },
	  methods:{
		  search:function(){
			  $.post("duty/dutytime/list.do",mbean=>{
				  if(mbean.id == 0){
					  this.dataList = mbean.data;
				  }
			  });
		  },
		  add:function(){
			  $.post("duty/dutytime/save.do",this.editJson,mbean=>{
				  this.search();
			  });
		  },
		  edit:function(index){
			  var json = this.dataList[index];
			  //this.editJson = Object.assign({},this.editJson,this.dataList[index]);
			  this.$set(this.editJson,"startTime",json.startTimeStr);
			  this.$set(this.editJson,"endTime",json.endTimeStr);
			  this.$set(this.editJson,"timeId",json.timeId);
		  },
		  deleteRow:function(index,timeId){
			  var r = confirm("确定要删除吗?");
			  if(!r) return;
			  $.post("duty/dutytime/delete.do",{timeId:timeId},mbean=>{
				  this.search();
			  });
		  }
	  }
	});
</script>
