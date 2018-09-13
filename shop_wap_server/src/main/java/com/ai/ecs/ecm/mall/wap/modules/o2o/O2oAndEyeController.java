package com.ai.ecs.ecm.mall.wap.modules.o2o;

import com.ai.ecs.common.utils.StringUtils;
import com.ai.ecs.ecm.mall.wap.modules.goods.utils.AndEyeUtils;
import com.ai.ecs.ecm.mall.wap.modules.o2o.util.O2oParamUtils;
import com.ai.ecs.ecm.mall.wap.platform.utils.TriDes;
import com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils;
import com.ai.ecs.ecsite.modules.broadBand.entity.AndEyeCloudCondition;
import com.ai.ecs.ecsite.modules.broadBand.service.BroadBandService;
import com.ai.ecs.ecsite.modules.dobusiness.entity.HqDreamNetQryCondition;
import com.ai.ecs.ecsite.modules.dobusiness.service.BasisServeService;
import com.ai.ecs.ecsmc.domain.ec.po.ShopInfo;
import com.ai.ecs.goods.api.IGoodsManageService;
import com.ai.ecs.goods.entity.goods.AndEyeItemEntity;
import com.ai.ecs.goods.entity.goods.TfGoodsInfo;
import com.ai.ecs.member.entity.ChannelInfo;
import com.ai.ecs.member.entity.MemberVo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by hexiao3@asiainfo.com on 2018/3/5.
 */
@RequestMapping("o2oAndEyeCloud")
@Controller
public class O2oAndEyeController {
    private Logger logger = Logger.getLogger(O2oAndEyeController.class);
    @Autowired
    private IGoodsManageService goodsManageService;

    @Autowired
    private BroadBandService broadBandService;

    @Autowired
    BasisServeService basisServeService;
    @Autowired
    private O2oParamUtils o2oParamUtils;

    private static String keyStr = "ABCDEFGHIJKLMNOP" + "QRSTUVWXYZabcdef" + "ghijklmnopqrstuv"
            + "wxyz0123456789+/" + "=";
    /**
     * 号码填写
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping("init")
    public String init(HttpServletRequest request , HttpServletResponse response, Model model)  throws Exception {
        //获取办理用户手机号码
        MemberVo memberVo = UserUtils.getLoginUser(request);
        ChannelInfo channelInfo = memberVo.getChannelInfo();
        ShopInfo shopInfo=memberVo.getShopInfo();
        model.addAttribute("staffId",channelInfo.getTradeStaffId());
        model.addAttribute("shopId",shopInfo.getShopId());
        return "web/broadband/o2o/andCloud/init";
    }
    /**i
     * WAP商城和目首页
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping("index")
    public String index(HttpServletRequest request , HttpServletResponse response, Model model)  throws Exception {
        Map<String,Object> resultMap = new HashMap<String,Object>();
        String installPhoneNum = request.getParameter("installPhoneNum");
//		List<TfGoodsInfo> goodsInfoList = goodsManageService.queryGoodsInfoByParams(params);
//        model.addAttribute("goodsInfoList",goodsInfoList);
        if(StringUtils.isBlank(installPhoneNum)){
            throw new Exception("请输入手机号!");
        }else{
            model.addAttribute("installPhoneNum", installPhoneNum);
        }
        return "web/broadband/o2o/andCloud/index";
    }

    /**
     * 和目云存储业务办理提交
     * @param request
     * @param response
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("comfirmOrder")
    public String comfirmOrder(HttpServletRequest request , HttpServletResponse response, Model model) throws Exception {
        Map resultMap = new HashMap();
        resultMap.put("respCode","-1");
        resultMap.put("respDesc", "和目云存储业务办理失败!");

        String installPhoneNum = request.getParameter("installPhoneNum");
        String staffPwd = request.getParameter("staffPassword");//预约安装日期段
        staffPwd = TriDes.getInstance().strDec(staffPwd, keyStr, null, null);
        String duration = request.getParameter("DURATION_VAL");
        String packageName = request.getParameter("PACKAGE_VAL");
        model.addAttribute("packageName",packageName);
        model.addAttribute("duration",duration);
        /**
         * 商品查询
         */
        Map<String,Object> params = new HashMap<String,Object>();

        params.put("containGoodsSkuIdInfo",true);
        params.put("containGoodsBusiParam",true);
        params.put("duration","DURATION");
        params.put("durationVal",duration);
        params.put("package","PACKAGE");
        params.put("packageVal",packageName);
        List<TfGoodsInfo> goodsInfoList = goodsManageService.queryGoodsInfoByParams(params);
        List<AndEyeItemEntity> andEyeItemEntityList = AndEyeUtils.covertAndEye(goodsInfoList);

        /**
         * 已办理SP业务查询
         */
        HqDreamNetQryCondition hqDreamNetQryCondition = new HqDreamNetQryCondition();
//        hqDreamNetQryCondition.setSerialNumber("13974956455");
        hqDreamNetQryCondition.setDealTag("00");
        hqDreamNetQryCondition.setRemoveTag("0");
        hqDreamNetQryCondition.setTagChar("0");
//        hqDreamNetQryCondition.setStaffId("ITFWC000");
//        hqDreamNetQryCondition.setTradeDepartPassword("ai1234");
        hqDreamNetQryCondition.setSerialNumber(installPhoneNum);;
        Map hasHandled =basisServeService.getDreamBusi(hqDreamNetQryCondition);
        List<Map> handledList = (List) hasHandled.get("result");
        for(Map handle:handledList){
            Iterator iterable = andEyeItemEntityList.iterator();
            while (iterable.hasNext()){
                AndEyeItemEntity  andEyeItemEntity1= (AndEyeItemEntity) iterable.next();
                if(andEyeItemEntity1.getBizCode().equals(handle.get("BIZ_CODE"))){
                    iterable.remove();
                }
            }
        }
        /**
         * 选择办理产品
         */
        AndEyeItemEntity andEyeItemEntity = new AndEyeItemEntity();
        if(andEyeItemEntityList!=null&&andEyeItemEntityList.size()>0){
            andEyeItemEntity = andEyeItemEntityList.get(0);
            /**
             * 调用办理接口
             */
            MemberVo memberVo = UserUtils.getLoginUser(request);
            AndEyeCloudCondition condition = new AndEyeCloudCondition();
            condition.setSerialNumber(installPhoneNum);
            condition.setBizCode(andEyeItemEntity.getBizCode());
            condition.setBizTypeCode(andEyeItemEntity.getBizTypeCode());
            condition.setOperCode("06");
            condition.setOprSource("08");
            condition.setSpCode(andEyeItemEntity.getSpCode());
            condition.setTradeDepartPassword(staffPwd);
            if(memberVo.getChannelInfo()==null){
                resultMap.put("respCode",  "1");
                resultMap.put("respDesc", "受理校验失败：【未配置渠道信息，请用店长账户配置渠道信息!】");
            }
            o2oParamUtils.addConditionChannel(condition,memberVo.getChannelInfo(),installPhoneNum);
            resultMap = broadBandService.platInfoRegE(condition);
        }else{
            resultMap.put("respCode","-1");
            resultMap.put("respDesc", "用户已办理和目业务，无可用业务办理！");
        }
        model.addAttribute("resultMap",resultMap);
        return "web/broadband/o2o/andCloud/result";
    }
}
