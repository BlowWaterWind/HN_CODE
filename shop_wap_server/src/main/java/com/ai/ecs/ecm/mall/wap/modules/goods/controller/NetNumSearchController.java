package com.ai.ecs.ecm.mall.wap.modules.goods.controller;

import com.ai.ecs.ecsite.modules.netNumServer.entity.NetNum;
import com.ai.ecs.ecsite.modules.netNumServer.entity.NetNumQueryCondition;
import com.ai.ecs.ecsite.modules.netNumServer.service.NetNumServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * catalogGoodsController
 * 
 * @author taomy
 *
 */
@Controller
@RequestMapping("netNum")
public class NetNumSearchController{

	@Autowired
    NetNumServerService netNumServerService;
	
	/**
	 * 网上号码查询
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
    @RequestMapping(value = "netNumQuery",method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> netNumQuery(Map paramMap) throws Exception {

        Map<String, Object> returnMap = new HashMap<String, Object>(); // 存储栏目信息
		NetNumQueryCondition condition = new NetNumQueryCondition();
		condition.setConditionExtendsMap(paramMap);
        List<NetNum> netNumList =  netNumServerService.netNumQuery(condition);
        returnMap.put("NET_NUM_LIST",netNumList);
        
        return returnMap;
    }
	
	


}