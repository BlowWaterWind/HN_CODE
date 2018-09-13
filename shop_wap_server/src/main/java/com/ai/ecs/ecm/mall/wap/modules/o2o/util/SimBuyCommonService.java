package com.ai.ecs.ecm.mall.wap.modules.o2o.util;

import com.ai.ecs.common.utils.Base64;
import com.ai.ecs.common.utils.JedisClusterUtils;
import com.ai.ecs.ecm.mall.wap.modules.goods.controller.Rsa;
import com.ai.ecs.ecm.mall.wap.modules.goods.vo.UserGoodsCarModel;
import com.ai.ecs.ecm.mall.wap.platform.utils.RealNameMsDesPlus;
import com.ai.ecs.ecm.mall.wap.platform.utils.StringUtils;
import com.ai.ecs.entity.base.ConstantsBase;
import com.ai.ecs.exception.EcsException;
import com.ai.ecs.member.api.IMemberLoginService;
import com.ai.ecs.member.api.register.IRegisterService;
import com.ai.ecs.member.constant.MemberStatusConstant;
import com.ai.ecs.member.constant.MemberTypeConstant;
import com.ai.ecs.member.entity.MemberAddress;
import com.ai.ecs.member.entity.MemberLogin;
import com.ai.ecs.order.api.busi.IOrderAppointmentService;
import com.ai.ecs.order.api.recmd.IOrderNewBusiService;
import com.ai.ecs.order.api.recmd.IRecmdMainService;
import com.ai.ecs.order.entity.TfOrderAppointment;
import com.ai.ecs.order.entity.TfOrderDetailSim;
import com.ai.ecs.order.entity.TfOrderPushKey;
import com.ai.ecs.order.entity.TfOrderSub;
import com.ai.ecs.order.entity.recmd.TfOrderRecmdRef;
import com.ai.ecs.order.entity.recmd.TfOrderSimBusiConf;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import static com.ai.ecs.ecm.mall.wap.platform.utils.LogUtils.createStreamNo;
import static com.ai.ecs.ecm.mall.wap.platform.utils.LogUtils.processThrowableMessage;
import static com.ai.ecs.ecm.mall.wap.platform.utils.LogUtils.writerFlowLog;
import static com.ai.ecs.ecm.mall.wap.platform.utils.LogUtils.writerFlowLogOther;
import static com.ai.ecs.ecm.mall.wap.platform.utils.LogUtils.writerFlowLogThrowable;

/**
 * Created by cc on 2018/6/19.
 *
 */
@Service
public class SimBuyCommonService {

    /**
     * 日志对象
     */
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private  IMemberLoginService loginService;

    @Autowired
    private IRecmdMainService recmdMainService;

    @Autowired
    private  IRegisterService registerService;

    private static final String SIM_BUSI_CONF_KEY = "E007:SIM:BUSI:CONF";

    @Autowired
    private IOrderNewBusiService orderNewBusiService;

    @Autowired
    private IOrderAppointmentService orderAppointmentService;
    /**
     * 预约订单多久没提交失效,分钟
     */
    private static final int EXPIRATION_TIME=28;
    /**
     * 加密
     */
    private final static String  SIM_RE_DES_KEY="SIM_RE_DES_KEY";
    /**
     * 签名
     */
    private final static String  SIM_RE_SINGE_KEY="SIM_RE_SINGE_KEY";

    /**
     * key缓存时间
     */
    private final static int  SIM_RE_KEY_TIME=60*10;

    /**
     * 签名key 类型
     */
    private final static String KEY_TYPE_1="1";
    /**
     * 加密key 类型
     */
    private final static String KEY_TYPE_2="2";

    public  MemberLogin anonymousLogin(TfOrderDetailSim orderDetailSim, MemberAddress address) throws Exception{
        //依据memberId生成规则查该用户是否存在
        MemberLogin memberLogin;
        String psptId = orderDetailSim.getPsptId();
        String psptIdSplit = psptId.substring(psptId.length() - 6, psptId.length());
        String contactPhone = orderDetailSim.getContactPhone();
        String loginName = contactPhone + psptIdSplit;
        memberLogin = loginService.getByLoginMame(loginName, MemberTypeConstant.SYS_GEN_SIM.getValue());
        //用户不存在
        if (memberLogin == null) {
            MemberLogin tmpMemberLogin = new MemberLogin();
            tmpMemberLogin.setMemberLogingName(loginName);
            tmpMemberLogin.setMemberStatusId(MemberStatusConstant.NORMAL.getValue());//用户状态
            tmpMemberLogin.setMemberTypeId(MemberTypeConstant.SYS_GEN_SIM.getValue());//用户类型
            tmpMemberLogin.setMemberPassword(psptIdSplit);
            tmpMemberLogin.setMemberPhone(Long.parseLong(contactPhone));
            //保存用户信息
            memberLogin = registerService.registerBySimApplyInfo(tmpMemberLogin, address);
            logger.info("保存登录信息成功（生成新用户地址不保存）！");
        }
        return memberLogin;
    }

    public  List<TfOrderSimBusiConf> getBusiConfig() {
        List<TfOrderSimBusiConf> simBusiConfs = (List<TfOrderSimBusiConf>) JedisClusterUtils.getObject(SIM_BUSI_CONF_KEY);
        if (null == simBusiConfs) {
            TfOrderSimBusiConf orderSimBusiConf = new TfOrderSimBusiConf();
            orderSimBusiConf.setStatus(1);
            simBusiConfs = orderNewBusiService.queryBusiConfList(orderSimBusiConf);
            JedisClusterUtils.setObject(SIM_BUSI_CONF_KEY, simBusiConfs, 3600);
        }
        return simBusiConfs;
    }

    /**
     * 情况1: 由店员推荐出去下单,下单信息保存在tf_order_recmd_ref
     *
     * @param recmdCode
     * @param orderSub
     */
    public void saveSimRecmdInfo(String recmdCode, TfOrderSub orderSub) {
        String streamNo = createStreamNo();

        String recmdCode2 = Base64.decode(recmdCode.getBytes());//解码 -> rcd_conf_id
        Long recmdId = Long.valueOf(recmdCode2);//推荐ID
        //== 保存推荐号卡业务信息
        TfOrderRecmdRef orderRecmdRef = new TfOrderRecmdRef();
        orderRecmdRef.setRecmdId(recmdId);
        orderRecmdRef.setRcdedRefOrdSubId(orderSub.getOrderSubId());
        //被推荐关联子订单ID
        orderRecmdRef.setRcdedRefBusiFlag1(orderSub.getOrderSubNo());//订单号
        orderRecmdRef.setRcdedRefBusiFlag2(orderSub.getPhoneNumber());
        logger.info("==saveSimRecmdInfo保存推荐关联业务信息==>{}", JSONObject.toJSONString(orderRecmdRef));
        writerFlowLogThrowable(streamNo, "", "", this.getClass().getName(), "saveSimRecmdInfo", null, "==saveSimRecmdInfo保存推荐关联业务信息==>" + JSONObject.toJSONString(orderRecmdRef));
        recmdMainService.saveOrderRecmdRef(orderRecmdRef);
        logger.info("==saveSimRecmdInfo保存推荐关联业务信息成功！");
    }


    public UserGoodsCarModel getCallbackAppointment(String transactionId,String streamNo,UserGoodsCarModel cModel){
        //查询预约信息
        TfOrderAppointment orderAppointment;
        JSONObject param;
        orderAppointment = orderAppointmentService.getOrderAppointmentById(transactionId);
        if (orderAppointment != null && StringUtils.isNotEmpty(orderAppointment.getRequestParam())) {
            orderTimeout(orderAppointment);
            param = JSONObject.parseObject(orderAppointment.getRequestParam());
            UserGoodsCarModel cModelNew = disposeOrder(param,streamNo,orderAppointment,cModel);
            cModelNew.setParams(param);
        } else {
            writerFlowLogThrowable(streamNo,"","",getClass().getName(),"getCallbackAppointment",
                    null,String.format("预约订单参数据为空，请重新预约[%s]",transactionId));
            throw new RuntimeException(ConstantsBase.MY_EXCEP + "身份验证信息为空，请重新认证！");
        }
        return cModel;
    }

    /**
     * 重新设置订单数据
     * @param param
     * @param streamNo
     * @param orderAppointment
     * @return
     */
    public UserGoodsCarModel disposeOrder(JSONObject param ,String streamNo,TfOrderAppointment orderAppointment, UserGoodsCarModel  cModel ){
        try {
                TfOrderDetailSim orderDetailSim =cModel.getOrderDetailSim();
                orderDetailSim.setRegName(orderAppointment.getCustName());
                orderDetailSim.setPsptId(orderAppointment.getCustCertNo());
                orderDetailSim.setCityCode(param.getString("orderDetailSim.cityCode"));
                orderDetailSim.setPhone(orderAppointment.getSerialNumber());
                orderDetailSim.setTransactionId(orderAppointment.getTransactionId());
                cModel.setOrderDetailSim(orderDetailSim);

        }catch (EcsException e){
            writerFlowLogThrowable(streamNo,"","","",getClass().getName(),null,processThrowableMessage(e));
            throw new RuntimeException(ConstantsBase.MY_EXCEP + "身份验证处理异常，请重验证！");
        }
        return cModel;
    }

    /**
     * 更新预约表订单编号
     * @param transactionId
     * @param orderSubResult
     */
    public void updateOrderAppointment(String transactionId, TfOrderSub orderSubResult) {
        TfOrderAppointment orderAppointment1=new TfOrderAppointment();
        orderAppointment1.setTransactionId(transactionId);
        orderAppointment1.setOrderSubId(orderSubResult.getOrderSubId());
        orderAppointmentService.updateByTransactionId(orderAppointment1);
    }

    public void orderTimeout(TfOrderAppointment orderAppointment) {
        if (orderAppointment.getOrderSubId() != null) {
            throw new RuntimeException(ConstantsBase.MY_EXCEP + "订单已生成，请不要重复下单！");
        }
        if (com.ai.ecs.ecm.mall.wap.platform.utils.DateUtils.differMinute(orderAppointment.getCreateTime()) > EXPIRATION_TIME) {
            throw new RuntimeException(ConstantsBase.MY_EXCEP + "预约订单已超时失效，请重新预约！");
        }
    }

    /**
     * 生成签名链接
     * @param phone
     * @param reqParam
     * @param callBackUrl
     * @return
     * @throws Exception
     */
    public String preordainCheck(String phone, String reqParam, String callBackUrl,String streamNo) throws Exception {
        String busiCode = "PREORDAIN_CHECK";//业务编码
        String requestSource = "731009";//请求源编码
        String channelId ="onlineball";//渠道ID
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");
        long now = System.currentTimeMillis();
        String dateStr = df.format(now);//14位组包时间

        int youNumber = orderAppointmentService.seqTransactionId();//数据库中取到序列 269
        // 0 代表前面补充0
        // 6 代表长度为6
        // d 代表参数为正数型
        String strNum = String.format("%06d", youNumber);//格式化后的序列
        String transactionId = requestSource + dateStr + strNum;//全网唯一流水号
        String desKey =JedisClusterUtils.get(SIM_RE_DES_KEY);
        if(StringUtils.isEmpty(desKey)) {
            TfOrderPushKey orderPushKey = orderAppointmentService.selectByKeyType(KEY_TYPE_2);
            if (orderPushKey == null || StringUtils.isEmpty(orderPushKey.getKey())) {
                throw new RuntimeException(ConstantsBase.MY_EXCEP + "加密key获取失败");
            }
            desKey=orderPushKey.getKey();
            JedisClusterUtils.set(SIM_RE_DES_KEY,desKey,SIM_RE_KEY_TIME);
        }
        RealNameMsDesPlus realNameMsDesPlus = new RealNameMsDesPlus(desKey);
        String encryptionPhone = realNameMsDesPlus.encrypt(phone);//加密后的手机号码
        String encryptionChannelId = realNameMsDesPlus.encrypt(channelId);//加密后的渠道ID

        SimpleDateFormat aDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long now2 = System.currentTimeMillis();

        String dateStr2 = aDate.format(now2);//时间戳
        /**
         * 签名对入参进行签名（请求源编码+全网唯一操作流水号+手机号码+渠道ID+|+时间戳(yyyy-mm-dd hh24:mi:ss)）其中签名方法见后文附录
         * 签名秘钥获取见
         * 验签秘钥更新接口
         */
        String signature = requestSource  + transactionId  + phone  + channelId + "|" + dateStr2;
        writerFlowLogOther(streamNo,"",phone,getClass().getName(),"preordainCheck",null,String.format("签名前：%s",signature),null);
        String encryptionSignature = "";
        String signKey =JedisClusterUtils.get(SIM_RE_SINGE_KEY);
        if(StringUtils.isEmpty(signKey)) {
            TfOrderPushKey orderPushKey = orderAppointmentService.selectByKeyType(KEY_TYPE_1);//KeyType="1"
            if (orderPushKey == null || StringUtils.isEmpty(orderPushKey.getKey())) {
                throw new RuntimeException(ConstantsBase.MY_EXCEP + "签名key获取失败");
            }
            signKey=orderPushKey.getKey();
            JedisClusterUtils.set(SIM_RE_SINGE_KEY,signKey,SIM_RE_KEY_TIME);
        }
        encryptionSignature = new Rsa.Encoder(signKey).encode(signature);

        /**
         * get方式发送拼接后额字符串；
         */
        String requestStr = "requestSource" + "=" + requestSource + "&" + "transactionID" +
                "=" + transactionId + "&" + "signature" + "=" +URLEncoder.encode(encryptionSignature,"utf-8") + "&" +
                "billId" + "=" + encryptionPhone + "&" + "channelId" + "=" + encryptionChannelId +
                "&" + "busiCode" + "=" + busiCode;

        Calendar calendar=Calendar.getInstance();
        //获得当前时间的月份，月份从0开始所以结果要加1
        int month=calendar.get(Calendar.MONTH)+1;
        String monthS= Integer.toString(month);
        Long monthL = Long.valueOf(monthS);
        TfOrderAppointment orderAppointmentCond = new TfOrderAppointment();

        orderAppointmentCond.setTransactionId(transactionId);//全网唯一流水号
        orderAppointmentCond.setBusiCode(busiCode);//业务编码
        orderAppointmentCond.setRequestSource(requestSource);//请求源编码
        orderAppointmentCond.setSignature(requestStr);//签名
        orderAppointmentCond.setSerialNumber(phone);//手机号码
        orderAppointmentCond.setChannelId(channelId);//渠道ID
        orderAppointmentCond.setMonth(monthL);//分区字段
        orderAppointmentCond.setRequestParam(reqParam);//订单提交参数
        orderAppointmentCond.setCallBackUrl(callBackUrl+transactionId);//回调地址

        orderAppointmentService.insertOrderAppointment(orderAppointmentCond);
        writerFlowLogOther(streamNo,"",phone,getClass().getName(),"preordainCheck",
                orderAppointmentCond,"保存数据成功",null);
        return requestStr;
    }
}
