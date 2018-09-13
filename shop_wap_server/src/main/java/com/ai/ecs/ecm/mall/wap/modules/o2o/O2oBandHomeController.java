package com.ai.ecs.ecm.mall.wap.modules.o2o;

import java.util.ArrayList;
import java.util.List;

import com.ai.ecs.ecm.mall.wap.modules.goods.constant.BroadbandConstants;
import com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils;
import com.ai.ecs.member.entity.MemberVo;
import net.sf.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ai.ecs.ecm.mall.wap.platform.utils.InvokeEcop;
import com.ai.ecs.ecop.cms.entity.BroadbandPoster;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by think on 2017/9/12.
 */
@Controller
@RequestMapping("o2o/broadband")
public class O2oBandHomeController {
	
	@Autowired
	InvokeEcop invokeEcop;
	
    @RequestMapping("index")
    public String toBroadbandHomePage(Model model){
    	try {
    		//首页界面元素
    		List<BroadbandPoster> broadbandPosterList;
			broadbandPosterList = invokeEcop.getO2oBroadbandPosterInfo("","3");
			List<BroadbandPoster> carouselList = getPosItemList("轮播楼层",broadbandPosterList);
			List<BroadbandPoster> hotActivityList = getPosItemList("热门活动楼层",broadbandPosterList);
			List<BroadbandPoster> hotGoodsList = getPosItemList("热销商品楼层",broadbandPosterList);
			model.addAttribute("carouselList",carouselList);
			model.addAttribute("hotActivityList",hotActivityList);
			model.addAttribute("hotGoodsList",hotGoodsList);
    	} catch (Exception e) {
			e.printStackTrace();
		}
        return "web/broadband/o2o/index";
    }
    
    private List<BroadbandPoster> getPosItemList(String posterName,List<BroadbandPoster> broadbandPosterList){
		List<BroadbandPoster> posItemList = new ArrayList<BroadbandPoster>();
		if(broadbandPosterList.size() > 0){
			for(int i=0;i<broadbandPosterList.size();i++){	
				if(posterName.equals(broadbandPosterList.get(i).getPosterName())){
					posItemList.add(broadbandPosterList.get(i));
				}
			}
		}
		
		return posItemList;
	}

	/**
	 * 宽带业务预约列表
	 * @param model
	 * @return
     */
	@RequestMapping("broadBandIndex")
	public String broadBandIndex(Model model, HttpServletRequest request) throws Exception {
		model.addAttribute("secToken",request.getParameter("secToken"));
		LoggerFactory.getLogger("webDbLog").info("broadBandIndex secToken：" + request.getParameter("secToken"));
		MemberVo memberVo = UserUtils.getLoginUser(request);
		if(memberVo.getChannelInfo()!=null){
			if(memberVo.getChannelInfo().getTradeStaffId()!=null){
				if(BroadbandConstants.CITY_CODE_OF_LIUYANG.equals(memberVo.getChannelInfo().getCityCode())){
					model.addAttribute("openCity","LY");
				}
				return "web/broadband/o2o/broadBandIndex";
			}else{
				throw new Exception("未配置渠道信息，请用店长账户配置渠道信息!");
			}
		}else{
			throw new Exception("未配置渠道信息，请用店长账户配置渠道信息!");
		}
	}

	/**
	 * 宽带业务预约列表
	 * @param model
	 * @return
	 */
	@RequestMapping("broadFusionIndex")
	public String broadFusionIndex(Model model, HttpServletRequest request) throws Exception {
		model.addAttribute("secToken",request.getParameter("secToken"));
		LoggerFactory.getLogger("webDbLog").info("broadBandIndex secToken：" + request.getParameter("secToken"));
		MemberVo memberVo = UserUtils.getLoginUser(request);
		if(memberVo.getChannelInfo()!=null){
			if(memberVo.getChannelInfo().getTradeStaffId()!=null){
				return "web/broadband/o2o/broadFusionIndex";
			}else{
				throw new Exception("未配置渠道信息，请用店长账户配置渠道信息!");
			}
		}else{
			throw new Exception("未配置渠道信息，请用店长账户配置渠道信息!");
		}
	}
}
