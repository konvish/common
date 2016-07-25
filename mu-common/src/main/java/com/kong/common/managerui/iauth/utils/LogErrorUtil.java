package com.kong.common.managerui.iauth.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by Administrator on 2016/1/3.
 */
public class LogErrorUtil {
    public LogErrorUtil() {
    }

    public static String toString(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        String var3;
        try {
            t.printStackTrace(pw);
            var3 = sw.toString();
        } finally {
            pw.close();
        }

        return var3;
    }
}