<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<div class="qr-dl">
	<a id="chooseReceiptAddressBtn" href="javascript:void(0)">
		<i class="icon-default05 pull-left"></i>
		<div class="qr-con">
			<c:choose>
				<c:when test="${empty carModel.memberAddress}">
					<div class="pull-left">新增地址</div>
					<div class="clear"></div>
				</c:when>
				<c:otherwise>
					<div class="pull-left">收货人：${carModel.memberAddress.memberRecipientName}</div>
					<div class="pull-right">${carModel.memberAddress.memberRecipientPhone}</div>
					<div class="clear"></div>
					<div class="address address-con">
						收货人地址：${fn:split(carModel.memberAddress.memberRecipientProvince,":")[1]}
							${fn:split(carModel.memberAddress.memberRecipientCity,":")[1]}
							${fn:split(carModel.memberAddress.memberRecipientCounty,":")[1]}
							${carModel.memberAddress.memberRecipientAddress}
					</div>
					<input type="hidden" name="memberAddress.memberAddressId" value="${carModel.memberAddress.memberAddressId}"/>
					<input type="hidden" name="memberAddress.memberRecipientName" value="${carModel.memberAddress.memberRecipientName}"/>
					<input type="hidden" name="memberAddress.memberRecipientPhone" value="${carModel.memberAddress.memberRecipientPhone}"/>
					<input type="hidden" name="memberAddress.memberRecipientProvince" value="${fn:split(carModel.memberAddress.memberRecipientProvince,":")[1]}"/>
					<input type="hidden" name="memberAddress.memberRecipientCity" value="${fn:split(carModel.memberAddress.memberRecipientCity,":")[1]}"/>
					<input type="hidden" name="memberAddress.memberRecipientCounty" value="${fn:split(carModel.memberAddress.memberRecipientCounty,":")[1]}"/>
					<input type="hidden" name="memberAddress.memberRecipientAddress" value="${carModel.memberAddress.memberRecipientAddress}"/>
				</c:otherwise>
			</c:choose>
		</div>
		<i class="icon-default06 pull-right"></i>
	</a>
</div>