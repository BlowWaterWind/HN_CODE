package com.ai.ecs.ecm.mall.wap.modules.o2o;


import com.ai.ecs.broadband.api.IBroadBandService;
import com.ai.ecs.broadband.api.IHeFamilyService;
import com.ai.ecs.broadband.entity.BroadBandBookingRecord;
import com.ai.ecs.broadband.entity.HeFamilyBookingRecord;
import com.ai.ecs.common.utils.Collections3;
import com.ai.ecs.common.utils.StringUtils;
import com.ai.ecs.ecm.mall.wap.common.CommonParams;
import com.ai.ecs.ecm.mall.wap.modules.BaseController;
import com.ai.ecs.ecm.mall.wap.modules.goods.constant.BroadbandConstants;
import com.ai.ecs.ecm.mall.wap.modules.goods.service.IUppHtmlValidataService;
import com.ai.ecs.ecm.mall.wap.modules.goods.utils.AmountUtils;
import com.ai.ecs.ecm.mall.wap.modules.goods.utils.BroadbandUtils;
import com.ai.ecs.ecm.mall.wap.modules.goods.vo.UserGoodsCarModel;
import com.ai.ecs.ecm.mall.wap.modules.member.vo.MemberAddressResult;
import com.ai.ecs.ecm.mall.wap.platform.utils.DateUtils;
import com.ai.ecs.ecm.mall.wap.platform.utils.InvokeEcop;
import com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils;
import com.ai.ecs.ecop.cms.entity.BroadbandPoster;
import com.ai.ecs.ecop.cms.entity.Poster;
import com.ai.ecs.ecop.statis.util.DateFormatUtil;
import com.ai.ecs.ecop.sys.service.DictService;
import com.ai.ecs.ecsite.modules.broadBand.entity.*;
import com.ai.ecs.ecsite.modules.broadBand.entity.order.BroadBandPreOrderCondition;
import com.ai.ecs.ecsite.modules.broadBand.service.*;
import com.ai.ecs.ecsite.modules.myMobile.entity.PhoneAttributionModel;
import com.ai.ecs.ecsite.modules.phoneAttribution.service.PhoneAttributionService;
import com.ai.ecs.ecsite.modules.queryPackage.entity.QueryAccountPackagesCondition;
import com.ai.ecs.ecsite.modules.queryPackage.service.QueryAccountPackagesService;
import com.ai.ecs.ecsmc.domain.ec.po.ShopInfo;
import com.ai.ecs.entity.base.Page;
import com.ai.ecs.goods.api.IGoodsCommService;
import com.ai.ecs.goods.api.IGoodsManageService;
import com.ai.ecs.goods.entity.goods.BroadbandItemEntity;
import com.ai.ecs.goods.entity.goods.TfGoodsBusiParam;
import com.ai.ecs.goods.entity.goods.TfGoodsInfo;
import com.ai.ecs.goods.entity.goods.TfGoodsSku;
import com.ai.ecs.member.api.IMemberAddressService;
import com.ai.ecs.member.api.IMemberLoginService;
import com.ai.ecs.member.api.IUserBroadbrandService;
import com.ai.ecs.member.entity.*;
import com.ai.ecs.merchant.company.ICompanyAcctService;
import com.ai.ecs.merchant.entity.CompanyAcctInfo;
import com.ai.ecs.merchant.entity.CompanyInfo;
import com.ai.ecs.o2o.api.O2oBussinessRecommendService;
import com.ai.ecs.o2o.entity.O2oBussinessRecommend;
import com.ai.ecs.order.api.IOrderQueryService;
import com.ai.ecs.order.api.IOrderService;
import com.ai.ecs.order.constant.OrderConstant;
import com.ai.ecs.order.constant.Variables;
import com.ai.ecs.order.entity.*;
import com.ai.ecs.order.param.OrderProcessParam;
import com.ai.ecs.sales.api.IMarketService;
import com.ai.iis.upp.bean.MerChantBean;
import com.ai.iis.upp.service.IPayBankService;
import com.ai.iis.upp.util.UppCore;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * 宽带专区Controller
 * Created by wangqiang11 on 2016/5/14.
 */
@Controller
@RequestMapping("o2oBandBooking")
public class O2oBandBookingController extends BaseController {
    @Autowired
    private IGoodsManageService goodsManageService;
    @Autowired
    private IMemberAddressService memberAddressService;
	@Autowired
	QryAddressService qryAddressService;

	@Autowired
	private DictService dictService;
	

    @Autowired
    private IBroadBandService broadBandGoodsService;
	@Autowired
	private BroadbandOrderService broadbandOrderService;

    @Autowired
	InvokeEcop invokeEcop;
    
    private Logger logger = Logger.getLogger(O2oBandBookingController.class);

    /**
     * 跳转到预约安装页面
     * @param request
     * @return
     */
    @RequestMapping(value = "linktoBookInstall",method = RequestMethod.GET)
    public String linktoBookInstall(HttpServletRequest request){
		MemberVo memberVo = UserUtils.getLoginUser(request);
		ShopInfo shopInfo=memberVo.getShopInfo();
		String shopId = shopInfo.getShopId();
    	//地市信息
    	List<ThirdLevelAddress> cityList = memberAddressService.getChildrensByPId(BroadbandConstants.CITY_PID);
    	//区县信息
    	List<ThirdLevelAddress> countyList = null;
    	
    	
    	if(!CollectionUtils.isEmpty(cityList)){
    		countyList = memberAddressService.getChildrensByPId(cityList.get(0).getOrgId()+"");
    		//地市信息处理   用于适配Boss接口查询条件
    		for(ThirdLevelAddress  city : cityList){
    			city.setOrgName(city.getOrgName().replace("市", ""));
    			if("湘西土家族苗族自治州".equals(city.getOrgName())){
    				city.setOrgName("湘西州");
    			}
    		}
    	}
    	
    	List<Poster> posterList = null;
    	try {
			Map<String, String> param = new HashMap<String,String>();
			param.put("posterCode", "wapshopbroadbandyy");
			posterList = invokeEcop.getPosterInfo(param);
			
			if(posterList.size() > 0){
				request.setAttribute("picUrl", posterList.get(0).getPicUrl());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
        

        request.setAttribute("cityList", cityList);
        request.setAttribute("countyList", countyList);
		request.setAttribute("shopId",shopId);

    	return "web/broadband/o2o/booking/appointment";
    	
    }
    
    /**
     * 预约安装
     * @return
     */
    @RequestMapping("/broadBandBook")
    public String  broadBandBook(HttpServletRequest request,HttpServletResponse response) throws IOException{
    	Map<String, Object>  map = new HashMap<String, Object>();
    	
    	//获取页面值
    	String phoneNum = request.getParameter("phoneNum");
    	String contacts = request.getParameter("contacts");
    	String address = request.getParameter("address");
		String eparchyCode = request.getParameter("routeEparchyCode");
		String productName = request.getParameter("productName");
		String shopId = request.getParameter("shopId");
    	
    	if(sql_inj(phoneNum) || sql_inj(contacts) || sql_inj(address)){
    		response.sendRedirect("/");
    	}
    	
    	if(address == null){
    		address = "";
    	}
		BroadBandPreOrderCondition condition = new BroadBandPreOrderCondition();
		condition.setSerialNumber(phoneNum);
		condition.setEparchyCode(eparchyCode);
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.MINUTE, 5);
		condition.setAppointTime(DateFormatUtil.dateFormat(cal.getTime(),"yyyy-MM-dd"));
		condition.setCustName(contacts);
		condition.setAddress(address);
		condition.setActivityInfo(productName);

    	//初始化condition部分参数
    	Map<String,Object> resultMap = new HashMap<String,Object>();
    	resultMap.put("respCode", "-1");
    	resultMap.put("respDesc", "预约失败!");
    	try {

    		
    		BroadBandBookingRecord record=new BroadBandBookingRecord();
    		try {
       		 //添加预约记录
      		    String ip = getRemoteAddr(request);
      		    
      		    //预约号码
      		    record.setSerialNumber(phoneNum);
      		    //预约客户姓名
      		    record.setCustomerName(contacts);
				//预约地市编码
				record.setAreaCode(request.getParameter("routeEparchyCode"));
				//预约区县编码
				record.setCountyCode(request.getParameter("city"));
				//预约地址编码
				record.setHouseCode(request.getParameter("road"));
      		    //预约产品编码
      		    record.setProductId(productName);
      		    //预约安装详细地址
      		    record.setAddrName(address);
      		    //预约渠道
      		    record.setChannelCode("E007");
      		    //预约ip
      		    record.setIp(ip);
				//预约店铺
				record.setShopId(shopId);
      		   
       		    Long bookId =  broadBandGoodsService.addBroadBandBookingRecord(record);
				condition.setOutterOrderId(String.valueOf(bookId));
				resultMap = broadbandOrderService.synBroadBandPreOrder(condition);
				JSONArray resultArray = JSONArray.parseArray(String.valueOf(resultMap.get("result")));
        		JSONObject result = (JSONObject) resultArray.get(0);
				record.setRegId((String) result.get("REG_ID"));

				//预约结果状态码
				record.setRespCode((String)resultMap.get("respCode"));
				//预约结果
				record.setRespDesc((String)resultMap.get("respDesc"));
				record.setBookingRecordId(bookId);
				broadBandGoodsService.updateBroadBandBookingRecord(record);
			} catch (Exception e) {
				// TODO: handle exception
				logger.error("单品宽带预约记录日志失败---"+record.toString());
			}
    		
		} catch (Exception e) {
			logger.error("宽带网上预约", e );
		}
    	request.setAttribute("resultMap", resultMap);
    	return "web/goods/broadband/appointment/bookingResult";
    }
    
    public static boolean sql_inj(String str)
    {
	    String inj_str = "'|and|exec|insert|select|delete|update|count|*|%|chr|mid|master|truncate|char|declare|;|or|-|+";
	    String[] inj_stra=inj_str.split("\\|");
	    for (int i=0 ; i < inj_stra.length ; i++){
	    	if (str.indexOf(inj_stra[i])>=0){
	    		return true;
	    	}
	    }
	    return false;
    }
    
    @RequestMapping(value = "/gotoBookInstallSuccess",method = RequestMethod.GET)
    public String linktoBookInstallSuccess(HttpServletRequest request){
    	return "web/goods/broadband/appointment/appointmentSuccess";
    }
    
    @RequestMapping(value = "/gotoBookInstallError",method = RequestMethod.GET)
    public String linktoBookInstallError(HttpServletRequest request){
    	return "web/goods/broadband/appointment/appointmentError";
    }
    
    /**
     * 根据地市ID查询区县集合
     * @param cityId
     * @return
     */
    @RequestMapping("queryCountyList")
    public List<ThirdLevelAddress> queryCountyList(String cityId){
        return memberAddressService.getChildrensByPId(cityId);
    }


	/**
	 * 获得用户远程地址
	 */
	public static String getRemoteAddr(HttpServletRequest request){
		String remoteAddr = request.getHeader("X-Real-IP");
		if (StringUtils.isNotBlank(remoteAddr)) {
			remoteAddr = request.getHeader("X-Forwarded-For");
		}else if (StringUtils.isNotBlank(remoteAddr)) {
			remoteAddr = request.getHeader("Proxy-Client-IP");
		}else if (StringUtils.isNotBlank(remoteAddr)) {
			remoteAddr = request.getHeader("WL-Proxy-Client-IP");
		}
		return remoteAddr != null ? remoteAddr : request.getRemoteAddr();
	}


	/**
     * 三级地址获取
     * @param request
     * @param response
     * @param pId
     * @return
     */
    @RequestMapping(value = "/getChildrenByPid", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Map<String, Object> getChildrenByPid(HttpServletRequest request,
            HttpServletResponse response, String pId)
    {
    	Map<String, Object> resultmap = new HashMap<String, Object>();
        List<ThirdLevelAddress> addrChildren = memberAddressService
                .getChildrensByPId(pId);
        
        List<MemberAddressResult> addrList = new ArrayList<MemberAddressResult>();
        
        if (!CollectionUtils.isEmpty(addrChildren))
        {
            for (ThirdLevelAddress addr : addrChildren)
            {
            	//过滤掉虚拟地址
				if (!"b2badmin".equals(addr.getOperater())){
					MemberAddressResult res = new MemberAddressResult();
					res.setOrgId(addr.getOrgId());
					res.setOrgName(addr.getOrgName());
					addrList.add(res);
				}
            }
            resultmap.put("flag", "Y");
            resultmap.put("orgList", addrList);
        }
        else
        {
            resultmap.put("flag", "N");
        }
        return resultmap;
    }
    /**
     * 三级地址获取
     * @param request
     * @param response
     * @param cityCode
     * @return
     */
    @RequestMapping(value = "/getCountyFromBoss", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Map<String, Object> getCountyFromBoss(HttpServletRequest request,
            HttpServletResponse response, String cityCode)
    {
    	Map<String, Object> resultmap = new HashMap<String, Object>();
		List countyList = new ArrayList();
		QryAddressCityNameCondition condition = new QryAddressCityNameCondition();
		condition.setEPARCHY_CODE(String.valueOf(cityCode));
		condition.setStaffId("ITFWC000");
		condition.setTradeDepartPassword("ai1234");
		try{
			Map addreMap = qryAddressService.qryAddressCityName(condition);

			if("0".equals(addreMap.get("respCode"))){
				List list = (List) addreMap.get("result");
				Map result = (Map) list.get(0);
				countyList = (List) result.get("ADDRESS_INFO");
				if(countyList!=null&&countyList.size()>0){
					resultmap.put("flag", "Y");
					resultmap.put("orgList", countyList);
				}else{
					resultmap.put("flag", "N");
				}
			}else{
				resultmap.put("flag", "N");
			}
		}catch (Exception e){
			resultmap.put("flag", "N");
		}

        return resultmap;
    }

}