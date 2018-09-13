package com.ai.ecs.ecm.mall.wap.modules.goods.controller;

import com.ai.ecs.campusmark.api.ICampusMarkService;
import com.ai.ecs.campusmark.entity.TfCampusMarkCouponsGet;
import com.ai.ecs.campusmark.entity.TfCampusMarkGoods;
import com.ai.ecs.common.ResponseBean;
import com.ai.ecs.common.utils.JedisClusterUtils;
import com.ai.ecs.common.utils.PropertiesLoader;
import com.ai.ecs.common.utils.TFSClient;
import com.ai.ecs.ecm.mall.wap.common.security.FormAuthenticationFilter;
import com.ai.ecs.ecm.mall.wap.modules.member.vo.MemberBaseVo;
import com.ai.ecs.ecm.mall.wap.platform.annotation.RefreshCSRFToken;
import com.ai.ecs.ecm.mall.wap.platform.annotation.VerifyCSRFToken;
import com.ai.ecs.ecm.mall.wap.platform.utils.*;
import com.ai.ecs.ecop.wap.entity.CmnetIp;
import com.ai.ecs.ecop.wap.entity.GatewayIp;
import com.ai.ecs.ecop.wap.service.AllowIpService;
import com.ai.ecs.ecsite.modules.myMobile.entity.BasicInfoCondition;
import com.ai.ecs.ecsite.modules.myMobile.service.BasicInfoQryModifyService;
import com.ai.ecs.member.api.login.ILoginService;
import com.ai.ecs.member.api.register.IRegisterService;
import com.ai.ecs.member.constant.MemberStatusConstant;
import com.ai.ecs.member.constant.MemberTypeConstant;
import com.ai.ecs.common.utils.Base64;
import com.ai.ecs.member.entity.MemberLogin;
import com.ai.ecs.member.entity.MemberVo;
import com.ai.ecs.member.entity.UserContext;
import com.ai.ecs.order.api.recmd.IRecmdMainService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import jodd.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by pimengjue on 2018/8/20.
 */
@Controller
@RequestMapping("campusmark")
public class CampusMarkController extends RecmdDefaultController{

    @Autowired
    ICampusMarkService campusMarkService;

    @Autowired
    IRegisterService registerService;

    @Autowired
    IRecmdMainService recmdMainService;

    @Autowired
    public AllowIpService allowIpService;


    @Autowired
    ILoginService loginService;

    @Autowired
    private BasicInfoQryModifyService basicInfoQryModifyService;


    private static String keyStr = "ABCDEFGHIJKLMNOP" + "QRSTUVWXYZabcdef" + "ghijklmnopqrstuv"
            + "wxyz0123456789+/" + "=";

    private int JEDISTIMEOUT = 1800;

    private int SECONDSSMSOUT = 55;

    private int LOGINJEDISTIMEOUT = 1800;

    private int SMSOUT = 60 * 60 * 24;

    private int LOGINLOCKJEDISTIMEOUT = 60 * 60 * 24;

    private int CAPTCHAJEDISTIMEOUT = 1800;

    private static char[] RAND_CODES = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

    private String MOBILE="mob";
    private String BRAND="brand";



    /**
     * 活动首页显示所有手机
     * @param model
     * @return
     */
    @RequestMapping("/toIndex")
    public String index(HttpServletRequest request, HttpServletResponse response,Model model) {

        String mobile =getMobile(request);
        if(StringUtil.isBlank(mobile)){
        }
        try {
            Session session = UserUtils.getSession();
            UserContext context = new UserContext();
            context.setClientIp(request.getRemoteAddr());
            context.setMobile(mobile);
            context.setChannelCode("E007");
            Map<String, Object> loginRes =  loginService.loginBySms(context, ""); // 商城登录，首次登录会生成账号并记录用户信息
            if(loginRes==null){
            }
            Object obj = loginRes.get("data");
            MemberVo membervo = JSONObject.parseObject(JSONObject.toJSONString(obj), MemberVo.class);
            JedisClusterUtils.setObject("loginUser" + session.getId(), membervo, JEDISTIMEOUT);
            MemberBaseVo baseMember = new MemberBaseVo();
            baseMember.setMemberId(membervo.getMemberLogin().getMemberId());
            baseMember.setMemberPhone(membervo.getMemberLogin().getMemberPhone());
            CookieUtils.setCookie(response,MOBILE, mobile,60*60*24);

            JedisClusterUtils.set("LOGIN_BASEINFO_" + session.getId(), JSON.toJSONString(baseMember),JEDISTIMEOUT);
            JedisClusterUtils.set("sessionId" + session.getId(), session.getId() + "", JEDISTIMEOUT);
            getBasicInfo(request, response, mobile);
            //保存日志
            MemberLoginLogUtils.saveLog(request, "2", "0", "登录成功", null, mobile, membervo);
        } catch (Exception e) {
            logger.error("======loginError======", e);
        }

        List<TfCampusMarkGoods> userGoodsCarList =  campusMarkService.queryMobileList();
        model.addAttribute("mobileList", userGoodsCarList);
        return "web/campusmark/index";
    }

    //跳转到领取成功页面
    @RequestMapping("/toReceiveSuccess")
    public String toReceiveSuccess(@RequestParam Long quan,Model model) {
        String imgName = "";
        if(quan == 50){
            imgName = "price-50.png";
        }else if(quan == 100){
            imgName = "price-100.png";
        }else{
            imgName = "price-200.png";
        }
        model.addAttribute("imgName", imgName);
        model.addAttribute("quan", quan);
        return "web/campusmark/receiveSuccess";
    }

    //查看我的券:1、首页，2、领券成功后
    @RequestMapping("/toMyCoupons")
    public String toMyCoupons(HttpServletRequest request,Model model) {
        String quan = "";
        MemberVo member = UserUtils.getLoginUser(request);
        List<TfCampusMarkCouponsGet> myCoupons = null;
        Map<String, Object> returnMap = new HashMap<String, Object>();
        if (member != null) {//已登录,跳转到"查看我的券"
            returnMap.put("result","0");
            myCoupons =  campusMarkService.getMyCoupons(Long.parseLong(member.getMemberLogin().getMemberId().toString()));
            quan = campusMarkService.getMaxCoupon(Long.parseLong(member.getMemberLogin().getMemberId().toString()));
        }
        model.addAttribute("myCoupons", myCoupons);
        model.addAttribute("returnMap", returnMap);
        model.addAttribute("quan", quan);
        return "web/campusmark/myCoupons";
    }


    //分享二维码给好友
    @RequestMapping("/shareToFriends")
    public String shareToFriends(HttpServletRequest request,Model model) {
        String quan = request.getParameter("quan");
        MemberVo member = UserUtils.getLoginUser(request);

        //== 生成并保存二维码图片：生成图片 > 上传图片
        Long recmdId = recmdMainService.getIdWorkId();
        String recmdCode = Base64.encode((recmdId + "").getBytes());
        String basePath = request.getScheme() + "://"
                + request.getServerName() + ":" + request.getServerPort()
                + request.getContextPath();
        String simSellLink = basePath + "/campusmark/toIndex" ;
        //String styleColor = orderRecmd.getRecmdActConf().getConfMainColor().substring(1);
        byte[] bytes = QrCodeUtils.fetchQrCode(simSellLink, null, Integer.parseInt("000000", 16), 0xFFFFFFFF, 1);
        //== 生成图片到TFS
        String fileName = TFSClient.uploadFile(bytes, "png", null);
        model.addAttribute("quan", quan);
        model.addAttribute("qrCodePath", fileName);
        return "web/campusmark/shareToFriends";
    }


  /**
     * 判断是否已经登录
     * @param
     * @return
     * @throws Exception
     */
    @RequestMapping( value = "isLoginIndex",method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> isLoginIndex(HttpServletRequest request) throws Exception {
        MemberVo member = UserUtils.getLoginUser(request);
        Map<String, Object> returnMap = new HashMap<String, Object>();
        if (member != null) {
            returnMap.put("isLogin", "0");// 已登录
        }else{
            returnMap.put("isLogin", "1");// 未登录
        }
        return returnMap;
    }



    /**
     * 首页点击进入手机详细页面
     * @param goodsId
     * @return
     */
    @RequestMapping("/showDetail")
    @RefreshCSRFToken
    public String showDetail(@RequestParam Long goodsId,Model model) {
        logger.info("goodsId:"+goodsId);
        TfCampusMarkGoods goods = campusMarkService.getMobileDetail(goodsId);
        List<TfCampusMarkGoods> userGoodsCarList =  campusMarkService.queryMobileList();
        model.addAttribute("mobileList", userGoodsCarList);
        model.addAttribute("goods", goods);
        return "web/campusmark/showDetail";
    }


    /**
     * 用户名登录
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @VerifyCSRFToken
    @RequestMapping(value = "/loginByName", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> loginByName(HttpServletRequest request, HttpServletResponse response,
                                           String loginname, String password) throws Exception {

        loginname = TriDes.getInstance().strDec(loginname, keyStr, null, null);
        password = TriDes.getInstance().strDec(password, keyStr, null, null);
        Integer failLoginCount = 0;
        String failStr = JedisClusterUtils.get("failLoginCount" + loginname);
        if (StringUtils.isNotEmpty(failStr)) {
            failLoginCount = Integer.parseInt(failStr);
        }
        Map<String, String> resMap = new HashMap<String, String>();
        Session session = UserUtils.getSession();

        UserContext context = new UserContext();
        context.setClientIp(request.getRemoteHost());
        context.setSessionId(request.getSession().getId());
        context.setChannelCode("E007");
        try {

            if (failLoginCount > 5) {
                resMap.put("msg", "登录失败超过5次，您的账号被冻结30分钟");
                Integer failLoginLockCount = 0;
                String failLoginLockCountStr = JedisClusterUtils.get("failLoginLockCount" + loginname);
                if (StringUtils.isNotEmpty(failLoginLockCountStr)) {
                    failLoginLockCount = Integer.parseInt(failLoginLockCountStr);
                }
                if (failLoginLockCount < 5) {
                    JedisClusterUtils.set("failLoginLockCount" + loginname, ++failLoginLockCount + "",
                            LOGINLOCKJEDISTIMEOUT);
                } else {
                    MemberLogin memberLogin = memberLoginService.getByLoginMame(loginname, MemberTypeConstant.REGISTER.getValue());
                    if (memberLogin != null) {
                        memberLogin.setMemberPassword(null);
                        memberLogin.setMemberStatusId(MemberStatusConstant.LOCKUP.getValue());
                        memberLoginService.updatememberLogin(memberLogin);

                    }
                    resMap.put("msg", "冻结超过5次，您的账号被锁定，请联系客服!");
                }
                return resMap;
            }
            Map<String, Object> member = null;
            try {
                member = loginService.loginByName(context, loginname, password);
            } catch (NullPointerException e) {
                BusiLogUtils.writerLogging(request, "loginByName", null, null, DateUtils
                        .formatDate(new Date()), null, "该账号不存在", "该账号不存在!", "", e, "", loginname);
                failLoginCount++;
                resMap.put("msg", "用户名或密码错误!");
                JedisClusterUtils.set("failLoginCount" + loginname, failLoginCount + "", LOGINJEDISTIMEOUT);
                return resMap;
            }
            JedisClusterUtils.set("sessionId" + session.getId(), session.getId() + "", JEDISTIMEOUT);
            if (member != null) {
                if (member.get("msg") != null) {
                    resMap.put("msg", (String) member.get("msg"));
                    failLoginCount++;
                    JedisClusterUtils.set("failLoginCount" + loginname, failLoginCount + "",
                            LOGINJEDISTIMEOUT);
                    MemberLoginLogUtils.saveLog(request, "4", "1", "登录失败!", null, loginname, null);
                    return resMap;
                }
                Object obj = member.get("data");
                MemberVo membervo = JSONObject.parseObject(JSONObject.toJSONString(obj), MemberVo.class);
                JedisClusterUtils.setObject("loginUser" + session.getId(), membervo, JEDISTIMEOUT);
                JedisClusterUtils.del("VC_VALUE_LOGIN" + session.getId());
                resMap.put("username", loginname);
                resMap.put("msg", "success");
				/*
				 * BusiLogUtils.writerLogging(request, "loginByName", null, null,
				 * DateUtils.formatDate(new Date()), null, "用户名登录成功", "用户名登录成功", "", null, "",
				 * loginname);
				 */
                MemberBaseVo baseMember = new MemberBaseVo();
                baseMember.setMemberId(membervo.getMemberLogin().getMemberId());
                baseMember.setMemberPhone(membervo.getMemberLogin().getMemberPhone());
                JedisClusterUtils.set("LOGIN_BASEINFO_" + session.getId(), JSON.toJSONString(baseMember),
                        JEDISTIMEOUT);
                MemberLoginLogUtils.saveLog(request, "4", "0", "登录成功", null, loginname, membervo);
                request.getSession().setAttribute("CSRFToken", CSRFTokenUtil.generate(request));//刷新csrfToken的值
            }
        } catch (Exception e) {
            MemberLoginLogUtils.saveLog(request, "4", "1", "登录失败", null, loginname, null);
            resMap.put("msg", "会员服务异常");
        }

        return resMap;
    }

    /**
     * 用户名注册
     */
    @RequestMapping(value = "/doRegister",method = RequestMethod.POST,produces = "application/json; charset=UTF-8")
    @ResponseBody
    @VerifyCSRFToken
    public String doRegister(HttpServletRequest request, HttpServletResponse response,String sessionId,String member_email,String member_name,String member_passwd,String captcha) throws Exception {
        Session session = UserUtils.getSession();
        Integer failNum=0;
        String failNumKey="MALL_REGISTER_NUM1"+ FormAuthenticationFilter.getRemoteAddr(request);
        if(isSpecialChar(member_name)){
            return "用户名不能包含特殊字符！";
        }
        if(memberLoginService.checkEMail(member_email)){
            return "邮件地址已存在！";
        }
        if(JedisClusterUtils.exists(failNumKey)){
            failNum=Integer.parseInt(JedisClusterUtils.get(failNumKey));
        }
        if(failNum>5){
            return "系统异常，请稍候再试!!!";
        }
        member_passwd = TriDes.getInstance()
                .strDec(member_passwd, keyStr, null, null);
//        String captchaReal=  JedisClusterUtils.get("VC_VALUE"+session.getId());
//        if(StringUtils.isEmpty(captchaReal)){
//            failNum++;
//            JedisClusterUtils.set(failNumKey,failNum+"",60*60*24);
//            return "验证码失效，请刷新";
//        }
//        if(captchaReal!=null&&!captchaReal.equalsIgnoreCase(captcha)){
//            failNum++;
//            JedisClusterUtils.set(failNumKey,failNum+"",60*60*24);
//            return "验证码校验失败，请重新输入";
//        }
        MemberLogin login=memberLoginService.getByLoginMame(member_name,-1);
        if(login!=null&&login.getMemberId()!=null){
            failNum++;
            JedisClusterUtils.set(failNumKey,failNum+"",60*60*24);
            JedisClusterUtils.set("VC_VALUE"+session.getId(),"sdfre1",60*60);
            return "该用户名已存在";
        }
        MemberLogin member=new MemberLogin();
        member.setMemberLogingName(member_name);
        member.setMemberPassword(member_passwd);
        member.setMemberEmail(member_email);
        try {
            MemberLogin loginUser= registerService.registerByName(member);
            if(loginUser!=null){
                JedisClusterUtils.del("VC_VALUE"+session.getId());
                doLogin( request,  member_name,  member_passwd);
                return "注册成功";
            }
        }
        catch (Exception e)
        {
            logger.error("注册失败",e);
            return "注册失败";
        }

        return "注册服务异常中断，请重试！";
    }

    public static boolean isSpecialChar(String str) {
        String regEx = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.find();
    }

    //用户登录
    private void doLogin(HttpServletRequest request, String loginname, String password){
        Session session=UserUtils.getSession();
        UserContext context = new UserContext();
        context.setClientIp(request.getRemoteHost());
        context.setSessionId(session.getId()+"");
        context.setChannelCode("E006");
        try {
            Map<String, Object> member =null;
            try{
                member= loginService.loginByName(context, loginname, password);
            }catch(NullPointerException e){
                logger.error("注册登录失败:",e);
            }
            JedisClusterUtils.set("sessionId" + session.getId(), session.getId() + "", JEDISTIMEOUT);

            if(member!=null&&member.get("msg")!=null){//会员中心返回登录错误消息
                logger.error("注册登录失败:"+member.get("msg"));
            }else{//登录成功
                Object obj = member.get("data");
                MemberVo membervo=JSONObject.parseObject(JSONObject.toJSONString(obj),MemberVo.class );
                JedisClusterUtils.setObject("loginUser" + session.getId(),
                        membervo, JEDISTIMEOUT);
                JedisClusterUtils.del("VC_VALUE_LOGIN" + session.getId());
            }
        } catch (Exception e) {
        }
    }

    /**
     * 判断是否已经登录
     * @param paramMap
     * @return
     * @throws Exception
     */
    @RequestMapping( value = "isLogin",method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> isLogin(HttpServletRequest request, @RequestBody Map paramMap) throws Exception {
        MemberVo member = UserUtils.getLoginUser(request);
        Map<String, Object> returnMap = new HashMap<String, Object>();
        if (member != null) {
            returnMap.put("isLogin", "0");// 已登录
            //已登录的用户，直接领券
            paramMap.put("userid",member.getMemberLogin().getMemberId().toString());
            String result =  campusMarkService.receiveCoupon(paramMap);
            returnMap.put("result",result);
        }else{
            returnMap.put("isLogin", "1");// 未登录
            returnMap.put("result","1");
        }
        return returnMap;
    }

    /**
     * 领取优惠券
     * @param
     * @return
     * @throws Exception
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @RequestMapping(value = "receiveCoupon",method = RequestMethod.POST)
    public @ResponseBody Map<String,Object>  receiveCoupon(HttpServletRequest request,Map params) throws Exception {
        Map<String,Object> resultMap = new HashMap<String,Object>();
        MemberVo member = UserUtils.getLoginUser(request);
        if(member==null) {
            resultMap.put("flag", "0");
            resultMap.put("resultMessage", "您未登录，请登录");
            return resultMap;
        }
        params.put("memberId",member.getMemberLogin().getMemberId().toString());
        params.put("memberLoginName",member.getMemberLogin().getMemberLogingName());
        String message =  campusMarkService.receiveCoupon(params);
        resultMap.put("flag", "1");
        resultMap.put("resultMessage", message);

        return resultMap;
    }


    /**
     * 校验手机号是否为湖南移动号码
     *
     * @param mobile
     * @return Map
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/checkPhoneNumber")
    public ResponseBean checkPhoneNumber(@RequestParam String mobile) throws Exception
    {
        logger.info("mobile:"+mobile);
        ResponseBean responseBean = new ResponseBean("-1","");
        BasicInfoCondition basicInfoCondition = new BasicInfoCondition();
        basicInfoCondition.setSerialNumber(mobile);
        basicInfoCondition.setxGetMode("0");
        basicInfoCondition.setStaffId("ITFWC000");
        basicInfoCondition.setTradeDepartPassword("ai1234");
        Map map = basicInfoQryModifyService.queryUserBasicInfo(basicInfoCondition);
        logger.info("respcode:"+map.get("respCode"));
        if (map.get("respCode").equals("0")){
            responseBean.addSuccess(map);
        }
        return responseBean;
    }


    /**
     *
     * @todo getMobile 从wap网关获取手机号
     * @param request
     * @return
     */
    private String getMobile(HttpServletRequest request) {
        String mobile = null;
        boolean matchAllowIp = false; // 获取cmwap登录判断标识
        try {
            Enumeration headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String headerName = (String) headerNames.nextElement();
                // 判断是否存在X-UP-CALLING-LINE-ID字段，并获取其值
                if (headerName.toUpperCase().equals("X-UP-CALLING-LINE-ID")) {
                    mobile = getCallingId(request.getHeader(headerName));
                }
                //StringUtil.isNotBlank(mobile)&& 经过F5网关处理后的客户端IP地址 取客户端IP
                if (headerName.toUpperCase().equals("X-FORWARDED-FOR")) {
                    String clientIp = request.getHeader(headerName);
                    matchAllowIp = isForwardeId(clientIp);
                }
            }
        } catch (Exception e) {
            // 捕获手机号码获取时可能产生的异常
            logger.error("getMobile",e);
        }
        if (!matchAllowIp) {
            return null;
        }
        return mobile;
    }

    private void getBasicInfo(HttpServletRequest request, HttpServletResponse response, String mobile)  {
        try {
            BasicInfoCondition condition = new BasicInfoCondition();
            condition.setSerialNumber(mobile);
            condition.setxGetMode("0");
            condition.setRunningId(System.currentTimeMillis()+"");
            condition.setStaffId("ITFWAPNN");
            condition.setTradeDepartPassword("225071");
            Map userInfo = basicInfoQryModifyService.queryUserBasicInfo(condition);
            CookieUtils.setCookie(response,BRAND, brand(userInfo.get("BRAND")+""),60*60*24);
        }catch (Exception e){
            logger.error("getBasicInfo",e);
        }

    }


    /**
     * 品牌转换
     */
    private String brand (String name){
        String gotone="全球通";
        String Mzone="动感地带";
        String easyown="神州行";

        if(gotone.equals(name)){
            return "gotone";
        }else if(Mzone.equals(name)){
            return "Mzone";
        }else if(easyown.equals(name)){
            return "easyown";
        }else{
            return "";
        }

    }


    /**
     * 取出头文件中的手机号码
     * CALLING-LINE-
     * @param temvit
     * @return
     */
    private String  getCallingId(String temvit){
        String mobile = null;
        if (temvit.substring(0, 3).trim().equals("861")){
            mobile = temvit.substring(2, 13);
        }
        if (temvit.substring(0, 4).trim().equals("+861")){
            mobile = temvit.substring(3, 14);
        }
        if (temvit.substring(0, 2).trim().equals("13")){
            mobile = temvit;
        }
        if (temvit.substring(0, 2).trim().equals("14")){
            mobile = temvit;
        }
        if (temvit.substring(0, 2).trim().equals("15")){
            mobile = temvit;
        }
        if (temvit.substring(0, 2).trim().equals("18")){
            mobile = temvit;
        }
        if (temvit.substring(0, 2).trim().equals("17")){
            mobile = temvit;
        }
        logger.info("=====X-UP-CALLING-LINE-ID:"+temvit);
        return mobile;
    }

    /**
     * 判断用户手机获取的X-FORWARDED-FOR地址是否是wap网关地址
     * @param clientIp
     * @return
     */
    private boolean isForwardeId(String clientIp){
        //有多个IP的情况下，取最后一个
        if(clientIp.contains(",") || clientIp.contains("，")){
            clientIp = clientIp.replace("，", ",");
            String[] clientIpItem = clientIp.split(",");
            clientIp=clientIpItem[clientIpItem.length-1].trim();
        }
        try {
            //判断用户手机获取的X-FORWARDED-FOR地址是否是wap网关地址
            if (isAllowIp(clientIp.trim())) {	//用户经过F5处理的IP在白名单之内,表示未伪造用户信息，符合CMWAP自登录条件
                logger.info("=========用户经过F5处理的IP在白名单之内,表示未伪造用户信息，符合CMWAP自登录条件=========");
                return true;
            } else {
                logger.info("Filter client ip failed:client ip not in write list " + clientIp);
            }
        } catch (Exception e) {
            logger.error("Filter client ip failed:" + clientIp + ":" ,e);
        }
        return false;
    }


    private boolean isAllowIp(String clientIp) throws Exception {
        CmnetIp cmnetIp = new CmnetIp();
        cmnetIp.setPublicIp(clientIp);
        CmnetIp cmnetIpInfo = allowIpService.getCmnetIpInfo(cmnetIp);
        if(cmnetIpInfo !=null){
            if (StringUtils.isNotBlank(cmnetIpInfo.getPublicIp())){
                return true;
            }
        }
        GatewayIp gatewayIp = new GatewayIp();
        gatewayIp.setPublicIp(clientIp);
        gatewayIp  = allowIpService.getGatewayIpInfo(gatewayIp);
        if(gatewayIp !=null){
            if (StringUtils.isNotBlank(gatewayIp.getPublicIp())){
                return true;
            }
        }
        return false;
    }




}
