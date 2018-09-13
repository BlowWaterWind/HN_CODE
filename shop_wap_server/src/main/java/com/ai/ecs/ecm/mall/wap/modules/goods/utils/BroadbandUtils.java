package com.ai.ecs.ecm.mall.wap.modules.goods.utils;

import java.util.*;

import org.apache.http.conn.params.ConnConnectionParamBean;
import org.springframework.util.CollectionUtils;

import com.ai.ecs.common.utils.Collections3;
import com.ai.ecs.goods.entity.goods.BroadbandItemEntity;
import com.ai.ecs.goods.entity.goods.TfGoodsBusiParam;
import com.ai.ecs.goods.entity.goods.TfGoodsInfo;
import com.ai.ecs.goods.entity.goods.TfGoodsLabel;
import com.ai.ecs.goods.entity.goods.TfGoodsSku;
import com.ai.ecs.goods.entity.goods.TfGoodsSkuValueExt;
import com.ai.ecs.goods.entity.goods.TfGoodsStatic;
import com.google.common.collect.Lists;

/**
 * 宽带专区  工具类
 * @author tangfan
 *
 */
public class BroadbandUtils {
	
	
	/**
	 * 转换为宽带信息节点
	 * @param goodsInfoList
	 * @return
	 * @throws Exception 
	 * @throws NumberFormatException 
	 */
	public static  Map<String,List<BroadbandItemEntity>> convertBroadbandItem(List<TfGoodsInfo> goodsInfoList) throws NumberFormatException, Exception{
		Map<String,List<BroadbandItemEntity>> resMap = new HashMap<String,List<BroadbandItemEntity>>();
		List<BroadbandItemEntity>  broadbandItemList20 = new ArrayList<BroadbandItemEntity>();
		List<BroadbandItemEntity>  broadbandItemList50 = new ArrayList<BroadbandItemEntity>();
		List<BroadbandItemEntity>  broadbandItemList100 = new ArrayList<BroadbandItemEntity>();
		List<BroadbandItemEntity>  broadbandItemListAll = new ArrayList<BroadbandItemEntity>();
		if(goodsInfoList==null){
			return resMap;
		}
		
		for(TfGoodsInfo goodsInfo : goodsInfoList){
			BroadbandItemEntity broadbandItem = null;
			for(TfGoodsSku goodsSku : goodsInfo.getTfGoodsSkuList()){
				 broadbandItem = new BroadbandItemEntity();
				 broadbandItem.setGoodsId(goodsInfo.getGoodsId());
				 broadbandItem.setGoodsSkuId(goodsSku.getGoodsSkuId());
				 broadbandItem.setDesc(goodsInfo.getGoodsShortDesc());
				 if(goodsSku.getTfGoodsSkuAttrList()!=null){
					 //期限
					 broadbandItem.setTerm(goodsSku.getTfGoodsSkuAttrList().get(0).getGoodsAttrValue());
				 }
				 for(TfGoodsSkuValueExt goodsSkuValueExt : goodsSku.getTfGoodsSkuValueExtList()){
					//价格 
					 if("E007".equals(goodsSkuValueExt.getChnlCode())){
						 broadbandItem.setPrice(Double.valueOf(goodsSkuValueExt.getBarePrice()/100));
						 break;
					 }
				 }
				 for(TfGoodsLabel goodsLabel : goodsInfo.getTfGoodsLabelList()){
					 //标签
					 broadbandItem.setLabelId(goodsLabel.getGoodsLabelId());
					 broadbandItem.setLabelName(goodsLabel.getGoodsLabelName());
				 }
				if(goodsInfo.getGoodsName().contains("20M")){
					 broadbandItem.setBandWidth(20L);
					 broadbandItemList20.add(broadbandItem);
				}
				else if(goodsInfo.getGoodsName().contains("50M")){
					 broadbandItem.setBandWidth(50L);
					 broadbandItemList50.add(broadbandItem);
				}
				else if(goodsInfo.getGoodsName().contains("100M")){
					 broadbandItem.setBandWidth(100L);
					 broadbandItemList100.add(broadbandItem);
				}
				broadbandItemListAll.add(broadbandItem);
			}
		}
		resMap.put("20M", broadbandItemList20);
		resMap.put("50M", broadbandItemList50);
		resMap.put("100M", broadbandItemList100);
		resMap.put("all", broadbandItemListAll);
		return resMap;
	}
	
	
	/**
	 * 转换为宽带信息节点
	 * @return
	 * @throws Exception 
	 * @throws NumberFormatException 
	 */
	public static BroadbandItemEntity convertBroadbandItemInfo(TfGoodsInfo goodsInfo) throws NumberFormatException, Exception{
		if(goodsInfo==null){
			return null;
		}
		TfGoodsSku goodsSku = goodsInfo.getTfGoodsSkuList().get(0);
		BroadbandItemEntity broadbandItem = new BroadbandItemEntity(); 
		 broadbandItem.setGoodsId(goodsInfo.getGoodsId());
		 broadbandItem.setBroadbandItemName(goodsInfo.getGoodsName());
		 broadbandItem.setGoodsSkuId(goodsSku.getGoodsSkuId());
		 broadbandItem.setDesc(goodsInfo.getGoodsShortDesc());
		 if(goodsSku.getTfGoodsSkuAttrList()!=null){
			 //期限
			 broadbandItem.setTerm(goodsSku.getTfGoodsSkuAttrList().get(0).getGoodsAttrValue());
		 }
		 for(TfGoodsSkuValueExt goodsSkuValueExt : goodsSku.getTfGoodsSkuValueExtList()){
			//价格 
			 if("E007".equals(goodsSkuValueExt.getChnlCode())){
				 broadbandItem.setPrice(Double.valueOf(goodsSkuValueExt.getBarePrice())/100);
				 break;
			 }
		 }
		if(goodsSku.getTfGoodsBusiParamList()!=null){
			for(TfGoodsBusiParam  goodsBusiParam : goodsSku.getTfGoodsBusiParamList()){
				if("STB_PRICE".equals(goodsBusiParam.getSkuBusiParamName())){
					broadbandItem.setStbPrice(Long.valueOf(goodsBusiParam.getSkuBusiParamValue()));
				}
				else if("MODEM_PRICE".equals(goodsBusiParam.getSkuBusiParamName())){
					broadbandItem.setModemPrice(Long.valueOf(goodsBusiParam.getSkuBusiParamValue()));
				}
			}
		}

		if(goodsInfo.getGoodsName().contains("20M")){
			 broadbandItem.setBandWidth(20L);
		}
		else if(goodsInfo.getGoodsName().contains("50M")){
			 broadbandItem.setBandWidth(50L);
		}
		else if(goodsInfo.getGoodsName().contains("100M")){
			broadbandItem.setBandWidth(100L);
		}
		return broadbandItem;
		
	}
	
	
	/**
	 * 转换为宽带节点列表
	 * @param goodsInfoList
	 * @return
	 * @throws Exception 
	 * @throws NumberFormatException 
	 */
	public static  List<BroadbandItemEntity>   convertBroadbandItemList(List<TfGoodsInfo> goodsInfoList) throws NumberFormatException, Exception{
		List<BroadbandItemEntity>  broadbandItemList = Lists.newArrayList();
		for(TfGoodsInfo goodsInfo : goodsInfoList){
			BroadbandItemEntity broadbandItem = null;
			for(TfGoodsSku goodsSku : goodsInfo.getTfGoodsSkuList()){
				 broadbandItem = new BroadbandItemEntity();
				 broadbandItem.setGoodsId(goodsInfo.getGoodsId());
				 broadbandItem.setGoodsSkuId(goodsSku.getGoodsSkuId());
				 broadbandItem.setDesc(goodsInfo.getGoodsShortDesc());
				 //速率
				 if(goodsInfo.getGoodsName().contains("20M")){
					 broadbandItem.setBandWidth(20L);
				}
				else if(goodsInfo.getGoodsName().contains("50M")){
					 broadbandItem.setBandWidth(50L);
				}
				else if(goodsInfo.getGoodsName().contains("100M")){
					 broadbandItem.setBandWidth(100L);
				}
				 
				 if(goodsSku.getTfGoodsSkuAttrList()!=null){
					 //期限
					 broadbandItem.setTerm(goodsSku.getTfGoodsSkuAttrList().get(0).getGoodsAttrValue());
				 }
				 for(TfGoodsSkuValueExt goodsSkuValueExt : goodsSku.getTfGoodsSkuValueExtList()){
					//价格 
					 if("E007".equals(goodsSkuValueExt.getChnlCode())){
						 broadbandItem.setPrice(Double.valueOf(goodsSkuValueExt.getBarePrice()/100));
						 break;
					 }
				 }
				 for(TfGoodsLabel goodsLabel : goodsInfo.getTfGoodsLabelList()){
					 //标签
					 broadbandItem.setLabelId(goodsLabel.getGoodsLabelId());
					 broadbandItem.setLabelName(goodsLabel.getGoodsLabelName());
				 }
				 broadbandItemList.add(broadbandItem);
			}
		}
		return broadbandItemList;
	}
	
	
	/**
	 * 转换为新装宽带节点列表
	 * @param goodsInfoList
	 * @return
	 * @throws Exception 
	 * @throws NumberFormatException 
	 */
	public static  List<BroadbandItemEntity>   convertInstallBroadbandItemList(List<TfGoodsInfo> goodsInfoList) throws NumberFormatException, Exception{
		if(goodsInfoList==null ||goodsInfoList.size()==0){
			return null;
		}
		List<BroadbandItemEntity>  broadbandItemList = Lists.newArrayList();
		for(TfGoodsInfo goodsInfo : goodsInfoList){
			BroadbandItemEntity broadbandItem = null;
			//速率
			String goodsName = goodsInfo.getGoodsName();
			int startIndex = goodsName.indexOf("(");
			int endIndex = goodsName.indexOf(")");
			Long bandWidth =Long.valueOf(goodsName.substring(startIndex+1, endIndex-1));
			for(TfGoodsSku goodsSku : goodsInfo.getTfGoodsSkuList()){
				 broadbandItem = new BroadbandItemEntity();
				 broadbandItem.setGoodsId(goodsInfo.getGoodsId());
				 broadbandItem.setGoodsSkuId(goodsSku.getGoodsSkuId());
				 broadbandItem.setDesc(goodsInfo.getGoodsShortDesc());
				 broadbandItem.setBandWidth(bandWidth);
				 
				 if(goodsSku.getTfGoodsSkuAttrList()!=null){
					 //期限
					 broadbandItem.setTerm(goodsSku.getTfGoodsSkuAttrList().get(0).getGoodsAttrValue());
				 }
				 for(TfGoodsSkuValueExt goodsSkuValueExt : goodsSku.getTfGoodsSkuValueExtList()){
					//价格 
					 if("E007".equals(goodsSkuValueExt.getChnlCode())){
						 broadbandItem.setPrice(Double.valueOf(goodsSkuValueExt.getBarePrice())/100);
						 break;
					 }
				 }
				 for(TfGoodsLabel goodsLabel : goodsInfo.getTfGoodsLabelList()){
					 //标签
					 broadbandItem.setLabelId(goodsLabel.getGoodsLabelId());
					 broadbandItem.setLabelName(goodsLabel.getGoodsLabelName());
				 }
				 //业务参数
				 if(!CollectionUtils.isEmpty(goodsSku.getTfGoodsBusiParamList())){
					 for(TfGoodsBusiParam  goodsBusiParam : goodsSku.getTfGoodsBusiParamList()){
						 if("PRODUCT_ID".equals(goodsBusiParam.getSkuBusiParamName())){
							 broadbandItem.setProductId(goodsBusiParam.getSkuBusiParamName());
						 }
						 else if("PACKAGE_ID".equals(goodsBusiParam.getSkuBusiParamName())){
							 broadbandItem.setPackageId(goodsBusiParam.getSkuBusiParamName());
						 }
						 else if("DISCNT_CODE".equals(goodsBusiParam.getSkuBusiParamName())){
							 broadbandItem.setDiscntCode(goodsBusiParam.getSkuBusiParamName());
						 }
					 }
				 }
				 
				 broadbandItemList.add(broadbandItem);
			}
		}
		return broadbandItemList;
	}
	
	
	
	
	/**
	 * 转换和家庭宽带节点
	 * @param goodsInfoList
	 * @return
	 */
	public static List<BroadbandItemEntity>   convertHeBroadbandItemEntityList(List<TfGoodsInfo> goodsInfoList){
		if(goodsInfoList==null || goodsInfoList.size()==0){
			return null;
		}
		List<BroadbandItemEntity>  heBroadbandItemEntityList = new ArrayList<BroadbandItemEntity>();
		for(TfGoodsInfo goodsInfo : goodsInfoList){
			BroadbandItemEntity broadbandItem = new BroadbandItemEntity(); 
			TfGoodsSku goodsSku = goodsInfo.getTfGoodsSkuList().get(0); 
			broadbandItem.setGoodsId(goodsInfo.getGoodsId());
			 broadbandItem.setGoodsSkuId(goodsSku.getGoodsSkuId());
			 broadbandItem.setDesc(goodsInfo.getGoodsShortDesc());
			 broadbandItem.setBroadbandItemName(goodsInfo.getGoodsName());
			 
			 if(goodsSku.getTfGoodsBusiParamList()!=null){
				 for(TfGoodsBusiParam  goodsBusiParam : goodsSku.getTfGoodsBusiParamList()){
					 if("PRODUCT_ID".equals(goodsBusiParam.getSkuBusiParamName())){
						 broadbandItem.setProductId(goodsBusiParam.getSkuBusiParamValue());
					 }
					 else if("PACKAGE_ID".equals(goodsBusiParam.getSkuBusiParamName())){
						 broadbandItem.setPackageId(goodsBusiParam.getSkuBusiParamValue());
					 }
					 else if("ELEMENT_ID".equals(goodsBusiParam.getSkuBusiParamName())){
						 broadbandItem.setDiscntCode(goodsBusiParam.getSkuBusiParamValue());
					 }
					 else if("HE_PRODUCT_ID".equals(goodsBusiParam.getSkuBusiParamName())){
						 broadbandItem.setHeProductId(goodsBusiParam.getSkuBusiParamValue());
					 }
				 }
			 }
			 if(goodsSku.getTfGoodsSkuAttrList()!=null){
				 //带宽
				 if("20M".equals(goodsSku.getTfGoodsSkuAttrList().get(0).getGoodsAttrValue())){
					 broadbandItem.setBandWidth(20L);
				 }else if("50M".equals(goodsSku.getTfGoodsSkuAttrList().get(0).getGoodsAttrValue())){
					 broadbandItem.setBandWidth(50L);
				 }else{
					 broadbandItem.setBandWidth(100L);
				 }
			 }
			 for(TfGoodsSkuValueExt goodsSkuValueExt : goodsSku.getTfGoodsSkuValueExtList()){
				//价格 
				 if("E007".equals(goodsSkuValueExt.getChnlCode())){
					 broadbandItem.setPrice(Double.valueOf(goodsSkuValueExt.getBarePrice()/100.0));
					 break;
				 }
			 }
			heBroadbandItemEntityList.add(broadbandItem);
		}
		
		return heBroadbandItemEntityList;
	}
	
	/**
	 * 转换和家庭宽带节点
	 * @param goodsInfoList
	 * @return
	 */
	public static List<BroadbandItemEntity>   convertNewHeBroadbandItemEntityList(List<TfGoodsInfo> goodsInfoList){
		if(goodsInfoList==null || goodsInfoList.size()==0){
			return null;
		}
		List<BroadbandItemEntity>  heBroadbandItemEntityList = new ArrayList<BroadbandItemEntity>();
		for(TfGoodsInfo goodsInfo : goodsInfoList){
			BroadbandItemEntity broadbandItem = new BroadbandItemEntity();
			TfGoodsSku goodsSku = goodsInfo.getTfGoodsSkuList().get(0);
			broadbandItem.setGoodsId(goodsInfo.getGoodsId());
			 broadbandItem.setGoodsSkuId(goodsSku.getGoodsSkuId());
			 broadbandItem.setDesc(goodsInfo.getGoodsShortDesc());
			 broadbandItem.setBroadbandItemName(goodsInfo.getGoodsName());

			 if(goodsSku.getTfGoodsBusiParamList()!=null){
				 for(TfGoodsBusiParam  goodsBusiParam : goodsSku.getTfGoodsBusiParamList()){
					 if("PRODUCT_ID".equals(goodsBusiParam.getSkuBusiParamName())){
						 broadbandItem.setProductId(goodsBusiParam.getSkuBusiParamValue());
					 }
					 else if("PACKAGE_ID".equals(goodsBusiParam.getSkuBusiParamName())){
						 broadbandItem.setPackageId(goodsBusiParam.getSkuBusiParamValue());
					 }
					 else if("ELEMENT_ID".equals(goodsBusiParam.getSkuBusiParamName())){
						 broadbandItem.setDiscntCode(goodsBusiParam.getSkuBusiParamValue());
					 }
					 else if("HE_PRODUCT_ID".equals(goodsBusiParam.getSkuBusiParamName())){
						 broadbandItem.setHeProductId(goodsBusiParam.getSkuBusiParamValue());
					 }
					 else if("GIFT_DATA".equals(goodsBusiParam.getSkuBusiParamName())){
						 broadbandItem.setGiveData(goodsBusiParam.getSkuBusiParamValue());
					 }
					 else if("MBH".equals(goodsBusiParam.getSkuBusiParamName())){
						 broadbandItem.setMbh(goodsBusiParam.getSkuBusiParamValue());
					 }
					 else if("HOME_DATA".equals(goodsBusiParam.getSkuBusiParamName())){
						 broadbandItem.setHomeData(goodsBusiParam.getSkuBusiParamValue());
					 }
					 else if("HOME_VOICE".equals(goodsBusiParam.getSkuBusiParamName())){
						 broadbandItem.setHomeVoice(goodsBusiParam.getSkuBusiParamValue());
					 }
					 else if("BAND_WIDTH".equals(goodsBusiParam.getSkuBusiParamName())){
						 broadbandItem.setBandWidth(Long.valueOf(goodsBusiParam.getSkuBusiParamValue()));
					 }
					 else if("PRODUCT_LEVEL".equals(goodsBusiParam.getSkuBusiParamName())){
						 broadbandItem.setProductLevel(Long.valueOf(goodsBusiParam.getSkuBusiParamValue()));
					 }
					 else if("DISCNT_CODE".equals(goodsBusiParam.getSkuBusiParamName())){
						 broadbandItem.setDiscntCode(goodsBusiParam.getSkuBusiParamValue());
					 }
					 else if("STB_PRICE".equals(goodsBusiParam.getSkuBusiParamName())){
						 broadbandItem.setStbPrice(Long.valueOf(goodsBusiParam.getSkuBusiParamValue()));
					 }
					 else if("MODEM_PRICE".equals(goodsBusiParam.getSkuBusiParamName())){
						 broadbandItem.setModemPrice(Long.valueOf(goodsBusiParam.getSkuBusiParamValue()));
					 }

				 }
			 }
//			 if(goodsSku.getTfGoodsSkuAttrList()!=null){
//				 //带宽
//				 if("20M".equals(goodsSku.getTfGoodsSkuAttrList().get(0).getGoodsAttrValue())){
//					 broadbandItem.setBandWidth(20L);
//				 }else if("50M".equals(goodsSku.getTfGoodsSkuAttrList().get(0).getGoodsAttrValue())){
//					 broadbandItem.setBandWidth(50L);
//				 }else{
//					 broadbandItem.setBandWidth(100L);
//				 }
//			 }
			 for(TfGoodsSkuValueExt goodsSkuValueExt : goodsSku.getTfGoodsSkuValueExtList()){
				//价格
				 if("E007".equals(goodsSkuValueExt.getChnlCode())){
					 broadbandItem.setPrice(Double.valueOf(goodsSkuValueExt.getBarePrice()/100.0));
					 break;
				 }
			 }
			heBroadbandItemEntityList.add(broadbandItem);
		}

		return heBroadbandItemEntityList;
	}

	/**
	 * 转换消费叠加型节点
	 * @param goodsInfoList
	 * @return
	 */
	public static List<BroadbandItemEntity>   convertConsupostnItemEntityList(List<TfGoodsInfo> goodsInfoList){
		if(goodsInfoList==null || goodsInfoList.size()==0){
			return null;
		}
		List<BroadbandItemEntity>  consupostnItemEntityList = new ArrayList<BroadbandItemEntity>();
		for(TfGoodsInfo goodsInfo : goodsInfoList){
			BroadbandItemEntity broadbandItem = new BroadbandItemEntity(); 
			TfGoodsSku goodsSku = goodsInfo.getTfGoodsSkuList().get(0); 
			broadbandItem.setGoodsId(goodsInfo.getGoodsId());
			 broadbandItem.setGoodsSkuId(goodsSku.getGoodsSkuId());
			 broadbandItem.setDesc(goodsInfo.getGoodsShortDesc());
			 broadbandItem.setBroadbandItemName(goodsInfo.getGoodsName());
			//图片路径
			if(!Collections3.isEmpty(goodsInfo.getGoodsStaticList())){
				TfGoodsStatic goodsStatic = goodsInfo.getGoodsStaticList().get(0);
				broadbandItem.setImgUrl(goodsStatic.getGoodsStaticUrl());
			}
			if(goodsSku.getTfGoodsBusiParamList()!=null){
				 for(TfGoodsBusiParam  goodsBusiParam : goodsSku.getTfGoodsBusiParamList()){
					 if("PRODUCT_ID".equals(goodsBusiParam.getSkuBusiParamName())){
						 broadbandItem.setProductId(goodsBusiParam.getSkuBusiParamValue());
					 }else if("GIFT_ELEMENT_ID".equals(goodsBusiParam.getSkuBusiParamName())){
						 broadbandItem.setPackageId(goodsBusiParam.getSkuBusiParamValue());
					 }else if("BAND_WIDTH".equals(goodsBusiParam.getSkuBusiParamName())){
						 broadbandItem.setBandWidth(Long.valueOf(goodsBusiParam.getSkuBusiParamValue()));;
					 }else if("MONTH_COST".equals(goodsBusiParam.getSkuBusiParamName())){
						 broadbandItem.setDiscntCode(goodsBusiParam.getSkuBusiParamValue());
					 }else if("ORDER_PARAMCODE".equals(goodsBusiParam.getSkuBusiParamName())){
						 broadbandItem.setOrderParamCode(goodsBusiParam.getSkuBusiParamValue());
					 }else if("EXIST_PARAMCODE".equals(goodsBusiParam.getSkuBusiParamName())){
						 broadbandItem.setExistParamCode(goodsBusiParam.getSkuBusiParamValue());
					 }else if("SHORT_DESC".equals(goodsBusiParam.getSkuBusiParamName())){
						 broadbandItem.setExistParamCode(goodsBusiParam.getSkuBusiParamValue());
					 }
				 }
			 }
			 for(TfGoodsSkuValueExt goodsSkuValueExt : goodsSku.getTfGoodsSkuValueExtList()){
				//价格 
				 if("E006".equals(goodsSkuValueExt.getChnlCode())){
					 broadbandItem.setPrice(Double.valueOf(goodsSkuValueExt.getBarePrice()/100.0));
					 break;
				 }
			 }
			 consupostnItemEntityList.add(broadbandItem);
		}
		
		return consupostnItemEntityList;
	}
	
	/**
	 * 转换为宽带信息节点
	 * @return
	 * @throws Exception 
	 * @throws NumberFormatException 
	 */
	public static BroadbandItemEntity convertConsupostnItemInfo(TfGoodsInfo goodsInfo) throws NumberFormatException, Exception{
		if(goodsInfo==null){
			return null;
		}
		TfGoodsSku goodsSku = goodsInfo.getTfGoodsSkuList().get(0);
		BroadbandItemEntity broadbandItem = new BroadbandItemEntity(); 
		 broadbandItem.setGoodsId(goodsInfo.getGoodsId());
		 broadbandItem.setGoodsSkuId(goodsSku.getGoodsSkuId());
		 broadbandItem.setDesc(goodsInfo.getGoodsShortDesc());
		 broadbandItem.setBroadbandItemName(goodsInfo.getGoodsName());
		 if(goodsSku.getTfGoodsSkuAttrList()!=null){
			 //期限
			 broadbandItem.setTerm(goodsSku.getTfGoodsSkuAttrList().get(0).getGoodsAttrValue());
		 }
		 for(TfGoodsSkuValueExt goodsSkuValueExt : goodsSku.getTfGoodsSkuValueExtList()){
			//价格 
			 if("E007".equals(goodsSkuValueExt.getChnlCode())){
				 broadbandItem.setPrice(Double.valueOf(goodsSkuValueExt.getBarePrice()/100.0));
				 break;
			 }
		 }
		 //图片路径
		 if(!Collections3.isEmpty(goodsInfo.getGoodsStaticList())){
			 TfGoodsStatic goodsStatic = goodsInfo.getGoodsStaticList().get(0);
			 broadbandItem.setImgUrl(goodsStatic.getGoodsStaticUrl());
		 }
		if(goodsSku.getTfGoodsBusiParamList()!=null){
			for(TfGoodsBusiParam  goodsBusiParam : goodsSku.getTfGoodsBusiParamList()){
				 if("COMSU_PRODUCT_ID".equals(goodsBusiParam.getSkuBusiParamName())){
					 broadbandItem.setProductId(goodsBusiParam.getSkuBusiParamValue());
				 }
				 else if("GIFT_ELEMENT_ID".equals(goodsBusiParam.getSkuBusiParamName())){
					 broadbandItem.setPackageId(goodsBusiParam.getSkuBusiParamValue());
				 }
				 else if("ACT_ELEMENT_ID".equals(goodsBusiParam.getSkuBusiParamName())){
					 broadbandItem.setDiscntCode(goodsBusiParam.getSkuBusiParamValue());
				 }
				 else if("STB_PRICE".equals(goodsBusiParam.getSkuBusiParamName())){
					 broadbandItem.setStbPrice(Long.valueOf(goodsBusiParam.getSkuBusiParamValue()));
				 }
				 else if("MODEM_PRICE".equals(goodsBusiParam.getSkuBusiParamName())){
					 broadbandItem.setModemPrice(Long.valueOf(goodsBusiParam.getSkuBusiParamValue()));
				 }
			 }
		}
		if(goodsInfo.getGoodsName().contains("20M")){
			 broadbandItem.setBandWidth(20L);
		}
		else if(goodsInfo.getGoodsName().contains("50M")){
			 broadbandItem.setBandWidth(50L);
		}
		else if(goodsInfo.getGoodsName().contains("100M")){
			broadbandItem.setBandWidth(100L);
		}
		return broadbandItem;
		
	}
	
	/**
	 * 转换魔百和节点
	 * @param goodsInfoList
	 * @return
	 */
	public static List<BroadbandItemEntity> convertMbhItemEntityList(List<TfGoodsInfo> goodsInfoList){
		if(goodsInfoList==null || goodsInfoList.size()==0){
			return null;
		}
		List<BroadbandItemEntity>  mbhItemEntityList = new ArrayList<BroadbandItemEntity>();
		for(TfGoodsInfo goodsInfo : goodsInfoList){
			BroadbandItemEntity broadbandItem = new BroadbandItemEntity(); 
			TfGoodsSku goodsSku = goodsInfo.getTfGoodsSkuList().get(0); 
			broadbandItem.setGoodsId(goodsInfo.getGoodsId());
			broadbandItem.setGoodsSkuId(goodsSku.getGoodsSkuId());
			broadbandItem.setDesc(goodsInfo.getGoodsShortDesc());
			broadbandItem.setBroadbandItemName(goodsInfo.getGoodsName());
			broadbandItem.setTerm(goodsInfo.getKeyWord());
			//图片路径
			if(!Collections3.isEmpty(goodsInfo.getGoodsStaticList())){
				TfGoodsStatic goodsStatic = goodsInfo.getGoodsStaticList().get(0);
				broadbandItem.setImgUrl(goodsStatic.getGoodsStaticUrl());
			}
			if(goodsSku.getTfGoodsBusiParamList()!=null){
				 for(TfGoodsBusiParam  goodsBusiParam : goodsSku.getTfGoodsBusiParamList()){
					 if("TV_PRODUCT_ID".equals(goodsBusiParam.getSkuBusiParamName())){
						 broadbandItem.setProductId(goodsBusiParam.getSkuBusiParamValue());
					 }else if("REQ_PACKAGE_ID".equals(goodsBusiParam.getSkuBusiParamName())){
						 broadbandItem.setPackageId(goodsBusiParam.getSkuBusiParamValue());
					 }else if("REQ_ELEMENT_ID".equals(goodsBusiParam.getSkuBusiParamName())){
						 broadbandItem.setDiscntCode(goodsBusiParam.getSkuBusiParamValue());
					 }else if("GIFT_ELEMENT_ID".equals(goodsBusiParam.getSkuBusiParamName())){
						 broadbandItem.setHeProductId(goodsBusiParam.getSkuBusiParamValue());
					 }
				 }
			 }
			 for(TfGoodsSkuValueExt goodsSkuValueExt : goodsSku.getTfGoodsSkuValueExtList()){
				//价格 
				 if("E006".equals(goodsSkuValueExt.getChnlCode())){
					 broadbandItem.setPrice(Double.valueOf(goodsSkuValueExt.getBarePrice()/100.0));
					 break;
				 }if("E007".equals(goodsSkuValueExt.getChnlCode())){
					 broadbandItem.setPrice(Double.valueOf(goodsSkuValueExt.getBarePrice()/100.0));
					 break;
				 }
			 }
			 mbhItemEntityList.add(broadbandItem);
		}
		
		return mbhItemEntityList;
	}

	/**
	 * 转换和家庭宽带节点
	 * @param goodsInfoList
	 * @return
	 */
	public static List<BroadbandItemEntity>   convertNewHeHalfItemEntityList(List<TfGoodsInfo> goodsInfoList){
		if(goodsInfoList==null || goodsInfoList.size()==0){
			return null;
		}
		List<BroadbandItemEntity>  heBroadbandItemEntityList = new ArrayList<BroadbandItemEntity>();
		for(TfGoodsInfo goodsInfo : goodsInfoList){
			BroadbandItemEntity broadbandItem = new BroadbandItemEntity();
			TfGoodsSku goodsSku = goodsInfo.getTfGoodsSkuList().get(0);
			broadbandItem.setGoodsId(goodsInfo.getGoodsId());
			broadbandItem.setGoodsSkuId(goodsSku.getGoodsSkuId());
			broadbandItem.setDesc(goodsInfo.getGoodsShortDesc());
			broadbandItem.setBroadbandItemName(goodsInfo.getGoodsName());

			if(goodsSku.getTfGoodsBusiParamList()!=null){
				for(TfGoodsBusiParam  goodsBusiParam : goodsSku.getTfGoodsBusiParamList()){
					if("PRODUCT_ID".equals(goodsBusiParam.getSkuBusiParamName())){
						broadbandItem.setProductId(goodsBusiParam.getSkuBusiParamValue());
					}
					else if("PACKAGE_ID".equals(goodsBusiParam.getSkuBusiParamName())){
						broadbandItem.setPackageId(goodsBusiParam.getSkuBusiParamValue());
					}
					else if("PRODUCT_DESC".equals(goodsBusiParam.getSkuBusiParamName())){
						broadbandItem.setProductDesc(goodsBusiParam.getSkuBusiParamValue());
					}
					else if("PRODUCT_LEVEL".equals(goodsBusiParam.getSkuBusiParamName())){
						broadbandItem.setProductLevel(Long.valueOf(goodsBusiParam.getSkuBusiParamValue()));
					}
					else if("STANDAR_PRICE".equals(goodsBusiParam.getSkuBusiParamName())){
						broadbandItem.setStandarPrice(Long.valueOf(goodsBusiParam.getSkuBusiParamValue()));
					}
					else if("ELEMENT_ID".equals(goodsBusiParam.getSkuBusiParamName())){
						broadbandItem.setGiveData(goodsBusiParam.getSkuBusiParamValue());
					}
				}
			}
//
			for(TfGoodsSkuValueExt goodsSkuValueExt : goodsSku.getTfGoodsSkuValueExtList()){
				//价格
				if("E007".equals(goodsSkuValueExt.getChnlCode())){
					broadbandItem.setPrice(Double.valueOf(goodsSkuValueExt.getBarePrice()/100.0));
					break;
				}
			}
			heBroadbandItemEntityList.add(broadbandItem);
		}
		if(heBroadbandItemEntityList.size()>0){
			Collections.sort(heBroadbandItemEntityList, new Comparator(){
				@Override
				public int compare(Object o1, Object o2) {
					BroadbandItemEntity item1=(BroadbandItemEntity)o1;
					BroadbandItemEntity item2=(BroadbandItemEntity)o2;
					if(item1.getPrice()>item2.getPrice()){
						return 1;
					}else if(item1.getPrice().doubleValue()==item2.getPrice().doubleValue()){
						return 0;
					}else{
						return -1;
					}
				}
			});
		}
		return heBroadbandItemEntityList;
	}

}

