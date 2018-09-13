<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="cur-lt">
	<div class="sp-info sp03 sp-top sd-info sd-info02">
		<a id="recommenderBtn" href="javascript:void(0)">
			<i class="sd-icon01 sd-icon03"></i>
			<div class="pj-lt01">填写推荐人信息
				<span id="recommenderInfo">&nbsp;&nbsp;
					${carModel.recommendContact.recommendContactNo}&nbsp;
					${carModel.recommendContact.recommendContactName}&nbsp;
					${carModel.recommendContact.recommendContactPhone}
				</span>
				<input type="hidden" name="recommendContact.recommendContactNo" value="${carModel.recommendContact.recommendContactNo}"/>
				<input type="hidden" name="recommendContact.recommendContactName" value="${carModel.recommendContact.recommendContactName}"/>
				<input type="hidden" name="recommendContact.recommendContactPhone" value="${carModel.recommendContact.recommendContactPhone}"/>
			</div>
			<i class="sp-icon sp-icon04"></i>
		</a>
	</div>
</div>