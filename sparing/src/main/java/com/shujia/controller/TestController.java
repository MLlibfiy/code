package com.shujia.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 注解
 * 对这个类的一个标记
 */
@RestController
public class TestController {


    /**
     * 如果只有一个参数value 可以不写
     * method 默认就是get
     *
     * @return
     */
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test() {


        /**
         * 访问数据库，将查询的结果返回给页面
         */


        return "test";
    }
}
