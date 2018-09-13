<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.ai.ecs.member.entity.ThirdLevelAddress" %>
<%@page import="com.ai.ecs.ecm.mall.wap.platform.utils.HtmlUtils"%>
<!DOCTYPE html>
<html>
<head>
    <meta name="WT.si_n" content="和家庭" /> 
    <meta name="WT.si_x" content="业务详情" />
    
    <title>湖南移动网上营业厅</title>
    <%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ page contentType="text/html;charset=UTF-8" trimDirectiveWhitespaces="true" %>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" name="viewport" />
<meta http-equiv="Cache-Control" content="max-age=3600;must-revalidate" />
<meta name="keywords" content="中国移动网上商城,手机,值得买,可靠,质量好,移动合约机,买手机送话费,4G手机,手机最新报价,苹果,华为,魅族" />
<meta name="description" content="中国移动网上商城,提供最新最全的移动合约机、4G手机,买手机送话费,安全可靠,100%正品保证,让您足不出户,享受便捷移动服务！" />
<script src="${ctxStatic}/js/jquery/jquery-2.1.4.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/js/jquery/jquery-migrate-1.2.1.min.js"></script>
<%--<link href="${ctxStatic}/css/main.css" rel="stylesheet" type="text/css" />--%>
<link href="${ctxStatic}/css/media-style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" >
    var ctx="${ctx}",gUrl="${gUrl}",sUrl="${sUrl}",tfsUrl="${tfsUrl}";
</script>
    <link href="${ctxStatic}/css/main.css" rel="stylesheet" type="text/css" />

    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/broadBand/wap-order.css"/>
    <script type="text/javascript" src="${ctxStatic}/ap/lib/flexible/flexible.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/goods/broadband/appointment.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/load.js"></script>
    <%@ include file="/WEB-INF/views/include/message.jsp"%>
    <script type="text/javascript">
    	var  ctx = '${ctx}';
    </script>
    
</head>
<%
  ThirdLevelAddress address=(ThirdLevelAddress)request.getAttribute("addrParent");
%>
<body>
<%--<div class="top container">
    <div class="header sub-title">
        <a class="icon-left" href="javascript:void(0);" onclick="window.history.back()"></a>
        <h1>预约安装</h1>
    </div>
</div>--%>
<%--<c:set value="预约安装" var="titleName" ></c:set>--%>
<%--<%@ include file="/WEB-INF/views/include/broadBandHead.jsp"%>--%>
<!--套餐选择 start-->
<div class="container">
    <div class="kd-navbar-wrap">
        <div class="kd-navbar ">
            <div class="navbar-left">
                <a href="javascript:history.back();" class="arrow-left"></a>
            </div>
            <h3 class="navbar-title">宽带办理预约</h3>


        </div>
    </div>

    <div class="family-box clearfix">
        <form action="" id="form1" name="form1" method="post">
            <input type="hidden" name="address" id="address">
            <input type="hidden" name="shopId" value="${shopId}">
            <dl>
                <dt>联系电话：</dt>
                <dd><input class="input-com" type="text" name="phoneNum" id="phoneNum"></dd>
            </dl>
            <dl>
                <dt>联系人：</dt>
                <dd><input class="input-com" type="text" name="contacts" id="contacts"></dd>
            </dl>
            <dl>
                <dt>意向产品：</dt>
                <dd><input class="input-com" type="text" name="productName" id="productName"></dd>
            </dl>
            <dl>
                <dt>装机地址：</dt>
                <dd >
                    <!--地市选择 start-->
                    <ul class="change-list">
                        <li>

                            <div class="right-td"> <span class="td-fr"><i class="select-arrow"></i>
          <select id="eparchySelect" class="item-select" name="routeEparchyCode">
              <%--<option value="长沙市">长沙市</option>--%>
              <c:forEach items="${cityList}" var="cityName">
                  <option value="${cityName.eparchyCode}">${cityName.orgName}</option>
              </c:forEach>
          </select>
          <input type="hidden" name="eparchyCode" id="eparchyCode"/>
          </span> <span class="td-fr"> <i class="select-arrow"></i>
          <select id="city" class="item-select city-area" name="city">

          </select>
          </span></div>
                        </li>
                        <li>

                            <div class="right-td"> <span class="td-fr"><i class="select-arrow"></i>
          <input type="hidden" name="streetName" id="streetName" value="${o2oCommunity.streetName}"/>
          <select id="street" class="item-select" name="street">
          </select>
          </span> <span class="td-fr"> <i class="select-arrow"></i>
           <input type="hidden" name="roadName" id="roadName" value="${o2oCommunity.roadName}"/>
          <select id="road" class="item-select road-area" name="road">
              <%--<option value="岳麓区">岳麓区</option>--%>
          </select>
          </span></div>
                        </li>
                    </ul>
                    <!--地市选择 end-->

                    <input class="input-com" type="text" name="addressDetail" id="addressDetail" placeholder="请输入小区地址、楼栋、门牌号"> <a class="btn-bl" style="width: 6.566667rem;" id="a_apply">立即办理</a></dd>
            </dl>
        </form>

        <%--新增分享功能--%>
        <p class="pd10">分享给用户自行办理<a id="shareBtn" href="javascript:void(0);">
            <img src="${ctxStatic}/images/fx-icon.png" style="width: 30px;"></a>
        </p>
    </div>

    <div class="kd-navbar-wrap navbar-wrap-footer ">
        <div class="kd-navbar ">

        </div>
    </div>
</div>
<!--搜索地址 end-->

<!-- loading start -->

<!-- loading end -->

<script type="text/javascript">
var changeUrl="<%=request.getContextPath()%>/broadband/getChildrenByPid";
var submitUrl="<%=request.getContextPath()%>/broadband/broadBandBook";
var gotoSuccessUrl="<%=request.getContextPath()%>/broadband/gotoBookInstallSuccess";
var gotoErrorUrl="<%=request.getContextPath()%>/broadband/gotoBookInstallError";
</script>
<script>
    var addressQuery =
    {
        cityName: "长沙", //市
        cityCode: "0731", //市区域编码
        city: "233199472",
        cityArea: "天心", //区
        street: "-133509970",
        streetName: "金洲国际城街道办事处",
        road: "-133509974",
        roadName: "金湖路",
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
            $('.city-area').html("<option value=''>请选择</option>");
            $('#street').html("");
            $('#road').html("");
            if(cityCode==''){
                return;
            }
            $.ajaxSettings.async = false;
            $.getJSON('${ctx}/bandResourceQuery/qryAddressCityName', {
                        'cityCode': cityCode || '0731'
                    }, function (e) {
                        if (e.respDesc == "OK!") //接口请求成功
                        {
                            var ADDRESS_INFO = e.result[0].ADDRESS_INFO;
                            $.each(ADDRESS_INFO, function (i, item) {
                                $('.city-area').append("<option value=" + item.CITY_ID + ">" + item.CITY_NAME + "</option>");
                            });
                        } else {
                            alert("系统异常，请稍后再试");
                        }
                    }
            );
        },

        /**
         * 查询街道区域
         * @param cityCode 市区域编码
         */
        qryStreetArea: function (street) {
            $('#street').html("<option value=''>请选择</option>");
            $('#road').html("");
            if(street==''){
                return;
            }
            $.ajaxSettings.async = false;
            $.getJSON('${ctx}/bandResourceQuery/qryAddressCityName', {
                        'cityCode': street || '0731'
                    }, function (e) {
                        if (e.respDesc == "OK!") //接口请求成功
                        {
                            var ADDRESS_INFO = e.result[0].ADDRESS_INFO;
                            $.each(ADDRESS_INFO, function (i, item) {
                                $('#street').append("<option value=" + item.CITY_ID + ">" + item.CITY_NAME + "</option>");
                            });
                        } else {
                            alert("系统异常，请稍后再试");
                        }
                    }
            );
        },
        /**
         * 查询路巷名称
         * @param cityCode 市区域编码
         */
        qryRoadArea: function (road) {
            $('#road').html("");
            if(road==''){
                return;
            }
            $.ajaxSettings.async = false;
            $.getJSON('${ctx}/bandResourceQuery/qryAddressCityName', {
                        'cityCode': road
                    }, function (e) {
                        if (e.respDesc == "OK!") //接口请求成功
                        {
                            var ADDRESS_INFO = e.result[0].ADDRESS_INFO;
                            $.each(ADDRESS_INFO, function (i, item) {
                                $('#road').append("<option value=" + item.CITY_ID + ">" + item.CITY_NAME + "</option>");
                            });
                        } else {
                            alert("系统异常，请稍后再试");
                        }
                    }
            );
        },

        //TODO 调用根据区查询街道的接口
        //todo 调用查询路的接口
    };

    $(function(){
        addressQuery.qryCityArea(addressQuery.cityCode);
        $("#eparchySelect").live('change', function () {  //市级别选择事件监听
            $("#txt").val("");
            $(".adress-list").html("");
            addressQuery.cityCode = $(this).val();
            addressQuery.cityName = $(this).find("option:selected").text();
            addressQuery.qryCityArea(addressQuery.cityCode);
        });

        $("#city").live('change', function () {  //市级别选择事件监听
            $("#txt").val("");
            $(".adress-list").html("");
            addressQuery.city = $(this).val();
            addressQuery.cityArea = $(this).find("option:selected").text();
            addressQuery.qryStreetArea(addressQuery.city);
        });
        $("#street").live('change', function () {
            addressQuery.street = $(this).val();
            addressQuery.streetName = $(this).find("option:selected").text();
            addressQuery.qryRoadArea(addressQuery.street)
        });
        $("#txt").live('input propertychange', function () {  //关键字级别选择事件监听
            addressQuery.keyWord = $(this).val();
            addressQuery.cityArea = $(".city-area").val();
            $(".adress-list").html(""); //清空无效信息
            addressQuery.qryLastAddress(addressQuery.cityName, addressQuery.cityArea, addressQuery.keyWord);//关键字查询
        });
    });

    // 新增用户分享
    $("#shareBtn").click(function(){
        var productName = $("input[name='productName']").val();
        var shopId = $("input[name='shopId']").val();
        window.location.href="http://wap.hn.10086.cn/shop/broadband/linktoBookInstall?productName="+productName+"&shopId="+shopId;
    });
</script>
</body>
</html>