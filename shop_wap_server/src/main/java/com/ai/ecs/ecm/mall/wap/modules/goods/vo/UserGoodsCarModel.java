package com.ai.ecs.ecm.mall.wap.modules.goods.vo;

import com.ai.ecs.goods.entity.goods.TfUserGoodsCar;
import com.ai.ecs.member.entity.MemberAddress;
import com.ai.ecs.merchant.shop.pojo.Shop;
import com.ai.ecs.order.constant.OrderConstant;
import com.ai.ecs.order.entity.TfDeliveryMode;
import com.ai.ecs.order.entity.TfOrderDetailContract;
import com.ai.ecs.order.entity.TfOrderDetailSim;
import com.ai.ecs.order.entity.TfOrderReceiptTime;
import com.ai.ecs.order.entity.TfOrderRecommendContact;
import com.ai.ecs.order.entity.TfPayMode;
import com.ai.ecs.sales.entity.CouponInfo;
import com.google.common.collect.Lists;

import com.alibaba.fastjson.JSONObject;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

public class UserGoodsCarModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单备注
     */
    private String orderSubRemark;

    /**
     * 是否开发票，0：否，1：是
     */
    private Integer isInvoicing;

    /**
     * 发票抬头
     */
    private String invoicingTitle;

    /**
     * 支付金额
     */
    private Long paymentAmount;

    /**
     * 号卡明细
     */
    private TfOrderDetailSim orderDetailSim;

    /**
     * 合约明细
     */
    private TfOrderDetailContract orderDetailContract;

    /**
     * 收货地址
     */
    private MemberAddress memberAddress;

    /**
     * 配送方式
     */
    private TfDeliveryMode deliveryMode;

    /**
     * 自提网点
     */
    private String hallAddress;

    /**
     * 支付方式
     */
    private TfPayMode payMode;

    /**
     * 收货时间段
     */
    private TfOrderReceiptTime receiptTime;

    /**
     * 营销活动id
     */
    private Long marketId;

    /**
     * 订单推荐人
     */
    private TfOrderRecommendContact recommendContact;

    /**
     * 购物车
     */
    private List<TfUserGoodsCar> userGoodsCarList;

    private String payPlatform;//支付平台
    private String productName;//商品名称
    private String orderNos;//订单号
    private Set<Shop> shopSet;//店铺
    private List<Shop> shopList;//店铺集合，用于表单参数提交
    private List<Long> shopIdList;//店铺Id集合
    private List<CouponInfo> couponInfoList;//优惠券
    private Long rootCategoryId;//根类目ID
    private JSONObject params;//传值
    //== h5在线售卡默认信息设置
    public static UserGoodsCarModel getSimBaseInfo(UserGoodsCarModel carModel){
        List<TfUserGoodsCar> userGoodsCars = Lists.newArrayList();
        TfUserGoodsCar userGoodsCar = new TfUserGoodsCar();
        userGoodsCar.setShopId(1L);
        userGoodsCar.setShopName("湖南省移动公司");
        userGoodsCar.setShopTypeId(6);
        userGoodsCar.setGoodsName("号码");
        userGoodsCar.setGoodsBuyNum(1L);
        userGoodsCar.setIsChecked("Y");
        userGoodsCar.setCategoryId(2L);
        userGoodsCars.add(userGoodsCar);
        carModel.setUserGoodsCarList(userGoodsCars);
        MemberAddress address;
        if(OrderConstant.ONLI_RESU_ISSUC_O2O.equals(carModel.getOrderDetailSim().getSynOnliResuIssuc())){
            address = new MemberAddress();
            address.setMemberRecipientCounty("O2O号卡虚拟区县");
            address.setMemberRecipientCity("O2O号卡虚拟市");
            address.setMemberRecipientAddress("O2O号卡虚拟地址");

            TfDeliveryMode deliveryMode = new TfDeliveryMode();
            deliveryMode.setDeliveryModeId(3);
            carModel.setDeliveryMode(deliveryMode);
        }else{
            address = carModel.getMemberAddress();//收货地址只限于湖南省
            TfDeliveryMode deliveryMode = new TfDeliveryMode();
            deliveryMode.setDeliveryModeId(1);
            carModel.setDeliveryMode(deliveryMode);
        }
        address.setMemberRecipientProvince("湖南省");
        carModel.setMemberAddress(address);

        TfPayMode payMode = new TfPayMode();//支付方式
        payMode.setPayModeId(2);
        payMode.setPayModeName("在线支付");
        carModel.setPayMode(payMode);

        TfOrderDetailSim detailSim = carModel.getOrderDetailSim();
        detailSim.setRegType("1");  // 设置证件类型
        carModel.setOrderDetailSim(detailSim);
        return carModel;
    }

    public UserGoodsCarModel() {
    }

    public String getOrderSubRemark()
    {
        return orderSubRemark;
    }

    public void setOrderSubRemark(String orderSubRemark)
    {
        this.orderSubRemark = orderSubRemark;
    }

    public Integer getIsInvoicing()
    {
        return isInvoicing;
    }

    public void setIsInvoicing(Integer isInvoicing)
    {
        this.isInvoicing = isInvoicing;
    }

    public Long getPaymentAmount()
    {
        return paymentAmount;
    }

    public void setPaymentAmount(Long paymentAmount)
    {
        this.paymentAmount = paymentAmount;
    }

    public TfOrderDetailSim getOrderDetailSim()
    {
        return orderDetailSim;
    }

    public void setOrderDetailSim(TfOrderDetailSim orderDetailSim)
    {
        this.orderDetailSim = orderDetailSim;
    }

    public TfOrderDetailContract getOrderDetailContract()
    {
        return orderDetailContract;
    }

    public void setOrderDetailContract(TfOrderDetailContract orderDetailContract)
    {
        this.orderDetailContract = orderDetailContract;
    }

    public MemberAddress getMemberAddress()
    {
        return memberAddress;
    }

    public void setMemberAddress(MemberAddress memberAddress)
    {
        this.memberAddress = memberAddress;
    }

    public TfDeliveryMode getDeliveryMode()
    {
        return deliveryMode;
    }

    public void setDeliveryMode(TfDeliveryMode deliveryMode)
    {
        this.deliveryMode = deliveryMode;
    }

    public TfPayMode getPayMode()
    {
        return payMode;
    }

    public void setPayMode(TfPayMode payMode)
    {
        this.payMode = payMode;
    }

    public TfOrderReceiptTime getReceiptTime()
    {
        return receiptTime;
    }

    public void setReceiptTime(TfOrderReceiptTime receiptTime)
    {
        this.receiptTime = receiptTime;
    }

    public TfOrderRecommendContact getRecommendContact()
    {
        return recommendContact;
    }

    public void setRecommendContact(TfOrderRecommendContact recommendContact)
    {
        this.recommendContact = recommendContact;
    }

    public List<CouponInfo> getCouponInfoList()
    {
        return couponInfoList;
    }

    public void setCouponInfoList(List<CouponInfo> couponInfoList)
    {
        this.couponInfoList = couponInfoList;
    }

    public List<TfUserGoodsCar> getUserGoodsCarList()
    {
        return userGoodsCarList;
    }

    public void setUserGoodsCarList(List<TfUserGoodsCar> userGoodsCarList)
    {
        this.userGoodsCarList = userGoodsCarList;
    }

    public String getProductName()
    {
        return productName;
    }

    public void setProductName(String productName)
    {
        this.productName = productName;
    }

    public String getOrderNos()
    {
        return orderNos;
    }

    public void setOrderNos(String orderNos)
    {
        this.orderNos = orderNos;
    }

    public Set<Shop> getShopSet() {
        return shopSet;
    }

    public void setShopSet(Set<Shop> shopSet) {
        this.shopSet = shopSet;
    }

    public String getPayPlatform()
    {
        return payPlatform;
    }

    public void setPayPlatform(String payPlatform)
    {
        this.payPlatform = payPlatform;
    }

    public Long getRootCategoryId()
    {
        return rootCategoryId;
    }

    public void setRootCategoryId(Long rootCategoryId)
    {
        this.rootCategoryId = rootCategoryId;
    }

    public String getInvoicingTitle() {
        return invoicingTitle;
    }

    public void setInvoicingTitle(String invoicingTitle) {
        this.invoicingTitle = invoicingTitle;
    }

    public String getHallAddress() {
        return hallAddress;
    }

    public void setHallAddress(String hallAddress) {
        this.hallAddress = hallAddress;
    }

    public List<Shop> getShopList() {
        return shopList;
    }

    public void setShopList(List<Shop> shopList) {
        this.shopList = shopList;
    }

    public List<Long> getShopIdList() {
        return shopIdList;
    }

    public void setShopIdList(List<Long> shopIdList) {
        this.shopIdList = shopIdList;
    }
    public Long getMarketId() {
        return marketId;
    }

    public void setMarketId(Long marketId) {
        this.marketId = marketId;
    }

    public JSONObject getParams() {
        return params;
    }

    public void setParams(JSONObject params) {
        this.params = params;
    }
}
