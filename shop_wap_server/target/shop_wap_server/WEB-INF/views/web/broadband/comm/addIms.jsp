<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!--加装固话 start-->
<div class="wf-list change-new sub-new">
    <input type="hidden" id="imsPhone" name="imsPhone"/>
    <input type="hidden"  id="ims_productId" name="imsProductId"/>
    <input type="hidden"  id="ims_packageId" name="imsPackageId"/>
    <input type="hidden"  id="ims_elementId" name="imsElementId"/>
    <input type="hidden"  id="ims_level" name="imsLevel" />
    <input type="hidden" id="imsTabShow" name="imsTabShow" value="false"/>
    <div class="wf-ait clearfix">
        <a href="javascript:void(0)" class="install-btn install-icon01" id="openIms">加装固话</a>
        <ul class="bar-list install-content txt" style="display:none;" id="imsShow">
            <li> <span class="font-gray">固话套餐：</span>
                    <div id="div_broadbandItem" class="sub-text sub-broad">

                    </div>
                <div class="tib-sm">
                    <p id="item_tip">${broadbandItem.desc}</p>
                </div>
            </li>
            <li> <span class="font-gray">优惠价：</span>
                <div class="sub-text">1元/月</div>
            </li>
            <%--<li> <span class="font-gray">号码归属：</span>--%>
                <%--<input type="hidden" name="eparchCode" id="eparchCode" />--%>
                <%--<div class="sub-text">--%>
                    <%--<div class="td-fr">--%>
                        <%--<select class="form-control" id="city_name">--%>
                            <%--<option>长沙市</option>--%>
                        <%--</select>--%>
                    <%--</div>--%>
                <%--</div>--%>
            <%--</li>--%>
            <li>
                <div class="entrance-date" data-toggle="modal" data-target="#myModal_chooseNumber">
                    <label class="font-gray">固话号码：</label>
                        <input type="text" class="entry-box" readonly placeholder="请选择固话号码"   id="chooseNumber"/>
                    <span class="arrow"></span>
                </div>

            </li>
            <%--<li>--%>
                <%--<div class="entrance-date">--%>
                    <%--<label class="font-gray">固话号码：</label>--%>
                    <%--&lt;%&ndash;<span class="font-blue">点击认证</span>&ndash;%&gt;--%>
                <%--</div>--%>
            <%--</li>--%>
            <a href="javascript:void(0)" class="install-btn install-icon02" id="closeIms">取消固话</a>
        </ul>
    </div>
</div>
<!--加装固话 end-->
<script type="text/javascript" src="${ctxStatic}/js/artTemplate/dist/template.js"></script>
<script>
    $(function() {
        //切换
        $(".sub-broad a").on("click", function() {
            $(this).siblings().removeClass("active");
            $(this).addClass("active");
        });
        //展开隐藏
        $("#openIms").click(function() {
            $(this).hide();
            $("#imsShow").show();
            $("#closeIms").show();
            $("#imsTabShow").val("true");
        });
        $("#closeIms").click(function() {
            $(this).hide();
            $("#imsShow").hide();
            $("#openIms").show();
            $("#imsTabShow").val("false");
        });
        //号码搜索
        $("#search").click(function(){
            searchIms();
        });
        /**
         * 换一批号码
         * */
        $("#changeIms").click(function(){
            searchIms();
        })
        /**
         * 号码池初始化
         * */
        $("#chooseNumber").click(function(){
            searchIms();
        });
        $("#div_broadbandItem").find("a").bind("click",function(){
            addParam(this);
        });

        queryProducts();
        $("#div_broadbandItem").find("a").each(function (index) {
            if($(this).hasClass('active')){
                addParam(this);
            }
        });
    });

function addParam(obj){
    var ims_productId = $(obj).nextAll("input[name='item_productId']").eq(0).val();
    var ims_packageId = $(obj).nextAll("input[name='item_packageId']").eq(0).val();
    var ims_level = $(obj).nextAll("input[name='item_productLevel']").eq(0).val();
    var ims_desc=$(obj).nextAll("input[name='item_desc']").eq(0).val();
    var ims_element=$(obj).nextAll("input[name='item_elementId']").eq(0).val();
    $("#ims_productId").val(ims_productId);
    $("#ims_packageId").val(ims_packageId);
    $("#ims_level").val(ims_level);
    $("#item_tip").html(ims_desc);
    $("#ims_elementId").val(ims_element);

}
    function queryProducts(){
        $.ajax({
            type:'post',
            url : "${ctx}/imsOnly/getProduct",
            async : false,
            context : $(this),
            dataType : 'json',
            success : function(data) {
                console.log("data",data);

                if(data.respCode=='0'&&data.list.length>0){
                    $("#div_broadbandItem").html(template("imsBroadband", data));
                }
                $("#city_name").html("").append("<option>" + data.cityName + "</option>");
//                $("#eparchCode").val(data.eparchCode);
            },
            error :function(){

            }
        });
    }


    function searchIms(){
        var key = $("#imsKey").val();
        var eparchyCode=$("#eparchyCode").val()
        $.ajax({
            type:'get',
            url : "${ctx}/imsOnly/queryImsNumbers",
            cache : false,
            context : $(this),
            dataType : 'json',
            data : {
                key : key,
                eparchCode:eparchyCode
            },
            success : function(data) {
                console.log("data",data);
                var html = "";
                $.each(data,function(index,value){
                    if(value.SERIAL_NUMBER!=''&&value.SERIAL_NUMBER!=undefined){
                        html += "<li>"+value.SERIAL_NUMBER+"</li>"
                    }
                });
                $("#numberContent").html(html);
                $("#numberContent li").bind("click",function(){
                    var imsPhone = $(this).html();
                    console.info(imsPhone);
                    $("#imsPhone").val(imsPhone);
                    $("#chooseNumber").val(imsPhone);
                    $("#myModal_chooseNumber").hide();
                });
//                var html = "<li>"+value.SERIAL_NUMBER+"</li>"
            },
            error :function(){
                showAlert("号码池查询失败!");
            }
        });
    }
</script>
<script type="text/html" id="imsBroadband">

    {{each list as broadbandItem index}}

            {{if index==0 }}
            <a href="javascript:void(0)" class="bar-btn active">
                {{else}}
                <a href="javascript:void(0)" class="bar-btn">
                    {{/if}}
                        {{broadbandItem.productLevel}}元/月</a>
                <input type="hidden" id="item_skuId" name="item_skuId" value="{{broadbandItem.goodsSkuId}}"/>
                <input type="hidden" id="item_goodsId" name="item_goodsId" value="{{broadbandItem.goodsId}}"/>
                <input type="hidden" name="item_productId" value="{{broadbandItem.productId}}"/>
                <input type="hidden" name="item_elementId" value="{{broadbandItem.giveData}}"/>
                <input type="hidden" name="item_packageId" value="{{broadbandItem.packageId}}"/>
                <input type="hidden" name="item_productLevel" value="{{broadbandItem.productLevel}}"/>
                <input type="hidden" name="item_desc" value="{{broadbandItem.desc}}"/>
                <input type="hidden" name="item_goodsName" value="{{broadbandItem.broadbandItemName}}"/>
    {{/each}}
</script>
<!--号码选择 弹框 start-->
<div class="modal fade modal-prompt" id="myModal_chooseNumber" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog broadband-dialog">
        <div class="modal-con">
            <div class="fixed-modal">
                <div class="fixed-search">
                    <input type="text" class="form-control" placeholder="生日、幸运数字等" name ="imsKey" id="imsKey"/>
                    <a class="search-icon" id="search"></a>
                </div>
                <ul class="fixed-list" id="numberContent">

                </ul>

                <!--查找无记录 start-->
                <div class="fixed-records" style="display:none;">
                    <p>很抱歉</p>
                    <p>暂无符合要求号码</p>
                </div>
                <!--查找无记录 end-->

                <div class="fixed-change"><a href="javascript:void(0)" id="changeIms">换一批</a></div>
            </div>
        </div>
    </div>
</div>
<!--号码选择 弹框 end-->

<!--提示 弹框 start-->
<div class="modal fade modal-prompt" id="myModal_confirmNumber" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog broadband-dialog">
        <div class="modal-con">
            <div class="modal-text t-c">
                <p>尊敬的用户您好</p>
                <p>您将为13812344321办理<br>98元和家庭套餐<br>+9元档固话套餐</p>
            </div>
            <div class="dialog-btn">
                <a href="javascript:void(0);" class="font-gray" data-dismiss="modal">取消</a>
                <a href="javascript:void(0);" class="font-blue" data-dismiss="modal">确认</a>
            </div>
        </div>
    </div>
</div>
<!--提示 弹框 end-->