package com.ai.ecs.ecm.mall.wap.modules.member;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ai.ecs.ecm.mall.wap.modules.BaseController;
import com.ai.ecs.ecm.mall.wap.modules.member.vo.UserRatingResult;
import com.ai.ecs.ecm.mall.wap.platform.utils.UtilString;
import com.ai.ecs.integral.entity.UserRating;
import com.ai.ecs.integral.entity.UserRatingScore;
import com.ai.ecs.integral.service.UserRatingScoreService;
import com.ai.ecs.integral.service.UserRatingService;
import com.ai.ecs.member.api.IMemberInfoService;
import com.ai.ecs.member.api.IMemberLoginService;
import com.ai.ecs.member.entity.MemberInfo;
import com.ai.ecs.member.entity.MemberLogin;

@Controller
@RequestMapping("userRating")
public class UserRatingController extends BaseController
{
    @Autowired
    @Qualifier("userRatingServiceImpl")
    UserRatingService userRatingServiceImpl;
    
    @Autowired
    @Qualifier("userRatingScoreServiceImpl")
    UserRatingScoreService userRatingScoreServiceImpl;
    
    @Autowired
    IMemberInfoService memberInfoService;
    
    @Autowired
    IMemberLoginService memberLoginService;
    
    @RequestMapping(value = "/commentForGoods", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String toLogistics(HttpServletRequest request,
            HttpServletResponse response, Long goodsId) throws Exception
    {
        if(goodsId!=null){
            UserRating userRating=new UserRating();
            userRating.setGoodsId(goodsId);
            List<UserRating> ratinglist= userRatingServiceImpl.findAll(userRating);
            List<UserRatingResult> ratresList=new ArrayList<UserRatingResult>();
            if(CollectionUtils.isNotEmpty(ratinglist)){
                for(UserRating rating:ratinglist){
                    UserRatingResult res=new UserRatingResult();
                    UserRatingScore score = userRatingScoreServiceImpl.selectByPrimaryKey(rating.getUserRatingId());
                    res.setGoodsId(rating.getGoodsId());
                    res.setMemberId(rating.getMemberId());
                    MemberInfo info = memberInfoService.getMemberInfoByKey(rating.getMemberId());
                    res.setMemberName(UtilString.showMemberName(info.getMemberNickname()));
                    if(StringUtils.isEmpty(res.getMemberName())){
                        MemberLogin memberLogin = memberLoginService.getMemberLoginByKey(rating.getMemberId());
                        res.setMemberName(UtilString.showMemberName(memberLogin.getMemberLogingName()));
                    }
                    res.setScore(score.getGoodsRatingScore());
                    res.setRatingContain(rating.getRatingContain());
                    res.setRatingTime(rating.getRatingTime());
                    res.setOrderSubId(rating.getOrderSubId());
                    res.setGoodsId(rating.getGoodsId());
                    res.setUserRatingId(rating.getUserRatingId());
                    ratresList.add(res);
                    
                }
            }
            request.setAttribute("ratingList", ratresList);
        }
        
        return "web/member/goodsComment";
    }
    
    
}
