package com.shujia.hbase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class Testjunit {

    @Before
    public void init(){
        System.out.println("Before");
    }

    @Test
    public void test(){
        System.out.println("junit");
    }

    @Test
    public void test1(){
        System.out.println("junit1");
    }

    @After
    public void close(){
        System.out.println("close");
    }
}
