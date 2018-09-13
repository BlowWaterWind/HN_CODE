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
import com.ai.ecs.o2o.api.O2oCommunityService;
import com.ai.ecs.o2o.api.O2oCommunityUserService;
import com.ai.ecs.o2o.entity.O2oAddrSearchRecord;
import com.ai.ecs.o2o.entity.O2oCommunityInfoBak;
import com.ai.ecs.o2o.entity.O2oCommunityUserInfo;
import com.google.common.collect.Maps;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by cc on 2017/9/12.
 */
@Controller
@RequestMapping("businessPromotion")
public class BusinessPromotionController {

    @Autowired
    private O2oAddrSearchService o2oAddrSearchService;

    @Autowired
    private O2oCommunityService o2oCommunityService;

    @Autowired
    private O2oCommunityUserService o2oCommunityUserService;
    @Autowired
    private PhoneAttributionService phoneAttributionService;

    private Logger logger = Logger.getLogger(BusinessPromotionController.class);

    @RequestMapping("recommend")
    public String businessRecommend(HttpServletRequest request, HttpServletResponse response, Model model) {
        MemberVo memberVo = UserUtils.getLoginUser(request);
        MemberInfo member =memberVo.getMemberInfo();
        //1：小区搜索；2：商机搜索；3：营销知识库 4:小区名...
        try {
            String cityName=member.getMemberCity();
            logger.info("memberVo:"+memberVo);
            //获取手机号码的归属地市
            if(!StringUtils.isNotEmpty(cityName)){
                cityName = this.getCityName(String.valueOf(memberVo.getMemberLogin().getMemberPhone()));
            }
            String cityCode = this.getCityCode(String.valueOf(memberVo.getMemberLogin().getMemberPhone()));
            Map topType = Maps.newHashMap();
            Map hisRecord = Maps.newHashMap();
            topType.put("SEARCH_TYPE", "1");
            hisRecord.put("SERIAL_NUMBER", String.valueOf(memberVo.getMemberLogin().getMemberPhone()));
            hisRecord.put("SEARCH_TYPE", "1");
            List<O2oAddrSearchRecord> topList = o2oAddrSearchService.queryTopSearch(hisRecord);
            List<O2oAddrSearchRecord> records = o2oAddrSearchService.querySearchRecordByPhone(hisRecord);
            model.addAttribute("topList", topList);
            model.addAttribute("searchList", records);
            model.addAttribute("cityName",cityName);
            model.addAttribute("cityCode",cityCode);
        } catch (Exception e) {
            logger.error("查询搜索记录接口失败", e);
        }
        return "web/broadband/o2o/businessPromotion/businessRecommend";
    }


    @RequestMapping("deleteSearch")
    @ResponseBody
    public Integer deleteRecord(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        try {
            MemberVo memberVo = UserUtils.getLoginUser(request);
            map.put("SEARCH_TYPE", "1");
            map.put("SERIAL_NUMBER", memberVo.getMemberLogin().getMemberPhone());
            return o2oAddrSearchService.deleteByPhone(map);
        } catch (Exception e) {
            logger.error("删除历史记录失败", e);
        }
        return 0;
    }

    @RequestMapping("deleteRecordSearch")
    @ResponseBody
    public Integer deleteRecordSearch(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        try {
            MemberVo memberVo = UserUtils.getLoginUser(request);
            map.put("SEARCH_TYPE", "2");
            map.put("SERIAL_NUMBER", memberVo.getMemberLogin().getMemberPhone());
            return o2oAddrSearchService.deleteByPhone(map);
        } catch (Exception e) {
            logger.error("删除历史记录失败", e);
        }
        return 0;
    }


    @RequestMapping("search")
    public String businessRecommendSearch(HttpServletRequest request, HttpServletResponse response, Model model) {
        MemberVo memberVo = UserUtils.getLoginUser(request);
        try{
            //1：小区搜索；2：商机搜索；3：营销知识库 4:小区名...
            Map topType = Maps.newHashMap();
            Map hisRecord = Maps.newHashMap();
            topType.put("SEARCH_TYPE", "2");
            hisRecord.put("SERIAL_NUMBER", String.valueOf(memberVo.getMemberLogin().getMemberPhone()));
            hisRecord.put("SEARCH_TYPE", "2");
            List<O2oAddrSearchRecord> top = o2oAddrSearchService.queryTopSearch(topType);
            List<O2oAddrSearchRecord> history = o2oAddrSearchService.querySearchRecordByPhone(hisRecord);
            model.addAttribute("topList", top);
            model.addAttribute("searchList", history);
        }catch (Exception e){
            logger.error("初始化查询商机页面",e);
        }
        return "web/broadband/o2o/businessPromotion/businessSearch";
    }

    @RequestMapping("telSearch")
    @ResponseBody
    public ResponseBean unserInfo(HttpServletRequest request, HttpServletResponse response, O2oAddrSearchRecord o2oAddrSearchRecord) {
        ResponseBean responseBean = new ResponseBean();
        String telNumber = request.getParameter("searchString");
        MemberVo memberVo = UserUtils.getLoginUser(request);
        o2oAddrSearchRecord.setSerialNumber(String.valueOf(memberVo.getMemberLogin().getMemberPhone()));
        try {
            o2oAddrSearchService.insert(o2oAddrSearchRecord);
            List<O2oCommunityUserInfo> o2oCommunityUserInfos = o2oCommunityUserService.selectByTel(telNumber);
            responseBean.addSuccess(o2oCommunityUserInfos);
        } catch (Exception e) {
            responseBean.addError("-1","系统异常");
            logger.error("查询用户信息失败", e);
        }
        return responseBean;
    }

    @RequestMapping("qryAddressCommunityName")
    @ResponseBody
    public ResponseBean qryAddressCommunityName(HttpServletRequest request,Model model) {
        String keyWord = request.getParameter("keyWords");
        String cityCode = request.getParameter("cityCode");
        ResponseBean responseBean = new ResponseBean();
        try {
            List<O2oCommunityInfoBak> o2oCommunityInfoBaks = o2oCommunityService.selectByKeyWord(keyWord,cityCode);
            responseBean.addSuccess(o2oCommunityInfoBaks);
        } catch (Exception e) {
            responseBean.addError("-1","系统异常");
            logger.error("查询用户信息用户消费信息失败", e);
        }
        return responseBean;
    }


    @RequestMapping("fetchCustomerInfo")
    public String fetchCustomerInfo(HttpServletRequest request, HttpServletResponse response, Model model) {
        String communityId = request.getParameter("communityId");
        MemberVo memberVo = UserUtils.getLoginUser(request);
        try {
            O2oCommunityInfoBak o2oCommunityInfoBak = o2oCommunityService.selectByPrimaryKey(Long.parseLong(communityId));
            O2oAddrSearchRecord o2oAddrSearchRecord = new O2oAddrSearchRecord();
            //保存搜索记录
            o2oAddrSearchRecord.setSearchString(o2oCommunityInfoBak.getZoneName());
            o2oAddrSearchRecord.setSearchType("1");
            o2oAddrSearchRecord.setSerialNumber(String.valueOf(memberVo.getMemberLogin().getMemberPhone()));
            o2oAddrSearchService.insert(o2oAddrSearchRecord);
            model.addAttribute("o2oCommunityInfoBak", o2oCommunityInfoBak);
        } catch (Exception e) {
            logger.error("查询用户信息用户消费信息失败", e);
        }
        return "web/broadband/o2o/businessPromotion/businessPotential";
    }

    @RequestMapping("filterCustomer")
    @ResponseBody
    public ResponseBean filterCustomer(HttpServletRequest request) {
        ResponseBean responseBean = new ResponseBean();
        String communityId = request.getParameter("communityId");
        String type =  request.getParameter("type");
        try {
            List<O2oCommunityUserInfo> o2oCommunityUserInfos = o2oCommunityUserService.selectByType(Long.parseLong(communityId),type);
            responseBean.addSuccess(o2oCommunityUserInfos);
        } catch (Exception e) {
            responseBean.addError("-1","系统异常");
            logger.error("过滤客户失败", e);
        }
        return responseBean;
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
    /**
     * 获取手机号码的归属地市编码
     * @param installPhoneNum
     * @return
     * @throws Exception
     */
    private String getCityCode(String installPhoneNum) throws Exception{
        PhoneAttributionModel phoneAttributionModel = new PhoneAttributionModel();
        phoneAttributionModel.setSerialNumber(installPhoneNum);
        Map<String, Object> resultMap = phoneAttributionService.queryPhoneAttribution(phoneAttributionModel);
        String cityCode = String.valueOf(((Map)((List)resultMap.get("result")).get(0)).get("AREA_CODE"));
        return cityCode;
    }
}
