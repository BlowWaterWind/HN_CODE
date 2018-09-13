package com.ai.ecs.ecm.mall.wap.platform.constants;

import org.apache.commons.lang3.ObjectUtils;

import com.ai.ecs.member.constant.CodeConstant;

public enum OrderStatusConstant implements CodeConstant {

    NULL(-1, ""), WAITGHSENTER(21, "待供货商确认订单"), DELETEORDER(32, "订单删除"), CANCELORDER(18, "已取消"),
    WAITQDSENTER(22, "待渠道商确认订单"), WAITPAY(1, "待支付"), WAITSENDGOODS(6, "待发货"), WAITRECEIVEGOODS(7, "待收货"),
    THH(27, "退换货"), WAITCOMMENT(8, "待评价"), FINISHED(12, "已归档");
    public static OrderStatusConstant getByValue(Integer value) {
        OrderStatusConstant[] values = OrderStatusConstant.values();
        for (OrderStatusConstant result : values) {
            if (ObjectUtils.equals(result.value, value)) {
                return result;
            }
        }
        return NULL;
    }

    private Integer value;
    private String description;

    private OrderStatusConstant(Integer value, String description) {
        this.value = value;
        this.description = description;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
