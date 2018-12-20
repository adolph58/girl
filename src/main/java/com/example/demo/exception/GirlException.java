package com.example.demo.exception;

import com.example.demo.enums.ResultEnum;

/**
 * Created by 程江涛 on 2018/3/13 0013
 */
public class GirlException extends RuntimeException{

    private Integer code;

    public GirlException(ResultEnum resultEnum) {
        super(resultEnum.getMsg());
        this.code = resultEnum.getCode();
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
