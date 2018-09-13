<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>宽带移机</title>
    <%@include file="/WEB-INF/views/include/head.jsp"%>

    <link href="${ctxStatic}/css/main.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/css/media-style.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/css/kd.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/css/wt-sub.css" rel="stylesheet" type="text/css"/>
    <link href="${ctxStatic}/css/broadBand/channel.css" rel="stylesheet" type="text/css" />

    <script type="text/javascript" src="${ctxStatic}/js/qm/insdc_w2.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/modal.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/o2o/addressIndex.js"></script>
    <script  type="text/javascript" src="${ctxStatic}/js/member/securityencode.js" charset="utf-8"></script>
    <!-- 搜索地址框placeholder处理 -->
    <style>
        /* WebKit browsers */
        input:focus::-webkit-input-placeholder { color:transparent; }
        /* Mozilla Firefox 4 to 18 */
        input:focus:-moz-placeholder { color:transparent; }
        /* Mozilla Firefox 19+ */
        input:focus::-moz-placeholder { color:transparent; }
        /* Internet Explorer 10+ */
        input:focus:-ms-input-placeholder { color:transparent; }
    </style>

    <script>
        var keyStr = "ABCDEFGHIJKLMNOP" + "QRSTUVWXYZabcdef" + "ghijklmnopqrstuv"
                + "wxyz0123456789+/" + "=";
        $(function () {
            //确认预约时间
            $('#comfirmTime').on('click', function () {
               $("#time").html($("#bookDate").val()+"&nbsp;"+$("#bookSession option:selected").text());
            });
            $("#returnMain").click(function(){
                $("#div_searchAddress").hide();
            });

            $('#time').on('click', function () {
                $('#myModal2').modal('show');
            });
            //移机
            $('#move').on('click', function () {
                var serialNumber = $("#bandAccount").val();
                var addrId = $("#addrId").val();
                var addrDesc = $("#addrDesc").val();
                if(addrId.length == 0 || addrDesc.length == 0){
                    alert("请选择地址！");
                    return;
                }
                $('#myModal7').modal('show');
            });

            $('#submitMove').on('click', function () {
                var password = encode64($("#password").val());
                $("#password").val(password);
                $("#form1").submit();
            });

        });
        function show(){
            $("#div_searchAddress").show();
        }
        function encode64(password) {

            return strEnc(password, keyStr);
        }

    </script>
</head>
<body>
<form action="${ctx}/o2oMyBroadband/move" id="form1" name="form1" method="post">
    <input type="hidden"  name="bandAccount" id="bandAccount" value="${broadband.accessAcct}"/>
    <input type="hidden"  name="serialNumber" id="serialNumber" value="${broadband.serialNumber}"/>
    <input type="hidden" name="addrId" id="addrId" />
    <input type="hidden" name="addrDesc" id="addrDesc"/>
    <div class="top container">
    <div class="header sub-title"><a class="icon-left" href="javascript:void(0);" onclick="window.history.back()"></a>
        <h1>宽带移机</h1>
    </div>
</div>
<div class="container" id="mainContainer">
    <div class="broadband-con">
        <div class="transfer-list">
            <div class="transfer-title">原有装机地址</div>
            <div class="transfer-info">
                <p>
                    <label class="font-gray">套餐名称：</label><span>${broadband.discntName}</span></p>
                <p>
                    <label class="font-gray">合&nbsp;&nbsp;约&nbsp;期：</label><span>${broadband.startTime}~ ${broadband.endTime}</span></p>
                <p>
                    <label class="font-gray">宽带账号：</label><span>${broadband.accessAcct}</span></p>
                <p>
                    <label class="font-gray">宽带地址：</label><span>${broadband.addrDesc}</span></p>
            </div>
        </div>
        <div class="transfer-list">
            <div class="transfer-title">请输入装机地址</div>
            <div class="transfer-info transfer-line">
                <p>
                    <label class="font-gray">所在地区：</label><span id="cityPicker" class="arrow">
                    <select   id="city" name="city" class="no-border">
                    <c:forEach items="${cityList}" var="city" varStatus="status">
                        <c:if test="${fn:contains(city.eparchyCode,eparchy_Code)}">
                            <option value="${city.eparchyCode}"  orgid="${city.orgId}">${city.orgName}</option>
                        </c:if>
                    </c:forEach>
                </select>
                    <select  id="county" name="county" class="no-border">
                    <%--<c:forEach items="${countyList}" var="county" varStatus="status">--%>
                        <%--<option value="${county.orgName}"  >${county.orgName}</option>--%>
                    <%--</c:forEach>--%>
                </select> </span></p>
                <p class="transfter-adress"><input class="fs12" style="width:100%" placeholder="输入小区具体地址" type="text" id="showAddress" onclick="show()" onfocus="this.blur()"/><span class="iconfont icon-search pull-right  ml10 mt4 " onclick="show()" ></span></p>
            </div>
        </div>

        <div class="transfer-list">
            <div class="transfer-title">安装时间</div>
            <div class="transfer-info">
                <p>
                    <label class="font-gray">选择时间：</label><span data-toggle="modal" class="arrow" id="time">${bookInstallDateList[0]}&nbsp;上午8：00-12：00</span></p>
            </div>
        </div>
    </div>
    <%--<p class="pt20 t-c"><a href="javascript:void(0)" class="font-blue" data-toggle="modal" data-target="#myModal3">《宽带移机业务规则》</a></p>--%>
    <div class="broad-btn">
        <a href="javascript:void(0)" class="btn btn-blue" data-toggle="modal" data-target="#myModal">立即办理</a>
    </div>
</div>
<!--立即办理 弹框 start-->
<div class="modal fade modal-prompt" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog broadband-dialog">
        <div class="modal-con">
            <div class="modal-text">
                <p>您好，您目前正在办理宽带移机业务，请确认是否办理。</p>
            </div>
            <div class="dialog-btn">
                <a href="javascript:void(0);" class="font-gray" data-dismiss="modal">取消</a>
                <a href="javascript:void(0);" class="font-blue" data-dismiss="modal" id="move">确认办理</a>
            </div>
        </div>
    </div>
</div>
<!--立即办理 弹框 end-->
<!--选择时间 弹框 start-->
<!--选择时间 弹框 start-->
<div class="modal fade modal-prompt" id="myModal2" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog broadband-dialog">
        <div class="modal-con">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">&times;</button>
                <h4 class="modal-title">安装时间</h4>
            </div>
            <ul class="modal-form">
                <li>
                    <span class="rt-title">日期选择：</span>
                    <div class="modal-rt">
                        <select class="form-control" name="bookDate" id="bookDate">
                            <c:forEach items="${bookInstallDateList}" var="bookDate">
                            <option value="${bookDate}">${bookDate}</option>
                            </c:forEach>
                        </select>
                    </div>
                </li>
                <li>
                    <span class="rt-title">时间段选择：</span>
                    <div class="modal-rt">
                        <select class="form-control" name="bookSession" id="bookSession">
                            <option value="0">上午8：00-12：00</option>
                            <option value="1">中午12：00-14：00</option>
                            <option value="2">下午14：00-18：00</option>
                            <option value="3">晚上18：00-20：00</option>
                        </select>
                    </div>
                </li>
            </ul>
            <div class="subform-btn">
                <%--<a href="javascript:void(0)" class="btn btn-green" data-dismiss="modal">取消</a>--%>
                <a href="javascript:void(0)" class="btn btn-blue" data-dismiss="modal" id="comfirmTime">确定</a>
            </div>
        </div>
    </div>
</div>
<!--选择时间 弹框 end-->
<!--业务规则 弹框 start-->
<div class="modal fade modal-prompt" id="myModal3" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog broadband-dialog">
        <div class="modal-con">
            <div class="modal-header">
                <h4 class="modal-title">业务规则</h4>
            </div>
            <div class="modal-text">
                <p>业务规则业务规则业务规则业务规则业务规则业务规则</p>
            </div>
            <div class="dialog-btn">
                <a href="javascript:void(0);" class="font-blue" data-dismiss="modal">确定</a>
            </div>
        </div>
    </div>
</div>
<!--业务规则 弹框 end-->
    <!--输入密码弹框 start-->
    <div class="modal modal-prompt" id="myModal7" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog channel-modal">
            <div class="modal-info">
                <h4>请输入密码</h4>
                <div class="modal-txt">
                    <p><input type="password" class="form-control" name="password" id="password" placeholder="请输入密码"/></p>
                </div>
                <div class="modal-btn">
                    <a href="javascript:void(0);" data-dismiss="modal" id="submitMove">确定</a>
                </div>
            </div>
        </div>
    </div>
    <!--输入密码弹框 end-->

    <div class="modal fade modal-prompt" id="myModal4" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog broadband-dialog">
            <div class="modal-con">
                <div class="modal-text">
                    <p>系统繁忙，请稍后再试！</p>
                </div>
                <div class="dialog-btn"><a href="javascript:void(0);" class="font-blue" data-dismiss="modal">确认</a></div>
            </div>
        </div>
    </div>

        <div class="site-search panel" style="display:none" id="div_searchAddress">
            <div class="top container">
                <div class="header site-full sub-title"><a class="icon-left" href="javascript:void(0)" id="back"></a>
                    <div class="top-search list-search fit-ipnut">
                        <input type="text" id="txt" name="txt"  class="form-control form-fr" placeholder="请输入地址"/>
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
</form>

<script type="text/javascript">

    var addressQuery =
    {
        cityName: $("#city").text(), //市
        cityCode: $("#city").val(), //市区域编码
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
                            $('#county').html("");
                            var ADDRESS_INFO = e.result[0].ADDRESS_INFO;
                            $.each(ADDRESS_INFO, function (i, item) {
                                $('#county').append("<option>" + item.CITY_NAME + "</option>");
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
                                console.log(item);
//                                $("#lastAddress").append("<li last-address-building=" + item.HOUSE_CODE + " coverType=" + item.COVER_TYPE + ">" + item.ADDRESS_NAME + "</li>");
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
                                console.log(item);
                                $(".adress-list").append("<li><label><span coverType=" + item.COVER_TYPE + " address-path=" + item.ADDRESS_PATH + " class='adr-xz' onclick=searchBuilding2(this)><input type='radio' name='radio_address'></span>" +
                                        "<span coverType=" + item.COVER_TYPE + " address-path=" + item.ADDRESS_PATH + " class='adr-text'  onclick=searchBuilding2(this)>" + item.COMMUNITY_NAME +  "</span></label></li>");
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

//
    };

    $(function () {
        addressQuery.qryCityArea(addressQuery.cityCode); //默认初始化加载
        addressQuery.cityArea=$("#county").val();
        $("#city").live('change', function () {  //市级别选择事件监听
            $("#txt").val("");
            $(".adress-list").html("");
            addressQuery.cityCode = $(this).val();
            addressQuery.cityName = $(this).find("option:selected").text();
            addressQuery.qryCityArea(addressQuery.cityCode);
        });

        $("#county").live('change', function () {  //区域级别选择事件监听
            $("#txt").val("");
            $(".adress-list").html("");
            addressQuery.cityArea = $(this).val();
            console.log(addressQuery.cityCode);
        });

        $("#txt").live('input propertychange', function () {  //关键字级别选择事件监听
            addressQuery.keyWord = $(this).val();
            addressQuery.cityArea = $("#county").val();
            $(".adress-list").html(""); //清空无效信息
            addressQuery.qryLastAddress(addressQuery.cityName, addressQuery.cityArea, addressQuery.keyWord);//关键字查询
            console.log(addressQuery.keyWord);
        });

        $("#queryCommit").click(function(){  //地址信息选中事件监听
            var address = $('input[name="radio_address"]:checked ').parent().nextAll("span[class='adr-text']").text();
            var houseCode = $('input[name="radio_address"]:checked ').parent().nextAll("span[class='adr-text']").attr("last-address-building");
            var coverType = $('input[name="radio_address"]:checked ').parent().nextAll("span[class='adr-text']").attr("coverType");
            console.log("address:"+address);
            console.log("houseCode:"+houseCode);
            console.log("coverType:"+coverType);
            $("#addrDesc").val(address);
            $("#addrId").val(houseCode);
            $("#div_searchAddress").hide();
            $("#mainContainer").show();
            $("#showAddress").val(address);
            queryAddrId(houseCode);
        });

        $("#back").click(function(){  //地址信息选中事件监听
            $("#div_searchAddress").hide();
            $("#mainContainer").show();
        });

    });

    /**
     * 查询楼栋
     * @param residence 小区名
     */
    function searchBuilding2(obj){
        $(obj).addClass('cur').siblings().removeClass('cur');
        var residence = $(obj).attr("address-path");
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
                            $(".adress-list").append("<li><label><span coverType=" + item.COVER_TYPE + " address-path-building=" + item.ADDRESS_PATH + " class='adr-xz' onclick=chooseBuilding2(this)>" +
                                    "<input type='radio' name='radio_address'></span>" +
                                    "<span coverType=" + item.COVER_TYPE + " address-path-building=" + item.ADDRESS_PATH + " class='adr-text'  onclick=chooseBuilding2(this)>" + item.BUILDING_NAME +  "</span>" +
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
                            //console.log(item);
                            $(".adress-list").append("<li><label><span coverType=" + item.COVER_TYPE + " last-address-building=" + item.HOUSE_CODE + " class='adr-xz' onclick=chooseLastAddress2(this)><input type='radio' name='radio_address'></span><span coverType=" + item.COVER_TYPE + " last-address-building=" + item.HOUSE_CODE + " class='adr-text' onclick=chooseLastAddress2(this)>" + item.ADDRESS_NAME +  "</span></label></li>");
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

    function queryAddrId(addrId){
        $.ajax({
            type: "POST",
            dataType: "json",
            async: false,
            data : {
                addrId:addrId
            },
            url: ctx+"/o2oMyBroadband/queryAddr",
            success: function (data) {
                if(data.respCode == '0' && data.bookInstallDateList.length>0){
                    $("#bookDate").empty();
                    $.each(data.bookInstallDateList, function (i, item) {
                        $("#bookDate").append( "<option value='"+item+"'>"+item+"</option>");
                    });
                }
            },
            error: function (data) {

            }
        });
    }
</script>
</body>
</html>
