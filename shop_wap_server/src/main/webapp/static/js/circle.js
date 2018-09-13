var pie = {
    run:function(opts){
        if(!opts.container) throw new Error('must be element container.');
		opts.pie1 = opts.pie1 || "div.pie1";
		opts.pie2 = opts.pie2 || "div.pie2";
        var pie1 = $(opts.container).find(opts.pie1), pie2 = $(opts.container).find(opts.pie2);
        var percent = opts.percent || 0;
        var step = opts.step || 3;
        var delay = opts.delay || 50;
        var callback = opts.callback || $.noop;
        var i = 0, rage = 360 * percent;
        var djs = function(){
            i = i + step;
            if(i <= rage){
                if(i <= 180){
                    if((180 - i) < step){ i = 180; }
					pie1.css("transform","rotate(" + i + "deg)");
                    pie1.css("-ms-transform","rotate(" + i + "deg)");
					 pie1.css("-webkit-transform","rotate(" + i + "deg)");
					  
                } else {
                    if((rage - i) < step){ i = rage; }
					 pie2.css("transform","rotate(" + (i-180) + "deg)");
                    pie2.css("-ms-transform","rotate(" + (i-180) + "deg)");
					pie2.css("-webkit-transform","rotate(" + (i-180) + "deg)");
					
					
                }
                callback(i, rage);
                setTimeout(djs, 10);
            }
        };
        djs();
    }
};
pie.run({
    container:".circle-rose",
    percent:$(".circle-rose .circle b").text().replace("%","")/100
});
 
pie.run({
    container:".circle-blue",
    percent:$(".circle-blue .circle b").text().replace("%","")/100
});

pie.run({
    container:".circle-green",
    percent:$(".circle-green .circle b").text().replace("%","")/100
});