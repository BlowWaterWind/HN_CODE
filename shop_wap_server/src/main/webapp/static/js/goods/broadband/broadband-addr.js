$(function() {
    initAddrDiv();//初始化地址选择栏

    $("#memberRecipientCounty").bind("change",function(){
        $("#address").val("关键字搜索参考：请输入小区或周边标志性建筑");
        $("#queryAddress").val("");
        $(".adress-list").html("");
        $("#form1_houseCode").val("");
        $('#queryCommit').attr("disabled","disabled");
        $('#queryCommit').attr("class","btn btn-gray");
    });
});

function initAddrDiv(){
    $("#broadband_addr_div").html("");

    $.getJSON(ctx+'consupostn/getAreaInfo', {}, function (e)
    {
        var cityList = e.cityList;
        var cityHtml = '';
        var countyHtml = "";
        var cityOrgId = '';

        for(var i=0;i<cityList.length;i++){
            if(cityList[i].eparchyCode == $("#eparchy_Code").val()){
                cityOrgId = cityList[i].eparchyCode;
                cityHtml += '<option value="'+cityList[i].eparchyCode+'" orgid="'+cityList[i].orgId+'">'+cityList[i].orgName+'</option>';
                break;
            }
        }
        $("#memberRecipientCity").html(cityHtml);


        $.ajax({
            url: ctx+"broadband/getCountyFromBoss",
            data: {cityCode : cityOrgId},
            dataType: "json",
            type : 'post',
            success: function(data){
                $('#memberRecipientCounty').html('');
                if(data.flag=='Y'){
                    $.each(data.orgList,function(i,obj){
                        //popHtml += "<option value='"+obj.orgId+"'>"+obj.orgName+"</option>";
                        countyHtml+="<option value='"+obj.CITY_ID+"'>"+obj.CITY_NAME+"</option>";
                    });
                    $("#memberRecipientCounty").html(countyHtml);
                }
                else {
                    showAlert("无区县信息!");
                }
            },
            error: function(e) {
                showAlert("您查询的区县不存在！");
            }
        });
    });
}