package com.ai.ecs.ecm.mall.wap.platform.utils;

/**
 * ecs-ecm-project
 * <p>
 *
 * @author fengrh 2016/12/22  16:44
 * @see
 * @since 1.0
 * 修改人：fengrh 修改时间：2016/12/22  16:44 修改备注：
 */
public class ResultCode {
    private String code;

    private String message;

    public ResultCode(String code, String message) {

        this.code = code;
        this.message = message;
    }

    public String getCode() {

        return code;
    }

    public void setCode(String code) {

        this.code = code;
    }

    public String getMessage() {

        return message;
    }

    public void setMessage(String message) {

        this.message = message;
    }
}
