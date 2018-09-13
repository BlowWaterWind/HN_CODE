$(function(){
	/**
	 * 显示评价信息
	 */
	$("#loadMyModal7").on("click",function(){
		
		 var $this = $(this);
		 var url = baseProject + $(this).attr("url");
	     var $loadMorePj = $("#loadMorePj"); 
	     
	     var pageNo = $this.attr("pageNo");
	     var pageSize = $this.attr("pageSize");
	     var count = $this.attr("count");
	     var lastPage = $this.attr("lastPage");
	     if( lastPage != "true")
	     {
	     var param = {
	                     "goodsId":$this.attr("goodsId"),
	                     "pageSize":$this.attr("pageSize"),
	                     "pageNo":$this.attr("pageNo")
	                     };
	 	 
	        $.post(url,param,function(data){
	        	if(typeof(data) != 'undefined' && data!=''){
	                // $("#pj-list").append(template("pj-listTmpl",data1));
	                $("#pj-list").append(template('pj-listTmpl',data));
	               
	                var dataPageNo = data.pageNo + 1;
	                var dataCount = data.count;
	              
	                $this.attr("pageNo", dataPageNo);
	                $this.attr("pageSize", data.pageSize);
	                $this.attr("count", data.count);
	                $this.attr("lastPage", data.lastPage);
	               
	                $loadMorePj.attr("pageNo",dataPageNo);
	                $loadMorePj.attr("pageSize", data.pageSize);
	                $loadMorePj.attr("count", data.count);
	                $loadMorePj.attr("lastPage", data.lastPage);
	        	}
	            
	        });
	     }
		
		
	});
	
    /**
     * 点击加载更多评价
     */
    $("#loadMorePj").on("click",function(){
    	var url = baseProject + $(this).attr("url");
        var $loadMorePj = $(this); 
        var $loadMyModal7 = $("#loadMyModal7"); 
        
        var pageNo = $loadMorePj.attr("pageNo");
        var pageSize = $loadMorePj.attr("pageSize");
        var count = $loadMorePj.attr("count");
        var lastPage = $loadMorePj.attr("lastPage");
        if(lastPage != "true" )
        {
        var param = {
                     "goodsId":$loadMorePj.attr("goodsId"),
                     "pageNo":pageNo,
                     "pageSize":pageSize
                     };
        
        $.post(url,param,function(data){
            // 回填参数修改pageNo
        	if(typeof(data) != 'undefined' && data!=''){
                $("#pj-list").append(template('pj-listTmpl',data));
                var dataPageNo = data.pageNo + 1 ;
                var dataCount = data.count;
                
                $loadMorePj.attr("pageNo", dataPageNo);
                $loadMorePj.attr("pageSize", data.pageSize);
                $loadMorePj.attr("count", data.count);
                $loadMorePj.attr("lastPage", data.lastPage);
                
                $loadMyModal7.attr("pageNo", dataPageNo);
                $loadMyModal7.attr("pageSize", data.pageSize);
                $loadMyModal7.attr("count", data.count);
                $loadMyModal7.attr("lastPage", data.lastPage);
        	}
            
        });
        }
    })
});
