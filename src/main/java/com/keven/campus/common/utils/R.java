package com.keven.campus.common.utils;

import com.keven.campus.common.utils.enums.ResultCode;
import org.apache.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回数据
 *
 * @author Keven
 * @version 1.0
 */
public class R extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    protected int code;
    protected String msg;
    protected Map<String, Object> data;

    public R() {
        put("code", 200);
        put("msg", "success");
    }

    private R(ResultCode resultCode) {
        this.code = resultCode.getCode();
        this.msg = resultCode.getMsg();
        put("code", code);
        put("msg", msg);

    }

    public static R error() {
        return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, "未知异常，请联系管理员");
    }


    public static R error(String msg) {
        return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, msg);
    }

    public static R error(int code, String msg) {
        R r = new R();
        r.put("code", code);
        r.put("msg", msg);
        return r;
    }

    public static R error(ResultCode resultCode) {
        return new R(resultCode);
    }

    public static R ok(String msg) {
        R r = new R();
        r.put("msg", msg);
        return r;
    }

    public static R ok(Map<String, Object> map) {
        R r = new R();
        r.putAll(map);
        return r;
    }

    public static R ok() {
        return new R();
    }

    @Override
    public R put(String key, Object value) {
        super.put(key, value);
        return this;
    }

    public R put(Object value) {
        super.put("data", value);
        return this;
    }
}