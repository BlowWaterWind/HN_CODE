!function($){"use strict";$.fn.transitionEnd=function(n){function t(e){if(e.target===this)for(n.call(this,e),i=0;i<o.length;i++)r.off(o[i],t)}var o=["webkitTransitionEnd","transitionend","oTransitionEnd","MSTransitionEnd","msTransitionEnd"],i,r=this;if(n)for(i=0;i<o.length;i++)r.on(o[i],t);return this},$.support=function(){return{touch:!!("ontouchstart"in window||window.DocumentTouch&&document instanceof window.DocumentTouch)}}(),$.touchEvents={start:$.support.touch?"touchstart":"mousedown",move:$.support.touch?"touchmove":"mousemove",end:$.support.touch?"touchend":"mouseup"},$.getTouchPosition=function(n){return n=n.originalEvent||n,"touchstart"===n.type||"touchmove"===n.type||"touchend"===n.type?{x:n.targetTouches[0].pageX,y:n.targetTouches[0].pageY}:{x:n.pageX,y:n.pageY}},$.fn.scrollHeight=function(){return this[0].scrollHeight},$.fn.transform=function(n){for(var t=0;t<this.length;t++){var o=this[t].style;o.webkitTransform=o.MsTransform=o.msTransform=o.MozTransform=o.OTransform=o.transform=n}return this},$.fn.transition=function(n){"string"!=typeof n&&(n+="ms");for(var t=0;t<this.length;t++){var o=this[t].style;o.webkitTransitionDuration=o.MsTransitionDuration=o.msTransitionDuration=o.MozTransitionDuration=o.OTransitionDuration=o.transitionDuration=n}return this},$.getTranslate=function(n,t){var o,i,r,e;return void 0===t&&(t="x"),r=window.getComputedStyle(n,null),window.WebKitCSSMatrix?e=new WebKitCSSMatrix("none"===r.webkitTransform?"":r.webkitTransform):(e=r.MozTransform||r.OTransform||r.MsTransform||r.msTransform||r.transform||r.getPropertyValue("transform").replace("translate(","matrix(1, 0, 0, 1,"),o=e.toString().split(",")),"x"===t&&(i=window.WebKitCSSMatrix?e.m41:16===o.length?parseFloat(o[12]):parseFloat(o[4])),"y"===t&&(i=window.WebKitCSSMatrix?e.m42:16===o.length?parseFloat(o[13]):parseFloat(o[5])),i||0},$.requestAnimationFrame=function(n){return window.requestAnimationFrame?window.requestAnimationFrame(n):window.webkitRequestAnimationFrame?window.webkitRequestAnimationFrame(n):window.mozRequestAnimationFrame?window.mozRequestAnimationFrame(n):window.setTimeout(n,1e3/60)},$.cancelAnimationFrame=function(n){return window.cancelAnimationFrame?window.cancelAnimationFrame(n):window.webkitCancelAnimationFrame?window.webkitCancelAnimationFrame(n):window.mozCancelAnimationFrame?window.mozCancelAnimationFrame(n):window.clearTimeout(n)},$.fn.join=function(n){return this.toArray().join(n)}}($);