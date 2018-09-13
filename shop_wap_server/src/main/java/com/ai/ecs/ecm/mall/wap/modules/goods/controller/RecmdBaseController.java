package com.ai.ecs.ecm.mall.wap.modules.goods.controller;

import com.ai.ecs.common.ResponseBean;
import com.ai.ecs.common.utils.Base64;
import com.ai.ecs.common.utils.Collections3;
import com.ai.ecs.common.utils.IdGen;
import com.ai.ecs.common.utils.TFSClient;
import com.ai.ecs.common.web.Servlets;
import com.ai.ecs.ecm.mall.wap.common.CommonParams;
import com.ai.ecs.ecm.mall.wap.modules.BaseController;
import com.ai.ecs.ecm.mall.wap.modules.goods.constant.HNanConstant;
import com.ai.ecs.ecm.mall.wap.platform.utils.DateUtils;
import com.ai.ecs.ecm.mall.wap.platform.utils.QrCodeUtils;
import com.ai.ecs.ecm.mall.wap.platform.utils.StringUtils;
import com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils;
import com.ai.ecs.ecop.give.service.O2oSendFlowService;
import com.ai.ecs.ecsite.modules.dobusiness.entity.HqChangeProdAndElemCondition;
import com.ai.ecs.ecsite.modules.dobusiness.service.FlowServeService;
//import com.ai.ecs.ecop.give.service.O2oSendFlowService;
import com.ai.ecs.ecsite.modules.myMobile.entity.BasicInfoCondition;
import com.ai.ecs.ecsite.modules.myMobile.service.BasicInfoQryModifyService;
import com.ai.ecs.ecsite.modules.sms.entity.SmsSendCondition;
import com.ai.ecs.ecsite.modules.sms.service.SmsSendService;
import com.ai.ecs.entity.base.ConstantsBase;
import com.ai.ecs.entity.base.Page;
import com.ai.ecs.exception.ExceptionUtils;
import com.ai.ecs.goods.api.IPlansService;
import com.ai.ecs.goods.entity.TfH5SimConf;
import com.ai.ecs.goods.entity.TfPlans;
import com.ai.ecs.member.api.IMemberLoginService;
import com.ai.ecs.member.entity.MemberLogin;
import com.ai.ecs.member.entity.MemberVo;
import com.ai.ecs.order.api.IOrderConfigService;
import com.ai.ecs.order.api.busi.ISimBusiService;
import com.ai.ecs.order.api.recmd.IFlowCouponsGiveService;
import com.ai.ecs.order.api.recmd.IRecmdMainService;
import com.ai.ecs.order.api.recmd.ITfChnlEmpnoInfoService;
import com.ai.ecs.order.entity.TfOrderConfig;
import com.ai.ecs.order.entity.TfOrderDetailSim;
import com.ai.ecs.order.entity.TfOrderSub;
import com.ai.ecs.order.entity.recmd.TfChnlEmpnoInfo;
import com.ai.ecs.order.entity.recmd.TfOrderRecmd;
import com.ai.ecs.order.entity.recmd.TfRecmdActConf;
import com.ai.ecs.utils.LogUtils;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.commons.collections.MapUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import redis.clients.jedis.JedisCluster;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 号卡Controller公共方法
 * Created by hewei on 2017/12/7/007.
 */
public abstract class RecmdBaseController extends BaseController{

    @Autowired
    BasicInfoQryModifyService basicInfoQryModifyService;
    @Autowired
    SmsSendService smsSendService;
    @Autowired
    IRecmdMainService recmdMainService;
    @Autowired
    IMemberLoginService memberLoginService;
    @Autowired
    JedisCluster jedisCluster;
    @Autowired
    ITfChnlEmpnoInfoService chnlEmpnoInfoService;
    @Autowired
    IFlowCouponsGiveService flowCouponsGiveService;
    @Autowired
    IOrderConfigService configService;
    @Value("${recmdSimQrcodeDomain}")
    String recmdSimQrcodeDomain;
    @Autowired
    ITfChnlEmpnoInfoService tfChnlEmpnoInfoService;
    @Autowired
    IPlansService plansService;

    @Autowired
    ISimBusiService simBusiService;

    @Autowired
    private FlowServeService flowServeService;



    /**
     * 跳转推荐页面之前处理
     */
    public abstract TfOrderRecmd beforeToGenRecmdLink(TfOrderRecmd recmd);

    /**
     * 生成二维码之前处理
     */
    public abstract TfOrderRecmd beforeGenQrcode(TfOrderRecmd recmd);

    /**
     * 处理推荐展示信息：这个必须依据业务来进行展示
     */
    public abstract Model dealRecmdShowInfo(Model model,TfOrderRecmd orderRecmd);


    /**
     * 产生推荐链接
     * @param orderRecmd
     * @param request
     * @param model
     * @param
     * @return
     * @throws Exception
     */
    public final Model common2GenRecmdLink(TfOrderRecmd orderRecmd, HttpServletRequest request,Model model) throws Exception {
        orderRecmd = beforeToGenRecmdLink(orderRecmd);
        try {
            //== 判断用户是否登录，以自动显示号码
            MemberVo member = UserUtils.getLoginUser(request);
            if (member != null) {
                String serialNumber = member.getMemberLogin().getMemberPhone().toString();
                if (serialNumber != null && !(serialNumber.length() > 0)) {
                    model.addAttribute("recmdPhone", serialNumber);
                }
            }
            if(StringUtils.isEmpty(orderRecmd.getRcdConfId())){
                throw new RuntimeException(ConstantsBase.MY_EXCEP+"提交配置参数有误！");
            }
            String allow = isAllowRecmdV2(orderRecmd.getRcdConfId());
            if(!"1".equals(allow)){
                throw new RuntimeException(ConstantsBase.MY_EXCEP + "对不起，该推荐活动时间为："+allow+"。敬请期待！");
            }
            //== 验证是否有效
            TfRecmdActConf recmdActConf = recmdMainService.getRecmdActConf(orderRecmd.getRcdConfId());
            if(recmdActConf == null){
                throw new RuntimeException(ConstantsBase.MY_EXCEP+"对不起，参数不合法！");
            }
            model.addAttribute("orderRecmd", orderRecmd);
            model.addAttribute("recmdActConf", recmdActConf);
        } catch (Exception e) {
            e.printStackTrace();
            splitException(e, "对不起，推荐链接不合法");
        }
        return model;
    }
    /**
     * 微厅产生推荐链接
     * @param orderRecmd
     * @param request
     * @param model
     * @param
     * @return
     * @throws Exception
     */
    public final Model common2GenRecmdLinkWei(TfOrderRecmd orderRecmd, HttpServletRequest request,Model model) throws Exception {
        orderRecmd = beforeToGenRecmdLink(orderRecmd);
        try {
            //== 判断用户是否登录，以自动显示号码
            MemberVo member = UserUtils.getLoginUser(request);
            if (member != null) {
                String serialNumber = member.getMemberLogin().getMemberPhone().toString();
                if (serialNumber != null && !(serialNumber.length() > 0)) {
                    model.addAttribute("recmdPhone", serialNumber);
                }
            }

            model.addAttribute("orderRecmd", orderRecmd);
        } catch (Exception e) {
            e.printStackTrace();
            splitException(e, "对不起，推荐链接不合法");
        }
        return model;
    }

    /**
     * 判断该用户在该业务是否已经生成二维码
     */
    public final ResponseBean commonIsGenQrcode(String rcdConfId, String phone,String confId) throws Exception {
        ResponseBean resp = new ResponseBean();
        String retPhone = "";
        //== 判断电话号码或者工号
        String regEx = "^1[34578]\\d{9}$";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(phone);
        if (!matcher.matches()) {
            TfChnlEmpnoInfo empnoInfo = new TfChnlEmpnoInfo();
            empnoInfo.setEmpNo(phone);
            Page<TfChnlEmpnoInfo> chnlEmpnoInfos = chnlEmpnoInfoService.queryListPage(empnoInfo);
            if (chnlEmpnoInfos.getList().size() > 0) {
                String tmp = chnlEmpnoInfos.getList().get(0).getEmpPhone();
                if (!StringUtils.isEmpty(tmp)) {
                    retPhone = tmp;
                } else {
                    resp.addError("对不起，您的工号对应手机号码未在系统中绑定关系！");
                    return resp;
                }
            } else {
                resp.addError("对不起，您的工号信息未在系统中，请确定工号是否正确！");
                return resp;
            }
        } else {
            retPhone = phone;
        }
        //== 校验移动号码
        BasicInfoCondition biCond = new BasicInfoCondition();
        biCond.setSerialNumber(retPhone);
        biCond.setxGetMode("0");
        biCond.setStaffId("ITFWAPNN");//默认值没有权限，所以显式赋值
        biCond.setTradeDepartPassword("225071");
        logger.info("====isGenedQrcode用户资料条件===>" + JSONObject.toJSONString(biCond));
        Map userInfo = basicInfoQryModifyService.queryUserBasicInfo(biCond);
        logger.info("====isGenedQrcode用户资料返回===>" + JSONObject.toJSONString(userInfo));
        String respCode = String.valueOf(userInfo.get("respCode"));
        if (isProEnv()) {
            if (!"0".equals(respCode)) {
                resp.addError("-2",(String)userInfo.get("respDesc"));
                return resp;
            }
        }
        //== 号码校验通过后
        //针对该类型号卡，用户是否已经生成过二维码：RecmdType,RecmdSecType,RecmdPhone
        resp.addSuccess("1", retPhone, retPhone);
        TfOrderRecmd recmdCond = new TfOrderRecmd();
        recmdCond.setRcdConfId(rcdConfId);
        recmdCond.setRecmdPhone(retPhone);
        recmdCond.setConfId(confId);
        List<TfOrderRecmd> recmds = recmdMainService.getPage(recmdCond).getList();
        //能依据条件查询到数据，说明已经生成了二维码，直接跳转
        if (recmds.size() > 0) {
            resp.addSuccess(retPhone);
        }
        return resp;
    }
    /**
     * 微厅判断该用户在该业务是否已经生成二维码
     */
    public final ResponseBean commonIsGenQrcodeWei( String phone,TfOrderRecmd orderRecmd,Model model) throws Exception {
        ResponseBean resp = new ResponseBean();
        String retPhone = "";
        //== 判断电话号码或者工号
        String regEx = "^1[34578]\\d{9}$";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(phone);
        if (!matcher.matches()) {
            TfChnlEmpnoInfo empnoInfo = new TfChnlEmpnoInfo();
            empnoInfo.setEmpNo(phone);
            Page<TfChnlEmpnoInfo> chnlEmpnoInfos = chnlEmpnoInfoService.queryListPage(empnoInfo);
            if (chnlEmpnoInfos.getList().size() > 0) {
                String tmp = chnlEmpnoInfos.getList().get(0).getEmpPhone();
                if (!StringUtils.isEmpty(tmp)) {
                    retPhone = tmp;
                } else {
                    resp.addError("对不起，您的工号对应手机号码未在系统中绑定关系！");
                    return resp;
                }
            } else {
                resp.addError("对不起，您的工号信息未在系统中，请确定工号是否正确！");
                return resp;
            }
        } else {
            retPhone = phone;
        }
        //== 校验移动号码
        BasicInfoCondition biCond = new BasicInfoCondition();
        biCond.setSerialNumber(retPhone);
        biCond.setxGetMode("0");
        biCond.setStaffId("ITFWAPNN");//默认值没有权限，所以显式赋值
        biCond.setTradeDepartPassword("225071");
        logger.info("====isGenedQrcode用户资料条件===>" + JSONObject.toJSONString(biCond));
        Map userInfo = basicInfoQryModifyService.queryUserBasicInfo(biCond);
        logger.info("====isGenedQrcode用户资料返回===>" + JSONObject.toJSONString(userInfo));
        String respCode = String.valueOf(userInfo.get("respCode"));
        if (isProEnv()) {
            if (!"0".equals(respCode)) {
                resp.addError("-2",(String)userInfo.get("respDesc"));
                return resp;
            }
        }
        //== 号码校验通过后
        //针对该类型号卡，用户是否已经生成过二维码：RecmdType,RecmdSecType,RecmdPhone
        resp.addSuccess("1", retPhone, retPhone);
        TfOrderRecmd recmdCond = new TfOrderRecmd();
        recmdCond.setRecmdPhone(retPhone);
        recmdCond.setConfIds(orderRecmd.getConfIds());
        recmdCond.setRcdConfIds(Arrays.asList("10002","7001"));
        List<TfOrderRecmd> recmds = recmdMainService.getRecmdWei(recmdCond);
        //能依据条件查询到数据，说明已经生成了二维码，直接跳转
        if (recmds.size() > 1) {
            //已经生成了一个二维码，还需生成第二个后再进行跳转
//            if(recmds.size() >= 2){
//                resp.addSuccess(retPhone);
//            }else{
//                for(TfOrderRecmd tfOrderRecmd:recmds){
//                    commonGenRecmdQrcodeWei(tfOrderRecmd,model);
//                }
                resp.addSuccess(retPhone);
//            }
        }
        return resp;
    }


    /**
     * 生成短信验证码
     */
    public final ResponseBean commonGenSmsVerifyCode(String recmdPhone){
        TfOrderRecmd recmd = beforeGenQrcode(null);
        logger.info("==genSmsVerifyCode请求条件==>" + JSONObject.toJSONString(recmdPhone));
        ResponseBean resp = new ResponseBean();
        try {
            //如果不满足手机号码格式，则查询工号数据表，找到对应的手机号码 -->对不起，没有查到该工号对应的手机号码
            String regEx = "^1[34578]\\d{9}$";
            Pattern pattern = Pattern.compile(regEx);
            Matcher matcher = pattern.matcher(recmdPhone);
            if (!matcher.matches()) {
                logger.info("校验结果1：" + recmdPhone + "是工号，不是手机号码");
                TfChnlEmpnoInfo empnoInfo = new TfChnlEmpnoInfo();
                empnoInfo.setEmpNo(recmdPhone);
                Page<TfChnlEmpnoInfo> chnlEmpnoInfos = tfChnlEmpnoInfoService.queryListPage(empnoInfo);
                if (chnlEmpnoInfos.getList().size() > 0) {
                    logger.info("工号对应手机号码为：" + chnlEmpnoInfos.getList().get(0).getEmpPhone());
                    recmdPhone = chnlEmpnoInfos.getList().get(0).getEmpPhone();
                } else {
                    throw new RuntimeException(ConstantsBase.MY_EXCEP + "对不起，没有查到该工号对应的手机号码");
                }
            } else {
                logger.info("校验结果2：" + recmdPhone + "是手机号码，不是工号");
            }
            //== TODO
            //如果不满足手机号码格式，则查询工号数据表，找到对应的手机号码 --> 对不起，没有查到该工号对应的手机号码
            //== 号码归属地，判断用户归属地是否为湖南移动（前面有教养，这里就不要进行校验了）

            //== 发送验证码短信
            SmsSendCondition smsSendCond = new SmsSendCondition();
            smsSendCond.setSerialNumber(recmdPhone);
            String verifyCode = (IdGen.randomLong() + "").substring(0, 6);
            String noticeContent = recmd.getRecmdActConf().getRcdSms().replace("${arg0}",verifyCode);
            //判断是否存在用户恶意短信验证码轰炸
            String allowTime = jedisCluster.get(recmd.getSmsVerifyOutTimeRedisFlag() + recmdPhone);
            if (!StringUtils.isEmpty(allowTime)) {
                throw new Exception(ConstantsBase.MY_EXCEP + "离上次验证码未超过60s，请稍后再试");
            }
            jedisCluster.set(recmd.getSmsVerifyOutTimeRedisFlag() + recmdPhone, "1");
            jedisCluster.expire(recmd.getSmsVerifyOutTimeRedisFlag() + recmdPhone, 60);
            smsSendCond.setNoticeContent(noticeContent);
            logger.info("====genSmsVerifyCode短信请求===>" + JSONObject.toJSONString(smsSendCond));
            Map smsSendRetMap = smsSendService.sendSms(smsSendCond);
            logger.info("====genSmsVerifyCode短信返回===>" + JSONObject.toJSONString(smsSendRetMap));
            if (isProEnv()) {
                if (!"0".equals(String.valueOf(smsSendRetMap.get("respCode")))) {
                    printKaInfo(recmdPhone + "genSmsVerifyCode短信异常" + JSONObject.toJSONString(smsSendRetMap));
                    throw new Exception(ConstantsBase.MY_EXCEP + "对不起，短信验证码发送失败！");
                }
            }
            //存redis，5min有效
            jedisCluster.set(recmd.getSmsVerifyRedisFlag() + recmdPhone, verifyCode);
            jedisCluster.expire(recmd.getSmsVerifyRedisFlag() + recmdPhone, 5 * 60);
            logger.info("短信验证码发送成功，验证码为：" + verifyCode);
            resp.addSuccess("短信验证码发送成功");
        } catch (Exception e) {
            e.printStackTrace();
            resp.addError("对不起，无法获取短信验证码！");
            ExceptionUtils.dealExceptionRetMsg(resp, e, "对不起，无法获取短信验证码！");
        }
        return resp;
    }

    /**
     * 直接跳转二维码界面
     */
    public final Model commonDir2MyQrcodeWei(Model model, TfOrderRecmd recmd) {
        try {
            Session session = UserUtils.getSession();
            session.setAttribute("recmdPhone", recmd.getRecmdPhone());
            recmd.setConfIds(Arrays.asList(new String[]{"2002", "E007RSENMV"}));
            recmd.setRcdConfIds(Arrays.asList("10002", "7001"));
            List<TfOrderRecmd> recmdRsts = recmdMainService.getRecmdWei(recmd);
            logger.info("==dir2MyQrcode结果==>" + JSONObject.toJSONString(recmdRsts));
            if (!Collections3.isEmpty(recmdRsts)) {
                for (TfOrderRecmd recmdRst : recmdRsts) {
                    if ("2002".equals(recmdRst.getConfId())) {
                        model.addAttribute("orderRecmd0", recmdRst);
                    }
                    if ("E007RSENMV".equals(recmdRst.getConfId())) {
                        model.addAttribute("orderRecmd1", recmdRst);
                    }
                    model.addAttribute("orderRecmd", recmdRst);
                    TfH5SimConf conf = new TfH5SimConf();
                    conf.setConfId(getQueryString(recmdRst.getRecmdUserLink(), "confId"));
                    List<TfH5SimConf> h5Confs = plansService.queryListCond(conf);
                    if (!Collections3.isEmpty(h5Confs)) {
                        for (TfH5SimConf conf2 : h5Confs) {
                            if ("2002".equals(conf2.getConfId())) {
                                model.addAttribute("h5SimConf0", conf2);
                                model.addAttribute("h5SimConf", conf2);
                            }
                            if ("E007RSENMV".equals(conf2.getConfId())) {
                                model.addAttribute("h5SimConf1", conf2);
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            logger.error("commonDir2MyQrcodeWei:",e);
        }
        return model;
    }
    /**
     * 直接跳转二维码界面
     */
    public final Model commonDir2MyQrcode(Model model, TfOrderRecmd recmd) {

        logger.info("==dir2MyQrcode条件==>" + JSONObject.toJSONString(recmd));
        List<TfOrderRecmd> recmdRsts = recmdMainService.getOrderRecmdWithConf(recmd);
        logger.info("==dir2MyQrcode结果==>" + JSONObject.toJSONString(recmdRsts));
        TfOrderRecmd recmdRst;
        if(recmdRsts.size() > 0 && recmdRsts.get(0) != null){
            recmdRst = recmdRsts.get(0);
            //dealRecmdShowInfo(model, recmdRst);
        }else {
            throw new RuntimeException("对不起，参数有误！");//数据有误
        }
        model.addAttribute("orderRecmd",recmdRst);

        TfH5SimConf conf=new TfH5SimConf();
        conf.setConfId(getQueryString(recmdRst.getRecmdUserLink(),"confId"));
        List<TfH5SimConf> h5Confs = plansService.queryListCond(conf);
        if(!Collections3.isEmpty(h5Confs)){
            conf = h5Confs.get(0);
        }
        model.addAttribute("h5SimConf",conf);
        return model;
    }


    public final String getQueryString(String link,String paramName){
        String regEx = "(^|&|/?)" + paramName + "=([^&]*)(&|$)";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(link);
        String paramValue="";
        while (matcher.find()){
            paramValue= matcher.group(2);
        }
        return paramValue;
    }



    /**
     * 生成推荐二维码
     */
    public final Model commonGenRecmdQrcodeWei(TfOrderRecmd orderRecmd,Model model) throws Exception {
        logger.info("1commonGenRecmdQrcode生成二维码参数："+JSONObject.toJSONString(orderRecmd));
        orderRecmd =  beforeGenQrcode(orderRecmd);
        logger.info("2commonGenRecmdQrcode生成二维码参数："+JSONObject.toJSONString(orderRecmd));
        try {
            //== 验证该手机号码对应的验证码
            String code = jedisCluster.get(orderRecmd.getSmsVerifyRedisFlag() + orderRecmd.getRecmdPhone());
            if(null==UserUtils.getSession().getAttribute("recmdPhone")) {
                if (code == null) {
                    throw new RuntimeException(ConstantsBase.MY_EXCEP + "对不起，验证码超时！");
                }

                if (!code.equals(orderRecmd.getSmsCode())) {
                    printKaInfo("输入验证码：" + orderRecmd.getSmsCode() + "|redis真实验证码：" + code);
                    throw new RuntimeException(ConstantsBase.MY_EXCEP + "对不起，您的验证码不正确！");
                }
            }

            TfOrderRecmd recmdCond = new TfOrderRecmd();
            recmdCond.setRcdConfId(orderRecmd.getRcdConfId());
            recmdCond.setRecmdPhone(orderRecmd.getRecmdPhone());
            recmdCond.setConfId(orderRecmd.getRecmdBusiParam());
            List<TfOrderRecmd> recmds = recmdMainService.getPage(recmdCond).getList();
            if(recmds.size()==0) {
                //== 生成并保存二维码图片：生成图片 > 上传图片
                Long recmdId = recmdMainService.getIdWorkId();
                String recmdCode = Base64.encode((recmdId + "").getBytes());
                String simSellLink = orderRecmd.getRecmdUserLink() + orderRecmd.getRecmdBusiParam() + "&recmdCode=" + recmdCode;
                String styleColor = orderRecmd.getRecmdActConf().getConfMainColor().substring(1);
                byte[] bytes = QrCodeUtils.fetchQrCode(simSellLink, null, Integer.parseInt(styleColor, 16), 0xFFFFFFFF, 1);
                //== 生成图片到TFS
                String fileName = TFSClient.uploadFile(bytes, "png", null);
                logger.info("tfs上传后的文件名：" + fileName);
                //保存记录
                MemberLogin member = memberLoginService.getByMobile(Long.valueOf(orderRecmd.getRecmdPhone()));
                if (member != null) {
                    orderRecmd.setRecmdUserId(member.getMemberId());
                    orderRecmd.setRecmdUserName(member.getMemberLogingName());
                } else {
                    orderRecmd.setRecmdUserId(Long.getLong(orderRecmd.getRecmdPhone()));
                }
                orderRecmd.setRecmdId(recmdId);
                orderRecmd.setRecmdUserLink(simSellLink);
                orderRecmd.setRecmdUserQrcode(fileName);
                orderRecmd.setConfId(orderRecmd.getRecmdBusiParam());
                orderRecmd = recmdMainService.saveOrderRecmd(orderRecmd);
            }else {
                orderRecmd = recmds.get(0);
                orderRecmd.setRecmdBusiParam(orderRecmd.getConfId());
            }
            if("2002".equals(orderRecmd.getRecmdBusiParam())){
                model.addAttribute("orderRecmd0", orderRecmd);
                model.addAttribute("orderRecmd", orderRecmd);
                TfH5SimConf conf=new TfH5SimConf();
                conf.setConfId(getQueryString(orderRecmd.getRecmdUserLink(),"confId"));
                List<TfH5SimConf> h5Confs = plansService.queryListCond(conf);
                if(!Collections3.isEmpty(h5Confs)){
                    conf = h5Confs.get(0);
                }
                model.addAttribute("h5SimConf",conf);
            }
            if("E007RSENMV".equals(orderRecmd.getRecmdBusiParam())){
                model.addAttribute("orderRecmd1", orderRecmd);
            }
            //== 推荐的结果信息
            //model = dealRecmdShowInfo(model, orderRecmd);

            //model.addAttribute("styleColor",orderRecmd.getRecmdActConf().getConfMainColor());
        } catch (Exception e) {
            e.printStackTrace();
            splitException(e, "对不起，推荐二维码生成失败");
        }
        return model;
    }

    public final Model commonGenRecmdQrcode(TfOrderRecmd orderRecmd,Model model) throws Exception {
        logger.info("1commonGenRecmdQrcode生成二维码参数："+JSONObject.toJSONString(orderRecmd));
        orderRecmd =  beforeGenQrcode(orderRecmd);
        logger.info("2commonGenRecmdQrcode生成二维码参数："+JSONObject.toJSONString(orderRecmd));
        try {
            //== 验证该手机号码对应的验证码
            String code = jedisCluster.get(orderRecmd.getSmsVerifyRedisFlag() + orderRecmd.getRecmdPhone());
            if(null==UserUtils.getSession().getAttribute("recmdPhone") ||
                    !orderRecmd.getRecmdPhone().equals(UserUtils.getSession().getAttribute("recmdPhone"))) {
                if (code == null) {
                    throw new RuntimeException(ConstantsBase.MY_EXCEP + "对不起，验证码超时！");
                }

                if (!code.equals(orderRecmd.getSmsCode())) {
                    printKaInfo("输入验证码：" + orderRecmd.getSmsCode() + "|redis真实验证码：" + code);
                    throw new RuntimeException(ConstantsBase.MY_EXCEP + "对不起，您的验证码不正确！");
                }
            }

            TfOrderRecmd recmdCond = new TfOrderRecmd();
            recmdCond.setRcdConfId(orderRecmd.getRcdConfId());
            recmdCond.setRecmdPhone(orderRecmd.getRecmdPhone());
            recmdCond.setConfId(orderRecmd.getRecmdBusiParam());
            List<TfOrderRecmd> recmds = recmdMainService.getPage(recmdCond).getList();
            //能依据条件查询到数据，说明已经生成了二维码，直接跳转
            if (recmds.size() == 0) {

                //== 生成并保存二维码图片：生成图片 > 上传图片
                Long recmdId = recmdMainService.getIdWorkId();
                String recmdCode = Base64.encode((recmdId + "").getBytes());
                String simSellLink = orderRecmd.getRecmdUserLink() + orderRecmd.getRecmdBusiParam() + "&recmdCode=" + recmdCode;
                String styleColor = orderRecmd.getRecmdActConf().getConfMainColor().substring(1);
                byte[] bytes = QrCodeUtils.fetchQrCode(simSellLink, null, Integer.parseInt(styleColor, 16), 0xFFFFFFFF, 1);
                //== 生成图片到TFS
                String fileName = TFSClient.uploadFile(bytes, "png", null);
                logger.info("tfs上传后的文件名：" + fileName);
                //保存记录
                MemberLogin member = memberLoginService.getByMobile(Long.valueOf(orderRecmd.getRecmdPhone()));
                if (member != null) {
                    orderRecmd.setRecmdUserId(member.getMemberId());
                    orderRecmd.setRecmdUserName(member.getMemberLogingName());
                } else {
                    orderRecmd.setRecmdUserId(Long.getLong(orderRecmd.getRecmdPhone()));
                }
                orderRecmd.setRecmdId(recmdId);
                orderRecmd.setRecmdUserLink(simSellLink);
                orderRecmd.setRecmdUserQrcode(fileName);
                orderRecmd.setConfId(orderRecmd.getRecmdBusiParam());
                orderRecmd = recmdMainService.saveOrderRecmd(orderRecmd);
            }else {
                orderRecmd=recmds.get(0);
            }
            //== 推荐的结果信息
            //model = dealRecmdShowInfo(model, orderRecmd);
            TfH5SimConf conf=new TfH5SimConf();
            conf.setConfId(getQueryString(orderRecmd.getRecmdUserLink(),"confId"));
            List<TfH5SimConf> h5Confs = plansService.queryListCond(conf);
            if(!Collections3.isEmpty(h5Confs)){
                conf = h5Confs.get(0);
            }
            model.addAttribute("h5SimConf",conf);
            model.addAttribute("orderRecmd",orderRecmd);
            //model.addAttribute("styleColor",orderRecmd.getRecmdActConf().getConfMainColor());
        } catch (Exception e) {
            e.printStackTrace();
            splitException(e, "对不起，推荐二维码生成失败");
        }
        return model;
    }


    //========== 工具方法 ==========//
    /**
     * 判断是否允许推荐入口
     */
    @Deprecated
    public final int isAllowRecmd(IOrderConfigService configService) throws ParseException {
        //== 统一读取配置文件形式0
        List<TfOrderConfig> confs = configService.queryConfigList(TfOrderConfig.SIM_RECMD_TIME);
        Date startTime, endTime, now = new Date();
        startTime = endTime = now;
        for (TfOrderConfig conf : confs) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            if (conf.getConfigGroupName().equals(TfOrderConfig.SIM_RECMD_TIME_START)) {
                startTime = sdf.parse(conf.getConfigValue());
            }
            if (conf.getConfigGroupName().equals(TfOrderConfig.SIM_RECMD_TIME_END)) {
                endTime = sdf.parse(conf.getConfigValue());
            }
        }
        if (now.after(startTime) && now.before(endTime)) {
            return 1;
        }
        return 0;
    }

    /**
     * 判断是否允许推荐入口：每中类型独立配置
     */
    public final String isAllowRecmdV2(String rcdConfId) throws ParseException {
        TfRecmdActConf conf = recmdMainService.getRecmdActConf(rcdConfId);
        return dealRecmdAllow(conf);
    }

    /**
     * TfRecmdActConf中有beginTime和endTime，无需调服务，即可判断
     */
    public final String isAllowRecmdV2(TfRecmdActConf conf) throws ParseException {
        return dealRecmdAllow(conf);
    }

    private String dealRecmdAllow(TfRecmdActConf conf) {
        Date now = new Date();
        if(conf == null){
            return "-1";//没有任何与推荐有关的数据
        }
        else if(now.after(conf.getBeginTime()) && now.before(conf.getEndTime())) {
            return "1";//满足推荐条件
        }
        return  DateUtils.formatDateHMS(conf.getBeginTime())+"~"+DateUtils.formatDateHMS(conf.getEndTime());//有推荐配置，但是未在推荐时间范围内
    }

    /**
     * 生成推荐二维码
     */
    public final Model commonGenRecmdQrcodeByO2O(TfOrderRecmd orderRecmd,Model model,HttpServletRequest request) throws Exception {
        logger.info("commonGenRecmdQrcodeByO2O生成二维码参数："+JSONObject.toJSONString(orderRecmd));
        orderRecmd =  beforeGenQrcode(orderRecmd);
        logger.info("2commonGenRecmdQrcodeByO2O生成二维码参数："+JSONObject.toJSONString(orderRecmd));
        try {
            //== 生成并保存二维码图片：生成图片 > 上传图片
            Long recmdId = recmdMainService.getIdWorkId();
            String recmdCode = Base64.encode((recmdId + "").getBytes());
            String simSellLink = orderRecmd.getRecmdUserLink() + orderRecmd.getRecmdBusiParam() + "&recmdCode=" + recmdCode;
            String styleColor = orderRecmd.getRecmdActConf().getConfMainColor().substring(1);
            byte[] bytes = QrCodeUtils.fetchQrCode(simSellLink, null, Integer.parseInt(styleColor, 16), 0xFFFFFFFF, 1);
            //== 生成图片到TFS
            String fileName = TFSClient.uploadFile(bytes, "png", null);
            logger.info("tfs上传后的文件名：" + fileName);
            //保存记录
            MemberVo memberVo=UserUtils.getLoginUser(request);
            MemberLogin member = memberVo.getMemberLogin();
            if (member == null) {
                throw new Exception("登录超时，请重新登录！");
            }

            orderRecmd.setRecmdUserId(member.getMemberId());
            orderRecmd.setRecmdUserName(member.getMemberLogingName());
            orderRecmd.setRecmdPhone(member.getMemberLogingName());
            orderRecmd.setRecmdId(recmdId);
            orderRecmd.setRecmdUserLink(simSellLink);
            orderRecmd.setRecmdUserQrcode(fileName);
            orderRecmd.setConfId(orderRecmd.getRecmdBusiParam());
            orderRecmd = recmdMainService.saveOrderRecmd(orderRecmd);
        } catch (Exception e) {
            e.printStackTrace();
            splitException(e, "对不起，推荐二维码生成失败");
        }
        return model;
    }


    /**
     * 订单生成后处理
     */
    public void dealAfterNewOrder(HttpServletRequest request, TfOrderDetailSim orderDetailSim, TfPlans plans, long phone, TfOrderSub orderSubResult) {
        try {
            //== 发短信
            //该联系方式在移动这边是否有用户信息
            Map<String, Object> map = new HashMap<String, Object>();
            BasicInfoCondition biCond = new BasicInfoCondition();
            biCond.setSerialNumber(orderDetailSim.getContactPhone());
            biCond.setxGetMode("0");
            //默认值没有权限，所以显式赋值
            biCond.setStaffId("ITFWAPNN");
            biCond.setTradeDepartPassword("225071");
            logger.info("====用户资料条件===>" + JSONObject.toJSONString(biCond, SerializerFeature.WriteMapNullValue));
            Map userInfo = basicInfoQryModifyService.queryUserBasicInfo(biCond);
            logger.info("====用户资料返回===>" + JSONObject.toJSONString(userInfo, SerializerFeature.WriteMapNullValue));
            printKaInfo("submitOrderH5OnlineSim查询是否为湖南移动号码：" + JSONObject.toJSONString(userInfo));
            JSONObject phoneAttributionJSON = JSONObject.parseArray(userInfo.get("result").toString()).getJSONObject(0);
            String phoneNumberResultcode = phoneAttributionJSON.getString("X_RESULTCODE");
            if ("0".equals(phoneNumberResultcode)) {
                //发送短信
                SmsSendCondition smsSendCond = new SmsSendCondition();
                smsSendCond.setSerialNumber(orderDetailSim.getContactPhone());
                String noticeContent = "尊敬的客户，您已成功申请湖南移动" + plans.getPlanName() +
                        "卡，号码为" + orderDetailSim.getPhone() +
                        "，订单号为" + orderSubResult.getOrderSubNo() +
                        "。我们正在备货，您可关注“湖南移动微厅”查询订单详情。【中国移动】。";
                smsSendCond.setNoticeContent(noticeContent);
                logger.info("====短信请求===>" + JSONObject.toJSONString(smsSendCond, SerializerFeature.WriteMapNullValue));
                Map smsSendRetMap = smsSendService.sendSms(smsSendCond);
                logger.info("====短信返回===>" + JSONObject.toJSONString(smsSendRetMap, SerializerFeature.WriteMapNullValue));
                printKaInfo(phone + "号卡订单生成成功短信接口内容" + JSONObject.toJSONString(smsSendRetMap));
            }
            //== 根据用户IP地址记录选号次数
            simBusiService.setIpPhoneNumOrders(Servlets.getRemortIP(request));
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.printKaError(CommonParams.KA_LOG, "submitOrderH5OnlineSim发送短信失败", e);
        }
    }



    /**
     * 给指定号码赠送指定流量
     * @param flowCode 流量识别码
     * @param serialNumber 手机号
     * @return
     */
    protected boolean giftFlow(String flowCode,String serialNumber)throws Exception {
        boolean giftFlowFlag = false;
        try {
            printKaInfo("==流量赠送开始==>" +flowCode+"---"+serialNumber);
            HqChangeProdAndElemCondition hqChangeProdAndElemCondition = new HqChangeProdAndElemCondition();
            hqChangeProdAndElemCondition.setSERIAL_NUMBER(serialNumber);
            hqChangeProdAndElemCondition.setNUM(1);
            hqChangeProdAndElemCondition.setELEMENT_TYPE_CODE("D");
            hqChangeProdAndElemCondition.setMODIFY_TAG("0");
            hqChangeProdAndElemCondition.setPRODUCT_ID("");
            hqChangeProdAndElemCondition.setREMOVE_TAG("0");
            hqChangeProdAndElemCondition.setELEMENT_ID(flowCode);
            hqChangeProdAndElemCondition.setDISCNT(flowCode);
            hqChangeProdAndElemCondition.setStaffId("ITFWAPNN");//默认值没有权限，所以显式赋值
            hqChangeProdAndElemCondition.setTradeDepartPassword("225071");
            hqChangeProdAndElemCondition.setRunningId(String.valueOf(com.ai.ecs.common.utils.DateUtils.getDate("yyyyMMddHHmmss") + (int) ((Math.random() * 9 + 1) * 100000)));
            Map<String, Object> resultDataMap = flowServeService.changeProdAndElem(hqChangeProdAndElemCondition);
            printKaInfo(serialNumber + " giftFlow 流量赠送结果:" + resultDataMap);
            if (MapUtils.isNotEmpty(resultDataMap)) {
                if (HNanConstant.SUCCESS.equals(resultDataMap.get("respCode"))) {
                    giftFlowFlag = true;
                }
            }
        }catch (Exception e){
            printKaError("giftFlow 流量赠送异常：", e);
        }
        return giftFlowFlag;
    }


    public String generateFlowCode(String epachyCode,String flowCode){
        String result = flowCode.replace("**", epachyCode.substring(2, 4));
        return result;
    }

    /**
     * 获取地市编码
     */
    public String getEpachyCode(String serialNumber) throws Exception{
        String epachyCode= "";
        BasicInfoCondition basicInfoCondition = new BasicInfoCondition();
        basicInfoCondition.setSerialNumber(serialNumber);
        basicInfoCondition.setxGetMode("0");
        basicInfoCondition.setStaffId("ITFWAPNN");//默认值没有权限，所以显式赋值
        basicInfoCondition.setTradeDepartPassword("225071");
        Map<String,Object> returnDataMap = basicInfoQryModifyService.queryUserBasicInfo(basicInfoCondition);
        if (MapUtils.isNotEmpty(returnDataMap) && "0".equals(returnDataMap.get("respCode"))) {
            List<Map<String,Object>> returnData = (List<Map<String,Object>>)returnDataMap.get("result");
            epachyCode = String.valueOf(returnData.get(0).get("EPARCHY_CODE"));

        }else{
            throw new Exception("没有查询到该手机号的用户信息!"+serialNumber +returnDataMap.get("respDesc"));
        }
        return epachyCode;
    }




    /**
     * 是否在时间范围内
     * @param begin 2018-04-01
     * @param end 2018-07-01
     * @return
     * @throws Exception
     */
    protected boolean timeRange(String begin,String end)throws Exception{
        Date startTime, endTime, now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        startTime = sdf.parse(begin);
        endTime = sdf.parse(end);
        if (now.after(startTime) && now.before(endTime)) {
            return true;
        }
        return false;
    }
}
