function tab(nav,content,on,type)
{
	$(nav).children().bind(type,(function(){
		var $tab=$(this);
		var tab_index=$tab.prevAll().length;
		var $content = $(content).children();
		$(nav).children().removeClass(on);
		$tab.addClass(on);
		$content.hide();
		$content.eq(tab_index).show();
		// $content.find("img.lazy").show().lazyload();
		/*$content.find("img.lazy").lazyload({
		    effect: "show"
		});*/
	
	}));
}


//初始化延迟加载
function lazyload() {
	// 兼容不支持js的情况
	$("img.lazy").show().lazyload();
	$("img.lazy").lazyload({ 
		effect: "show"
	});
}

$(function (){

	//Tab切换
	tab(".Tab .TabHead",".Tab .TabNote","on","click");
    tab(".Tab02 .TabHead",".Tab02 .TabNote","on","click");
	//二级页面底部菜单切换
	tab("#label ul","#labelWrap","on","click");

	//竖Tab切换
	tab(".Tab-vertical .TabHead",".Tab-vertical .TabNote","on","click");
	
		//展开收起
	$(".deploy").off("click").on("click",".title",function()
	{
		var t = $(this).parent();
		t.toggleClass("deployChoose");
		var p = t.find(".SelectBox");
		if ($("#BillDetailPage").length > 0 || $("#chargeIndexPage").length > 0) {
			p.slideToggle(200, function() {
				if (typeof (topFloat) == "function") {
	                topFloat('topfloat');
	            }
			});
		} else {
			p.slideToggle(200);
		}
		var f=$(this).find(".unfold");
		if(f.hasClass("up")){
			f.html("收起");
		}else{
			f.html("展开");
		}
		f.toggleClass("up");
		p = t.find(".bankSelectBox");
		p.slideToggle(200);
	});
	
	$(".deploy").on("click", ".RadioBox ul li", function(){
		$(this).parents("ul").find("li").removeClass("on");
		$(this).parents("ul").find("li i").remove();
		$(this).addClass("on");
		$(this).append("<i></i>");
		
		if (typeof (calcPrice) == "function") {
			calcPrice();
        }
	})
    function list_oil() {

        var list = document.getElementById("box-oil");
        if(list!=null) {
           var list = list.getElementsByTagName("span");
            for (var i = 0; i < list.length; i++) {
                list[i].onclick = function () {
                    change(this);
                }
            }
        }
        function change(obj) {
            for (var i = 0; i < list.length; i++) {
                if (list[i] == obj) {
                    list[i].className = "on";
                }
                else {
                    list[i].className = "";


                }

            }

        }
    }

/************/

$(document).ready(function(){
	list_oil()
}
)
})
	
