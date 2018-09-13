<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>首页</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" name="viewport" />
	<%@include file="/WEB-INF/views/include/head.jsp" %>
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/ap/lib/normalize-5.0.0.css" />
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/ap/lib/swiper/swiper.min.css" />
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/ap/css/wap.css" />
	
    <script type="text/javascript" src="${ctxStatic}/ap/lib/jquery-2.2.4.min.js"></script>
	<script type="text/javascript" src="${ctxStatic}/ap/lib/flexible/flexible.js"></script>
	<script type="text/javascript" src="${ctxStatic}/ap/lib/jquery.tabslet.min.js"></script>
	<script type="text/javascript" src="${ctxStatic}/ap/lib/swiper/swiper.min.js"></script>
	<script type="text/javascript" src="${ctxStatic}/ap/lib/modal/modal.js"></script>
	<script type="text/javascript" src="${ctxStatic}/ap/js/comm.js"></script>
    <%@ include file="/WEB-INF/views/include/message.jsp"%>
    <title>湖南移动网上营业厅</title>
    <script type="text/javascript">
    	var  ctx = '${ctx}';
    </script>
</head>

<body>
	<div class="container">
        <img src="${ctxStatic}/ap/images/top.jpg" />
        <div class="broadband-tit detail-tit"><span class="broad-icon"></span>资费介绍</div>
        <div class="broad-con clearfix">
            <dl>
                <dt><img src="${ctxStatic}/ap/images/pro_tu01.png" /></dt>
                <dd>
                    <h4>家庭成员合计消费达到158元</h4>
                    <p class="font-blue fs36">100M光纤宽带免费用</p>
                    <p>4K高清互联网电视10元/月</p>
                    <p class="font-blue fs32">光猫、机顶盒费用全免(仅限线上办理)</p>
                </dd>
            </dl>
        </div>
        <div class="broadband-tit ywbl-list ywbl-detail"><span class="broad-icon"></span>办理流程</div>
        <div class="detail-list">
            <ul class="bus-list clearfix">
                <li class="col col-12-3">
                    <a href="javascript:void(0)">
                        <img src="${ctxStatic}/ap/images/bus_icon01.png" />
                        <p>选择宽带</p>
                    </a>
                </li>
                <li class="col col-12-3">
                    <a href="javascript:void(0)">
                        <img src="${ctxStatic}/ap/images/bus_icon02.png" />
                        <p>信息填写</p>
                    </a>
                </li>
                <li class="col col-12-3">
                    <a href="javascript:void(0)">
                        <img src="${ctxStatic}/ap/images/bus_icon03.png" />
                        <p>确认下单</p>
                    </a>
                </li>
                <li class="col col-12-3">
                    <a href="javascript:void(0)">
                        <img src="${ctxStatic}/ap/images/bus_icon04.png" />
                        <p>上门安装</p>
                    </a>
                </li>
            </ul>
            <div class="detail-btn"><a href="/shop/broadband/linktoBookInstall" class="btn btn-blue">戳我办理</a></div>
        </div>
        <div class="broadband-tit ywbl-list sperk-list"><span class="broad-icon"></span>活动规则</div>
        <div class="active-rule">
            <p><span class="rule-rand">1</span><span class="rule-text">保底时间12个月，承诺保底期间内，客户不允许降档或取消保底，消费保底与其他保底活动不互斥，就高不就低原则；</span></p>
            <p><span class="rule-rand">2</span><span class="rule-text">互联网高清电视第一年10元/月，从第13个月起按20元/月从手机账户上扣取；</span></p>
            <p><span class="rule-rand">3</span><span class="rule-text">老用户宽带到期前参与本活动时，宽带的余额次月自动转入捆绑手机号码的现金存折（不可清退），可用于抵扣手机套餐费用。套餐、宽带当月生效，办理当月按天收取保底费用，当月宽带月功能费、互联网电视月功能费，不计入整体优惠期；</span></p>
            <p><span class="rule-rand">4</span><span class="rule-text">办理号码必须是家庭网成员，不区分家庭网主副卡。同一家庭网成员只能参与一次本活动。</span></p>
            <!--swiper start-->
            <div class="swiper-con">
                <div class="swiper-container">
                    <div class="swiper-wrapper">
                        <div class="swiper-slide">
                            <div class="broad-con clearfix">
                                <dl>
                                    <dt><img src="${ctxStatic}/ap/images/detail_tu.png" /></dt>
                                    <dd>
                                        <h4>168元和家庭套餐</h4>
                                        <p>100M光纤宽带+2年互联网电视+2G 流量+2000分钟通话
                                        </p>
                                        <a href="/shop/BroadbandTrade/heBroadbandInstall" class="btn btn-blue">戳我办理</a>
                                    </dd>
                                </dl>
                            </div>
                        </div>
                        <div class="swiper-slide">
                            <div class="broad-con clearfix">
                                <dl>
                                    <dt><img src="${ctxStatic}/ap/images/detail_tu02.png" /></dt>
                                    <dd>
                                        <h4>个人版：消费保底48元以上</h4>
                                        <p>个人消费保底金额48元及以上免费用 4K高清互联网电视
                                        </p>
                                        <a href="/shop/broadband/linktoBookInstall" class="btn btn-blue">戳我办理</a>
                                    </dd>
                                </dl>
                            </div>
                        </div>
                    </div>
                    <!-- Add Arrows -->
                    <div class="swiper-button-next"></div>
                    <div class="swiper-button-prev"></div>
                </div>
            </div>
            <!--swiper end-->
        </div>
    </div>
    <script>
        var swiper = new Swiper('.swiper-container', {
            nextButton: '.swiper-button-next',
            prevButton: '.swiper-button-prev',
            //pagination: '.swiper-pagination',
            paginationType: 'fraction'
        });
        
        $(function() {
        	var WTjson=WTjson||[];
        	WTjson["WT.branch"]="hn";
        	var ua = navigator.userAgent.toLowerCase();
        	var st = '';
        	if(ua.match(/leadeon/i)=="leadeon"){
        		//手厅插码
        		WTjson["WT.plat"]="app";
        		leadeon.init=function(){
        			//获取用户信息
        			leadeon.getUserInfo({
        				debug:false,
        				success:function(res){
        					//保存用户信息
        					if(!!res.cid){
        						WTjson["WT.cid"]=res.cid;
        					}
        					
        					if(!!res.phoneNumber){
        						WTjson["WT.mobile"]=res.phoneNumber;
        					}
        				},
        				error:function(res1){
        					
        				}
        			});
        		}
        	}else{
        		WTjson["WT.plat"]="touch";
        	}
        	
        	if(ua.match(/leadeon/i)=="leadeon"){
        		//走手厅
        		document.write('<script type="text/javascript" src="${ctxStatic}/js/qm/sdc_leadeon.js"><\/script>');
        	}else{
        		document.write('<script type="text/javascript" src="${ctxStatic}/js/qm/sdc9.js"><\/script>');
        		document.write('<script type="text/javascript" src="${ctxStatic}/js/qm/insdc_w2.js"><\/script>');
        	}
        });
    </script>
</body>
</html>