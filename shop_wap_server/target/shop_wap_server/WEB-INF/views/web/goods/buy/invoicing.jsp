<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="sp-pj sp09">
	<a id="chooseInvoiceBtn" href="javascript:void(0)">
		<span class="gm-sl">是否开发票</span>
		<c:choose>
			<c:when test="${empty carModel.isInvoicing}">
				<input type="hidden" name="isInvoicing" value="0"/>
				<span class="gm-zj gm-zj02">不开</span>
			</c:when>
			<c:otherwise>
				<input type="hidden" name="isInvoicing" value="${carModel.isInvoicing}"/>
				<input type="hidden" name="invoicingTitle" value="${carModel.invoicingTitle}"/>
					<span class="gm-zj gm-zj02">
						<c:choose>
							<c:when test="${carModel.isInvoicing == 1 }">
								开
							</c:when>
							<c:when test="${carModel.isInvoicing == 0 }">
								不开
							</c:when>
						</c:choose>
					</span>
			</c:otherwise>
		</c:choose>
		<span class="sp-icon"></span>
	</a>
</div>