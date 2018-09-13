package com.ai.ecs.ecm.mall.wap.modules.goods.vo;

import java.io.Serializable;

public class VisualActivityInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private  String serialNumber;//手机号码
	private  String activityId;//活动id
	private  String activityUrl; //活动宣传图片
	private  String elementId;//商品业务编码
	private  Long goodsId;//商品ID
	private  String goodsSkuId;//商品的skuID
	private  String goodsSkuPrice;//商品的价格
	private  Long marketPrice;//活动价格
	private  String goodsUrl;//商品的URL
	private  String goodsName;//商品的名称
	private  String goodsFormat;//商品的附加属性
	private  String orderSubmitType;//订单类型
	private  String busiType;//业务类型
	private  String shopId;//店铺的id
	private  String shopName;//店铺名称
	private  String shopTypeId;//店铺类型Id
	private  String productId;//产品id
	private  String chanId;//渠道编码
	private  String staffId;//员工工号
	private  String tradeDepartPassword;//工号密码
	private  String orderType;//订单类型（虚拟和实物）
	private  String inModeCode;
	private  String XY_packageId;//营销活动的业务包编码
	private  String XY_productId;//营销活动的业务产品编码
	private  String mermberId;//下单用户ID
	private String activityName;//活动名称
	private Integer activityType;//活动类型1(走kafka队列,虚拟商品),2(走跳转连接提交订单)
	private Long activityStock;//商品库存
	private Long activityMaxQuali; //最大的资格数量
	private String activityDesc; // 活动宣传语
	private Integer userBuyLimit;//限购数量(总共可以秒多少个)默认1
	private Long startDate;//开始时间
	private Long endDate;//结束时间
	private String activitySessionName;//活动sessionId

	public Long getActivityMaxQuali() {
		return activityMaxQuali;
	}

	public void setActivityMaxQuali(Long activityMaxQuali) {
		this.activityMaxQuali = activityMaxQuali;
	}


	public String getActivityDesc() {
		return activityDesc;
	}

	public void setActivityDesc(String activityDesc) {
		this.activityDesc = activityDesc;
	}

	public String getActivityUrl() {
		return activityUrl;
	}

	public void setActivityUrl(String activityUrl) {
		this.activityUrl = activityUrl;
	}

	public Long getMarketPrice() {
		return marketPrice;
	}
	public void setMarketPrice(Long marketPrice) {
		this.marketPrice = marketPrice;
	}
	public String getActivitySessionName() {
		return activitySessionName;
	}
	public void setActivitySessionName(String activitySessionName) {
		this.activitySessionName = activitySessionName;
	}
	public String getActivityName() {
		return activityName;
	}
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
	public Integer getActivityType() {
		return activityType;
	}
	public void setActivityType(Integer activityType) {
		this.activityType = activityType;
	}
	public Long getActivityStock() {
		return activityStock;
	}
	public void setActivityStock(Long activityStock) {
		this.activityStock = activityStock;
	}
	public Integer getUserBuyLimit() {
		return userBuyLimit;
	}
	public void setUserBuyLimit(Integer userBuyLimit) {
		this.userBuyLimit = userBuyLimit;
	}
	public Long getStartDate() {
		return startDate;
	}
	public void setStartDate(Long startDate) {
		this.startDate = startDate;
	}
	public Long getEndDate() {
		return endDate;
	}
	public void setEndDate(Long endDate) {
		this.endDate = endDate;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getXY_packageId() {
		return XY_packageId;
	}
	public void setXY_packageId(String xY_packageId) {
		XY_packageId = xY_packageId;
	}
	public String getXY_productId() {
		return XY_productId;
	}
	public void setXY_productId(String xY_productId) {
		XY_productId = xY_productId;
	}
	public String getMermberId() {
		return mermberId;
	}
	public void setMermberId(String mermberId) {
		this.mermberId = mermberId;
	}
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	public String getActivityId() {
		return activityId;
	}
	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}
	public String getElementId() {
		return elementId;
	}
	public void setElementId(String elementId) {
		this.elementId = elementId;
	}
	public Long getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}
	public String getGoodsSkuId() {
		return goodsSkuId;
	}
	public void setGoodsSkuId(String goodsSkuId) {
		this.goodsSkuId = goodsSkuId;
	}
	public String getGoodsSkuPrice() {
		return goodsSkuPrice;
	}
	public void setGoodsSkuPrice(String goodsSkuPrice) {
		this.goodsSkuPrice = goodsSkuPrice;
	}
	public String getGoodsUrl() {
		return goodsUrl;
	}
	public void setGoodsUrl(String goodsUrl) {
		this.goodsUrl = goodsUrl;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getGoodsFormat() {
		return goodsFormat;
	}
	public void setGoodsFormat(String goodsFormat) {
		this.goodsFormat = goodsFormat;
	}
	public String getOrderSubmitType() {
		return orderSubmitType;
	}
	public void setOrderSubmitType(String orderSubmitType) {
		this.orderSubmitType = orderSubmitType;
	}
	public String getBusiType() {
		return busiType;
	}
	public void setBusiType(String busiType) {
		this.busiType = busiType;
	}
	public String getShopId() {
		return shopId;
	}
	public void setShopId(String shopId) {
		this.shopId = shopId;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public String getShopTypeId() {
		return shopTypeId;
	}
	public void setShopTypeId(String shopTypeId) {
		this.shopTypeId = shopTypeId;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getChanId() {
		return chanId;
	}
	public void setChanId(String chanId) {
		this.chanId = chanId;
	}
	public String getStaffId() {
		return staffId;
	}
	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}
	public String getTradeDepartPassword() {
		return tradeDepartPassword;
	}
	public void setTradeDepartPassword(String tradeDepartPassword) {
		this.tradeDepartPassword = tradeDepartPassword;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getInModeCode() {
		return inModeCode;
	}
	public void setInModeCode(String inModeCode) {
		this.inModeCode = inModeCode;
	}
}
