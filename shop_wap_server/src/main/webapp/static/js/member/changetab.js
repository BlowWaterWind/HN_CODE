$(document).ready( function () {   
			var $li = $('#mytabs li');
			var $ul = $('#tab-content');
			var $div = $('#tab-content .tab-pane');	
            var $div02 = $('#tab-content02 .tab-pane');
			$li.click(function(){
				var $this = $(this);
				var $t = $this.index();
				$li.removeClass();
				$this.addClass('active');
				$div.css('display','none');
				$div02.css('display','none');
				$ul.eq($t).css('display','block');
				$div.eq($t).css('display','block');
				$div02.eq($t).css('display','block');
				
			});
			var $li02 = $('#tips li');
			var $ul02 = $('#tips-content ul');
			var $div02 = $('#tips-content .tips-con');	
			$li02.click(function(){
				var $this = $(this);
				var $t = $this.index();
				$li02.removeClass();
				$this.addClass('active');
				$div02.css('display','none');
				$ul02.eq($t).css('display','block');
				$div02.eq($t).css('display','block');
				
			});
			
			var $li03 = $('#zxtips li');
			var $ul03 = $('#zx-content ul');
			var $div03 = $('#zx-content .zx-con');	
			$li03.click(function(){
				var $this = $(this);
				var $t = $this.index();
				$li03.removeClass();
				$this.addClass('active');
				$div03.css('display','none');
				$ul03.eq($t).css('display','block');
				$div03.eq($t).css('display','block');
				
			});
			
});
//单品页商品参数选择
$('.sp-button li').bind('click', function(){ 
    $(this).addClass('list-active').siblings().removeClass('list-active'); 
}); 
$('.sp-button02 li').bind('click', function(){ 
    $(this).addClass('active').siblings().removeClass('active'); 
});
$('#mytabs li').bind('click', function(){ 
    $(this).addClass('active').siblings().removeClass('active'); 
});
$('.tab-er').bind('click', function(){ 
    $("#mytabs li").removeClass('active'); 
});
$('.filter-result-w').bind('click', function(){
    $("#mytabs li").removeClass('active'); 
}); 	
