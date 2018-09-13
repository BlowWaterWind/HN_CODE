package com.ai.ecs.ecm.mall.wap.modules.o2o;

import com.ai.ecs.common.ResponseBean;
import com.ai.ecs.common.utils.JedisClusterUtils;
import com.ai.ecs.common.utils.StringUtils;
import com.ai.ecs.ecm.mall.wap.modules.BaseController;
import com.ai.ecs.ecop.sys.service.DictService;
import com.ai.ecs.o2o.api.O2oOrderService;
import com.ai.ecs.o2o.api.O2oOrderTempService;
import com.ai.ecs.o2o.entity.O2oOrderTempInfo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.ai.ecs.ecm.mall.wap.platform.utils.LogUtils.createStreamNo;
import static com.ai.ecs.ecm.mall.wap.platform.utils.LogUtils.writerFlowLogEnterMenthod;

/**
 * Created by Administrator on 2017/9/16.
 */

@Controller
@RequestMapping("o2oBusiness")
public class O2oBusinessController extends BaseController {
    @Autowired
    private DictService dictService;

    @Autowired
    private O2oOrderTempService o2oOrderTempService;

    @Autowired
    private O2oOrderService o2oOrderService;

    @RequestMapping(value="invokeSmsInterface")
    @ResponseBody
    public Map<String,Object> invokeSmsInterface( String goodsName,String  serialNumber,String broadbandType,Long orderId,HttpServletRequest request)throws Exception{
        Map<String,Object> ajaxData = new HashMap<String,Object>();
        LoggerFactory.getLogger("webDbLog").info("o2oBusiness invokeSmsInterface invokeSmsInterface");
        ajaxData.put("X_RESULTCODE", "-1");
        ajaxData.put("X_RESULTINFO", "短信发送失败");
        LoggerFactory.getLogger("webDbLog").info("o2oBusiness invokeSmsInterface :"+serialNumber+"===="+broadbandType+"====="+orderId);
        if(StringUtils.isBlank(serialNumber) || StringUtils.isBlank(broadbandType) || orderId ==null){
            ajaxData.put("X_RESULTINFO", "参数不能为空");
            return ajaxData;
        }
        if("13".equals(broadbandType)||"14".equals(broadbandType)){
            goodsName = URLDecoder.decode(goodsName,"utf-8");
        }
        try {
            String NumCode = dictService.getDictValue("O2O_BROADBAND_TYPE", broadbandType);
            LoggerFactory.getLogger("webDbLog").info("o2oBusiness invokeSmsInterface numcode:"+NumCode);
            Map<Object, Object> map = new HashMap<Object, Object>();
            map.put("orderId", orderId);
            JedisClusterUtils.setObjectMap("O2O_BROADBAND_" + serialNumber + "_" + NumCode + "_3", map, 30 * 60);
            //更新记录 发送短信时间，随机码, 发送次数，>=3次 不发送

            O2oOrderTempInfo orderTempInfo = o2oOrderTempService.selectByPrimaryKey(orderId);
            if(orderTempInfo==null){
                ajaxData.put("X_RESULTINFO", "未找到记录");
                return ajaxData;
            }
            int sendTimes=orderTempInfo.getSms_send_times()==null?0:orderTempInfo.getSms_send_times();
            if(sendTimes>=3){
                ajaxData.put("X_RESULTINFO", "已发送"+sendTimes+"次，不能再次发送");
                return ajaxData;
            }
            long orderStatus=orderTempInfo.getOrder_status()==null?0L:orderTempInfo.getOrder_status();

            if(orderStatus!=0 && orderStatus!=2){
                ajaxData.put("X_RESULTINFO", "该订单状态不能再次发送短信！");
                return ajaxData;
            }
            int flag = this.SmsSendOut(serialNumber, NumCode, goodsName, request);
//            int flag = 200;

            orderTempInfo.setSms_code(Long.parseLong(NumCode));
            orderTempInfo.setSms_insert_time(new Date());
            orderTempInfo.setOrder_status(0L);
            orderTempInfo.setSms_send_times(sendTimes+1);
            o2oOrderTempService.updateByPrimaryKeySelective(orderTempInfo);
            if (flag == 200) {
                ajaxData.put("X_RESULTCODE", "0");
                ajaxData.put("X_RESULTINFO", "ok");
            }
            ajaxData.put("NumCode", NumCode);
            ajaxData.put("serialNumber", serialNumber);
        }catch (Exception e){
            LoggerFactory.getLogger("webDbLog").info("o2oBusiness invokeSmsInterface e:" + e);
            String streamNo=createStreamNo();
            writerFlowLogEnterMenthod(streamNo,"","",getClass().getName(),
                    "sendSmsError",request.getParameterMap(),"发送短信失败:"+serialNumber+"orderId:"+orderId,request);
            ajaxData.put("X_RESULTINFO", e.getMessage());
        }
        return ajaxData;
    }


    public  int  SmsSendOut(String SerialNumber,String NumCode,String  goodsName,HttpServletRequest request){
        String  mapString = null;
        Map  data = new HashMap();
        data.put("src_terminal_id", SerialNumber);
        data.put("dest_id", "10086591"+NumCode+"3");
        data.put("msg_content", "尊敬的客户，现在为您办理 "+goodsName+" ,回复\"是\"确认办理，不回复或回复其他内容不办理。（30分钟内回复有效）");
        data.put("chan_id", "E050");

        JSON json = (JSON) JSONObject.toJSON(data);
        mapString = json.toString();

        String url = "http://10.154.73.84:4080/smsitf/SendImmeMessage/sendMsg";
        HttpClient httpClient = new HttpClient();
        // 设置连接超时时间(单位毫秒)
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(60*1000);
        // 设置读取超时时间(单位毫秒)
        httpClient.getHttpConnectionManager().getParams().setSoTimeout(60*1000);
        PostMethod method = new PostMethod(url);
        String info = null;
        int code =0 ;
        try {
            RequestEntity entity = new StringRequestEntity(mapString, "application/json", "UTF-8");
            method.setRequestEntity(entity);
            httpClient.executeMethod(method);
            code = method.getStatusCode();
            if (code == HttpStatus.SC_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream()));
                StringBuffer stringBuffer = new StringBuffer();
                String str = "";
                while ((str = reader.readLine()) != null) {
                    stringBuffer.append(str);
                }
                info = stringBuffer.toString();
                LoggerFactory.getLogger("webDbLog").info("=======SmsSendOut response：" + info);
            }else{
                LoggerFactory.getLogger("webDbLog").info("=======SmsSendOut 接口返回失败 httpStatusCode：" + code);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (method != null) {
                method.releaseConnection();
            }
        }

        return code;
    }

    @RequestMapping(value = "initFlow",method = RequestMethod.GET)
    public void initBusinessServe(HttpServletRequest request,String serilNumber,Long orderId) throws Exception {
        o2oOrderService.createOrder(serilNumber, orderId);
    }
    
    @RequestMapping("smssuccess")
	public String smssuccess(HttpServletRequest request , HttpServletResponse response, Model model)  throws Exception {
    	return "/web/broadband/o2o/smsResult";
	}
}
