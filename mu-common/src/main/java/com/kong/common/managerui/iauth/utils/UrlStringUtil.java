package com.kong.common.managerui.iauth.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Administrator on 2016/1/3.
 */
public class UrlStringUtil {
    public UrlStringUtil() {
    }

    public static String paramsMapToURLString(Map<String, String> params) throws UnsupportedEncodingException {
        if(params != null && !params.isEmpty()) {
            StringBuilder ex = new StringBuilder("?");
            Iterator i$ = params.entrySet().iterator();

            while(i$.hasNext()) {
                Map.Entry entry = (Map.Entry)i$.next();
                ex.append((String)entry.getKey() + "=" + URLEncoder.encode((String) entry.getValue(), "utf-8"));
                ex.append("&");
            }

            ex.delete(ex.length() - 1, ex.length());
            return ex.toString();
        } else {
            return "";
        }
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        HashMap map = new HashMap();
        map.put("username", "gbdai");
        map.put("passwd", "passwd");
        map.put("error", "中文支持");
        System.out.println(paramsMapToURLString(map));
    }
}
