package com.ai.ecs.ecm.mall.wap.modules.o2o;


import com.ai.ecs.common.ResponseBean;
import com.ai.ecs.common.utils.Base64;
import com.ai.ecs.common.utils.TFSClient;
import com.ai.ecs.ecm.mall.wap.modules.BaseController;
import com.ai.ecs.ecm.mall.wap.platform.utils.*;
import com.ai.ecs.entity.base.ConstantsBase;
import com.ai.ecs.exception.EcsException;
import com.ai.ecs.exception.ExceptionUtils;
import com.ai.ecs.goods.api.IPlansService;
import com.ai.ecs.goods.entity.TfH5SimConf;
import com.ai.ecs.member.entity.ChannelInfo;
import com.ai.ecs.member.entity.MemberVo;
import com.ai.ecs.merchant.shop.service.IShopInfoService;
import com.ai.ecs.order.api.recmd.IRecmdMainService;
import com.ai.ecs.order.entity.recmd.TfOrderRecmd;
import com.ai.ecs.order.entity.recmd.TfRecmdActConf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.web.bind.annotation.ResponseBody;

/**
 * O2O全网销售二维码管理器
 */
@Controller
@RequestMapping("qrcodeOper")
public class O2oSimWholeNetQrcodeController extends BaseController {

    private static final String URL_PREFIX = "http://wap.hn.10086.cn/shop/simBuy/simH5OnlineToApply?Prov=731&";

    private static final String URL_PREFIX_BAK = "http://localhost:8084/shop/simBuy/simH5OnlineToApply?Prov=731&";

    private static final String EFFECTIVE = "1";

    @Autowired
    private IRecmdMainService recmdMainService;

    @Autowired
    private IPlansService plansService;


    @RequestMapping("qrlist")
    public String qrlist(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        MemberVo member = UserUtils.getLoginUser(request);
        if (member == null) {
            loginPrompt(request, response, "单点登录超时");
            return null;
        } else {
            //管理配置的confId的二维码
            List<TfH5SimConf> confs = new ArrayList<>();
            String[] confIds = DictUtil.getDictValue("O2O_GROUP", "O2O_GROUP", "XYYXDWK;XYYXRSENMV8").split(";");
            for (String confId : confIds) {
                TfH5SimConf cond = new TfH5SimConf();
                cond.setConfId(confId);
                List<TfH5SimConf> h5Confs = plansService.queryListCond(cond);
                confs.add(h5Confs.get(0));
            }
            model.addAttribute("confs", confs);
            return "web/goods/o2osimgrcode/qrlist";
        }
    }


    @RequestMapping("qrDetail")
    public String qrGen(HttpServletRequest request, HttpServletResponse response, TfOrderRecmd orderRecmd, Model model) throws Exception {
        MemberVo member = UserUtils.getLoginUser(request);
        if (member == null) {
            loginPrompt(request, response, "单点登录超时");
        }
        TfOrderRecmd cmdParam = new TfOrderRecmd();
        cmdParam.setStatus(EFFECTIVE);
        cmdParam.setRecmdPhone(member.getMemberLogin().getMemberLogingName());
        cmdParam.setConfId(orderRecmd.getConfId());
        TfOrderRecmd tfOrderRecmd = recmdMainService.getOrderRecmd(cmdParam);
        Boolean hasQr;
        if (tfOrderRecmd == null) {
            hasQr = false;
        } else {
            hasQr = true;
            model.addAttribute("orderRecmd", tfOrderRecmd);
        }
        model.addAttribute("hasQr", hasQr);
        return "web/goods/o2osimgrcode/qrmanage";
    }


    @RequestMapping("deleteQr")
    @ResponseBody
    public ResponseBean deleteQr(String recmdId) {
        ResponseBean responseBean = new ResponseBean();
        try {
            recmdMainService.logicalDelete(recmdId);
            responseBean.addSuccess();
        } catch (Exception e) {
            logger.error("删除推荐码失败", e);
            responseBean.addError("-1", "删除推荐码失败");
        }
        return responseBean;
    }

    @RequestMapping("genQr")
    @ResponseBody
    public ResponseBean genQr(HttpServletRequest request, HttpServletResponse response, TfOrderRecmd orderRecmd) throws Exception {
        ResponseBean responseBean = new ResponseBean();
        MemberVo member = UserUtils.getLoginUser(request);
        if (member == null) {
            loginPrompt(request, response, "单点登录超时");
        }
        try {
            TfH5SimConf confResult = checkH5SimConf(orderRecmd.getConfId());
            TfOrderRecmd orderRecmdNew = genQrcode(member, confResult);
            responseBean.addSuccess(orderRecmdNew);
        } catch (Exception e) {
            if (e instanceof EcsException) {
                EcsException e1 = (EcsException) e;
                logger.error("生成数据异常", e1);
                responseBean.addError("-1", e1.getFriendlyDesc());
            } else {
                logger.error("新增推荐码失败", e);
                responseBean = ExceptionUtils.dealExceptionRetMsg(responseBean, e, "新增推荐码失败");
            }
        }
        return responseBean;
    }

    @RequestMapping("o2OGroupGenRecmdQrCode")
    public void o2OGroupGenRecmdQrCode(String confId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        TfOrderRecmd recmdCondition = new TfOrderRecmd();
        TfH5SimConf confResult = null;
        String shopId = "";
        MemberVo member = UserUtils.getLoginUser(request);
        if (member == null) {
            loginPrompt(request, response, "单点登录超时");
        }
        //如果channelInfo为空会在O2oAppSsoInterceptor被拦截跳转到提示页面
        ChannelInfo channelInfo = member.getChannelInfo();
        confResult = checkH5SimConf(confId);
        String serialNumber = member.getMemberLogin().getMemberLogingName();
        try {
            recmdCondition.setRecmdPhone(serialNumber);
            recmdCondition.setRecmdBusiParam(confId);
            recmdCondition.setRcdConfId(confResult.getRcdConfId());
            //== 验证是否有效
            TfRecmdActConf recmdActConf = recmdMainService.getRecmdActConf(recmdCondition.getRcdConfId());
            if (recmdActConf == null) {
                throw new RuntimeException("对不起，推荐活动为空！");
            }
            String allow = dealRecmdAllow(recmdActConf);
            if (!"1".equals(allow)) {
                throw new RuntimeException("对不起，该推荐活动时间为：" + allow + "。敬请期待！");
            }
            recmdCondition.setConfId(confId);
            //根据TF_SYS_USER的NO关联T_DISTRIBUTION_STAFF的STAFF_ID得到的数据
            //T_DISTRIBUTION_STAFF.CHAN_ID 对应 TF_F_O2O_ACCOUNT_INFO的departId
            //T_DISTRIBUTION_STAFF.STAFF_ID 对应TF_F_O2O_ACCOUNT_INFO的STAFF_ID 对应TF_SYS_USER的NO字段
            recmdCondition.setChanId(channelInfo.getTradeStaffId());//COZZC 80827(这个对应TF_F_O2O_ACCOUNT_INFO的departId)
            recmdCondition.setRecEmpID(channelInfo.getTradeStaffId());//ITFWPMAL CY188158(这个对应TF_F_O2O_ACCOUNT_INFO的STAFF_ID)
            List<TfOrderRecmd> recmds = recmdMainService.getPage(recmdCondition).getList();
            TfOrderRecmd tfOrderRecmd;//新生成的土建对象
            //能依据条件查询到数据，说明已经生成了二维码
            if (recmds.size() > 0) {
                tfOrderRecmd = recmds.get(0);
            } else {
                tfOrderRecmd = genQrcode(member, confResult);
            }
            //订单的shopId通过和掌柜登录的login_name查询得到(shopId必须填准,不然生成报表会出问题)
            response.sendRedirect(tfOrderRecmd.getRecmdUserLink());
        } catch (Exception e) {
            if (e instanceof EcsException) {
                EcsException e1 = (EcsException) e;
                logger.error("生成数据异常", e1);
                loginPrompt(request, response, e1.getFriendlyDesc());
            } else {
                loginPrompt(request, response, "生成推荐链接失败!请重试!");
                logger.error("新增推荐码失败", e);
            }
        }
    }

    private TfH5SimConf checkH5SimConf(String confId) {
        TfH5SimConf cond = new TfH5SimConf();
        cond.setConfId(confId);
        List<TfH5SimConf> h5Confs = plansService.queryListCond(cond);
        if (h5Confs.size() == 0) {
            throw new RuntimeException(ConstantsBase.MY_EXCEP + "该号卡配置信息不存在！");
        }
        TfH5SimConf confResult = h5Confs.get(0);

        if (confResult.getConfStatus().equals(TfH5SimConf.ConfStatus.SC_OFFLINE.getValue())) {
            throw new RuntimeException(ConstantsBase.MY_EXCEP + "该号卡销售已经下线！");
        }
        String rcdConfId = confResult.getRcdConfId();
        if (StringUtils.isEmpty(rcdConfId)) {
            throw new RuntimeException(ConstantsBase.MY_EXCEP + "提交配置参数有误！");
        }
        return confResult;
    }


    private TfOrderRecmd genQrcode(MemberVo member, TfH5SimConf tfH5SimConf) {
        ChannelInfo channelInfo = member.getChannelInfo();
        //ChanID是company_boss_code RecEmpID是tf_sys_user的no字段对应的值
        String url = URL_PREFIX + "ChanID=" + channelInfo.getTradeDepartId() + "&RecEmpID=" + channelInfo.getTradeStaffId();
        Long recmdId = recmdMainService.getIdWorkId();
        String recmdCode = Base64.encode((recmdId + "").getBytes());
        url += "&recmdCode=" + recmdCode + "&confId=" + tfH5SimConf.getConfId();
        TfRecmdActConf actConf = recmdMainService.getRecmdActConf(tfH5SimConf.getRcdConfId());
        String styleColor = actConf.getConfMainColor().substring(1);
        byte[] bytes = QrCodeUtils.fetchQrCode(url, null, Integer.parseInt(styleColor, 16), 0xFFFFFFFF, 1);
        String fileName = TFSClient.uploadFile(bytes, "png", null);
        logger.info("tfs上传后的文件名：" + fileName);
        TfOrderRecmd orderRecmd = new TfOrderRecmd();

        orderRecmd.setRecmdUserId(member.getMemberLogin().getMemberId());
        orderRecmd.setRecmdUserName(member.getMemberLogin().getMemberLogingName());
        orderRecmd.setRecmdId(recmdId);
        orderRecmd.setRecmdUserLink(url);
        orderRecmd.setRecmdUserQrcode(fileName);
        orderRecmd.setRecmdPhone(member.getMemberLogin().getMemberLogingName());
        orderRecmd.setConfId(tfH5SimConf.getConfId());//要推荐的confId
        orderRecmd.setChanId(channelInfo.getTradeDepartId());
        orderRecmd.setRecEmpID(channelInfo.getTradeStaffId());
        orderRecmd.setStatus(EFFECTIVE);
        orderRecmd.setCityCode(member.getShopInfo().getShopCity());
        recmdMainService.saveOrderRecmd(orderRecmd);
        return orderRecmd;
    }

    private void loginPrompt(HttpServletRequest request, HttpServletResponse response, String prompt) throws Exception {
        String url = "/o2oTest/loginError?value=" + URLEncoder.encode(URLEncoder.encode(prompt, "utf-8"));
        String contextPath = request.getContextPath();
        String redirect = contextPath + url;
        response.sendRedirect(redirect);
    }


    private String dealRecmdAllow(TfRecmdActConf conf) {
        Date now = new Date();
        if (conf == null) {
            return "-1";//没有任何与推荐有关的数据
        } else if (now.after(conf.getBeginTime()) && now.before(conf.getEndTime())) {
            return "1";//满足推荐条件
        }
        return DateUtils.formatDateHMS(conf.getBeginTime()) + "~" + DateUtils.formatDateHMS(conf.getEndTime());//有推荐配置，但是未在推荐时间范围内
    }
}
