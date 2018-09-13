package com.ai.ecs.ecm.mall.wap.modules.goods.controller;


import com.ai.ecs.common.ResponseBean;
import com.ai.ecs.common.web.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * Created by cuixiaoqing on 2017/8/7.
 * 手机营业厅跳转商城免登录
 */
@Controller
@RequestMapping("GetIp")
public class GetIpv6Controller extends BaseController {


    /**
     * 获取客户端ip
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "getIPv6")
    @ResponseBody
    public Object getClientIp(HttpServletRequest request) {

        ResponseBean dataResult = new ResponseBean();
        String clientIp = "";
        try {

            clientIp = request.getHeader("x-forwarded-for");
            //取客户端IP
            //经过F5网关处理后的客户端IP地址
            //有多个IP的情况下，取最后一个
            if (clientIp.contains(",") || clientIp.contains("，")) {
                clientIp = clientIp.replace("，", ",");
                String[] clientIpItem = clientIp.split(",");
                clientIp = clientIpItem[clientIpItem.length - 1].trim();
            }
        } catch (Exception e) {
            // 捕获手机号码获取时可能产生的异常
            //LoggerPrint.logWebInfo(e.getMessage());
        }
        dataResult.addSuccess(clientIp);

        return dataResult;

    }
}
