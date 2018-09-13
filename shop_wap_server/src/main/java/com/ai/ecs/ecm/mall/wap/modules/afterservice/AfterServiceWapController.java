package com.ai.ecs.ecm.mall.wap.modules.afterservice;

import com.ai.ecs.afterservice.api.IAftersaleServerService;
import com.ai.ecs.afterservice.constant.AftersaleSatusContant;
import com.ai.ecs.afterservice.constant.AftersaleTypeConstant;
import com.ai.ecs.afterservice.entity.AftersaleApply;
import com.ai.ecs.afterservice.entity.AftersaleApplyImg;
import com.ai.ecs.afterservice.entity.AftersaleApplyType;
import com.ai.ecs.afterservice.entity.AftersaleReplyReason;
import com.ai.ecs.common.ResponseBean;
import com.ai.ecs.ecm.mall.wap.modules.BaseController;
import com.ai.ecs.ecm.mall.wap.modules.afterservice.service.AftersaleCommonService;
import com.ai.ecs.ecm.mall.wap.platform.config.Global;
import com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils;
import com.ai.ecs.entity.base.Page;
import com.ai.ecs.order.api.IOrderQueryService;
import com.ai.ecs.order.api.IOrderSubDetailGiftService;
import com.ai.ecs.order.api.aftersale.IChangeGoodUserService;
import com.ai.ecs.order.api.aftersale.IReturnGoodUserService;
import com.ai.ecs.order.constant.Variables;
import com.ai.ecs.order.entity.TfOrderSub;
import com.ai.ecs.order.entity.TfOrderSubDetailGift;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("afterserviceWap/aftersaleService")
public class AfterServiceWapController extends BaseController {

    @Autowired
    private IAftersaleServerService aftersaleService;

    @Autowired
    private IOrderQueryService orderQueryService;

    @Autowired
    private IReturnGoodUserService retGoodUserService;

    @Autowired
    private IOrderSubDetailGiftService giftService;

    @Autowired
    private IChangeGoodUserService changeGoodService;

    @Autowired
    private IReturnGoodUserService retGoodService;

    @Autowired
    private AftersaleCommonService commonService;

    private final String pagePath = "web/afterservice/";

    /**
     * 测试页面转向（初期使用，欲删）
     */
    @RequestMapping("/orderDemoTest")
    public String orderDetailTest() {
        return pagePath + "orderDemoWapTest";
    }

    /**
     * 跳转到售后服务大类型页面，只做跳转工作（初期使用，欲删）
     */
    @RequestMapping(value = "asServerUI")
    public String toASServerUI(HttpServletRequest request, Map<String, Object> retMap) throws Exception {
        logger.info("========== 进入wap端售后申请 ==========");
        String serverType = request.getParameter("serverType");
        String serverName = request.getParameter("serverName");
        String asServerTypeId = request.getParameter("asServerTypeId");
        //售后服务类型
        int typeId = Integer.parseInt(request.getParameter("asServerTypeId"));
        List<AftersaleReplyReason> asReasonL = aftersaleService.selectAllListByTypeId(typeId);
        retMap.put("asReasonL", asReasonL);
        retMap.put("serverType", serverType);
        retMap.put("serverName", serverName);
        retMap.put("asServerTypeId", asServerTypeId);
        return pagePath + serverName;
    }


    /**
     * 初始化原因列表框
     */
    @RequestMapping(value = "initData", method = RequestMethod.POST)
    @ResponseBody
    public List<AftersaleReplyReason> ajaxDatas(HttpServletRequest request)
            throws Exception {
        int typeId = Integer.parseInt(request.getParameter("asServerTypeId"));
        List<AftersaleReplyReason> asReasonL;
        asReasonL = aftersaleService.selectAllListByTypeId(typeId);
        // 使用的是fastjson包中list转json方式
        //String testJson = JSON.toJSONString(asReasonL, true);
        //logger.info(testJson);
        return asReasonL;
    }

    /**
     * 跳转申请售后页面
     */
    @RequestMapping(value = "/toAftersaleServiceUI")
    public String toMyAftersaleServiceUI(HttpServletRequest request, Map<String, Object> retMap) {
        logger.info("========== 跳转wap端售后申请页面 ==========");
        long subOrderId = Long.parseLong(request.getParameter("subOrderId"));
        long subOrderDetailId = Long.parseLong(request.getParameter("subOrderDetailId"));
        TfOrderSub showOrder = new TfOrderSub();
        try {
            //== 检查是否可以申请退换货 ==//
            long currentMemberId = UserUtils.getLoginUser(request).getMemberLogin().getMemberId();
            Map resultMap = commonService.applyIsDuringValid(retMap, subOrderId, subOrderDetailId,currentMemberId);
            boolean flage = (boolean) resultMap.get("flage");
            retMap = (Map<String, Object>) resultMap.get("retMap");
            //统一返回结果页面
            if (!flage) {
                retMap.put("msgCode", "-1");
                return pagePath + "aftersalePromptWap";
            }
            //== 得到相应Id对应的子订单信息 ==//
            showOrder = orderQueryService.getSubOrderAndSingleDetl(subOrderId, subOrderDetailId);
            if(ObjectUtils.isEmpty(currentMemberId)||currentMemberId!=showOrder.getOrderUserRef().getMemberId()){
         	   throw new Exception("非法访问限制！");
            }
        }catch (Exception e){
            e.printStackTrace();
            retMap.put("msgCode","-1");
            return pagePath + "aftersalePromptWap";
        }
        retMap.put("showOrder", showOrder);
        return pagePath + "aftersaleServiceSelectWap";
    }

    /**
     * 申请维修
     */
    @RequestMapping(value = "/repairApply")
    public String repairApply(DefaultMultipartHttpServletRequest multipartRequest,
            HttpServletRequest request, Map<String, Object> retMap) {
        logger.info("========== 进入wap端申请维修 ==========");
        long orderSubId = Long.parseLong(request.getParameter("orderSubId"));
        long orderSubDetailId = Long.parseLong(request.getParameter("orderSubDetailId"));
        String repairReason = request.getParameter("repairReason");
        String repairExplain = request.getParameter("repairExplain");

        Long memberId = UserUtils.getLoginUser(request).getMemberLogin().getMemberId();
        String memberLoginName = UserUtils.getLoginUser(request).getMemberLogin().getMemberLogingName();
        AftersaleApply asApp = new AftersaleApply();

        // 处理子订单号
        TfOrderSub subOrder = new TfOrderSub();
        subOrder.setOrderSubId(orderSubId);
        TfOrderSub showOrder = new TfOrderSub();
        try {
            showOrder = orderQueryService.queryComplexOrder(subOrder, Variables.ACT_GROUP_MEMBER);
            asApp.setOrderSubNo(showOrder.getOrderSubNo());
            retMap.put("orderSubNo", showOrder.getOrderSubNo());
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        asApp.setOrderSubId(orderSubId);
        asApp.setOrderSubDetailId(orderSubDetailId);
        asApp.setAftersaleApplyReason(repairReason);
        asApp.setAftersaleApplyDescribe(repairExplain);
        asApp.setMemberId(memberId);
        asApp.setMemberLoginName(memberLoginName);
        asApp.setAftersaleApplyTime(new Date());
        asApp.setAftersaleApplyStatusId(AftersaleSatusContant.AFTERSALE_PENDING);
        asApp.setAftersaleApplyTypeId(AftersaleTypeConstant.AFTERSALE_TYPE_REPAIR);
        String appNo = AfterserviceTool.genOrderNum();
        asApp.setAftersaleApplyNum(appNo);

        retMap.put("asServerName", "repair");
        retMap.put("repairReason", repairReason);
        retMap.put("repairExplain", repairExplain);

        try {
            // 获取、上传图片
            ResponseBean<String> responsebean = AfterserviceTool.upload2tfs(request,
                    multipartRequest);
            // 上传失败
            if (responsebean.getCode().equals("-1")) {
                retMap.put("errorMsg", "上传图片失败");
                return pagePath + "aftersaleServiceSelectWapBack";
            }
            // 上传成功，有返回名
            String tfsRetDataName = responsebean.getData();

            // 没有上传图片：只存储申请单
            List<AftersaleApplyImg> asAppImgL = new ArrayList<AftersaleApplyImg>();
            // 处理图片
            if (tfsRetDataName != null) {
                if (!tfsRetDataName.contains(",")) {
                    AftersaleApplyImg asAppImg = new AftersaleApplyImg();
                    asAppImg.setAftersaleApplyImgUrl(tfsRetDataName);
                    asAppImgL.add(asAppImg);
                }
                else {
                    String[] s = tfsRetDataName.split(",");
                    for (int i = 0; i < s.length; i++) {
                        AftersaleApplyImg asAppImg = new AftersaleApplyImg();
                        asAppImg.setAftersaleApplyImgUrl(s[i]);
                        asAppImgL.add(asAppImg);
                    }
                }
            }
            aftersaleService.insertAftersaleImgAndApply(asApp, asAppImgL);
        }
        catch (Exception e) {
            e.printStackTrace();
            retMap.put("errorMsg", "维修申请失败");
            return pagePath + "aftersaleServiceSelectWapBack";
        }
        retMap.put("sucMsg", "维修申请成功，等待审核");
        retMap.put("appNo", appNo);
        return pagePath + "aftersaleServiceSelectWapBack";
    }

    /**
     * 补寄发票
     */
    @RequestMapping(value = "/resendReceiptApply")
    public String resendReceiptApply(DefaultMultipartHttpServletRequest multipartRequest,
            HttpServletRequest request, Map<String, Object> retMap) {
        logger.info("========== wap端补寄发票申请 ==========");
        long orderSubId = Long.parseLong(request.getParameter("orderSubId"));
        long orderSubDetailId = Long.parseLong(request.getParameter("orderSubDetailId"));
        String resendReceiptReason = request.getParameter("resendReceiptReason");
        String resendReceiptExplain = request.getParameter("resendReceiptExplain");
        Long memberId = UserUtils.getLoginUser(request).getMemberLogin().getMemberId();
        String memberLoginName = UserUtils.getLoginUser(request).getMemberLogin().getMemberLogingName();
        AftersaleApply asApp = new AftersaleApply();
        // 处理子订单号
        TfOrderSub subOrder = new TfOrderSub();
        subOrder.setOrderSubId(orderSubId);
        TfOrderSub showOrder = new TfOrderSub();
        try {
            showOrder = orderQueryService.queryComplexOrder(subOrder, Variables.ACT_GROUP_MEMBER);
            asApp.setOrderSubNo(showOrder.getOrderSubNo());
            retMap.put("orderSubNo", showOrder.getOrderSubNo());
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        asApp.setOrderSubId(orderSubId);
        asApp.setOrderSubDetailId(orderSubDetailId);
        asApp.setAftersaleApplyReason(resendReceiptReason);
        asApp.setAftersaleApplyDescribe(resendReceiptExplain);
        asApp.setMemberId(memberId);
        asApp.setMemberLoginName(memberLoginName);

        asApp.setAftersaleApplyTime(new Date());
        asApp.setAftersaleApplyStatusId(AftersaleSatusContant.AFTERSALE_PENDING);
        asApp.setAftersaleApplyTypeId(AftersaleTypeConstant.AFTERSALE_TYPE_RESENDRECEIPT);
        String appNo = AfterserviceTool.genOrderNum();
        asApp.setAftersaleApplyNum(appNo);

        retMap.put("asServerName", "resendReceipt");
        retMap.put("resendReceiptReason", resendReceiptReason);
        retMap.put("resendReceiptExplain", resendReceiptExplain);

        try {
            // 获取、上传图片
            ResponseBean<String> responsebean = AfterserviceTool.upload2tfs(request, multipartRequest);
            // 上传失败
            if (responsebean.getCode().equals("-1")) {
                retMap.put("errorMsg", "上传图片失败");
                return pagePath + "aftersaleServiceSelectWapBack";
            }
            // 上传成功，有返回名
            String tfsRetDataName = responsebean.getData();

            // 没有上传图片：只存储申请单
            List<AftersaleApplyImg> asAppImgL = new ArrayList<AftersaleApplyImg>();
            // 处理图片
            if (tfsRetDataName != null) {
                if (!tfsRetDataName.contains(",")) {
                    AftersaleApplyImg asAppImg = new AftersaleApplyImg();
                    asAppImg.setAftersaleApplyImgUrl(tfsRetDataName);
                    asAppImgL.add(asAppImg);
                }
                else {
                    String[] s = tfsRetDataName.split(",");
                    for (int i = 0; i < s.length; i++) {
                        AftersaleApplyImg asAppImg = new AftersaleApplyImg();
                        asAppImg.setAftersaleApplyImgUrl(s[i]);
                        asAppImgL.add(asAppImg);
                    }
                }
            }
            aftersaleService.insertAftersaleImgAndApply(asApp, asAppImgL);
        }
        catch (Exception e) {
            e.printStackTrace();
            retMap.put("errorMsg", "补寄申请失败");
            return pagePath + "aftersaleServiceSelectWapBack";
        }
        retMap.put("sucMsg", "补寄申请成功，等待审核");
        retMap.put("appNo", appNo);
        return pagePath + "aftersaleServiceSelectWapBack";
    }

    /**
     * 赠品问题
     */
    @RequestMapping(value = "/giftProblemApply")
    public String giftProblemApply(DefaultMultipartHttpServletRequest multipartRequest,
            HttpServletRequest request, Map<String, Object> retMap) {
        logger.info("========== wap端赠品问题申请 ==========");
        long orderSubId = Long.parseLong(request.getParameter("orderSubId"));
        long orderSubDetailId = Long.parseLong(request.getParameter("orderSubDetailId"));
        String giftProblemReason = request.getParameter("giftProblemReason");
        String giftProblemExplain = request.getParameter("giftProblemExplain");

        Long memberId = UserUtils.getLoginUser(request).getMemberLogin().getMemberId();
        String memberLoginName = UserUtils.getLoginUser(request).getMemberLogin().getMemberLogingName();
        retMap.put("asServerName", "giftProblem");
        AftersaleApply asApp = new AftersaleApply();

        // 处理子订单号
        TfOrderSub showOrder = new TfOrderSub();
        try {
            //== 子订单信息 ==//
            TfOrderSub subOrder = new TfOrderSub();
            subOrder.setOrderSubId(orderSubId);
            showOrder = orderQueryService.queryComplexOrder(subOrder, Variables.ACT_GROUP_MEMBER);
            asApp.setOrderSubNo(showOrder.getOrderSubNo());
            retMap.put("orderSubNo", showOrder.getOrderSubNo());

            asApp.setOrderSubId(orderSubId);
            asApp.setOrderSubDetailId(orderSubDetailId);
            asApp.setAftersaleApplyReason(giftProblemReason);
            asApp.setAftersaleApplyDescribe(giftProblemExplain);
            asApp.setMemberId(memberId);
            asApp.setMemberLoginName(memberLoginName);
            asApp.setAftersaleApplyTime(new Date());
            asApp.setAftersaleApplyStatusId(AftersaleSatusContant.AFTERSALE_PENDING);
            asApp.setAftersaleApplyTypeId(AftersaleTypeConstant.AFTERSALE_TYPE_GIFTPROBLEM);
            String appNo = AfterserviceTool.genOrderNum();
            asApp.setAftersaleApplyNum(appNo);
            retMap.put("appNo", appNo);
            retMap.put("giftProblemReason", giftProblemReason);
            retMap.put("giftProblemExplain", giftProblemExplain);

            //== 查询是否符合赠品申请的要求 ==//
            List<TfOrderSubDetailGift> orderDetlGifts = new ArrayList<TfOrderSubDetailGift>();
            orderDetlGifts = giftService.queryOrderGiftByDetlId(orderSubDetailId);
            if (orderDetlGifts.size() == 0) {
                retMap.put("errorMsg", "对不起，该商品没有赠品信息");
                return pagePath + "aftersaleServiceSelectWapBack";
            }
            //== 循环遍历每个商品的赠品是否已经赠送 ==//
            boolean flage = true;
            for (TfOrderSubDetailGift detlGift : orderDetlGifts) {
                if ("N".equals(detlGift.getIsGift())) {
                    flage = false;
                    break;
                }
            }
            if (flage == true) {
                retMap.put("errorMsg", "对不起，您的赠品已经赠送");
                return pagePath + "aftersaleServiceSelectWapBack";
            }

            // 获取、上传图片
            ResponseBean<String> responsebean = AfterserviceTool.upload2tfs(request,multipartRequest);
            // 上传失败
            if (responsebean.getCode().equals("-1")) {
                retMap.put("errorMsg", "上传图片失败");
                return pagePath + "aftersaleServiceSelectWapBack";
            }
            // 上传成功，有返回名
            String tfsRetDataName = responsebean.getData();

            // 没有上传图片：只存储申请单
            List<AftersaleApplyImg> asAppImgL = new ArrayList<AftersaleApplyImg>();
            // 处理图片
            if (tfsRetDataName != null) {
                if (!tfsRetDataName.contains(",")) {
                    AftersaleApplyImg asAppImg = new AftersaleApplyImg();
                    asAppImg.setAftersaleApplyImgUrl(tfsRetDataName);
                    asAppImgL.add(asAppImg);
                }
                else {
                    String[] s = tfsRetDataName.split(",");
                    for (int i = 0; i < s.length; i++) {
                        AftersaleApplyImg asAppImg = new AftersaleApplyImg();
                        asAppImg.setAftersaleApplyImgUrl(s[i]);
                        asAppImgL.add(asAppImg);
                    }
                }
            }
            aftersaleService.insertAftersaleImgAndApply(asApp, asAppImgL);
        }
        catch (Exception e) {
            e.printStackTrace();
            retMap.put("errorMsg", "数据异常，赠品申请失败");
            return pagePath + "aftersaleServiceSelectWapBack";
        }
        retMap.put("sucMsg", "赠品申请成功，等待审核");
        return pagePath + "aftersaleServiceSelectWapBack";
    }

    /**
     * 申请投诉
     */
    @RequestMapping(value = "/complaintApply")
    public String complaintApply(DefaultMultipartHttpServletRequest multipartRequest,
            HttpServletRequest request, Map<String, Object> retMap) {
        logger.info("========== 进入wap端投诉申请 ==========");
        long orderSubId = Long.parseLong(request.getParameter("orderSubId"));
        long orderSubDetailId = Long.parseLong(request.getParameter("orderSubDetailId"));
        String complaintReason = request.getParameter("complaintReason");
        String complaintExplain = request.getParameter("complaintExplain");
        Long memberId = UserUtils.getLoginUser(request).getMemberLogin().getMemberId();
        String memberLoginName = UserUtils.getLoginUser(request).getMemberLogin().getMemberLogingName();

        AftersaleApply asApp = new AftersaleApply();
        // 处理子订单号
        TfOrderSub subOrder = new TfOrderSub();
        subOrder.setOrderSubId(orderSubId);
        TfOrderSub showOrder = new TfOrderSub();
        try {
            showOrder = orderQueryService.queryComplexOrder(subOrder, Variables.ACT_GROUP_MEMBER);
            asApp.setOrderSubNo(showOrder.getOrderSubNo());
            retMap.put("orderSubNo", showOrder.getOrderSubNo());
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        asApp.setOrderSubId(orderSubId);
        asApp.setOrderSubDetailId(orderSubDetailId);
        asApp.setAftersaleApplyReason(complaintReason);
        asApp.setAftersaleApplyDescribe(complaintExplain);
        asApp.setMemberId(memberId);
        asApp.setMemberLoginName(memberLoginName);

        asApp.setAftersaleApplyTime(new Date());
        asApp.setAftersaleApplyStatusId(AftersaleSatusContant.AFTERSALE_PENDING);
        asApp.setAftersaleApplyTypeId(AftersaleTypeConstant.AFTERSALE_TYPE_COMPLAINT);
        String appNo = AfterserviceTool.genOrderNum();
        asApp.setAftersaleApplyNum(appNo);

        retMap.put("asServerName", "complaint");
        retMap.put("complaintReason", complaintReason);
        retMap.put("complaintExplain", complaintExplain);

        try {
            ResponseBean<String> responsebean = AfterserviceTool.upload2tfs(request,
                    multipartRequest);
            if (responsebean.getCode().equals("-1")) {
                retMap.put("errorMsg", "上传图片失败");
                return pagePath + "aftersaleServiceSelectWapBack";
            }
            // 上传成功，有返回名
            String tfsRetDataName = responsebean.getData();
            // 没有上传图片：只存储申请单
            List<AftersaleApplyImg> asAppImgL = new ArrayList<AftersaleApplyImg>();
            // 处理图片
            if (tfsRetDataName != null) {
                if (!tfsRetDataName.contains(",")) {
                    AftersaleApplyImg asAppImg = new AftersaleApplyImg();
                    asAppImg.setAftersaleApplyImgUrl(tfsRetDataName);
                    asAppImgL.add(asAppImg);
                }
                else {
                    String[] s = tfsRetDataName.split(",");
                    for (int i = 0; i < s.length; i++) {
                        AftersaleApplyImg asAppImg = new AftersaleApplyImg();
                        asAppImg.setAftersaleApplyImgUrl(s[i]);
                        asAppImgL.add(asAppImg);
                    }
                }
            }
            aftersaleService.insertAftersaleImgAndApply(asApp, asAppImgL);
        }
        catch (Exception e) {
            e.printStackTrace();
            retMap.put("errorMsg", "订单数据异常，申请投诉服务失败");
            return pagePath + "aftersaleServiceSelectWapBack";
        }
        retMap.put("sucMsg", "投诉申请成功，管理员会在24小时内审核");
        retMap.put("appNo", appNo);
        return pagePath + "aftersaleServiceSelectWapBack";
    }

    /**
     * 跳转售后服务中心，查看列表信息
     * @param request
     * @return
     */
    @RequestMapping(value="/toAftersaleServiceCenter")
    public String toAftersaleServiceCenter(){
        logger.info("========== 跳转进入售后服务中心 ==========");
        return pagePath+"aftersaleServiceListWap";
    }
    
    /**
     * 售后管理列表信息
     */
    @RequestMapping(value = "aftersaleServiceList", method = RequestMethod.POST)
    @ResponseBody
    public String reqMyAftersaleList(HttpServletRequest request, HttpServletResponse response) {
        logger.info("========== wap端异步申请售后服务列表信息 ==========");
        String tfsUrl = request.getParameter("tfsUrl");
        String projectUrl = request.getContextPath();
        AftersaleApply cond = new AftersaleApply();
        Long memberId = UserUtils.getLoginUser(request).getMemberLogin().getMemberId();
        cond.setMemberId(memberId);

        //== 得到分页数据 ==//
        Page<AftersaleApply> page = cond.getPage();
        page.setPageNo(getPageNo(request, response));
        page.setPageSize((getPageSize(request, response, 5)));
        Page<AftersaleApply> listPage = new Page<AftersaleApply>();
        List<AftersaleApplyType> astypeL = new ArrayList<AftersaleApplyType>();
        HashMap<String, Object> respAftersalMap = new HashMap<String, Object>();
        try {
            astypeL = aftersaleService.selectAllAppTypeL();
            listPage = aftersaleService.queryAftersaleInfoPage(page,cond);
            //售后单循环赋值订单信息
            for(AftersaleApply app:listPage.getList()){
                TfOrderSub orderSubTemp = orderQueryService.getSubOrderAndSingleDetl(
                        app.getOrderSubId(),
                        app.getOrderSubDetailId()
                );
                app.setOrderSub(orderSubTemp);
            }
            respAftersalMap.put("serverType", "aftersale");
            respAftersalMap.put("astypeL", astypeL);
            respAftersalMap.put("tfsUrl",tfsUrl);
            respAftersalMap.put("projectUrl",projectUrl);

            //==如果没有售后列表信息，放一个空对象到其中，避免模板错误,【线上环境不可能存在这种情况】==//
            if (listPage.getList().size() == 0) {
                respAftersalMap.put("retMsg", Global.NO);
                Page<AftersaleApply> pageNull = new Page<AftersaleApply>();
                List<AftersaleApply> appList = new ArrayList<AftersaleApply>();
                pageNull.setList(appList);
                respAftersalMap.put("page", pageNull);
            }
            else {
                respAftersalMap.put("retMsg", Global.YES);
                respAftersalMap.put("page", listPage);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        String asAppLJson = JSONObject.toJSONString(respAftersalMap,SerializerFeature.WriteMapNullValue);
        logger.info("===== aftersaleServiceList ====="+JSONObject.toJSONString(respAftersalMap,SerializerFeature.WriteMapNullValue));
        return asAppLJson;
    }

    /**
     * 售后列表详情查看
     */
    @RequestMapping(value = "/listDetail")
    public String listDetail(HttpServletRequest request, Map<String, Object> retMap) {
        logger.info("========== 进入售后单详情 ==========");
        long aftersaleApplyId = Long.parseLong(request.getParameter("appId"));
        try {
            //售后单信息
            //AftersaleQueryCond cond = new AftersaleQueryCond();
            //cond.setAsAppId(aftersaleApplyId);
            //AftersaleApply app = aftersaleService.queryAftersaleInfoByCond(cond).get(0);
            //retMap.put("app", app);
            //审计信息

            AftersaleApply asApp = aftersaleService.queryAftersaleAuditDeal(aftersaleApplyId);
            TfOrderSub orderSub = orderQueryService.getSubOrderAndSingleDetl(asApp.getOrderSubId(), asApp.getOrderSubDetailId());
            asApp.setOrderSub(orderSub);
            retMap.put("asApp", asApp);
            logger.info("===== 售后：listDetail ====="+JSONObject.toJSONString(asApp,SerializerFeature.WriteMapNullValue));
        }
        catch (Exception e) {
            e.printStackTrace();
            retMap.put("errorMsg", "售后申请单数据异常");
            return pagePath + "aftersaleServiceSelectWapBack";
        }
        return pagePath + "aftersaleServiceDetailWap";
    }
}
