package com.ai.ecs.ecm.mall.wap.modules.goods.controller;

import com.ai.ecs.common.ResponseBean;
import com.ai.ecs.common.utils.Collections3;
import com.ai.ecs.common.utils.DateUtils;
import com.ai.ecs.common.utils.JedisClusterUtils;
import com.ai.ecs.ecm.mall.wap.modules.goods.utils.HttpRequest;
import com.ai.ecs.ecm.mall.wap.modules.goods.utils.Utility;
import com.ai.ecs.ecm.mall.wap.platform.utils.CookieUtils;
import com.ai.ecs.ecm.mall.wap.platform.utils.DictUtil;
import com.ai.ecs.ecm.mall.wap.platform.utils.StringUtils;
import com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils;
import com.ai.ecs.ecsite.modules.sms.entity.SmsSendCondition;
import com.ai.ecs.ecsite.modules.sms.service.SmsSendService;
import com.ai.ecs.entity.base.ConstantsBase;
import com.ai.ecs.entity.base.Page;
import com.ai.ecs.goods.api.IPlansService;
import com.ai.ecs.goods.entity.TfH5SimConf;
import com.ai.ecs.member.entity.MemberVo;
import com.ai.ecs.order.api.IOrderQueryService;
import com.ai.ecs.order.api.recmd.IFlowCouponsGiveService;
import com.ai.ecs.order.api.recmd.IMallSmsSendService;
import com.ai.ecs.order.api.recmd.IRecmdMainService;
import com.ai.ecs.order.entity.TfOrderDetailSim;
import com.ai.ecs.order.entity.TfOrderSub;
import com.ai.ecs.order.entity.recmd.TfCouponsConf;
import com.ai.ecs.order.entity.recmd.TfFlowcouponsGive;
import com.ai.ecs.order.entity.recmd.TfMallSmsSend;
import com.ai.ecs.order.entity.recmd.TfOrderFirstCharge;
import com.ai.ecs.order.entity.recmd.TfOrderRecmd;
import com.ai.ecs.order.entity.recmd.TfOrderRecmdRef;
import com.ai.ecs.order.entity.recmd.TfRecmdActConf;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.json.JSONObject;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 商城推荐功能公共化
 * Created by hewei on 2017/11/27/027.
 */
@Controller
@RequestMapping("recmd")
public class RecmdSimController extends RecmdBaseController {

    @Autowired
    IRecmdMainService recmdMainService;
    @Autowired
    IFlowCouponsGiveService flowCouponsGiveService;
    @Autowired
    @Value("${recmdSimQrcodeDomain}")
    String recmdSimQrcodeDomain;
    @Autowired
    IPlansService plansService;
    @Autowired
    private SmsSendService smsSendService;
    @Autowired
    private IMallSmsSendService mallSmsSendService;
    @Autowired
    private IOrderQueryService iOrderQueryService;//查询号卡信息


    private final static String SMS_NOTICE_PREFIX = "SMS_NOTICE_PREFIX";



    private static String SIM_RECMD_SMS_VERI_CODE = "SIM_RECMD_SMS_VERI_CODE_"; //短信验证码标记
    private static String SIM_RECMD_SMS_VERI_CODE_TIME = "SIM_RECMD_SMS_VERI_CODE_TIME_"; //短信验证码过期时间标记
    private final String RECMD_PHONE="recmdPhone";
    /**
     * 跳转手机号sim卡号互查询页面
     */
    @RequestMapping("phoneSimExchange")
    public String phoneSimExchange(HttpServletRequest request, HttpServletResponse response, Model model){
        return  "web/goods/sim/phoneSimExchange";
    }
    /**
     * 查询
     */
    @RequestMapping(value = "/findPhoneSim")
    @ResponseBody
    public String findPhoneSim(HttpServletRequest request, HttpServletResponse response, Model model) {
        String JsonObject = null;
        String psptId = request.getParameter("psptId");
        TfOrderDetailSim tfOrderDetailSim = new TfOrderDetailSim();
        if(psptId.length() > 11){
            tfOrderDetailSim.setIccid(psptId);
        }else if(psptId.length() == 11){
            tfOrderDetailSim.setPhone(psptId);
        }
        try {
            List<TfOrderDetailSim> list = new ArrayList<TfOrderDetailSim>();
            if(psptId.length() >= 11){
                list =  iOrderQueryService.queryFindDefault(tfOrderDetailSim);
            }
            ObjectMapper objectMapper = new ObjectMapper();
            JsonObject = objectMapper.writeValueAsString(list);
            logger.info("--JsonObject--");
            logger.info(JsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JsonObject;
    }
    /**
     * 跳转我要推荐链接生成
     */
    @RequestMapping("toGenSimRecmdLink")
    public String toGenRecmdLink(TfOrderRecmd orderRecmd, HttpServletRequest request,Model model) throws Exception {
        //取session中存在的号码
        Session session = UserUtils.getSession();
        String phone=null;
        try {
             phone =  null!=session.getAttribute("recmdPhone")?session.getAttribute("recmdPhone").toString():"";
        }catch(Exception e){
            e.getStackTrace();
        }
        if(StringUtils.isNotEmpty(phone)){
            ResponseBean bean=commonIsGenQrcode(orderRecmd.getRcdConfId(),phone,orderRecmd.getRecmdBusiParam());
            if(("0").equals(bean.getCode())){
                //直接到推荐页面
                orderRecmd.setRecmdPhone(phone);
               return dir2MyQrcode(model,orderRecmd);
            }
        }
        common2GenRecmdLink(orderRecmd, request, model);
        return "web/goods/sim/recmd/genRecmdLink";
    }

    /**
     * 微厅跳转我要推荐链接生成
     */
    @RequestMapping("toGenSimRecmdLinkWei")
    public String toGenRecmdLinkWei(TfOrderRecmd orderRecmd, HttpServletRequest request,Model model) throws Exception {
        //取session中存在的号码
        Session session = UserUtils.getSession();
        String phone=null;
        try {
            phone = null!=session.getAttribute("recmdPhone")?session.getAttribute("recmdPhone").toString():"";
        }catch (Exception e){
            e.getStackTrace();
        }
        if(StringUtils.isNotEmpty(phone)){
            orderRecmd.setConfIds(Arrays.asList(new String[]{"2002","E007RSENMV" }));
            ResponseBean bean=commonIsGenQrcodeWei(phone,orderRecmd,model);
            if(("0").equals(bean.getCode())){
                //直接到推荐页面
                logger.info("进入了");
                orderRecmd.setRecmdPhone(phone);
                return dir2MyQrcodeWei(model,orderRecmd);
            }
        }
        logger.info("没进入");
        common2GenRecmdLinkWei(orderRecmd, request, model);
        return "web/goods/sim/recmd/genRecmdLink";
    }




    /**
     * 点击验证码异步请求 是否已经生成过二维码：每个渠道生成一个二维码
     * 大部分的号码校验在这里完成
     * code：链接参数
     * phone：生成推荐信息的手机号码
     */
    @RequestMapping("isGenedQrcode")
    @ResponseBody
    public Object isGenedQrcode(String rcdConfId, String phone,String confId,HttpServletResponse response,HttpServletRequest request) throws Exception {
        Session session = UserUtils.getSession();
        String cookiePhone= CookieUtils.getCookie(request,RECMD_PHONE);
        ResponseBean bean=new ResponseBean();
        bean = commonIsGenQrcode(rcdConfId, phone, confId);
        if(!phone.equals(cookiePhone) && "0".equals(bean.getCode())) {
            bean.addSuccess("1", phone, phone);
        }
        if (("0").equals(bean.getCode())) {
            session.setAttribute("recmdPhone", phone);
        }

        return bean;
    }
    /**
     * 微厅跳转推荐
     * 点击验证码异步请求 是否已经生成过二维码：每个渠道生成一个二维码
     * 大部分的号码校验在这里完成
     * code：链接参数
     * phone：生成推荐信息的手机号码
     */
    @RequestMapping("isGenedQrcodeWei")
    @ResponseBody
    public Object isGenedQrcodeWei( String phone,HttpServletResponse response,Model model,HttpServletRequest request) throws Exception {
        Session session = UserUtils.getSession();
        String cookiePhone= CookieUtils.getCookie(request,RECMD_PHONE);
        ResponseBean bean=new ResponseBean();
        TfOrderRecmd orderRecmd = new TfOrderRecmd();
        orderRecmd.setConfIds(Arrays.asList(new String[]{"2002", "E007RSENMV"}));
        bean = commonIsGenQrcodeWei(phone, orderRecmd, model);
        if(!phone.equals(cookiePhone) && "0".equals(bean.getCode())) {
            bean.addSuccess("1", phone, phone);
        }
        if (("0").equals(bean.getCode())) {
            session.setAttribute("recmdPhone", phone);
        }
        return bean;
    }


    /**
     * 已经生成二维码的信息：直接跳转二维码结果页面
     */
    @RequestMapping("dir2MyQrcode")
    public String dir2MyQrcode(Model model, TfOrderRecmd recmd) {
        if(null==recmd.getRecmdPhone()){

            return "web/goods/sim/recmd/genRecmdLink";
        }
        recmd.setConfId(recmd.getRecmdBusiParam());
        commonDir2MyQrcode(model, recmd);

        return "web/goods/sim/recmd/myRecmdQrCode";
    }
    /**
     * 微厅已经生成二维码的信息：直接跳转二维码结果页面
     */
    @RequestMapping("dir2MyQrcodeWei")
    public String dir2MyQrcodeWei(Model model, TfOrderRecmd recmd) {
        if(null==recmd.getRecmdPhone()){
            return "web/goods/sim/recmd/genRecmdLink";
        }
        commonDir2MyQrcodeWei(model, recmd);
        model.addAttribute("weixin",true);
        return "web/goods/sim/recmd/myRecmdQrCode";
    }


    /**
     * 调用短信接口生成短信验证码
     */
    @RequestMapping("genSmsVerifyCode")
    @ResponseBody
    public Object genSmsVerifyCode(String recmdPhone) {
        return commonGenSmsVerifyCode(recmdPhone);
    }

    /**
     * 号卡推荐 生成推荐二维码
     */
    @RequestMapping("genRecmdQrCode")
    public String genSimRecmdQrCode(Model model, TfOrderRecmd orderRecmd, HttpServletResponse response) throws Exception {


        commonGenRecmdQrcode(orderRecmd,model);
        Session session = UserUtils.getSession();
        session.setAttribute("recmdPhone", orderRecmd.getRecmdPhone());
        int secondsLeftToday = (int) (86400 - DateUtils.getFragmentInSeconds(Calendar.getInstance(), Calendar.DATE));
        CookieUtils.setCookie(response,RECMD_PHONE,orderRecmd.getRecmdPhone(),secondsLeftToday);
        return "web/goods/sim/recmd/myRecmdQrCode";
    }
    /**
     * 微厅号卡推荐 生成推荐二维码
     */
    @RequestMapping("genRecmdQrCodeWei")
    public String genSimRecmdQrCodeWei(Model model, TfOrderRecmd orderRecmd, HttpServletResponse response) throws Exception {
        TfH5SimConf tfH5SimConf = new TfH5SimConf();
        tfH5SimConf.setConfIds( Arrays.asList(new String[]{"2002","E007RSENMV" }));
        List<TfH5SimConf> h5SimConf =  plansService.selectByConfIds(tfH5SimConf);
        if (!Collections3.isEmpty(h5SimConf)){
            for(TfH5SimConf simConf:h5SimConf){
                    orderRecmd.setRcdConfId(simConf.getRcdConfId());
                    orderRecmd.setRecmdBusiParam(simConf.getConfId());
                    commonGenRecmdQrcodeWei(orderRecmd,model);
                    if(simConf.getConfId().equals("2002")){
                        model.addAttribute("h5SimConf0",simConf);
                    }else if(simConf.getConfId().equals("E007RSENMV")){
                        model.addAttribute("h5SimConf1",simConf);
                    }
            }

        }
        Session session = UserUtils.getSession();
        session.setAttribute("recmdPhone", orderRecmd.getRecmdPhone());
        int secondsLeftToday = (int) (86400 - DateUtils.getFragmentInSeconds(Calendar.getInstance(), Calendar.DATE));
        CookieUtils.setCookie(response,RECMD_PHONE,orderRecmd.getRecmdPhone(),secondsLeftToday);
        model.addAttribute("weixin",true);
        return "web/goods/sim/recmd/myRecmdQrCode";
    }


    /**
     * 跳转了解更多详情页面
     * @param
     * @return
     */
    @RequestMapping(value="/getMoreDetail" )
    public String getMoreDetail( HttpServletRequest request,Model model){

        return "web/goods/sim/recmd/kingDetail";
    }

    /**
     * 解析TfOrderRecmd：可能存在很多个链接参数需要往后面传值，然后从中间分离出来recmdConfId
     * 默认不处理
     */
    @Override
    public TfOrderRecmd beforeToGenRecmdLink(TfOrderRecmd recmd) {
        return recmd; //链接参数只有confId，无需单独处理
    }

    /**
     * 生成二维码之前处理
     */
    @Override
    public TfOrderRecmd beforeGenQrcode(TfOrderRecmd recmd) {
        if(recmd == null )
            recmd =  new TfOrderRecmd();
        recmd.setRecmdUserLink(recmdSimQrcodeDomain + "/simBuy/simH5OnlineToApply?confId=");
        recmd.setSmsVerifyRedisFlag(SIM_RECMD_SMS_VERI_CODE);
        recmd.setSmsVerifyOutTimeRedisFlag(SIM_RECMD_SMS_VERI_CODE_TIME);
        TfRecmdActConf actConf = recmdMainService.getRecmdActConf(recmd.getRcdConfId());
       
        if(actConf ==null){
            actConf = new TfRecmdActConf();
        }
        actConf.setRcdSms("感谢您参与号卡推广活动，您的验证码是：${arg0}，验证码2分钟内有效，如非本人操作，请忽略本信息。【中国移动】)");
        recmd.setRecmdActConf(actConf);
        return recmd;
    }

    /**
     *，不过可以写个默认的实现
     * 该部分无法公共化，必须写成架构中的抽象类
     * @param model
     * @param orderRecmd：必须有RecmdPhone/RecmdId
     */
     @Override
     public Model dealRecmdShowInfo(Model model, TfOrderRecmd orderRecmd) {
        //== 统计推荐人数/成功人数：口径是所有不是当月|所有号卡类型
        TfOrderRecmdRef refCond = new TfOrderRecmdRef();
        TfOrderRecmd recmd = new TfOrderRecmd();
        recmd.setRecmdPhone(orderRecmd.getRecmdPhone());
        refCond.setOrderRecmd(recmd);
        List<TfOrderRecmdRef> refs = recmdMainService.getListSimActed(refCond);
        int acted = 0;
        for (TfOrderRecmdRef ref : refs) {
            if (ref.getOrderSimActed() != null) {
                acted++;
            }
        }
        //== 查询流量赠送信息：只查询号卡推荐+激活|当月的|赠送成功。是否要定位到具体的号卡业务（刘杰说不需要）
        TfFlowcouponsGive flowcoupons = new TfFlowcouponsGive();
        flowcoupons.setSerialNumber(orderRecmd.getRecmdPhone());
        flowcoupons.setGiveStatus(TfFlowcouponsGive.FLOW_COPON_STATUS_GIVE_SUC);
        List condList = new ArrayList();
        condList.add(TfCouponsConf.COPONS_ID_FLOW_SIM_RECMD);
        condList.add(TfCouponsConf.COPONS_ID_FLOW_SIM_RECMD_ACTED);
        flowcoupons.setCouponsIds(condList);//推荐+激活
        flowcoupons.setStorageBeginTime(DateUtils.parseDate(DateUtils.getDate("yyyy-MM")));
        //订单数据
        Page<TfFlowcouponsGive> flowPage = flowCouponsGiveService.findPage(flowcoupons);
        int recmdReward = 0, actedReward = 0;
        for (TfFlowcouponsGive tmp : flowPage.getList()) {
            TfCouponsConf conf = tmp.getCouponsConf();
            if (tmp.getCouponsId() == TfCouponsConf.COPONS_ID_FLOW_SIM_RECMD) {
                recmdReward += conf.getCouponsValue();
            } else if (tmp.getCouponsId() == TfCouponsConf.COPONS_ID_FLOW_SIM_RECMD_ACTED) {
                actedReward += conf.getCouponsValue();
            }
        }
        //推荐人数/成功人数:推荐获取流量|赠送获取流量
        model.addAttribute("recmded", refs.size());
        model.addAttribute("acted", acted);
        model.addAttribute("recmdReward", recmdReward);
        model.addAttribute("actedReward", actedReward / 1024);
        return model;
    }


    /**
     * O2O号卡推荐 生成推荐二维码，直接跳转生成后页面
     * recmd/o2OGenRecmdQrCode?confId=2002
     */
    @RequestMapping("o2OGenRecmdQrCode")
    public void o2OGenRecmdQrCode(Model model,TfH5SimConf cond,HttpServletRequest request,HttpServletResponse response) throws Exception {
        TfOrderRecmd orderRecmd = new TfOrderRecmd();
        TfH5SimConf confResult = null;
        String shopId="";
        try {
            //取单点登录后用户信息
            MemberVo member = UserUtils.getLoginUser(request);
            if (member != null) {
                String serialNumber = member.getMemberLogin().getMemberLogingName();
                orderRecmd.setRecmdPhone(serialNumber);

                if (cond.getConfId() == null || cond.getConfId().length() == 0) {
                    throw new RuntimeException(ConstantsBase.MY_EXCEP + "套餐信息配置参数错误！");
                }
                List<TfH5SimConf> h5Confs = plansService.queryListCond(cond);
                if (h5Confs.size() == 0) {
                    throw new RuntimeException(ConstantsBase.MY_EXCEP + "该号卡配置信息不存在！");
                }
                confResult = h5Confs.get(0);

                if (confResult.getConfStatus().equals(TfH5SimConf.ConfStatus.SC_OFFLINE.getValue())) {
                    throw new RuntimeException(ConstantsBase.MY_EXCEP + "该号卡销售已经下线！");
                }

                String rcdConfId = confResult.getRcdConfId();
                if (StringUtils.isEmpty(rcdConfId)) {
                    throw new RuntimeException(ConstantsBase.MY_EXCEP + "提交配置参数有误！");
                }

                orderRecmd.setRecmdBusiParam(cond.getConfId());
                orderRecmd.setRcdConfId(rcdConfId);
                //== 验证是否有效
                TfRecmdActConf recmdActConf = recmdMainService.getRecmdActConf(orderRecmd.getRcdConfId());
                if (recmdActConf == null) {
                    throw new RuntimeException(ConstantsBase.MY_EXCEP + "对不起，推荐活动为空！");
                }
                String allow = isAllowRecmdV2(recmdActConf);
                if (!"1".equals(allow)) {
                    throw new RuntimeException(ConstantsBase.MY_EXCEP + "对不起，该推荐活动时间为：" + allow + "。敬请期待！");
                }
                orderRecmd.setRecmdActConf(recmdActConf);
                shopId=member.getShopInfo().getShopId();
            }else{
                String url = "/o2oTest/loginError?value="+ URLEncoder.encode(URLEncoder.encode("单点登录超时","utf-8"));
                String contextPath = request.getContextPath();
                StringBuilder redirect = new StringBuilder();
                redirect.append(contextPath).append(url);
                response.sendRedirect(redirect.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            splitException(e, "对不起，推荐链接不合法");
        }

        orderRecmd.setConfId(cond.getConfId());
        List<TfOrderRecmd> recmds = recmdMainService.getPage(orderRecmd).getList();
        //能依据条件查询到数据，说明已经生成了二维码
        if (recmds.size() > 0) {
            orderRecmd=recmds.get(0);
        }else{
            commonGenRecmdQrcodeByO2O(orderRecmd,model,request);
        }
        if("15".equals(confResult.getPageCode())){
            response.sendRedirect(orderRecmd.getRecmdUserLink()+"&CHANID=E050&TYPE=O2O&shopId="+shopId);
        }
        response.sendRedirect(orderRecmd.getRecmdUserLink()+"&CHANID=E050&shopId="+shopId);
    }

    @RequestMapping("o2OSimIndex")
    public String o2OSimIndex(Model model,TfH5SimConf cond,HttpServletRequest request,HttpServletResponse response) throws Exception {
        //取单点登录后用户信息
        MemberVo member = UserUtils.getLoginUser(request);
        if (member == null) {
            String url = "/o2oTest/loginError?value="+ URLEncoder.encode(URLEncoder.encode("单点登录超时","utf-8"));
            String contextPath = request.getContextPath();
            StringBuilder redirect = new StringBuilder();
            redirect.append(contextPath).append(url);
            response.sendRedirect(redirect.toString());
        }
        return "web/goods/sim/o2oSimIndex";
    }

    /**
     * 宽带业务预约列表
     * @param model
     * @return
     */
    @RequestMapping("o2oHaoduankaIndex")
    public String broadBandIndex(Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
        MemberVo memberVo = UserUtils.getLoginUser(request);
        if (memberVo == null) {
            ssoTimeout(request,response);
        }
        String shopId=memberVo.getShopInfo().getShopId();
        String recmdPhone = memberVo.getMemberLogin().getMemberLogingName();
        String[] confIds = DictUtil.getDictValue("HAODUANKA","O2O_qyyx","XYYXDWK;XYYXRSENMV8").split(";");
        List<TfH5SimConf> simConfList = new ArrayList<>();
        for(String confId : confIds){
            TfOrderRecmd orderRecmd = new TfOrderRecmd();
            TfH5SimConf cond = new TfH5SimConf();
            cond.setConfId(confId);
            orderRecmd.setConfId(confId);
            orderRecmd.setRecmdPhone(recmdPhone);
            List<TfH5SimConf> h5Confs = plansService.queryListCond(cond);
            TfH5SimConf h5Conf = h5Confs.get(0);
            String templateId = DictUtil.getDictValue(h5Conf.getCardType(),"O2O_qyyx","2018080801");
            List<TfOrderRecmd> recmds = recmdMainService.getPage(orderRecmd).getList();
            //能依据条件查询到数据，说明已经生成了二维码
            if (recmds.size() > 0) {
                orderRecmd=recmds.get(0);
            }else{
                orderRecmd.setRecmdBusiParam(confId);
                orderRecmd.setRcdConfId(h5Conf.getRcdConfId());
                TfRecmdActConf recmdActConf = recmdMainService.getRecmdActConf(orderRecmd.getRcdConfId());
                orderRecmd.setRecmdActConf(recmdActConf);
                commonGenRecmdQrcodeByO2O(orderRecmd,model,request);
            }
            h5Conf.setTemplateId(templateId);
            h5Conf.setRecmdUserLink(orderRecmd.getRecmdUserLink()+"&shopId="+shopId);
            simConfList.add(h5Conf);
        }
        model.addAttribute("recmds",simConfList);
        return "web/goods/sim/o2oHaoduanIndex";

    }

    @RequestMapping("o2OSimRedirect")
    public void o2OSimRedirect(HttpServletRequest request,HttpServletResponse response) throws Exception {
        //取单点登录后用户信息
        MemberVo member = UserUtils.getLoginUser(request);
        if (member == null) {
            ssoTimeout(request,response);
        }
        response.sendRedirect(request.getContextPath()+"/o2oSimPreBuy/realNameRegistry?CHANID=E050");
    }

    private void ssoTimeout(HttpServletRequest request,HttpServletResponse response) throws Exception{
        String url = "/o2oTest/loginError?value="+ URLEncoder.encode(URLEncoder.encode("单点登录超时","utf-8"));
        String contextPath = request.getContextPath();
        StringBuilder redirect = new StringBuilder();
        redirect.append(contextPath).append(url);
        response.sendRedirect(redirect.toString());
    }

        /**
         * 功能简述 修改分享url
         *
         * @param request
         * @return
         * @author：qianman@asiainfo.com
         * @create：2016年8月9日 下午1:58:54
         */
    @RequestMapping(value = "ticketSignature")
    @ResponseBody
    public Map<String, String> ticketSignature(HttpServletRequest request) {
        Map<String, String> map = new HashMap<String, String>();
        JSONObject json = new JSONObject();
        String url = request.getParameter("url");// 获取分享的url
        String nonceStr = UUID.randomUUID().toString().replaceAll("-", "");
        json.put("nonceStr", nonceStr);//随机数
        json.put("url", url);
        json.put("chanId", "ZZZZ");//
        String paramSign=nonceStr+"a65da37b5f0cbea6a6fc20d1d3f0bab6";
        String sign= Utility.getMd5(paramSign);
        json.put("sign", sign);// 微信号
        JSONObject jsonObject = HttpRequest.sendRequest("http://wechat.page.com/wm/ExternalInterface/getShareParam", json);
        String ticket="";
        String noncestr="";
        String timestamp="";
        String appId="";
        String signature="";
        if("0".equals(jsonObject.get("resultCode")+"")){
            ticket=jsonObject.get("ticket")+"";
            noncestr=jsonObject.get("noncestr")+"";
            timestamp=jsonObject.get("timestamp")+"";
            appId=jsonObject.get("appId")+"";
            signature=jsonObject.get("signature")+"";
        }
        map.put("nonceStr", noncestr);
        map.put("url", url);
        map.put("timeStamp", timestamp);
        map.put("signature", signature);// 转换为小写
        map.put("appId", appId);// 公众号Id
        return map;
    }

    /**
     *我的邀请记录
     */
    @RequestMapping(value = "myInvitationRecord")
    public String myInvitationRecord(HttpServletResponse response,Model model){
        Session session = UserUtils.getSession();
        if(session.getAttribute("recmdPhone") == null){
            return "redirect:"+"toGenSimRecmdLinkWei";
        }
        String phone = session.getAttribute("recmdPhone").toString();
        TfOrderRecmd orderRecmd = new TfOrderRecmd();
        orderRecmd.setRecmdPhone(phone);
        Page pege=new Page();
        pege.setPageSize(500);
        orderRecmd.setPage(pege);
        List<TfOrderFirstCharge> orderRecmdRefList = recmdMainService.getListSimDetail(orderRecmd);
        int charge = 0;
        if(!Collections3.isEmpty(orderRecmdRefList)){
            for(TfOrderFirstCharge tfOrderFirstCharge:orderRecmdRefList){
                if(null==tfOrderFirstCharge.getOrderFirstCharge()||"".equals(tfOrderFirstCharge.getOrderFirstCharge())) {
                    charge = charge+1;
                }
                String recmdedPhone = tfOrderFirstCharge.getPhone();//被推荐人所购买的号码
                if(recmdedPhone!=null ){
                    recmdedPhone = recmdedPhone.replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2");
                    tfOrderFirstCharge.setPhone(recmdedPhone);
                }

            }
            model.addAttribute("orderRecmdRefList",orderRecmdRefList);
        }
        model.addAttribute("size",orderRecmdRefList.size());
        model.addAttribute("charge",charge);
        return "web/goods/sim/recmd/myInviteRecord";
    }
    /**
     * 提醒充值,
     */
    @RequestMapping(value = "noticeSms")
    @ResponseBody
    public ResponseBean noticeSms(HttpServletResponse response){
        ResponseBean resp = new ResponseBean();
        Session session = UserUtils.getSession();
        if(session.getAttribute("recmdPhone") == null){
            resp.addError("-3","登录超时,请重新前往登录");
            return resp;
        }
        String phone = session.getAttribute("recmdPhone").toString();
        if(JedisClusterUtils.get(SMS_NOTICE_PREFIX+phone) != null){
            resp.addError("-2","正在发送短信,请勿重复提交!");
            return resp;
        }
        JedisClusterUtils.set(SMS_NOTICE_PREFIX+phone,phone,5*60);
        TfOrderRecmd orderRecmd = new TfOrderRecmd();
        orderRecmd.setRecmdPhone(phone);
        List<TfOrderFirstCharge> orderRecmdRefList = recmdMainService.getListSimDetail(orderRecmd);

        if(!Collections3.isEmpty(orderRecmdRefList)){
            for(TfOrderFirstCharge firstCharge:orderRecmdRefList){
                //首冲是空发送首冲提醒,且被推荐的号码已经激活才发送短信
                if((null == firstCharge.getOrderFirstCharge()) && firstCharge.getActStatus() != null ){
                    if(null==firstCharge.getMallSmsSend()) {
                        //1、先保存到短信表中
                        TfMallSmsSend mallSmsSend1 = new TfMallSmsSend();
                        mallSmsSend1.setSerialNumber(firstCharge.getPhone());//被推荐人号码
                        mallSmsSend1.setRecmdPhone(phone);//推荐人号码
                        mallSmsSend1.setStatus("0");
                        Calendar calendar = Calendar.getInstance();
                        int month = calendar.get(Calendar.MONTH) + 1;
                        mallSmsSend1.setMonth(month);
                        mallSmsSendService.saveSms(mallSmsSend1);
                    }
                        //2、查询需要发送短信的号码
                        TfMallSmsSend mallSmsSend2 = new TfMallSmsSend();
                        mallSmsSend2.setRecmdPhone(phone);
                        mallSmsSend2.setStatus("0");
                        List<TfMallSmsSend> mallSmsSendList = mallSmsSendService.selectByStatus(mallSmsSend2);
                        if (!Collections3.isEmpty(mallSmsSendList)) {
                            try{
                                for (TfMallSmsSend smsSend : mallSmsSendList) {
                                    String smsContent = "亲爱的王者，您的好友温馨提示您，现在给您的号码首次充值50元得100元话费哦，立即充值（ http://dx.10086.cn/UYjYbeq ）享福利！【湖南移动】";
                                    SmsSendCondition sms = new SmsSendCondition();
                                    sms.setNoticeContent(smsContent);
                                    //给新用户发送短信
                                    sms.setSerialNumber(firstCharge.getPhone());
                                    Map map = smsSendService.sendSms(sms);
                                    TfMallSmsSend mallSmsSend = new TfMallSmsSend();
                                    if (null != map && "0".equals(map.get("respCode"))) {
                                        mallSmsSend.setStatus("1");
                                        resp.addSuccess("提醒成功");
                                    } else {
                                        mallSmsSend.setStatus("0");
                                        resp.addError("-1","提醒失败");
                                    }
                                    mallSmsSend.setSmsContent(smsContent);
                                    mallSmsSend.setSendTime(new Date());
                                    Calendar calendar = Calendar.getInstance();
                                    int month = calendar.get(Calendar.MONTH) + 1;
                                    mallSmsSend.setMonth(month);
                                    mallSmsSend.setSmsSendId(smsSend.getSmsSendId());
                                    mallSmsSendService.updateStatus(mallSmsSend);
                                }
                            }catch (Exception e){
                                logger.error("短信发送失败",e);
                                resp.addError("-1","系统异常,短信发送失败!");
                            }finally {
                                JedisClusterUtils.del(SMS_NOTICE_PREFIX+phone);
                            }
                    }
                }
            }
        }
        JedisClusterUtils.del(SMS_NOTICE_PREFIX+phone);
        return resp;
    }

}
