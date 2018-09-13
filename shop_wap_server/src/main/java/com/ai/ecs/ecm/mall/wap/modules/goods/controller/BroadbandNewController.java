package com.ai.ecs.ecm.mall.wap.modules.goods.controller;


import com.ai.ecs.ecm.mall.wap.modules.BaseController;
import com.ai.ecs.ecm.mall.wap.platform.utils.StringUtils;
import com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils;
import com.ai.ecs.ecop.broadband.entity.TfBroadbandConf;
import com.ai.ecs.ecop.broadband.service.IBroadBandConfService;
import com.ai.ecs.ecop.sys.entity.Dict;
import com.ai.ecs.ecop.sys.service.DictService;
import com.ai.ecs.ecsite.modules.broadBand.entity.*;
import com.ai.ecs.ecsite.modules.broadBand.service.BroadBandService;
import com.ai.ecs.ecsite.modules.myMobile.entity.PhoneAttributionModel;
import com.ai.ecs.ecsite.modules.offerBalance.entity.OfferBalanceCondition;
import com.ai.ecs.ecsite.modules.offerBalance.service.OfferBalanceService;
import com.ai.ecs.ecsite.modules.phoneAttribution.service.PhoneAttributionService;
import com.ai.ecs.member.entity.MemberVo;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.ai.ecs.ecm.mall.wap.platform.utils.LogUtils.*;

/**
 * 宽带专区Controller
 * Created by wangqiang11 on 2016/5/14.
 */
@Controller
@RequestMapping("broadbandNew")
public class BroadbandNewController extends BaseController {

	@Autowired
	private IBroadBandConfService broadBandConfService;
	@Autowired
	private PhoneAttributionService phoneAttributionService;
	@Autowired
	private OfferBalanceService offerBalanceService;
	@Autowired
	private BroadBandService broadBandServiceImpl;
	@Autowired
	private DictService dictService;
	/**
	 * 宽带首页初始化
	 */
	@RequestMapping("home")
	public String home(HttpServletRequest request,Model model){
		String type = request.getParameter("type");
		String cityCode = request.getParameter("cityCode");
		Map<String,Object> resultMap = new HashMap<String,Object>();
		String streamNo=createStreamNo();


		MemberVo memberVo = UserUtils.getLoginUser(request);
		//登录手机号码
		String installPhoneNum = "";
		//地市编号
		String EPARCHY_CODE = "";
		//地市查询参数
		List<String> cityCodes = new ArrayList<>();
		//地市名称
		String CITY_NAME = "";
		if(memberVo!=null){
			installPhoneNum= String.valueOf(memberVo.getMemberLogin().getMemberPhone());
			model.addAttribute("isLogin",true);
		}else{
			model.addAttribute("isLogin",false);
		}

		//校验是否登录
		try {
			if(StringUtils.isNotEmpty(installPhoneNum)){
				PhoneAttributionModel phoneAttributionModel1 = new PhoneAttributionModel();
				phoneAttributionModel1.setSerialNumber(installPhoneNum);
				resultMap = phoneAttributionService.queryPhoneAttribution(phoneAttributionModel1);
				 EPARCHY_CODE= String.valueOf(((Map) ((List) resultMap.get("result")).get(0)).get("AREA_CODE"));
				CITY_NAME= String.valueOf(((Map) ((List) resultMap.get("result")).get(0)).get("CITY_NAME"));

				model.addAttribute("isLogin",true);
				model.addAttribute("CITY_NAME", CITY_NAME);
				if (!"731".equals(((Map) ((List) resultMap.get("result")).get(0)).get("PROVINCE_CODE"))){
					resultMap.put("respCode", "-1");
					resultMap.put("respDesc", "请输入湖南移动号码办理!");
					model.addAttribute("resultMap",resultMap);
					return "web/goods/broadband/newInstall/checkError";
				}

				Map result  = queryBanlance(installPhoneNum,EPARCHY_CODE);
				Map bandResult  = queryBroadBandInfo(installPhoneNum);
				model.addAttribute("bandResult",bandResult);
				model.addAttribute("result",result);
			//未登录
			}else{
				List<Dict> areaList = new ArrayList<>();
				areaList = dictService.getDictList("CITY_CODE_CHECKBOXES");
				//用户是否选择区域，以选择区域适配页面
				if(com.ai.ecs.common.utils.StringUtils.isNotEmpty(cityCode)){
					EPARCHY_CODE = cityCode;
				}else{
					//默认长沙市
					EPARCHY_CODE = "0731";
				}
				String cityName = "";
				for(Dict dict:areaList){
					if(dict.getValue().equals(EPARCHY_CODE)){
						cityName = dict.getLabel();
						break;
					}
				}
				model.addAttribute("CITY_NAME",cityName);
				model.addAttribute("areaList",areaList);
				model.addAttribute("isLogin",false);
			}
			model.addAttribute("eparchy_Code", EPARCHY_CODE);
			TfBroadbandConf record = new TfBroadbandConf();
			cityCodes.add(EPARCHY_CODE);
			cityCodes.add("43");
			record.setChanelId("E007");
			record.setCityCodes(cityCodes);
			record.setPageNum("1");
			List<TfBroadbandConf> allElements = broadBandConfService.findAll(record);
			List<TfBroadbandConf> intro = getBroadItemList("0","2",allElements);
			List<TfBroadbandConf> oneFloor = getBroadItemList("1","1",allElements);
			List<TfBroadbandConf> twoFloorTitle = getBroadItemList("2","5",allElements);
			List<TfBroadbandConf> twoFloor = getBroadItemList("2","1",allElements);
			List<TfBroadbandConf> threeFloorTitle =getBroadItemList("3","5",allElements);
			List<TfBroadbandConf> threeFloor = getBroadItemList("3","1",allElements);
			List<TfBroadbandConf> fourFloorTitle = getBroadItemList("4","5",allElements);
			List<TfBroadbandConf> fourFloor = getBroadItemList("4","3",allElements);
			model.addAttribute("oneFloor",oneFloor);
			model.addAttribute("twoFloorTitle",twoFloorTitle!=null&&twoFloorTitle.size()>0?twoFloorTitle.get(0):null);
			model.addAttribute("twoFloor",twoFloor);
			model.addAttribute("threeFloorTitle",threeFloorTitle!=null&&threeFloorTitle.size()>0?threeFloorTitle.get(0):null);
			model.addAttribute("threeFloor",threeFloor);
			model.addAttribute("fourFloorTitle",fourFloorTitle!=null&&fourFloorTitle.size()>0?fourFloorTitle.get(0):null);
			model.addAttribute("fourFloor",fourFloor);
			model.addAttribute("intro",intro!=null&&intro.size()>0?intro.get(0):null);

//            model.addAttribute("eparchy_Code","0731");
		}catch (Exception e) {
			logger.error("查询用户信息失败 :"+e.getMessage());
			writerFlowLogThrowable(streamNo,"","",getClass().getName(),"home",null,"查询用户信息失败:"+processThrowableMessage(e));
			resultMap.put("respCode", "-1");
			resultMap.put("respDesc", "查询用户信息失败!");
			model.addAttribute("resultMap",resultMap);
			return "web/goods/broadband/newInstall/checkError";
		}
		model.addAttribute("type",type);
		return "web/broadband/home/new_home";
	}
	/**
	 * 宽带首页初始化
	 */
	@RequestMapping("terminal")
	public String terminalInit(HttpServletRequest request,Model model){
		String type = request.getParameter("type");
		String cityCode = request.getParameter("cityCode");
		Map<String,Object> resultMap = new HashMap<String,Object>();
		String streamNo=createStreamNo();
		MemberVo memberVo = UserUtils.getLoginUser(request);
		String installPhoneNum = "";
		//地市编号
		String EPARCHY_CODE = "";
		//地市查询参数
		List<String> cityCodes = new ArrayList<>();
		if(memberVo!=null){
			installPhoneNum= String.valueOf(memberVo.getMemberLogin().getMemberPhone());
			model.addAttribute("isLogin",true);
		}else{
			model.addAttribute("isLogin",false);
		}
		//校验
		try {
			if(StringUtils.isNotEmpty(installPhoneNum)) {
				PhoneAttributionModel phoneAttributionModel1 = new PhoneAttributionModel();
				phoneAttributionModel1.setSerialNumber(installPhoneNum);
				resultMap = phoneAttributionService.queryPhoneAttribution(phoneAttributionModel1);
				EPARCHY_CODE = String.valueOf(((Map) ((List) resultMap.get("result")).get(0)).get("AREA_CODE"));
				model.addAttribute("eparchy_Code", EPARCHY_CODE);
				if (!"731".equals(((Map) ((List) resultMap.get("result")).get(0)).get("PROVINCE_CODE"))) {
					resultMap.put("respCode", "-1");
					resultMap.put("respDesc", "请输入湖南移动号码办理!");
					model.addAttribute("resultMap", resultMap);
					return "web/goods/broadband/newInstall/checkError";
				}
			}else{
				//用户是否选择区域，以选择区域适配页面
				if(com.ai.ecs.common.utils.StringUtils.isNotEmpty(cityCode)){
					EPARCHY_CODE = cityCode;
				}else{
					//默认长沙市
					EPARCHY_CODE = "0731";
				}
			}
			TfBroadbandConf record = new TfBroadbandConf();
			cityCodes.add(EPARCHY_CODE);
			cityCodes.add("43");
			record.setChanelId("E007");
			record.setCityCodes(cityCodes);
			record.setPageNum("3");
			List<TfBroadbandConf> allElements = broadBandConfService.findAll(record);
			List<TfBroadbandConf> oneFloor = getBroadItemList("1","1",allElements);
			List<TfBroadbandConf> intro = getBroadItemList("0","2",allElements);
			List<TfBroadbandConf> twoFloorTitle = getBroadItemList("2","5",allElements);
			List<TfBroadbandConf> twoFloor = getBroadItemList("2","6",allElements);
			List<TfBroadbandConf> threeFloorTitle =getBroadItemList("3","5",allElements);
			List<TfBroadbandConf> threeFloor = getBroadItemList("3","6",allElements);
			List<TfBroadbandConf> threeXFloor = getBroadItemList("3","7",allElements);
			List<TfBroadbandConf> fourFloorTitle = getBroadItemList("4","5",allElements);
			List<TfBroadbandConf> fourFloor = getBroadItemList("4","6",allElements);
			model.addAttribute("oneFloor",oneFloor);
			model.addAttribute("twoFloorTitle",twoFloorTitle!=null&&twoFloorTitle.size()>0?twoFloorTitle.get(0):null);
			model.addAttribute("twoFloor",twoFloor);
			model.addAttribute("threeFloorTitle",threeFloorTitle!=null&&threeFloorTitle.size()>0?threeFloorTitle.get(0):null);
			model.addAttribute("threeFloor",threeFloor);
			model.addAttribute("threeXFloor",threeXFloor);
			model.addAttribute("fourFloorTitle",fourFloorTitle!=null&&fourFloorTitle.size()>0?fourFloorTitle.get(0):null);
			model.addAttribute("fourFloor",fourFloor);
			model.addAttribute("intro",intro!=null&&intro.size()>0?intro.get(0):null);
//            model.addAttribute("eparchy_Code","0731");
		}catch (Exception e) {
			logger.error("查询用户信息失败 :"+e.getMessage());
			writerFlowLogThrowable(streamNo,"","",getClass().getName(),"home",null,"查询用户信息失败:"+processThrowableMessage(e));
			resultMap.put("respCode", "-1");
			resultMap.put("respDesc", "查询用户信息失败!");
			model.addAttribute("resultMap",resultMap);
			return "web/goods/broadband/newInstall/checkError";
		}
		model.addAttribute("type",type);
		return "web/broadband/home/new_terminal";
	}

	/**
	 * 宽带首页初始化
	 */
	@RequestMapping("tv")
	public String tvInit(HttpServletRequest request,Model model){
		String type = request.getParameter("type");
		String cityCode = request.getParameter("cityCode");
		Map<String,Object> resultMap = new HashMap<String,Object>();
		String streamNo=createStreamNo();
		MemberVo memberVo = UserUtils.getLoginUser(request);
		String installPhoneNum = "";
		//地市编号
		String EPARCHY_CODE = "";
		//地市查询参数
		List<String> cityCodes = new ArrayList<>();
		if(memberVo!=null){
			installPhoneNum= String.valueOf(memberVo.getMemberLogin().getMemberPhone());
			model.addAttribute("isLogin",true);
		}else{
			model.addAttribute("isLogin",false);
		}
		//校验
		try {
			if(StringUtils.isNotEmpty(installPhoneNum)) {
				PhoneAttributionModel phoneAttributionModel1 = new PhoneAttributionModel();
				phoneAttributionModel1.setSerialNumber(installPhoneNum);
				resultMap = phoneAttributionService.queryPhoneAttribution(phoneAttributionModel1);
				EPARCHY_CODE = String.valueOf(((Map) ((List) resultMap.get("result")).get(0)).get("AREA_CODE"));
				model.addAttribute("eparchy_Code", EPARCHY_CODE);
				if (!"731".equals(((Map) ((List) resultMap.get("result")).get(0)).get("PROVINCE_CODE"))) {
					resultMap.put("respCode", "-1");
					resultMap.put("respDesc", "请输入湖南移动号码办理!");
					model.addAttribute("resultMap", resultMap);
					return "web/goods/broadband/newInstall/checkError";
				}
			}else{
				//用户是否选择区域，以选择区域适配页面
				if(com.ai.ecs.common.utils.StringUtils.isNotEmpty(cityCode)){
					EPARCHY_CODE = cityCode;
				}else{
					//默认长沙市
					EPARCHY_CODE = "0731";
				}
			}
			TfBroadbandConf record = new TfBroadbandConf();
			cityCodes.add(EPARCHY_CODE);
			cityCodes.add("43");
			record.setChanelId("E007");
			record.setCityCodes(cityCodes);
			record.setPageNum("2");
			List<TfBroadbandConf> allElements = broadBandConfService.findAll(record);
			List<TfBroadbandConf> oneFloor = getBroadItemList("1","6",allElements);
			List<TfBroadbandConf> intro = getBroadItemList("0","2",allElements);
			List<TfBroadbandConf> twoFloor = getBroadItemList("2","3",allElements);
			List<TfBroadbandConf> twoFloorTitle = getBroadItemList("2","5",allElements);
			List<TfBroadbandConf> threeFloor = getBroadItemList("3","6",allElements);
			List<TfBroadbandConf> threeFloorTitle = getBroadItemList("3","5",allElements);
			List<TfBroadbandConf> fourFloorTitle = getBroadItemList("4","5",allElements);
			List<TfBroadbandConf> fourFloor = getBroadItemList("4","6",allElements);
			model.addAttribute("oneFloor",oneFloor);
			model.addAttribute("twoFloorTitle",twoFloorTitle!=null&&twoFloorTitle.size()>0?twoFloorTitle.get(0):null);
			model.addAttribute("twoFloor",twoFloor);
			model.addAttribute("threeFloorTitle",threeFloorTitle!=null&&threeFloorTitle.size()>0?threeFloorTitle.get(0):null);
			model.addAttribute("threeFloor",threeFloor);
			model.addAttribute("fourFloorTitle",fourFloorTitle!=null&&fourFloorTitle.size()>0?fourFloorTitle.get(0):null);
			model.addAttribute("fourFloor",fourFloor);
			model.addAttribute("intro",intro!=null&&intro.size()>0?intro.get(0):null);
//            model.addAttribute("eparchy_Code","0731");
		}catch (Exception e) {
			logger.error("查询用户信息失败 :"+e.getMessage());
			writerFlowLogThrowable(streamNo,"","",getClass().getName(),"home",null,"查询用户信息失败:"+processThrowableMessage(e));
			resultMap.put("respCode", "-1");
			resultMap.put("respDesc", "查询用户信息失败!");
			model.addAttribute("resultMap",resultMap);
			return "web/goods/broadband/newInstall/checkError";
		}
		model.addAttribute("type",type);
		return "web/broadband/home/new_tv";
	}

	@RequestMapping("detail")
	public String detail(HttpServletRequest request,Model model,String confId){
		TfBroadbandConf broadbandConf = broadBandConfService.selectByPrimaryKey(confId);
		model.addAttribute("broadbandConf",broadbandConf);
		return "web/broadband/home/new_detail";
	}

	/**
	 * 获取话费、流量余额与总量
	 * @param serialNumber
	 * @param eparchyCode
	 * @return
	 * @throws Exception
     */
	private Map<String,Object> queryBanlance(String serialNumber,String eparchyCode) throws Exception {
		OfferBalanceCondition condition = new OfferBalanceCondition();
		condition.setSerialNumber(serialNumber);
		condition.setBcycId(getCurrentTime());
		condition.setEparchyCode(eparchyCode);
		Map<String,Object> info = new HashMap<>();
		Map resultMap = offerBalanceService.queryBalance(condition);
		logger.info(JSON.toJSONString(resultMap, true));
		List<Map<String,String>> balanceData = (List<Map<String,String>>)resultMap.get("result");

		//语音总量
		BigDecimal voiceTotal = new BigDecimal(0);
		BigDecimal voiceBalance = new BigDecimal(0);
		BigDecimal gprsBalance = new BigDecimal(0);
		BigDecimal gprsTotalB = new BigDecimal(0);
		BigDecimal gprsUse = new BigDecimal(0);
		BigDecimal voiceUse = new BigDecimal(0);

		String gprsTotal = "";
		if(balanceData.size()>0){
			for(Map<String,String> element:balanceData){
				if(element.containsKey("BALANCE") && element.containsKey("BALANCE")){
					String balance = element.get("BALANCE");
					if("-".equals(balance)){
						balance = "0";
					}
					String highFee=element.get("HIGH_FEE");
					if("-".equals(highFee)){
						highFee = "0";
					}
					if("1".equals(element.get("REMIND_ID"))){//语音
						voiceBalance = voiceBalance.add(new BigDecimal(balance));
						voiceTotal = voiceTotal.add(new BigDecimal(highFee));
					}else if("3".equals(element.get("REMIND_ID"))){//流量
						checkFlow(element);
						if("TRUE".equals(element.get("VALID"))){
							gprsBalance = gprsBalance.add(new BigDecimal(balance));
						}
					}
				}
			}
		}

		if(balanceData.size()>0){
			if(((Map)balanceData.get(0)).containsKey("BALANCE") && ((Map)balanceData.get(0)).containsKey("BALANCE")){
				info.put("voiceResv", voiceTotal);//通话总量
				info.put("voiceBalance", voiceBalance);//通话余量
			}else{
				info.put("voiceResv", "");//通话总量
				info.put("voiceBalance", "");//通话余量
			}
			if(((Map)balanceData.get(0)).containsKey("RSRV_FEE3") && ((Map)balanceData.get(0)).containsKey("RSRV_FEE1")){
				gprsTotal = ((Map)balanceData.get(0)).get("RSRV_FEE1").toString();//总流量
			}
		}else{
			info.put("voiceResv", "");//通话总量
			info.put("voiceBalance", "");//通话余量
		}
		gprsTotalB = new BigDecimal(gprsTotal);
		gprsUse = gprsTotalB.subtract(gprsBalance).divide(new BigDecimal(1024));
		voiceUse = voiceTotal.subtract(voiceBalance);
		info.put("gprsBalance", String.format("%.2f", gprsBalance.divide(new BigDecimal(1024))));//流量余量
		info.put("gprsTotal", String.format("%.2f", gprsTotalB.divide(new BigDecimal(1024))));//总流量
		info.put("gprsUse", String.format("%.2f", gprsUse));//已使用流量
		info.put("voiceUse", String.format("%.2f",voiceUse));//总语音
		return info;
	}

	/**
	 * 获取宽带信息
	 * @param serialNumber
	 * @return
	 * @throws Exception
     */
	public Map<String,Object> queryBroadBandInfo(String serialNumber) throws Exception {
		Map<String,Object> resultMap = new HashMap<String,Object>();
		Map<String,String> broadbandInfoMap = new HashMap<String,String>();
		BroadbandDetailInfoCondition condition = new BroadbandDetailInfoCondition();
		condition.setSerialNumber(serialNumber);
		BroadbandDetailInfoResult broadbandDetailInfoResult = broadBandServiceImpl.broadbandDetailInfo(condition);
		if("0".equals(broadbandDetailInfoResult.getResultCode()) && "成功".equals(broadbandDetailInfoResult.getResultInfo())) {
			resultMap.put("respCode", "1");
			BroadbandDetailInfo broadbandDetailInfo = broadbandDetailInfoResult.getBroadbandDetailInfo();
			broadbandInfoMap.put("mobile", serialNumber);
			broadbandInfoMap.put("accessAcct", broadbandDetailInfo.getAccessAcct());
			broadbandInfoMap.put("rate", broadbandDetailInfo.getRate());
			broadbandInfoMap.put("startTime", broadbandDetailInfo.getNewProductsInfo().get(0).getStart_date().substring(0, 10));
			broadbandInfoMap.put("endTime", broadbandDetailInfo.getEndTime().substring(0, 10));
			broadbandInfoMap.put("discntName", broadbandDetailInfo.getDiscntName());
			resultMap.put("broadbandInfoMap", broadbandInfoMap);
		}
		return resultMap;
	}
	/**
	 * 检查话费/流量是否过期
	 * @param element
	 * @return
	 * @throws Exception
     */
	public Map<String,String> checkFlow (Map<String,String> element) throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date now = new Date();
		Date endTime = sdf.parse(element.get("END_DATE"));
		if(now.getTime()>endTime.getTime()){
			element.put("VALID","FALSE");                            // 失效
		}else{
			element.put("VALID","TRUE");
		}
		return element;
	}

	/**
	 * 获取当前时间
	 * @return
     */
	public static Integer getCurrentTime() {
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		String date = sdf.format(d);
		return Integer.valueOf(date);
	}

	/**
	 * 根据元素类型和楼层过来出所有元素
	 * @param floorNum
	 * @param confType
	 * @param broadbandConfs
     * @return
     */
	private List<TfBroadbandConf> getBroadItemList(String floorNum,String confType,List<TfBroadbandConf> broadbandConfs){
		List<TfBroadbandConf> bandConfs = new ArrayList<TfBroadbandConf>();
		if(broadbandConfs.size() > 0){
			for(int i=0;i<broadbandConfs.size();i++){
				if(floorNum.equals(broadbandConfs.get(i).getFloorNum())&&confType.equals(broadbandConfs.get(i).getConfType())){
					bandConfs.add(broadbandConfs.get(i));
				}
			}
		}

		return bandConfs;
	}



}