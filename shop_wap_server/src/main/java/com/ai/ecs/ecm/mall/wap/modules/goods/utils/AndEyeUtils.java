package com.ai.ecs.ecm.mall.wap.modules.goods.utils;

import com.ai.ecs.goods.entity.goods.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 和目业务参数转换
 * Created by think on 2018/3/2.
 */
public class AndEyeUtils {


    /**
     * 转换和家庭宽带节点
     * @param goodsInfoList
     * @return
     */
    public static List<AndEyeItemEntity> covertAndEye(List<TfGoodsInfo> goodsInfoList){
        if(goodsInfoList==null || goodsInfoList.size()==0){
            return null;
        }
        List<AndEyeItemEntity>  andEyeItemEntities = new ArrayList<AndEyeItemEntity>();
        for(TfGoodsInfo goodsInfo : goodsInfoList){
            AndEyeItemEntity andEyeItemEntity = new AndEyeItemEntity();
            TfGoodsSku goodsSku = goodsInfo.getTfGoodsSkuList().get(0);
            andEyeItemEntity.setGoodsId(goodsInfo.getGoodsId());
            andEyeItemEntity.setGoodsSkuId(goodsSku.getGoodsSkuId());
            andEyeItemEntity.setDesc(goodsInfo.getGoodsShortDesc());
            andEyeItemEntity.setName(goodsInfo.getGoodsName());

            if(goodsSku.getTfGoodsBusiParamList()!=null){
                for(TfGoodsBusiParam goodsBusiParam : goodsSku.getTfGoodsBusiParamList()){
                    if("BIZ_TYPE_CODE".equals(goodsBusiParam.getSkuBusiParamName())){
                        andEyeItemEntity.setBizTypeCode(goodsBusiParam.getSkuBusiParamValue());
                    }
                    else if("BIZ_CODE".equals(goodsBusiParam.getSkuBusiParamName())){
                        andEyeItemEntity.setBizCode(goodsBusiParam.getSkuBusiParamValue());
                    }
                    else if("SP_CODE".equals(goodsBusiParam.getSkuBusiParamName())){
                        andEyeItemEntity.setSpCode(goodsBusiParam.getSkuBusiParamValue());
                    }
                }
            }
            for(TfGoodsSkuValueExt goodsSkuValueExt : goodsSku.getTfGoodsSkuValueExtList()){
                //价格
                if("E007".equals(goodsSkuValueExt.getChnlCode())){
                    andEyeItemEntity.setPrice(Double.valueOf(goodsSkuValueExt.getBarePrice()/100.0));
                    break;
                }
            }
            andEyeItemEntities.add(andEyeItemEntity);
        }

        return andEyeItemEntities;
    }
}
