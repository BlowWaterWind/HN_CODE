function loadWTScript(a, b) {
	var c = document.createElement("script");
	c.id = "wtjs";
	c.type = "text/javascript",
	c.async = !0,
	c.src = a,
	dcsReady(c, b),
	document.getElementsByTagName("head")[0].appendChild(c)
}
function dcsReady(a, b) {
	a.readyState ? a.onreadystatechange = function() { ("loaded" == a.readyState || "complete" == a.readyState) && (a.onreadystatechange = null, b())
	}: a.onload = function() {
		b()
	}
}
if(document.getElementById("wtjs")==null){
loadWTScript('//wap.hn.10086.cn/wap/js/sdc_wap.js', function(){ 
    if (typeof(_tag) != "undefined"){
            _tag.dcsCollect();
    }
})}