package com.ai.ecs.ecm.mall.wap.modules.goods.controller;

import com.ai.ecs.common.ResponseBean;
import com.ai.ecs.common.utils.Base64;
//import com.ai.ecs.components.api.IOcrService;
import com.ai.ecs.ecm.mall.wap.platform.utils.DictUtil;
import com.ai.ecs.ecm.mall.wap.platform.utils.TriDes;
import com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils;
import com.ai.ecs.ecsite.modules.o2osim.entity.SimCardCheckCondition;
import com.ai.ecs.ecsite.modules.o2osim.service.O2oSimService;
import com.ai.ecs.ecsite.modules.sim.entity.QuerySimMainDisCondition;
import com.ai.ecs.ecsmc.common.util.StringUtils;
import com.ai.ecs.entity.base.ConstantsBase;
import com.ai.ecs.exception.EcsException;
import com.ai.ecs.goods.entity.TfH5SimConf;
import com.ai.ecs.goods.entity.TfPlans;
import com.ai.ecs.member.entity.MemberVo;
import com.ai.ecs.o2o.api.O2oPreOrderService;
import com.ai.ecs.o2o.entity.O2oPreOrder;
import com.ai.ecs.order.constant.OrderConstant;
import com.ai.ecs.order.entity.TfOrderDetailSim;
import com.ai.ecs.order.entity.recmd.TfOrderRecmd;
import com.ai.ecs.order.entity.recmd.TfRecmdActConf;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ai.ecs.ecm.mall.wap.platform.utils.LogUtils.createStreamNo;
import static com.ai.ecs.ecm.mall.wap.platform.utils.LogUtils.processThrowableMessage;
import static com.ai.ecs.ecm.mall.wap.platform.utils.LogUtils.writerFlowLogEnterMenthod;
import static com.ai.ecs.ecm.mall.wap.platform.utils.LogUtils.writerFlowLogExitMenthod;
import static com.ai.ecs.ecm.mall.wap.platform.utils.LogUtils.writerFlowLogThrowable;

/**
 * Created by cc on 2018/1/30.
 *
 * @author xiejq
 */
@Controller
@RequestMapping("o2oSimPreBuy")
public class O2oSimPreBuyController extends RecmdDefaultController {

    @Autowired
    private O2oPreOrderService o2oPreOrderService;

//    @Autowired
//    private IOcrService ocrService;
    @Autowired
    private O2oSimService o2oSimInterfaceService;

    private static final String ORDER_CREATE = "0";

    private static final String WHITE_CARD = "18";

    private static final String OK = "1";

    /**
     * @param request
     * @param confId
     * @param planId
     * @param CHANID
     * @param model
     * @param shopId
     * @return
     */
    @RequestMapping("/toValidateSim")
    public String toValidateSim(HttpServletRequest request, String confId, String planId, String CHANID, Model model, String shopId) {
        String streamNo = createStreamNo();
        writerFlowLogEnterMenthod(streamNo, "", "", getClass().getName(), "toValidateSim", request.getParameterMap(), "实名制入参", request);
        if(StringUtils.isEmpty(confId)){
            throw new RuntimeException("请点击立即办卡进入页面");
        }
        model.addAttribute("confId", confId);
        model.addAttribute("planId", planId);
        String recmdCode = request.getParameter("reCmdCode");
        model.addAttribute("recmdCode", recmdCode);
        model.addAttribute("CHANID", CHANID);
        model.addAttribute("shopId", shopId);
        Session session = UserUtils.getSession();
        session.setAttribute("shopId", shopId);
        return "web/goods/o2osim/validateSim";
    }

    @ResponseBody
    @RequestMapping("/validateSim")
    public ResponseBean validateSim(HttpServletRequest request,String simCardNo, O2oPreOrder cond) {
        String streamNo = createStreamNo();
        writerFlowLogEnterMenthod(streamNo, "", "", getClass().getName(), "toValidateSim", request.getParameterMap(), "提交sim卡", request);
        ResponseBean responseBean = new ResponseBean();
        try {
            TfH5SimConf simConf = new TfH5SimConf();
            simConf.setConfId(cond.getConfId());
            List<TfH5SimConf> confList = plansService.queryListCond(simConf);
            SimCardCheckCondition condition = new SimCardCheckCondition();
            condition.setSimCardNo(simCardNo);
            if (OrderConstant.O2O_TYPE_XYYX.equals(confList.get(0).getExtContent2())) {
                condition.setNewHaoduanka("Y");
            }
            if(StringUtils.isNotEmpty(cond.getReCmdCode())) {
                String reCmdId = Base64.decode(cond.getReCmdCode().getBytes());
                TfOrderRecmd recmdRst = recmdMainService.getOrderRecmd(new TfOrderRecmd(Long.valueOf(reCmdId)));
                cond.setLoginName(recmdRst.getRecmdPhone());
            }
            Map map = o2oSimInterfaceService.simCardCheck(condition);
            Map<String, String> mp = new HashMap<>();
            //如果输的是白卡,则取BoSS simCardNo
            if (simCardNo.startsWith(WHITE_CARD)) {
                cond.setWhiteCardNo(simCardNo);
                simCardNo = String.valueOf(map.get("SIM_CARD_NO"));
                if (StringUtils.isEmpty(simCardNo) || "null".equals(simCardNo)) {
                    responseBean.addError("您输入的是白卡，BOSS未返回SIM卡号！");
                    return responseBean;
                }
            }
            //校园营销sim卡套餐检查
            boolean checkResult = checkDiscntCode(simCardNo, responseBean, cond, map, confList);
            if (!checkResult) {
                return responseBean;
            }
            cond.setSimEparchyCode(map.get("EPARCHY_CODE").toString());
            cond.setCityCode(map.get("CITY_CODE").toString());
            cond.setCityName(map.get("CITY_NAME").toString());
            cond.setSimCardNo(simCardNo);
            cond.setStatus(ORDER_CREATE);
            String orderPreId = o2oPreOrderService.insertPreOrder(cond);
            if (orderPreId == null) {
                responseBean.addError("-1", "预下单失败");
                return responseBean;
            }
            mp.put("orderPreId", orderPreId);
            mp.put("cardId", cond.getSimCardNo());
            responseBean.addSuccess(mp);
            return responseBean;
        } catch (Exception e) {
            writerFlowLogThrowable(streamNo, simCardNo, "", getClass().getName(),
                    "validateSim", "", "系统异常:" + processThrowableMessage(e));
            logger.error("查询号码异常", e);
            if (e instanceof EcsException) {
                EcsException e1 = (EcsException) e;
                responseBean.addError("-1", e1.getFriendlyDesc());
            } else {
                responseBean.addError("-1", "系统异常");
            }
            return responseBean;
        }
    }

    /**
     * 校园卡营销检查所选套餐与sim卡是否一致
     *
     * @param simCardNo
     * @param responseBean
     * @param cond
     * @param map
     * @return
     * @throws Exception
     */
    private boolean checkDiscntCode(String simCardNo, ResponseBean responseBean, O2oPreOrder cond, Map map, List<TfH5SimConf> confList) {
        boolean result = true;
        String streamNo = createStreamNo();
        if (OrderConstant.O2O_TYPE_XYYX.equals(confList.get(0).getExtContent2())) {
            try {
                QuerySimMainDisCondition querySimMainDisCondition = new QuerySimMainDisCondition();
                querySimMainDisCondition.setSimCardNo(simCardNo);
                Map<String, Object> resultMap = o2oSimInterfaceService.querySimMainDis(querySimMainDisCondition);

                if (resultMap != null && OK.equals(resultMap.get("RESULT_CODE"))) {
                    String discntCode = String.valueOf(resultMap.get("DISCNT_CODE"));
                    String discntName = String.valueOf(resultMap.get("DISCNT_NAME"));
                    cond.setPlanName(discntName);
                    cond.setPlanCode(discntCode);
                } else {
                    writerFlowLogExitMenthod(streamNo, "", "", getClass().getName(),
                            "checkDiscntCode", resultMap, String.format("SIM卡套餐验证失败[%s]", simCardNo));
                    responseBean.addError("-1", String.format("SIM卡套餐验证失败，[%s]", resultMap.get("RESULT_INFO")));
                    result = false;
                }
            } catch (EcsException e) {
                writerFlowLogThrowable(streamNo, "", "", getClass().getName(),
                        "checkDiscntCode", processThrowableMessage(e),
                        String.format("SIM卡套餐验证异常[%s]", simCardNo));
                responseBean.addError("-1", "SIM卡套餐验证异常，请重试！");
                result = false;
            }
        }
        return result;
    }

//    @RequestMapping("ocr")
//    @ResponseBody
//    @RequestMapping("/validateSim")
//    public ResponseBean validateSim(HttpServletRequest request,String simCardNo, O2oPreOrder cond) {
//        String streamNo = createStreamNo();
//        writerFlowLogEnterMenthod(streamNo, "", "", getClass().getName(), "toValidateSim", request.getParameterMap(), "提交sim卡", request);
//        ResponseBean responseBean = new ResponseBean();
//        try {
//            TfH5SimConf simConf = new TfH5SimConf();
//            simConf.setConfId(cond.getConfId());
//            List<TfH5SimConf> confList = plansService.queryListCond(simConf);
//            SimCardCheckCondition condition = new SimCardCheckCondition();
//            condition.setSimCardNo(simCardNo);
//            if (OrderConstant.O2O_TYPE_XYYX.equals(confList.get(0).getExtContent2())) {
//                condition.setNewHaoduanka("Y");
//            }
//            if(StringUtils.isNotEmpty(cond.getReCmdCode())) {
//                String reCmdId = Base64.decode(cond.getReCmdCode().getBytes());
//                TfOrderRecmd recmdRst = recmdMainService.getOrderRecmd(new TfOrderRecmd(Long.valueOf(reCmdId)));
//                cond.setLoginName(recmdRst.getRecmdPhone());
//            }
//        }catch (Exception e){
//            logger.info("error---------------"+e);
//            bean.addDefaultError();
//            e.printStackTrace();
//        }
//        return bean;
//    }
}
