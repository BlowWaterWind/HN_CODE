package com.ai.ecs.ecm.mall.wap.modules.o2o.business;

import com.ai.ecs.common.ResponseBean;
import com.ai.ecs.common.utils.StringUtils;
import com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils;
import com.ai.ecs.ecsite.modules.myMobile.entity.PhoneAttributionModel;
import com.ai.ecs.ecsite.modules.phoneAttribution.service.PhoneAttributionService;
import com.ai.ecs.member.api.IMemberInfoService;
import com.ai.ecs.member.entity.MemberInfo;
import com.ai.ecs.member.entity.MemberVo;
import com.ai.ecs.o2o.api.O2oAddrSearchService;
import com.ai.ecs.o2o.api.O2oBusinessService;
import com.ai.ecs.o2o.entity.*;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by cc on 2017/9/12.
 */
@Controller
@RequestMapping("businessKnowledge")
public class BusinessKnowledgeController {

    private Logger logger = Logger.getLogger(BusinessKnowledgeController.class);

    @Autowired
    private O2oAddrSearchService o2oAddrSearchService;

    @Autowired
    private O2oBusinessService o2oBusinessService;

    @Autowired
    private PhoneAttributionService phoneAttributionService;

    @RequestMapping("list")
    public String businessKnowledge(HttpServletRequest request, Model model){
        MemberVo memberVo = UserUtils.getLoginUser(request);
        MemberInfo member =memberVo.getMemberInfo();
        //1：小区搜索；2：商机搜索；3：营销知识库 4:小区名..
        //
        String cityName=member.getMemberCity();
        logger.info("memberVo:"+memberVo);
        //获取手机号码的归属地市
        try{
            if(!StringUtils.isNotEmpty(cityName)){
                cityName = this.getCityName(String.valueOf(memberVo.getMemberLogin().getMemberPhone()));
            }
        }catch (Exception e){
            logger.error("查询营销知识业务规则失败", e);
        }
        model.addAttribute("cityName",cityName);
        return "web/broadband/o2o/businessKnowledge/businessKnowledge";
    }

    @RequestMapping("deleteRecord")
    @ResponseBody
    public Integer deleteRecord(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        try {
            MemberVo memberVo = UserUtils.getLoginUser(request);
            map.put("SEARCH_TYPE", "3");
            map.put("SERIAL_NUMBER", memberVo.getMemberLogin().getMemberPhone());
            return o2oAddrSearchService.deleteByPhone(map);
        } catch (Exception e) {
            logger.error("删除历史记录失败", e);
        }
        return 0;
    }

    @RequestMapping("businessRule")
    public String businessRule(HttpServletRequest request,Model model,String businessName) {
        MemberVo memberVo = UserUtils.getLoginUser(request);
        try {
            String businessId = request.getParameter("businessId");
            List<O2oBusinessRule> o2oBusinessRules = o2oBusinessService.selectByBusinessId(businessId);
            O2oAddrSearchRecord o2oAddrSearchRecord = new O2oAddrSearchRecord();
            o2oAddrSearchRecord.setSearchType("3");
            o2oAddrSearchRecord.setSearchString(businessName);
            o2oAddrSearchRecord.setSerialNumber(memberVo.getMemberLogin().getMemberLogingName());
            o2oAddrSearchService.insert(o2oAddrSearchRecord);
            model.addAttribute("businessId",businessId);
            model.addAttribute("o2oBusinessRules",o2oBusinessRules);
        } catch (Exception e) {
            logger.error("查询营销知识业务规则失败", e);
        }
        return "web/broadband/o2o/businessKnowledge/businessKnowledgeDetail";
    }


    @RequestMapping("businessDetail")
    @ResponseBody
    public ResponseBean businessDetail(HttpServletRequest request,Model model) {
        ResponseBean responseBean = new ResponseBean();
        try {
            //1 业务规则 2 业务办理渠道 3 业务问答
            String businessId = request.getParameter("businessId");
            String type = request.getParameter("type");
            if("1".equals(type)){
                List<O2oBusinessRule> o2oBusinessRules = o2oBusinessService.selectByBusinessId(businessId);
                responseBean.addSuccess(o2oBusinessRules);
            }else if("2".equals(type)){
                List<O2oBusinessChannel> o2oBusinessChannels = o2oBusinessService.selectByBusinessChannel(businessId);
                responseBean.addSuccess(o2oBusinessChannels);
            }else{
                List<O2oBusinessQa> o2oBusinessQaList = o2oBusinessService.selectByBusinessQa(businessId);
                responseBean.addSuccess(o2oBusinessQaList);
            }
        } catch (Exception e) {
            logger.error("查询营销知识详情失败", e);
        }
        return responseBean;
    }


    @RequestMapping("search")
    @ResponseBody
    public ResponseBean businessKnowledgeSearch(HttpServletRequest request){
        MemberVo memberVo = UserUtils.getLoginUser(request);
        ResponseBean responseBean = new ResponseBean();
        try{
            MemberInfo member = memberVo.getMemberInfo();
            // type=1 查询资费信息 type=2 促销活动
            String type = request.getParameter("type");
            // condition 0 全部资费 1 宽带资费 2 产品资费 3 魔百盒
            // condition 0 全部活动 1 存送优惠 2 终端促销 3 业务促销
            String condition = request.getParameter("condition");
            List<O2oBusiness> o2oBusinessList = o2oBusinessService.selectBusiness(type,condition);
            responseBean.addSuccess("0","成功",o2oBusinessList);
        }catch (Exception e){
            logger.error("按类型查询营销知识失败",e);
            responseBean.addError("失败");
        }
        return responseBean;
    }

    @RequestMapping("keyWordSearch")
    @ResponseBody
    public ResponseBean keyWordSearch(HttpServletRequest request){
        ResponseBean responseBean = new ResponseBean();
        try{
            List<O2oBusiness> o2oBusinessList = o2oBusinessService.selectByKeyWord(request.getParameter("keyWords"));
            responseBean.addSuccess("0","成功",o2oBusinessList);
        }catch (Exception e){
            logger.error("关键字查询营销知识失败",e);
            responseBean.addError("失败");
        }
        return responseBean;
    }


    @RequestMapping("searchRecord")
    public void fetchSearchRecord(HttpServletRequest request,HttpServletResponse response){
        JSONObject result = new JSONObject();
        result.put("resultCode",-1);
        MemberVo memberVo = UserUtils.getLoginUser(request);
        try{
            Map topType = Maps.newHashMap();
            Map hisRecord = Maps.newHashMap();
            //1：小区搜索；2：商机搜索；3：营销知识库 4:小区名
            topType.put("SEARCH_TYPE","3");
            hisRecord.put("SERIAL_NUMBER",String.valueOf(memberVo.getMemberLogin().getMemberPhone()));
            hisRecord.put("SEARCH_TYPE","3");
            List<O2oAddrSearchRecord> topList = o2oAddrSearchService.queryTopSearch(topType);
            List<O2oAddrSearchRecord> historyList = o2oAddrSearchService.querySearchRecordByPhone(hisRecord);
            result.put("resultCode",0);
            result.put("topList",topList);
            result.put("historyList",historyList);
            response.setContentType("text/html;charset=UTF-8");
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            PrintWriter pw = response.getWriter();
            pw.write(result.toJSONString());
            pw.close();
        }catch (Exception e){
            logger.error("查询热门记录或用户搜索记录失败",e);
        }
    }


    /**
     * 获取手机号码的归属地市编码
     * @param installPhoneNum
     * @return
     * @throws Exception
     */
    private String getCityName(String installPhoneNum) throws Exception{
        PhoneAttributionModel phoneAttributionModel = new PhoneAttributionModel();
        phoneAttributionModel.setSerialNumber(installPhoneNum);
        Map<String, Object> resultMap = phoneAttributionService.queryPhoneAttribution(phoneAttributionModel);
        return String.valueOf(((Map)((List)resultMap.get("result")).get(0)).get("CITY_NAME"));
    }

}
