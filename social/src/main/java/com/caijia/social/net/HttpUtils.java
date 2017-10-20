package com.caijia.social.net;

import android.content.Context;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by cai.jia on 2017/6/9 0009
 */

public class HttpUtils {

    public static String getUrlWithQueryString(String url, Map<String, Object> paramsMap) {
        if (paramsMap != null) {
            String paramString = getParamString(paramsMap);
            if (!url.contains("?")) {
                url += "?" + paramString;
            } else {
                url += "&" + paramString;
            }
        }
        return url;
    }

    public static String getParamString(Map<String, Object> paramsMap) {
        StringBuilder params = new StringBuilder();
        for (Map.Entry<String, Object> entry : paramsMap.entrySet()) {
            if (params.length() > 0) {
                params.append("&");
            }

            params.append(entry.getKey());
            params.append("=");
            try {
                params.append(URLEncoder.encode(String.valueOf(entry.getValue()), "UTF-8"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return params.toString();
    }

    public static String streamToString(InputStream inputStream) {
        ByteArrayOutputStream out = null;
        BufferedInputStream in = null;
        try {
            out = new ByteArrayOutputStream();
            in = new BufferedInputStream(inputStream);
            int len;
            byte[] buffer = new byte[1024 * 8];
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
        } catch (Exception e) {

        } finally {
            try {
                if (out != null) {
                    out.close();
                }

                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (out != null) {
            return out.toString();
        }
        return null;
    }

    public static String streamToFile(Context context,String fileName, InputStream inputStream) {
        if (context == null) {
            return "";
        }

        File cacheDir = context.getCacheDir();
        File saveFile = new File(cacheDir, fileName);
        BufferedOutputStream out = null;
        BufferedInputStream in = null;
        try {
            out = new BufferedOutputStream(new FileOutputStream(saveFile));
            in = new BufferedInputStream(inputStream);
            int len ;
            byte[] buffer = new byte[1024 * 8];
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
        } catch (Exception e) {
            return "";
        }finally {
            try {
                if (out != null) {
                    out.close();
                }

                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return saveFile.getAbsolutePath();
    }

    public static File getCacheImage(Context context, String url) {
        File cacheDir = context.getCacheDir();
        String fileName = MD5.getMD5(url) + ".jpg";
        return new File(cacheDir, fileName);
    }
}
