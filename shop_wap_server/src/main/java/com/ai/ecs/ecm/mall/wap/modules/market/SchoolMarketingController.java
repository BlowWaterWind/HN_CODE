package com.ai.ecs.ecm.mall.wap.modules.market;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.ecs.common.utils.Collections3;
import com.ai.ecs.common.utils.JedisClusterUtils;
import com.ai.ecs.ecm.mall.wap.modules.BaseController;
import com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils;
import com.ai.ecs.ecsite.modules.mobilePayHongBao.entity.MobilePayHongBaoCondition;
import com.ai.ecs.ecsite.modules.mobilePayHongBao.service.IMobilePayHongBaoService;
import com.ai.ecs.member.entity.MemberLogin;
import com.ai.ecs.sales.api.IRedPacketService;
import com.ai.ecs.sales.entity.RedPacketConfig;
import com.ai.ecs.sales.entity.RedPacketGrabRecord;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;

@Controller
@RequestMapping("schoolMarketing")
public class SchoolMarketingController extends BaseController{
	
private int CAPTCHAJEDISTIMEOUT = 1800;
	
	private static char[] RAND_CODES = new char[] { '0', '1', '2', '3', '4',
		'5', '6', '7', '8', '9' };
	
	
	@Autowired
	private IRedPacketService redPacketService;
	
	@Autowired
	private IMobilePayHongBaoService mobilePayHongBaoService;
	

	
	@ResponseBody
	@RequestMapping("/testShowData")
	public Map<String,Object>  testShowData(HttpServletRequest request,HttpServletResponse response){
		String oper = request.getParameter("oper");
		Map<String,Object> resultMap = Maps.newHashMap();
		
		try{
			if("show".equals(oper)){
				//获得库存
				String redPacketStockJSON = JedisClusterUtils.get("SCHOOL_MARKRTING_REDPACKET_STOCK"); 
				String redPacketJSON = JedisClusterUtils.get("SCHOOL_MARKRTING_REDPACKET"); 
				resultMap.put("SCHOOL_MARKRTING_REDPACKET_STOCK", redPacketStockJSON);
				resultMap.put("SCHOOL_MARKRTING_REDPACKET", redPacketJSON);
			}
			else if("del".equals(oper)){
				JedisClusterUtils.del("SCHOOL_MARKRTING_REDPACKET"); 
				JedisClusterUtils.del("SCHOOL_MARKRTING_REDPACKET_STOCK"); 
				resultMap.put("code", "0");
				resultMap.put("message", "删除成功!");
			}
			else if("add".equals(oper)){
				//获得今天参与秒杀的红包
				Date now = new Date();
				List<RedPacketConfig>  redPacketConfigList = redPacketService.queryRedPacketByTime(now);
				if(!Collections3.isEmpty(redPacketConfigList)){
					RedPacketConfig redPacketConfig = redPacketConfigList.get(0);
					JedisClusterUtils.set("SCHOOL_MARKRTING_REDPACKET",  JSONObject.toJSONString(redPacketConfig), 86400);
					JedisClusterUtils.expires("SCHOOL_MARKRTING_REDPACKET",86400*7);
					JedisClusterUtils.set("SCHOOL_MARKRTING_REDPACKET_STOCK",JSONObject.toJSONString(redPacketConfig.getRedPacketStock()), 86400);
					JedisClusterUtils.expires("SCHOOL_MARKRTING_REDPACKET_STOCK",86400*7);
					resultMap.put("code", "0");
					resultMap.put("memssage", "添加成功!");
				}
				else {
					resultMap.put("code", "-1");
					resultMap.put("memssage", "删除失败,没有配置红包!");
				}
			}
			
		}
		catch(Exception e){
			resultMap.put("code", "-1");
			resultMap.put("memssage",e.getMessage());
		}
		return resultMap;
		
	}
	
	
	@ResponseBody
	@RequestMapping("/testGrant")
	public Map<String,Object>  testGrant(HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> resultMap = Maps.newHashMap();
		//获得今天参与秒杀的红包
		Date now = new Date();
		List<RedPacketGrabRecord>  redPacketGrabRecordList = redPacketService.queryGrantRedPacketByDate(now);
		if(!Collections3.isEmpty(redPacketGrabRecordList)){
			for(RedPacketGrabRecord redPacketGrabRecord : redPacketGrabRecordList){
				redPacketGrabRecord.setIsGrant(1);
				boolean flag = redPacketService.updateRedPacketGrabRecordGrand(redPacketGrabRecord);
				if(flag){
					//TODO 调发放红包的接口
					MobilePayHongBaoCondition condition = new MobilePayHongBaoCondition();
					condition.setTradeTypeCode("7008");
					condition.setCredenceBatchNo(redPacketGrabRecord.getRedPacketBatchNum()+"");
					condition.setSerialNumber(redPacketGrabRecord.getPhoneNum());
					try {
						Map<String,Object> map = mobilePayHongBaoService.mobilePayHongBao(condition);
						logger.info(JSONArray.toJSONString(map));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						resultMap.put("memssage", e.getMessage());
					}
					
				}
				else {
					logger.error("校园营销发放红包任务: 修改发放状态失败,redPacketGrabRecordId="+redPacketGrabRecord.getRedPacketGrabRecordId());
				}
				
			}
			resultMap.put("memssage", "发放成功");
		}
		else {
			resultMap.put("memssage", "发放失败");
			
		}
		return resultMap;
		
	}
	
	
	
	
	
	@ResponseBody
	@RequestMapping("/queryRedPacketInfo")
	public String  queryRedPacketInfo(HttpServletRequest request,HttpServletResponse response,String callback){
		try{
			//获得参与秒杀的红包
			String redPacketJSON = JedisClusterUtils.get("SCHOOL_MARKRTING_REDPACKET"); 
			return callback + "(" + redPacketJSON + ")";
		}
		catch(Exception e){
			logger.error("查询校园营销活动异常:"+e);
		}
		return null;
	}
	
	
	@ResponseBody
	@RequestMapping("/secKillRedPacket")
	public String secKillRedPacket(HttpServletRequest request,HttpServletResponse response,RedPacketGrabRecord redPacketGrabRecord,String callback){
		Map<String,Object> resultMap = Maps.newHashMap();
		try{
			//验证码
			String verCode = request.getParameter("verCode");
			Session session = UserUtils.getSession();
			String captchaReal = (String) JedisClusterUtils.get("VC_VALUE_LOGIN"
					+ session.getId());
			if(verCode.equals(captchaReal)){
				resultMap.put("resultCode", "fail");
				resultMap.put("resultInfo", "验证码不正确!");
				String resultMapJSON = JSONObject.toJSONString(resultMap);
				return resultMapJSON;
			}
			//获得参与秒杀的红包
			String redPacketJSON = JedisClusterUtils.get("SCHOOL_MARKRTING_REDPACKET"); 
			RedPacketConfig  redPacketConfig = JSONObject.parseObject(redPacketJSON,RedPacketConfig.class);
			//获得库存
			String redPacketStockJSON = JedisClusterUtils.get("SCHOOL_MARKRTING_REDPACKET_STOCK"); 
			Long redPacketStock = JSONObject.parseObject(redPacketStockJSON,Long.class);
			
			//秒杀验证
			Date now = new Date();
			//是否在活动期间
			if(now.getTime() < redPacketConfig.getRedPacketStarttime().getTime()){
				resultMap.put("resultCode", "fail");
				resultMap.put("resultInfo", "活动未开始!");
				String resultMapJSON = JSONObject.toJSONString(resultMap);
				return resultMapJSON;
			}
			if(now.getTime()>redPacketConfig.getRedPacketEndtime().getTime()){
				resultMap.put("resultCode", "fail");
				resultMap.put("resultInfo", "活动已结束!");
				String resultMapJSON = JSONObject.toJSONString(resultMap);
				return resultMapJSON;
			}
			if(redPacketStock==0){
				resultMap.put("resultCode", "fail");
				resultMap.put("resultInfo", "购机券已抢完!");
				String resultMapJSON = JSONObject.toJSONString(resultMap);
				return resultMapJSON;
			}
			
			 
			String ip = getRemoteAddr(request);
			MemberLogin memberLogin = UserUtils.getLoginUser(request).getMemberLogin();
			String phoneNum = memberLogin.getMemberPhone()+"";
			if(StringUtils.isEmpty(phoneNum)){
				phoneNum = memberLogin.getMemberLogingName();
			}
			
			redPacketGrabRecord.setRedPacketId(redPacketConfig.getRedPacketId());
			redPacketGrabRecord.setRedPacketBatchNum(redPacketConfig.getRedPacketBatchNum());
			redPacketGrabRecord.setRedPacketName(redPacketConfig.getRedPacketName());
			redPacketGrabRecord.setMemberId(memberLogin.getMemberId());
			redPacketGrabRecord.setPhoneNum(phoneNum);
			redPacketGrabRecord.setIp(ip);
			redPacketGrabRecord.setGrabTime(new Date());
			redPacketGrabRecord.setIsGrant(0);
			redPacketGrabRecord.setChannelCode("E007");
			//判断是否已参与本次秒杀
			RedPacketGrabRecord record  = redPacketService.queryRedPacketGrabRecord(redPacketGrabRecord);
			if(record!=null){
				resultMap.put("resultCode", "fail");
				resultMap.put("resultInfo", "您已参与过本次秒杀!");
				String resultMapJSON = JSONObject.toJSONString(resultMap);
				return resultMapJSON;
			}
			boolean isInsertRecord = redPacketService.insertRedPacketGrabRecord(redPacketGrabRecord);
			if(isInsertRecord){
				resultMap.put("resultCode", "success");
				resultMap.put("resultInfo", "秒杀成功!");
			}
		}
		catch(Exception e){
			e.printStackTrace();
			resultMap.put("resultCode", "fail");
			resultMap.put("resultInfo", "秒杀失败!");
		}
		String resultMapJSON = JSONObject.toJSONString(resultMap);
		return callback + "(" + resultMapJSON + ")";
	}
	
	
	/**
	 * 获取图片验证码
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "getCaptchaImage.do", method = RequestMethod.GET)
	public void getCaptchaImage(HttpServletRequest request,
			HttpServletResponse response, String type) throws IOException {
		// 禁止缓存
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "No-cache");
		response.setDateHeader("Expires", 0);
		// 指定生成的响应是图片
		response.setContentType("image/jpeg");
		int width = 200;
		int height = 60;
		// 创建BufferedImage类的对象
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
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
			Color color = new Color(20 + rand.nextInt(110),
					20 + rand.nextInt(110), 20 + rand.nextInt(110));
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
			g.drawString(String.valueOf(ctmp), 30 * i + 40,
					35 + rand.nextInt(17));
		}
		g.dispose();

		// 将生成的验证码保存到Session中
		Session session = UserUtils.getSession();
		request.setAttribute("sessionId", session.getId());
		if (StringUtils.isEmpty(type)) {
			JedisClusterUtils.set("VC_VALUE_MARKETING" + session.getId(),
					sRand.toString(), CAPTCHAJEDISTIMEOUT);
			JedisClusterUtils.set("VC_CREATE_TIME" + session.getId(),
					System.currentTimeMillis() + "", CAPTCHAJEDISTIMEOUT);
		} else if ("findPass".equals(type)) {
			JedisClusterUtils.set("VC_VALUE_MARKETING_FINDPASS" + session.getId(),
					sRand.toString(), CAPTCHAJEDISTIMEOUT);
			JedisClusterUtils.set("VC_CREATE_TIME_FINDPASS" + session.getId(),
					System.currentTimeMillis() + "", CAPTCHAJEDISTIMEOUT);
		}
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
	
	
	/**
	 * 获得用户远程地址
	 */
	public static String getRemoteAddr(HttpServletRequest request){
		String remoteAddr = request.getHeader("X-Real-IP");
        if (StringUtils.isNotBlank(remoteAddr)) {
        	remoteAddr = request.getHeader("X-Forwarded-For");
        }else if (StringUtils.isNotBlank(remoteAddr)) {
        	remoteAddr = request.getHeader("Proxy-Client-IP");
        }else if (StringUtils.isNotBlank(remoteAddr)) {
        	remoteAddr = request.getHeader("WL-Proxy-Client-IP");
        }
        return remoteAddr != null ? remoteAddr : request.getRemoteAddr();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
