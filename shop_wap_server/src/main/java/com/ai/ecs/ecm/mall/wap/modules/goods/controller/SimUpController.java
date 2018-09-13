package com.ai.ecs.ecm.mall.wap.modules.goods.controller;

import com.ai.ecs.common.ResponseBean;
import com.ai.ecs.common.utils.DateUtils;
import com.ai.ecs.ecm.mall.wap.modules.BaseController;
import com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils;
import com.ai.ecs.ecsite.modules.dobusiness.entity.BkhtRecvfeeCondition;
import com.ai.ecs.ecsite.modules.dobusiness.entity.HqGetUserAllDiscntCondition;
import com.ai.ecs.ecsite.modules.dobusiness.service.FlowServeService;
import com.ai.ecs.ecsite.modules.feeBalanceQuery.entity.FeeBalanceQueryCondition;
import com.ai.ecs.ecsite.modules.feeBalanceQuery.service.FeeBalanceQueryService;
import com.ai.ecs.ecsite.modules.myMobile.entity.BasicInfoCondition;
import com.ai.ecs.ecsite.modules.myMobile.service.BasicInfoQryModifyService;
import com.ai.ecs.ecsite.modules.offerBalance.service.OfferBalanceService;
import com.ai.ecs.member.entity.MemberVo;
import com.ai.ecs.order.api.busi.ISimBusiService;
import com.ai.ecs.order.entity.recmd.*;
import com.ai.ecs.utils.LogUtils;
import com.ai.iis.upp.util.UppCore;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
/**
 * H5大王卡升级
 */
@Controller
@RequestMapping("simUp")
public class SimUpController extends BaseController {
    @Autowired
    private OfferBalanceService offerBalanceService;
    @Autowired
    private FeeBalanceQueryService feeBalanceQueryService;
    @Autowired
    private FlowServeService flowServeService;
    @Autowired
    private ISimBusiService iSimBusiService;
    @Autowired
    private BasicInfoQryModifyService basicInfoQryModifyService;


    @RequestMapping("/toUpgradePackage")
    @ResponseBody
    public Object upgradePackage(HttpServletRequest request) throws Exception {
        MemberVo member = UserUtils.getLoginUser(request);
        ResponseBean resp = new ResponseBean();
        if (member != null && member.getMemberLogin().getMemberTypeId() == 2) {
            //查询用户已定套餐
            String serialNumber = String.valueOf(member.getMemberLogin().getMemberLogingName());
            Map DiscntMap = queryAllDiscnt(serialNumber);
             if(isOpenDwSim(DiscntMap)){//判断是否办理了大王卡
                 String runId = (String.valueOf(request.getAttribute("runningId")));
                 Map restFeeMap = queryRestFee(serialNumber,runId);
                 if( isEnoughFee(restFeeMap)){//判断余额是否大于等于200元
                     resp.addSuccess();//表示成功通过办理前的校验
                 }else{
                     resp.addError("-2");//表示余额不足200元
                 }
              }else{
                  resp.addError("-1");//表示未办理大王卡业务
             }

        }else{
            resp.addError("-4");//表示未用手机号码登录
        }
        return resp;
    }

    /**
     * 确认办理，调接口
     * @param request
     * @return
     * @throws Exception
     * 2017-03-31下线
     */
    //@RequestMapping("/confirmUpgradePackage")
    //@ResponseBody
    public Object confirmUpgradePackage(HttpServletRequest request) throws Exception {
        MemberVo member = UserUtils.getLoginUser(request);
        ResponseBean resp = new ResponseBean();
        if (member != null && member.getMemberLogin().getMemberTypeId() == 2) {
            Map<String, Object> tmpDataMap = new HashMap<String, Object>();
            String serialNumber = String.valueOf(member.getMemberLogin().getMemberLogingName());
            String runId = (String.valueOf(request.getAttribute("runningId")));
            String epachyCode = getEpachyCode(serialNumber);
            try {

                Map DiscntMap = queryAllDiscnt(serialNumber);
                if(isdoDwSim(DiscntMap)){
                    resp.addError("-5");
                }else {
                    Map restFeeMap = queryRestFee(serialNumber, runId);

                    if (isOpenDwSim(DiscntMap) && isEnoughFee(restFeeMap)) {//判断是否办理了大王卡
                        tmpDataMap = doUpgradePackage(serialNumber, epachyCode);
                    }
                    if (!MapUtils.isEmpty(tmpDataMap) && "0".equals(tmpDataMap.get("respCode"))) {
                        resp.addSuccess();
                    } else {
                        resp.addError((String) tmpDataMap.get("respDesc"));
                    }
                }
            } catch (Exception e) {
                LogUtils.printKaError("confirmUpgradePackage:","大王卡升级失败"+serialNumber,e); ;
            } finally {
                try {
                    TfOrderBusiness tfOrderBusiness = new TfOrderBusiness();
                    tfOrderBusiness.setSerialNumber(serialNumber);
                    tfOrderBusiness.setEparchyCode(epachyCode);
                    tfOrderBusiness.setPackageId("99960163");
                    tfOrderBusiness.setProductId("99813036");
                    tfOrderBusiness.setDiscountCode("99543856");
                    tfOrderBusiness.setResultCode(tmpDataMap.get("respCode") + "");
                    tfOrderBusiness.setResultInfo(tmpDataMap.get("respDesc") + "");
                    iSimBusiService.saveOrderBusiness(tfOrderBusiness);
                } catch (Exception e) {
                    LogUtils.printKaError("tfOrderBusiness数据入库异常:" + serialNumber, e);
                }
            }
        } else {
            resp.addError("-3");//办理失败
        }
        return resp;
    }



    //判断是否开通了大王卡
    private boolean isOpenDwSim(Map discntMap) {
        boolean Flag=false;
        if (MapUtils.isNotEmpty(discntMap) && "0".equals(discntMap.get("respCode"))) {
            JSONArray allDiscntList = JSONArray.parseArray(discntMap.get("result").toString());
            for (int j = 0, discntlen = allDiscntList.size(); j < discntlen; j++) {
                Map<String, Object> discnt = allDiscntList.getJSONObject(j);
                logger.info("用户的主套餐为:" + discnt.get("DISCNT_CODE").toString());
                if (discnt.get("DISCNT_CODE").toString().substring(2).equals("541688") || discnt.get("DISCNT_CODE").toString().substring(2).equals("543333")) {
                    logger.info("开通大王卡升级，用户的主套餐为:" + discnt.get("DISCNT_CODE").toString());
                    Flag = true;
                }
            }
        }
        return  Flag;
    }
    private boolean isdoDwSim(Map discntMap) {
        boolean Flag=false;
        if (MapUtils.isNotEmpty(discntMap) && "0".equals(discntMap.get("respCode"))) {
            JSONArray allDiscntList = JSONArray.parseArray(discntMap.get("result").toString());
            for (int j = 0, discntlen = allDiscntList.size(); j < discntlen; j++) {
                Map<String, Object> discnt = allDiscntList.getJSONObject(j);
                logger.info("用户的主套餐为:" + discnt.get("DISCNT_CODE").toString());
                if (discnt.get("DISCNT_CODE").toString().equals("99543856") ) {
                    logger.info("开通大王卡升级，用户的主套餐为:" + discnt.get("DISCNT_CODE").toString());
                    Flag = true;
                }
            }
        }
        return  Flag;
    }

    //判断用户余额是否大于等于200元
    private boolean isEnoughFee(Map restFeeMap) {
        boolean flag = false;
        if (MapUtils.isNotEmpty(restFeeMap) && "0".equals(restFeeMap.get("respCode"))) {
            Map feeData = (Map) ((List) restFeeMap.get("result")).get(0);
            Object test = feeData.get("NEW_ALLLEAVE_FEE");
            if (test != null) {
                Long restFee = Long.parseLong(test.toString())/100L;
                logger.info("==cxq用户余额==>" + restFee);
                if (restFee >= 200L) {
                    return flag = true;
                }
            }
        }
        return flag;
    }

    private Map queryAllDiscnt(String serialNumber) throws Exception{
        HqGetUserAllDiscntCondition disCondition = new HqGetUserAllDiscntCondition();
        disCondition.setSerialNumber(serialNumber);
        disCondition.setxGetmode(3);
        disCondition.setRemoveTag("0");
        Map resultOfferMap = offerBalanceService.queryOffer(disCondition);
        logger.info("==cxq套餐查询请求信息==>" + JSONObject.toJSONString(disCondition, SerializerFeature.WriteMapNullValue));
        logger.info("==cxq套餐查询返回信息==>" + JSONObject.toJSONString(resultOfferMap, SerializerFeature.WriteMapNullValue));
        return resultOfferMap;
    }
    private Map queryRestFee(String serialNumber, String runId) throws Exception{
        FeeBalanceQueryCondition feeCondition = new FeeBalanceQueryCondition();
        feeCondition.setSerialNumber(serialNumber);
        feeCondition.setRunningId(runId);
        Map resultFeeMap = feeBalanceQueryService.queryBalance(feeCondition);
        logger.info("==cxq余额查询请求信息==>" + JSONObject.toJSONString(feeCondition, SerializerFeature.WriteMapNullValue));
        logger.info("==cxq余额查询返回信息==>" + JSONObject.toJSONString(resultFeeMap, SerializerFeature.WriteMapNullValue));
        return resultFeeMap;
    }
    /**
     * 升级大王卡套餐
     *
     * @param serialNumber
     * @return
     */
    private Map<String,Object> doUpgradePackage( String serialNumber,String epachyCode) throws Exception{

        BkhtRecvfeeCondition bkhtRecvfeeCondition = new BkhtRecvfeeCondition();
        bkhtRecvfeeCondition.setACCT_ID("");
        bkhtRecvfeeCondition.setCARRIER_CODE(0);
        bkhtRecvfeeCondition.setCARRIER_ID("");
        bkhtRecvfeeCondition.setCHANNEL_ACCEPT_TIME(DateUtils.getCurrentTime());
        bkhtRecvfeeCondition.setCHANNEL_TRADE_ID(DateUtils.formatDate(new Date(), "yyyyMMddHHmmssSSS") + UppCore.genRandom(3));
        bkhtRecvfeeCondition.setCHARGE_ID("");
        bkhtRecvfeeCondition.setCHARGE_SOURCE_CODE(37);//不确定
        bkhtRecvfeeCondition.setDEPOSIT_CODE("");
        bkhtRecvfeeCondition.setPACKAGE_ID("99960163");
        bkhtRecvfeeCondition.setPRODUCT_ID("99813036");
        bkhtRecvfeeCondition.setPARA_CODE1("HF");
        bkhtRecvfeeCondition.setPARA_CODE2("");
        bkhtRecvfeeCondition.setPARA_CODE3("");
        bkhtRecvfeeCondition.setPARA_CODE4("");
        bkhtRecvfeeCondition.setPARA_CODE5("");
        bkhtRecvfeeCondition.setCHANNEL_ID("ITFWTNNN");
        bkhtRecvfeeCondition.setPAY_FEE_MODE_CODE(0);
        bkhtRecvfeeCondition.setRECOVER_TAG("0");
        bkhtRecvfeeCondition.setSERIAL_NUMBER(serialNumber);
        bkhtRecvfeeCondition.setREMOVE_TAG("0");
        bkhtRecvfeeCondition.setWRITEOFF_MODE("0");
        bkhtRecvfeeCondition.setX_FPAY_FEE("0");
        bkhtRecvfeeCondition.setTRADE_TYPE_CODE("240");
        bkhtRecvfeeCondition.setEPARCHY_CODE(epachyCode);
        bkhtRecvfeeCondition.setStaffId("ITFWTNNN");
        bkhtRecvfeeCondition.setTradeDepartPassword("909880");
        Map<String, Object> resultMap = flowServeService.MGTJ4Handle(bkhtRecvfeeCondition);
        logger.info("==cxq升级套餐请求信息==>" + JSONObject.toJSONString(bkhtRecvfeeCondition, SerializerFeature.WriteMapNullValue));
        logger.info("==cxq升级套餐返回信息==>" + JSONObject.toJSONString(resultMap, SerializerFeature.WriteMapNullValue));
       return resultMap;
    }
    /**
     * 获取地市编码
     */
    private String getEpachyCode(String serialNumber) throws Exception{
        String epachyCode= "";
        BasicInfoCondition basicInfoCondition = new BasicInfoCondition();
        basicInfoCondition.setSerialNumber(serialNumber);
        basicInfoCondition.setxGetMode("0");
        basicInfoCondition.setStaffId("ITFWTNNN");
        basicInfoCondition.setTradeDepartPassword("909880");
        Map<String,Object> returnDataMap = basicInfoQryModifyService.queryUserBasicInfo(basicInfoCondition);
        logger.info("==cxq查询用户信息请求信息==>" + JSONObject.toJSONString(basicInfoCondition, SerializerFeature.WriteMapNullValue));
        logger.info("==cxq查询用户信息返回信息==>" + JSONObject.toJSONString(returnDataMap, SerializerFeature.WriteMapNullValue));
        if (MapUtils.isNotEmpty(returnDataMap) && "0".equals(returnDataMap.get("respCode"))) {
            List<Map<String,Object>> returnData = (List<Map<String,Object>>)returnDataMap.get("result");
            epachyCode = String.valueOf(returnData.get(0).get("EPARCHY_CODE"));

        }else{
            throw new Exception("没有查询到该手机号的用户信息!"+returnDataMap.get("respDesc"));
        }
            return epachyCode;
    }

}
