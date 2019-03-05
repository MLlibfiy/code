package com.shujia;

import org.springframework.util.DigestUtils;

public class Test {
    public static void main(String[] args) {
        String s = DigestUtils.md5DigestAsHex("1234".getBytes());
        System.out.println(s);
    }
}