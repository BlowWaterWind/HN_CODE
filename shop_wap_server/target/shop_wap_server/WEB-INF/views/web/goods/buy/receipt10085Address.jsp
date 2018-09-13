<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script type="text/javascript">
    window.onload = function(){
        queryAddress('0','-1');
    }
    function queryAddress(aim,flag){//第一个参数是用户点击获得，第二个参数页面加载的时候用到
        var address;
        if(flag=='-1'){//页面首次加载，地址为空说
            address = '-1';
        }else if(flag=='0'){//用户点击
            address=jQuery("#address"+aim).find('option:selected').attr("regnCode");
        }

        $.ajax({
            type:'post',
            url:'${ctx}/addressQuery',
            data:{
                superRegnCode:address
            },
            cache:true,
            success:function(data){
//                alert(data);
                //处理aim+1;Number(aim)+1
                var num = (Number(aim)+1);
                var aimId = "#address"+num;

                //解析json串
                var options = "";
                for(var i =0;i<data.length;i++){
                    var address = data[i];
                    options+= "<option value='"+address.regnName+"' zip='"+address.zipCD+"' regnCode='"+address.regnCode+"' supRegnCode='"+address.superRegnCode+"'>"+address.regnName+"</option>";
                }
                $(aimId).empty();
                $(aimId).append(options);


                //继续获取默认的第一个
                if(num<3)
                    queryAddress(num,'0');
                else
                    modifyShow('1');
            },
            error:function(){
                showAlert("数据获取失败");
            }
        });
    }
    /*提交订单*/
    function submit10085Order(){


        //校验是否勾选同意服务协议
        if(!$("#agreeCB").is(":checked")){
            showAlert('2','请阅读并同意请《营销案购买协议》和《购前须知》');
            return;
        }
        $("#confirmOrderForm").submit();
    }
function modifyShow(aim){
    if(aim=='1'){
        //修改地址
        //修改邮编从最小的地区寻找，一旦寻找到则终止
        var zip3 = $("#address3").find('option:selected').attr('zip');
        var zip2 = $("#address2").find('option:selected').attr('zip');
        var zip1 = $("#address1").find('option:selected').attr('zip');
        if(zip3!=null&&zip3!='undefined'&&zip3!=''){
            $("#memZip").val(zip3);
        }else if(zip2!=null&&zip2!='undefined'&&zip2!=''){
            $("#memZip").val(zip2);
        }else if(zip1!=null&&zip1!='undefined'&&zip1!=''){
            $("#memZip").val(zip1);
        }else{
            $("#memZip").val("000000");
        }

        var address = $("#address1").val()+$("#address2").val()+($("#address3").val()==null?"":$("#address3").val())+$("#detailAddress").val();

        $("#receiptAddressSP").text(address);
    }else if(aim=='2'){
        //修改姓名
        var memName = $("#memName").val();
        $("#receiptNameSP").text(memName);
    }
}

</script>
<div class="qr-dl">
        <div class="qr-con">
            <table class="addressInput">
                <tr>
                    <td>选择地区:</td>
                    <td>
                        <select attr="memberRecipientProvince" onchange="queryAddress('1','0');" id="address1"><option value="beijing">北京</option></select><br/>
                        <select attr="memberRecipientCity" id="address2" onchange="queryAddress('2','0');"; ></select><br/>
                        <select attr="memberRecipientCounty" id="address3" onchange="modifyShow('1');"></select><br/>
                    </td>
                </tr>
                <tr>
                    <td>详细地址:</td>
                    <td>
                        <input type="text" attr="memberRecipientAddress" id="detailAddress" onblur="modifyShow('1');"/>
                    </td>
                </tr>
                <tr>
                    <td>收货姓名:</td>
                    <td>
                        <input type="text" attr="memberRecipientName" id="memName" onblur="modifyShow('2');"/>
                    </td>
                </tr>
                <tr>
                    <td> 手机号码:</td>
                    <td>
                        <input type="number" attr="memberRecipientPhone" id="phNumber"/><br/>
                    </td>
                </tr>
                <tr>
                    <td>邮政编码:</td>
                    <td>
                        <input type="number" attr="memberRecipientZip" id="memZip" /><br/>
                    </td>
                </tr>
                <tr>
                    <td>邮箱:</td>
                    <td>
                        <input type="email" attr="email" id="memEmail"/>
                    </td>
                </tr>
            </table>
            <input type="hidden" name="memberAddress.memberRecipientName" id="memberRecipientName"/>
            <input type="hidden" name="memberAddress.memberRecipientPhone" id="memberRecipientPhone"/>
            <input type="hidden" name="memberAddress.memberIsDefault" id="memberIsDefault"/>
            <input type="hidden" name="memberAddress.memberRecipientProvince" id="memberRecipientProvince"/>
            <input type="hidden" name="memberAddress.memberRecipientCity" id="memberRecipientCity"/>
            <input type="hidden" name="memberAddress.memberRecipientCounty" id="memberRecipientCounty"/>
            <input type="hidden" name="memberAddress.memberRecipientAddress" id="memberRecipientAddress"/>
            <input type="hidden" name="memberAddress.email" id="memberRecipientEmail"/>
            <input type="hidden" name="memberAddress.zip" id="memberRecipientZip"/>
        </div>
</div>