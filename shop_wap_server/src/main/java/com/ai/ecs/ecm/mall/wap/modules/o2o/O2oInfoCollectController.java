package com.ai.ecs.ecm.mall.wap.modules.o2o;

import com.ai.ecs.common.ResponseBean;
import com.ai.ecs.common.utils.StringUtils;
import com.ai.ecs.common.utils.TFSClient;
import com.ai.ecs.ecm.mall.wap.modules.BaseController;
import com.ai.ecs.ecm.mall.wap.modules.o2o.util.O2oParamUtils;
import com.ai.ecs.ecm.mall.wap.platform.utils.TriDes;
import com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils;
import com.ai.ecs.ecop.statis.util.DateFormatUtil;
import com.ai.ecs.ecop.sys.entity.Dict;
import com.ai.ecs.ecop.sys.service.DictService;
import com.ai.ecs.ecsite.modules.broadBand.entity.*;
import com.ai.ecs.ecsite.modules.broadBand.service.QryAddressService;
import com.ai.ecs.entity.base.Page;
import com.ai.ecs.member.api.IMemberInfoService;
import com.ai.ecs.member.entity.ChannelInfo;
import com.ai.ecs.member.entity.MemberInfo;
import com.ai.ecs.member.entity.MemberVo;
import com.ai.ecs.o2o.api.O2oAddrSearchService;
import com.ai.ecs.o2o.api.O2oCommunityPartnersService;
import com.ai.ecs.o2o.api.O2oCommunityService;
import com.ai.ecs.o2o.api.O2oCommunityUserService;
import com.ai.ecs.o2o.entity.*;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

/**
 * Created by cc on 2017/9/18.
 */
@Controller
@RequestMapping("infoCollect")
public class O2oInfoCollectController extends BaseController {

    @Autowired
    private O2oCommunityService o2oCommunityService;

    @Autowired
    private O2oCommunityPartnersService o2oCommunityPartnersService;

    @Autowired
    private O2oCommunityUserService o2oCommunityUserService;

    @Autowired
    private O2oAddrSearchService o2oAddrSearchService;

    @Autowired
    private DictService dictService;

    @Autowired
    private QryAddressService qryAddressService;
    @Autowired
    private O2oParamUtils o2oParamUtils;

    private static String keyStr = "ABCDEFGHIJKLMNOP" + "QRSTUVWXYZabcdef" + "ghijklmnopqrstuv"
            + "wxyz0123456789+/" + "=";

    private static int pageSizePartner = 4;

    private static int pageSizeUser = 8;

    /**
     * 初始化地市编码
     */
    private static Map<String, String> cityName = new LinkedHashMap<>();

    static {
        cityName.put("0731", "长沙");
        cityName.put("0733", "株洲");
        cityName.put("0732", "湘潭");
        cityName.put("0734", "衡阳");
        cityName.put("0739", "邵阳");
        cityName.put("0730", "岳阳");
        cityName.put("0736", "常德");
        cityName.put("0744", "张家界");
        cityName.put("0737", "益阳");
        cityName.put("0735", "郴州");
        cityName.put("0746", "永州");
        cityName.put("0745", "怀化");
        cityName.put("0738", "娄底");
        cityName.put("0743", "湘西州");
    }


    @RequestMapping("search")
    public String businessRecommendSearch(HttpServletRequest request, HttpServletResponse response, Model model) {
        MemberVo memberVo = UserUtils.getLoginUser(request);
        try {
            //1：小区搜索；2：商机搜索；3：营销知识库 4:小区名...
            Map topType = Maps.newHashMap();
            Map hisRecord = Maps.newHashMap();
            topType.put("SEARCH_TYPE", "4");
            hisRecord.put("SERIAL_NUMBER", String.valueOf(memberVo.getMemberLogin().getMemberPhone()));
            hisRecord.put("SEARCH_TYPE", "4");
            List<O2oAddrSearchRecord> topList = o2oAddrSearchService.queryTopSearch(topType);
            List<O2oAddrSearchRecord> historyList = o2oAddrSearchService.querySearchRecordByPhone(hisRecord);
            for(O2oAddrSearchRecord record : topList){
                String[] strings = record.getSearchString().split(";");
                record.setSearchStringBak(strings[2]);
            }
            for(O2oAddrSearchRecord record : historyList){
                String[] strings = record.getSearchString().split(";");
                record.setSearchStringBak(strings[2]);
            }
            model.addAttribute("topList", topList);
            model.addAttribute("historyList", historyList);
            model.addAttribute("cityNames", cityName);
        } catch (Exception e) {
            logger.error("查询热门搜索和历史搜索失败", e);
        }
        return "web/broadband/o2o/sourceMgr/infoSearch";
    }

    /**
     * 搜索完之后返回时更新infoSearch.jsp的搜索记录
     *
     * @param request
     * @return
     */
    @RequestMapping("updateRecord")
    @ResponseBody
    public Map updateRecord(HttpServletRequest request) {
        //1：小区搜索；2：商机搜索；3：营销知识库 4:小区名
        MemberVo memberVo = UserUtils.getLoginUser(request);
        String type = request.getParameter("type");
        Map topType = Maps.newHashMap();
        Map hisRecord = Maps.newHashMap();
        Map result = Maps.newHashMap();
        result.put("resultCode", "0");
        topType.put("SEARCH_TYPE", type);
        hisRecord.put("SERIAL_NUMBER", String.valueOf(memberVo.getMemberLogin().getMemberPhone()));
        hisRecord.put("SEARCH_TYPE", type);
        try {
            List<O2oAddrSearchRecord> topList = o2oAddrSearchService.queryTopSearch(topType);
            List<O2oAddrSearchRecord> historyList = o2oAddrSearchService.querySearchRecordByPhone(hisRecord);
            for(O2oAddrSearchRecord record : topList){
                String[] strings = record.getSearchString().split(";");
                record.setSearchStringBak(strings[2]);
            }
            for(O2oAddrSearchRecord record : historyList){
                String[] strings = record.getSearchString().split(";");
                record.setSearchStringBak(strings[2]);
            }
            result.put("topList", topList);
            result.put("historyList", historyList);
        } catch (Exception e) {
            logger.error("更新历史记录失败", e);
            result.put("resultCode", "-1");
        }
        return result;
    }

    @RequestMapping("deleteRecord")
    @ResponseBody
    public Integer deleteRecord(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        try {
            MemberVo memberVo = UserUtils.getLoginUser(request);
            map.put("SEARCH_TYPE", "4");
            map.put("SERIAL_NUMBER", memberVo.getMemberLogin().getMemberPhone());
            return o2oAddrSearchService.deleteByPhone(map);
        } catch (Exception e) {
            logger.error("删除历史记录失败", e);
        }
        return 0;
    }

    @RequestMapping("initAdd")
    public String initAdd(HttpServletRequest request, O2oCommunityInfoBak o2oCommunityInfoBak, O2oAddrSearchRecord o2oAddrSearchRecord, Model model) {
        MemberVo memberVo = UserUtils.getLoginUser(request);
        // infoType=1 从添加按钮进入-->添加地址信息 infoType=2 从我的录入过的信息或添加完用户及友商进入该页面-->不能修改地址信息
        String infoType = request.getParameter("infoType");
        String tabType = request.getParameter("tab");
        model.addAttribute("tabType", tabType);
        try {
            o2oAddrSearchRecord.setSerialNumber(String.valueOf(memberVo.getMemberLogin().getMemberPhone()));
            o2oAddrSearchService.insert(o2oAddrSearchRecord);
            if("2".equals(infoType)){
                Long communityId = o2oCommunityInfoBak.getCommunityId();
                O2oCommunityInfoBak o2oCommunity = o2oCommunityService.selectByPrimaryKeyBak(communityId);
                model.addAttribute("o2oCommunity", o2oCommunity);
//            }else{
//                model.addAttribute("o2oCommunity", new O2oCommunityInfoBak());
            }
            List<Dict> addrType = dictService.getDictList("ADDR_TYPE");
            List<Dict> cellType = dictService.getDictList("CELL_TYPE");
            List<Dict> buildType = dictService.getDictList("BUILD_TYPE");
            List<Dict> buildCoverType = dictService.getDictList("BUILD_COVER_TYPE");
            List<Dict> areaType = dictService.getDictList("TYPE");
            List<Dict> userScene = dictService.getDictList("USER_SCENE");
            //地址类型1 地址类型2 城乡类型 用户场景
            model.addAttribute("addrType", addrType);
            model.addAttribute("cellType", cellType);
            model.addAttribute("buildCoverType", buildCoverType);
            model.addAttribute("buildType", buildType);
            model.addAttribute("areaType", areaType);
            model.addAttribute("userScene", userScene);
            model.addAttribute("cityNames", cityName);
            //情况1:查询进入该页面
            model.addAttribute("infoType", infoType);
           model.addAttribute("o2oCommunityInfoBak", o2oCommunityInfoBak);
        } catch (Exception e) {
            logger.error("进入添加盲点小区页失败", e);
        }
        return "web/broadband/o2o/sourceMgr/informationCollect";
    }

    @RequestMapping("initAddPartnerOrUser")
    public String initAddPartnerOrUser(HttpServletRequest request, O2oCommunityInfoBak o2oCommunityInfoBak, Model model,O2oAddrSearchRecord o2oAddrSearchRecord) {
        // type为空，从crm查询得到小区 进入该页面录入友商或用户
        // type不为空，录入友商或用户之后返回该页面
        try {
            MemberVo memberVo = UserUtils.getLoginUser(request);
            o2oAddrSearchRecord.setSerialNumber(String.valueOf(memberVo.getMemberLogin().getMemberPhone()));
            o2oAddrSearchService.insert(o2oAddrSearchRecord);

            String type = request.getParameter("type");
            if (type != null) {
                String tab = request.getParameter("tab");
                String communityId = request.getParameter("communityId");
                O2oCommunityInfoBak o2oCommunityInfo = o2oCommunityService.selectByPrimaryKeyBak(Long.parseLong(communityId));
                model.addAttribute("tabType", tab);
                model.addAttribute("o2oCommunityInfoBak", o2oCommunityInfo);
            } else {
                model.addAttribute("o2oCommunityInfoBak", o2oCommunityInfoBak);
            }
        } catch (Exception e) {
            logger.error("进入添加盲点小区页失败", e);
        }
        return "web/broadband/o2o/sourceMgr/partnerAndUserCollect";
    }

    @RequestMapping("saveCommunityInfo")
    @ResponseBody
    public Map saveCommunityInfo(HttpServletRequest request, O2oCommunityInfoBak o2oCommunityInfoBak) {
        Map resultMap = Maps.newHashMap();
        MemberVo memberVo = UserUtils.getLoginUser(request);
        ChannelInfo channelInfo=memberVo.getChannelInfo();
        o2oCommunityInfoBak.setInsertStaffId(String.valueOf(memberVo.getMemberLogin().getMemberPhone()));
        o2oCommunityInfoBak.setAuthSerialNumber(o2oCommunityInfoBak.getFeedbackMobile());
        String staffPwd = request.getParameter("staffPwd");//预约安装日期段
        staffPwd = TriDes.getInstance().strDec(staffPwd, keyStr, null, null);
        Map result = new HashMap();
        try {
            CommunityInfoCondition condition = new CommunityInfoCondition();
            condition.setRouteEparchyCode(o2oCommunityInfoBak.getRouteEparchyCode());
            condition.setEparchy(o2oCommunityInfoBak.getEparchy());
            condition.setEparchyName(o2oCommunityInfoBak.getEparchyName());
            condition.setBaBuildingCnt(o2oCommunityInfoBak.getBaBuildingCnt());
            condition.setAuthSerialNumber(o2oCommunityInfoBak.getFeedbackMobile());
            condition.setFeedbackMobile(o2oCommunityInfoBak.getFeedbackMobile());
            condition.setFeedbackPerson(o2oCommunityInfoBak.getFeedbackPerson());
            //家庭场景
            condition.setBaUserScene(o2oCommunityInfoBak.getBaUserScene());
            condition.setStreet(o2oCommunityInfoBak.getStreet());
            condition.setRoadName(o2oCommunityInfoBak.getRoadName());
            condition.setZoneName(o2oCommunityInfoBak.getZoneName());
            condition.setCommunityCode(o2oCommunityInfoBak.getCommunityCode());
            condition.setBuildinfName(o2oCommunityInfoBak.getBuildinfName());
            condition.setBuildingType(o2oCommunityInfoBak.getBuildingType());
            condition.setUnitName(o2oCommunityInfoBak.getUnitName());
            condition.setRoomNumber(o2oCommunityInfoBak.getRoomNumber());
            condition.setBuildingCoverType(o2oCommunityInfoBak.getBuildingCoverType());
            Calendar c = Calendar.getInstance();
            c.setTime(o2oCommunityInfoBak.getBaBuiltYear());
            int year = c.get(Calendar.YEAR);
            condition.setBaBuiltYear(String.valueOf(year));
            condition.setBaAddrType(o2oCommunityInfoBak.getBaAddrType());
            condition.setStreetName(o2oCommunityInfoBak.getStreetName());
            condition.setCityName(o2oCommunityInfoBak.getCityName());
            condition.setCity(o2oCommunityInfoBak.getCity());
            condition.setRoad(o2oCommunityInfoBak.getRoad());
            condition.setBaType(o2oCommunityInfoBak.getBaType());
            condition.setBaCellType(o2oCommunityInfoBak.getBaCellType());
//            condition(o2oCommunityInfoBak.getLatitude());
//            condition.setLONGITUDE(o2oCommunityInfoBak.getLongitude());
            o2oParamUtils.removeParams();
            o2oParamUtils.addConditionChannel(condition,channelInfo,"");
            condition.setTradeDepartPassword(staffPwd);
//            condition.setTradeDepartPassword("ai1234");
            condition.setCityCode(o2oCommunityInfoBak.getRouteEparchyCode());
            condition.setEparchyCodeFCust(o2oCommunityInfoBak.getRouteEparchyCode());
             result = qryAddressService.synchronizedCommunityInfo(condition);
            logger.info(JSON.toJSONString(resultMap,true));
            JSONObject jsonObject = (JSONObject)result;
         if("0".equals(jsonObject.get("respCode"))){
                List<Map> list = (List) result.get("result");
                if(list!=null&&list.size()>0){
                    Map addressMap = list.get(0);
//                    o2oCommunityInfoBak.setCommunityCode(String.valueOf(addressMap.get("ADDR_ID")));
                    O2oCommunityInfoBak o2oCommunityInfo = o2oCommunityService.insert(o2oCommunityInfoBak);
                    resultMap.put("resultCode","0");
                    resultMap.put("message","成功");
                    resultMap.put("data",o2oCommunityInfo);
                    resultMap.put("resultMap",result);
                }
            }else{
                resultMap.put("resultCode","-1");
                resultMap.put("message","失败");
                resultMap.put("resultMap",result);
            }
        } catch (Exception e) {
            result.put("respDesc","插入小区表失败!");
            logger.error("插入小区表失败", e);
            resultMap.put("resultCode","-1");
            resultMap.put("message","同步失败");
            resultMap.put("resultMap",result);
        }
        return resultMap;
    }

    @RequestMapping("updateCommunityInfo")
    @ResponseBody
    public ResponseBean updateCommunityInfo(HttpServletRequest request, O2oCommunityInfoBak o2oCommunityInfoBak) {
        ResponseBean responseBean = new ResponseBean();
        MemberVo memberVo = UserUtils.getLoginUser(request);
        o2oCommunityInfoBak.setInsertStaffId(String.valueOf(memberVo.getMemberLogin().getMemberPhone()));
        try {
            o2oCommunityService.updateByPrimaryKeySelective(o2oCommunityInfoBak);
            responseBean.addSuccess("更新成功");
        } catch (Exception e) {
            logger.error("更新小区表失败", e);
            responseBean.addError("-1", "更新失败");
        }
        return responseBean;
    }

    @RequestMapping("fetchInfo")
    @ResponseBody
    public ResponseBean fetchInfo(HttpServletRequest request, O2oCommunityInfoBak o2oCommunityInfoBak) {
        ResponseBean responseBean = new ResponseBean();
        String type = request.getParameter("type");
        int pageNo = 1;
        if ("1".equals(type)) {
            if (StringUtils.isNotBlank(request.getParameter("pageNoPartner"))) {
                pageNo = Integer.parseInt(request.getParameter("pageNoPartner"));
            }
        } else {
            if (StringUtils.isNotBlank(request.getParameter("pageNoUser"))) {
                pageNo = Integer.parseInt(request.getParameter("pageNoUser"));
            }
        }
        try {
            if ("1".equals(type)) {//找友商的信息
                O2oCommunityPartnersInfo o2oCommunityPartnersInfo = new O2oCommunityPartnersInfo();
                Page<O2oCommunityPartnersInfo> oCommunityPartnersInfoPage = new Page<>();
                oCommunityPartnersInfoPage.setPageSize(pageSizePartner);
                oCommunityPartnersInfoPage.setPageNo(pageNo);
                o2oCommunityPartnersInfo.setPage(oCommunityPartnersInfoPage);
                o2oCommunityPartnersInfo.setCommunityId(o2oCommunityInfoBak.getCommunityId());
                Page<O2oCommunityPartnersInfo> partnerPage = o2oCommunityPartnersService.selectByCommunity(o2oCommunityPartnersInfo);
                responseBean.addSuccess(type, partnerPage);
            } else {//查添加的用户的信息
                O2oCommunityUserInfo o2oCommunityUserInfo = new O2oCommunityUserInfo();
                Page<O2oCommunityUserInfo> o2oCommunityUserInfoPage = new Page<>();
                o2oCommunityUserInfoPage.setPageSize(pageSizeUser);
                o2oCommunityUserInfoPage.setPageNo(pageNo);
                o2oCommunityUserInfo.setPage(o2oCommunityUserInfoPage);
                o2oCommunityUserInfo.setCommunityId(o2oCommunityInfoBak.getCommunityId());
                Page<O2oCommunityUserInfo> userPage = o2oCommunityUserService.selectByCommunity(o2oCommunityUserInfo);
                responseBean.addSuccess(type, userPage);
            }
            return responseBean;
        } catch (Exception e) {
            logger.error("查询小区相关信息失败", e);
        }
        return null;
    }


    @RequestMapping("initAddPartnerInfo")
    public String initAddPartnerInfo(HttpServletRequest request, O2oCommunityPartnersInfo o2oCommunityPartnersInfo, Model model) {
        String synchronizedType = request.getParameter("synchronizedType");
        Long communityId = o2oCommunityPartnersInfo.getCommunityId();
        String eparchy = request.getParameter("eparchy");
        O2oCommunityInfoBak o2oCommunityInfoBak = o2oCommunityService.selectByPrimaryKeyBak(communityId);
        model.addAttribute("o2oCommunityPartnersInfo", o2oCommunityPartnersInfo);
        model.addAttribute("synchronizedType", synchronizedType);
        model.addAttribute("o2oCommunityInfoBak", o2oCommunityInfoBak);
        model.addAttribute("eparchy",eparchy);
        return "web/broadband/o2o/sourceMgr/partnerInfoCollect";
    }

    /**
     * 新增友商信息返回到信息收集页面
     *
     * @param request
     * @param o2oCommunityPartnersInfo
     * @return
     */
    @RequestMapping("addPartnerInfo")
    @ResponseBody
    public ResponseBean addPartnerInfo(HttpServletRequest request, O2oCommunityPartnersInfo o2oCommunityPartnersInfo, O2oCommunityInfoBak record) {
        ResponseBean responseBean = new ResponseBean();
        try {
            MemberVo memberVo = UserUtils.getLoginUser(request);
            o2oCommunityPartnersInfo.setInsertStaffId(Long.parseLong(String.valueOf(memberVo.getMemberLogin().getMemberPhone())));
            O2oCommunityInfoBak o2oCommunityInfoBak = o2oCommunityService.selectByPrimaryKey(o2oCommunityPartnersInfo.getCommunityId());
            if (o2oCommunityInfoBak == null) {
                //从CRM侧查询得到的小区，先在电渠侧保存
                record.setInsertStaffId(String.valueOf(memberVo.getMemberLogin().getMemberPhone()));
                o2oCommunityService.synchronizedCRM(record);
            }
            o2oCommunityPartnersService.insert(o2oCommunityPartnersInfo);
            responseBean.addSuccess("添加友商信息成功");
        } catch (Exception e) {
            responseBean.addError("添加友商信息失败");
            logger.error("添加友商失败", e);
        }
        logger.debug("添加友商信息成功");
        return responseBean;
    }

    @RequestMapping("initAddUserInfo")
    public String initAddUserInfo(HttpServletRequest request,O2oCommunityUserInfo o2oCommunityUserInfo, Model model) {
        Long communityId = o2oCommunityUserInfo.getCommunityId();
        String synchronizedType = request.getParameter("synchronizedType");
        String eparchy = request.getParameter("eparchy");
        O2oCommunityInfoBak o2oCommunityInfoBak = o2oCommunityService.selectByPrimaryKeyBak(communityId);
        model.addAttribute("o2oCommunityUserInfo", o2oCommunityUserInfo);
        model.addAttribute("o2oCommunityInfoBak", o2oCommunityInfoBak);
        model.addAttribute("synchronizedType", synchronizedType);
        model.addAttribute("eparchy",eparchy);
        return "web/broadband/o2o/sourceMgr/userinfoCollect";
    }


    @RequestMapping("addUserInfo")
    @ResponseBody
    public ResponseBean addUserInfo(HttpServletRequest request, O2oCommunityUserInfo o2oCommunityUserInfo, O2oCommunityInfoBak record) {
        ResponseBean responseBean = new ResponseBean();
        try {
            MemberVo memberVo = UserUtils.getLoginUser(request);
            o2oCommunityUserInfo.setInsertStaffId(Long.parseLong(String.valueOf(memberVo.getMemberLogin().getMemberPhone())));
            O2oCommunityInfoBak o2oCommunityInfoBak = o2oCommunityService.selectByPrimaryKey(o2oCommunityUserInfo.getCommunityId());
            CommunityUserCondition condition = new CommunityUserCondition();
            condition.setAddrId(record.getCommunityCode());
            condition.setSerialNumber(o2oCommunityUserInfo.getSerialNumber());
            condition.setCustName(o2oCommunityUserInfo.getUserName());
            condition.setPsptId(o2oCommunityUserInfo.getCardId());
            condition.setZoneName(o2oCommunityUserInfo.getCommunityName());
            condition.setSubType(o2oCommunityUserInfo.getBroadbandProvider());
            condition.setFee(String.valueOf(o2oCommunityUserInfo.getBroadbandPrice()));
            condition.setArup(o2oCommunityUserInfo.getUserType());
            condition.setOperator(memberVo.getMemberLogin().getMemberLogingName());
            condition.setDiscntName(o2oCommunityUserInfo.getBroadbandPackage());
            condition.setPlatName(o2oCommunityUserInfo.getFamilyAddedService());
            condition.setExpireTime(DateFormatUtil.dateFormat(o2oCommunityUserInfo.getEndTime(),"yyyy-MM-dd"));
            condition.setUpdateTime(DateFormatUtil.dateFormat(new Date(),"yyyy-MM-dd"));
            condition.setEparchyCode(record.getEparchy());
            condition.setRootEparchyCode(record.getEparchy());
//            condition.setStaffId("ABOSS_68");
//            condition.setTradeDepartPassword("ai1234");
            Map resultMap = qryAddressService.synchronizedCommunityUserInformation(condition);
            if("0".equals(resultMap.get("respCode"))){
                if (o2oCommunityInfoBak == null) {
                    //从CRM侧查询得到的小区，先在电渠侧保存
                    record.setInsertStaffId(String.valueOf(memberVo.getMemberLogin().getMemberPhone()));
                    o2oCommunityService.synchronizedCRM(record);
                }
                o2oCommunityUserService.insert(o2oCommunityUserInfo);
                responseBean.addSuccess("添加用户信息成功");
            }else{
                responseBean.addError(String.valueOf(resultMap.get("respDesc")));
            }
        } catch (Exception e) {
            responseBean.addError("添加用户信息失败");
            logger.error("增加用户信息失败", e);
        }
        return responseBean;
    }

    @RequestMapping("myInfo")
    public String myInfo(HttpServletRequest request, HttpServletResponse response, Model model,
                         O2oCommunityUserInfo o2oCommunityUserInfo) {
        MemberVo memberVo = UserUtils.getLoginUser(request);
        Long insertStaffId =memberVo.getMemberLogin().getMemberPhone();
        try {
            List<O2oCommunityInfoBak> list = o2oCommunityService.selectMyInfo(String.valueOf(insertStaffId));
            model.addAttribute("myInfos", list);
        } catch (Exception e) {
            logger.error("增加用户信息失败", e);
        }
        return "web/broadband/o2o/sourceMgr/myInfos";
    }

    @RequestMapping(value = "ajaxImageUpload", method = RequestMethod.POST)
    public Object ajaxImageUpload(
            DefaultMultipartHttpServletRequest multipartRequest,
            HttpServletRequest request, HttpServletResponse response,
            Model model) throws IOException {
        List<Map<String, String>> resultList = new ArrayList<>();
        Iterator<String> iterator = multipartRequest.getFileNames();
        String filedName = "";
        while (iterator.hasNext()) {
            filedName = iterator.next();
            List<MultipartFile> multipartFiles = multipartRequest.getFiles(filedName);
            for (MultipartFile multipartFile : multipartFiles) {
                Map<String, String> map = new HashMap<>();
                try {
                    String fileName = multipartFile.getOriginalFilename();
                    int index = fileName.lastIndexOf(".");
                    String tfsSuffix = index <= 0 ? "" : fileName.substring(index);// 文件扩展名
                    String newName = TFSClient.uploadFile(multipartFile.getInputStream(), tfsSuffix, null);// 上传文件到tfs系统
                    if (newName != null && !"".equals(newName) && !newName.startsWith("null.")) {
                        logger.info("文件上传成功:原文件名:" + fileName + ",新文件名:" + newName);
                        map.put("orgFileName", fileName);
                        map.put("fileName", newName);
                        map.put("filePath", "/res/img/" + newName);
                        map.put("flag", "y");
                    } else {
                        map.put("flag", "n");
                        map.put("info", "文件系统上传文件失败!");
                    }
                } catch (IOException e1) {
                    map.put("flag", "n");
                    map.put("info", "系统出错,上传文件失败!");
                    logger.error("图片上传出错：" + e1.toString());
                }
                resultList.add(map);
            }
        }
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.print(JSONArray.toJSONString(resultList).toString());
        out.flush();
        out.close();
        return null;
    }

}
