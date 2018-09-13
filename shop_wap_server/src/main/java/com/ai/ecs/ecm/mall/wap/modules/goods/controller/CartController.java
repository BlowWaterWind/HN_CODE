package com.ai.ecs.ecm.mall.wap.modules.goods.controller;

import com.ai.ecs.ecm.mall.wap.common.CommonParams;
import com.ai.ecs.ecm.mall.wap.modules.BaseController;
import com.ai.ecs.ecm.mall.wap.modules.goods.service.GoodsService;
import com.ai.ecs.ecm.mall.wap.modules.goods.vo.UserGoodsCarModel;
import com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils;
import com.ai.ecs.goods.api.IGoodsManageService;
import com.ai.ecs.goods.api.IGoodsStaticService;
import com.ai.ecs.goods.api.IUserGoodsCarService;
import com.ai.ecs.goods.entity.goods.GoodsWater;
import com.ai.ecs.goods.entity.goods.TfShopGoodsChannelRef;
import com.ai.ecs.goods.entity.goods.TfUserGoodsCar;
import com.ai.ecs.member.api.IMemberFavoriteService;
import com.ai.ecs.member.entity.MemberFollow;
import com.ai.ecs.member.entity.MemberLogin;
import com.ai.ecs.member.entity.MemberVo;
import com.ai.ecs.merchant.shop.pojo.Shop;
import com.ai.ecs.sales.api.IMarketService;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("cart")
public class CartController extends BaseController
{

	@Autowired
	IUserGoodsCarService userGoodsCarService;
	@Autowired
	IMemberFavoriteService memberFavoriteService;
	@Autowired
	IMarketService marketService;
	@Autowired
	IGoodsStaticService goodsStaticService;
	@Autowired
	GoodsService goodsService;
	@Autowired
    IGoodsManageService goodsManageService;
	
	/**
	 * 跳转到购物车列表页面
	 * @param model
	 * @return
	 */
	@RequestMapping("/linkToCartList")
	public String linkToCartList(HttpServletRequest request,Model model){
		try {
			Long memberId = UserUtils.getLoginUser(request).getMemberLogin().getMemberId();
			String blurryKey = "B2C" + memberId;
			List<TfUserGoodsCar> userGoodsCarList =  userGoodsCarService.queryGoodsCarListByBlurryKey(blurryKey);

			if(userGoodsCarList == null || userGoodsCarList.size() == 0){
				return "web/goods/cart/cartEmpty";
			}

			//获取购物车中商品价格
			for (TfUserGoodsCar car : userGoodsCarList) {
				GoodsWater goodsWater = goodsService.queryGoodsSkuInfo(car);
				car.setGoodsSalePrice(goodsWater.getGoodsSalePrice());
			}

			Long [] skuIds = new Long[userGoodsCarList.size()];
			Long cartGoodsAmount = 0L;
			Long cartGoodsNum = 0L;

			// 解决缓存数据中无sukurl的数据
			for (int i = 0; i < userGoodsCarList.size(); i++) {
				TfUserGoodsCar cartInfo = userGoodsCarList.get(i);

				skuIds[i] = cartInfo.getGoodsSkuId();
				cartGoodsNum += cartInfo.getGoodsBuyNum();
				cartGoodsAmount += cartInfo.getGoodsSalePrice() * cartInfo.getGoodsBuyNum();

				Map<String, Object> param = Maps.newHashMap();
				param.put("goodsId", cartInfo.getGoodsId());
				param.put("channelCode", CommonParams.CHANNEL_CODE);
				List<TfShopGoodsChannelRef> result = goodsManageService.queryShopGoodsChannelRefByCds(param);
				if (CollectionUtils.isNotEmpty(result)) {
					cartInfo.setGoodsSkuUrl(result.get(0).getGoodsUrl());
				}
			}

			//获取购物车中的店铺并去重复
			Set<Shop> shopSet = goodsService.getShopSetFromGoodsCar(userGoodsCarList);
			model.addAttribute("shopSet", shopSet);
			model.addAttribute("userGoodsCarList", userGoodsCarList);
			model.addAttribute("skuIds", StringUtils.join(skuIds,";"));
			model.addAttribute("cartGoodsNum", cartGoodsNum);
			model.addAttribute("cartGoodsAmount", cartGoodsAmount);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "web/goods/cart/cartList";
	}

	/**
	 * 更新购物车
	 * @param goodsCar
	 * @return
	 */
	@RequestMapping("/updateCart")
	@ResponseBody
	public String updateCart(HttpServletRequest request,TfUserGoodsCar goodsCar){
		try {
			Long memberId = UserUtils.getLoginUser(request).getMemberLogin().getMemberId();

			String key = "B2C" + memberId;
			return userGoodsCarService.update(key, goodsCar);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}
	
	/**
	 * 加入到购物车
	 * @param carModel
	 */
	@RequestMapping("/addCart")
	@ResponseBody
	public String addCart(HttpServletRequest request,UserGoodsCarModel carModel){
		try {
			TfUserGoodsCar goodsCar = carModel.getUserGoodsCarList().get(0);
			if(goodsService.checkGoodsIdHasSku(goodsCar)){
				GoodsWater goodsWater = goodsService.queryGoodsSkuInfo(goodsCar);
				goodsCar.setGoodsSkuId(goodsWater.getGoodsSkuId());
				goodsCar.setGoodsSalePrice(goodsWater.getGoodsSkuPrice());
				goodsCar.setGoodsName(goodsWater.getGoodsName());


				Long memberId = UserUtils.getLoginUser(request).getMemberLogin().getMemberId();

				String key = "B2C" + memberId;
				userGoodsCarService.insert(key, goodsCar);
				return "OK";
			}else{
				return "加入购物车失败,该产品下无此产品规格";
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}

	/**
	 * 删除购物车
	 * @param goodsSkuIds
	 * @return
	 */
	@RequestMapping("/deleteCart")
	@ResponseBody
	public String deleteCart(HttpServletRequest request,@RequestParam(value = "goodsSkuIds[]") String[] goodsSkuIds){
		try {
			Long memberId = UserUtils.getLoginUser(request).getMemberLogin().getMemberId();

			for (String goodsSkuId : goodsSkuIds) {
				String key = "B2C" + memberId;
				userGoodsCarService.delete(key, Long.parseLong(goodsSkuId));
			}

			return "success";
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "fail";
	}

	/**
	 * 获取购物车数量
	 * @return
	 */
	@RequestMapping("/getCartCount")
	@ResponseBody
	public Integer getCartCount(HttpServletRequest request){
		try {
			MemberVo memberVo = UserUtils.getLoginUser(request);
			if(null == memberVo){
				return 0;
			}else{
				MemberLogin memberLogin = UserUtils.getLoginUser(request).getMemberLogin();
				if(null == memberLogin){
					return 0;
				} else {
					Long memberId = memberLogin.getMemberId();
					String blurryKey = "B2C" + memberId;
					Integer cartCount = userGoodsCarService.getKeyCountByBlurryKey(blurryKey);
					return cartCount;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 加入收藏
	 * @param userGoodsCar
	 */
	@RequestMapping(value = "/addFollow" ,method = RequestMethod.POST)
	@ResponseBody
	public String addCart(HttpServletRequest request,TfUserGoodsCar userGoodsCar){
		try {
		    Long memberId = UserUtils.getLoginUser(request).getMemberLogin().getMemberId();

			MemberFollow memberFollow = new MemberFollow();
			memberFollow.setMemberFollowBusiId(Long.valueOf(userGoodsCar.getGoodsSkuId()));
			memberFollow.setMemberFollowBusiImgUrl(userGoodsCar.getGoodsSkuImgUrl());
			memberFollow.setMemberFollowBusiUrl(userGoodsCar.getGoodsSkuUrl());
			memberFollow.setMemberFollowBusiName(userGoodsCar.getGoodsName());
			memberFollow.setMemberFollowBusiType("1");
			memberFollow.setMemberId(memberId);
			
			memberFavoriteService.saveMemberFollow(memberFollow);
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "fail";
	}

	/**
	 * 转换商品属性格式
	 * 字符串转换成Map
	 * @param params
	 * @return
	 */
	public Map<String, String> parseParams(String params) {
		if (params == null || "".equals(params)) {
			return null;
		}
		Map<String, String> paramsMap = Maps.newHashMap();
		String[] paramsArr = params.split("&");
		for (String paramItem : paramsArr) {
			String[] item = paramItem.split("=");
			paramsMap.put(item[0], item[1]);
		}
		return paramsMap;
	}

}
