<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div id="vue_frame">
	<div style="margin:3px;">
		<input type="text" v-model="editJson.teamName" placeholder="班组名称" style="width:80px;"/>
		<input type="text"  v-model="editJson.remark" placeholder="备注"  style="width:80px;"/>
		<input type="button" value="保存" @click="add"/>
	</div>
	<table>
		<tr>
			<th>序号</th>
			<th>班组名称</th>
			<th>备注</th>
			<th>操作</th>
		</tr>
		<template v-for="(json,index) in dataList">
		 	<tr>
		 		<td>{{json.teamId}}</td>
				<td>{{json.teamName}}</td>
				<td>{{json.remark}}</td>
				<td>
					<span class="span_btn" @click="edit(index)">编辑</span>
					<span class="span_btn" @click="addStaff(index,json.teamId,json.teamName)">值班人员</span>
					<span class="span_btn" @click="deleteRow(index,json.teamId)">删除</span>
				</td>
		 	</tr>
		</template>
	</table>
	
	<div class="m-dialog" v-show="staffModel">
		<div class="m-dialog-mask"></div>
		<div class="m-dialog-body">
			<div style="text-align:center;">
				<template v-for="(json,index) in staffList">
					<input :id="json.staffId" type="checkbox" :value="json.staffId" v-model="checkStaffs">
					<label :for="json.staffId">{{json.staffName}}</label>
				</template>
			</div>
			<div style="text-align:center;">
				<input type="button" value="保存" @click="saveStaff"/>
				<input type="button" value="取消" @click="staffModel=false"/>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
	var app1 = new Vue({
	  el: '#vue_frame',
	  data: {
	    message: 'I am DutyTime.',
	    dataList:[],
	    editJson:{},
	    staffModel:false,
	    staffList:[],
	    checkStaffs:["15144", "15145", "15146"]
	  },
	  mounted:function(){
		  console.log("load DutyTime");
		  this.search();
		  this.loadStaff();
	  },
	  methods:{
		  loadStaff:function(){// 
			  $.post("duty//staff/list.do",{staffType:3},mbean=>{
				  if(mbean.id == 0){
					  this.staffList = mbean.data;
				  }
			  });
		  },
		  search:function(){
			  $.post("duty/dutyteam/jsons.do",mbean=>{
				  if(mbean.id == 0){
					  this.dataList = mbean.data;
					  this.editJson = {};
				  }
			  });
		  },
		  add:function(){
			  $.post("duty/dutyteam/save.do",this.editJson,mbean=>{
				  this.search();
			  });
		  },
		  edit:function(index){
			  var json = this.dataList[index];
			  //this.editJson = Object.assign({},this.editJson,this.dataList[index]);
			  this.$set(this.editJson,"teamName",json.teamName);
			  this.$set(this.editJson,"remark",json.remark);
			  this.$set(this.editJson,"teamId",json.teamId);
		  },
		  deleteRow:function(index,id){
			  var r = confirm("确定要删除吗?");
			  if(!r) return;
			  $.post("duty/dutyteam/delete.do",{teamId:id},mbean=>{
				  this.search();
			  });
		  },
		  addStaff:function(index,id,name){
			  //查询班组成员
			  $.post("duty/dutyteam/staffIds.do",{teamName:name},e=>{
				  this.staffModel = true;
				  this.checkStaffs = e.data;
				  this.editJson.teamId = id;
			  });
		  },
		  saveStaff:function(){
			  console.log(this.checkStaffs);
			  this.editJson.staffIds = this.checkStaffs.join(",");
			  $.post("duty/dutyteam/addStaffs.do",this.editJson,e=>{
				  this.editJson = {};
				  this.staffModel = false;
			  });
		  }
	  }
	});
</script>
