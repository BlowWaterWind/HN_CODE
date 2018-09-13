package com.ai.ecs.ecm.mall.wap.modules.goods.controller;

import com.ai.ecs.common.utils.JedisClusterUtils;
import com.ai.ecs.ecm.mall.wap.modules.BaseController;
import com.ai.ecs.ecm.mall.wap.modules.goods.constant.HNanConstant;
import com.ai.ecs.ecm.mall.wap.modules.member.vo.MemberBaseVo;
import com.ai.ecs.ecm.mall.wap.platform.utils.*;
import com.ai.ecs.ecsite.modules.login.service.LoginService;
import com.ai.ecs.ecsite.modules.login.service.entity.UserInfoGetCondition;
import com.ai.ecs.member.api.login.ILoginService;
import com.ai.ecs.member.entity.MemberVo;
import com.ai.ecs.member.entity.UserContext;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import jodd.util.StringUtil;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by cuixiaoqing on 2017/8/7.
 * 手机营业厅跳转商城免登录
 */
@Controller
@RequestMapping("GroupNoLogin")
public class GroupNoLoginController extends BaseController {
    private final static Logger logger = LoggerFactory.getLogger(GroupNoLoginController.class);
    @Autowired
    private LoginService loginService2;

    @Autowired
    private ILoginService loginService;

    private int JEDISTIMEOUT=1800;

    @Value("${ssoServerHost}")
    private String ssoServerHost;

    /**
     * 获取号码，对号码进行解密，并在商城进行登录
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "initPage")
    @ResponseBody
    public Map<String, Object> initPage(HttpServletRequest request, HttpServletResponse response) throws Exception{
        Map<String,Object> dataResult = new HashMap<String,Object>();
        //号码解密
        String serialNumber = "";
        String snEncryptStr = request.getParameter("serialNumber");
        printKaInfo("获取加密串:snEncryptStr:"+snEncryptStr);
        try {
           serialNumber = RSAUtils.decrypt(snEncryptStr);
            printKaInfo("解密后:serialNumber:"+serialNumber);
            if(isAttackReq(snEncryptStr, serialNumber)){
                dataResult.put(HNanConstant.resultCode, HNanConstant.FAIL);
                dataResult.put(HNanConstant.resultInfo, "访问异常");
                return dataResult;
            }
        } catch (Exception e) {
            //解密失败，返回
            dataResult.put(HNanConstant.resultCode, HNanConstant.FAIL);
            dataResult.put(HNanConstant.resultInfo, "系统异常");
            printKaError("initPage1",e);
            return dataResult;
        }

        try {
            logger.error("-----开始执行集团免登录的wapLogin方法------");
            wapLogin(serialNumber, request,response);//商城登录
            logger.error("-----执行集团免登录的wapLogin方法结束------");
            dataResult.put(HNanConstant.resultCode, HNanConstant.SUCCESS);
            logger.error("全部走完");
        } catch (Exception e) {
            dataResult.put(HNanConstant.resultCode, HNanConstant.FAIL);
            dataResult.put(HNanConstant.resultInfo, "系统异常");
            printKaError("initPage2",e);
        }
        logger.error("返回前");
        return dataResult;
    }

    /**
     * 商城登录
     * @param serialNumber
     * @param request
     * @param response
     */
    private void wapLogin(String serialNumber, HttpServletRequest request, HttpServletResponse response) {
        try {
            Session session = UserUtils.getSession();
            printKaInfo("wapLogin登录前用户session:"+session.toString());
            String userKey="loginUser" + session.getId();
            printKaInfo("wapLogin_userKey:" + userKey);
            //当前登录号码是否为传入手机号
            if(JedisClusterUtils.exists(userKey)){
                MemberVo m = (MemberVo) JedisClusterUtils.getObject(userKey);
                printKaInfo("wapLogin_MemberVo:" + m );
                if(m.getMemberLogin()!=null && serialNumber.equals(m.getMemberLogin().getMemberLogingName())){
                    String baseUserKey = "LOGIN_BASEINFO_" + session.getId();
                    String str = JedisClusterUtils.get(baseUserKey);
                    printKaInfo("wapLogin_baseUserKey:" + baseUserKey );
                    printKaInfo("wapLogin_str:" + str );
                    JSONObject jsonObject = JSONObject.parseObject(str);

                    JedisClusterUtils.set(baseUserKey, jsonObject.toJSONString(), JEDISTIMEOUT);
                    return ;
                }else{
                    //退出登陆
                    loginOut(request,response);
                }
            }
            UserContext context = new UserContext();
            context.setClientIp(StringUtils.getRemoteAddr(request));
            context.setMobile(serialNumber);
            Map<String, Object> loginRes =  loginService.loginBySms(context, ""); // 商城登录，首次登录会生成账号并记录用户信息
            printKaInfo("wapLogin_loginRes:" + loginRes );
            if(loginRes==null){
                return ;
            }
            Object obj = loginRes.get("data");
            MemberVo membervo = JSONObject.parseObject(JSONObject.toJSONString(obj), MemberVo.class);
            membervo.getMemberLogin().setMemberPhone(Long.parseLong(serialNumber));
            JedisClusterUtils.setObject("loginUser" + session.getId(), membervo, JEDISTIMEOUT);
            MemberBaseVo baseMember = new MemberBaseVo();
            baseMember.setMemberId(membervo.getMemberLogin().getMemberId());
            baseMember.setMemberPhone(Long.parseLong(serialNumber));
            JedisClusterUtils.set("LOGIN_BASEINFO_" + session.getId(), JSON.toJSONString(baseMember),JEDISTIMEOUT);
            JedisClusterUtils.set("sessionId" + session.getId(), session.getId() + "", JEDISTIMEOUT);
            //保存日志
            MemberLoginLogUtils.saveLog(request, "2", "0", "登录成功", null, serialNumber, membervo);
            printKaInfo("wapLogin登录成功:" + serialNumber );
        } catch (Exception e) {
            printKaError("cqx===wapLogin_exception:",e);
            logger.error("======loginError======", e);
            e.printStackTrace();


        }
    }


    /**
     * isAttackReq 防攻击
     * @param snEncryptStr 密文
     * @param serialNumber 明文
     * @return
     * @return boolean返回说明
     * @Exception 异常说明
     * @author：shubo@asiainfo.com
     * @create：2017年4月19日 下午2:54:16
     * @moduser：
     * @moddate：
     * @remark：
     */
    public boolean isAttackReq(String snEncryptStr, String serialNumber){
        String sidKey = "newInstall_sid_" + serialNumber;
        String sidVal = JedisClusterUtils.get(sidKey);
        if (sidVal == null || !sidVal.equals(snEncryptStr)) {
            JedisClusterUtils.set(sidKey, snEncryptStr, 20);
            return false;
        }else{
            return true;
        }
    }
    /**
     * 退出登陆
     * @param request
     * @param response
     */
    public void loginOut(HttpServletRequest request, HttpServletResponse response)   {
        String logoutUrl = ssoServerHost + "ecsuc/remote/userServer/userLogout";
        Session session = UserUtils.getSession();
        String tkey = CookieUtils.getCookie(request, "ticketId"); // 从COOKIE获取用户登录令牌信息
        String uId = CookieUtils.getCookie(request, "uId");
        try {
            if (StringUtil.isNotBlank(tkey) && StringUtil.isNotBlank(uId)) { // 令牌不为空，则调用单点登录中心登出
                Map<String, String[]> paramMap = new HashMap<String, String[]>();
                paramMap.put("ticketId", new String[] { tkey });
                paramMap.put("uId", new String[] { uId });
                String strres = HttpClientUtils.doPostAndGetString(logoutUrl, paramMap);
                CookieUtils.setCookie(response, "ticketId", "", -1);
                CookieUtils.setCookie(response, "uid", "", -1);
            }
            JedisClusterUtils.delObject("loginUser" + session.getId());
            JedisClusterUtils.delObject("LOGIN_BASEINFO_" + session.getId());
            JedisClusterUtils.del("sessionId" + session.getId());
            UserUtils.getSubject().logout();
        } catch (Exception e) {
            logger.error("========loginOut-------====",e);
        }
    }

}
