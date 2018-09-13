package com.ai.ecs.ecm.mall.wap.modules.member;

import com.ai.ecs.common.utils.JedisClusterUtils;
import com.ai.ecs.ecm.mall.wap.modules.BaseController;
import com.ai.ecs.ecm.mall.wap.platform.annotation.RefreshCSRFToken;
import com.ai.ecs.ecm.mall.wap.platform.annotation.VerifyCSRFToken;
import com.ai.ecs.ecm.mall.wap.platform.utils.CSRFTokenUtil;
import com.ai.ecs.ecm.mall.wap.platform.utils.TriDes;
import com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils;
import com.ai.ecs.ecsite.modules.sms.entity.SmsSendCondition;
import com.ai.ecs.ecsite.modules.sms.service.SmsSendService;
import com.ai.ecs.member.api.IMemberLoginService;
import com.ai.ecs.member.api.IMemberSecurityService;
import com.ai.ecs.member.api.IPwdQuestionService;
import com.ai.ecs.member.entity.MemberLogin;
import com.ai.ecs.member.entity.MemberSecurity;
import com.ai.ecs.member.entity.MemberVo;
import com.ai.ecs.member.entity.PwdQuestion;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Controller
@RequestMapping("memberSecurity")
public class MemberSecurityController extends BaseController {
	private static final Logger logger = LoggerFactory
			.getLogger(MemberSecurityController.class);

	@Autowired
	private SmsSendService smsSendService;

	@Autowired
	IMemberSecurityService memberSecurityService;

	@Autowired
	private IPwdQuestionService pwdQuestionService;

	@Autowired
	IMemberLoginService memberLoginService;

	private String PASSWORD_KEY = "MOBILE.COM";

	private int SMSOUT = 60 * 60 * 24;

	private int JEDISTIMEOUT = 1800;

	private int SECONDSSMSOUT = 55;

	private static String keyStr = "ABCDEFGHIJKLMNOP" + "QRSTUVWXYZabcdef"
			+ "ghijklmnopqrstuv" + "wxyz0123456789+/" + "=";

	@Value("${ssoServerHost}")
	private String ssoServerHost;

	/**
	 * 跳转到校验会员密保
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/toSetMemberSecurity", method = RequestMethod.GET)
	public String toSetMemberSecurity(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		return "web/member/checkMemberSecurity";
	}

	@RequestMapping(value = "/toSetUserName", method = RequestMethod.GET)
	@RefreshCSRFToken
	public String toSetUserName(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		return "web/member/findByUsername";
	}

	/**
	 * 跳转到校验会员密保
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RefreshCSRFToken
	@RequestMapping(value = "/saveMemberSecurity")
	public String saveMemberSecurity(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		List<PwdQuestion> pwdList = pwdQuestionService.getAllPwdList();
		List<PwdQuestion> pwdListSelect = new ArrayList<PwdQuestion>();
		if (!CollectionUtils.isEmpty(pwdList)) {
			for (PwdQuestion que : pwdList) {
				if (pwdListSelect.size() >= 3) {
					break;
				}
				pwdListSelect.add(que);

			}
		}
		request.setAttribute("pwdList", pwdListSelect);
		return "web/member/setMemberSecurity";
	}

	@RequestMapping(value = "setUserName.do")
	@VerifyCSRFToken
	public String setUserName(HttpServletRequest request,
			HttpServletResponse response, Model model, String loginName) {
		try {
			if(StringUtils.isEmpty(loginName)){
				return "web/member/findByUsername";
			}
			MemberLogin login = memberLoginService.getByLoginMame(loginName,-1);
			if (login == null) {
				return "web/member/resetPassMethod";
			}
			List<MemberSecurity> securityList = memberSecurityService
					.getByMemberId(login.getMemberId());
			if (CollectionUtils.isEmpty(securityList)) {
				return "web/member/resetPassMethod";
			}
			Session session = UserUtils.getSession();
			session.setAttribute("memberId", login.getMemberId());
			request.setAttribute("securityList", securityList);
		} catch (Exception e) {
			logger.error("设置用户名失败", e);
		}
		return "web/member/checkMemberSecurity";
	}

	/**
	 * 设置会员密保
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @param memberId
	 * @param pwdQuestionName
	 * @param pwdQuestionAnswer
	 * @return
	 */
	@VerifyCSRFToken
	@RequestMapping(value = "setMemberSecurity.json", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String setMemberSecurity(HttpServletRequest request,
			HttpServletResponse response, Model model,
			String pwdQuestionName, String pwdQuestionAnswer) {
		boolean res=false;
		try {
			MemberVo member = UserUtils.getLoginUser(request);
			if (member != null) {
				Long memberId = member.getMemberLogin().getMemberId();

				pwdQuestionAnswer = TriDes.getInstance().strDec(pwdQuestionAnswer,
						keyStr, null, null);
				List<MemberSecurity> securityList = memberSecurityService
						.getByMemberId(memberId);
				if (!CollectionUtils.isEmpty(securityList)) {
					return "该帐户已设置密保，请进行密保验证";
				}
				MemberSecurity security = new MemberSecurity();
				security.setMemberId(memberId);
				security.setPwdQuestionName(pwdQuestionName);
				security.setPwdQuestionAnswer(pwdQuestionAnswer);
				res = memberSecurityService.saveMemberSecurity(security);

			}
		} catch (Exception e) {
			throw e;
		}

		if (res) {
			request.getSession().setAttribute("CSRFToken", CSRFTokenUtil.generate(request));
			return "设置成功";
		} else {
			return "设置失败";
		}
	}

	/**
	 * 校验会员密保
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/toCheckMemberSecurity.json", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String toCheckMemberSecurity(HttpServletRequest request,
			HttpServletResponse response, Model model, String loginName) {
		try {
			MemberLogin login = memberLoginService.getByLoginMame(loginName,-1);
			if (login == null) {
				return "请先设置用户名！";
			}
			Session session = UserUtils.getSession();
			List<MemberSecurity> securityList = memberSecurityService
					.getByMemberId(login.getMemberId());
			if (!CollectionUtils.isEmpty(securityList)) {
				session.setAttribute("security", securityList.get(0));
			} else {
				return "您未开通密保，请先设置密保";
			}
		} catch (Exception e) {
			return "校验密保失败";
		}
		return "success";
	}

	/**
	 * 跳转重置密码选择方式界面
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RefreshCSRFToken
	@RequestMapping(value = "/toResetPassword", method = RequestMethod.GET)
	public String toResetPassword(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		return "web/member/resetPassMethod";
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
	@RequestMapping(value = "/toResetPass", method = RequestMethod.GET)
	public String toResetPass(HttpServletRequest request,
			HttpServletResponse response, Model model, Long memberId,
			Long mobile) {
		Session session = UserUtils.getSession();
		MemberLogin login = new MemberLogin();
		try {
			if (memberId != null) {
				login = memberLoginService.getMemberLoginByKey(memberId);
			} else if (mobile != null) {
				login = memberLoginService.getByMobile(mobile);
			}
		} catch (Exception e) {
			logger.error("重置密码失败：", e);
			return "redirect:/memberSecurity/toSetMemberSecurity";
		}
		String findpassKey = (String) session.getAttribute("findPassKey");
		if (StringUtils.isNotEmpty(findpassKey)
				&& findpassKey.contains(PASSWORD_KEY) && login != null) {
			request.setAttribute("memberId", login.getMemberId());
			request.setAttribute("loginName", login.getMemberLogingName());
			return "web/member/resetPass";
		} else {
			return "redirect:/memberSecurity/toSetMemberSecurity";
		}
	}

	/**
	 * 重置密码
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@VerifyCSRFToken
	@RequestMapping(value = "/resetPass.json", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String resetPass(HttpServletRequest request,
			HttpServletResponse response, Long memberId,
			String memberLogingName, String memberPassword) {
		memberPassword = TriDes.getInstance().strDec(memberPassword, keyStr,
				null, null);
		if (memberId != null) {
			Session session = UserUtils.getSession();
			String findpassKey = (String) session.getAttribute("findPassKey");
			if (StringUtils.isNotEmpty(findpassKey)
					&& findpassKey.contains(PASSWORD_KEY)) {
				try {
					MemberLogin memberRel = memberLoginService
							.getMemberLoginByKey(memberId);
					memberRel.setMemberPassword(memberPassword);
					boolean res = memberLoginService
							.updatememberLogin(memberRel);
					if (!res) {
						return "密码重置异常，请重试！";
					}
				} catch (Exception e) {
					return "密码重置异常，请重试！";
				}
			} else {
				return "请先检验会员密保！";
			}
		}
		return "success";
	}

	/**
	 * 校验会员密保
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @param memberId
	 * @param pwdQuestionName
	 * @param pwdQuestionAnswer
	 * @return
	 */
	@RequestMapping(value = "checkMemberSecurity.json", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String checkMemberSecurity(HttpServletRequest request,
			HttpServletResponse response, Model model, Long memberId,
			String pwdQuestionName, String pwdQuestionAnswer) {
		try {
			pwdQuestionAnswer = TriDes.getInstance().strDec(pwdQuestionAnswer,
					keyStr, null, null);
			MemberSecurity security = new MemberSecurity();
			List<MemberSecurity> securityList = memberSecurityService
					.getByMemberId(memberId);
			if (!CollectionUtils.isEmpty(securityList)) {
				security = securityList.get(0);
				if (security.getPwdQuestionName().equals(pwdQuestionName)
						&& security.getPwdQuestionAnswer().equals(
								pwdQuestionAnswer)) {
					Session session = UserUtils.getSession();
					session.setAttribute("findPassKey", session.getId()
							.toString() + PASSWORD_KEY);
					return "success";
				} else {
					return "校验失败，密保答案错误";
				}
			}
		} catch (Exception e) {
			throw e;
		}
		return "校验失败";

	}

	@RequestMapping(value = "/findPassByPhone", produces = "application/json;charset=UTF-8")
	@RefreshCSRFToken
	public String findPassByPhone(HttpServletRequest request,
			HttpServletResponse response) {
		return "web/member/findPassByPhone";
	}

	@RequestMapping(value = "/sendSms", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String sendSms(HttpServletRequest request,
			HttpServletResponse response, String mobile) {
		String countstr = JedisClusterUtils.get("FindPasswordSendSmsCount"
				+ mobile);
		Integer count = 0;
		if (StringUtils.isNotEmpty(countstr)) {
			count = Integer.parseInt(countstr);
		}
		if (count >= 10) {
			return "短信验证码发送超过限制，请明天再来！";
		}

		String secondsSmsCountStr = JedisClusterUtils
				.get("FindPasswordSecondsSmsCountStr" + mobile);// 短信1分钟内发送次数
		Integer secondsSmsCount = 0;
		if (StringUtils.isNotEmpty(secondsSmsCountStr)) {
			secondsSmsCount = Integer.parseInt(secondsSmsCountStr);
		}
		if (secondsSmsCount >= 1) {
			return "短信发送时间间隔太短，请稍后再试！";
		}
		if (!JedisClusterUtils.exists(mobile + "_FindPassWord_Vcode_Wap")){
			if (StringUtils.isNotBlank(mobile)) {
				sendVcode(mobile,request);
				count++;
				secondsSmsCount++;
				JedisClusterUtils.set("FindPasswordSendSmsCount" + mobile, count
						+ "", SMSOUT);
				JedisClusterUtils.set("FindPasswordSecondsSmsCountStr" + mobile,
						secondsSmsCount + "", SECONDSSMSOUT);
				Session session = UserUtils.getSession();
				JedisClusterUtils.set("FindPassword" + session.getId() + mobile,
						mobile, JEDISTIMEOUT);
				return "success";
			} else {
				return "系统繁忙，请稍后再试";
			}
		}else{
			return "短信密码1分钟之内有效，请不要重复获取";
		}
	}

	/**
	 * 校验短信验证码
	 * 
	 * @param request
	 * @param response
	 * @param mobile
	 * @param smsCaptcha
	 * @param imageCaptcha
	 * @return
	 */
	@VerifyCSRFToken
	@RequestMapping(value = "/checkSms", method = RequestMethod.POST)
	@ResponseBody
	public String checkSms(HttpServletRequest request,
			HttpServletResponse response, String mobile, String smsCaptcha) {
		Session session = UserUtils.getSession();
		String checkMobile=JedisClusterUtils.get("FindPassword"+session.getId()+mobile);
		if(checkMobile==null){
			return "请发送短信验证码！";
		}else if(!checkMobile.equals(mobile)){
			return "短信验证手机与发送手机号不一致！";
		}
		// 如果短信验证码未失效
		if (JedisClusterUtils.exists(mobile + "_FindPassWord_Vcode_Wap")) {
			String vcode = JedisClusterUtils.get(mobile + "_FindPassWord_Vcode_Wap");
			if (vcode.equals(smsCaptcha)) {
				session.setAttribute("findPassKey", session.getId().toString()
						+ PASSWORD_KEY);
				JedisClusterUtils.del("FindPassword"+session.getId()+mobile);
				JedisClusterUtils.del("FindPasswordSecondsSmsCountStr"+mobile);
				return "success";
			} else {
				return "短信密码错误";
			}
		} else {
			return "短信密码已失效";
		}
	}

	private String getFixLengthString(int strLength) {

		Random rm = new Random();

		// 获得随机数
		double pross = (1 + rm.nextDouble()) * Math.pow(10, strLength);

		String fixLengthString = String.valueOf(pross);

		return fixLengthString.substring(1, strLength + 1);

	}

	/**
	 * sendSms 方法作用:发送短信
	 *
	 * @param serialNumber
	 * @param noticeContent
	 * @throws Exception
	 * @return void返回说明
	 * @Exception 异常说明
	 * @author：xxx@asiainfo.com
	 * @create：2016年4月16日 上午11:10:44 @moduser： @moddate： @remark：
	 */
	private void sendSms(String serialNumber, String noticeContent) {
		SmsSendCondition condition = new SmsSendCondition();
		condition.setSerialNumber(serialNumber);
		condition.setNoticeContent(noticeContent);
		try {
			smsSendService.sendSms(condition);
		} catch (Exception e) {
			logger.error("==smsSendService.sendSms==:", e);
		}
	}
	private void sendVcode(String tel,HttpServletRequest request) {
		String vcode = getFixLengthString(6);
		// 发送短信码
		System.out.println("随机短信密码：" + vcode);
		sendSms(tel, "尊敬的用户，您的随机验证码为：" + vcode);
		JedisClusterUtils.set(tel + "_FindPassWord_Vcode_Wap", vcode, 1 * 60);
	}
}
