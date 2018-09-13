<%--
  Created by IntelliJ IDEA.
  User: cc
  友商信息录入
  Date: 2017/9/18
  Time: 20:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/views/include/head.jsp" %>
<%@ include file="/WEB-INF/views/include/message.jsp" %>
<!DOCTYPE html>
<html>

<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0"/>
    <meta name="format-detection" content="telphone=no, email=no"/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>湖南移动商城</title>
    <link href="${ctxStatic}/css/main.css" rel="stylesheet" type="text/css"/>
    <link href="${ctxStatic}/css/broadBand/pure-grids.css" rel="stylesheet" type="text/css"/>
    <link href="${ctxStatic}/js/imgup/imgup.css" rel="stylesheet" type="text/css"/>
    <link href="${ctxStatic}/css/broadBand/channel.css" rel="stylesheet" type="text/css"/>
    <link href="${ctxStatic}/js/iOSselect/iosSelect.css" rel="stylesheet" type="text/css" />
</head>

<body>
<div class="top container">
    <div class="header border-bottom"><a class="icon-main-back" href="search"></a>
        <%--如果添加信息未成功，返回到搜索页面，如果添加成功，自动跳转到原信息收集页面--%>
        <h1>信息录入</h1>
    </div>
</div>
<div class="container">
    <div class="channel-entry">
        <form method="post" id="partnerInfoForm" action="addPartnerInfo">
            <input type="hidden" name="communityId" value="${o2oCommunityPartnersInfo.communityId}">
            <c:if test="${o2oCommunityInfoBak==null}">
                <input type="hidden" name="communityCode" value="${o2oCommunityPartnersInfo.communityId}">
                <input type="hidden" name="eparchy" value="${eparchy}">
            </c:if>
            <c:if test="${o2oCommunityInfoBak!=null}">
                <input type="hidden" name="communityCode" value="${o2oCommunityInfoBak.communityCode}">
                <input type="hidden" name="eparchy" value="${o2oCommunityInfoBak.eparchy}">
            </c:if>

            <input type="hidden" name="communityName" value="${o2oCommunityPartnersInfo.communityName}">
            <input type="hidden" name="addressPath" value="${o2oCommunityPartnersInfo.addressPath}">
            <input type="hidden" id="synchronizedType" value="${synchronizedType}">
            <%--友商名称(明明是竞争对手说成友商)--%>
            <div class="entry-list">
                <div class="entry-title b-color01">友商名称</div>
                <div class="posted-list">
                    <p class="posted-box">
                        <input id="partner" name="partnersName" type="hidden"/>
                        <input id="partnerName" type="text" class="form-control" maxlength="100" placeholder="友商名称"  readonly="readonly"/>
                    </p>
                </div>
            </div>
            <!--宽带覆盖情况 start-->
            <div class="entry-list">
                <div class="entry-title b-color01">宽带覆盖情况</div>
                <div class="posted-list">
                    <p class="posted-box">
                        <input id="coverInfo" name="coverInfo" type="text" maxlength="100" class="form-control" placeholder="输入宽带覆盖情况"/>
                    </p>
                </div>
            </div>
            <!--宽带是否独占 start-->
            <div class="entry-list">
                <div class="entry-title b-color01">是否独家进驻</div>
                <div class="posted-list">
                    <p class="posted-box">
                    <select id="activityAddress" name="activityAddress" class="form-control">
                        <option value="">---请选择---</option>
                        <option value="否">否</option>
                        <option value="是">是</option>
                    </select>
                    </p>
                </div>
            </div>
            <!--宽带覆盖情况 end-->
            <!--宽带客户数量 start-->
            <div class="entry-list">
                <div class="entry-title b-color02">宽带客户数量</div>
                <div class="posted-list">
                    <p class="posted-box">
                        <input id="userNum" name="userNum" type="number" maxlength="10" oninput="if(value.length>10)value=value.slice(0,10)" class="form-control" placeholder="输入宽带客户数量"/>
                    </p>
                </div>
            </div>
            <!--宽带客户数量 end-->
            <!--宽带营销及回挖政策 start-->
            <div class="entry-list">
                <div class="entry-title b-color03">宽带营销及回挖政策</div>
                <div class="posted-list">
                    <p class="posted-box">
                        <input id="stealBackPolicy" name="stealBackPolicy" type="text" maxlength="100" class="form-control" placeholder="输入宽带营销及回挖政策"/>
                    </p>
                </div>

            </div>
            <!--宽带营销及回挖政策 end-->
            <!--现场活动上传 start-->
            <div class="entry-list">
                <div class="entry-title b-color04">现场活动上传</div>
                <div class="posted-con">
                    <div class="posted-list">
                        <p class="posted-box">
                            <input id="" name="" type="text" class="form-control" value="${o2oCommunityPartnersInfo.addressPath}"/>
                        </p>
                    </div>
                    <p class="p10">活动图片上传</p>
                    <div class="list_img">
                        <div class="z_photo">
                            <div class="z_file fl">
                                <span class="add-img">+</span>
                                <input type="file" name="file" id="file" class="file" value=""
                                       accept="image/jpg,image/jpeg,image/png,image/bmp" multiple/>
                            </div>
                        </div>
                        <div class="imgup-mask works-mask">
                            <div class="mask-content">
                                <p>确认删除？</p>
                                <p class="del-p channel-gray9">您确定要删除图片吗？</p>
                                <p class="check-p pure-g"><span class="pure-u-1-2 wsdel-no">取消</span><span
                                        class="pure-u-1-2 wsdel-ok">确定</span></p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <!--现场活动上传 end-->
        </form>
    </div>
    <div class="channel-btn"><a onclick="confirmSubmit()" class="broad-btn broad-btn-blue" id="confrimBtn">确认提交</a>
    </div>
    <!--按钮变灰class broad-btn-blue换成broad-btn-gray-->
</div>
<script type="text/javascript" src="${ctxStatic}/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/o2o/infoCollect/partnerInfo.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/load.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/modal.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/iOSselect/iscroll.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/iOSselect/iosSelect.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/goods/ajaxfileupload.js"></script>
</body>
<script type="text/javascript">
    var data = [
        {'id': '10001', 'value': '移动宽带'},
        {'id': '10002', 'value': '联通宽带'},
        {'id': '10003', 'value': '电信宽带'}
    ];
    var partnerDom = document.querySelector('#partner');
    var partnerIdDom = document.querySelector('#partnerName');
    partnerIdDom.addEventListener('click', function () {
        var operaId = partnerDom.dataset['id'];
        var operaName = partnerDom.dataset['value'];

        var operaSelect = new IosSelect(1,
            [data],
            {
                container: '.container',
                title: '宽带运营商',
                itemHeight: 50,
                itemShowCount: 3,
                oneLevelId: operaId,
                callback: function (selectOneObj) {
                    partnerDom.value = selectOneObj.value;
                    partnerIdDom.innerHTML = selectOneObj.value;
                    partnerDom.dataset['id'] = selectOneObj.id;
                    partnerDom.dataset['value'] = selectOneObj.value;
                    partnerIdDom.value = selectOneObj.value;
                }
            });
    });
</script>

</html>