<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <%@include file="/WEB-INF/views/include/head.jsp" %>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/main.css" />
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/swiper.min.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/wt-sub.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/oil.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/kd.css"/>
    <script type="text/javascript" src="${ctxStatic}/js/public.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/load.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/nav.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/tab.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/o2o/share.js"></script>

	<!-- 宽带多级地址搜索框 -->
    <%@ include file="/WEB-INF/views/include/message.jsp"%><!-- 提示弹框 -->
    <title>湖南移动网上营业厅</title>

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

	<script type="text/javascript">
    	var  ctx = '${ctx}';
    </script>
</head>

<body>
<div class="top container">
  <div class="header sub-title"><a class="icon-left" href="javascript:void(0);" onclick="window.history.back()"></a>
    <h1>输入办理号码</h1>
  </div>
</div>
<div class="container">
	<form action="index" id="form1" name="form1" method="post">
    <input type="hidden" name="minCost" value="${minCost}"/>
    <input type="hidden" name="busiType" value="${busiType}"/>
		<input type="hidden" name="shopId" id="shopId" value="${shopId}"/>
		<input type="hidden" name="staffId" id="staffId" value="${staffId}"/>
		<input type="hidden" name="secToken" id="secToken" value="${secToken}"/>
		<!--套餐信息 start-->
	<div class="wf-list change-info sub-new">
		<div class="wf-ait clearfix">
			<c:choose>
				<c:when test="${minCost == 0}">
					<dl>
						<dt><img src="${ctxStatic}/images/kd/familynet-logo.jpg"/></dt>
						<dd>
							<h4>组家庭网送宽带</h4>
							<p>套餐内容：100M光纤宽带免费用，4K高清互联网电视20元/月，光猫、机顶盒费用全免。</p>
						</dd>
					</dl>
				</c:when>
				<c:when test="${minCost == 48}">
					<dl>
						<dt><img src="${ctxStatic}/images/kd/familynet-logo.jpg"/></dt>
						<dd>
							<h4>${minCost}元保底套餐</h4>
							<p>享光纤宽带、高清电视最低20元/月</p>
						</dd>
					</dl>
				</c:when>
				<c:when test="${minCost == 88}">
					<dl>
						<dt><img src="${ctxStatic}/images/kd/88.jpg"/></dt>
						<dd>
							<h4>${minCost}元保底套餐</h4>
							<p>享光纤宽带、高清电视最低20元/月。</p>
						</dd>
					</dl>
				</c:when>
				<c:when test="${minCost == 98}">
					<dl>
						<dt><img src="${ctxStatic}/images/kd/98.jpg"/></dt>
						<dd>
							<h4>${minCost}元保底套餐</h4>
							<p>套餐内容：100M光纤宽带免费用，4K高清互联网电视20元/月。</p>
						</dd>
					</dl>
				</c:when>
				<c:when test="${minCost == 158}">
					<dl>
						<dt><img src="${ctxStatic}/images/kd/familynet-logo.jpg"/></dt>
						<dd>
							<h4>${minCost}元保底套餐</h4>
							<p>套餐内容：100M光纤宽带免费用，4K高清互联网电视20元/月，光猫、机顶盒费用全免。</p>
						</dd>
					</dl>
				</c:when>
			</c:choose>
		</div>
	</div>
	<!--套餐信息 end-->

	<!--填写信息 start-->
	<div class="wf-list sub-info sub-new">
      <div class="wf-ait clearfix">
        <%--<ul class="wf-con clearfix">--%>
          	<%--<li>--%>
           	  <%--<span class="font-gray" style="line-height:35px;">手机：</span>--%>
           	  <%--<div class="sub-text"><input type="text" id="installPhoneNum" name="installPhoneNum" value="" class="form-control" placeholder="请输入手机号码" /></div>--%>
          	<%--</li>--%>
			<p class="pd10">方式1：
				<input type="text" id="installPhoneNum" name="installPhoneNum" value="" style="width:80%" class="form-control form-con sjh-jd" placeholder="请输入手机号码" />
				<%--<input id="servicePhoneNo" type="tel" maxlength="11" style="width:80%" class="form-control form-con sjh-jd" placeholder="请输入要办理业务的手机号" />--%>
			</p>
			 <%--<p class="pd10">方式2：对多个号码进行业务办理请点击<a id="batchBtn" href="javascript:void(0);" class="font-blue" style="padding-right:10px">批量办理</a></p>--%>
			<p class="pd10">方式2：分享给用户自行办理<a id="shareBtn" href="javascript:void(0);">
				<img src="${ctxStatic}/images/fx-icon.png" style="width: 30px;"></a>
			</p>
        <%--</ul>--%>
      </div>
   </div>
   </form>
</div>

<div class="fix-br">
	<div class="affix foot-box new-foot">
		<div class="container active-fix">
			<div class="fl-left"><a href="javascript:void(0);" data-toggle="modal" data-target="#myModal">温馨提示</a></div>
			<div class="fl-right"><a id="operBtn">下一步</a></div>
		</div>
	</div>
</div>

<!--温馨提示弹框 start-->
<div class="modal fade modal-prompt" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-dialog new-modal">
    <div class="modal-dow">
      <h4>温馨提示</h4>
      <div class="modal-text">
      	<c:choose>
			<c:when test="${minCost == 0}">
				<p>（1）仅限大于等于38元的4G套餐用户办理，办理后不可降档。</p>
				<p>（2）必须办理魔百和业务。</p>
				<p>（3）用户必须已组建大于等于3人的非0元档家庭网（未组建必须先组建家庭网，不含家庭网体验用户），即可享受宽带免费用。一个家庭网仅限主卡办理一次。</p>
				<p>（4）宽带免费优惠期限为两年，优惠到期后按标准资费收取宽带月费（40/50元/月），并下发短信提醒至办理号码。</p>
				<p>（5）优惠期限内，若主卡套餐降档至38元以下，或者家庭网成员退出、离网或家庭网取消时，宽带免费优惠期限终止，系统直接按标准资费收取宽带月费，同时用户可以办理其他档次消费叠加型。</p>
			</c:when>
			<c:when test="${minCost == 48}">
				<p>（1）办理号码承诺保底消费不低于48元/月，保底时间为12个月，承诺期内不允许降档、取消保底。</p>
				<p>（2）互联网高清电视第一年10元/月，从第13个月起按20元/月从手机账户上扣取。</p>
				<p>（3）老用户宽带到期前参与本活动时，宽带的余额次月自动转入捆绑手机号码的现金存折（不可清退），可用于抵扣手机套餐费用。套餐、宽带当月生效，办理当月按天收取保底费用，当月宽带月功能费、互联网电视月功能费，不计入整体优惠期。</p>
			</c:when>
			<c:when test="${minCost ==88}">
				<p>1、保底时间24个月，承诺保底期内不降挡或取消保底，24个月后按30元/月宽带功能费；</p>
				<p>2、魔百和标准资费为20元/月，从手机账户上扣取；</p>
				<p>3、保底套餐当月办理，当月生效，当月按天扣费并分摊，宽带和魔百和免办理当月月功能费，不计入整体优惠期；</p>
				<p>4、手机号码停机，宽带、电视停机（宽带电视延迟3天停机，3天内手机复机，宽带电视不停机），手机号码复机，宽带、电视立即复机，手机销号或停机120天以上，同步取消宽带、电视和手机号码的绑定关系，终止宽带、电视。</p>
			</c:when>
			<c:when test="${minCost == 98}">
				<p> 1、保底时间24个月，承诺保底期内不降挡或取消保底，24个月后按20元/月宽带功能费；</p>
				<p> 2、魔百和标准资费为20元/月，从手机账户上扣取；</p>
				<p> 3、保底套餐当月办理，当月生效，当月按天扣费并分摊，宽带和魔百和免办理当月月功能费，不计入整体优惠期；</p>
				<p> 4、手机号码停机，宽带、电视停机（宽带电视延迟3天停机，3天内手机复机，宽带电视不停机），手机号码复机，宽带、电视立即复机，手机销号或停机120天以上，同步取消宽带、电视和手机号码的绑定关系，终止宽带、电视。</p>
				<p> 5、办理成员人数需达到4人及以上，不区分主副卡。同一集团客户只能参与一次本次活动</p>
			</c:when>
			<c:when test="${minCost == 158}">
				<p>（1）仅限家庭网用户办理。</p>
				<p>（2）同一家庭网成员只能办理一次。</p>
				<p>（3）惠期12个月，12个月内不能转出，到期之后按标准资费收取月费。</p>
				<p>（4）家庭网成员合计消费承诺达到158元/月，如果不足约定金额，系统将自动从主卡手机帐号扣费补足差额。</p>
			</c:when>
		</c:choose>
      </div>
      <div class="modal-btn"><a href="javascript:void(0);" data-dismiss="modal">确定</a></div>
    </div>
 </div>
</div>
<!--温馨提示弹框 end-->
<script type="text/javascript" src="${ctxStatic}/js/rem.js"></script>
<script type="text/javascript">
	$(function(){
		var isMobile =/^1(([0-9][0-9]))\d{8}$/;
		$("#operBtn").click(function(){
			if($("#installPhoneNum").val() == ""){
				showAlert("请输入办理用户手机号码！");
				return;
			}
			if(!isMobile.test($("#installPhoneNum").val())){
                showAlert("手机号码格式错误,手机号码为11位数字");
                return ;
            }
			$("#form1").submit();
		});
	});
</script>

<script type="text/javascript">
    var changeUrl="<%=request.getContextPath()%>/broadband/getChildrenByPid";
    var submitUrl="<%=request.getContextPath()%>/broadband/broadBandBook";
    var gotoSuccessUrl="<%=request.getContextPath()%>/broadband/gotoBookInstallSuccess";
    var gotoErrorUrl="<%=request.getContextPath()%>/broadband/gotoBookInstallError";
</script>
</body>
</html>