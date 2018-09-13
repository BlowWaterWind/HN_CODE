package com.ai.ecs.ecm.mall.wap.platform.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import redis.clients.jedis.JedisCluster;

import com.ai.ecs.common.utils.JedisClusterUtils;
import com.ai.ecs.ecop.cms.entity.Article;
import com.ai.ecs.ecop.cms.entity.BroadbandPoster;
import com.ai.ecs.ecop.cms.entity.Poster;
import com.ai.ecs.ecop.cms.service.ArticleService;
import com.ai.ecs.ecop.cms.service.BroadbandPosterService;
import com.ai.ecs.ecop.cms.service.PosterService;
import com.ai.ecs.ecop.sys.entity.Catalog;
import com.ai.ecs.ecop.sys.entity.Dict;
import com.ai.ecs.ecop.sys.service.CatalogService;
import com.ai.ecs.ecop.sys.service.DictService;
import com.ai.ecs.ecop.wap.entity.CmnetIp;
import com.ai.ecs.ecop.wap.entity.GatewayIp;
import com.ai.ecs.ecop.wap.service.AllowIpService;


@Component
public class InvokeEcop {


	@Autowired
	public CatalogService catalogService;


	@Autowired
	private JedisCluster jedisCluster;
	
	@Autowired
	private DictService dictService;
	
	 @Autowired
	public AllowIpService allowIpService;
	
	@Autowired
	private ArticleService articleService;
	
	@Autowired
	public PosterService posterService;
	
	@Autowired
	public BroadbandPosterService broadbandPosterService;

	private static final org.slf4j.Logger logger = LoggerFactory
            .getLogger(InvokeEcop.class);
	
//	/**
//	 * getGoodsInfo 根据栏目id获取Ecop栏目和绑定商品信息
//	 * @param param
//	 * @return
//	 * @throws Exception
//	 * @return Map<String,Object>返回说明
//	 * @Exception 异常说明
//	 * @author：gupq@asiainfo.com
//	 * @create：2016-3-8 下午04:10:33
//	 * @moduser：
//	 * @moddate：
//	 * @remark：
//	 */
//	public Map<String, Object> getGoodsInfo(Map<String, String> param) throws Exception {
//
//		String catalogId = param.get("catalogId");
//		String cacheKey = "WEB_ECOP_CATALOGINFO_" + catalogId;
//		Map map = new HashMap();
//		List resultList = new ArrayList();
//		String resultStr = jedisCluster.get(cacheKey);
//		if (StringUtils.isBlank(resultStr)) {
//			Catalog catalog = new Catalog();
//			catalog.setChanId("E003");
//			catalog.setCatalogId(catalogId);
//			resultList = catalogService.getCatalogGoodsListById(catalog, null);
//		} else {
//			// Map<String, Class> info = new HashMap<String, Class>();
//			// info.put("X_RESULTLIST", CommodityEc.class);
//			// info.put("X_RESULT", Catalog.class);
//
//			JSONObject jsonObj = JSONObject.fromObject(resultStr);
//			// result = (Result)JSONObject.toBean(jsonObj, Result.class,info);
//		}
//
//		map.put("X_RESULT", resultList);
//		return map;
//	}
	
	/**
	 * 获取Ecop广告位信息
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Poster> getPosterInfo(Map<String, String> param) throws Exception {

		String posterCode = param.get("posterCode");// 广告规则编码
		String cacheKey = "ECOP_AD_E007_" + posterCode;
		jedisCluster.del(cacheKey);
		List<Poster> resultList = new ArrayList<Poster>();

		// 如果缓存中不存在数据，则调用ECOP接口查询数据并进行缓存
		if (jedisCluster.exists(cacheKey)&&null!=JedisClusterUtils.getObject(cacheKey)) {
			resultList = (List<Poster>) JedisClusterUtils.getObject(cacheKey);
		} else {
			
			try{
				resultList = posterService.findAdvertisementList(posterCode, "E007");
			} catch (Exception e){
				throw e;
			}

			// 如果ECOP返回查询成功，并且有数据则进行缓存
			JedisClusterUtils.setObject(cacheKey, resultList, 0);
		}
		return resultList;
	}

	/**
	 * 获取宽带首页元素信息
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<BroadbandPoster> getBroadbandPosterInfo(String posterName,String posterType) throws Exception {
		List<BroadbandPoster> resultList = new ArrayList<BroadbandPoster>();
		BroadbandPoster broadbandPoster = new BroadbandPoster();
		broadbandPoster.setPosterName(posterName);
		broadbandPoster.setPosterType(posterType);
		broadbandPoster.setChannel("E007");
		try{
			resultList = broadbandPosterService.findAll(broadbandPoster);
		} catch (Exception e){
			throw e;
		}
		return resultList;
	}
	
	/**
	 * 获取和掌柜宽带首页元素信息
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<BroadbandPoster> getO2oBroadbandPosterInfo(String posterName,String posterType) throws Exception {
		List<BroadbandPoster> resultList = new ArrayList<BroadbandPoster>();
		BroadbandPoster broadbandPoster = new BroadbandPoster();
		broadbandPoster.setPosterName(posterName);
		broadbandPoster.setPosterType(posterType);
		broadbandPoster.setChannel("E050");
		try{
			resultList = broadbandPosterService.findAll(broadbandPoster);
		} catch (Exception e){
			throw e;
		}
		return resultList;
	}

	/**
	 * 获取Ecop文章信息
	 * @return
	 * @throws Exception
	 */
	public List<Article> getArticleInfo(Map<String, String> param) throws Exception {
		Article article = new Article();
		article.setCatalog(this.getCatalogListById(param));
		List<Article> list = articleService.findList(article);//获取文章列表
		//获取已发布的的文章列表
		Iterator<Article> iter = list.iterator();
		while(iter.hasNext()){
			if(!"3".equals(iter.next().getStatus())){
				iter.remove();
			}
		}
		return list;
	}

	/**
	 * 获取Ecop文章信息
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "initSearchInfo",method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getSearchInfo(HttpServletRequest request) throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("DATA_COUNT", "20");
		// map.put("REQ_DATA", getData());

		return map;
	}

	/**
	 * [简要描述]:根据栏目id获取Ecop栏目和绑定商品信息<br/>
	 * [详细描述]: catalogId: 栏目id<br/>
	 * @param param Map<参数名称，参数值>
	 * @return ECOP结果数据
	 * @throws Exception
	 * @exception
	 */
	public Catalog getCatalogGoodsListById(Map<String, String> param) throws Exception {

		String catalogId = param.get("catalogId");
		String cacheKey = "E006_ECOP_getCatalogGoodsListById_" + catalogId;

		jedisCluster.del(cacheKey);
		Catalog catalog = (Catalog) JedisClusterUtils.getObject(cacheKey);

		// 如果缓存中不存在数据，则调用ECOP接口查询数据并进行缓存
		if (catalog == null) {
			Catalog catalogCondition = new Catalog();
			catalogCondition.setChanId("E006");
			catalogCondition.setCatalogId(catalogId);
			catalog = catalogService.getCatalogGoodsListById(catalogCondition);
			if (null != catalog) {
				JedisClusterUtils.setObject(cacheKey, catalog, 86400);
			}
		}

		return catalog;
	}

	/**
	 * [简要描述]:根据栏目id获取Ecop栏目下子栏目信息<br/>
	 * [详细描述]: catalogId: 栏目id<br/>
	 * @param param Map<参数名称，参数值>
	 * @return ECOP结果数据
	 * @throws Exception
	 * @exception
	 */
	public Catalog getCatalogListById(Map<String, String> param) throws Exception {

		String catalogId = param.get("catalogId");
		String cacheKey = "E006_ECOP_getCatalogListById_" + catalogId;
		jedisCluster.del(cacheKey);
		Catalog catalog = (Catalog) JedisClusterUtils.getObject(cacheKey);
		
		// 如果缓存中不存在数据，则调用ECOP接口查询数据并进行缓存
		if (catalog == null) {
			Catalog catalogCondition = new Catalog();
			catalogCondition.setChanId("E006");
			catalogCondition.setCatalogId(catalogId);
			catalog = catalogService.getCatalogListById(catalogCondition);

			if (null != catalog) {
				JedisClusterUtils.setObject(cacheKey, catalog, 86400);
			}
		}

		return catalog;

	}

	/**
	 * [简要描述]:根据栏目编号查询栏目信息<br/>
	 * [详细描述]: catalogCode: 栏目Code<br/>
	 * @param param Map<参数名称，参数值>
	 * @return ECOP结果数据
	 * @throws Exception
	 * @exception
	 */
	public String getCatalogByCode(String catalogCode) throws Exception {

		Catalog catalog = new Catalog();
		catalog.setChanId("E006");
		catalog.setCatalogCode(catalogCode);

		return catalogService.getCatalogByCode(catalog);
	}

	/**
	 * getGoodsInfoByGoodsId 根据商品ID查询商品详情
	 * @param param
	 * @return
	 * @throws Exception
	 * @return Map<String, Object>返回说明
	 * @Exception 异常说明
	 * @author：wuyz@asiainfo.com
	 * @create：2016-3-16 上午09:44:25
	 * @moduser：
	 * @moddate：
	 * @remark：
	 */
	/*
	 * public Result getGoodsInfoByGoodsId(Map<String,String> param)throws Exception{ String goodsId
	 * = param.get("goodsId"); String cacheKey = "E003_ECOP_getGoodsInfoByGoodsId_"+goodsId;
	 * Map<String, Object> dataMap = new HashMap<String, Object>(); Result result =
	 * (Result)JedisClusterUtils.getObject(cacheKey); // 如果缓存中不存在数据，则调用ECOP接口查询数据并进行缓存
	 * if(result==null) { Catalog catalog=new Catalog(); catalog.setChanId("E003"); CommodityEc
	 * commodityEc=new CommodityEc(); commodityEc.setGoodsId(goodsId);
	 * catalog.setCommodityEc(commodityEc); result =
	 * catalogService.getCatalogGoodsByGoodsId(catalog); // 如果ECOP返回查询成功，并且有数据则进行缓存 if
	 * ("0000".equals(result.getxResultCode()) && CollectionUtils.isNotEmpty(result.getxInfos())){
	 * JedisClusterUtils.setObject(cacheKey, result,0); } } return result; }
	 */

	/**
	 * 
	 * getCityOrProvice 从字典表中取出全国各省和湖南省地级市
	 * @return
	 * @throws Exception
	 * @return List<String>返回说明
	 * @Exception 异常说明
	 * @author：zhangqd3@asiainfo.com
	 * @create：2016年4月16日 下午4:24:44 
	 * @moduser： 
	 * @moddate：
	 * @remark：
	 */
	public List<Dict> getCityOrProvince(String type)throws Exception{
		List<Dict> cityOrProvince = new ArrayList<Dict>();
		String cacheKey = "WEB_ECOP_DICT_" + type;
		jedisCluster.del(cacheKey);
		String resultStr = jedisCluster.get(cacheKey);
		// 如果缓存中不存在数据，则调用ECOP接口查询数据并进行缓存
		if (resultStr != "" && resultStr != null) {
			List<Dict> dictList = (List<Dict>)JedisClusterUtils.getObjectList(resultStr);
		} else {
			cityOrProvince = dictService.getDictList(type);
			JedisClusterUtils.setObjectList(cacheKey, cityOrProvince, 86400);
		}
		return cityOrProvince;
	}
	
	public CmnetIp getCmnetIpInfo(String ip)throws Exception{
        CmnetIp cmnetIp = new CmnetIp();
        cmnetIp.setPublicIp(ip);
        try{
			
			logger.info("==allowIpService.getCmnetIpInfo==request==:[" + ReflectionToStringBuilder.reflectionToString(cmnetIp, ToStringStyle.MULTI_LINE_STYLE) + "]");
			
			cmnetIp = allowIpService.getCmnetIpInfo(cmnetIp); 
			
			logger.info("==allowIpService.getCmnetIpInfo==response==:" + ReflectionToStringBuilder.reflectionToString(cmnetIp, ToStringStyle.MULTI_LINE_STYLE));
			
		} catch (Exception e){
			
			logger.error("==allowIpService.getCmnetIpInfo==response==:", e);
			
			throw e;
		}
        return cmnetIp;
    }
	
	public GatewayIp getGatewayIpInfo(String ip)throws Exception{
        GatewayIp gatewayIp = new GatewayIp();
        gatewayIp.setPublicIp(ip);
        try{
			
			logger.info("==allowIpService.getGatewayIpInfo==request==:[" + ReflectionToStringBuilder.reflectionToString(gatewayIp, ToStringStyle.MULTI_LINE_STYLE) + "]");
			
			gatewayIp = allowIpService.getGatewayIpInfo(gatewayIp); 
			
			logger.info("==allowIpService.getGatewayIpInfo==response==:" + ReflectionToStringBuilder.reflectionToString(gatewayIp, ToStringStyle.MULTI_LINE_STYLE));
			
		} catch (Exception e){
			
			logger.error("==allowIpService.getGatewayIpInfo==response==:", e);
			
			throw e;
		}
        	
        return gatewayIp;
    }

}
