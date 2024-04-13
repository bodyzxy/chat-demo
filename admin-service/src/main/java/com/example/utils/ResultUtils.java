package com.example.utils;

import com.example.common.BaseResponse;
import com.example.common.ErrorCode;

public class ResultUtils {

    /**
     * 成功
     * @param data
     * @return
     * @param <T>
     */
    public static <T>BaseResponse<T> success(T data) {return new BaseResponse<>(0,data,"ok");}

    /**
     * 失败
     * @param errorCode
     * @return
     */
    public static BaseResponse error(ErrorCode errorCode){
        return new BaseResponse<>(errorCode);
    }

    /**
     * 失败
     * @param errorCode
     * @param msg
     * @return
     */
    public static BaseResponse error(ErrorCode errorCode,String msg){
        return new BaseResponse<>(errorCode.getCode(),null,msg);
    }

    /**
     * 失败
     * @param code
     * @param msg
     * @return
     */
    public static BaseResponse error(int code,String msg){
        return new BaseResponse(code,null,msg);
    }

}
