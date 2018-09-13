<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="sp-pj sp09">
	<a id="networkInfoBtn" href="javascript:void(0);">
		<span class="gm-sl">填写入网信息</span>
		<span class="gm-zj gm-zj02">
			${carModel.orderDetailSim.regName}&nbsp;&nbsp;
			${carModel.orderDetailSim.psptId}
		</span>
		<span class="sp-icon"></span>
		<input type="hidden" name="orderDetailSim.phone" value="${carModel.orderDetailSim.phone}"/>
		<input type="hidden" name="orderDetailSim.preFee" value="${carModel.orderDetailSim.preFee}"/>
		<input type="hidden" name="orderDetailSim.simProductId" value="${carModel.orderDetailSim.simProductId}"/>
		<input type="hidden" name="orderDetailSim.baseSet" value="${carModel.orderDetailSim.baseSet}"/>
		<input type="hidden" name="orderDetailSim.baseSetName" value="${carModel.orderDetailSim.baseSetName}"/>
		<input type="hidden" name="orderDetailSim.regName" value="${carModel.orderDetailSim.regName}"/>
		<input type="hidden" name="orderDetailSim.psptId" value="${carModel.orderDetailSim.psptId}"/>
		<input type="hidden" name="orderDetailSim.psptAddr" value="${carModel.orderDetailSim.psptAddr}"/>
	</a>
</div>