<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <%@include file="/WEB-INF/views/include/head.jsp" %>

    <script type="text/javascript" src="${ctxStatic}/js/qm/insdc_w2.js"></script>
    <title>湖南移动网上营业厅</title>
    <style type="text/css">
        .gird-101 {
            width: 100%;/*margin-bottom:10px;*/
            overflow: hidden;
            background-color: #fff;
            border-bottom: 1px solid #e5e5e5;
        }
        .gird-101 li {
            width: 33%;
            float: left;
            display: inline-block;
            padding: 0;
            margin: 0;
            text-align: center;
            box-sizing: border-box;
            /*border-top: 1px solid #E5E5E5;*/
            /*border-bottom: 1px solid #E5E5E5;*/
            /*border-right: 1px solid #E5E5E5;*/
            margin-bottom: -1px
        }
        .gird-101 li a {
            background: #fff;
            overflow: hidden;
            display: block;
            padding: 12px 2px;
            font-size: 14px
        }
        .gird-101 li a [class*="icon-wt-"] {
            width: 32px;
            height: 32px;
            margin: 0px auto 5px auto;
            background-size: 100%;
        }

        .gird-list_2 li img {
            width: 3.2rem;
            height: 3.2rem;
        }
        .gird-list_2 li p {
            padding-top: 0.5rem;
            font-size: 1.0rem;
            display: block;
            overflow: hidden;
            text-overflow: ellipsis;
            /*white-space: nowrap;*/
        }
    </style>
</head>

<body>
<div class="top container">
    <div class="header border-bottom"><%--<a class="icon-main-back" href="javascript:void(0);" onclick="window.history.back()"></a>--%>
        <h1>号段卡</h1>
    </div>
</div>
<div class="container">
    <%--<ul class="channel-list">

        <li>
            <a href="${ctx}recmd/o2OGenRecmdQrCode?confId=5005">
                <img src="${ctxStatic}/images/o2osim/sim.png" />
                <h4>大王卡</h4>
                <p class="channel-txt">号段卡（大王卡）</p>
            </a>
        </li>
        <li>
            <a href="${ctx}recmd/o2OGenRecmdQrCode?confId=E050RSENSD">
                <img src="${ctxStatic}/images/o2osim/sim.png" />
                <h4>新和家庭</h4>
                <p class="channel-txt">号段卡（新和家庭）</p>
            </a>
        </li>
    </ul>--%>

        <ul class="gird-101 gird-list_2 clearfix">
            <li>
                <a href="${ctx}recmd/o2OGenRecmdQrCode?confId=5005" otype="button" otitle="号段卡（大王卡）" oarea="号段卡（大王卡）">
                    <img src="${ctxStatic}/images/o2osim/sim.png"><p>大王卡</p></a></li>

            <li>
                <a href="${ctx}recmd/o2OGenRecmdQrCode?confId=E050RSENSD" otype="button" otitle="号段卡（新和家庭）" oarea="号段卡（新和家庭）">
                    <img src="${ctxStatic}/images/o2osim/sim.png"><p>新和家庭</p></a></li>
            <li>
                <a href="${ctx}recmd/o2OGenRecmdQrCode?confId=XYYXDWK" otype="button" otitle="自助购卡(全省)" oarea="自助购卡(全省)">
                    <img src="${ctxStatic}/images/o2osim/sim.png"><p>自助购卡(全省)</p></a>
            </li>
            <li>
                <a href="${ctx}recmd/o2OGenRecmdQrCode?confId=XYYXRSENMV8" otype="button" otitle="自助购卡(长沙)" oarea="自助购卡(长沙)">
                    <img src="${ctxStatic}/images/o2osim/sim.png"><p>自助购卡(长沙)</p></a>
            </li>

            <%--<li>--%>
                <%--<a href="${ctx}recmd/o2OGenRecmdQrCode?confId=E007SZ6BTQ" otype="button" otitle="O2O在线售卡" oarea="O2O在线售卡">--%>
                    <%--<img src="${ctxStatic}/images/o2osim/sim.png"><p>O2O在线售卡</p></a>--%>
            <%--</li>--%>
            <%--<li>--%>
                <%--<a href="${ctx}o2oSimOper/orderlist" otype="button" otitle="O2O售卡管理" oarea="O2O售卡管理">--%>
                    <%--<img src="${ctxStatic}/images/o2osim/sim.png"><p>O2O售卡管理</p></a>--%>
            <%--</li>--%>
        </ul>
</div>
</body>

</html>