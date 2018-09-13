package com.ai.ecs.ecm.mall.wap.modules.o2o;

import com.ai.ecs.common.utils.StringUtils;
import com.ai.ecs.ecm.mall.wap.modules.goods.constant.BroadbandConstants;
import com.ai.ecs.ecm.mall.wap.modules.goods.utils.BroadbandUtils;
import com.ai.ecs.ecm.mall.wap.platform.interceptor.LoginInterceptor;
import com.ai.ecs.ecm.mall.wap.platform.utils.DateUtils;
import com.ai.ecs.ecm.mall.wap.platform.utils.InvokeEcop;
import com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils;
import com.ai.ecs.ecop.cms.entity.BroadbandPoster;
import com.ai.ecs.ecop.statis.util.DateFormatUtil;
import com.ai.ecs.ecop.sys.service.DictService;
import com.ai.ecs.ecsite.modules.broadBand.entity.QryAddressCityNameCondition;
import com.ai.ecs.ecsite.modules.broadBand.entity.QryAddressNameCondition;
import com.ai.ecs.ecsite.modules.broadBand.service.QryAddressService;
import com.ai.ecs.ecsite.modules.myMobile.entity.PhoneAttributionModel;
import com.ai.ecs.ecsite.modules.phoneAttribution.service.PhoneAttributionService;
import com.ai.ecs.goods.api.IGoodsManageService;
import com.ai.ecs.goods.entity.goods.BroadbandItemEntity;
import com.ai.ecs.goods.entity.goods.TfGoodsInfo;
import com.ai.ecs.member.api.IMemberAddressService;
import com.ai.ecs.member.api.IMemberInfoService;
import com.ai.ecs.member.entity.MemberInfo;
import com.ai.ecs.member.entity.MemberVo;
import com.ai.ecs.member.entity.ThirdLevelAddress;
import com.ai.ecs.o2o.api.O2oAddrSearchService;
import com.ai.ecs.o2o.entity.O2oAddrSearchRecord;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by think on 2017/9/11.
 */
@Controller
@RequestMapping("o2oBandSource")
public class O2oBandSourceController {
    private final static Logger logger = LoggerFactory.getLogger(O2oBandSourceController.class);

    @Autowired
    private O2oAddrSearchService o2oAddrSearchService;
    @Autowired
    private IMemberInfoService memberInfoService;
    @Autowired
    private DictService dictService;
    @Autowired
    private IGoodsManageService goodsManageService;
    @Autowired
    InvokeEcop invokeEcop;
    @Autowired
    QryAddressService qryAddressService;
    @Autowired
    private PhoneAttributionService phoneAttributionService;
    @Autowired
    private IMemberAddressService memberAddressService;
    /**
     * 宽带地址查询页面
     * @param model
     * @return
     */
    @RequestMapping("address")
    public String bandAddressQuery(Model model, HttpServletRequest request) throws Exception {
        MemberVo memberVo = UserUtils.getLoginUser(request);
        MemberInfo member =memberVo.getMemberInfo();
        String cityName=member.getMemberCity();
        String county=member.getMemberCounty();
        logger.info("memberVo:"+memberVo);
        //获取手机号码的归属地市
        Map<String,String> cityResult = this.getCityName(String.valueOf(memberVo.getMemberLogin().getMemberPhone()));
        cityName =cityResult.get("CITY_NAME");
        String cityCode =cityResult.get("EPARCHY_CODE");
        logger.info("cityName:"+cityName);
        //查询区县信息
        List countyList = null;
        QryAddressCityNameCondition condition = new QryAddressCityNameCondition();
        condition.setEPARCHY_CODE(String.valueOf(cityCode));
        Map addreMap = qryAddressService.qryAddressCityName(condition);
       if("0".equals(addreMap.get("respCode"))){
           List list = (List) addreMap.get("result");
           Map result = (Map) list.get(0);
           countyList = (List) result.get("ADDRESS_INFO");
           if(countyList!=null&&countyList.size()>0){
               Map countyMap = (Map) countyList.get(0);
               county = (String) countyMap.get("CITY_NAME");
               model.addAttribute("countyList",countyList);
           }else{
               throw new Exception("根据手机号未获取到区县信息");
           }
       }else{
           throw new Exception("县区查询接口异常");
       }
        logger.info("countyList:"+ JSON.toJSONString(countyList));
        logger.info("county:"+county);
        if ("湘西土家族苗族自治州".equals(cityName)) {
            model.addAttribute("city","湘西州");
        }
        if(cityName.indexOf("市")!=-1){
            model.addAttribute("city",cityName.substring(0,cityName.indexOf("市")));
        }else{
            model.addAttribute("city",cityName);
        }
        model.addAttribute("county",county);
        //查询热门搜索记录与用户搜索记录
        Map<String,Object> map = new HashMap<>();
        map.put("SEARCH_TYPE","1");
        List<O2oAddrSearchRecord> topList = o2oAddrSearchService.queryTopSearch(map);
        logger.info("countyList:"+ JSON.toJSONString(countyList));
        map.put("SERIAL_NUMBER",String.valueOf(memberVo.getMemberLogin().getMemberPhone()));
        List<O2oAddrSearchRecord> searchList = o2oAddrSearchService.querySearchRecordByPhone(map);
        logger.info("searchList:"+ JSON.toJSONString(searchList));
        model.addAttribute("cityCode",cityCode);
        model.addAttribute("topList",topList);
        model.addAttribute("searchList",searchList);
        return "web/broadband/o2o/source/addressIndex";
    }

    /**
     * 套餐查询
     * @param model
     * @return
     */
    @RequestMapping("queryPackage")
    public String  queryPackage(HttpServletRequest request,Model model,String addressPath,String communityName,String city,String county) throws Exception {
        //单宽带
        Map<String,Object> bbArgs = new HashMap<>();
        bbArgs.put("preCategoryId", dictService.getDictValue("BROADBAND_CATEGORY_ID","BROADBAND_CATEGORY_ID_ADD"));
        List<String> eparchyCodes = new ArrayList<>();
        eparchyCodes.add(BroadbandConstants.EPARCHY_CODE);
        bbArgs.put("eparchyCodes",eparchyCodes);
        bbArgs.put("orderColumn","SV.BARE_PRICE");
        bbArgs.put("orderType","ASC");
        bbArgs.put("goodsStatusId",4);
        bbArgs.put("chnlCode","E007");
        MemberVo memberVo = UserUtils.getLoginUser(request);
        MemberInfo member =memberVo.getMemberInfo();
        communityName = java.net.URLDecoder.decode(communityName,"UTF-8");
        addressPath = java.net.URLDecoder.decode(addressPath,"UTF-8");
        city = java.net.URLDecoder.decode(city,"UTF-8");
        county = java.net.URLDecoder.decode(county,"UTF-8");
        model.addAttribute("communityName",communityName);
        model.addAttribute("addressPath",addressPath);
        model.addAttribute("city",city);
        model.addAttribute("county",county);
        List<TfGoodsInfo> goodsInfoList = goodsManageService.queryBroadInfos(bbArgs);


        // 是否有宽带覆盖
        Boolean flag = false;
        // 查询地址是否宽带覆盖
        QryAddressNameCondition condition = new QryAddressNameCondition();
        if (city != null && county != null){
            String tempStr = city+" "+county+" "+addressPath;
            condition.setKEY_STRING(tempStr);
        }else{ //通过楼栋查询最终地址
            condition.setKEY_STRING(addressPath);
        }
        Map<String, Object> map = new HashMap<>();
        try{
            map = qryAddressService.qryAddressName(condition);
        }catch (Exception e){
            map.put("X_RESULTCODE", -1); //接口调用失败
            e.printStackTrace();
        }
        List list = (List) map.get("result");
        Map result = (Map) list.get(0);
        List addressList= (List) result.get("ADDRESS_INFO");
        if(addressList!=null&&addressList.size()>0){
            Map addressInfo = (Map) addressList.get(0);
            if(StringUtils.isNotEmpty(String.valueOf(addressInfo.get("HOUSE_NAME")))){
                flag = true;
            }
        }
        List<BroadbandPoster> broadbandPosterList = invokeEcop.getO2oBroadbandPosterInfo("光宽带办理","2");
        List<BroadbandPoster> hebroadbandPosterList = invokeEcop.getO2oBroadbandPosterInfo("和家庭套餐办理","2");
        List<BroadbandPoster> consupostnPosterList = invokeEcop.getO2oBroadbandPosterInfo("消费叠加型套餐办理","2");
        //是否有宽带业务
        Boolean  hasData = broadbandPosterList.size()==0&&hebroadbandPosterList.size()==0&&consupostnPosterList.size()==0;
        if(hasData){
            flag = false;
        }else if(!hasData&&flag){
            flag = true;
        }else{
            flag = false;
        }
        model.addAttribute("broadbandPosterList", broadbandPosterList);
        model.addAttribute("hebroadbandPosterList", hebroadbandPosterList);
        model.addAttribute("consupostnPosterList",consupostnPosterList);
        model.addAttribute("hasData",flag);
        return "web/broadband/o2o/source/packageIndex";
    }
    @RequestMapping("initSourceIndex")
    public String initSourceIndex(Model model,String city,String county,String communityName ){
        model.addAttribute("city",city);
        model.addAttribute("county",county);
        model.addAttribute("communityName",communityName);
        return "web/broadband/o2o/source/initSourceIndex";
    }

    /**
     * 保存搜索记录
     * @param request
     * @param record
     * @return
     */
    @RequestMapping("saveSearch")
    @ResponseBody
    public Integer saveSearchRecord(HttpServletRequest request,O2oAddrSearchRecord record){
        MemberVo memberVo = UserUtils.getLoginUser(request);
        record.setSearchTime(new Date());
        record.setSerialNumber(String.valueOf(memberVo.getMemberLogin().getMemberPhone()));
        record.setSearchId(getSequenceID());
        return o2oAddrSearchService.insert(record);
    }


    /**
     * 删除查询记录
     * @param request
     * @return
     */
    @RequestMapping("deleteRecord")
    @ResponseBody
    public Integer deleteRecord(HttpServletRequest request){
        MemberVo memberVo = UserUtils.getLoginUser(request);
        Map<String,Object> map = new HashMap<>();
        map.put("SEARCH_TYPE","1");
        map.put("SERIAL_NUMBER",memberVo.getMemberLogin().getMemberPhone());
        return o2oAddrSearchService.deleteByPhone(map);
    }

    private Long getSequenceID(){
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateString = formatter.format(currentTime);
        Random rand = new Random();
        int randNum = rand.nextInt(9000)+1000;
        String sequenceId = dateString+randNum;
        return Long.parseLong(sequenceId);
    }

    private  String regPattern(String value,String regx){
        // 创建 Pattern 对象
        Pattern r = Pattern.compile(regx);
        String result = "";
        // 现在创建 matcher 对象
        Matcher m = r.matcher(value);
        if (m.find( )) {
            result = m.group(1);
        }
        return result;
    }

    /**
     * 获取手机号码的归属地市编码
     * @param installPhoneNum
     * @return
     * @throws Exception
     */
    private Map<String,String> getCityName(String installPhoneNum) throws Exception{
        Map<String,String> result = new HashMap<>();
        PhoneAttributionModel phoneAttributionModel = new PhoneAttributionModel();
        phoneAttributionModel.setSerialNumber(installPhoneNum);
        Map<String, Object> resultMap = phoneAttributionService.queryPhoneAttribution(phoneAttributionModel);
        String EPARCHY_CODE= String.valueOf(((Map) ((List) resultMap.get("result")).get(0)).get("AREA_CODE"));
        String CITY_NAME= String.valueOf(((Map) ((List) resultMap.get("result")).get(0)).get("CITY_NAME"));
        result.put("EPARCHY_CODE",EPARCHY_CODE);
        result.put("CITY_NAME",CITY_NAME);
        return result;
    }
}
