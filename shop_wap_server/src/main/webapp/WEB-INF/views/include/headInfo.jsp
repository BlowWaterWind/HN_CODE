<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%--<%@ attribute name="whiteTop" type="java.lang.String" required="false" description=""%>--%>
<%--<%@ attribute name="cartTop" type="java.lang.String" required="false" description=""%>--%>
<script type="text/javascript" src="${ctxStatic}/js/nav.js"></script>

<a href="javascript:void(0);" class="icon-right" id="whiteTop"></a>

<a href="${ctx}/cart/linkToCartList" class="icon-top-cart" id="cartTop"><span class="badge" id="cartNum" style="display:none;"></span></a>
<ul class="nav-slide">
    <li class="nav-arrow"></li>
    <li class="nav-arrow"></li>
    <li><a href="${ctx}"><i class="icon-nav-wt-home"></i>移动商城</a></li>
    <%--<li><a href="号卡-首页.html"><i class="icon-nav-lh"></i>靓号</a></li>--%>
    <li><a href="${ctx}goodsQuery/linkToGoodsList?categoryId=10000001&queryListpageCode=PHONE_LISTPAGE_B2C&queryListpageId=100"><i class="icon-nav-home"></i>选机中心</a></li>
    <li><a href="${ctx}goods/T1SaETBsZT_RXx1p6K.html"><i class="icon-nav-ss"></i>最新活动</a></li>
    <li><a href="${ctx}memberInfo/toMemberCenter"><i class="icon-nav-wo"></i>个人中心</a></li>
</ul>
<div class="modal-bg"></div>
<script type="text/javascript" language="javascript">
    $(document).ready(function () {
        var whiteTop = "${whiteTop}";
        var cartTop = "${cartTop}";
        if(whiteTop =="1"){
            $("#whiteTop").addClass("icon-lr02");
            $("#cartTop").addClass("icon-cart02");
        }
        if(cartTop =="1"){
            /**购物车点击编辑本页js操作*/
        	$("#cartTop").replaceWith('<a href="javascript:void(0)" id="editOrSave"  class="icon-top-cart icon-editor" param="0">编辑</a>');
        }else if(cartTop =="2"){
            $("#cartTop").replaceWith('<a href="${ctx}login/logout" class="recot" onclick="appLoginLogout()">注销</a>');
        }else if(cartTop =="3"){
            $("#cartTop").replaceWith('<a href="${ctx}/" id="editOrSave"  class="icon-top-cart icon-editor" param="0">完成</a>');
        } else if(cartTop =="4"){
            $("#cartTop").replaceWith('');
        }
        $("#cartNum").hide();
        getCartNum();

    });
    

    /**
     * 购物车数量
     */
    function getCartNum() {
        $.ajax({
                    type: "POST",
                    url: "${ctx}cart/getCartCount",
                    dataType: "json",
                    success: function (data) {
                        count = "";
                            var count = data;
                            if (count != undefined) {
                                if (count > 99) {
                                    count = "99+";
                                }
                            } else {
                                count = "";
                            }
                            $("#cartNum").text(count);
                            $("#cartNum").show();
                            if(count=="0"){
                                $("#cartNum").hide();
                            }
                    }
                }

        );
    }

</script>