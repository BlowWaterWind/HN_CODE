package com.ai.ecs.ecm.mall.wap.modules.member;

import com.ai.ecs.common.utils.JedisClusterUtils;
import com.ai.ecs.ecm.mall.wap.common.security.FormAuthenticationFilter;
import com.ai.ecs.ecm.mall.wap.modules.BaseController;
import com.ai.ecs.ecm.mall.wap.platform.annotation.RefreshCSRFToken;
import com.ai.ecs.ecm.mall.wap.platform.annotation.VerifyCSRFToken;
import com.ai.ecs.ecm.mall.wap.platform.utils.TriDes;
import com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils;
import com.ai.ecs.integral.entity.IntegrayDetail;
import com.ai.ecs.integral.service.IntegrayAccountService;
import com.ai.ecs.integral.service.IntegrayDetailService;
import com.ai.ecs.member.api.IMemberLoginService;
import com.ai.ecs.member.api.login.ILoginService;
import com.ai.ecs.member.api.register.IRegisterService;
import com.ai.ecs.member.entity.MemberLogin;
import com.ai.ecs.member.entity.MemberVo;
import com.ai.ecs.member.entity.UserContext;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping("register")
public class RegisterController extends BaseController {
    @Autowired
    IRegisterService registerService;
    
    @Autowired
	ILoginService loginService;
	
    @Autowired
    IMemberLoginService memberLoginService;
    
    @Autowired
    @Qualifier("integrayAccountServiceImpl")
    IntegrayAccountService integrayAccountService;
    
    @Autowired
    @Qualifier("integrayDetailServiceImpl")
    IntegrayDetailService integrayDetailService;
    
    @Value("${integralRegister}")
    private String integralRegister;
    
    @Value("${integralRegisterTime}")
    private String integralRegisterTime;
    
    private int JEDISTIMEOUT = 1800;

 	private int CAPTCHAJEDISTIMEOUT = 1800;
    
    private static final Logger logger = LoggerFactory.getLogger(RegisterController.class);
    
    private static String keyStr = "ABCDEFGHIJKLMNOP" + "QRSTUVWXYZabcdef"
			+ "ghijklmnopqrstuv" + "wxyz0123456789+/" + "=";

    private static char[] RAND_CODES = new char[] { '0','1','2', '3', '4', '5', '6', '7', '8', '9'};

    /**
     * 跳转到注册页面
     * 
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RefreshCSRFToken
    @RequestMapping(value = "/toRegister", method = RequestMethod.GET)
    public String toRegister(HttpServletRequest request, HttpServletResponse response) {
        return "web/member/registerForm";
    }
    
    //@RequestMapping(value = "/checkUserName", method = RequestMethod.GET)
    @ResponseBody
    public String checkUserName(HttpServletRequest request, HttpServletResponse response,String member_name) {
       try{ 
        MemberLogin login=memberLoginService.getByLoginMame(member_name,-1);
        if(login!=null&&login.getMemberId()!=null){
            return "该用户名已存在";
        }
       }catch(Exception e){
           
       }
        return "success";
    }
    
    private void doLogin(HttpServletRequest request, String loginname, String password){
    	Session session=UserUtils.getSession();
    	UserContext context = new UserContext();
		context.setClientIp(request.getRemoteHost());
		context.setSessionId(session.getId()+"");
		context.setChannelCode("E006");
		try {
			Map<String, Object> member =null;
			try{
				member= loginService.loginByName(context, loginname,
					password);
			}catch(NullPointerException e){
				logger.error("注册登录失败:",e);
			}
			JedisClusterUtils.set("sessionId" + session.getId(),
					session.getId() + "", JEDISTIMEOUT);

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
     * 用户名注册
     * @param session
     * @param request
     * @param response
     * @param member_name
     * @param member_pass
     * @param captcha
     * @return
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
        String captchaReal=  JedisClusterUtils.get("VC_VALUE"+session.getId());
        if(StringUtils.isEmpty(captchaReal)){
            failNum++;
            JedisClusterUtils.set(failNumKey,failNum+"",60*60*24);
            return "验证码失效，请刷新";
        }
        if(captchaReal!=null&&!captchaReal.equalsIgnoreCase(captcha)){
            failNum++;
            JedisClusterUtils.set(failNumKey,failNum+"",60*60*24);
            return "验证码校验失败，请重新输入";
        }
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
        try
        {
           MemberLogin loginUser= registerService.registerByName(member);
           if(loginUser!=null){
               try{
               //会员注册送积分
                   IntegrayDetail integrayDetail=new IntegrayDetail();
                   integrayDetail.setIntegralSourceId(Short.parseShort("1"));
                   integrayDetail.setIntegralSourceName("注册赠送");
                   integrayDetail.setIntegrayDetailDesc("注册赠送积分");
                   Calendar cal = Calendar.getInstance(); 
                   cal.add(Calendar.DATE, Integer.parseInt(integralRegisterTime));   
                   Date date = cal.getTime();
                   integrayDetail.setIntegrayDetailEndTiem(date);
                   integrayDetail.setMemberId(loginUser.getMemberId());
                   integrayDetail.setMemberLoginName(loginUser.getMemberLogingName());
                   integrayDetail.setIntegrayDetailAmount(Long.parseLong(integralRegister));
                   integrayDetail.setShopId(1L);
                   integrayDetail.setShopName("自营");
                   integrayDetail.setIntegrayStatusId(Short.parseShort("1"));
                   integrayDetail.setIntegrayStatusName("正常");
                   integrayDetail.setIntegrayTypeId(Short.parseShort("1"));
                   integrayDetail.setIntegrayTypeName("赠送");
                 
                   String resDetail=integrayDetailService.insert(integrayDetail);
                   if(StringUtils.isNotEmpty(resDetail)){
                       logger.error("积分明细增加失败，失败原因："+resDetail);
                   }
                   
               }catch(Exception e){
                   logger.error("积分增加失败，失败原因：",e);
               }
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
    
    
    /**
     * 获取图片验证码
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(value="getCaptchaImage.do",method = RequestMethod.GET)
    public void getCaptchaImage(HttpServletRequest request, HttpServletResponse response ) throws IOException {
        // 禁止缓存
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "No-cache");
        response.setDateHeader("Expires", 0);
        // 指定生成的响应是图片
        response.setContentType("image/jpeg");
        int width = 200;
        int height = 60;
        // 创建BufferedImage类的对象
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // 创建Graphics类的对象
        Graphics g = image.getGraphics();
        // 通过Graphics类的对象创建一个Graphics2D类的对象
        Graphics2D g2d = (Graphics2D) g;
        // 实例化一个Random对象
        Random rand = new Random();
        // 通过Font构造字体
        Font nFont = g.getFont();
        g.setFont(nFont.deriveFont(50F));
        // 改变图形的当前颜色为随机生成的颜色
        g.setColor(getRandColor(200, 250));
        // 绘制一个填色矩形
        g.fillRect(0, 0, width, height);
        // 画一条折线
        BasicStroke bs = new BasicStroke(2f, BasicStroke.CAP_BUTT,
        // 创建一个供画笔选择线条粗细的对象
                BasicStroke.JOIN_BEVEL);
        // 改变线条的粗细
        g2d.setStroke(bs);
        // 设置当前颜色为预定义颜色中的深灰色
        g.setColor(Color.DARK_GRAY);
        int[] xPoints = new int[3];
        int[] yPoints = new int[3];
        for (int j = 0; j < 3; j++) {
            xPoints[j] = rand.nextInt(width - 1);
            yPoints[j] = rand.nextInt(height - 1);
        }
        g.drawPolyline(xPoints, yPoints, 3);
        // 生成并输出随机的验证文字
        StringBuilder sRand = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            char ctmp = RAND_CODES[rand.nextInt(RAND_CODES.length)];
            sRand.append(ctmp);
            Color color = new Color(20 + rand.nextInt(110), 20 + rand.nextInt(110), 20 + rand.nextInt(110));
            g.setColor(color);
            /**** 随机缩放文字并将文字旋转指定角度 **/
            // 将文字旋转指定角度
            // Graphics2D g2d_word = (Graphics2D) g;
            AffineTransform trans = new AffineTransform();
            trans.rotate(rand.nextInt(45) * 3.14 / 180, 15 * i + 10, 7);
            // 缩放文字
            float scaleSize = rand.nextFloat() + 0.8f;
            if (scaleSize > 1.1f)
                scaleSize = 1f;
            trans.scale(scaleSize, scaleSize);
            // g2d_word.setTransform(trans);
            /************************/
            g.drawString(String.valueOf(ctmp), 30 * i + 40, 35 + rand.nextInt(17));
        }
        g.dispose();

        // 将生成的验证码保存到Session中
        Session session = UserUtils.getSession();
        request.setAttribute("sessionId", session.getId());
        JedisClusterUtils.set("VC_VALUE"+session.getId(),  sRand.toString(),CAPTCHAJEDISTIMEOUT);
        JedisClusterUtils.set("VC_CREATE_TIME"+session.getId(), System.currentTimeMillis()+"",CAPTCHAJEDISTIMEOUT); 
        ImageIO.write(image, "JPEG", response.getOutputStream());
    }
    
    
    

    private Color getRandColor(int s, int e) {
        Random random = new Random();
        if (s > 255) {
            s = 255;
        }
        if (e > 255) {
            e = 255;
        }
        int r = s + random.nextInt(e - s);
        int g = s + random.nextInt(e - s);
        int b = s + random.nextInt(e - s);
        return new Color(r, g, b);
    }
}
