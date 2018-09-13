function list_oil(){


 try{
  var list=document.getElementById("list_oil").getElementsByTagName("li");
 }catch (e){
  console.log(e);
 }
for(var i=0;i<list.length;i++){
list[i].onclick=function(){change(this);}
}
function change(obj){
for(var i=0;i<list.length;i++)
{
if(list[i]==obj){
list[i].className="on";
}
else{
list[i].className="";


}

}

}
}
 function list_oil_1(){

var list=document.getElementById("list_oil_1").getElementsByTagName("li");
for(var i=0;i<list.length;i++){
list[i].onclick=function(){change(this);}
}
function change(obj){
for(var i=0;i<list.length;i++)
{
if(list[i]==obj){
list[i].className="on";
}
else{
list[i].className="";


}

}

}
}
window.onload = function() {
list_oil()
list_oil_1();
}


