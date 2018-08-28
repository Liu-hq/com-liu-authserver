package com.liu.authserver.utils;

import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/8/27.
 */
public class ReturnJson {

    public static Map jsonData(boolean result, Object data, int errorCode, String msg) {
        Map json = new HashMap();
        json.put("result", result);
        json.put("data", data);
        json.put("errcode", errorCode);
        json.put("msg", msg);
        return json;
    }
}
