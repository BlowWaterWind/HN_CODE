package com.ai.ecs.ecm.mall.wap.modules.goods.controller.simbank;

import com.ai.ecs.common.ResponseBean;
import com.ai.ecs.common.utils.JedisClusterUtils;
import com.ai.ecs.ecm.mall.wap.common.CommonParams;
import com.ai.ecs.ecm.mall.wap.modules.BaseController;
import com.ai.ecs.ecm.mall.wap.platform.utils.DictUtil;
import com.ai.ecs.ecm.mall.wap.platform.utils.KeyValueUtil;
import com.ai.ecs.ecm.mall.wap.platform.utils.MD5;
import com.ai.ecs.ecm.mall.wap.platform.utils.MemberLoginLogUtils;
import com.ai.ecs.goods.entity.TfBankFamilyPlan;
import com.ai.ecs.goods.entity.TfPlans;
import com.ai.ecs.order.api.IOrderQueryService;
import com.ai.ecs.order.constant.OrderConstant;
import com.ai.ecs.order.entity.TfOrderBankDeposit;
import com.ai.ecs.order.entity.TfOrderSub;
import com.ai.ecs.utils.LogUtils;
import com.ai.iis.upp.util.UppCore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

/**
 * Created by cc on 2018/5/25.
 * 建设银行优惠办理活动
 */
@Controller
@RequestMapping("/ccbank")
public class SimCcbBuyController extends BaseController{



    @Autowired
    private BankCommonService bankCommonService;

    @Autowired
    private IOrderQueryService orderQueryService;

    private static final String ORDER_SUB_NO = "orderSubNo";

    private static final String CCB_BANK = "CCB";

    private static final String KEY = "o5QLVefRcE2PseQpldj1gJgNsD4A1qDdzYNF1SQQ";

    private static final String WAPHTMLEXCEPTION_0097="0097&签名验证未通过";

    private static final String WAPHTMLEXCEPTION_0098="0098&链接已过期";

    private static final Long OVER_DUE = 10 * 60 * 1000L;


    @RequestMapping("toCheckNum")
    public String toCheckNum(HttpServletRequest request) {
        Map<String, String> ccbParamMap = UppCore.getHashMapParam(request.getParameterMap());
        String sign = ccbParamMap.remove("hmac");
        if(!MD5.verify(KeyValueUtil.mapToString(ccbParamMap),sign,KEY,"UTF-8")){
            throw new RuntimeException(WAPHTMLEXCEPTION_0097);
        }
        String timestamp = ccbParamMap.get("timestamp");
        String callSystemId = ccbParamMap.get("callSystemId");
        if(System.currentTimeMillis()-Long.parseLong(timestamp) > OVER_DUE){
            throw new RuntimeException(WAPHTMLEXCEPTION_0098);
        }
        //记录访问日志
        MemberLoginLogUtils.saveLog(request, "5", "1", "建行访问成功", null, "2000", null);
        return "web/goods/simccb/checkNum_01";
    }

    /**
     * 防止客户自行修改可以办理的等级
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("toChooseOffer")
    public String toChooseOffer(HttpServletRequest request, Model model) {
        bankCommonService.toChooseOffer(request,model,CCB_BANK);
        return "web/goods/simccb/chooseOffer_02";
    }


    /**
     * 跳转到填写短信验证码的页面
     * @param request
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("toConfirmOffer")
    public String toConfirmOffer(HttpServletRequest request, Model model) throws Exception {
        bankCommonService.toConfirmOffer(request,model);
        return "web/goods/simccb/validatePay_03";
    }

    @RequestMapping("/toPay")
    public String toPay(HttpServletRequest request, Model model) {
        String orderSubNo = request.getParameter(ORDER_SUB_NO);
        String fail = request.getParameter("fail");
        bankCommonService.toPay(orderSubNo,fail,model);
        return "web/goods/simccb/toPay_04";
    }

    /**
     * 支付同步回调接口
     * 成功跳冻结保证金页面
     *
     * @param model
     * @param returnCode
     * @param chargeflowId
     * @return
     */
    @RequestMapping("toPayResult")
    public String toPayResult(Model model, String returnCode, Long chargeflowId) throws Exception {
        TfOrderSub orderSubParam = new TfOrderSub();
        orderSubParam.setOrderId(chargeflowId);
        TfOrderSub orderSub = orderQueryService.queryComplexOrder(orderSubParam);
        TfOrderBankDeposit deposit = orderSub.getOrderSpdBankDeposit();
        model.addAttribute("deposit", deposit);
        model.addAttribute("orderSub", orderSub);
        if ("0000".equals(returnCode)) {
            model.addAttribute("minute", "00");
            model.addAttribute("second", "00");
            //冻结保证金页面 第一次进入retry=false
            model.addAttribute("fail", false);
            return "web/goods/simccb/afterPay_05";
        } else {
            //支付失败跳转到原支付页面
            return "redirect:" + "toPay?orderSubNo=" + orderSub.getOrderSubNo() + "&fail=fail";
        }
    }



}
