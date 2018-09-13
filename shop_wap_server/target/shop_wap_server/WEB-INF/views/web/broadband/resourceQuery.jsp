<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <%@include file="/WEB-INF/views/include/head.jsp" %>
    <link href="${ctxStatic}/css/swiper.min.css" rel="stylesheet" type="text/css"/>
    <link href="${ctxStatic}/css/main.css" rel="stylesheet" type="text/css"/>
    <link href="${ctxStatic}/css/wt-sub.css" rel="stylesheet" type="text/css"/>
    <link href="${ctxStatic}/css/oil.css" rel="stylesheet" type="text/css">
    <link href="${ctxStatic}/css/kd.css" rel="stylesheet" type="text/css"/>
    <link href="${ctxStatic}/css/media-style.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${ctxStatic}/js/load.js"></script>
    <title>湖南移动网上营业厅</title>
</head>

<body>
<%--<div class="top container">
    <div class="header sub-title"><a class="icon-left" href="javascript:void(0);" onclick="window.history.back()"></a>
        <h1>资源查询</h1>
    </div>
</div>--%>
<c:set value="资源查询" var="titleName" ></c:set>
<%@ include file="/WEB-INF/views/include/broadBandHead.jsp"%>
<form  id="form1" name="form1" method="post">
    <input type="hidden" id="EPARCHY_CODE" name="EPARCHY_CODE"  />
    <input type="hidden" id="MAX_WIDTH" name="MAX_WIDTH"  />
    <input type="hidden" id="lastAddress" name="lastAddress"/>
    <input type="hidden" id="form1_freePort" name="form1_freePort"/>
    <input type="hidden" id="form1_houseCode" name="form1_houseCode"/>
    <input type="hidden" id="form1_coverType" name="form1_coverType"/>

</form>
<div class="container bg-gray hy-tab">
    <div class="wf-list tab-con">
        <div class="tab-tit font-red">请点选您所在的地市和区域，然后在查询框内输入您的地址</div>
        <!--地市选择 start-->
        <ul class="change-list">
            <li>
                <label>地&emsp;&emsp;区：</label>
                <div class="right-td"> <span class="td-fr"><i class="select-arrow"></i>
          <select id="city" class="set-sd">
              <%--<option value="长沙市">长沙市</option>--%>
              <c:forEach items="${cityNames}" var="cityName">
                  <option value="${cityName.key}" <c:if test="${cityName.key==cityCode}"> selected</c:if>  >${cityName.value}</option>
              </c:forEach>
          </select>
          </span> <span class="td-fr"> <i class="select-arrow"></i>
          <select class="set-sd city-area" id="county">
              <%--<option value="岳麓区">岳麓区</option>--%>
          </select>
          </span></div>
            </li>
            <li style="border:none;" class="change-cit">
                <label>在所选城市区域中搜索：</label>
                <input id="communityAddress" type="text" class="form-control flip" value="请输入小区、楼盘、写字楼" readonly="readonly" onfocus="this.blur()" />
            </li>
        </ul>
        <!--地市选择 end-->
    </div>
</div>
<!--搜索地址 start-->
<div class="site-search panel" style="display:none">
    <div class="top container">
        <div class="header site-full sub-title"><a class="icon-left" href="javascript:void(0)" onclick="window.history.back()"></a>
            <div class="top-search list-search fit-ipnut">
                <input type="text" id="txt" name="txt" class="form-control form-fr" placeholder="请输入地址"/>
                <a href="#" class="icon-search"  ></a></div>
        </div>
    </div>
    <!--没资源 start-->
    <div class="mzy-con hide"><!--没资源时去掉hide-->
        <img src="${ctxStatic}/images/error.png"/>
        <div class="mzy-text">
            <p>尊敬的用户，非常抱歉！</p>
            <p>您所在区域宽带资源还在建设中，</p>
            <p>有宽带资源后，我们会第一时间通过短信告知您！</p>
        </div>
    </div>
    <!--没资源 end-->
    <!--展示地址信息 start-->
    <ul class="container adress-list">
        <%-- <li>
             <label><span class="adr-xz">
         <input type="radio" name="radio"/>
         </span><span class="adr-text">长沙岳麓区麓谷街道办事处桐梓坡西路(铁通光改)小区桐657#外墙栋3单元1层102号</span></label>
         </li>--%>
    </ul>
    <!--展示地址信息 end-->
    <div class="fix-br container fix-top fix-fb">
        <div class="affix container foot-menu">
                <div class="container form-group tj-btn"> <a  href="##" id="queryCommit" class="btn btn-gray" disabled="disabled">确定</a> <a id="queryCancel" class="btn btn-gray site-close" href="##">取消</a> </div>
        </div>
    </div>
</div>
<!--搜索地址 end-->
<script type="text/javascript" src="${ctxStatic}/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/nav.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/public.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/rem.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/tab.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/modal.js"></script>
<script type="text/javascript">

    var addressQuery =
    {
        cityName: "长沙", //市
        cityCode: '${cityCode}'||"0731", //市区域编码
        cityArea: "天心", //区
        communityAddressName: "", //小区
        buildingName: "", //楼栋
        keyWord: "", //关键字
        lastAddress: "", //最终地址
        maxWidth: "", //最大带宽

        /**
         * 查询城市区域
         * @param cityCode 市区域编码
         */
        qryCityArea: function (cityCode) {
            $.getJSON('${ctx}/bandResourceQuery/qryAddressCityName', {
                        'cityCode': cityCode || '0731'
                    }, function (e) {
                        if (e.respDesc == "OK!") //接口请求成功
                        {
                            $('.city-area').html("");
                            var ADDRESS_INFO = e.result[0].ADDRESS_INFO;
                            $.each(ADDRESS_INFO, function (i, item) {
                                $('.city-area').append("<option>" + item.CITY_NAME + "</option>");
                            });
                        } else {
                            alert("系统异常，请稍后再试");
                        }
                    }
            );
        },

        /**
         * 查询最终地址
         * 通过参数个数进行重载
         */
        qryLastAddress: function () {
            if (arguments.length == 1) {
                addressQuery.qryLastAddressByPath(arguments[0]);
            }
            if (arguments.length == 3) {
                addressQuery.qryLastAddressByKeyWords(arguments[0], arguments[1], arguments[2]);
            }
        },

        /**
         * 通过楼栋查询最终地址
         * @param ADDRESS_PATH 楼栋名称
         */
        qryLastAddressByPath: function (ADDRESS_PATH) {
            $.getJSON('${ctx}/bandResourceQuery/qryAddressName', {
                        'addressKeyString': ADDRESS_PATH
                    }, function (e) {
                        if (e.respDesc == "OK!") //接口请求成功
                        {
                            var ADDRESS_INFO = e.result[0].ADDRESS_INFO;
                            if (ADDRESS_INFO.length == 0) {
//                                $("#lastAddress").append("该地址暂未开通宽带资源办理");
                                return;
                            }
                            $.each(ADDRESS_INFO, function (i, item) {
                                $("#lastAddress").append("<li last-address-building=" + item.HOUSE_CODE + " coverType=" + item.COVER_TYPE + ">" + item.ADDRESS_NAME + "</li>");
                            });
                        } else {
                            alert("系统异常，请稍后再试");
                        }
                    }
            );
        },

        /**
         * 通过关键字查询最终地址
         *
         * @param cityName 市
         * @param cityArea 区
         * @param keyWords 查询关键字
         */
        qryLastAddressByKeyWords: function (cityName, cityArea, keyWords) {
            if (!$.trim(addressQuery.keyWord)) //空字符串
            {
                return;
            }
            $.getJSON('${ctx}/bandResourceQuery/qryAddressCommunityName', {
                        'cityName': encodeURI(cityName,'UTF-8'),
                        'cityArea': encodeURI(cityArea,'UTF-8'),
                        'keyWords': encodeURI(keyWords,"UTF-8")
                    }, function (e) {
                        if (e.respDesc == "OK!") //接口请求成功
                        {
                            var ADDRESS_INFO = e.result[0].ADDRESS_INFO;
                            if (ADDRESS_INFO.length == 0) {
                                $(".adress-list").html("").append("该地址暂未开通宽带资源办理");
                                return;
                            }
                            $.each(ADDRESS_INFO, function (i, item) {
                                console.log("coumunity:",item);
                                $(".adress-list").append("<li><label><span coverType=" + item.COVER_TYPE + " address-path=" + item.ADDRESS_PATH + " class='adr-xz'  address-community="+item.COMMUNITY_NAME+" onclick=searchBuilding2(this)><input type='radio' name='radio_address'></span>" +
                                        "<span coverType=" + item.COVER_TYPE + " address-path=" + item.ADDRESS_PATH + " class='adr-text' address-community="+item.COMMUNITY_NAME+"  onclick=searchBuilding2(this)>" + item.COMMUNITY_NAME +  "</span></label></li>");
                            });
                            if (typeof(dcsPageTrack)=="function"){ dcsPageTrack ("WT.si_n","单宽带或者和家庭",1, "WT.si_x","选择地址",1);}
                            $('#queryCommit').attr("disabled","disabled");
                            $('#queryCommit').attr("class","btn btn-gray");
                        } else {
                            alert("系统异常，请稍后再试");
                        }
                    }
            );
        },

        /**
         * 资源覆盖能力查询
         * @param addrCode 标准地址code
         * @param coverType 覆盖类型
         */
        coverInfo: function (addrCode, coverType) {
        	if(coverType == "学校FTTB" || coverType == "学校LAN" || coverType == "合作宽带" || coverType == "农村无线宽带"
    	    	|| coverType == "铁通FTTH" || coverType == "铁通FTTB" || coverType == "铁通学校FTTH" || coverType == "铁通ADSL"
    	    		|| coverType == "铁通学校FTTH" || coverType == "铁通学校FTTB" || coverType == "铁通学校LAN" || coverType == "铁通合作宽带"
    	    			|| coverType == "校园AP宽带"){
    	    	return;
    	    }
        	
            $.getJSON('${ctx}/bandResourceQuery/coverInfo', {
                        'addressCode': addrCode,
                        'coverType': encodeURI(coverType,"UTF-8")
                    }, function (e) {
                        if (e.result)  //接口请求成功
                        {
                            var freePort = e.result[0].FREE_PORT_NUM;
                            var MAX_WIDTH = e.result[0].FREE_PORT_NUM;
                            var cityCode =  addressQuery.cityCode
                            if (freePort && MAX_WIDTH && freePort > 0 && MAX_WIDTH > 0) {  //可以办理宽带
                                console.log("空闲端口数：" + freePort);
                                console.log("最大带宽：" + MAX_WIDTH);
                                addressQuery.queryBandGoods(MAX_WIDTH,cityCode);
                            } else {
//                                $("#recommendPlanList").html("<h1>该地址暂时无法办理宽带</h1>");
                                alert("该地址暂时无法办理宽带");
                            }
                        } else {
                            alert("系统异常，请稍后再试");
                        }
                    }
            );
        },

//       $("#recommendPlanList").load("queryBandGoods", {'MAX_WIDTH': addressQuery.maxWidth, 'EPARCHY_CODE': addressQuery.cityCode});

        <%--queryBandGoods: function (maxWidth, cityCode) {--%>

            <%--$.ajax(--%>
                    <%--{--%>
                        <%--type: 'post',--%>
                        <%--url: '${ctx}/bandResourceQuery/queryBandGoods',--%>
                        <%--data: {'MAX_WIDTH': maxWidth, 'EPARCHY_CODE': cityCode, 'lastAddress': addressQuery.lastAddress},--%>
                        <%--dataType:'json',--%>
                        <%--success:function(e)--%>
                        <%--{--%>

                            <%--console.log("queryBandGoods success");--%>
                        <%--}--%>
                    <%--}--%>
            <%--)--%>
        <%--}--%>
    };

    $(function () {
        addressQuery.qryCityArea(addressQuery.cityCode); //默认初始化加载

        $("#city").live('change', function () {  //市级别选择事件监听
            $("#txt").val("");
            $(".adress-list").html("");
            addressQuery.cityCode = $(this).val();
            addressQuery.cityName = $(this).find("option:selected").text();
            addressQuery.qryCityArea(addressQuery.cityCode);
        });

        $(".city-area").live('change', function () {  //区域级别选择事件监听
            $("#txt").val("");
            $(".adress-list").html("");
            addressQuery.cityArea = $(this).val();
        });

        $("#txt").live('input propertychange', function () {  //关键字级别选择事件监听
            addressQuery.keyWord = $(this).val();
            addressQuery.cityArea = $(".city-area").val();
            $(".adress-list").html(""); //清空无效信息
            addressQuery.qryLastAddress(addressQuery.cityName, addressQuery.cityArea, addressQuery.keyWord);//关键字查询
        });

        $("#queryCommit").click(function(){  //地址信息选中事件监听
            var address = $('input[name="radio_address"]:checked ').parent().nextAll("span[class='adr-text']").text();
            var houseCode = $('input[name="radio_address"]:checked ').parent().nextAll("span[class='adr-text']").attr("last-address-building");
            var coverType = $('input[name="radio_address"]:checked ').parent().nextAll("span[class='adr-text']").attr("coverType");
            var COMMUNITY_NAME  = $('input[name="radio_address"]:checked ').parent().nextAll("span[class='adr-text']").attr("address-community");
            var city = $("#city").val();
            var county = $("#county").val();
            console.log("city;",city);
            console.log("county:",county);
            console.log("address:",address);
            console.log("houseCode:",houseCode);
//            showLoadPop("正在建设中...");
            //检查能装多少M的宽带
            showLoadPop("正在查询资源...");
            window.location.href=ctx+"bandResourceQuery/queryWapPackage?addressPath="+encodeURI(encodeURI(address,"UTF-8"))+"&communityName="+encodeURI(encodeURI(COMMUNITY_NAME,"UTF-8"))+"&city="+city+"&county="+encodeURI(encodeURI(county,"UTF-8"));
//            $.getJSON(ctx+'bandResourceQuery/coverInfo', {
//                        'addressCode': houseCode,
//                        'coverType': coverType
//                    }, function (e) {
//                        if (e.result)  //接口请求成功
//                        {
//                            var freePort = e.result[0].FREE_PORT_NUM;
//                            var maxWidth = e.result[0].MAX_WIDTH;
//                            var cityCode =  addressQuery.cityCode
//                            if (freePort && maxWidth && freePort > 0 && maxWidth > 0) {  //可以办理宽带
//                                $("#MAX_WIDTH").val(maxWidth);
//                                $("#form1_freePort").val(freePort);
//                                $("#form1_coverType").val(coverType);
//                                $("#EPARCHY_CODE").val(cityCode);
//                                $("#lastAddress").val(address);
//                                $("#form1_houseCode").val(houseCode);
//                                $(".panel").toggle();
//                                $("#form1").attr("action", ctx + "/bandResourceQuery/queryBandGoods");
//                                $("#form1").submit();
//                            }
//                            else {
//                                alert("选择的地址暂无宽带资源,请重新选择!");
//                            }
//                        } else {
//                            alert("系统异常，请稍后再试");
//                        }
//                //hideLoadPop();
//                    }
//            );
        });



    });

    /**
     * 查询楼栋
     * @param residence 小区名
     */
    function searchBuilding2(obj){
        $(obj).addClass('cur').siblings().removeClass('cur');
        var residence = $(obj).attr("address-path");
        var COMMUNITY_NAME = $(obj).attr("address-community");
        $("#residential").val(residence);

        $.getJSON(ctx+'bandResourceQuery/qryAddressBuildingName', {
                    'communityAddressName': encodeURI(residence,"UTF-8")
                }, function (e) {
                    if (e.respDesc == "OK!") //接口请求成功
                    {
                        $(".adress-list").html("");
                        var ADDRESS_INFO = e.result[0].ADDRESS_INFO;
                        if (ADDRESS_INFO.length == 0) {
                            $(".adress-list").append("该地址暂未开通宽带办理");
                            return;
                        }
                        $.each(ADDRESS_INFO, function (i, item) {
                            $(".adress-list").append("<li><label><span coverType=" + item.COVER_TYPE + " address-path-building=" + item.ADDRESS_PATH + " class='adr-xz'  address-community="+COMMUNITY_NAME+" onclick=chooseBuilding2(this)>" +
                                    "<input type='radio' name='radio_address'></span>" +
                                    "<span coverType=" + item.COVER_TYPE + " address-path-building=" + item.ADDRESS_PATH + " class='adr-text' address-community="+COMMUNITY_NAME+"  onclick=chooseBuilding2(this)>" + item.BUILDING_NAME +  "</span>" +
                                    "</label></li>");
                        });
                    }
                }
        );
    }
    /**
     * 选择楼栋
     * @param obj
     * @returns
     */
    function chooseBuilding2(obj){
        var ADDRESS_PATH = $(obj).attr("address-path-building");
        var COMMUNITY_NAME = $(obj).attr("address-community");
        $.getJSON(ctx+'bandResourceQuery/qryAddressName', {
                    'addressKeyString':  encodeURI(ADDRESS_PATH,"UTF-8")
                }, function (e) {
                    if (e.respDesc == "OK!") //接口请求成功
                    {
                        $(".adress-list").html("");
                        var ADDRESS_INFO = e.result[0].ADDRESS_INFO;
                        if (ADDRESS_INFO.length == 0) {
                            $(".adress-list").append("该地址暂未开通宽带资源办理");
                            return;
                        }
                        $.each(ADDRESS_INFO, function (i, item) {
                            $(".adress-list").append("<li><label><span coverType=" + item.COVER_TYPE + " last-address-building=" + item.HOUSE_CODE + " address-community="+COMMUNITY_NAME+" class='adr-xz' onclick=chooseLastAddress2(this)><input type='radio' name='radio_address'></span><span coverType=" + item.COVER_TYPE + " last-address-building=" + item.HOUSE_CODE + " class='adr-text'  address-community="+COMMUNITY_NAME+" onclick=chooseLastAddress2(this)>" + item.ADDRESS_NAME +  "</span></label></li>");
                        });
                    } else {
                        alert("系统异常，请稍后再试");
                    }
                }
        );

    }
    function chooseLastAddress2(obj){
        $('#queryCommit').removeAttr("disabled");
        $('#queryCommit').attr("class","btn btn-blue");
    }

</script>
</body>
</html>
