// JavaScript Document
$(function(){
     $("#dxdiv").hide();
    })
	function clickCheckBox(obj){
	if("fw"==obj){
	$("#fw").attr("checked",'true');
	$("#dx").removeAttr("checked");
	$("#dxdiv").hide();
	$("#fwdiv").show();
	$("#li_kdxx").show();
	$("#li_mbhxx").show();
	}else if("dx"==obj){
	$("#fw").removeAttr("checked");
	$("#dx").attr("checked",'true');
	$("#dxdiv").show();
	$("#fwdiv").hide();
	$("#li_kdxx").hide();
	$("#li_mbhxx").hide();
	}
	}
$(function(){
     $("#bxaw").hide();
    })
	function adc(obj){
	if("bw"==obj){
	$("#bw").attr("checked",'true');
	$("#bx").removeAttr("checked");
	$("#bxaw").hide();
	$("#bws").show();
	}else if("bx"==obj){
	$("#bw").removeAttr("checked");
	$("#bx").attr("checked",'true');
	$("#bxaw").show();
	$("#bws").hide();
	}
	}
$(function(){
     $("#mxaw").hide();
    })
	function def(obj){
	if("xw"==obj){
	$("#xw").attr("checked",'true');
	$("#mx").removeAttr("checked");
	$("#mxaw").hide();
	$("#xws").show();
	}else if("mx"==obj){
	$("#xw").removeAttr("checked");
	$("#mx").attr("checked",'true');
	$("#mxaw").show();
	$("#xws").hide();
	}
	}