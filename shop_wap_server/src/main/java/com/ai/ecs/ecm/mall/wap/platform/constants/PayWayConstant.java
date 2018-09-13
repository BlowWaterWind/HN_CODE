package com.ai.ecs.ecm.mall.wap.platform.constants;

import org.apache.commons.lang3.ObjectUtils;

import com.ai.ecs.member.constant.CodeConstant;

public enum PayWayConstant implements CodeConstant {

    NULL(-1, ""), HDFK(1, "货到付款"), ZXZF(2, "在线支付"), DTZT(3, "到厅自提"),
    BXZF(4, "不需支付");

    public static PayWayConstant getByValue(Integer value) {
        PayWayConstant[] values = PayWayConstant.values();
        for (PayWayConstant result : values) {
            if (ObjectUtils.equals(result.value, value)) {
                return result;
            }
        }
        return NULL;
    }

    private Integer value;
    private String description;

    private PayWayConstant(Integer value, String description) {
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
