package com.kong.dap.dataservice;

import java.io.IOException;
import java.io.InputStream;

public class Utils {

    public static String inputStream2Str(InputStream in) {
        StringBuffer sb = new StringBuffer();
        if (in != null) {
            byte[] b = new byte[4096];
            try {
                for (int n; (n = in.read(b)) != -1; ) {
                    sb.append(new String(b, 0, n));
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }


    public static boolean isEmpty(String str) {
        boolean result = false;
        if (str == null || "".endsWith(str)) {
            result = true;
        }
        return result;
    }


    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

