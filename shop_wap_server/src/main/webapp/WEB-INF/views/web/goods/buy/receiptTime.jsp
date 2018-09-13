<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="sp-pj sp09" id="receiptTimeIdContainer">
	<a id="chooseReceiptTimeBtn" href="javascript:void(0)">
		<span class="gm-sl">收货时间段</span>
		<c:choose>
			<c:when test="${empty carModel.receiptTime}">
				<input type="hidden" name="receiptTime.receiptTimeId" value="2"/>
				<span class="gm-zj gm-zj02">只工作日送货</span>
			</c:when>
			<c:otherwise>
				<input type="hidden" name="receiptTime.receiptTimeId" value="${carModel.receiptTime.receiptTimeId}"/>
				<span class="gm-zj gm-zj02">${carModel.receiptTime.receiptTimeName}</span>
			</c:otherwise>
		</c:choose>
		<span class="sp-icon"></span>
	</a>
</div>