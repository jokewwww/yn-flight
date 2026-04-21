

var app1 = new Vue({
  el: '#div_btns',
  data: {
    message: 'Hello Vue I am !',
    btns:[{
    	url:"duty/DutyTime.jsp",
    	value:'值班时间段',
    	active:false
    },{
    	url:"duty/DutyTeam.jsp",
    	value:'值班组',
    	active:false
    }]
  },
  mounted:function(){
	  console.log("load init");
  },
  methods:{
	  onTabClick:function(index){
		  this.btns.forEach(e=>e.active=false);
		  console.log(index);
		  const btnJson = this.btns[index];
		  btnJson.active = true;
		  $.get(btnJson.url,function(html){
			  $("#vue_group").html(html);
		  });
	  }
  }
});
