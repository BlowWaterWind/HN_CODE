package com.ai.ecs.ecm.mall.wap.modules.Bargain;

import com.ai.ecs.ecm.mall.wap.modules.BaseController;
import com.ai.ecs.ecsite.modules.myMobile.entity.BasicInfoCondition;
import com.ai.ecs.ecsite.modules.myMobile.service.BasicInfoQryModifyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hacker on 2016/12/9.
 */
@Controller
@RequestMapping(value="/checkmobile")
public class CheckPhoneController extends BaseController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private BasicInfoQryModifyService basicInfoQryModifyService;

    @RequestMapping("extract")
    @ResponseBody
    public Map extract(HttpServletRequest req,String mobile)
    {
        String msg="手机号必须为湖南移动手机号码";
        Map map=null;
        try {
            Map checkMap = checkPhoneNo(mobile);
            if (checkMap.get("respCode").equals("0")){
                map = new HashMap();
                map.put("X_RESULTCODE", "0");
                map.put("X_RESULTINFO", "该手机号是湖南移动手机号码");
            }else
            {
                map = new HashMap();
                map.put("X_RESULTCODE", "-1");
                map.put("X_RESULTINFO", msg);
            }
        }catch (Exception e){
            map = new HashMap();
            map.put("X_RESULTCODE","-1");
            map.put("X_RESULTINFO", "异常");
        }
        return map;
    }
    /**
     * 校验手机号是否为湖南移动号码
     *
     * @param mobile
     * @return Map
     * @throws Exception
     */
    public Map checkPhoneNo(String mobile) throws Exception
    {
        BasicInfoCondition basicInfoCondition = new BasicInfoCondition();
        basicInfoCondition.setSerialNumber(mobile);
        basicInfoCondition.setxGetMode("0");
        Map returnData = basicInfoQryModifyService.queryUserBasicInfo(basicInfoCondition);
        logger.info("移动号码【"+mobile+"】信息："+returnData);
        return returnData;
    }
}
