package com.caijia.social.net;

import java.security.MessageDigest;

/**
 * MD5加密
 */
public class MD5 {
    private static final String ALGORITHM_MD5 = "MD5";

    public static String getMD5(String pass) {
        String keys = null;
        try {
            MessageDigest md = MessageDigest.getInstance(ALGORITHM_MD5);
            if (pass == null) {
                pass = "";
            }
            byte[] bPass = pass.getBytes("UTF-8");
            md.update(bPass);
            keys = bytesToHexString(md.digest());
        } catch (Exception aex) {
        }
        return keys;
    }

    private static String bytesToHexString(byte[] bArray) {
        StringBuilder sb = new StringBuilder(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2) {
                sb.append(0);
            }
            sb.append(sTemp);
        }
        return sb.toString().toUpperCase();
    }
}
