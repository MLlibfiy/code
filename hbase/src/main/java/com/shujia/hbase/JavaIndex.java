package com.shujia.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.BinaryPrefixComparator;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

public class JavaIndex {
    HBaseAdmin hBaseAdmin;
    HConnection connection;

    @Before
    public void init() {
        Configuration conf = new Configuration();
        conf.set("hbase.zookeeper.quorum", "node1:2181,node2:2181,node3:2181");


        try {
            //连接Master 创建hbase操作对象，主要用来创建，修改，删除表
            hBaseAdmin = new HBaseAdmin(conf);

//           //丽娜姐regionserver,  负责表的增删改查
            connection = HConnectionManager.createConnection(conf);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 查询数据之前先查询索引表
     */
    @Test
    public void queryIndex() {
        try {
            //数据表
            HTableInterface student = connection.getTable("student");
            //索引表
            HTableInterface studentIndex = connection.getTable("student_index");

            /**
             * 查询文科一班的所有学生
             *
             */

            Scan scan = new Scan();
            /**
             * rowkey前缀过滤
             *
             * 不会做全表扫描
             *
             */
            BinaryPrefixComparator binaryPrefixComparator = new BinaryPrefixComparator("文科一班".getBytes());
            RowFilter rowFilter = new RowFilter(CompareFilter.CompareOp.EQUAL, binaryPrefixComparator);
            scan.setFilter(rowFilter);

            ResultScanner scanner = studentIndex.getScanner(scan);


            //构建gets
            ArrayList<Get> gets = new ArrayList<>();

            Result result;
            while ((result = scanner.next()) != null) {
                String rowkey = Bytes.toString(result.getRow());
                String stuId = rowkey.split("_")[1];//学号

                Get get = new Get(stuId.getBytes());
                gets.add(get);

            }


            //通过查询回来的学号去数据表里面查询数据
            Result[] results = student.get(gets);

            //表里结果
            for (Result result1 : results) {
                //获取rowkey
                String id = Bytes.toString(result1.getRow());
                String name = Bytes.toString(result1.getValue("info".getBytes(), "name".getBytes()));

                int age = Bytes.toInt(result1.getValue("info".getBytes(), "age".getBytes()));

                String gendet = Bytes.toString(result1.getValue("info".getBytes(), "gender".getBytes()));
                String clazz = Bytes.toString(result1.getValue("info".getBytes(), "clazz".getBytes()));
                System.out.println(id + "\t" + name + "\t" + age + "\t" + gendet + "\t" + clazz);
            }



        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     *
     * 插入数据之前先插入索引表
     *
     */

    @Test
    public void insertIndex(){

        try {
            //数据表
            HTableInterface student = connection.getTable("student");
            //索引表
            HTableInterface studentIndex = connection.getTable("student_index");


            //建立索引
            String indexKey = "文科一班_1500101001";
            Put indexPut = new Put(indexKey.getBytes());
            indexPut.add("f".getBytes(),"c".getBytes(),"|".getBytes());
            studentIndex.put(indexPut);


            //创建put对象，指定rowkey
            Put put = new Put("1500101001".getBytes());

            //插入一列
            put.add("info".getBytes(), "name".getBytes(), "张三".getBytes());

            //Bytes.toBytes   将基本数据类型转成字节数组
            put.add("info".getBytes(), "age".getBytes(), Bytes.toBytes(12));

            put.add("info".getBytes(), "gender".getBytes(), "男".getBytes());

            put.add("info".getBytes(), "clazz".getBytes(), "文科一班".getBytes());

            student.put(put);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @After
    public void close() {
        //关闭连接
        try {
            hBaseAdmin.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
