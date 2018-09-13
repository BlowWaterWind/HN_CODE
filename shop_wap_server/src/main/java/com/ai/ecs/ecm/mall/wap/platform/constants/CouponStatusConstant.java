package com.ai.ecs.ecm.mall.wap.platform.constants;

import org.apache.commons.lang3.ObjectUtils;

import com.ai.ecs.member.constant.CodeConstant;

public enum CouponStatusConstant implements CodeConstant {

    NULL(-1, ""), NORMAL(9, "正常"), USED(10, "已使用"), OVERTIME(11, "已过期");

    public static CouponStatusConstant getByValue(Integer value) {
        CouponStatusConstant[] values = CouponStatusConstant.values();
        for (CouponStatusConstant result : values) {
            if (ObjectUtils.equals(result.value, value)) {
                return result;
            }
        }
        return NULL;
    }

    private Integer value;
    private String description;

    private CouponStatusConstant(Integer value, String description) {
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
