package com.ai.ecs.ecm.mall.wap.modules;


import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisCluster;

/**
 * Created by Administrator on 2017/6/2.
 */
public class HeBaoController extends BaseController {

    @Autowired
    protected JedisCluster jedisCluster;

    private static String SHARE_PRE = "SHARE_";

    /**
     * 是否已分享
     * @param activityId
     * @return
     */
    public boolean isShare(String activityId, String phone) {
        boolean flag = false;
        try {
            String shareFlag = jedisCluster.hget(SHARE_PRE + activityId, phone);
            if ("true".equals(shareFlag)) {
                flag = true;
            }
        } catch (Exception e) {
            logger.error("查询分享出错。", e);
        }
        return flag;
    }


    public JSONObject addToShare(String activityId, String phone) {
        boolean flag = false;
        if(StringUtils.isNotEmpty(activityId)) {
            try {
                jedisCluster.hset(SHARE_PRE + activityId, phone, "true");
                flag = true;
            } catch (Exception e) {
                logger.error("查询分享出错。", e);
            }
        }
        JSONObject returnJson = new JSONObject();
        returnJson.put("flag", flag);
        return returnJson;
    }
}
