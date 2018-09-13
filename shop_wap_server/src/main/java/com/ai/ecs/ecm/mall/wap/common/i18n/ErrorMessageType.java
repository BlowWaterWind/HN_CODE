package com.ai.ecs.ecm.mall.wap.common.i18n;

/*****
 * <pre>
 * 类名称：MessageType
 * 类描述：错误消息类型定义
 * 创建人：JokenWang
 * 创建时间：2015年10月1日 下午2:19:32
 * </pre>
 * 
 * @version 1.0.0
 */
public enum ErrorMessageType
{
    /** ERROR_500=系统内部错误 **/
    SYSTEM_INNER_ERROR("500"),
    /** ERROR_400= 缺少必需业务参数 **/
    SYSTEM_MISSING_MUST_PARAMETERS("400"),
    
    /** ERROR_100=商品不存在或已下架，请选购其它商品 **/
    SHOPPING_CART_GOODS_NOT_EXIST("100"),
    /** ERROR_101=只有裸机和配件才能加入购物车 **/
    SHOPPING_CART_GOODS_NOT_ADD("101"),
    /** ERROR_102=商品库存不足 **/
    SHOPPING_CART_GOODS_NUM_NOT_ENOUGH("102"),
    /** ERROR_103=购物车空空如也，赶紧去选购吧或者登录查看您的购物车 **/
    SHOPPING_CART_GOODS_EMPTY("103"),
    /** ERROR_104=商品数量不能小于0 **/
    SHOPPING_CART_GOODS_NUM_NOT_LT_0("104"),
    /** ERROR_105=您要关注的商品已不存在 **/
    SHOPPING_CART_GOODS_ATTENTION_NOT_EXIST("105"),
    /** ERROR_401=生成订单出现异常，请稍后重试 **/
    ORDER_GENERATE_ERROR("401"),
    /** ERROR_402=订单签名数据异常 **/
    ORDER_SIGN_ERROR("402"),
    /** ERROR_403=合约校验失败，您暂时无法下单 **/
    ORDER_AGREE_CHECK_ERROR("403"),
    /** ERROR_404=充值金额不能小于10 **/
    ORDER_RECHARGE_MUST_GT_0("404"),
    /** ERROR_405=必须是江西移动手机卡用户，才可充值 **/
    ORDER_RECHARGE_MUST_CMC_PHONE_CARD("405"),
    /** ERROR_406=手机充值费率最多一种 **/
    ORDER_RECHARGE_FEE_DISCOUNT_ONLY_ONE("406"),
    /** ERROR_407=充值金额校验失败 **/
    ORDER_RECHARGE_FEE_VALIDATE_FAILED("407"),
    /** ERROR_408=充值金额生成订单失败 **/
    ORDER_RECHARGE_FEE_ORDER_FAILED("408"),
    /** ERROR_409=资费变更或流量包生成订单失败 **/
    ORDER_RECHARGE_PROD_ORDER_FAILED("409"),
    /** ERROR_410=所在地市没有此套餐！ **/
    ORDER_FEE_CHANGE_AREA_NO_PROD("410"),
    /** ERROR_411=该地市套餐配置互斥！ **/ 
    ORDER_FEE_CHANGE_HAS_DUBLE_PROD("411"),
    /** ERROR_412=充值失败，号码未注册！ **/
    ORDER_RECHARGE_NO_REGIST_PHONE("412"),
    /** ERROR_413=必须是江西移动手机卡用户，才可办理 **/
    ORDER_MUST_CMC_PHONE_CARD("413"),
    /** ERROR_414=赠送金额错误 **/
    ORDER_SEND_FEE_ERROR("414"),
    /** ERROR_415=充值失败，获取不到账户 **/
    ORDER_GET_ACCOUNT_ERROR("415"),
    /** ERROR_416=充值失败，获取账户异常 **/
    ORDER_GET_ACCOUNT_EXCEPTION("416"),
    /** ERROR_417=充值金额不对 **/
    ORDER_RECHARGE_FEE_ERROR("417"),
    /** ERROR_418=目前暂不支持多商家合并支付 **/
    ORDER_NOT_SUPPORT_MUTI_SHOPPER_PAY("418"),
    /** ERROR_419=请选择到店自提或设置收货地址 **/
    ORDER_BRANCHID_RECEIVEID_NOT_BOTH_BLANK("419"),
    /** ERROR_420=充值金额必须大于5000元 **/
    ORDER_RECHARGE_MUST_GT_5000("420"),
    /** ERROR_421=营销商品不存在或活动已结束 **/
    ORDER_SALES_ORDER_GOODS_NOT_EXIST("421"),
    /** ERROR_501=商家还没设置收款账户，暂时不能付款 **/
    ORDER_PAY_SHOP_ACCOUNT_NOT_SETTING("501"),
    /** ERROR_502=订单支付异常 **/
    ORDER_PAY_ERROR("502"),
    
    /** 尊敬的用户，短信验证码发送失败，请稍后再试！ */
    USER_SMS_CODE_ERROR("201"),
    /** 发送的手机号码与验证的手机号码不一致！ */
    USER_SMS_CODE_MOOBLE_ERROR("202"),
    
    /** 尊敬的用户，此手机号码已注册！ */
    USER_REGISTER_MOBILE_ERROR("301"),
    /** 密码不能为空！ */
    USER_REGISTER_PASS_ERROR("302"),
    /** 验证码错误 */
    USER_REGISTER_CODE_ERROR("303"),
    /** ERROR_601=您要关注的店铺信息已不存在 **/
    SHOP_ATTENTION_NOT_EXIST("601"),
    /** 注册失败，请稍后再试！ */
    USER_REGISTER_ERROR("304");
    private String code;
    
    private ErrorMessageType(final String code)
    {
        this.code = code;
    }
    
    public String getCode()
    {
        return this.code;
    }
}
