function loadWTScript(a, b) {
	var c = document.createElement("script");
	c.id = "wtjs";
	c.type = "text/javascript",
	c.async = true,
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
loadWTScript('/shop/static/js/qm/webtrends.js', function(){ //sdc9.js文件的相对路径
    if (typeof(_rhtag) != "undefined"){
            _rhtag.dcsid="v1/dcs5w0txb10000wocrvqy1nqm_6n1p";
            _rhtag.dcsCollect();
    }
})}

