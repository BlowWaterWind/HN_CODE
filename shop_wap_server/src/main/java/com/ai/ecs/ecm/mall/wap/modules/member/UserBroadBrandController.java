package com.ai.ecs.ecm.mall.wap.modules.member;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.ecs.ecm.mall.wap.modules.member.vo.MemberSsoVo;
import com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils;
import com.ai.ecs.ecop.sys.service.DictService;
import com.ai.ecs.ecsite.modules.broadBand.entity.BroadPwdChangeCondition;
import com.ai.ecs.ecsite.modules.broadBand.service.BroadBandService;
import com.ai.ecs.entity.base.Page;
import com.ai.ecs.goods.api.IGoodsManageService;
import com.ai.ecs.goods.entity.goods.TfGoodsInfo;
import com.ai.ecs.member.api.IUserBroadbrandService;
import com.ai.ecs.member.entity.MemberVo;
import com.ai.ecs.member.entity.UserBroadbrand;
import com.ai.ecs.order.api.IOrderQueryService;
import com.ai.ecs.order.api.IOrderService;
import com.ai.ecs.order.constant.OrderConstant;
import com.ai.ecs.order.constant.Variables;
import com.ai.ecs.order.entity.TfOrderSub;
import com.ai.ecs.order.entity.TfOrderSubDetail;
import com.ai.ecs.order.entity.TfOrderUserRef;

@Controller
@RequestMapping("userBroadBrand")
public class UserBroadBrandController {
	@Autowired
	@Qualifier("userBroadbrandService")
	IUserBroadbrandService userBroadbrandService;
	
	@Autowired
	@Qualifier("broadBandService")
	BroadBandService broadBandService;

	@Autowired
	@Qualifier("orderQueryService")
	IOrderQueryService orderQueryService;

	@Autowired
	@Qualifier("orderService")
	IOrderService orderService;
	
	@Autowired
	@Lazy
	@Qualifier("dictService")
	DictService dictService;
	
	@Autowired
	@Qualifier("goodsManageService")
	IGoodsManageService goodsManageService;
	
	@RequestMapping(value = "/toMyBroadBrand", method = RequestMethod.GET)
	public String toMyBroadBrand(HttpServletRequest request,
			HttpServletResponse response) {
		MemberVo member = UserUtils.getLoginUser(request);
		if (member != null) {
			try {
				List<UserBroadbrand> brandlist = userBroadbrandService
						.getUserBroadbrandByMemberId(member.getMemberLogin()
								.getMemberId());
				if (CollectionUtils.isEmpty(brandlist)) {
					brandlist = new ArrayList<UserBroadbrand>();
				}
				request.setAttribute("brandList", brandlist);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "web/member/broadband/myBroadbrand";
	}

	@RequestMapping(value = "/toMyBroadBrandDetail", method = RequestMethod.GET)
	public String toMyBroadBrandDetail(HttpServletRequest request,
			HttpServletResponse response, Long bandId) {
		UserBroadbrand band = null;
		try {
			band = userBroadbrandService.getUserBroadbrandByKey(bandId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (band == null) {
			band = new UserBroadbrand();
		}
		request.setAttribute("band", band);
		MemberVo member = UserUtils.getLoginUser(request);
		if (member != null && member.getMemberLogin() != null) {
			request.setAttribute("phone", member.getMemberLogin()
					.getMemberPhone());
		}
		return "web/member/broadband/myBroadbrandDetail";
	}

	@RequestMapping(value = "/toUpdatePass", method = RequestMethod.GET)
	public String toUpdatePass(HttpServletRequest request,
			HttpServletResponse response) {
		MemberVo member = UserUtils.getLoginUser(request);
		if (member != null) {
			try {
				List<UserBroadbrand> brandlist = userBroadbrandService
						.getUserBroadbrandByMemberId(member.getMemberLogin()
								.getMemberId());
				if (CollectionUtils.isEmpty(brandlist)) {
					brandlist = new ArrayList<UserBroadbrand>();
				}
				request.setAttribute("brandList", brandlist);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "web/member/broadband/myBroadbrand";
	}
	
	@RequestMapping(value = "/updatePass", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String updatePass(HttpServletRequest request,
			HttpServletResponse response) {
		try{
		MemberVo user=UserUtils.getLoginUser(request);
		MemberSsoVo ssoUser = UserUtils.getSsoLoginUser(request,null);
		BroadPwdChangeCondition pwd=new BroadPwdChangeCondition();
		if(ssoUser!=null){
			pwd.setEparchy_code(ssoUser.getRemoveEparchyCode());
		}
		if(user!=null){
			pwd.setKd_serial_number(user.getMemberLogin().getMemberPhone()+"");
		}
		broadBandService.broadPwdChange(pwd);
		}catch(Exception e){
			return "密码修改失败！";
		}
		return "success";
	}
	
	@RequestMapping(value = "/toMyAllOrderList", method = RequestMethod.GET)
	public String getMyOrderList(HttpServletRequest request,
			HttpServletResponse response, Integer pageSize, Integer pageNo) {
		MemberVo member = UserUtils.getLoginUser(request);
		if (pageSize == null) {
			pageSize = 1;
		}
		if (pageNo == null) {
			pageNo = 1000;
		}
		if (member != null) {
			Page<TfOrderSub> page = new Page<TfOrderSub>();
			TfOrderSub orderSub = new TfOrderSub();
			page.setPageSize(pageSize);
			page.setPageNo(pageNo);
			orderSub.setPage(page);
			orderSub.setOrderTypeId(OrderConstant.TYPE_BROADBAND);

			TfOrderUserRef orf = new TfOrderUserRef();
			orf.setMemberId(member.getMemberLogin().getMemberId());
			orderSub.setOrderUserRef(orf);
			Page<TfOrderSub> orderPage = orderQueryService
					.queryComplexOrderPage(orderSub, Variables.ACT_GROUP_MEMBER);
			List<TfOrderSub> orderListNew = new ArrayList<TfOrderSub>();
			if (orderPage != null
					&& !CollectionUtils.isEmpty(orderPage.getList())) {
				for (TfOrderSub order : orderPage.getList()) {
					List<TfOrderSubDetail> detaillist = order.getDetailList();
					List<TfOrderSubDetail> detailListNew = wrapGoodsUrl(request, detaillist);
					order.setDetailList(detailListNew);
					orderListNew.add(order);
				}
				orderPage.setList(orderListNew);
			}
			if (orderPage != null) {
				request.setAttribute("orderviewList", orderPage);
			}
		}
		return "web/member/broadband/myBroadbandOrder";
	}
	
	private List<TfOrderSubDetail> wrapGoodsUrl(HttpServletRequest request,
			List<TfOrderSubDetail> detaillist) {
		List<TfOrderSubDetail> detailListNew = new ArrayList<TfOrderSubDetail>();
		if (CollectionUtils.isNotEmpty(detaillist)) {
			String IMAGE_SERVER_PATH = dictService.getDictValue(
					"STATIC_IMAGE_SERVER", "STATIC_SERVER_PATH", "");
			for (TfOrderSubDetail detail : detaillist) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("containGoodsStaticInfo", true);
				map.put("containShopGoodsChannelRef", true);
				map.put("goodsSkuId", detail.getGoodsSkuId());
				List<TfGoodsInfo> goods = goodsManageService
						.queryGoodsInfoByCds(map);
				if (CollectionUtils.isNotEmpty(goods)) {
					if (CollectionUtils.isNotEmpty(goods.get(0)
							.getGoodsStaticList())
							&& StringUtils.isNotEmpty(goods.get(0)
									.getGoodsStaticList().get(0)
									.getGoodsStaticUrl())) {
						detail.setGoodsImgUrl(IMAGE_SERVER_PATH
								+ goods.get(0).getGoodsStaticList().get(0)
										.getGoodsStaticUrl());
					}
					if (CollectionUtils.isNotEmpty(goods.get(0)
							.getGoodsStaticList())
							&& StringUtils.isNotEmpty(goods.get(0)
									.getGoodsStaticList().get(0)
									.getGoodsStaticUrl())) {
						detail.setGoodsUrl(goods.get(0)
								.getTfShopGoodsChannelRefList().get(0)
								.getGoodsUrl());
					}
				}
				detailListNew.add(detail);

			}

		}
		return detailListNew;

	}

}
