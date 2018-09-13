package com.ai.ecs.ecm.mall.wap.modules.market;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.ecs.common.utils.DateUtils;
import com.ai.ecs.sales.api.IHomeService;
import com.ai.ecs.sales.entity.PortalSubjectDisplay;
import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("marketHome")
public class MarketHomeController {
	
	@Autowired
	private IHomeService homeService;
	
	  @ResponseBody
	  @RequestMapping("/queryFrontSubjectDisplayList")
	  public void queryFrontSubjectDisplayList(HttpServletRequest request,HttpServletResponse response,Model model) throws IOException{
		  	String marketTypeId = request.getParameter("marketTypeId");
			String channelCode = "E007";
			Map<String,String> map = new HashMap<String,String>();
			map.put("marketTypeId", marketTypeId);
			map.put("channelCode", channelCode);
			List<PortalSubjectDisplay> subjectDisplayList = homeService.queryFrontSubjectDisplayList(map);
			//服务器当前时间
			String nowDate = DateUtils.formatDate(new Date(),"yyyy/MM/dd HH:mm:ss");
			JSONObject resultJson = new JSONObject();
			resultJson.put("subjectDisplayList", subjectDisplayList);
			resultJson.put("nowDate", nowDate);
			response.setContentType("text/html;charset=UTF-8");
			response.setHeader("Pragma", "No-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);
			PrintWriter pw = response.getWriter();
			pw.write(resultJson.toString());
			resultJson.clear();
			pw.flush();
			pw.close();
			
	  }
	
	
	
	
	
}
