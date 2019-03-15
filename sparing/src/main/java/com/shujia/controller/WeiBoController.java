package com.shujia.controller;

import com.shujia.bean.WeiBo;
import com.shujia.service.WeiBoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WeiBoController {

    @Autowired
    private WeiBoService weiBoService;


    @RequestMapping("/getWeibo")
    public WeiBo getWeibo(String id){

       return weiBoService.findById(id);
    }

}
