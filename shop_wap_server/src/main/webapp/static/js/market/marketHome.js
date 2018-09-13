//选择活动
	function chooseMarket(marketTypeId){
		$.ajax({
			  url: baseProject+"marketHome/queryFrontSubjectDisplayList",
			  data: {marketTypeId:marketTypeId},
			  contentType: "application/x-www-form-urlencoded; charset=UTF-8",
			  dataType: "json",
			  success: function(data){
				  $("#div_"+marketTypeId).html("");
				  var htmlText = "";
				  var nowDate = data.nowDate;
				  $.each(data.subjectDisplayList,function(i,item){
					  if(marketTypeId==1){
						  htmlText +="<dl><a href='"+baseProject+pageTfsServerPath+"/"+item.busiUrl+"'>"
						  		   + "<dt><img src='"+imageTfsServerPath+item.busiImgUrl+"' /></dt>"
						  		   + "<dd> <span href='"+pageTfsServerPath+item.busiUrl+"' class='tu-con'>"+item.businessName+"<br> "+item.busiDesc+"</span>"
						  		   + "<div class='tu-fr tu-tip'><div class='pull-left'>  <span class='tu-cl xj-fr'><strong>团购价：￥"+changeF2Y(item.businessPrice/100,2)+"</strong></span> </div>"
						  		   + "</div></dd></a></dl>";
						  
					  }
					  else if(marketTypeId==2){
						  var end_time = new Date(item.endTime).getTime();
						  var sys_second = (end_time-new Date(nowDate).getTime())/1000;
						  if(sys_second>0){
							  var timeHtml = "距离秒杀结束还剩:<span class='colockbox f14 font-gray3' id='colockbox"+i+"'> <span class='day'>03</span>天<span class='hour'>03</span>小时 <span class='minute'>03</span>分 <span class='second'>03</span>秒</span></span></div>";
						  }
						  else {
							  timeHtml = "秒杀活动已经结束";
							  
						  }
						  htmlText +="<dl><a href='"+baseProject+pageTfsServerPath+"/"+item.busiUrl+"'>"
							 + "<dt><img src='"+imageTfsServerPath+item.busiImgUrl+"' /></dt>"
							 + "<dd> <span href='"+pageTfsServerPath+item.busiUrl+"' class='tu-con'>"+item.businessName+"<br> "+item.busiDesc+"</span>"
							 + "<div class='tu-fr tu-tip'><div class='pull-left'>  <span class='tu-cl xj-fr'><strong>秒杀价：￥"+changeF2Y(item.businessPrice/100,2)+"</strong></span> </div>"
							 + "</div></dd></a><div class='clear'></div><div class='st-fr'>"+timeHtml+"</dl>";
						  countDown(item.endTime,nowDate,"#colockbox"+i+" .day","#colockbox"+i+" .hour","#colockbox"+i+" .minute","#colockbox"+i+" .second");
						
					  }
					  else if(marketTypeId==3){
						  htmlText+="<dl> <a href='"+baseProject+pageTfsServerPath+"/"+item.busiUrl+"'> <dt><img src='"+imageTfsServerPath+item.busiImgUrl+"' /></dt>"
						  		  + "<dd> <span href='"+pageTfsServerPath+item.busiUrl+"' class='tu-con'>"+item.businessName+"<br>"+item.busiDesc+" </span>"
						  		  + "<div class='tu-fr tu-tip'><div class='pull-left'> <span class='tu-cl xj-fr'><strong>预售价：￥"+changeF2Y(item.businessPrice/100,2)+"</strong></span> </div>"
						  		  + "</div></dd></a></dl>";
					  }
				  });
				  $("#div_"+marketTypeId).append(htmlText);
				  
			  }
		});
	}
	
	//s:传入的float数字 ，n:希望返回小数点几位
	 function changeF2Y(s, n) {
	 	n = n > 0 && n <= 20 ? n : 2;
	 	s = parseFloat((s + "").replace(/[^\d\.-]/g, "")).toFixed(n) + "";
	 	var l = s.split(".")[0].split("").reverse(), r = s.split(".")[1];
	 	t = "";
	 	for (i = 0; i < l.length; i++) {
	 		t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "," : "");
	 	}
	 	return t.split("").reverse().join("") + "." + r;
	 }
	
	