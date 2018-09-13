<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="WT.si_n" content="和家庭" />
    <meta name="WT.si_x" content="填写地址" />
    <%@include file="/WEB-INF/views/include/head.jsp" %>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/swiper.min.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/wt-sub.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/oil.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/kd.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/main.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/media-style.css"/>
    <script type="text/javascript" src="${ctxStatic}/js/public.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/load.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/kd.js"></script>

    <!-- 宽带新装相关js -->
    <script type="text/javascript" src="${ctxStatic}/js/goods/broadband/heBroadbandInstall.js"></script>
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

    <%@ include file="/WEB-INF/views/include/message.jsp"%>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" name="viewport"/>
    <title>湖南移动网上营业厅</title>
    <script type="text/javascript">
        var  ctx = '${ctx}';
    </script>
</head>

<body>
<%--<div class="top container">
    <div class="header sub-title"><a class="icon-left" href="javascript:void(0);" onclick="window.history.back()"></a>
        <h1>和家庭</h1>
    </div>
</div>--%>
<c:set value="和家庭" var="titleName" ></c:set>
<%@ include file="/WEB-INF/views/include/broadBandHead.jsp"%>

<div class="container bg-gray hy-tab">
    <form action="confirmHeInstall" id="form1" name="form1" method="post">
        <input type="hidden" id="form1_skuId" name="form1_skuId"  />
        <input type="hidden" id="form1_houseCode" name="form1_houseCode" value="${broadbandDetailInfoResult.broadbandDetailInfo.addrId}"/>
        <input type="hidden" id="form1_addressName" name="form1_addressName" value="${broadbandDetailInfoResult.broadbandDetailInfo.addrName}"/>
        <input type="hidden" id="form1_addrDesc" name="form1_addrDesc" value="${broadbandDetailInfoResult.broadbandDetailInfo.addrDesc}"/>
        <input type="hidden" id="form1_hasBroadband" name="form1_hasBroadband" value="${resultMap.isBroadBand}" />
        <input type="hidden" id="form1_hasMbh" name="form1_hasMbh" value="${resultMap.isMbh}" />
        <input type="hidden" id="form1_maxWidth" name="form1_maxWidth"  />
        <input type="hidden" id="form1_freePort" name="form1_freePort"  />
        <input type="hidden" id="form1_eparchyCode" name="form1_eparchyCode" value="${eparchy_Code}" />
        <input type="hidden" id="form1_county" name="form1_county"  />
        <input type="hidden" id="form1_coverType" name="form1_coverType" value="${broadbandDetailInfoResult.broadbandDetailInfo.coverType}"/>
        <input type="hidden" id="form1_chooseBandWidth" name="form1_chooseBandWidth"  />
        <input type="hidden"  id="submit_productId" name="submit_productId"  />
        <input type="hidden"  id="submit_packageId" name="submit_packageId"  />
        <input type="hidden"  id="submit_discntCode" name="submit_discntCode"  />
        <input type="hidden" id="form1_chooseCat" name="form1_chooseCat"  />
        <input type="hidden" id="form1_Mbh" name="form1_Mbh" />
        <input type="hidden" id="form1_custName" name="form1_custName" value="${broadbandDetailInfoResult.broadbandDetailInfo.custName}"/>
        <input type="hidden" id="form1_psptId" name="form1_psptId" value="${broadbandDetailInfoResult.broadbandDetailInfo.psptId}"/>
        <input type="hidden" id="installPhoneNum" name="installPhoneNum" value="${installPhoneNum}"/>

    </form>
    <div class="wf-list tab-con">
    <c:choose>
        <c:when test="${resultMap.isBroadBand=='1'}">
            <!--地市选择 start-->
            <ul class="change-list">
                <li>
                    <label>地&emsp;&emsp;区：</label>
                    <div class="right-td">
                        <span class="td-fr">
                        <i class="select-arrow"></i>
                        <select class="set-sd" id="memberRecipientCity" name="memberRecipientCity">
                            <c:forEach items="${cityList}" var="city" varStatus="status" >
                                <c:if  test="${fn:contains(city.eparchyCode,eparchy_Code)}">
                                    <option value="${city.eparchyCode}"  orgid="${city.orgId}" otitle="地市选择" otype="button">${city.orgName}</option>
                                </c:if>
                            </c:forEach>
                        </select>
                        </span>
                        <span class="td-fr"> <i class="select-arrow"></i>
                          <select class="set-sd"  id="memberRecipientCounty" name="memberRecipientCounty">
                              <c:forEach items="${countyList}" var="county" varStatus="status">
                                  <option value="${county.orgId}" otitle="区县选择" otype="button">${county.orgName}</option>
                              </c:forEach>
                          </select>
                         </span>
                    </div>
                </li>
                <li style="border:none;">
                    <label>详细地址：</label>
                    <div class="right-td">
                        <input type="hidden"  id="houseCode" name="houseCode"  />
                        <input type="text"   id="address" name="address" class="form-control flip" value="请输入小区、写字楼等关键字进行查询" readonly="readonly" onfocus="this.blur()"    otitle="详细地址选择" otype="button"/>
                    </div>
                </li>
            </ul>
            <!--地市选择 end-->
        </c:when>
        <c:otherwise>
            <!--已有地址 start-->
            <div class="wf-ait clearfix">
                <div class="wf-tit wf-cit font-gray">安装地址：</div>
                <div class="wf-con">
                    <p>${broadbandDetailInfoResult.broadbandDetailInfo.addrDesc}</p>
                </div>
            </div>
            <!--已有地址 end-->
        </c:otherwise>
    </c:choose>
    <!--选择套餐 start-->
    <div class="change-tc">
        <!--宽带新装金额 start-->
        <h2 class="renew-title">请选择新装套餐</h2>

        <ul class="add_oil select-renew clearfix" >
            <c:forEach items="${heBroadbandItemList}" var="broadbandItem" varStatus="status">
                <li name="li_broadbandItem" price="${broadbandItem.price}">${broadbandItem.price}元/月
                    <p class="font-gray">${broadbandItem.bandWidth}M宽带0元享</p>
                    <s></s>
                    <input type="hidden" id="skuId" name="skuId" value="${broadbandItem.goodsSkuId}"/>
                    <input type="hidden" id="goodsId" name="goodsId" value="${broadbandItem.goodsId}"/>
                    <input type="hidden" id="chooseBandWidth" name="chooseBandWidth" value="${broadbandItem.bandWidth}"/>
                    <input type="hidden" name="productId" value="${broadbandItem.heProductId}"/>
                    <input type="hidden" name="desc" value="${broadbandItem.desc}"/>
                    <c:if test="${not empty broadbandItem.labelName}">
                        <span class="tip tip-rose">${broadbandItem.labelName}</span>
                    </c:if>
                </li>
            </c:forEach>
        </ul>
        <!--充值金额 end-->
        <div class="renew-content nofiexd">
            <ul class="ren-tit clearfix">
                <li class="clearfix bg-grayf2">
                    <div class="cp-con"><span class="renew-tip">含</span><span id="desc1" class="renew-tip2">送价值240元互联网电视一年+价值50元5000积分+100元优惠购机顶盒</span></div>
                </li>
                <li>
                    <div class="sm-list">
                        <h4>入网必备</h4>
                        <label class="ren-bt">
                            <div class="iyq-list">
                                <div class="iyq-pic"><span class="iyq-iocn"></span></div>
                                <div class="iyq-text pt0" style="border:none;">
                                    <p>宽带光猫<del class="sm-sql font-gray">￥100.00</del></p>
                                    <p class="font-gray yx-date">移动专用光猫，稳定、极速上网</p>
                                    <div class="bot-text">
                                        <b class="yq-jb font-rose">￥0.00</b>
                                        <%--<input type="checkbox" class="sub-radio "  id="form2_chooseCat" name="form2_chooseCat"  value="1" checked="checked"/>--%>
                                    </div>
                                </div>
                            </div>
                        </label>
                    </div>
                </li>
                <c:if test="${resultMap.isMbh == '1'}">
                    <li>
                        <div class="sm-list">
                            <h4>优惠赠送</h4>
                            <label class="ren-bt">
                                <div class="iyq-list">
                                    <div class="iyq-pic"><span class="sm-icon"></span></div>
                                    <div class="iyq-text pt0">
                                        <p>高清机顶盒<del class="sm-sql font-gray">￥100.00</del></p>
                                        <p class="font-gray yx-date">互联网电视专用，享受智能高清电视节目</p>
                                        <div class="bot-text">
                                            <b class="yq-jb font-rose">￥0.00</b>
                                            <%--<input type="checkbox" class="sub-radio" id='form2_Mbh' name="form2_Mbh"   value="0" checked="checked" />--%>
                                        </div>
                                    </div>
                                    <div class="iyq-text">
                                        <p>牌照方</p>
                                        <div class="iyq-menu">
                                            <div class="bx-list clearfix">
                                            	<label class="kd-change">
                                                    <input type="radio" name="boxType" checked id='bx' onclick="adc('bx')" value="1"/>
                                                    <span>未来电视</span></label>
                                                <label class="kd-change" disabled="disabled">
                                                    <input type="radio" name="boxType" id='bw' onclick="adc('bw')" value="0" disabled="disabled"/>
                                                    <span>芒果TV（库存不足）</span></label>
                                            </div>
                                            <div id="bws" class="chang-text">湖南自制剧、热播电影、综艺、电影片库、动漫等。</div>
                                            <div id="bxaw" class="chang-text">央视连续剧、少儿动漫、极光专区、超级回看中的电影。</div>
                                        </div>
                                    </div>
                                </div>
                            </label>
                        </div>
                    </li>
                </c:if>
            </ul>
        </div>
        <!--选择套餐 end-->
        </div>
    </div>
</div>
<div class="fix-br">
    <div class="affix foot-box">
        <div id="div_settlement" class="container active-fix kd-disabled">
            <div class="fl-left">
                <p class="p1">应付总额：<b class="cl-rose" id="b_total">￥<span id="span_total1">0.00</span></b></p>
            </div>
            <div class="fl-right"><a href="javascript:;" id="a_confirmInstall" otitle="立即订购" otype="button">立即订购</a></div>
            <!--当用户不能点击时在a class加dg-gray-->
        </div>
    </div>
</div>
<!--搜索地址 start-->
<div id="div_searchAddress" class="site-search panel" style="display:none">
    <div class="top container">
        <div class="header site-full sub-title"><a class="icon-left site-close" id="a_retSearch" ></a>
            <div class="top-search list-search fit-ipnut">
                <input type="text" id="queryAddress" name="queryAddress" class="form-control form-fr" placeholder="关键字搜索参考：请输入小区或周边标志性建筑" autofocus="autofocus" />
                <a href="javascript:;" class="icon-search"  id="a_search" ></a></div>
        </div>
    </div>
    <!--没资源 start-->
    <div class="mzy-con hide" id="div_message"><!--没资源时去掉hide-->
        <img src="${ctxStatic}/images/error.png" />
        <div class="mzy-text">
            <p>尊敬的用户，非常抱歉！</p>
            <p>您所在区域宽带资源还在建设中，</p>
            <p>有宽带资源后，我们会第一时间通过短信告知您！</p>
        </div>
    </div>
    <!--没资源 end-->
    <!--展示地址信息 start-->
    <ul class="container adress-list">
    </ul>
    <!--展示地址信息 end-->
    <div class="fix-br container fix-top fix-fb">
        <div class="affix container foot-menu">
            <div class="container form-group tj-btn"> <a  href="##" id="queryCommit" class="btn btn-gray" disabled="disabled">确定</a> <a id="queryCancel" class="btn btn-gray site-close" href="##">取消</a> </div>
            <!--预约安装 start-->
            <!--预约安装 start-->
        </div>
    </div>
</div>
<!--搜索地址 end-->
<script type="text/javascript" src="${ctxStatic}/js/nav.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/rem.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/public.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/tab.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/modal.js"></script>
<script type="text/javascript">
    var changeUrl="<%=request.getContextPath()%>/broadband/getChildrenByPid";
    var submitUrl="<%=request.getContextPath()%>/broadband/broadBandBook";
    var gotoSuccessUrl="<%=request.getContextPath()%>/broadband/gotoBookInstallSuccess";
    var gotoErrorUrl="<%=request.getContextPath()%>/broadband/gotoBookInstallError";
</script>
</body>
</html>