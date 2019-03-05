package com.shujia.util;
import org.springframework.util.DigestUtils;
public class Md5Util {

    /**
     * md5加密工具
     */
    public static String md5(String str){
        return  DigestUtils.md5DigestAsHex(str.getBytes()).toUpperCase();
    }
}
