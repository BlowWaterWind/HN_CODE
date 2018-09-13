package com.ai.ecs.ecm.mall.wap.modules.goods.controller;


import com.ai.ecs.common.ResponseBean;
import com.ai.ecs.common.utils.Exceptions;
import com.ai.ecs.ecm.mall.wap.modules.BaseController;
import com.ai.ecs.ecm.mall.wap.modules.goods.vo.UserGoodsCarModel;
import com.ai.ecs.ecm.mall.wap.modules.o2o.util.SimBuyCommonService;
import com.ai.ecs.ecm.mall.wap.platform.annotation.VerifyCSRFToken;
import com.ai.ecs.ecm.mall.wap.platform.utils.StringUtils;
import com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils;
import com.ai.ecs.ecsite.modules.myMobile.entity.PhoneAttributionModel;
import com.ai.ecs.ecsite.modules.phoneAttribution.service.PhoneAttributionService;
import com.ai.ecs.exception.EcsException;
import com.ai.ecs.exception.ExceptionUtils;
import com.ai.ecs.goods.api.IPlansService;
import com.ai.ecs.goods.entity.TfH5SimConf;
import com.ai.ecs.goods.entity.goods.TfUserGoodsCar;
import com.ai.ecs.member.entity.MemberAddress;
import com.ai.ecs.member.entity.MemberLogin;
import com.ai.ecs.member.entity.MemberVo;
import com.ai.ecs.order.api.IOrderService;
import com.ai.ecs.order.api.busi.IOrderAppointmentService;
import com.ai.ecs.order.api.busi.ISimBusiService;
import com.ai.ecs.order.constant.OrderConstant;
import com.ai.ecs.order.entity.*;
import com.ai.iis.upp.util.UppCore;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ai.ecs.ecm.mall.wap.platform.utils.LogUtils.*;

@Controller
@RequestMapping("/schoolSim")
public class SimSchoolBuyController extends BaseController {

    @Autowired
    private ISimBusiService simBusiService;


    @Autowired
    private IOrderService orderService;

    @Autowired
    private SimBuyCommonService simBuyCommonService;

    @Autowired
    private PhoneAttributionService phoneAttributionService;


    @Autowired
    private IOrderAppointmentService orderAppointmentService;

    @Value("${realNameAuthPath}")
    private String realNameAuthPath;

    @Value("${realNameCallBackUrlPath}")
    private String realNameCallBackUrlPath;

    @Autowired
    private IPlansService plansService;


    /**
     * 预约采集信息验签接口
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "payAppointment")
    @ResponseBody
    public ResponseBean payAppointment(HttpServletRequest request) {
        String streamNo = createStreamNo();
        ResponseBean resp = new ResponseBean();
        try {
            Map<String, String> param = UppCore.getHashMapParam(request.getParameterMap());
            String reqParam = JSONObject.toJSONString(param);
            String phone = param.get("orderDetailSim.phone");
            writerFlowLogEnterMenthod(streamNo, "", "", getClass().getName(),
                    "schoolPayAppointment", request.getParameterMap(), "预约下单", request);
            String callBackUrl = realNameCallBackUrlPath + "/schoolSim/simh5OnlineToApplyCallback?transactionId=";
            String requestStr = simBuyCommonService.preordainCheck(phone, reqParam, callBackUrl,streamNo);
            resp.addSuccess("成功", realNameAuthPath + requestStr);
            logger.info(JSONObject.toJSONString(resp));
        } catch (Exception e) {
            writerFlowLogThrowable(streamNo, "", "", this.getClass().getName(),
                    "payAppointment-error-school", null, "预约发起失败:" + processThrowableMessage(e));
            resp = ExceptionUtils.dealExceptionRetMsg(resp, e, "预约发起失败");
        }

        return resp;
    }


    @RequestMapping("simh5OnlineToApplyCallback")
    public String simh5OnlineToApplyCallback(String transactionId, Model model) {
        String streamNo = createStreamNo();
        TfOrderAppointment orderAppointment;
        JSONObject param;
        //查询预约信息
        orderAppointment = orderAppointmentService.getOrderAppointmentById(transactionId);
        TfH5SimConf simConf;
        if (orderAppointment != null && com.ai.ecs.ecm.mall.wap.platform.utils.StringUtils.isNotEmpty(orderAppointment.getRequestParam())) {
            simBuyCommonService.orderTimeout(orderAppointment);//号码不需要预占,应该不需要判断28分钟超时了
            param = JSONObject.parseObject(orderAppointment.getRequestParam());
            TfH5SimConf cond = new TfH5SimConf();
            cond.setConfId(param.getString("confId"));
            List<TfH5SimConf> h5Confs = plansService.queryListCond(cond);
            simConf = h5Confs.get(0);
            model.addAttribute("phone", param.getString("orderDetailSim.phone"));
            model.addAttribute("iccid",param.getString("orderDetailSim.iccid"));
            model.addAttribute("confId", param.getString("confId"));
            model.addAttribute("recmdCode", param.getString("recmdCode"));
            model.addAttribute("CHANID", param.getString("CHANID"));
            model.addAttribute("shopId", param.getString("shopId"));
            model.addAttribute("regName",orderAppointment.getCustName());
            model.addAttribute("transactionId", transactionId);
            model.addAttribute("conf",simConf);
            if (com.ai.ecs.common.utils.StringUtils.isEmpty(orderAppointment.getCustCertNo())) {
                writerFlowLogThrowable(streamNo, "", "", "", getClass().getName(),
                        null, String.format("身份证信息为空[%s]", transactionId));
                throw new RuntimeException("身份验证信息为空，请重新认证！");
            }
            return "web/goods/sim/applySimH5OnlineV2-" + simConf.getPageCode();
        } else {
            writerFlowLogThrowable(streamNo, "", "", "", getClass().getName(),
                    null, String.format("预约订单参数据为空，请重新预约[%s]", transactionId));
            throw new RuntimeException("身份验证信息处理异常，请重新认证！");
        }

    }


    /**
     * 从页面17填写信息提交订单
     * @param request
     * @param cModel
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/createSchoolSimOrder")
    @ResponseBody
    @VerifyCSRFToken
    public ResponseBean createShoolSimOrder(HttpServletRequest request, UserGoodsCarModel cModel, Model model) throws Exception {
        String streamNo = createStreamNo();
        writerFlowLogEnterMenthod(streamNo, "", "", getClass().getName(), "createShoolSimOrder", request.getParameterMap(), "提交参数", request);
        ResponseBean responseBean = new ResponseBean();
        String confId = request.getParameter("confId");
        String CHANID = request.getParameter("CHANID");
        String shopId = request.getParameter("shopId");
        String recmdCode = request.getParameter("recmdCode");
        String transactionId=request.getParameter("transactionId");
        if (StringUtils.isEmpty(CHANID)) {
            //如果从和掌柜分享出去,没有CHANID,可以通过推荐记录,生成订单时OrderChannelCode不能为空;设置默认渠道为手机商城
            CHANID = "E007";
        }
        JSONObject param;
        try {
            cModel = simBuyCommonService.getCallbackAppointment(transactionId,streamNo,cModel);
        }catch (EcsException e){
            writerFlowLogThrowable(streamNo,"","","",getClass().getName(),"",processThrowableMessage(e));
            throw new RuntimeException("身份验证信息处理异常，请重新认证！");//ConstantsBase.MY_EXCEP要和ExceptionUtils.dealExceptionRetMsg配合使用
        }
        try {

            MemberVo member = UserUtils.getLoginUser(request);
            TfOrderDetailSim orderDetailSim = cModel.getOrderDetailSim();
            orderDetailSim.setRegType("1");//设置证件类型
            orderDetailSim.setSynOnliResuIssuc("O2O");
            if (isProEnv()) {
                simBusiService.oneIdFiveNoVerify(orderDetailSim);
                logger.info("一证五号校验成功！");
            }
            cModel = UserGoodsCarModel.getSimBaseInfo(cModel);//放入号卡固定信息(这个业务不能放入号卡固定信息 跟在线售卡不同,要在tf_order_sub中填准shopId)
            cModel.getUserGoodsCarList().get(0).setShopId(StringUtils.isNotEmpty(shopId) ? Long.parseLong(shopId) : 1L);
            PhoneAttributionModel phoneAttributionModel = new PhoneAttributionModel(); //查询归属地
            phoneAttributionModel.setSerialNumber(orderDetailSim.getPhone());
            String cityCode = phoneAttributionService.queryPhoneAttributionAnalysis(phoneAttributionModel);
            orderDetailSim.setCityCode(cityCode);
            TfOrderSub tfOrderSub = equipOrderSub(cModel, confId, member, CHANID);
            List<TfOrderSub> orderSubList = orderService.newOrder(tfOrderSub);
            TfOrderSub orderSub = orderSubList.get(0);
            Map map = new HashMap<>();
            map.put("orderSubNo", orderSub.getOrderSubNo());
            if (orderSub.getOrderSubPayAmount() > 0) {
                map.put("type", "toPay");
                map.put("confId", confId);
                map.put("selectPhone", tfOrderSub.getPhoneNumber());
            } else {
                map.put("type", "toSuccess");
            }
            if (StringUtils.isNotEmpty(recmdCode)) {
                simBuyCommonService.saveSimRecmdInfo(recmdCode, orderSub);
            }
            responseBean.addSuccess(map);
        } catch (Exception e) {
            writerFlowLogThrowable(streamNo, "", "", this.getClass().getName(), "createShoolSimSubmitOrder", null, Exceptions.getStackTraceAsString(e));
            if (e instanceof EcsException) {
                EcsException e1 = (EcsException) e;
                logger.error("接口异常", e);
                responseBean.addError("-1", e1.getFriendlyDesc());
            } else {
                logger.error("校园促销提交订单是失败", e);
                responseBean = ExceptionUtils.dealExceptionRetMsg(responseBean, e, "订单生成失败");
            }
        }
        return responseBean;
    }

    private TfOrderSub equipOrderSub(UserGoodsCarModel cModel, String confId, MemberVo member, String CHANID) throws Exception {
        TfOrderSub orderSub = new TfOrderSub();
        TfOrderDetailSim detailSim = cModel.getOrderDetailSim();
        TfUserGoodsCar userGoodsCar = cModel.getUserGoodsCarList().get(0);
        MemberAddress address = cModel.getMemberAddress();
        address.setMemberRecipientName(detailSim.getRegName());
        orderSub.setOrderTypeId(OrderConstant.TYPE_SIM);
        //用户购买的号码保存在PHONE_NUMBER中,用于支付成功后的入参
        orderSub.setPhoneNumber(detailSim.getPhone());
        orderSub.setDeliveryModeId(cModel.getDeliveryMode().getDeliveryModeId());
        detailSim.setContactPhone(detailSim.getPhone());
        detailSim.setBaseSetName("移动校园卡");
        MemberLogin memberLogin;
        if (member == null) {
            detailSim.setIsCreateUser("RE");//分享办理
            memberLogin = simBuyCommonService.anonymousLogin(detailSim, address);
        } else {
            detailSim.setIsCreateUser("AP");
            memberLogin = member.getMemberLogin();
        }
        TfOrderUserRef userRef = new TfOrderUserRef();
        userRef.setMemberId(memberLogin.getMemberId());
        userRef.setMemberLogingName(detailSim.getCustName()); //归属地市，直接使用收货地址信息
        userRef.setEparchyCode(address.getMemberRecipientCity());//归属地市，直接使用收货地址信息
        userRef.setCounty(address.getMemberRecipientCounty());//归属区县，直接使用收货地址信息
        userRef.setContactPhone(detailSim.getPhone());
        userRef.setMemberCreditClass("0"); //用户星级 | 客户类型，为空则为普通用户
        orderSub.setShopId(userGoodsCar.getShopId());//从获取的
        orderSub.setShopName(userGoodsCar.getShopName());


        detailSim.setMemberId(memberLogin.getMemberId());
        detailSim.setCustName(memberLogin.getMemberLogingName());
        detailSim.setSynOnliResuIssuc("O2O");
        detailSim.setTerminalType("XYCX");//校园促销
        detailSim.setPsptAddr(cModel.getMemberAddress().getMemberRecipientProvince()
                + cModel.getMemberAddress().getMemberRecipientCounty()
                + cModel.getMemberAddress().getMemberRecipientCity()
        );
        detailSim.setChnlCodeOut("E050");
        TfOrderSubDetail orderSubDetail = new TfOrderSubDetail();
        orderSubDetail.setRootCateId(2L);//产品类别(javaBean中定义非空)

        orderSubDetail.setShopId(userGoodsCar.getShopId());//(javaBean中定义非空)
        orderSubDetail.setShopName(userGoodsCar.getShopName());//(javaBean中定义非空)
        orderSubDetail.setShopTypeId(userGoodsCar.getShopTypeId());//(javaBean中定义非空)

        orderSubDetail.setGoodsName("校园促销在线号卡订单");// 商品信息(javaBean中定义非空)
        orderSubDetail.setGoodsImgUrl("www.wap.hn.10086.cn/shop");//默认商城的首页
        orderSubDetail.setGoodsRemark(userGoodsCar.getGoodsRemark());//备注
        orderSubDetail.setGoodsSkuId(userGoodsCar.getGoodsSkuId());
        orderSubDetail.setGoodsSkuPrice(0L);//在生成订单是自动设置tfOrderSub的支付金额(javaBean中定义非空)
        orderSubDetail.setGoodsSkuNum(1L);//每个订单只能一个号码(javaBean中定义非空 订单生成过程中用skuNum * skuPrice)
        orderSubDetail.setGoodsSkuId(Long.parseLong(detailSim.getPhone()));//保存套餐的planId(javaBean中定义非空)
        orderSubDetail.setGoodsId(Long.parseLong(detailSim.getPhone()));//号卡订单:号码作为GoodsId(javaBean中定义非空)
        orderSubDetail.setGoodsFormat(detailSim.getPhone());
        orderSubDetail.setGoodsUrl(confId);//号卡订单:confId保存在goodsFormat中(javaBean中定义非空)
        orderSubDetail.setOrderDetailSim(detailSim);//装配sim订单信息
        orderSub.addOrderDetail(orderSubDetail);//装配订单详情信息
        orderSub.setOrderUserRef(userRef);//装配用户信息
        //装配物流信息
        TfOrderRecipient receipt = new TfOrderRecipient();
        receipt.setOrderRecipientProvince(address.getMemberRecipientProvince());
        receipt.setOrderRecipientCity(address.getMemberRecipientCity());
        receipt.setOrderRecipientCounty(address.getMemberRecipientCounty());
        receipt.setOrderRecipientAddress(address.getMemberRecipientAddress());
        //如果收货人电话为空，则取号卡订单详情表中的联系电话，用于网选厅取和h5匿名在线售卡
        String endContactPhone = detailSim.getContactPhone();
        receipt.setOrderRecipientPhone(endContactPhone);
        //如果没有收货人，则用身份证的名字
        receipt.setOrderRecipientName(detailSim.getRegName());
        orderSub.setRecipient(receipt);
        orderSub.setOrderChannelCode(CHANID);//渠道编码,供和掌柜查询(javaBean中定义渠道非空)
        orderSub.setPayModeId(cModel.getPayMode().getPayModeId());//支付方式
        return orderSub;

    }
}

