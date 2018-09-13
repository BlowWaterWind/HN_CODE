<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<div class="add-list">
    <label>详细地址：</label>
    <div class="add-box">
        <input type="hidden" id="houseCode" name="houseCode"/>
        <input type="text" class="form-control" name="address" id="address"
               placeholder="选择详细地址">
    </div>
</div>


<script>
    /*展示地址搜索框*/
    $("#address").bind("click", function () {
        var addrDiv = '<div id="div_searchAddress" class="site-search panel">';
        addrDiv+='<div class="top container">';
        addrDiv+='<div class="header site-full sub-title"><a class="icon-left site-close" id="a_retSearch"></a>';
        addrDiv+='<div class="top-search list-search fit-ipnut">';
        addrDiv+='<input type="text" id="queryAddress" name="queryAddress" class="form-control form-fr"';
        addrDiv+=' placeholder="关键字搜索参考：请输入小区或周边标志性建筑" autofocus="autofocus"/>';
        addrDiv+='<a href="javascript:;" class="icon-search" id="a_search"></a></div>';
        addrDiv+=' </div>';
        addrDiv+='</div>';
        addrDiv+='<ul class="container adress-list">';
        addrDiv+='</ul>';
        addrDiv+='<div class="fix-br container fix-top fix-fb">';
        addrDiv+='<div class="affix container foot-menu">';
        addrDiv+=' <div class="container form-group tj-btn"><a href="##" id="queryCommit" class="btn btn-gray"';
        addrDiv+='disabled="disabled">确定</a> <a id="queryCancel"';
        addrDiv+=' class="btn btn-gray site-close"';
        addrDiv+='  href="##">取消</a></div>';
        addrDiv+=' </div> </div> </div>';
        $("body").append(addrDiv);
//        $(".panel").toggle();


        $("#a_retSearch").click(function () {
            $("#div_searchAddress").remove();
        });
        $("#queryCancel").click(function () {
            $("#div_searchAddress").remove();
        });
        $("#queryAddress").bind("keyup", function () {
            var cityName = $("#memberRecipientCity").find("option:selected").text();
            var cityArea = $("#memberRecipientCounty").find("option:selected").text();
            var keyWords = $("#queryAddress").val();
            if (!$.trim(keyWords)) //空字符串
            {
                return;
            }
            $.getJSON(ctx + 'bandResourceQuery/qryAddressCommunityName', {
                        'cityName': encodeURI(cityName, "UTF-8"),
                        'cityArea': encodeURI(cityArea, "UTF-8"),
                        'keyWords': encodeURI(keyWords, "UTF-8")
                    }, function (e) {
                        if (e.respDesc == "OK!") //接口请求成功
                        {
                            $(".adress-list").html("");
                            var ADDRESS_INFO = e.result[0].ADDRESS_INFO;
                            if (ADDRESS_INFO.length == 0) {
                                $(".adress-list").append("<li>该地址暂无宽带办理信息</li>");
                                return;
                            }
                            $.each(ADDRESS_INFO, function (i, item) {
                                $(".adress-list").append("<li><label><span  coverType=" + item.COVER_TYPE + " community_code=" +item.COMMUNITY_CODE+" address-path=" + item.ADDRESS_PATH + " onclick=searchBuilding2(this) class='adr-xz'><input type='radio' name='radio_address'></span><span coverType=" + item.COVER_TYPE + " community_code=" +item.COMMUNITY_CODE+" address-path=" + item.ADDRESS_PATH + " class='adr-text'  onclick=searchBuilding2(this)>" + item.COMMUNITY_NAME + "</span></label></li>");
                            });
                            if (typeof(dcsPageTrack) == "function") {
                                dcsPageTrack("WT.si_n", "单宽带或者和家庭", 1, "WT.si_x", "选择地址", 1);
                            }
                            $('#queryCommit').attr("disabled", "disabled");
                            $('#queryCommit').attr("class", "btn btn-gray");
                        } else {
                            showAlert("系统异常，请稍后再试");
                        }
                    }
            );

        });

        /*最终地址查询*/
        $("#a_search").click(function () {
            var cityName = $("#memberRecipientCity").find("option:selected").text();
            var cityArea = $("#memberRecipientCounty").find("option:selected").text();
            var keyWords = $("#queryAddress").val();
            if (!$.trim(keyWords)) //空字符串
            {
                return;
            }
            $.getJSON(ctx + 'bandResourceQuery/qryAddressCommunityName', {
                        'cityName': encodeURI(cityName, "UTF-8"),
                        'cityArea': encodeURI(cityArea, "UTF-8"),
                        'keyWords': encodeURI(keyWords, "UTF-8")
                    }, function (e) {
                        if (e.respDesc == "OK!") //接口请求成功
                        {
                            $(".adress-list").html("");
                            var ADDRESS_INFO = e.result[0].ADDRESS_INFO;
                            if (ADDRESS_INFO.length == 0) {
                                $(".adress-list").html("").append("该地址暂未开通宽带资源办理");
                                return;
                            }
                            $.each(ADDRESS_INFO, function (i, item) {
                                $(".adress-list").append("<li><label><span  coverType=" + item.COVER_TYPE + " community_code=" +item.COMMUNITY_CODE+" address-path=" + item.ADDRESS_PATH + " onclick=searchBuilding2(this) class='adr-xz'><input type='radio' name='radio_address'></span><span coverType=" + item.COVER_TYPE + " community_code=" +item.COMMUNITY_CODE+" address-path=" + item.ADDRESS_PATH + " class='adr-text'  onclick=searchBuilding2(this)>" + item.COMMUNITY_NAME + "</span></label></li>");
                            });
                            if (typeof(dcsPageTrack) == "function") {
                                dcsPageTrack("WT.si_n", "单宽带或者和家庭", 1, "WT.si_x", "选择地址", 1);
                            }
                            $('#queryCommit').attr("disabled", "disabled");
                            $('#queryCommit').attr("class", "btn btn-gray");
                        } else {
                            showAlert("系统异常，请稍后再试");
                        }
                    }
            );

        });

        /*选择最终地址*/
        $("#queryCommit").click(function () {
            var address = $('input[name="radio_address"]:checked ').parent().nextAll("span[class='adr-text']").text();
            var houseCode = $('input[name="radio_address"]:checked ').parent().nextAll("span[class='adr-text']").attr("last-address-building");
            var coverType = $('input[name="radio_address"]:checked ').parent().nextAll("span[class='adr-text']").attr("coverType");
            if (coverType == "学校FTTB" || coverType == "学校LAN" || coverType == "合作宽带" || coverType == "农村无线宽带"
                    || coverType == "铁通FTTH" || coverType == "铁通FTTB" || coverType == "铁通学校FTTH" || coverType == "铁通ADSL"
                    || coverType == "铁通学校FTTH" || coverType == "铁通学校FTTB" || coverType == "铁通学校LAN" || coverType == "铁通合作宽带"
                    || coverType == "校园AP宽带") {
                $("#form1_eparchyCode").val($("#memberRecipientCity").val());
                $("#form1_county").val($("#memberRecipientCounty").val());
                $("#form1_addressName").val(address);
                $("#form1_houseCode").val(houseCode);
                $("#form1_coverType").val(coverType);
                $("#address").val(address);
                $("#houseCode").val(houseCode);
                $("#eparchyCode").val($("#memberRecipientCity").val());
                $("#county").val($("#memberRecipientCounty").val());
                $("#addressName").val(address);
                $("#coverType").val(coverType);
                $("#div_searchAddress").remove();
                communitySearch(houseCode)
                return;
            }

            showLoadPop("正在查询资源...");
            //检查能装多少M的宽带
            $.getJSON(ctx + 'bandResourceQuery/coverInfo', {
                        'addressCode': houseCode,
                        'coverType': encodeURI(coverType, "UTF-8")
                    }, function (e) {
                        if (e.result)  //接口请求成功
                        {
                            var freePort = e.result[0].FREE_PORT_NUM;
                            var maxWidth = e.result[0].MAX_WIDTH;
                            if (freePort && maxWidth && freePort > 0 && maxWidth > 0) {  //可以办理宽带
                                $("#form1_maxWidth").val(maxWidth);
                                $("#form1_freePort").val(freePort);
                                $("#form1_eparchyCode").val($("#memberRecipientCity").val());
                                $("#form1_county").val($("#memberRecipientCounty").val());
                                $("#form1_addressName").val(address);
                                $("#form1_houseCode").val(houseCode);
                                $("#form1_coverType").val(coverType);
                                $("#address").val(address);
                                $("#houseCode").val(houseCode);
                                $("#eparchyCode").val($("#memberRecipientCity").val());
                                $("#county").val($("#memberRecipientCounty").val());
                                $("#addressName").val(address);
                                $("#coverType").val(coverType);
                                $("#maxWidth").val(maxWidth);
                                $("#freePort").val(freePort);
                                $("#div_searchAddress").remove();

                            }
                            else {
                                showAlert("选择的地址暂无宽带资源,请重新选择!");
                            }
                        } else {
                            alert("系统异常，请稍后再试");
                        }
                        hideLoadPop();
                    }
            );
            //查询地址城乡类型
            communitySearch(houseCode);
        });
    });

    function communitySearch(houseCode){
        //查询地址城乡类型
        $.ajax({
            url:ctx+"newHeBand/queryCommunityType",
            data:{
                houseCode:houseCode
            },
            success:function(data){
                if(data.respCode=='0'){
                    $("#form1_communityType").val(data.data);
                }else{
                    showAlert(data.message);
                }
            },
            error:function(data){
                showAlert("城乡类型查询失败!");
            }
        });
    }
    /**
     * 查询楼栋
     * @param residence 小区名
     */
    function searchBuilding2(obj) {
        $(obj).addClass('cur').siblings().removeClass('cur');
        var residence = $(obj).attr("address-path");
        var community_code=$(obj).attr("community_code");
        $("#residential").val(residence);

        $.getJSON(ctx + 'bandResourceQuery/qryAddressBuildingName', {
                    'communityAddressName': encodeURI(residence, "UTF-8")
                }, function (e) {
                    if (e.respDesc == "OK!") //接口请求成功
                    {
                        $(".adress-list").html("");
                        var ADDRESS_INFO = e.result[0].ADDRESS_INFO;
                        if (ADDRESS_INFO.length == 1 && ADDRESS_INFO[0].BUILDING_NAME === "") {
                            //$(".adress-list").append("该地址暂未开通宽带办理");
                            showAlert("该地址暂未开通宽带资源办理");
                            return;
                        }
                        $.each(ADDRESS_INFO, function (i, item) {

                            $(".adress-list").append("<li><label>" +
                                    "<span coverType=" + item.COVER_TYPE +" buildingCode="+item.BUILDING_CODE+" community_code="+community_code+ " address-path-building=" + item.ADDRESS_PATH + " class='adr-xz' onclick=chooseBuilding2(this)><input type='radio' name='radio_address'></span>" +
                                    "<span coverType=" + item.COVER_TYPE +" buildingCode="+item.BUILDING_CODE+" community_code="+community_code+ " address-path-building=" + item.ADDRESS_PATH + " class='adr-text'  onclick=chooseBuilding2(this)>" + item.BUILDING_NAME + "</span>" +
                                    "</label></li>");
                        });
                    }
                }
        );
    }

    /**
     * 选择楼栋，查询单元
     * @param obj
     * @returns
     */
    function chooseBuilding2(obj) {
        var ADDRESS_PATH = $(obj).attr("address-path-building");
        var eparchyCode = $("#memberRecipientCity").val();
        var communityCode = $(obj).attr("community_code");
        var buildingCode = $(obj).attr("buildingCode");
        $.getJSON(ctx + 'bandResourceQuery/qryAddressUnit', {
                    'eparchyCode': eparchyCode,
                    'buildingCode': buildingCode,
                    'comunityCode': communityCode
                }, function (e) {
                    if (e.respDesc == "OK!") //接口请求成功
                    {
                        $(".adress-list").html("");
                        var ADDRESS_INFO = e.result[0].ADDRESS_UNIT_INFO;
                        if (ADDRESS_INFO.length == 1 && ADDRESS_INFO[0].UNIT_NAME == "") {
                            //$(".adress-list").append("该地址暂未开通宽带资源办理");
                            showAlert("该地址暂未开通宽带资源办理");
                            return;
                        }
                        $.each(ADDRESS_INFO, function (i, item) {
                            $(".adress-list").append("<li><label>" +
                                    "<span  unitCode=" + item.UNIT_CODE  + " community_code="+communityCode+ " class='adr-xz' onclick=chooseUnit(this)><input type='radio' name='radio_address' ></span>" +
                                    "<span unitCode=" + item.UNIT_CODE +" community_code="+communityCode+  " class='adr-text' onclick=chooseUnit(this)>" + item.UNIT_NAME + "</span>" +
                                    "</label></li>");
                        });
                    } else {
                        showAlert("系统异常，请稍后再试");
                    }
                }
        );

    }


    /**
     * 选择单元,查询楼层
     * @param obj
     * @returns
     */
    function chooseUnit(obj) {
        var eparchyCode = $("#memberRecipientCity").val();
        var communityCode = $(obj).attr("community_code");
        var unitCode = $(obj).attr("unitCode");
//        var unitCode = '-109585671';
        $.getJSON(ctx + 'bandResourceQuery/qryAddressFloor', {
                    'eparchyCode': eparchyCode,
                    'unitCode': unitCode,
                    'comunityCode': communityCode
                }, function (e) {
                    if (e.respDesc == "OK!") //接口请求成功
                    {
                        $(".adress-list").html("");
                        var ADDRESS_INFO = e.result[0].ADDRESS_FLOOR_INFO;
                        if (ADDRESS_INFO.length == 1 && ADDRESS_INFO[0].FLOOR_NAME == "") {
                            //$(".adress-list").append("该地址暂未开通宽带资源办理");
                            showAlert("该地址暂未开通宽带资源办理");
                            return;
                        }
                        $.each(ADDRESS_INFO, function (i, item) {
                            $(".adress-list").append("<li><label>" +
                                    "<span  floorCode=" + item.FLOOR_CODE + " community_code="+communityCode+ " class='adr-xz' onclick=chooseFloor(this)><input type='radio' name='radio_address' ></span>" +
                                    "<span floorCode=" + item.FLOOR_CODE +" community_code="+communityCode+  " class='adr-text' onclick=chooseFloor(this)>" + item.FLOOR_NAME + "</span>" +
                                    "</label></li>");
                        });
                    } else {
                        showAlert("系统异常，请稍后再试");
                    }
                }
        );

    }


    /**
     * 选择楼层,查询房号
     * @param obj
     * @returns
     */
    function chooseFloor(obj) {
        var eparchyCode = $("#memberRecipientCity").val();
        var communityCode = $(obj).attr("community_code");
        var floorCode = $(obj).attr("floorCode");
//        var floorCode = '-109585672';
        $.getJSON(ctx + 'bandResourceQuery/qryAddressHouseCode', {
                    'eparchyCode': eparchyCode,
                    'floorCode': floorCode,
                    'comunityCode': communityCode
                }, function (e) {
                    if (e.respDesc == "OK!") //接口请求成功
                    {
                        $(".adress-list").html("");
                        var ADDRESS_INFO = e.result[0].ADDRESS_HOUSE_INFO;
                        if (ADDRESS_INFO.length == 1 && ADDRESS_INFO[0].HOUSE_NAME == "") {
                            //$(".adress-list").append("该地址暂未开通宽带资源办理");
                            showAlert("该地址暂未开通宽带资源办理");
                            return;x
                        }
                        $.each(ADDRESS_INFO, function (i, item) {
                            $(".adress-list").append("<li><label>" +
                                    "<span  last-address-building=" + item.HOUSE_CODE+ " coverType="+item.COVER_TYPE + " community_code="+communityCode+ " class='adr-xz' onclick=chooseLastAddress2(this)><input type='radio' name='radio_address' ></span>" +
                                    "<span last-address-building=" + item.HOUSE_CODE+ " coverType="+item.COVER_TYPE +" community_code="+communityCode+  " class='adr-text' onclick=chooseLastAddress2(this)>" + item.ADDRESS_NAME + "</span>" +
                                    "</label></li>");
                        });
                    } else {
                        showAlert("系统异常，请稍后再试");
                    }
                }
        );

    }

    function chooseLastAddress2(obj) {
        $('#queryCommit').removeAttr("disabled");
        $('#queryCommit').attr("class", "btn btn-blue");
    }
</script>