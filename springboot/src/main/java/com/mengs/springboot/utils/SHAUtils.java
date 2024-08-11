package com.mengs.springboot.utils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * SHA 加密算法
 */
public class SHAUtils {


    /**
     * 加密成字节数组
     */
    public static byte[] encrypt(String s) {
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA");
            return sha.digest(s.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 加密成大写字符串
     */
    public static String encryptToUpperString(String s) {
        return encryptToString(s, "%02X");
    }

    /**
     * 加密成小写字符串
     */
    public static String encryptToLowerString(String s) {
        return encryptToString(s, "%02x");
    }

    /**
     * 加密成字符串
     */
    private static String encryptToString(String s, String format) {
        byte[] digest = encrypt(s);
        StringBuilder build = new StringBuilder();
        for (int i = 0; i < digest.length; i++) {
            build.append(String.format(format, digest[i]));
        }

        return build.toString();
    }



    public static String pdfFileSHA(String fileUUID, String filePath) throws IOException {
        FileInputStream in = new FileInputStream(filePath + fileUUID);

        BufferedInputStream bis = new BufferedInputStream(in);

        byte[] data = new byte[in.available()];
        bis.read(data);

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            stringBuilder.append(data[i]);
        }

        in.close();
        bis.close();

        return SHAUtils.encryptToLowerString(stringBuilder.toString());
    }


}
