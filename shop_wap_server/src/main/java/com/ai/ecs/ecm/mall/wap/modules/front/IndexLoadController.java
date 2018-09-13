package com.ai.ecs.ecm.mall.wap.modules.front;

import com.ai.ecs.common.utils.JedisClusterUtils;
import com.ai.ecs.ecm.mall.wap.modules.BaseController;
import com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils;
import com.ai.ecs.ecop.sys.entity.Catalog;
import com.ai.ecs.ecop.sys.service.CatalogService;
import com.ai.ecs.member.api.login.ILoginService;
import com.ai.ecs.member.entity.MemberInfo;
import com.ai.ecs.member.entity.MemberLogin;
import com.ai.ecs.member.entity.MemberVo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import redis.clients.jedis.JedisCluster;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("indexLoad")
public class IndexLoadController extends BaseController
{
    @Autowired
    ILoginService loginService;
    
    @Autowired
    CatalogService catalogService;
    
    @Autowired
    JedisCluster JedisCluster;
    
    private static final Logger logger = LoggerFactory.getLogger(IndexLoadController.class);

    /**
     * 判断是否已经登录
     * @param paramMap
     * @return
     * @throws Exception
     */
    @RequestMapping( value = "isLogin",method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> isLogin(HttpServletRequest request,Map paramMap) throws Exception 
    {
        Map<String, Object> returnMap = new HashMap<String, Object>(); // 存储栏目信息
        
        MemberVo member = UserUtils.getLoginUser(request);
        if (member != null)
        {
            MemberLogin memberLogin=member.getMemberLogin();
            MemberInfo memberInfo=member.getMemberInfo();
            if(memberLogin==null){
                memberLogin=new MemberLogin();
            }
            if(memberInfo==null){
                memberInfo=new MemberInfo();
            }
            if(memberInfo.getMemberNickname()==null){
                memberInfo.setMemberNickname(memberLogin.getMemberLogingName());
            }
            returnMap.put("isLogin", "0");// 已登录
            returnMap.put("nickName",memberInfo.getMemberNickname());
            returnMap.put("memberId", memberInfo.getMemberId());
            if(UserUtils.isMobileLogin(request))
            {
                returnMap.put("isMobileLogin", "true");
            }else
            {
                returnMap.put("isMobileLogin", "false");
            }
        }else{
            returnMap.put("isLogin", "1");// 未登录
        }
        return returnMap;
    }
    
   /**
    * 查询wap端右侧导航栏信息
    * @param catalogCode
    * @return
    * @throws Exception
    */
    @RequestMapping(value = "getRightNavigation",method = RequestMethod.POST)
    public @ResponseBody Catalog getRightNavigation(String catalogCode) throws Exception 
    {
        Map<String, Object> returnMap = new HashMap<String, Object>(); // 存储栏目信息
        String codeKey = "E007_ECOP_getCatalogGoodsListByCode_" + catalogCode;
       // JedisCluster.del(codeKey);
        // 查询catalogId
        String catalogId = (String) JedisClusterUtils.getObject(codeKey);
        if (catalogId == null) {
            Catalog catalogCondition = new Catalog();
            catalogCondition.setChanId("E004");
            catalogCondition.setCatalogCode(catalogCode);
            catalogId = catalogService.getCatalogByCode(catalogCondition);
            
            if (null != catalogId) {
                JedisClusterUtils.setObject(codeKey, catalogId, 86400);
            }
        }
        
        // 查询catalog 信息
        String cacheKey = "E007_ECOP_getCatalogGoodsListById_" + catalogId;
        Catalog catalog = (Catalog) JedisClusterUtils.getObject(cacheKey);
          
          // 如果缓存中不存在数据，则调用ECOP接口查询数据并进行缓存
          if (catalog == null) {
              Catalog catalogCondition = new Catalog();
              catalogCondition.setChanId("E004");
              catalogCondition.setCatalogId(catalogId);
              catalog = catalogService.getCatalogGoodsListById(catalogCondition);
              if (null != catalog) {
                  JedisClusterUtils.setObject(cacheKey, catalog, 86400);
              }
          }
        
                 
        return catalog;
    }
    
    
}
