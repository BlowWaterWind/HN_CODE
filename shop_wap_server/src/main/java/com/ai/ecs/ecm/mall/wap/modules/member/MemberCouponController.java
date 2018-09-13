package com.ai.ecs.ecm.mall.wap.modules.member;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.ecs.ecm.mall.wap.modules.BaseController;
import com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils;
import com.ai.ecs.member.entity.MemberVo;
import com.ai.ecs.sales.api.ICouponService;
import com.ai.ecs.sales.entity.CouponInfo;

@Controller
@RequestMapping("memberCoupon")
public class MemberCouponController extends BaseController {
	@Autowired
	ICouponService couponService;

	@Value("${ssoServerHost}")
	private String ssoServerHost;

	@RequestMapping(value = "/myCoupon", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public String myOtherCoupon(HttpServletRequest request,
			HttpServletResponse response, Model model, Integer status) {
		if (status == null) {
			status = 0;
		}
		MemberVo member = UserUtils.getLoginUser(request);
		if (member != null) {
			List<CouponInfo> couponList = couponService
					.getCouponInfoByMemberId(member.getMemberLogin()
							.getMemberId(), status);
			request.setAttribute("couponList", couponList);
			request.setAttribute("status", status);
		}
		return "web/member/myCoupon";
	}

	@RequestMapping(value = "/deleteCoupon", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String deleteCoupon(HttpServletRequest request,
			HttpServletResponse response, String couponId) {
		try {
			if (StringUtils.isNotEmpty(couponId)) {
				String[] ids = couponId.split(",");
				for(String id:ids){
				MemberVo member = UserUtils.getLoginUser(request);
				Map<String, String> map = new HashMap<String, String>();
				map.put("couponId", id);
				if (member != null && member.getMemberLogin() != null) {
					map.put("operator", member.getMemberLogin()
							.getMemberLogingName());
				}
				couponService.deleteCouponInfo(map);
				}
			}
		} catch (Exception e) {
			return "删除失败";
		}
		return "success";
	}

}
