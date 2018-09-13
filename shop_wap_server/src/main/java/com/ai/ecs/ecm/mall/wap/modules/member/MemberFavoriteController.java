package com.ai.ecs.ecm.mall.wap.modules.member;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.ecs.ecm.mall.wap.modules.BaseController;
import com.ai.ecs.ecm.mall.wap.modules.member.vo.FavoriteGoodsResult;
import com.ai.ecs.ecm.mall.wap.modules.member.vo.FavoriteShopResult;
import com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils;
import com.ai.ecs.ecop.sys.service.DictService;
import com.ai.ecs.goods.api.IGoodsManageService;
import com.ai.ecs.goods.entity.goods.TfGoodsInfo;
import com.ai.ecs.member.api.IMemberFavoriteService;
import com.ai.ecs.member.entity.MemberFollow;
import com.ai.ecs.member.entity.MemberVo;

@Controller
@RequestMapping("memberFavorite")
public class MemberFavoriteController extends BaseController {
	@Autowired
	@Qualifier("goodsManageService")
	IGoodsManageService goodsManageService;

	@Autowired
	@Qualifier("dictService")
	DictService dictService;

	@Autowired
	IMemberFavoriteService memberFavoriteService;


	@RequestMapping(value = "/toFavoriteGoods", method = RequestMethod.GET)
	public String toFavoriteGoods(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		MemberVo member = UserUtils.getLoginUser(request);
		List<FavoriteGoodsResult> favGoodsList = new ArrayList<FavoriteGoodsResult>();
		if (member != null) {
			List<MemberFollow> followList = memberFavoriteService
					.getMemberFollows(member.getMemberLogin().getMemberId(),
							"1");
			if (!CollectionUtils.isEmpty(followList)) {
				for (MemberFollow follow : followList) {
					TfGoodsInfo goodsInfo = goodsManageService
							.queryGoodsInfoById(follow.getMemberFollowBusiId(),"E007");
					FavoriteGoodsResult favorite = new FavoriteGoodsResult();
					if (follow.getMemberFollowBusiImgUrl() != null) {
						favorite.setMemberFollowBusiImgUrl(follow.getMemberFollowBusiImgUrl());
					}
					favorite.setMemberFollowBusiUrl(goodsInfo.getGoodsUrl());
					favorite.setMemberFollowId(follow.getMemberFollowId());
					favorite.setMemberId(follow.getMemberId());
					favorite.setMemberFollowBusiName(follow
							.getMemberFollowBusiName());
					if (goodsInfo != null) {
						favorite.setPrice(goodsInfo.getMinGoodsSalePrice());
					} else {
						favorite.setPrice(0L);
					}
					favGoodsList.add(favorite);
				}

			}
			request.setAttribute("favoriteList", favGoodsList);
		}
		return "web/member/favoriteGoodsList";
	}

	@RequestMapping(value = "/toFavoriteShop", method = RequestMethod.GET)
	public String toFavoriteShop(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		 MemberVo member = UserUtils.getLoginUser(request);
	        List<FavoriteShopResult> favShopList = new ArrayList<FavoriteShopResult>();
	        if (member != null)
	        {
	            List<MemberFollow> followList = memberFavoriteService
	                    .getMemberFollows(member.getMemberLogin().getMemberId(),
	                            "2");
	            if (!CollectionUtils.isEmpty(followList))
	            {
	                for (MemberFollow follow : followList)
	                {
	                    FavoriteShopResult favorite = new FavoriteShopResult();
	                    favorite.setMemberFollowId(follow.getMemberFollowId());
	                    favorite.setMemberId(follow.getMemberId());
	                    if (follow.getMemberFollowBusiImgUrl() != null)
	                    {
	                        favorite.setMemberFollowBusiImgUrl( follow.getMemberFollowBusiImgUrl());
	                    }
	                    if (follow.getMemberFollowBusiUrl() != null)
	                    {
	                        favorite.setMemberFollowBusiUrl( follow.getMemberFollowBusiUrl());
	                    }
	                    favorite.setMemberFollowBusiName(follow
	                            .getMemberFollowBusiName());
	                    favorite.setGoodsCount(11);
	                    favShopList.add(favorite);
	                }

	            }
	            request.setAttribute("favoriteShopList", favShopList);
	        }
		return "web/member/favoriteShopList";
	}

	@RequestMapping(value = "/cancelFav", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String deleteCoupon(HttpServletRequest request,
							   HttpServletResponse response, Long followId) throws Exception {
		boolean res;
		try {
			MemberVo member = UserUtils.getLoginUser(request);
			MemberFollow follow = memberFavoriteService.getMemberFollowDetail(followId);
			if (!member.getMemberLogin().getMemberId().equals(follow.getMemberId())) {
				throw new Exception("非法访问限制！");
			}
			res = memberFavoriteService.delMemberFollow(followId);

		} catch (Exception e) {
			logger.error("取消收藏deleteCoupon:",e);
			return "取消收藏失败！";
		}
		if (!res) {
			return "取消收藏失败！";
		}else {
			return "success";
		}
	}
	
}
