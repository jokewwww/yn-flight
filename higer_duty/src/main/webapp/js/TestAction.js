
$(function(){
	$(".title").click(function(){
		$(this).next(".body").toggle();
	});
	$(".test_btn").click(function(){
		var jqBody = $(this).parents("form");
		var jqReslut = jqBody.find("#action_result");
		var params = array2Json(jqBody.serializeArray());
		//console.log(params);
		if(params.paramsType == "application/x-www-form-urlencoded"){
			try{
				var json = JSON.parse(params.body);
				params.body = $.param(json);
			}catch(err){
				alert("参数:json格式不正确");
			}
		}
		jqReslut.text("");
		$.ajax({
		  type: params.methodType,
		  url: params.url,
		  dataType: "text",
		  data:params.body,
		  contentType:params.paramsType,
		  success: function(msg){
			  jqReslut.text(msg);
		  },
		  error:function(jqXHR,textStatus,errorThrown){
			  jqReslut.text(jqXHR.responseText)
		  }
		});
	});
	
	function array2Json(array){
		var json = {};
		$.each(array,function(i,e){
			json[e.name] = e.value;
		});
		return json;
	}
});