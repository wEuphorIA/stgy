package com.atguigu.lease.common.exception;

import com.atguigu.lease.common.result.ResultCodeEnum;
import lombok.Data;

/**
 @author Euphoria
 @version 1.0
 @description: TODO
 @date 2025/8/25 下午9:05 */
@Data
public class LeaseException extends RuntimeException {

    private Integer code;

    public LeaseException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public LeaseException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
    }
}
