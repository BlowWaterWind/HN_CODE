/**
 * @Company  亚信南京
 * @Copyright  版权
 * @author maohui
 * @date 2016-8-11
 * @description 购机券使用专区JS
**/

(function(doc, win) {
	//根据屏幕尺寸自动调整根元素字体大小
	var docEl = doc.documentElement,
		resizeEvt = 'orientationchange' in window ? 'orientationchange' : 'resize', //监听屏幕变化
		recalc = function() {
			var clientWidth = docEl.clientWidth;
			if (!clientWidth) {return;}
			docEl.style.fontSize = 10 * (clientWidth / 320) + 'px'; //以320宽，10px为基准
		};
	if (!doc.addEventListener) {return;}
	win.addEventListener(resizeEvt, recalc, false);
	doc.addEventListener('DOMContentLoaded', recalc, false);

})(document, window);