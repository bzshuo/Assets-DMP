package com.mengs.springboot.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 *  字段使用参考如下
 *  code：状态码
 *  msg：页面展示信息
 *  data：返回数据 / 业务异常信息
 *  busId：业务ID（查日志用）
 *
 *
 * @author ：ms(YX)
 * @description：通用返回
 * @date ：Born in 2019/10/9 20:19
 */

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataResponse<T> extends BaseEntity implements Serializable {
    private String code;
    private String msg;
    private T data;

    public DataResponse() {
    }

    public DataResponse(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public DataResponse(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public DataResponse(String code, String msg, String busId) {
        this.code = code;
        this.msg = msg;
        this.busId = busId;
    }

    public DataResponse(String code, String msg, String busId, T data) {
        this.code = code;
        this.msg = msg;
        this.busId = busId;
        this.data = data;
    }

    public static DataResponse ok() {
        return new DataResponse("0000", "ok");
    }

    public static DataResponse ok(String msg) {
        return new DataResponse("0000", msg);
    }

    public static DataResponse ok(String msg, String busId) {
        return new DataResponse("0000", msg, busId);
    }

    public static <T> DataResponse<T> ok(T data) {
        return new DataResponse("0000", "ok", data);
    }

    public static <T> DataResponse<T> ok(String busId, T data) {
        return new DataResponse("0000", "ok", busId, data);
    }

    public static DataResponse error() {
        return new DataResponse("9999", "error");
    }

    public static DataResponse error(String msg) {
        return new DataResponse("9999", msg);
    }

    public static DataResponse error(String code, String msg) {
        return new DataResponse(code, msg);
    }

    public static DataResponse error(String code, String msg, String busId) {
        return new DataResponse(code, msg, busId);
    }

    public static DataResponse error(String code, String msg, String busId, String desc) {
        return new DataResponse(code, msg, busId, desc);
    }
}
