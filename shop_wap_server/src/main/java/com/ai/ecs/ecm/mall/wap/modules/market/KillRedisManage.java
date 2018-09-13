package com.ai.ecs.ecm.mall.wap.modules.market;

import com.ai.ecs.ecm.mall.wap.modules.goods.vo.VisualActivityInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisCluster;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class KillRedisManage {

    @Autowired
    private JedisCluster jedisCluster;
    private final static Logger LOGGER = LoggerFactory.getLogger(KillRedisManage.class);
    public static final int DEFAULT_SINGLE_EXPIRE_TIME = 9;
    public boolean tryLock (String key) {
        return tryLock(key, 0, null);
    }

    /**
     * 锁在给定的等待时间内空闲，则获取锁成功 返回true， 否则返回false
     *
     * @param timeout
     * @param unit
     * @return
     */
    public boolean tryLock(String key, long timeout, TimeUnit unit) {
        try {
            long nano = System.nanoTime();  //系统计时器的当前值，以毫微秒为单位。
            do {
                LOGGER.debug("try lock key: " + key);
                Long i = jedisCluster.setnx(key, key); // 将 key 的值设为 value 1成功  0失败
                jedisCluster.expire(key, DEFAULT_SINGLE_EXPIRE_TIME); // 设置过期时间
                if (i == 1) {
                    LOGGER.debug("get lock, key: " + key + " , expire in " + DEFAULT_SINGLE_EXPIRE_TIME + " seconds.");
                    return Boolean.TRUE; // 成功获取锁，返回true
                } else { // 存在锁,循环等待锁
                    if (LOGGER.isDebugEnabled()) {
                        String desc = jedisCluster.get(key);
                        LOGGER.debug("key: " + key + " locked by another business：" + desc);
                    }
                }
                if (timeout <= 0) {  //没有设置超时时间，直接退出等待
                    break;
                }
                Thread.sleep(300);
            } while ((System.nanoTime() - nano) < unit.toNanos(timeout));
            return Boolean.FALSE;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return Boolean.FALSE;
    }

    /**
     * 释放锁
     * @param key
     */
    public void unLock(String key) {
        try {
            jedisCluster.del(key);
            LOGGER.debug("release lock, keys :" + key);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }





    /*public static void main(String args[]) {
        List<VisualActivityInfo> list = new ArrayList<VisualActivityInfo>();
        VisualActivityInfo v = new VisualActivityInfo();

        v.setStartDate(901L);
        v.setEndDate(904L);
        v.setActivityId("3");
        list.add(v);

        v = new VisualActivityInfo();
        v.setStartDate(404L);
        v.setEndDate(500L);
        v.setActivityId("2");
        list.add(v);

        v = new VisualActivityInfo();
        v.setStartDate(1L);
        v.setEndDate(3L);
        v.setActivityId("1");
        list.add(v);


        System.out.println(getActivityId(list , -100L));

    }*/
}
