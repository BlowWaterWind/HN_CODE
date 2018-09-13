<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <%@include file="/WEB-INF/views/include/head.jsp" %>
    <meta name="WT.si_n" content="自助排障" />
    <meta name="WT.si_x" content="业务详情" />
    <link href="${ctxStatic}/css/swiper.min.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/css/main.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/css/wt-sub.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/css/oil.css" rel="stylesheet" type="text/css">
    <link href="${ctxStatic}/css/kd.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/css/media-style.css" rel="stylesheet" type="text/css" />
    <%@ include file="/WEB-INF/views/include/message.jsp"%>
    <title>湖南移动网上营业厅</title>
</head>

<body>

<div class="top container">
		<div class="header sub-title"><a class="icon-left" href="javascript:void(0);" onclick="window.history.back()"></a>
				<h1>自助排障</h1>
		</div>
</div>
<div class="container bg-gray">
		 <!--自助排障 start-->
		 <div class="pz-con">
		 	  <h4 class="font-blue">常见问题：</h4>
		 	  <div class="zcpz-con" >
		 	  	  <div class="pz-text">
		 	  	  	  <h5>一、家庭宽带简介</h5>
		 	  	  	  <p class="font-blue" msg="移动家庭宽带是移动在全业务竞争时代推出的一项全新服务，它采用高速光纤传输，提供优质的高速接入互联网的服务，从而为人们创导一种全新的网络生活方式。">1.什么是移动家庭宽带？</p>
		 	  	  	  <p class="font-blue" msg="互联网不同于传统交换网，客户实际使用的网络带宽是动态变化的，主要取决于客户所访问的内容提供商的带宽、骨干带宽和运营商提供给客户的接入带宽。移动公司向客户提供宽带业务时，按下行速率最高可达进行承诺。下行速率最高可达是客户终端（不含）到运营商设备能支持的最大信息传送能力，是在理想网络条件下，客户上网可能达到的最高速率，移动公司对达到最高速率的时间段和时间比例不作承诺。">2.网速带宽说明？</p>
		 	  	  </div>
		 	  	  <div class="pz-text">
		 	  	  	  <h5>二、宽带故障处理</h5>
		 	  	  	  <p class="font-blue" msg="自助排障方法：（1）请先确认ONT（光猫）、路由器是否有电。（2）是否安装家用路由器,若有：去掉路由器，电脑直接连接ONT拨号，如果能上网说明是客户路由器问题；若无：电脑直接连接ONT拨号，根据拨号错误码进行处理。（3）电脑网线连接是否正常，若不正常：拔下网线后重新连接；正常：查看账号密码是否输入错误，将上网密码也重新输入一遍,然后点一下连接，看是否能够正常上网。账号密码输入正确，查看其它故障现象。">1.不能连接上网？</p>
		 	  	  	  <p class="font-blue" msg="自助排障方法：（1）部分网页打不开：建议您咨询您身边的朋友或者用其他电脑测试可不可以打开这部分网页。若其他电脑可以打开，建议您尝试关闭防火墙或上网助手再试一下，如果还是打不开，建议您检查您的电脑是否设置了过滤功能，阻止浏览这些网页，检测您的IE浏览器。若其他电脑不能打开应该是对方网站服务器出现问题，具体情况请您咨询被访问网站提供商，或者稍后再尝试一下。（2）全部网页打不开：建议检查电脑的DNS相关设置。在电脑桌面的“网上邻居”点右键－>属性－>本地连接点右键－>TCP/IP协议查看DNS设备情况。正确DNS地址应为：主用：211.142.211.124备用：111.8.14.18，或者修改DNS为自动获取。也可以运行《湖南移动10086光宽带管家》，点击“电脑体检”按钮，检测电脑网络配置，如发现DNS问题，点击“修复DNS”可自动修复。">2.部分或全部网页打不开？</p>
		 	  	  	  <p class="font-blue" msg="自助排障方法：上网会遇到网页打不开、下载中断、或者在线视频、音频流中断、QQ掉线、游戏掉线等现象。掉线涉及到几个方面的问题，包括线路故障(线路干扰)、网卡故障(速度慢、驱动程序陈旧)等。当您遇到频繁掉线时，请确认是单机上网，还是多机共享上网。若是多机共享上网，请使用单机拨号上网检测，单机拨号上网时仍出现此现象可以做如下几方面的检查：（1）检查一下家里线路。（2）电脑查杀病毒，有能力情况下可重新安装系统。 （3）如机器使用有双网卡，卸载一块网卡测试。（4）安装防火墙软件后限制：检查安装的防火墙、共享上网的代理服务器软件、上网加速软件等，停止运行这类软件后，再上网测试，看速度是否恢复正常。如果上网不稳定，可以尝试先关闭防火墙，测试稳定与否，在进行相应的设置。其他故障现象。">3.上网经常断线？</p>
		 	  	  	  <p class="font-blue" msg="自助排障方法： （1）请客户进行单机测速(要求不打开其他应用的情况下)，根据测试所得数据，判断测速数据是否正常，若不正常，检查电脑是否已安装杀毒软件。（2）若电脑本身速度很慢，而又没有安装杀毒软件，则可能客户电脑中毒，请客户杀毒后再试。（3）请检查电脑配置是否偏低，请参照主流电脑配置升级您的电脑。（4）检查是否安装了过多的应用软件,将一些不必要的应用软件卸载掉。（5）检查是否打开的窗口太多，占用了内存。（6）检查是否C盘（系统盘）空间不够，删除一些临时文件。">4.网速慢？</p>
		 	  	  	  <p class="font-blue" msg="自助排障方法：（1）请确认输入的上网帐号（上网客户名）或密码填写是否准确无误后，重新再输入一次。请注意客户名和密码都需要区分大小写。（2）请确认宽带是否存在欠费暂停，如果帐户金额不足或到期需续费开通。">5.客户名密码错（系统提示错误代码691/619）</p>
		 	  	  	  <p class="font-blue" msg="自助排障方法：（1）把电脑上的网线重新插拔再试。（2）重启电脑，关掉防火墙再试。（3）查看网卡状态：若网卡异常，则卸载网卡驱动程序，然后重新安装；若网卡正常，则卸载拨号程序，然后重新安装。（4）首次使用则需创建拨号连接。">6.远程计算机没有响应（系统提示错误代码XP系统报错678，Vista系统报错815，Win7系统报错651）</p>
		 	  	  	  <p class="font-blue" msg="自助排障方法：734/720错误常见于使用Windows XP自带拨号软件上网时出现的一个故障提示，可重新建立拨号连接，如不行就可以判断为电脑问题。">7. 连接控制协议中止（系统提示错误代码734/720）</p>
		 	  	  	  <p class="font-blue" msg="自助排障方法：建议用户自行处理系统盘文件。">8. 用户系统盘已无空间(系统提示错误代码1019)</p>
		 	  	  	  <p class="font-blue" msg="自助排障方法：（1）电脑桌面上找到网络图标，按鼠标“右键”，选择属性，打开“网络连接”窗口(或者选择左下角的“开始”－> “程序”－>“附件”－>“通讯”－>“网络连接”)。(2)当前“本地连接”是灰色的，并且下面已经显示禁用两字。请按鼠标“右键”选择“启用”，系统就会自动启用本地连接。当本地连接启动好后，可重新拨号尝试能否上网。(3)如果在桌面上没有“网上邻居”，请把鼠标放在最下面的“开始”菜单，选择“程序”－>“附件”－>“通讯”－>“网络连接”，打开网络链接窗口后，选中“本地连接”图标，按鼠标“右键”选择“启用”，系统就会自动启用本地连接。当本地连接启动好后，可重新拨号进行测试。(4)若找不到“本地连接”图标，可在桌面上点击“我的电脑”图标按鼠标右键选择“属性”，点击“硬件”－>“设备管理器”－>“网卡或网络适配器”，若网卡标识出现红叉或打了一个黄色的“！”号，请右键点击它选择卸载，然后右键点击其中一个图标选择“扫描硬件改动”，您会看到“发现新硬件”的提示，网卡或网络适配器高有了一个红叉或打了一个黄色的“！”号，确认后请尝试连接网络。如若找不到“网卡或网络适配器”，表示电脑“网卡”没有正常工作或出现硬件故障，可对电脑重启，若仍无法解决可打开电脑机箱把网卡拔出重新换一个插槽尝试。">9.找不到指定目标错误(系统提示错误代码769)</p>
		 	  	  	  <p class="font-blue" msg="自助排障方法：重新建立PPPOE拨号设置，同时检查网线是否正确连接。">10.拨号网络网络连接的设备已经断开(系统提示错误代617)</p>
		 	  	  </div>
		 	  </div>
		 	  <div class="page-info" style="display:none">
 	 	  <div class="page-text">
 	 	  	 <span class="page-tu"></span>
 	 	  	 <div class="page-rt">
	 	  	 	  <span class="arrow-icon"></span>
 	 	  	 	 <div id="ans_message" class="page-con"></div>
 	 	  	 </div>
 	 	  </div>
	 	 </div>
	 	 <a href="##" class="p-link" style="display:none">返回问题&gt;&gt;</a>
		 </div>
		 <!--自助排障 end-->
</div>
<div class="afix-box container">
	<div class="pz-afix">若自助排查后仍无法解决宽带问题，请拨打移动服务热线<b class="font-blue">10086</b></div>
</div>

<script type="text/javascript">
  	 $(function(){
					  $("p[class=font-blue]").click(function(){
						  $("#ans_message").html($(this).attr("msg"));
						   	$(".zcpz-con").fadeToggle();
							   $(".page-info").fadeToggle();
							   $(".p-link").fadeToggle();
       });
					  $(".p-link").click(function(){
										$(".zcpz-con").fadeToggle();
										$(".page-info").fadeToggle();
										$(".p-link").fadeToggle();
	      });
				});
</script>

</body>
</html>