package com.ai.ecs.ecm.mall.wap.modules.o2o;

import com.ai.ecs.common.ResponseBean;
import com.ai.ecs.ecm.mall.wap.modules.BaseController;
import com.ai.ecs.ecm.mall.wap.modules.goods.controller.simbank.BankCommonService;
import com.ai.ecs.ecm.mall.wap.platform.annotation.RefreshCSRFToken;
import com.ai.ecs.ecm.mall.wap.platform.annotation.VerifyCSRFToken;
import com.ai.ecs.ecm.mall.wap.platform.utils.CSRFTokenUtil;
import com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils;
import com.ai.ecs.ecsite.modules.onlinesim.entity.CreateOrderForO2OCondition;
import com.ai.ecs.ecsite.modules.onlinesim.service.OnlineSimService;
import com.ai.ecs.entity.base.Page;
import com.ai.ecs.exception.EcsException;
import com.ai.ecs.exception.ExceptionUtils;
import com.ai.ecs.goods.api.IPlansService;
import com.ai.ecs.goods.entity.TfH5SimConf;
import com.ai.ecs.member.entity.MemberVo;
import com.ai.ecs.order.api.IOrderQueryService;
import com.ai.ecs.order.api.IOrderService;
import com.ai.ecs.order.constant.OrderConstant;
import com.ai.ecs.order.constant.Variables;
import com.ai.ecs.order.entity.*;
import com.ai.ecs.order.param.OrderProcessParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

import java.net.URLEncoder;
import java.util.*;

import static com.ai.ecs.ecm.mall.wap.platform.utils.LogUtils.convertParamToMap;
import static com.ai.ecs.ecm.mall.wap.platform.utils.LogUtils.createStreamNo;
import static com.ai.ecs.ecm.mall.wap.platform.utils.LogUtils.writerFlowLogThrowable;
import com.ai.ecs.common.utils.Exceptions;

/**
 * Created by cc on 2018/6/7.
 * 店员操作O2O在线售卡的订单
 *
 */
@Controller
@RequestMapping("o2oSimOper")
public class O2oSimOnlineOrderController extends BaseController {

    @Autowired
    private IOrderQueryService orderQueryService;

    @Autowired
    private BankCommonService bankCommonService;

    @Autowired
    private IPlansService plansService;

    @Autowired
    private OnlineSimService onlineSimService;

    private static final String CANCEL_SIM = "cancel";

    private static final String CONTINUE_SIM = "continueSim";


    /**
     * 进入订单查询页面(给九爷的地址,spring-mvc.xml配置经过拦截器)
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("orderlist")
    public String toList(HttpServletRequest request) throws Exception{
        MemberVo member = UserUtils.getLoginUser(request);
        if (member == null) {
            String url = "/o2oTest/loginError?value="+ URLEncoder.encode(URLEncoder.encode("登录超时","utf-8"),"UTF-8");
            return "redirect:" + url;
        }
        return "web/goods/o2osimshop/orderList";
    }


    /**
     * orderType 1 到店自取订单 2快递邮寄订单(进入页面发起立即发起请求)
     * @param orderStatus
     * @param orderType
     * @param searchKey
     * @param request
     * @param subOrderPage
     * @return
     */
    @RequestMapping("getOrderList")
    @ResponseBody
    public Page getOrderList(Integer orderStatus, String orderType,String searchKey,HttpServletRequest request, Page<TfOrderSub> subOrderPage) {
        MemberVo member = UserUtils.getLoginUser(request);
        if(member == null){
            subOrderPage.setList(new ArrayList<TfOrderSub>());
            return subOrderPage;
        }
        TfOrderSub orderSub = new TfOrderSub();
        switch (orderStatus.intValue()){
            //待写卡
            case 1 : orderSub.setOrderStatusId(76);break;
            //待处理 2订单确认(一般自动确认同步失败回到订单确认节点) 4备卡 6待发货 7待收货
            case 2 : orderSub.setOrderStatusIds(Arrays.asList(2,4,6,7));break;
            //已超时 18支付超时结束
            case 3 : orderSub.setOrderStatusIds(Arrays.asList(18,79));break;
            //办理成功 12已归档
            case 4 : orderSub.setOrderStatusId(12);break;
            //办理失败 79写卡超时结束 16订单退款结束
            case 5 : orderSub.setOrderStatusIds(Arrays.asList(79,16));break;
            default: orderSub.setExcludeOrderStatusIds(Arrays.asList(OrderConstant.STATUS_CREATE));
        }
        orderSub.setValetStaffId(member.getMemberLogin().getMemberLogingName());//根据渠道商的号码查询他所有办理的号码
        orderSub.setOrderAddressId(Long.valueOf(orderType));
        orderSub.setOrderTypeIds(String.valueOf(OrderConstant.TYPE_SIM));
        orderSub.setPage(subOrderPage);
        orderSub.setSearchKey(searchKey);

        Calendar calendar = Calendar.getInstance();
        orderSub.setEndOrderTime(calendar.getTime());
        calendar.add(Calendar.MONTH, -6);
        orderSub.setStartOrderTime(calendar.getTime());
        subOrderPage = orderQueryService.queryOrderPageO2OSim(orderSub);
        return subOrderPage;
    }


    @RequestMapping("getOrderDetail")
    public String getOrderDetail(TfOrderSub subParam, Model model) {
        TfOrderSub orderView = orderQueryService.queryComplexOrder(subParam);
        TfOrderSubDetail detail = orderView.getDetailList().get(0);
        TfH5SimConf cond = new TfH5SimConf();
        cond.setConfId(detail.getGoodsUrl());
        TfH5SimConf resultPlan = plansService.queryListCond(cond).get(0);
        model.addAttribute("conf", resultPlan);
        model.addAttribute("orderSub", orderView);
        return "web/goods/o2osimshop/orderDetailShop";
    }

    /**
     * 进入写卡页面
     *
     * @param subParam
     * @param model
     * @return
     */
    @RequestMapping("writesim")
    @RefreshCSRFToken
    public String toWritesim(HttpServletRequest request, TfOrderSub subParam, Model model) throws Exception {
        MemberVo member = UserUtils.getLoginUser(request);
        if (member == null) {
            String url = "/o2oTest/loginError?value=" + URLEncoder.encode(URLEncoder.encode("登录超时", "UTF-8"), "UTF-8");
            return "redirect:" + url;
        }
        TfOrderSub orderView = orderQueryService.queryComplexOrder(subParam);
        model.addAttribute("orderSub", orderView);
        return "web/goods/o2osimshop/simwrite";
    }

    /**
     * 进入写卡页面
     *
     * @param subParam
     * @param model
     * @return
     */
    @RequestMapping("deliverySim")
    @RefreshCSRFToken
    public String toDeliverySim(HttpServletRequest request, TfOrderSub subParam, Model model) throws Exception {
        MemberVo member = UserUtils.getLoginUser(request);
        if (member == null) {
            String url = "/o2oTest/loginError?value=" + URLEncoder.encode(URLEncoder.encode("登录超时", "UTF-8"), "UTF-8");
            return "redirect:" + url;
        }
        TfOrderSub orderView = orderQueryService.queryComplexOrder(subParam);
        model.addAttribute("orderSub", orderView);
        return "web/goods/o2osimshop/deliverySim";
    }



    /**
     * 调用接口
     *
     * @param subParam
     * @param simCardNo
     * @return
     */
    @RequestMapping("simBind")
    @ResponseBody
    @VerifyCSRFToken
    public ResponseBean sinBind(HttpServletRequest request, TfOrderSub subParam, String simCardNo) throws Exception {
        String streamNo = createStreamNo();
        ResponseBean responseBean = new ResponseBean();
        TfOrderSub orderView = orderQueryService.queryComplexOrder(
                subParam, Variables.ACT_GROUP_MEMBER);
        TfOrderDetailSim detailSim = orderView.getDetailSim();
        detailSim.setIccid(simCardNo);
        CreateOrderForO2OCondition condition = new CreateOrderForO2OCondition();
        condition.setSIM_CARD_NO(simCardNo);
        condition.setPRE_ID(detailSim.getJobNo());//预订单返回的SYNC_ID
        try {
            //写卡成功后WriteSimProcess.java更新订单状态detailSim的状态,在OrderListener中跟新tfOrderSub的状态,写卡失败,任务不结束,可以继续调写卡接口
            if(onlineSimService.creteOrderO2Oonline(condition)){
                bankCommonService.completeOrder(orderView, 1, OrderConstant.STATUS_TO_WRITE_SIM);
                if(orderView.getOrderAddressId() == 1){//到厅取的方式,写卡
                    bankCommonService.completeOrder(orderView, 1, OrderConstant.STATUS_DELIVERY);
                }
                responseBean.addSuccess();
            }
        } catch (Exception e) {
            writerFlowLogThrowable(streamNo, "", "", this.getClass().getName(), "simBind", null, Exceptions.getStackTraceAsString(e));
            if(e instanceof EcsException){
                EcsException e1 = (EcsException) e;
                logger.error("写卡失败", e1);
                responseBean.addError("-1", e1.getFriendlyDesc());
            }else{
                logger.error("订单扭转失败", e);
                responseBean.addError("-1", "订单扭转失败!");
            }
        }
        request.getSession().setAttribute("CSRFToken", CSRFTokenUtil.generate(request));
        return responseBean;
    }


    /**
     * 进入订单处理页面;在订单详情页处理订单
     * 1. 订单写卡(status = 76)
     * 2. 订单同步失败,回到订单确认节点(status=2 确认act=1再同步一次 act=0释放卡资源[结束或进入退款环节])
     *
     * @param request
     * @param process
     * @return
     */
    @RequestMapping("process")
    @ResponseBody
    public ResponseBean process(HttpServletRequest request, TfOrderSub subParam, String process) throws Exception {
        MemberVo member = UserUtils.getLoginUser(request);
        String streamNo = createStreamNo();
        ResponseBean responseBean = new ResponseBean();
        if (member == null) {
            responseBean.addError("-1","登录超时,请重新进入页面！");
            return responseBean;
        }
        try {
            TfOrderSub orderView = orderQueryService.queryComplexOrder(
                    subParam, Variables.ACT_GROUP_MEMBER);
            if (CANCEL_SIM.equals(process)) {
                //同步订单失败,展示时候原因后由店员(店主)主动释放卡资源;
                bankCommonService.completeOrder(orderView, 0, OrderConstant.STATUS_AUDIT);
            }
            if (CONTINUE_SIM.equals(process)) {
                //同步订单失败,展示时候原因后由店员(店主)继续发起订单同步
                TfOrderDetailSim detailSim = orderView.getDetailSim();
                detailSim.setSynchTimes(1);//sql里面设值为加1
                OrderProcessParam param = new OrderProcessParam();
                Map map= new HashMap<>();
                map.put("occupyTimeMillis",orderView.getOrderTime().getTime());
                param.setParams(map);
                bankCommonService.completeOrder(orderView, 1, OrderConstant.STATUS_AUDIT,param);
                TfOrderSub orderViewAfter = orderQueryService.queryComplexOrder(
                        subParam, Variables.ACT_GROUP_MEMBER);
                if(orderViewAfter.getOrderStatusId() == OrderConstant.STATUS_AUDIT){
                    //还在订单确认状态
                    responseBean.addError("-1",orderViewAfter.getExceptionCause());
                    return responseBean;
                }
            }
            //确认发货
            if ("conDelivery".equals(process)) {
                String logisticsCompany = request.getParameter("logisticsCompany");
                String logisticsNum = request.getParameter("logisticsNum");
                orderView.setLogisticsCompany(logisticsCompany);
                orderView.setLogisticsNum(logisticsNum);
                bankCommonService.completeOrder(orderView, 1, OrderConstant.STATUS_DELIVERY);
            }
            //确认收货
            if ("received".equals(process)) {
                bankCommonService.completeOrder(orderView, 1, OrderConstant.STATUS_RECEIPT);
            }
            responseBean.addSuccess("订单处理成功");
        } catch (Exception e) {
            writerFlowLogThrowable(streamNo, "", "", this.getClass().getName(), "process", null, Exceptions.getStackTraceAsString(e));
            logger.error("处理订单流程错误：", e);
            responseBean = ExceptionUtils.dealExceptionRetMsg(responseBean, e, "订单扭转失败！");
        }
        return responseBean;
    }

}
