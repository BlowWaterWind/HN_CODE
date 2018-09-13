package com.ai.ecs.ecm.mall.wap.platform.utils;

import com.ai.ecs.common.framework.exception.ServiceException;
import com.ai.ecs.common.utils.SpringContextHolder;
import com.ai.ecs.ecm.mall.wap.modules.goods.constant.HNanConstant;
import com.ai.ecs.ecsite.modules.dobusiness.entity.HqGetUserAllDiscntCondition;
import com.ai.ecs.ecsite.modules.dobusiness.service.FlowServeService;
import com.ai.ecs.ecsite.modules.offerBalance.entity.OfferBalanceCondition;
import com.ai.ecs.order.api.IOrderQueryService;
import com.ai.ecs.order.entity.TfOrderSub;
import com.ai.ecs.order.entity.TfOrderUserRef;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;
import java.util.*;

/**
 * Created by xtf on 2016/11/9.
 * 验证客户是否是实名认证，并且套餐是否合理
 */
public class RealAndAvailable {
    private static Logger logger = Logger.getLogger(RealAndAvailable.class);

    static FlowServeService flowServeServiceImpl = SpringContextHolder.getBean("flowServeServiceImpl");
    static IOrderQueryService orderQueryService = SpringContextHolder.getBean("orderQueryService");
    public static String isAvailable(Long memberId,String goodsPrice, String phoneNum) throws Exception {
        //校验订单，是否购买过
        if (checkOrderSub(memberId)) {
            return "-2";//6个月内购买过，则不可在进行购买
        }
        //查看用户是否实名认证
        boolean real = true;
        if(!real){//是3高用户，但是没有实名认证，不可以购买。
            logger.info("是3高用户，但是没有实名认证，不可以购买");
            return "2";//没有实名
        }else{
            Map<String,Object> infos = queryAllDiscnt(phoneNum);
            JSONArray resultArr = (JSONArray) infos.get("result");
            //根据价格判断是否办理了合理的套餐
            /**
             * 大于500,查看是否办理了
             * （1）需办理28元及以上4G套餐。
             * （2）180元及以上流量半年包、年包
             * （3）办理30元及以上流量月套餐
             */
            boolean available;
            if(Long.parseLong(goodsPrice)>50000){
                available=checkDiscnt(resultArr,28l,30l);
                //加入流量年包、半年包判断
                if(!available){//上面判断为假时再判断，防止多余判断
                    for(int i=0;i<resultArr.size();i++) {
                        JSONObject obj = resultArr.getJSONObject(i);
                        String name = obj.getString("DISCNT_NAME");//优惠名称
                        if((name.startsWith("流量年包（")||name.startsWith("流量半年包（"))&&name.endsWith("元）")){
                            Long price = Long.parseLong(name.substring(name.indexOf('（')+1,name.length()-2));
                            if(price>=180){
                                available = true;
                                break;
                            }
                        }
                    }
                }
            }else{
                /**
                 * （1）需办理18元及以上4G套餐。
                 * （2）办理20元及以上流量月套餐
                 */
                available=checkDiscnt(resultArr,18l,20l);
            }
            if(available){
                logger.info("是3高用户，已经办理合理套餐，可以购买");
                return "0";//3高用户购买
            }else{
                logger.info("是3高用户，未办理合理套餐，不可以购买");
                return "3";//3高用户套餐不合理，需要办理套餐
            }
        }
    }

    /**
     * 套餐判断
     * @param resultArr
     * @param mainDiscntPrice
     * @param flowPrice
     * @return
     */
    private static boolean checkDiscnt(JSONArray resultArr,Long mainDiscntPrice,Long flowPrice){
        for(int i=0;i<resultArr.size();i++){
            JSONObject obj = resultArr.getJSONObject(i);
            String name = obj.getString("DISCNT_NAME");//优惠名称
            //根据名称进行判断
            //2GB国内流量 -50元
            if("2GB国内流量".equals(name)){
                logger.info("套餐情况符合:"+name);
                return true;
            }else if(name.startsWith("4G飞享套餐（")&&name.endsWith("元）")){
                //4G飞享套餐判断价格
                Long price = Long.parseLong(name.substring(name.indexOf('（')+1, name.length()-2));
                if(price>=mainDiscntPrice){
                    logger.info("套餐情况符合:"+name);
                    return true;
                }
            }else if(name.startsWith("4G流量王(2015)")&&name.endsWith("套餐")){
                //4G流量王(2015)28套餐
                Long price = Long.parseLong(name.substring(name.indexOf(')')+1, name.length()-2));
                if(price>=mainDiscntPrice){
                    logger.info("套餐情况符合:"+name);
                    return true;
                }
            }else if(name.startsWith("4G流量王·宽带版（")&&name.endsWith("元）")){
                //4G流量王·宽带版（68元）
                Long price = Long.parseLong(name.substring(name.indexOf('（')+1, name.length()-2));
                if(price>=mainDiscntPrice){
                    logger.info("套餐情况符合:"+name);
                    return true;
                }
            }else if(name.startsWith("4G商旅套餐（")&&name.endsWith("元）")){
                //4G商旅套餐（88元）
                Long price = Long.parseLong(name.substring(name.indexOf('（')+1, name.length()-2));
                if(price>=mainDiscntPrice){
                    logger.info("套餐情况符合:"+name);
                    return true;
                }
            }else if(name.startsWith("4G上网套餐（")&&name.endsWith("元）")){
                //4G上网套餐（128元）
                Long price = Long.parseLong(name.substring(name.indexOf('（')+1, name.length()-2));
                if(price>=mainDiscntPrice){
                    logger.info("套餐情况符合:"+name);
                    return true;
                }
            }else if(name.startsWith("4G自选套餐（")&&name.endsWith("）")){
                //4G自选套餐（38元）    4G自选套餐（328元包3000分钟）
                Long price = Long.parseLong(name.substring(name.indexOf('（')+1, name.indexOf('元')));
                if(price>=mainDiscntPrice){
                    logger.info("套餐情况符合:"+name);
                    return true;
                }
            }else if(name.startsWith("4G自选套餐")&&name.contains("元(")){
                //4G自选套餐98元(28元语音模组+70元流量模组)
                //4G自选套餐508元(408元语音模组+100元流量模组)
                //4G自选套餐98元(48元语音模组（新）+50元流量模组)
                Long price = Long.parseLong(name.substring(6, name.indexOf("元(")));
                if(price>=mainDiscntPrice){
                    logger.info("套餐情况符合:"+name);
                    return true;
                }
            }else if(name.startsWith("动感地带·上网套餐（")&&name.endsWith("元）")){
                //动感地带·上网套餐（28元）
                Long price = Long.parseLong(name.substring(name.indexOf('（')+1, name.length()-2));
                if(price>=mainDiscntPrice){
                    logger.info("套餐情况符合:"+name);
                    return true;
                }
            }else if(name.startsWith("流量包（")&&name.endsWith("GB）")){
                //流量包（180元包6GB）
                Long price = Long.parseLong(name.substring(name.indexOf('（')+1,name.indexOf("元")));
                if(price>=flowPrice){
                    logger.info("套餐情况符合:"+name);
                    return true;
                }
            }else if(name.startsWith("流量包月套餐（")&&name.endsWith("元）")){
                //流量包月套餐（200元）
                Long price = Long.parseLong(name.substring(name.indexOf('（')+1, name.length()-2));
                if(price>=flowPrice){
                    logger.info("套餐情况符合:"+name);
                    return true;
                }
            }else if(name.startsWith("流量月包（")&&name.endsWith("GB）")){
                //流量月包（70元包2GB）
                Long price = Long.parseLong(name.substring(name.indexOf('（')+1,name.indexOf("元")));
                if(price>=flowPrice){
                    logger.info("套餐情况符合:"+name);
                    return true;
                }
            }else if((name.startsWith("流量卡2015版（")||name.startsWith("流量卡（")
                    ||name.startsWith("流量王（")||name.startsWith("全球通·本地套餐2（")
                    ||name.startsWith("全球通·本地套餐·优化版（")
                    ||name.startsWith("全球通·畅聊套餐·2014版（")||name.startsWith("全球通·商旅套餐·优化版（")
                    ||name.startsWith("全球通·上网套餐·优化版（")||name.startsWith("神州行·本地套餐（")
                    ||name.startsWith("神州行·长途套餐（"))
                    &&name.endsWith("）")){
                Long price = Long.parseLong(name.substring(name.indexOf('（')+1,name.length()-2));
                if(price>=mainDiscntPrice){
                    logger.info("套餐情况符合:"+name);
                    return true;
                }
            }
        }
        return false;
    }
    /**
     *查询用户6个月内是否购买过集团购机商品，并且是以集团客户身份购买的
     * @param memberId
     * @return
     */
    private static boolean checkOrderSub(Long memberId) {
        TfOrderSub orderSub = new TfOrderSub();
        TfOrderUserRef userRef = new TfOrderUserRef();
        Calendar calendar = Calendar.getInstance(); //得到日历
        calendar.setTime(new Date());//把当前时间赋给日历
        calendar.add(calendar.MONTH, -6);  //设置为前6月
        Date dBefore = calendar.getTime();   //得到前6月的时间
        orderSub.setStartOrderTime(dBefore);//开始时间为6个月前
        userRef.setMemberId(memberId);
        userRef.setUserType("1");//1是集团用户
        orderSub.setOrderUserRef(userRef);
        List<TfOrderSub> subs = orderQueryService.queryBaseOrderList(orderSub);
        for(TfOrderSub sub:subs){
            if(sub.getOrderStatusId()!=18&&sub.getOrderStatusId()!=16){//非取消订单,非退款订单
                return true;
            }
        }
        return false;
    }

    /**
     *
     * queryAllDiscnt 查询所有优惠
     * @return
     * @return Map返回说明
     * @Exception 异常说明
     * @author：zhangqd3@asiainfo.com
     * @create：2016年4月1日 下午3:28:48
     * @moduser：
     * @moddate：
     * @remark：
     */
    private static Map<String,Object> queryAllDiscnt(String SERIAL_NUMBER)throws Exception{
        Map<String, Object> ajaxData = new HashMap<String,Object>();
        HqGetUserAllDiscntCondition hqGetUserAllDiscntCondition = new HqGetUserAllDiscntCondition();
        hqGetUserAllDiscntCondition.setSerialNumber(SERIAL_NUMBER);
        hqGetUserAllDiscntCondition.setRemoveTag("0");//销号标志
        hqGetUserAllDiscntCondition.setxGetmode(3);//1查询用户当前正在使用的优惠 2，预约的优惠 3，两种优惠都查询出来

        OfferBalanceCondition condition = new OfferBalanceCondition();
        condition.setSerialNumber(SERIAL_NUMBER);
        Map<String,Object> allDiscntResult =
//                offerBalanceServiceImpl.queryBalance(condition);
                flowServeServiceImpl.queryDiscnt(hqGetUserAllDiscntCondition);
        if(!HNanConstant.SUCCESS.equals(allDiscntResult.get("respCode"))|| MapUtils.isEmpty(allDiscntResult)){
            ajaxData.put(HNanConstant.resultCode, HNanConstant.FAIL);
            throw new ServiceException(HNanConstant.FAIL, allDiscntResult.get("respDesc").toString(), null);
        }
        return allDiscntResult;
    }
}
