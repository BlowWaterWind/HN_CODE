package com.ai.ecs.ecm.mall.wap.modules.o2o;

import com.ai.ecs.common.utils.TFSClient;
import com.ai.ecs.ecm.mall.wap.modules.BaseController;
import com.ai.ecs.ecm.mall.wap.modules.goods.constant.BroadbandConstants;
import com.ai.ecs.ecm.mall.wap.platform.utils.StringUtils;
import com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils;
import com.ai.ecs.entity.base.Page;
import com.ai.ecs.member.entity.ChannelInfo;
import com.ai.ecs.member.entity.MemberVo;
import com.ai.ecs.o2o.api.O2oAddrSearchService;
import com.ai.ecs.o2o.api.O2oBusiCollectService;
import com.ai.ecs.o2o.entity.*;
import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * Created by Administrator on 2018/2/26.
 */
@Controller
@RequestMapping("o2oBusiCollect")
public class O2oBusiCollectController extends BaseController {


    @Autowired
    private O2oAddrSearchService o2oAddrSearchService;

    @Autowired
    private O2oBusiCollectService o2oBusiCollectService;

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
    public String search(HttpServletRequest request, HttpServletResponse response, Model model,String type) {
        MemberVo memberVo = UserUtils.getLoginUser(request);
        try {
            //1：小区搜索；2：商机搜索；3：营销知识库 4:小区名...
            Map topType = Maps.newHashMap();
            Map hisRecord = Maps.newHashMap();
            topType.put("SEARCH_TYPE", type);
            hisRecord.put("SERIAL_NUMBER", String.valueOf(memberVo.getMemberLogin().getMemberPhone()));
            hisRecord.put("SEARCH_TYPE", type);
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
            model.addAttribute("type",type);
            if("5".equals(type)){
                return "web/broadband/o2o/businessCollect/searchIndexCoo";
            }else if("6".equals(type)){
                return "web/broadband/o2o/businessCollect/searchIndexComu";
            }
        } catch (Exception e) {
            logger.error("查询热门搜索和历史搜索失败", e);
        }
        return "web/broadband/o2o/businessCollect/searchIndex";
    }

    @RequestMapping("searchVillage")
    public String searchVillage(HttpServletRequest request, HttpServletResponse response, Model model) {
        MemberVo memberVo = UserUtils.getLoginUser(request);
        try {
            String type="7";
            Map topType = Maps.newHashMap();
            Map hisRecord = Maps.newHashMap();
            topType.put("SEARCH_TYPE", type);
            hisRecord.put("SERIAL_NUMBER", String.valueOf(memberVo.getMemberLogin().getMemberPhone()));
            hisRecord.put("SEARCH_TYPE", type);
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
            model.addAttribute("type",type);
            O2oOrg o2oOrg=new O2oOrg();
            o2oOrg.setParentId(0L);
            List orgList =o2oBusiCollectService.getOrgListByParentId(o2oOrg);
            model.addAttribute("orgList",orgList);
        } catch (Exception e) {
            logger.error("查询热门搜索和历史搜索失败", e);
        }
        return "web/broadband/o2o/businessCollect/searchIndexVillage";
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
    public Integer deleteRecord(HttpServletRequest request,String type) {
        Map<String, Object> map = new HashMap<>();
        try {
            MemberVo memberVo = UserUtils.getLoginUser(request);
            map.put("SEARCH_TYPE", type);
            map.put("SERIAL_NUMBER", String.valueOf(memberVo.getMemberLogin().getMemberPhone()));
            return o2oAddrSearchService.deleteByPhone(map);
        } catch (Exception e) {
            logger.error("删除历史记录失败", e);
        }
        return 0;
    }

    @RequestMapping("myCooResourceList")
    public String myCooResourceList(HttpServletRequest request, HttpServletResponse response, Model model) {
//        MemberVo memberVo = UserUtils.getLoginUser(request);
//        O2oCooResource resource=new O2oCooResource();
//        resource.setMemberId(memberVo.getMemberLogin().getMemberId());
//        List<O2oCooResource> list = o2oBusiCollectService.getCooResourceList(resource);
//        model.addAttribute("myCooResourceList", list);
        return "web/broadband/o2o/businessCollect/cooResourceList";
    }

    @RequestMapping("myCompetitionList")
    public String myCompetitionList(HttpServletRequest request, HttpServletResponse response, Model model) {
//        MemberVo memberVo = UserUtils.getLoginUser(request);
//        O2oCommunityCompetition competition=new O2oCommunityCompetition();
//        competition.setMemberId(memberVo.getMemberLogin().getMemberId());
//        List<O2oCommunityCompetition> list=o2oBusiCollectService.getComuCompetitionList(competition);
//        model.addAttribute("myComuCompetitionList", list);
        return "web/broadband/o2o/businessCollect/comuCompetitionList";
    }

    @RequestMapping("myVillageReserveList")
    public String myVillageReserveList(HttpServletRequest request, HttpServletResponse response, O2oVillageReserve o2oVillageReserve,Model model) {
//        MemberVo memberVo = UserUtils.getLoginUser(request);
//        if(o2oVillageReserve!=null){
//            o2oVillageReserve.setMemberId(memberVo.getMemberLogin().getMemberId());
//        }
//        List villageList =o2oBusiCollectService.getVillageList(o2oVillageReserve);
//        model.addAttribute("villageReserveList", villageList);
        return "web/broadband/o2o/businessCollect/villageReserveList";
    }

    @RequestMapping("initAddCooResource")
    public String initAddCooResource(HttpServletRequest request, O2oCooResource resource, Model model,O2oAddrSearchRecord o2oAddrSearchRecord,String query) {
        try {
            MemberVo memberVo = UserUtils.getLoginUser(request);
            model.addAttribute("query",query);
            if(resource!=null &&resource.getRecordId()!=null){
                resource=o2oBusiCollectService.selectCooResourceById(resource.getRecordId());
                resource.setIsNew("1");
            }else{
                resource.setIsNew("0");
            }
            if(o2oAddrSearchRecord!=null&&StringUtils.isNotBlank(o2oAddrSearchRecord.getSearchString())){
                o2oAddrSearchRecord.setSerialNumber(String.valueOf(memberVo.getMemberLogin().getMemberPhone()));
                o2oAddrSearchService.insert(o2oAddrSearchRecord);
            }
            model.addAttribute("o2oCooResource",resource);
        } catch (Exception e) {
            logger.error("进入有线宽带“建营装维”第三方合作资源页失败", e);
        }
        return "web/broadband/o2o/businessCollect/addCooResource";
    }

    @RequestMapping("initAddComuCompetition")
    public String initAddComuCompetition(HttpServletRequest request, O2oCommunityCompetition communityCompetition, Model model, O2oAddrSearchRecord o2oAddrSearchRecord,String query) {
        try {
            MemberVo memberVo = UserUtils.getLoginUser(request);
            model.addAttribute("query",query);
            if(communityCompetition!=null &&communityCompetition.getRecordId()!=null){
                communityCompetition=o2oBusiCollectService.selectComuCompetitionById(communityCompetition.getRecordId());
                communityCompetition.setIsNew("1");
                if(StringUtils.isNotBlank(communityCompetition.getImagesUrl())){
                    communityCompetition.setImageUrls(Arrays.asList(communityCompetition.getImagesUrl().split(";")));
                }
            }else{
                communityCompetition.setIsNew("0");
            }
            if(StringUtils.isBlank(communityCompetition.getCommunityId())){
                model.addAttribute("flag","0");
            }
            if(o2oAddrSearchRecord!=null&&StringUtils.isNotBlank(o2oAddrSearchRecord.getSearchString())){
                o2oAddrSearchRecord.setSerialNumber(String.valueOf(memberVo.getMemberLogin().getMemberPhone()));
                o2oAddrSearchService.insert(o2oAddrSearchRecord);
            }
            model.addAttribute("communityCompetition",communityCompetition);
        } catch (Exception e) {
            logger.error("进入小区竞争录入页失败", e);
        }
        return "web/broadband/o2o/businessCollect/addComuCompetition";
    }

    @RequestMapping("initAddvillageReverse")
    public String initAddvillageReverse(HttpServletRequest request, O2oVillageReserve o2oVillageReserve, Model model, O2oAddrSearchRecord o2oAddrSearchRecord, String query) {
        try {
            MemberVo memberVo = UserUtils.getLoginUser(request);
            model.addAttribute("query",query);
            O2oOrg o2oOrg=new O2oOrg();
            o2oOrg.setParentId(0L);
            List orgList =o2oBusiCollectService.getOrgListByParentId(o2oOrg);
            model.addAttribute("orgList",orgList);
            if(o2oVillageReserve!=null &&o2oVillageReserve.getRecordId()!=null){
                o2oVillageReserve=o2oBusiCollectService.selectVillageById(o2oVillageReserve.getRecordId());
                o2oVillageReserve.setIsNew("1");
            }else{
                o2oVillageReserve.setIsNew("0");
                o2oVillageReserve.setRecordType("0");
            }
            if(o2oAddrSearchRecord!=null&&StringUtils.isNotBlank(o2oAddrSearchRecord.getSearchString())){
                o2oAddrSearchRecord.setSerialNumber(String.valueOf(memberVo.getMemberLogin().getMemberPhone()));
                o2oAddrSearchService.insert(o2oAddrSearchRecord);
            }
            model.addAttribute("o2oVillageReserve"+o2oVillageReserve.getRecordType(),o2oVillageReserve);
            model.addAttribute("type",o2oVillageReserve.getRecordType());
        } catch (Exception e) {
            logger.error("进入农村预约营销录入页失败", e);
        }
        return "web/broadband/o2o/businessCollect/addVillageReserve";
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
//                        map.put("filePath", "http://10.13.4.15:7500/v1/tfs/" + newName);
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


    @RequestMapping("addCooResource")
    @ResponseBody
    public Map addCooResource(HttpServletRequest request, O2oCooResource resource, Model model) {
        Map<String,Object>  resultMap=new HashMap<>();
        try {
            resultMap.put(BroadbandConstants.RESPONSE_CODE,"-1");
            resultMap.put(BroadbandConstants.RESPONSE_DESC,"提交失败！");
            MemberVo memberVo = UserUtils.getLoginUser(request);
            if(resource==null){
                return resultMap;
            }
            if("0".equals(resource.getIsNew())){
                Calendar cal=Calendar.getInstance();
                resource.setCreateTime(cal.getTime());
                resource.setMonth(cal.get(Calendar.MONTH)+1);
                resource.setMemberId(memberVo.getMemberLogin().getMemberId());
                resource.setSerialNumber(memberVo.getMemberLogin().getMemberPhone());
                resource.setIsExist("1");
                o2oBusiCollectService.addCooResource(resource);
            }else{
                resource.setModifyTime(new Date());
                o2oBusiCollectService.updateCooResource(resource);
            }
            resultMap.put(BroadbandConstants.RESPONSE_CODE,"0");
            resultMap.put(BroadbandConstants.RESPONSE_DESC,"提交成功！");
        } catch (Exception e) {
            resultMap.put(BroadbandConstants.RESPONSE_DESC,"系统异常！");
            logger.error("有线宽带“建营装维”第三方合作资源录入失败", e);
        }
        return resultMap;
    }

    @RequestMapping("addCommuCompetition")
    @ResponseBody
    public Map addComuCompetition(HttpServletRequest request, O2oCommunityCompetition o2oCommunityCompetition, Model model) {
        Map<String,Object>  resultMap=new HashMap<>();
        try {
            resultMap.put(BroadbandConstants.RESPONSE_CODE,"-1");
            resultMap.put(BroadbandConstants.RESPONSE_DESC,"提交失败！");
            MemberVo memberVo = UserUtils.getLoginUser(request);
            if(o2oCommunityCompetition==null){
                return resultMap;
            }
            if(o2oCommunityCompetition.getImageUrls()!=null && o2oCommunityCompetition.getImageUrls().size()>0){
                String str = StringUtils.join(o2oCommunityCompetition.getImageUrls().toArray(), ";");
                o2oCommunityCompetition.setImagesUrl(str);
            }
            if("0".equals(o2oCommunityCompetition.getIsNew())){
                Calendar cal=Calendar.getInstance();
                o2oCommunityCompetition.setCreateTime(cal.getTime());
                o2oCommunityCompetition.setMemberId(memberVo.getMemberLogin().getMemberId());
                o2oCommunityCompetition.setSerialNumber(memberVo.getMemberLogin().getMemberPhone());
                ChannelInfo channelInfo=memberVo.getChannelInfo();
                o2oCommunityCompetition.setRecordStaff(channelInfo.getTradeStaffId());
                o2oBusiCollectService.addComuCompetition(o2oCommunityCompetition);
            }else{
                o2oCommunityCompetition.setModifyTime(new Date());
                o2oBusiCollectService.updateComuCompetition(o2oCommunityCompetition);
            }
            resultMap.put(BroadbandConstants.RESPONSE_CODE,"0");
            resultMap.put(BroadbandConstants.RESPONSE_DESC,"提交成功！");
        } catch (Exception e) {
            resultMap.put(BroadbandConstants.RESPONSE_DESC,"系统异常！");
            logger.error("有线宽带“建营装维”第三方合作资源录入失败", e);
        }
        return resultMap;
    }

    @RequestMapping("addVillageReverse")
    @ResponseBody
    public Map addVillageReverse(HttpServletRequest request, O2oVillageReserve o2oVillageReserve, Model model) {
        Map<String,Object>  resultMap=new HashMap<>();
        try {
            resultMap.put(BroadbandConstants.RESPONSE_CODE,"-1");
            resultMap.put(BroadbandConstants.RESPONSE_DESC,"提交失败！");
            MemberVo memberVo = UserUtils.getLoginUser(request);
            if(o2oVillageReserve==null){
                return resultMap;
            }
            if("0".equals(o2oVillageReserve.getIsNew()) || StringUtils.isBlank(o2oVillageReserve.getIsNew())){
                Calendar cal=Calendar.getInstance();
                o2oVillageReserve.setCreateTime(cal.getTime());
                o2oVillageReserve.setMemberId(memberVo.getMemberLogin().getMemberId());
                o2oVillageReserve.setSerialNumber(memberVo.getMemberLogin().getMemberPhone());
                o2oBusiCollectService.addVillageReverse(o2oVillageReserve);
            }else{
                o2oVillageReserve.setModifyTime(new Date());
                o2oBusiCollectService.updateVillage(o2oVillageReserve);
            }
            resultMap.put(BroadbandConstants.RESPONSE_CODE,"0");
            resultMap.put(BroadbandConstants.RESPONSE_DESC,"提交成功！");
        } catch (Exception e) {
            resultMap.put(BroadbandConstants.RESPONSE_DESC,"系统异常！");
            logger.error("有线宽带“建营装维”第三方合作资源录入失败", e);
        }
        return resultMap;
    }

    @RequestMapping("qryOrgByParentId")
    @ResponseBody
    public Map qryOrgByParentId(HttpServletRequest request, O2oOrg o2oOrg) {
        Map<String,Object>  resultMap=new HashMap<>();
        try {
            MemberVo memberVo = UserUtils.getLoginUser(request);
            List orgList =o2oBusiCollectService.getOrgListByParentId(o2oOrg);
            resultMap.put("data",orgList);
            resultMap.put(BroadbandConstants.RESPONSE_CODE,"0");
            resultMap.put(BroadbandConstants.RESPONSE_DESC,"ok");
        } catch (Exception e) {
            resultMap.put(BroadbandConstants.RESPONSE_DESC,"系统异常！");
            resultMap.put(BroadbandConstants.RESPONSE_CODE,"-1");
            logger.error("有线宽带“建营装维”第三方合作资源录入失败", e);
        }
        return resultMap;
    }

    @RequestMapping("qryVillageList")
    @ResponseBody
    public Map qryVillageList(HttpServletRequest request, O2oVillageReserve o2oVillageReserve) {
        Map<String,Object>  resultMap=new HashMap<>();
        try {
            MemberVo memberVo = UserUtils.getLoginUser(request);
            if(o2oVillageReserve!=null){
                o2oVillageReserve.setMemberId(memberVo.getMemberLogin().getMemberId());
                o2oVillageReserve.setCountyName(java.net.URLDecoder.decode(o2oVillageReserve.getCountyName(),"UTF-8"));
                o2oVillageReserve.setEparchyName(java.net.URLDecoder.decode(o2oVillageReserve.getEparchyName(),"UTF-8"));
            }
            List villageList =o2oBusiCollectService.getVillageList(o2oVillageReserve);
            resultMap.put("data",villageList);
            resultMap.put(BroadbandConstants.RESPONSE_CODE,"0");
            resultMap.put(BroadbandConstants.RESPONSE_DESC,"ok");
        } catch (Exception e) {
            resultMap.put(BroadbandConstants.RESPONSE_DESC,"系统异常！");
            resultMap.put(BroadbandConstants.RESPONSE_CODE,"-1");
            logger.error("有线宽带“建营装维”第三方合作资源录入失败", e);
        }
        return resultMap;
    }


    @ResponseBody
    @RequestMapping("/queryVillageListByPage")
    public Page<O2oVillageReserve> queryVillageListByPage(HttpServletRequest request, HttpServletResponse response,O2oVillageReserve o2oVillageReserve) {
        MemberVo memberVo = UserUtils.getLoginUser(request);
        if(o2oVillageReserve!=null){
            o2oVillageReserve.setMemberId(memberVo.getMemberLogin().getMemberId());
        }
        Page<O2oVillageReserve> page = o2oBusiCollectService.findVillagePage(new Page<O2oVillageReserve>(request, response), o2oVillageReserve);
        return page;
    }

    @ResponseBody
    @RequestMapping("/queryComputitionListByPage")
    public Page<O2oCommunityCompetition> queryComputitionListByPage(HttpServletRequest request, HttpServletResponse response,O2oCommunityCompetition o2oCommunityCompetition) {
        MemberVo memberVo = UserUtils.getLoginUser(request);
        if(o2oCommunityCompetition!=null){
            o2oCommunityCompetition.setMemberId(memberVo.getMemberLogin().getMemberId());
        }
        Page<O2oCommunityCompetition> page = o2oBusiCollectService.findCompetitionPage(new Page<O2oCommunityCompetition>(request, response), o2oCommunityCompetition);
        return page;
    }

    @ResponseBody
    @RequestMapping("/queryCooResourceListByPage")
    public Page<O2oCooResource> queryCooResourceListByPage(HttpServletRequest request, HttpServletResponse response,O2oCooResource o2oCooResource) {
        MemberVo memberVo = UserUtils.getLoginUser(request);
        if(o2oCooResource!=null){
            o2oCooResource.setMemberId(memberVo.getMemberLogin().getMemberId());
        }
        Page<O2oCooResource> page = o2oBusiCollectService.findCooResourcePage(new Page<O2oCooResource>(request, response), o2oCooResource);
        return page;
    }
}
