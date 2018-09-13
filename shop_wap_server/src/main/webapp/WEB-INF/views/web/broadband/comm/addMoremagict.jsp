<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!--魔百盒 start-->
<div class="wf-ait clearfix">
    <a href="javascript:void(0)" class="install-btn install-icon01" id="openMoremagic">新增魔百和</a>

    <input type="hidden" name="moreMagicTabShow" id="moreMagicTabShow" value="false"/>

    <div class="bar-list install-content txt" style="display:none;" id="moremagicShow">
        <!--选择套餐 start-->
        <div class="change-tc mb-change mb-list">

            <div class="renew-content nofiexd" style="display: block">
                <ul class="clearfix">
                    <li class="clearfix bg-grayf2">
                        <div class="mb-text">
                            <span class="">播放内容品牌：</span>
                            <input type="hidden" name="TV_PRODUCT_ID" id="TV_PRODUCT_ID"/>
                            <input type="hidden" name="REQ_ELEMENT_ID" id="REQ_ELEMENT_ID"/>
                            <input type="hidden" name="GIFT_ELEMENT_ID" id="chooseGiftElementId"/>
                            <input type="hidden" name="term" id="term"/>
                            <div class="mb-rt">
                                <label><span class="pull-left"><input type="radio" name="tvType" class="cbox" value="20142000" attr-text="芒果TV"/>芒果TV</span><span class="font-gray">湖南卫视优质点播资源，芒果自制节目及其他热门电影，电视剧，综艺等</span></label>
                                <label><span class="pull-left"><input type="radio" name="tvType" class="cbox" value="20141000" attr-text="未来电视"/>未来电视</span><span class="font-gray">央视优质点播资源，独有电视看点及超长回看，更有腾讯专区等特色板块</span></label>
                            </div>
                        </div>
                    </li>
                </ul>
                <!--添加办理按钮 start-->
                <%--<div class="addBorad-box"><a href="javascript:;" class="addBorad-btn">添加办理</a></div>--%>
                <!--添加办理按钮 end-->
            </div>

            <h2 class="renew-title">魔百和必选服务</h2>
            <ul class="add_oil select-renew clearfix" id="requireListUl">
            </ul>
            <h2 class="renew-title">魔百和可选礼包</h2>
            <ul class="add_oil select-renew clearfix" id="chooseListUl">
            </ul>
            <!--添加办理按钮 start-->
            <div class="addBorad-box"><a href="javascript:;" class="addBorad-btn">添加办理</a></div>
            <!--添加办理按钮 end-->
        </div>
        <!--选择套餐 end-->

        <!--已选套餐 start-->
        <div class="selected-wrap mb-change">
            <h2 class="selected-title">已选：</h2>
            <div class="selected-box">
                <%--<span class="selected-list">180元/年+芒果TV<i class="de-icon"></i></span>--%>
                <%--<span class="selected-list">300元/2年<i class="de-icon"></i></span>--%>
            </div>
        </div>
        <!--已选套餐 end-->

        <a href="javascript:void(0)" class="install-btn install-icon02" id="closeMoremagic">取消魔百和</a>
    </div>
</div>
<!--魔百盒 end-->

<script type="text/javascript" src="${ctxStatic}/js/artTemplate/dist/template.js"></script>
<script type="text/html" id="requireList">
    {{each requireList as mbhItem index}}
    <li item-name="{{mbhItem.broadbandItemName}}" tv_product_id="{{mbhItem.productId}}" gift_element_id="{{mbhItem.heProductId}}" REQ_ELEMENT_ID="{{mbhItem.discntCode}}" onclick="chooseProduct(this,'requireListUl')">{{mbhItem.broadbandItemName}}<s></s></li>
    {{/each}}
</script>
<script type="text/html" id="chooseList">
    {{each chooseList as mbhItem index}}
    <li item-name="{{mbhItem.broadbandItemName}}" tv_product_id="{{mbhItem.productId}}" gift_element_id="{{mbhItem.heProductId}}" REQ_ELEMENT_ID="{{mbhItem.discntCode}}" goods_sku_id="{{mbhItem.goodsSkuId}}" onclick="chooseProduct(this,'chooseListUl')">{{mbhItem.broadbandItemName}}<s></s></li>
    {{/each}}
</script>
<script>
    $(function() {

        //选择魔百盒
        $("#openMoremagic").click(function() {
            $(this).hide();
            $("#moremagicShow").show();
            //$(".mb-change").toggle();
            $("#moreMagicTabShow").val("true");
        });
        //取消魔百盒
        $("#closeMoremagic").click(function() {
            $("#moremagicShow").hide();
            $("#openMoremagic").show();
            $("#moreMagicTabShow").val("false");
        });

        //根据电视牌照加载魔百盒必选服务和魔百盒可选礼包
        $("input[name='tvType']").click(function() {
            var tvType=$(this).val();
            $.ajax({
                url: ctx+"mbh/getTvProductByType",
                data: {'tvProductId':tvType},
                dataType: "json",
                type : 'post',
                success: function(e){
                    $("#requireListUl").html("");
                    $("#chooseListUl").html("");
                    if(e.requireList.length>0){
                        $("#requireListUl").html(template("requireList", e));
                    }
                    if(e.chooseList.length>0){
                        $("#chooseListUl").html(template("chooseList", e));
                    }
                },
                error: function(e) {
                    showAlert("系统错误，请稍后重试！");
                }
            });
        });

        //添加套餐
        $(".addBorad-btn").click(function() {
            var tvType = $("input[name='tvType']:checked").val();
            if(!$.trim(tvType)){
                showAlert("请先选择电视牌照方");
                return;
            }
            var tvName = $("input[name='tvType']:checked").attr('attr-text');

            //必选服务
            if($("#requireListUl li[class='on']").length<1){
                showAlert("请先选择必选服务！");
                return;
            }
            //判断原有魔百盒个数 原有+新增不能超过3
            if($(".selected-box .selected-list").length>=3){
                showAlert("魔百和个数不能大于3！");
                return;
            }
            var item_name ='';
            var proinfo ='';
            $("#requireListUl li").each(function(){
                if($(this).hasClass('on')){
                     item_name +=$(this).attr('item-name')+'+';
                     proinfo +=$(this).attr('tv_product_id')+'|'+$(this).attr('REQ_ELEMENT_ID');
                    console.info(proinfo+'----');
                }
            });
            $("#chooseListUl li").each(function(){
                if($(this).hasClass('on')){
                     item_name +=$(this).attr('item-name')+'+';
                     proinfo +='|'+$(this).attr('goods_sku_id');
                    console.info(proinfo+'----');
                }
            });

            var htmlSelect='<span class="selected-list">'+item_name+tvName+'<i class="de-icon" onclick="deletePro(this)"></i>'
            htmlSelect +='<input type="hidden" name="mbh" value=\''+proinfo+'\'/></span>'
            $(".selected-box").append(htmlSelect);
        });

    });


    var proinfo;
    function submit(){
        proinfo = new Array();
        selectId="";
        $("input[name=tv_productId]").each(function(){
            var product=$(this).val();
            if(!product){
                return null;
            }
            var str=product.split('|');
            var pro ={'TV_PRODUCT_ID':str[0],'REQ_ELEMENT_ID':str[1],'GIFT_ELEMENT_ID':str[2]};
            proinfo.push(pro);
        });
    }

    function deletePro(obj){
        $(obj).parent().remove();
    }

    function chooseProduct(obj,id){
        if($(obj).hasClass("on")){
            $(obj).removeClass('on');
            $('.active-fix').addClass('kd-disabled');
        } else
        {
            $("#"+id+" li").removeClass('on');
            $(obj).addClass('on');
            $('.active-fix').removeClass('kd-disabled');
        }
    }
</script>
<script type="text/javascript" src="${ctxStatic}/js/public.js"></script>

