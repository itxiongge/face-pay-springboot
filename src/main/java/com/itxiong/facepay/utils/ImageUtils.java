package com.itxiong.facepay.utils;

import org.apache.commons.io.FileUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;

public class ImageUtils {
    /**
     * base64编码图片数据生成图片
     */
    public static boolean generateImage(String imgStr, String filePath, String fileName) {
        try {
            if (imgStr == null) {
                return false;
            }
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] b = decoder.decodeBuffer(imgStr);
            File file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            OutputStream out = new FileOutputStream(filePath+fileName);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    //图片base64编码
    public static String imageBase64(String filePath) {
        try {
            if (filePath == null) {
                return null;
            }
            BASE64Encoder encoder = new BASE64Encoder();
            byte[] bytes = FileUtils.readFileToByteArray(new File(filePath));
            String imgStr = encoder.encode(bytes);//加密
            return imgStr;
        } catch (Exception e) {
            return null;
        }
    }
}
