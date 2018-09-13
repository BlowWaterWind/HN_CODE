/**
 * Created by hewei on 2017/12/21/021.
 */
/**
 * 自动绑定地市归属
 */
var adrsRef  = [{"cityName":"长沙市","genCityCode":"430100","locCityCode":190001,"locEparchyCode":"0731"},
    {"cityName":"株洲市","genCityCode":"430200","locCityCode":190012,"locEparchyCode":"0733"},
    {"cityName":"湘潭市","genCityCode":"430300","locCityCode":190023,"locEparchyCode":"0732"},
    {"cityName":"衡阳市","genCityCode":"430400","locCityCode":190030,"locEparchyCode":"0734"},
    {"cityName":"邵阳市","genCityCode":"430500","locCityCode":190044,"locEparchyCode":"0739"},
    {"cityName":"岳阳市","genCityCode":"430600","locCityCode":190058,"locEparchyCode":"0730"},
    {"cityName":"张家界市","genCityCode":"430800","locCityCode":190080,"locEparchyCode":"0744"},
    {"cityName":"益阳市","genCityCode":"430900","locCityCode":190086,"locEparchyCode":"0737"},
    {"cityName":"常德市","genCityCode":"430700","locCityCode":190069,"locEparchyCode":"0736"},
    {"cityName":"娄底市","genCityCode":"431300","locCityCode":190134,"locEparchyCode":"0738"},
    {"cityName":"郴州市","genCityCode":"43100","locCityCode":190094,"locEparchyCode":"0735"},
    {"cityName":"永州市","genCityCode":"431100","locCityCode":190107,"locEparchyCode":"0746"},
    {"cityName":"怀化市","genCityCode":"431200","locCityCode":190120,"locEparchyCode":"0745"},
    {"cityName":"湘西土家族苗族自治区","genCityCode":"433100","locCityCode":190141,"locEparchyCode":"0743"}];
$(function() {
    setTimeout(function () {
        $.ajax({
            type: "POST",
            url: ctx + "simBuy/getDefaultAddressByIp",
            data:{
                ip:$("#keleyivisitorip").text()
            },
            dataType: "json",
            success: function (data) {
                console.log("映射地市编码：" + data);
                $.each(adrsRef, function (idx, obj) {
                    if (obj.genCityCode == data){
                        $("input[name='orderDetailSim.cityCode']").val(obj.locEparchyCode);//保存编码
                        $("#picker1").html(obj.cityName);  //设置中文
                        return;
                    }
                });
            },
        });
    }, 1000);
});
