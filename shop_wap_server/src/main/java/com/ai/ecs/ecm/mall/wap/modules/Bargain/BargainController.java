package com.ai.ecs.ecm.mall.wap.modules.Bargain;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.ecs.activity.api.IActivityTargetNumberService;
import com.ai.ecs.activity.entity.ActivityCutPrice;
import com.ai.ecs.activity.entity.ActivityCutPriceDetail;
import com.ai.ecs.activity.entity.ActivityGoodsPara;
import com.ai.ecs.common.utils.JedisClusterUtils;
import com.ai.ecs.ecm.mall.wap.modules.BaseController;
import com.ai.ecs.ecm.mall.wap.modules.goods.constant.HNanConstant;
import com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils;
import com.ai.ecs.ecm.mall.wap.platform.utils.UtilString;
import com.ai.ecs.ecsite.modules.myMobile.entity.BasicInfoCondition;
import com.ai.ecs.ecsite.modules.myMobile.entity.PhoneAttributionModel;
import com.ai.ecs.ecsite.modules.myMobile.service.BasicInfoQryModifyService;
import com.ai.ecs.ecsite.modules.phoneAttribution.service.PhoneAttributionService;
import com.ai.ecs.ecsite.modules.sms.entity.SmsSend4AllChanCondition;
import com.ai.ecs.ecsite.modules.sms.entity.SmsSendCondition;
import com.ai.ecs.ecsite.modules.sms.service.SmsSendService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;

import redis.clients.jedis.JedisCluster;

@Controller
@RequestMapping(value="/bargain")
public class BargainController extends BaseController{
    protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
      SmsSendService smsSendService;
    @Autowired
	IActivityTargetNumberService activityTargetNumberServiceImpl;
   
    SmsSend4AllChanCondition smsSend4AllChanCondition = new SmsSend4AllChanCondition();
         
	@Autowired
	BasicInfoQryModifyService  basicInfoQryModifyService;

    
    @Autowired
    private JedisCluster jedisCluster;
    
    static String CUT_PPRICE = "2017_DOUBLE_CUT";
    static  String SID = "2017_DOUBLE_CUT_NUMBER";
    static  ActivityGoodsPara STATCactivityGoodsPara = new ActivityGoodsPara();
    static List <Double>CUTPrice = Lists.newArrayList();
    
    
    @RequestMapping("joinCut")
    @ResponseBody
    public  Map<String,Object>  joinCut(HttpServletRequest request,Model model){    
    	Map<String, Object>  map = new HashMap<String, Object>();
    	 
    	if (STATCactivityGoodsPara == null || STATCactivityGoodsPara.getActCode() == null || "".equals(STATCactivityGoodsPara.getActCode())){
    		STATCactivityGoodsPara = this.getInitActivityGoodsPara();
    	} 
    	     
    	if(STATCactivityGoodsPara.getStartTime().after(new Date())){
    		map.put("resultCode", "9");//没有登录需短信登录
    		map.put("resultInfo", "该活动尚未开始！!"); 
    		return map;    		
    	}
    	
    	if(STATCactivityGoodsPara.getEndTime().before(new Date())){
    		map.put("resultCode", "9");//没有登录需短信登录
    		map.put("resultInfo", "该活动已经下线！!"); 
    		STATCactivityGoodsPara = null;
    		return map;    		
    	}
    	
    	Session session = UserUtils.getSession();
    	Object numberTmp = session.getAttribute(SID);
      	logger.error("++joinCut++cutNumber+++ ");
    	
    	if(numberTmp == null || "".equals(numberTmp)){
    		map.put("resultCode", "0");//没有登录需短信登录
    		map.put("resultInfo", "需要短信登录!"); 
    	}
    	else{
    		String number = numberTmp.toString();
    		
    		//查询是否参与
    		String actId  = getUserActId(request,number);
    		if("".equals(actId)){    		
    			
    			//判断是否满足条件并给予提示
   			       ActivityCutPrice activityCutPriceTemp = new ActivityCutPrice();    	
   			       activityCutPriceTemp.setDiscount(Double.parseDouble(STATCactivityGoodsPara.getDisMount()));
   			       activityCutPriceTemp.setGoodCode(STATCactivityGoodsPara.getGoodCode()); 
    	    	 //活动人数是否满足活动设定
    	    		List<ActivityCutPrice> queryActivityCutPriceListTmp  = activityTargetNumberServiceImpl.queryActivityCutPriceList(activityCutPriceTemp);
         	     
    	    		if(queryActivityCutPriceListTmp.size()>=STATCactivityGoodsPara.getMaxNum())
        	        {        	
        	                map.put("resultCode", "9");
        			        map.put("resultInfo","砍价得购机红包活动已经达到上限！谢谢参与.");      //
        			        return map;
        	        }
    			 
    		      logger.info(" joinCut activityGoodsPara " + STATCactivityGoodsPara.toString());
    			 ActivityCutPrice activityCutPrice = new ActivityCutPrice();
                 activityCutPrice.setActCode(CUT_PPRICE);      
	             activityCutPrice.setActNumber(number);
	             activityCutPrice.setCreateTime(new Date());               
	             activityCutPrice.setTotalAmount(0.0);
	             activityCutPrice.setTotalNum(0);
	             activityCutPrice.setGoodCode(STATCactivityGoodsPara.getGoodCode());
	             activityCutPrice.setStatus("0");
               int num =  activityTargetNumberServiceImpl.addActivityCutPrice(activityCutPrice);  
              
               if(num>-1)
               {
            	   actId = getUserActId(request,number);
                   //已经参与过--跳到分分享页面
                   map.put("resultCode", "1");
           		   map.put("actId",actId);  
               }else{
            	   map.put("resultCode", "9");
           		   map.put("resultInfo","系统异常请稍候在尝试！");  
               } 
    		}else{    			 
    			//判断是否满足条件并给予提示
    			 ActivityCutPrice activityCutPrice = new ActivityCutPrice();    	
    				activityCutPrice.setId(actId); 
    			List<ActivityCutPrice> queryActivityCutPriceList  = activityTargetNumberServiceImpl.queryActivityCutPriceList(activityCutPrice);
    	        if(queryActivityCutPriceList.size()==0)
    	        {        	
    	                map.put("resultCode", "9");
    			        map.put("resultInfo","系统异常请稍候在尝试！");      //
    			        return map;
    	        }
    	        ActivityCutPrice activityCutPriceTmp = queryActivityCutPriceList.get(0);
     	        if(!activityCutPriceTmp.getStatus().equals("0")){
     	        	SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	        	map.put("resultCode", "9");
    			    map.put("resultInfo","你已经获得"+STATCactivityGoodsPara.getDisMount()+
    			    		"元购机红包，请在"+time.format(STATCactivityGoodsPara.getDisTime())+"之前购买移动商城同款活动机型即可减免."); 
    			    return map; 
    	        }  else{
    			map.put("resultCode", "1");//已经参与过
        		map.put("actId",actId);  
    	        }
    		} 
    	}
    	return  map;
    }
    
 
    //查询活动信息
    @RequestMapping("queryCutInfo")
    @ResponseBody
    public  Map<String,Object>  queryCutInfo(HttpServletRequest request,Model model){    
    	Map<String, Object>  map = new HashMap<String, Object>(); 
    	if (STATCactivityGoodsPara == null || STATCactivityGoodsPara.getActCode() == null || "".equals(STATCactivityGoodsPara.getActCode())){
    		STATCactivityGoodsPara = this.getInitActivityGoodsPara();
    	}     	
         if(STATCactivityGoodsPara != null ){
        	 //activityGoodsPara =  queryActivityGoodsParaList.get(0);
        	 map.put("cost", STATCactivityGoodsPara.getCostPrice());        	 
        	 map.put("cutPrice", Double.parseDouble(STATCactivityGoodsPara.getSalePrice())-Double.parseDouble(STATCactivityGoodsPara.getDisMount()));    
        	 map.put("totoalCut", STATCactivityGoodsPara.getDisMount());   
        	 map.put("resultCode", "1");
         }else{
        	 map.put("resultCode", "9");
     		 map.put("resultInfo","系统异常请稍候在尝试！");   
         }        
    	return  map;
    }
    
    
    //查询当前用户活动信息 （ID是活动唯一的）
    @RequestMapping("queryUserCutInfo")
    @ResponseBody
    public  Map<String,Object>  queryUserCutInfo(HttpServletRequest request,Model model){       	
    	 Map<String, Object>  map = new HashMap<String, Object>();    	 	
    	 
    	 if (STATCactivityGoodsPara == null || STATCactivityGoodsPara.getActCode() == null || "".equals(STATCactivityGoodsPara.getActCode())){
     		STATCactivityGoodsPara = this.getInitActivityGoodsPara();
     	}  
    	String actId = request.getParameter("actId");
    	ActivityCutPrice activityCutPrice = new ActivityCutPrice();
    		activityCutPrice.setId(actId);   
         List<ActivityCutPrice> queryActivityCutPriceList  = activityTargetNumberServiceImpl.queryActivityCutPriceList(activityCutPrice);
         logger.info("queryUserCutInfo " + queryActivityCutPriceList.get(0));
        if(queryActivityCutPriceList.size()>0){
        	map.put("resultCode", "1");
        	 //个人信息手机号码模糊化        	
        	map.put("cutNumebr",UtilString.showMemberName(queryActivityCutPriceList.get(0).getActNumber()));
        	map.put("totalAmount",queryActivityCutPriceList.get(0).getTotalAmount());
        	map.put("sale",Double.parseDouble(STATCactivityGoodsPara.getCostPrice()) - queryActivityCutPriceList.get(0).getTotalAmount());
        }
        else{
        	 map.put("resultCode", "9");
     		 map.put("resultInfo","系统异常请稍候在尝试！");
        }
    	return  map;
    }
    
    
    //查询帮助砍价的明细
    @RequestMapping("queryUserCutInfoDetail")
    @ResponseBody
    public  Map<String,Object>  queryUserCutInfoDetail(HttpServletRequest request,Model model){    
    	Map<String, Object>  map = new HashMap<String, Object>();
    	String actId = request.getParameter("actId");
    	if(actId == null || "".equals(actId))
    	{
    		  map.put("resultCode", "9");
      		  map.put("resultInfo","微信进行砍价活动，请保持正确的姿势！"); 
      		 
      		  return  map; 
    	}
    	 
    	  ActivityCutPriceDetail  activityCutPriceDetail  = new ActivityCutPriceDetail();
          activityCutPriceDetail.setId(actId);        
          List<ActivityCutPriceDetail>  queryActivityCutPriceDetailList = 
        		  activityTargetNumberServiceImpl.queryActivityCutPriceDetailList(activityCutPriceDetail);
          if(queryActivityCutPriceDetailList.size()>0){
        	  map.put("resultCode", "1"); //计算总额
        	  double cutTotal = 0.0;
        	 
        	  StringBuffer showInfo = new StringBuffer();
        	  for(ActivityCutPriceDetail tmpActivityCutPriceDetail : queryActivityCutPriceDetailList){        		 
        		  showInfo.append("<div class=\"col col-6\">"+UtilString.showMemberName(tmpActivityCutPriceDetail.getCutNumber())+"</div>")
        		  .append("<div class=\"col col-6\">"+tmpActivityCutPriceDetail.getCutAmount()+"</div>");
        		  cutTotal+=tmpActivityCutPriceDetail.getCutAmount();
        	  }         	  
        	  map.put("cutTotal", cutTotal);
        	  map.put("resultInfo",showInfo); 
          }else{
        	  map.put("resultCode", "9");
        	  map.put("resultInfo","<div class=\"col col-6\">还没砍价记录，快快发起砍价呢！</div>"); 
          }
        
    	return  map;
    }
      
    
    @RequestMapping("helpCut")
    @ResponseBody
    public  Map<String,Object>  helpCut(HttpServletRequest request,Model model){    
    	Map<String, Object>  map = new HashMap<String, Object>();
    	//判断是否是本人    	
    	String actId = request.getParameter("actId"); 
    	
    	Session session = UserUtils.getSession();    	
       Object numberTmp = session.getAttribute(SID);
    	
    	if(numberTmp == null || "".equals(numberTmp)){
    		map.put("resultCode", "0");//没有登录需短信登录
    		map.put("resultInfo", "需要短信登录!"); 
    		return map;
    	} 
    	
    	if(!this.judgeActiveByActId(actId)){
    		    map.put("resultCode", "9");
		        map.put("resultInfo","旧的砍价活动已经失效！");      //
		        return map;
    	} 
    	
    	if (STATCactivityGoodsPara == null || STATCactivityGoodsPara.getActCode() == null || "".equals(STATCactivityGoodsPara.getActCode())){
    		STATCactivityGoodsPara = this.getInitActivityGoodsPara();
    	} 
    	
    	 ActivityCutPrice activityCutPrice = new ActivityCutPrice();    	
		activityCutPrice.setId(actId);   
    
		List<ActivityCutPrice> queryActivityCutPriceList  = activityTargetNumberServiceImpl.queryActivityCutPriceList(activityCutPrice);
        if(queryActivityCutPriceList.size()==0)
        {        	
                map.put("resultCode", "9");
		        map.put("resultInfo","系统异常请稍候在尝试！");      //
		        return map;
        }
        
        ActivityCutPrice activityCutPriceTmp = queryActivityCutPriceList.get(0);
        String  cutNumber = numberTmp.toString(); 
        if(!activityCutPriceTmp.getStatus().equals("0")){
        	map.put("resultCode", "9");
		    map.put("resultInfo","该用户砍掉的金额已经满足优惠金额！");      //用户砍掉的金额已经满足优惠金额！
		    return map; 
        } 
        
        if(activityCutPriceTmp.getActNumber().equals(cutNumber)){
        	//不可以给自己的活动进行砍价
			map.put("resultCode", "9");
    		map.put("resultInfo", "不可以给自己的活动进行砍价!");
    		 return map; 
        }
        
        
        ActivityCutPriceDetail  activityCutPriceDetail  = new ActivityCutPriceDetail();
        activityCutPriceDetail.setId(actId);
        activityCutPriceDetail.setCutNumber(cutNumber);
        List<ActivityCutPriceDetail> listActivityCutPriceDetail = activityTargetNumberServiceImpl.queryActivityCutPriceDetailList(activityCutPriceDetail);
        if(listActivityCutPriceDetail.size()>0){
       	 map.put("resultCode", "1");//已经参与过
       	 map.put("resultInfo","你已经帮好友砍过不能再砍。");   
       	 return map;
       	 }
        
        activityCutPriceDetail.setCutName("");//无值塞空
        activityCutPriceDetail.setCutAmount(getCurrentCutPrice(actId));
        activityCutPriceDetail.setCutDate(new Date());
        int result =  activityTargetNumberServiceImpl.addActivityCutPriceDetail(activityCutPriceDetail); 
        updateUserCutInfo(actId);
        map.put("resultCode", "1");//已经参与过
		map.put("resultInfo","你已经成功帮助好友砍价!");  
		//更新当前的用户状态	        	 
		
		map.put("resultCode", "1");
   	
		
		//砍价成功之后立即看到砍价效果
 	   activityCutPrice.setActNumber(null);
       queryActivityCutPriceList  = activityTargetNumberServiceImpl.queryActivityCutPriceList(activityCutPrice);
       logger.info("queryUserCutInfo " + queryActivityCutPriceList.get(0));
		    if(queryActivityCutPriceList.size()>0){
		    	map.put("resultCode", "1");    	 
		    	 //个人信息手机号码模糊化        	
		    	map.put("cutNumebr",UtilString.showMemberName(queryActivityCutPriceList.get(0).getActNumber()));
		    	map.put("totalAmount",queryActivityCutPriceList.get(0).getTotalAmount());
		    	map.put("sale",Double.parseDouble(STATCactivityGoodsPara.getCostPrice()) - queryActivityCutPriceList.get(0).getTotalAmount());
		    	map.put("totoalCut", STATCactivityGoodsPara.getDisMount()); 
		     } 
		    		 
		      activityCutPriceDetail.setCutNumber(null); 
	          List<ActivityCutPriceDetail>  queryActivityCutPriceDetailList = 
	        		  activityTargetNumberServiceImpl.queryActivityCutPriceDetailList(activityCutPriceDetail);
	          if(queryActivityCutPriceDetailList.size()>0){       	 
	        	  StringBuffer showInfo = new StringBuffer();
	        	  for(ActivityCutPriceDetail tmpActivityCutPriceDetail : queryActivityCutPriceDetailList){        		 
	        		  showInfo.append("<div class=\"col col-6\">"+UtilString.showMemberName(tmpActivityCutPriceDetail.getCutNumber())+"</div>")
	        		  .append("<div class=\"col col-6\">"+tmpActivityCutPriceDetail.getCutAmount()+"</div>");
 	        	  }    
	        	  map.put("showInfo",showInfo); 
	          } 
		return map; 
    }
    
  //固定的一个砍价数组，也可以修改随机生成砍价的金额，帮砍的人数是RSRV1的数值的量，所有数值相加需要等于DIS_MOUN的金额
  //TF_AT_GOODS_PARA---RSRV1
  private double getCurrentCutPrice(String actId) {
	  ActivityCutPriceDetail  activityCutPriceDetail  = new ActivityCutPriceDetail();
      activityCutPriceDetail.setId(actId);
       List<ActivityCutPriceDetail> listActivityCutPriceDetail = activityTargetNumberServiceImpl.queryActivityCutPriceDetailList(activityCutPriceDetail);
      if(listActivityCutPriceDetail.size()>0){
     	 return CUTPrice.get(listActivityCutPriceDetail.size());     			  
     	}
		return CUTPrice.get(0);
	}


	//查询当前用户活动信息
    @RequestMapping("queryWxUserInfo")
    @ResponseBody
    public  Map<String,Object>  queryWxUserInfo(HttpServletRequest request,Model model){    
    	Map<String, Object>  map = new HashMap<String, Object>();
      String wxUid = request.getParameter("wxUid");
    	// 取当前的手机号通过redis，微信存值在redis
    	logger.error("++++wxUid+++ " + wxUid);
     String  cutNumber = JedisClusterUtils.get(wxUid); 
    	logger.error("++++cutNumber+++ " + cutNumber);
    	if(cutNumber == null || "".equals(cutNumber)){    	
    		
    	}else {  	 
    	 
      // String  cutNumber = "15709876567";    		
    		Session session = UserUtils.getSession();
    		session.setAttribute(SID, cutNumber);
    	   } 
    	return  map;
    }
    
    
    //更新用户数据
    private void updateUserCutInfo(String actId) {
    	if (STATCactivityGoodsPara == null || STATCactivityGoodsPara.getActCode() == null || "".equals(STATCactivityGoodsPara.getActCode())){
    		STATCactivityGoodsPara = this.getInitActivityGoodsPara();
    	}     	
         if(STATCactivityGoodsPara != null ){ 
        	 //砍价的最高金额
        	 double totalDis =  Double.parseDouble(STATCactivityGoodsPara.getDisMount());    
         	 double totalCut = 0.0;
        	 
         	 ActivityCutPriceDetail  activityCutPriceDetail  = new ActivityCutPriceDetail();
             activityCutPriceDetail.setId(actId);        
             List<ActivityCutPriceDetail>  queryActivityCutPriceDetailList = 
           		  activityTargetNumberServiceImpl.queryActivityCutPriceDetailList(activityCutPriceDetail);
             if(queryActivityCutPriceDetailList.size()>0){
            	
            	 for(ActivityCutPriceDetail  activityCutPriceDetailTmp:queryActivityCutPriceDetailList){
            		 totalCut +=activityCutPriceDetailTmp.getCutAmount();
            	 } 
            	 
            	  logger.info("updateUserCutInfo actId  totalCut  " + totalCut);
            		ActivityCutPrice activityCutPrice = new ActivityCutPrice();    	
            		activityCutPrice.setId(actId);   
            		activityCutPrice.setTotalNum(queryActivityCutPriceDetailList.size());
             		activityCutPrice.setTotalAmount(totalCut);
            		if(totalCut >= totalDis){  
            			  logger.info("updateUserCutInfo actId  totalDis  " + totalDis);
            	    activityCutPrice.setStatus("1"); 
            		  //活动状态 0，砍价金额未满足砍活动要求，1、用户已经满足要求活动要求，2，用户已经占用取或者赠与代金券，
     	             //3，用户已经使用代金券，4、活动已经达到人数限制，5，活动已经达到金额限制            	    
            	    logger.info("updateUserCutInfo activityCutPrice  " + activityCutPrice); 
            	    //抵扣的金額是配置參數表的優惠金額 ，滿足條件的情况下才更新
            	    activityCutPrice.setDiscount(totalDis);
            	    
            	    int  p = activityTargetNumberServiceImpl.updateActivityCutPrice(activityCutPrice);
            	    if(p>-1){
            	    	//下发短信
            	    	   ActivityCutPrice activityCutPriceTemp = new ActivityCutPrice();    	
           			       activityCutPriceTemp.setId(actId);             	    	   
            	    		List<ActivityCutPrice> queryActivityCutPriceListTmp  = activityTargetNumberServiceImpl.queryActivityCutPriceList(activityCutPriceTemp);
                 	       if(null != queryActivityCutPriceListTmp && queryActivityCutPriceListTmp.size()>0){
                 	    		SmsSendCondition condition = new SmsSendCondition();
    	                        condition.setSerialNumber(queryActivityCutPriceListTmp.get(0).getActNumber());
    	                        condition.setNoticeContent(STATCactivityGoodsPara.getSmscontent());
    	                        try {
									smsSendService.sendSms(condition);
								} catch (Exception e) {									 
									e.printStackTrace();
								}
                 	       } 
            	      }           	  
            	    }else{
            	    	activityTargetNumberServiceImpl.updateActivityCutPrice(activityCutPrice);
            	    } 
            	 }  
             }
	}
    
    
  //判断当是否参与过砍价活动
    private boolean judgeActiveByActId(String actId) {
    	if (STATCactivityGoodsPara == null || STATCactivityGoodsPara.getActCode() == null || "".equals(STATCactivityGoodsPara.getActCode())){
    		STATCactivityGoodsPara = this.getInitActivityGoodsPara();
    	}     	
         if(STATCactivityGoodsPara != null ){	  
	    logger.info("   judgeActiveByActId activityGoodsPara " + STATCactivityGoodsPara.toString());
	   	ActivityCutPrice activityCutPrice = new ActivityCutPrice();    	
		activityCutPrice.setId(actId);   
		activityCutPrice.setActCode(STATCactivityGoodsPara.getActCode());		
		activityCutPrice.setGoodCode(STATCactivityGoodsPara.getGoodCode());
		List<ActivityCutPrice> queryActivityCutPriceList  = activityTargetNumberServiceImpl.queryActivityCutPriceList(activityCutPrice);
			if (queryActivityCutPriceList.size() > 0) {
				return true;
			}
         }
         return false;
	}
    
 // 查询ACTID
    public String getUserActId(HttpServletRequest request, String mobile) {
    	if (STATCactivityGoodsPara == null || STATCactivityGoodsPara.getActCode() == null || "".equals(STATCactivityGoodsPara.getActCode())){
    		STATCactivityGoodsPara = this.getInitActivityGoodsPara();
    	}     	
         if(STATCactivityGoodsPara != null ){	  
    	ActivityCutPrice activityCutPrice = new ActivityCutPrice();
        activityCutPrice.setActCode(CUT_PPRICE);      
        activityCutPrice.setActNumber(mobile);
        activityCutPrice.setGoodCode(STATCactivityGoodsPara.getGoodCode());
        List<ActivityCutPrice> queryActivityCutPriceList  = activityTargetNumberServiceImpl.queryActivityCutPriceList(activityCutPrice);
        if(queryActivityCutPriceList.size()>0){
        	return queryActivityCutPriceList.get(0).getId(); 
        }
        else return "";
         }  else{
        	 return ""; 
        }
    }
    
    
    //查询当前活动参数信息
    public  ActivityGoodsPara  getInitActivityGoodsPara(){
    	 ActivityGoodsPara activityGoodsPara  = new ActivityGoodsPara();
		 activityGoodsPara.setActCode(CUT_PPRICE);
		 activityGoodsPara.setChanId("E007");
		  List<ActivityGoodsPara> queryActivityGoodsParaList = activityTargetNumberServiceImpl.queryActivityGoodsParaList(activityGoodsPara);
	       if(null== queryActivityGoodsParaList || queryActivityGoodsParaList.size() == 0){
	    	 return null;
	       } 
	      activityGoodsPara  = queryActivityGoodsParaList.get(0);   	
	      if(null != activityGoodsPara && !"".equals(activityGoodsPara.getRsrv1())){
	    	  String  tempArra[] =  activityGoodsPara.getRsrv1().split(",");
	    	  for(String var : tempArra){
	    		  CUTPrice.add(Double.parseDouble(var));
	    	  }
	      } 
	      return activityGoodsPara;
    } 

	@RequestMapping(value = "sendBgRandomCode")
	@ResponseBody
	public Map<String, Object> sendRandomCode(HttpServletRequest request, String mobile) throws Exception {
		String code = "";
		Map<String, Object> map = new HashMap<String, Object>();		 		
		
		    BasicInfoCondition condition = new BasicInfoCondition();
	    	condition.setSerialNumber(mobile);
	    	condition.setxGetMode("0"); 
	     	 condition.setStaffId("ITFPCMAL");
	         condition.setTradeDepartPassword("656219");
	    	Map userInfo = basicInfoQryModifyService.queryUserBasicInfo(condition);	         	 
	    	JSONObject phoneAttributionJSON = JSONObject.parseArray(userInfo.get("result").toString()).getJSONObject(0);
			logger.info(JSON.toJSONString(userInfo,true));
			 String phoneNumberResultcode = phoneAttributionJSON.getString("X_RESULTCODE"); 
			if(!"0".equals(phoneNumberResultcode)){
			 
				map.put("X_RESULTINFO", "非湖南移动用户不能参与砍价活动！");
				map.put("X_RESULTCODE", "-1"); 
				return map;
			} 
	 
		if (jedisCluster.exists("BargainSmsSimMaxBomb_" + mobile)) {//判断5分钟内是否发送过短信
			map.put("X_RESULTINFO", "您的短信密码已发送到手机，5分钟内不重复发送");
			map.put("X_RESULTCODE", "-1");
		} else {
			smsSend4AllChanCondition.setSERIAL_NUMBER(mobile);
			smsSend4AllChanCondition.setCacheKey(mobile + "BargainSms_SMS_WEB");
			//允许用户输入错误三次
			smsSend4AllChanCondition.setErrorTimes(3);
			smsSend4AllChanCondition.setCodeLength(6);
			smsSend4AllChanCondition.setNOTICE_CONTENT("湖南移动微信营业厅提醒你：您本次参加砍价活动获得的验证码为：{$}");
			map = smsSendService.sendSms4AllChan(smsSend4AllChanCondition);
			code = JedisClusterUtils.get(mobile + "BargainSms_SMS_WEB");
			if (MapUtils.isNotEmpty(map) && "0".equals(map.get("X_RESULTCODE"))) {
				jedisCluster.set("BargainSmsSimMaxBomb_" + mobile, "1");
				jedisCluster.expire("BargainSmsSimMaxBomb_" + mobile, 300);//保持5分钟
			}
		}
		logger.error("发送随机短信密码，号码：" + mobile + "，code:" + map.get("X_RESULTCODE") + ",info:" + map.get("X_RESULTINFO") + "随机码：" + code);
		return map;
	}

	/**
	 * 校验验证码
	 * @param smsCode
	 * @return Map<String,Object>
	 * @throws Exception
	 */
	@RequestMapping(value="checkBarginCode")
	@ResponseBody
	public Map<String,Object> checkCode(String phoneNo,String smsCode,HttpServletRequest request)throws Exception{

		Map<String, Object> map = new HashMap<String, Object>();
		String flag = request.getParameter("flag");
		if(!(StringUtils.isNotEmpty(flag) && Integer.parseInt(flag)==0)){
			if(StringUtils.isEmpty( JedisClusterUtils.get(phoneNo+"BargainSms_SMS_WEB"))){
				map.put("X_RESULTCODE", "-1");
				map.put("X_RESULTINFO", "请先发送验证码");
				return map;
			}
		}
		smsSend4AllChanCondition.setSERIAL_NUMBER(phoneNo);
		smsSend4AllChanCondition.setCodeValue(smsCode);
		smsSend4AllChanCondition.setCacheKey(phoneNo+"BargainSms_SMS_WEB");
		Map<String, Object> smsResult = smsSendService.CheckSms4AllChan(smsSend4AllChanCondition);
		map.put("runningId", smsResult.get("runningId"));
		if(!("0".equals(smsResult.get("X_RESULTCODE")))){
			map.put("X_RESULTCODE", "-1");
			map.put("X_RESULTINFO", smsResult.get("message"));
			logger.error("校验短信验证码，号码：" + phoneNo + "，code:" + map.get("X_RESULTCODE") + ",info:" + map.get("X_RESULTINFO"));
			return map;
		} 
		
		Session session = UserUtils.getSession();    	
		session.setAttribute(SID, phoneNo);
		//校验成功放置在session 随取随用
		
		JedisClusterUtils.del(phoneNo + "BargainSms_SMS_WEB");
		map.put("X_RESULTCODE","0");
		map.put("X_RESULTINFO","验证码验证成功！");
		logger.error("校验短信验证码，号码："+phoneNo+"，code:"+map.get("X_RESULTCODE")+",info:"+map.get("X_RESULTINFO"));
		return map;
	} 
}
