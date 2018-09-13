package com.ai.ecs.ecm.mall.wap.modules.member;

import com.ai.ecs.common.utils.JedisClusterUtils;
import com.ai.ecs.ecm.mall.wap.modules.BaseController;
import com.ai.ecs.ecm.mall.wap.modules.member.vo.OrderCountResult;
import com.ai.ecs.ecm.mall.wap.platform.annotation.RefreshCSRFToken;
import com.ai.ecs.ecm.mall.wap.platform.annotation.VerifyCSRFToken;
import com.ai.ecs.ecm.mall.wap.platform.constants.OrderStatusConstant;
import com.ai.ecs.ecm.mall.wap.platform.utils.*;
import com.ai.ecs.member.api.IMemberAddressService;
import com.ai.ecs.member.api.IMemberInfoService;
import com.ai.ecs.member.api.IMemberLoginService;
import com.ai.ecs.member.api.login.ILoginService;
import com.ai.ecs.member.entity.*;
import com.ai.ecs.order.api.IOrderQueryService;
import com.ai.ecs.order.entity.TfOrderSub;
import com.ai.ecs.order.entity.TfOrderUserRef;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("memberInfo")
public class MemberInfoController extends BaseController {
    @Autowired
    IMemberInfoService memberInfoService;

    @Autowired
    IMemberLoginService memberLoginService;

    @Autowired
    @Qualifier("orderQueryService")
    IOrderQueryService orderQueryService;

    private int SMSOUT = 60 * 60 * 24;

    @Autowired
    ILoginService loginService;

    private int SECONDSSMSOUT = 55;
    @Autowired
    IMemberAddressService memberAddressService;

    @Value("${ssoServerHost}")
    private String ssoServerHost;

//	@Value("${project.name}")
//	private String projectName;

    private String PASSWORD_KEY = "MOBILE.COM";

    private static String keyStr = "ABCDEFGHIJKLMNOP" + "QRSTUVWXYZabcdef"
            + "ghijklmnopqrstuv" + "wxyz0123456789+/" + "=";

    private int JEDISTIMEOUT = 1800;

    @RequestMapping(value = "/toRefUrl", method = RequestMethod.GET)
    public String toRefUrl(HttpServletRequest request,
                           HttpServletResponse response, Model model) throws Exception {
        MemberVo member = UserUtils.getLoginUser(request);
        Session session = UserUtils.getSession();
//		 String defaultUrl =request.getHeader("Host")+projectName;
        if (member != null) {
            String refUrl = JedisClusterUtils.get("redirectUrl" + session.getId());
            if (StringUtils.isNotEmpty(refUrl)) {
                refUrl = URLDecoder.decode(refUrl, "UTF-8");
                JedisClusterUtils.del("redirectUrl" + session.getId());
                return "redirect:" + refUrl;
            }
        }

        return "redirect:/";

    }

    @RequestMapping(value = "/toSetMemberInfo", method = RequestMethod.GET)
    public String toSetMemberInfo(HttpServletRequest request,
                                  HttpServletResponse response, Model model) {
        MemberVo member = UserUtils.getLoginUser(request);
        try {
            if (member != null) {
                MemberInfo memberInfo;
                memberInfo = memberInfoService.getMemberInfoByKey(member
                        .getMemberLogin().getMemberId());

                if (memberInfo != null) {
                    memberInfo.setMemberProvince((ArrayUtils.getFirst(memberInfo
                            .getMemberProvince().split(":"))));
                    memberInfo.setMemberCity(ArrayUtils.getFirst(memberInfo
                            .getMemberCity().split(":")));
                    memberInfo.setMemberCounty(ArrayUtils.getFirst(memberInfo
                            .getMemberCounty().split(":")));
                    List<ThirdLevelAddress> citys = memberAddressService
                            .getChildrensByPId(memberInfo.getMemberProvince());
                    List<ThirdLevelAddress> countys = memberAddressService
                            .getChildrensByPId(memberInfo.getMemberCity());

                    request.setAttribute("citys", citys);
                    request.setAttribute("countys", countys);

                    member.setMemberInfo(memberInfo);
                }

                //模糊化电话号码
                Long lPhone = member.getMemberLogin().getMemberPhone();
                if(lPhone != null){
                    String strPhone = String.valueOf(lPhone);
                    StringBuilder blurryPhone = new StringBuilder("");
                    if (strPhone.length() >= 11){
                        blurryPhone.append(strPhone.substring(0,3));
                        blurryPhone.append("****");
                        blurryPhone.append(strPhone.substring(7));
                    } else {
                        blurryPhone.append(strPhone);
                    }
                    request.setAttribute("blurryPhone",blurryPhone.toString());

                }

                List<ThirdLevelAddress> addrParent = memberAddressService
                        .getParents();
                request.setAttribute("addrParent", addrParent);
                request.setAttribute("member", member);
            }
        } catch (Exception e) {
            logger.error("查询会员资料失败：", e);
        }
        return "web/member/setMemberInfo";
    }

    @RequestMapping(value = "/toMemberCenter", method = RequestMethod.GET)
    public String toMemberCenter(HttpServletRequest request,
                                 HttpServletResponse response, Model model) {
        MemberVo member = UserUtils.getLoginUser(request);
        Session session = UserUtils.getSession();
        if (member != null) {
            MemberLogin memberLogin = member.getMemberLogin();
            MemberInfo memberInfo = member.getMemberInfo();
            if (memberLogin == null) {
                memberLogin = new MemberLogin();
            }
            if (memberInfo == null) {
                memberInfo = new MemberInfo();
            }
            if (memberInfo.getMemberNickname() == null) {
                memberInfo.setMemberNickname(memberLogin.getMemberLogingName());
            }

            if (memberLogin.getMemberTypeId() == 1) {
                session.setAttribute("flag", false);
            } else {
                session.setAttribute("flag", true);
            }
            TfOrderSub orderSub = new TfOrderSub();
            TfOrderUserRef orderUserRef = new TfOrderUserRef();
            orderUserRef.setMemberId(member.getMemberLogin().getMemberId());
            orderSub.setOrderUserRef(orderUserRef);
            long waitPayCount = orderQueryService.queryOrderCount(orderSub);
            orderSub.setOrderStatusId(OrderStatusConstant.WAITSENDGOODS
                    .getValue());
            long waitSendCount = orderQueryService.queryOrderCount(orderSub);
            orderSub.setOrderStatusId(OrderStatusConstant.WAITRECEIVEGOODS
                    .getValue());
            long waitReceiveCount = orderQueryService.queryOrderCount(orderSub);
            orderSub.setOrderStatusId(OrderStatusConstant.WAITCOMMENT
                    .getValue());
            long waitCommentCount = orderQueryService.queryOrderCount(orderSub);
            OrderCountResult orderCount = new OrderCountResult();
            orderCount.setMemberId(member.getMemberLogin().getMemberId());
            orderCount.setWaitPayCount(waitPayCount);
            orderCount.setWaitSendCount(waitSendCount);
            orderCount.setWaitReceiveCount(waitReceiveCount);
            orderCount.setWaitCommentCount(waitCommentCount);

            request.setAttribute("orderCount", orderCount);
            request.setAttribute("memberId", memberLogin.getMemberId());
            request.setAttribute("mobile", UtilString.showMemberName(memberLogin.getMemberPhone() + ""));
            request.setAttribute("nickName", UtilString.isMobileNO(memberInfo.getMemberNickname()));
        }
        return "web/member/memberCenter";
    }

    /**
     * 设置会员资料
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/setMemberInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String setMemberInfo(HttpServletRequest request,
                                HttpServletResponse response, String memberNickname,
                                String memberEmail, String memberRealname, String memberSex,
                                String memberQq, String memberPhone, String memberAddress,
                                String memberProvince, String memberCity, String memberCounty) {
        MemberVo member = UserUtils.getLoginUser(request);
        try {
            MemberLogin memberLogin = member.getMemberLogin();
            if (StringUtils.isNotEmpty(memberPhone)) {
               String memberPhoneTmp = UtilString.clearXss(TriDes.getInstance().strDec(memberPhone, keyStr, null, null));
                if(StringUtils.isNotEmpty(memberPhoneTmp)){
                    memberPhone=memberPhoneTmp;
                }
                memberLogin.setMemberPhone(Long.parseLong(memberPhone));
            }

            memberRealname = UtilString.clearXss(TriDes.getInstance().strDec(memberRealname, keyStr, null, null));
            memberEmail = UtilString.clearXss(TriDes.getInstance().strDec(memberEmail, keyStr, null, null));
            memberAddress = UtilString.clearXss(TriDes.getInstance().strDec(memberAddress, keyStr, null, null));
            if (StringUtils.isNotEmpty(memberEmail)) {
                memberLogin.setMemberEmail(memberEmail);
            }
            memberLogin.setMemberPassword(null);
            boolean updateLoginRes = memberLoginService
                    .updatememberLogin(memberLogin);
            if (!updateLoginRes) {
                return "设置memberLogin失败，请重试";
            }
            member.setMemberLogin(memberLogin);
            boolean res = false;
            MemberInfo memberInfo = memberInfoService.getMemberInfoByKey(member.getMemberLogin().getMemberId());
            logger.info("---更新用户信息---begin");
            if (memberInfo == null || memberInfo.getMemberId() == null) {
                memberInfo = member.getMemberInfo();
                logger.info("新增：" + memberInfo + "：memberId:" + member.getMemberLogin().getMemberId());
                if (null == memberInfo) {
                    memberInfo = new MemberInfo();
                }
                memberInfo.setMemberId(member.getMemberLogin().getMemberId());
                ThirdLevelAddress province = memberAddressService
                        .getById(Integer.parseInt(memberProvince));
                ThirdLevelAddress city = memberAddressService.getById(Integer
                        .parseInt(memberCity));
                ThirdLevelAddress region = memberAddressService.getById(Integer
                        .parseInt(memberCounty));
                memberInfo.setMemberProvince(province.getOrgId() + ":"
                        + province.getOrgName());
                memberInfo.setMemberCity(city.getOrgId() + ":"
                        + city.getOrgName());
                memberInfo.setMemberCounty(region.getOrgId() + ":"
                        + region.getOrgName());
                memberInfo.setMemberNickname(memberNickname);
                memberInfo.setMemberRealname(memberRealname);
                memberInfo.setMemberSex(memberSex);
                memberInfo.setMemberAddress(memberAddress);
                if(!memberQq.contains("*")){
                    String memberQqDecode = UtilString.clearXss(TriDes.getInstance()
                            .strDec(memberQq, keyStr, null, null));
                    memberInfo.setMemberQq(Long.parseLong(memberQqDecode));
                }

                res = memberInfoService.saveMemberInfo(memberInfo);
            } else {
                memberInfo = member.getMemberInfo();
                logger.info("更新：" + memberInfo + "：memberId:" + member.getMemberLogin().getMemberId());
                if (null == memberInfo) {
                    memberInfo = new MemberInfo();
                }
                ThirdLevelAddress province = memberAddressService
                        .getById(Integer.parseInt(memberProvince));
                ThirdLevelAddress city = memberAddressService.getById(Integer
                        .parseInt(memberCity));
                ThirdLevelAddress region = memberAddressService.getById(Integer
                        .parseInt(memberCounty));
                memberInfo.setMemberProvince(province.getOrgId() + ":"
                        + province.getOrgName());
                memberInfo.setMemberCity(city.getOrgId() + ":"
                        + city.getOrgName());
                memberInfo.setMemberCounty(region.getOrgId() + ":"
                        + region.getOrgName());
                memberInfo.setMemberNickname(memberNickname);
                memberInfo.setMemberRealname(memberRealname);
                memberInfo.setMemberAddress(memberAddress);
                memberInfo.setMemberSex(memberSex);
                if(!memberQq.contains("*")){
                    String memberQqDecode = UtilString.clearXss(TriDes.getInstance()
                            .strDec(memberQq, keyStr, null, null));
                    memberInfo.setMemberQq(Long.parseLong(memberQqDecode));
                }
                memberInfo.setMemberId(member.getMemberLogin().getMemberId());
                res = memberInfoService.updatememberInfo(memberInfo);
            }
            if (!res) {
                return "设置memberInfo失败，请重试";
            }
            logger.info("---更新用户信息---end");
            member.setMemberInfo(memberInfo);
            Session session = UserUtils.getSession();
            JedisClusterUtils.delObject("loginUser" + session.getId());
            JedisClusterUtils.setObject("loginUser" + session.getId(), member,
                    JEDISTIMEOUT);
        } catch (Exception e) {
            logger.error("设置失败：", e);
            return "设置失败，请重试";
        }
        return "success";
    }

    /**
     * 跳转重置密码界面
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RefreshCSRFToken
    @RequestMapping(value = "/toUpdatePass", method = RequestMethod.GET)
    public String toUpdatePass(HttpServletRequest request,
                               HttpServletResponse response, Model model) {
        return "web/member/updatePass";
    }

    /**
     * 发送短信验证码
     *
     * @param request
     * @param response
     * @param mobile
     * @return
     */
    @RequestMapping(value = "/sendSms", method = RequestMethod.POST)
    @ResponseBody
    public String sendSms(HttpServletRequest request,
                          HttpServletResponse response, String mobile) {
        String countstr = JedisClusterUtils.get("MemberInfoSendSmsCount" + mobile);
        Integer count = 0;
        if (StringUtils.isNotEmpty(countstr)) {
            count = Integer.parseInt(countstr);
        }
        if (count >= 10) {
            return "短信验证码发送超过限制，请明天再来！";
        }

        String secondsSmsCountStr = JedisClusterUtils.get("MemberInfoSecondsSmsCountStr" + mobile);//短信1分钟内发送次数
        Integer secondsSmsCount = 0;
        if (StringUtils.isNotEmpty(secondsSmsCountStr)) {
            secondsSmsCount = Integer.parseInt(secondsSmsCountStr);
        }
        if (secondsSmsCount >= 1) {
            return "短信发送时间间隔太短，请稍后再试！";
        }
        UserContext context = new UserContext();
        context.setClientIp(request.getRemoteAddr());
        context.setSessionId(request.getSession().getId());

        String smsUrl = ssoServerHost
                + "ecsuc/remote/userServer/sendSmsCodeByPhone";
        Map<String, String[]> paramMap = new HashMap<String, String[]>();
        paramMap.put("phone", new String[]{mobile});

        String strres = "";
        try {
            strres = HttpClientUtils.doPostAndGetString(smsUrl, paramMap);
            JSONObject jsonRes = JSONObject.parseObject(strres);
            Long code = jsonRes.getLong("code");
            String msg = jsonRes.getString("msg");
            if (code != 200) {
                if (StringUtils.isNotEmpty(msg)) {
                    return msg;
                }
                return "获取短信验证码失败，BOSS服务异常";
            }
            count++;
            secondsSmsCount++;
            JedisClusterUtils.set("MemberInfoSendSmsCount" + mobile, count + "", SMSOUT);
            JedisClusterUtils.set("MemberInfoSecondsSmsCountStr" + mobile, secondsSmsCount + "", SECONDSSMSOUT);
            Session session = UserUtils.getSession();
            JedisClusterUtils.set("MemberInfo" + session.getId() + mobile, mobile, JEDISTIMEOUT);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "获取短信验证码失败";
    }

    /**
     * 校验短信验证码
     *
     * @param request
     * @param response
     * @param mobile
     * @param smsCaptcha
     * @return
     */
    @RequestMapping(value = "/checkSms", method = RequestMethod.POST)
    @ResponseBody
    public String checkSms(HttpServletRequest request,
                           HttpServletResponse response, String mobile, String smsCaptcha) {
        String smsUrl = ssoServerHost
                + "ecsuc/remote/userServer/validateSmsCode";
        UserContext context = new UserContext();
        Session session = UserUtils.getSession();
        context.setClientIp(request.getRemoteAddr());
        context.setSessionId(session.getId() + "");
        String checkMobile = JedisClusterUtils.get("MemberInfo" + session.getId() + mobile);
        if (checkMobile == null) {
            return "请发送短信验证码！";
        } else if (!checkMobile.equals(mobile)) {
            return "短信验证手机与发送手机号不一致！";
        }

        Map<String, String[]> paramMap = new HashMap<String, String[]>();
        paramMap.put("phone", new String[]{mobile});
        paramMap.put("smsCode", new String[]{smsCaptcha});
        String strres = "";
        try {
            strres = HttpClientUtils.doPostAndGetString(smsUrl, paramMap);
            JSONObject jsonRes = JSONObject.parseObject(strres);
            Long code = jsonRes.getLong("code");
            String msg = jsonRes.getString("msg");
            if (code != 200) {
                if (StringUtils.isNotEmpty(msg)) {
                    return msg;
                }
                return "获取短信验证码失败，BOSS服务异常";
            }
            JedisClusterUtils.del("MemberInfo" + session.getId() + mobile);
            JedisClusterUtils.del("MemberInfoSecondsSmsCountStr" + mobile);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "获取短信验证码失败";
    }

    /**
     * 重置密码
     *
     * @param request
     * @param response
     * @return
     */
    @VerifyCSRFToken
    @RequestMapping(value = "/updatePass.json", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String updatePass(HttpServletRequest request,
                             HttpServletResponse response,
                             String memberLogingName, String memberPassword, String oldPass) {
        oldPass = TriDes.getInstance()
                .strDec(oldPass, keyStr, null, null);
        memberPassword = TriDes.getInstance()
                .strDec(memberPassword, keyStr, null, null);
        MemberVo member = UserUtils.getLoginUser(request);
        if (member != null) {
            try {
                MemberLogin memberRel = memberLoginService
                        .getMemberLoginByKey(member.getMemberLogin().getMemberId());
                String passOld = MD5.sign(oldPass, PASSWORD_KEY, "UTF-8");
                if (memberRel != null
                        && StringUtils.equals(memberRel.getMemberPassword(),
                        passOld)) {
                    memberRel.setMemberPassword(memberPassword);
                    boolean res = memberLoginService.updatememberLogin(memberRel);
                    if (!res) {
                        return "密码重置异常，请重试！";
                    }
                } else {
                    return "原密码输入有误，请重新输入！";
                }
            } catch (Exception e) {
                return "密码重置异常，请重试！";
            }
        }
        return "success";
    }

}
