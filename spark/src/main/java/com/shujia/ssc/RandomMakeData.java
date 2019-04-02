package com.shujia.ssc;

import java.io.BufferedReader;
import java.io.FileReader;

public class RandomMakeData {
    public static void main(String[] args) throws Exception {

        /**
         * 生成数据，每200毫秒打印一条
         *
         */

        FileReader fileReader = new FileReader("/tmp/flume/dianxin_data");
        BufferedReader reader = new BufferedReader(fileReader);

        String line;
        while ((line = reader.readLine()) != null) {

            //每200毫秒打印一次

            System.out.println(line);

            Thread.sleep(200);


        }


    }
}
