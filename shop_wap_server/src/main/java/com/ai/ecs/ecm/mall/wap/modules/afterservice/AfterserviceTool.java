package com.ai.ecs.ecm.mall.wap.modules.afterservice;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import com.ai.ecs.afterservice.constant.AftersaleSatusContant;
import com.ai.ecs.afterservice.constant.AftersaleTypeConstant;
import com.ai.ecs.afterservice.entity.AftersaleApply;
import com.ai.ecs.afterservice.entity.AftersaleApplyImg;
import com.ai.ecs.common.ResponseBean;
import com.ai.ecs.common.utils.TFSClient;

public class AfterserviceTool {
    public static Properties properties = new Properties();

    static {
        try {
            properties.load(AfterserviceTool.class.getClassLoader().getResourceAsStream(
                    "tfs-config.properties"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断上传文件的类型是否符合要求
     */
    public static boolean uploadImgType(String filename) {
        int index = filename.indexOf(".");
        // 文件扩展名
        String tfsSuffix = index <= 0 ? "" : (filename.substring(index)).toLowerCase();
        String value = (String) properties.get("ImgType");
        boolean retflag = value.contains(tfsSuffix) == true ? true : false;
        return retflag;
    }

    /**
     * 上传文件到tfs
     *
     * @param request
     * @return 返回结果在ResponseBean中
     * @throws Exception 
     */
    @SuppressWarnings("rawtypes")
    public static ResponseBean<String> upload2tfs(HttpServletRequest request,
            DefaultMultipartHttpServletRequest multipartRequest) throws Exception {
        StringBuffer buf = new StringBuffer();
        ResponseBean responseBean = new ResponseBean<String>();
        if (multipartRequest != null) {

            Iterator iterator = multipartRequest.getFileNames();
            while (iterator.hasNext()) {
                MultipartFile multifile = multipartRequest.getFile((String) iterator.next());
                String originalName = multifile.getOriginalFilename();

                if (originalName.length() > 0) {
                    // 截取文件后缀名
                    int start = (multifile.getOriginalFilename()).lastIndexOf(".");
                    //String fileLastName = (multifile.getOriginalFilename()).substring(start + 1);
                    String value = multifile.getName();
                    // 索引到最后一个反斜杠
                    start = value.lastIndexOf("\\");
                    // 截取 上传文件的 字符串名字，加1是 去掉反斜杠，
                    String filename = value.substring(start + 1);
                    if (StringUtils.isBlank(filename)||!uploadImgType(filename)) {
                        throw new Exception("图片格式错误！");
                    }
                    int index = filename.indexOf(".");
                    String tfsSuffix = index <= 0 ? "" : filename.substring(index);// 文件扩展名
                    // 上传文件到tfs系统

                    String newName = TFSClient.uploadFile(multifile.getInputStream(), tfsSuffix,
                            null);
                    // int start1 = newName.lastIndexOf(".");
                    String tfsName = newName.substring(0, newName.lastIndexOf("."));
                    buf.append(tfsName).append(",");
                }
            }
            if (buf.length() > 0) {
                String ret = buf.substring(0, buf.length() - 1);
                responseBean.addSuccess(ret);
            }
        }
        return responseBean;
    }

    /**
     * 生成指定位数随机数：返回长度为【strLength】的随机数，在前面补0
     */
    public static String getFixLenthString(int strLength) {
        Random rm = new Random();
        // 获得随机数
        double pross = (1 + rm.nextDouble()) * Math.pow(10, strLength);
        // 将获得的获得随机数转化为字符串
        String fixLenthString = String.valueOf(pross);
        // 返回固定的长度的随机数
        return fixLenthString.substring(2, strLength + 2);
    }

    /**
     * 生成订单号：时间+8位随机数
     */
    public static String genOrderNum() {
        // DateUtils.parseDate(DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
        Date now = new Date();
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String str = df.format(now) + AfterserviceTool.getFixLenthString(8);// 得到当前时间的毫秒值
        return str;
    }

    /**
     * 存申请单
     */
    public static Map<String, Object> autoValue(long orderSubId, String orderSubNo,
            long orderSubDetailId, String complaintReason, String complaintExplain, long memberId,
            String memberLoginName, String aftersaleApplyPerson,
            DefaultMultipartHttpServletRequest multipartRequest, HttpServletRequest request)
            throws Exception {
        AftersaleApply asApp = new AftersaleApply();
        asApp.setOrderSubId(orderSubId);
        asApp.setOrderSubNo(orderSubNo);
        asApp.setOrderSubDetailId(orderSubDetailId);
        asApp.setAftersaleApplyReason(complaintReason);
        asApp.setAftersaleApplyDescribe(complaintExplain);
        asApp.setMemberId(memberId);
        asApp.setMemberLoginName(memberLoginName);

        asApp.setAftersaleApplyTime(new Date());
        asApp.setAftersaleApplyStatusId(AftersaleSatusContant.AFTERSALE_PENDING);
        asApp.setAftersaleApplyTypeId(AftersaleTypeConstant.AFTERSALE_TYPE_COMPLAINT);
        // 代理申请人为空
        if ("".equals(aftersaleApplyPerson) && aftersaleApplyPerson != null) {
            asApp.setAftersaleApplyPerson(aftersaleApplyPerson);
        }
        asApp.setAftersaleApplyNum(AfterserviceTool.genOrderNum());

        Map<String, Object> retMap = new HashMap<String, Object>();
        // 获取、上传图片
        ResponseBean<String> responsebean = AfterserviceTool.upload2tfs(request, multipartRequest);
        // 上传失败
        if (responsebean.getCode().equals("0")) {
            retMap.put("errorMsg", "上传图片失败");
        }
        // 上传成功，有返回名
        String tfsRetDataName = responsebean.getData();

        // 没有上传图片：9只存储39申请单
        List<AftersaleApplyImg> asAppImgL = new ArrayList<AftersaleApplyImg>();
        // 处理图片
        if (tfsRetDataName != null) {
            if (!tfsRetDataName.contains(",")) {
                AftersaleApplyImg asAppImg = new AftersaleApplyImg();
                asAppImg.setAftersaleApplyImgUrl(tfsRetDataName);
                asAppImgL.add(asAppImg);
            }
            else {
                String[] s = tfsRetDataName.split(",");
                for (int i = 0; i < s.length; i++) {
                    AftersaleApplyImg asAppImg = new AftersaleApplyImg();
                    asAppImg.setAftersaleApplyImgUrl(s[i]);
                    asAppImgL.add(asAppImg);
                }
            }
        }
        retMap.put("asAppImgL", asAppImgL);
        retMap.put("asApp", asApp);
        return retMap;
    }
}
