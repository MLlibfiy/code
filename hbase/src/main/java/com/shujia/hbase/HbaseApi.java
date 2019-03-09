package com.shujia.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class HbaseApi {

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

    @Test
    public void createTable() {
        //创建表得描述对象
        HTableDescriptor student = new HTableDescriptor("student");

        //创建列蹴得描述对象
        HColumnDescriptor info = new HColumnDescriptor("info");
        student.addFamily(info);
        //创建表
        try {
            hBaseAdmin.createTable(student);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void dropTable() {
        try {

            if (hBaseAdmin.tableExists("student")) {
                System.out.println("表存在");
            }

            //1、让表失效
            hBaseAdmin.disableTable("student");
            //hBaseAdmin.enableTable("student");//让表生效
            //2、删除表
            hBaseAdmin.deleteTable("student");


            if (!hBaseAdmin.tableExists("student")) {
                System.out.println("表不存在");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 插入一行数据
     */
    @Test
    public void insert() {
        try {
            //获取表的实例对象
            HTableInterface student = connection.getTable("student");

            //创建put对象，指定rowkey
            Put put = new Put("001".getBytes());

            //插入一列
            put.add("info".getBytes(), "name".getBytes(), "张三".getBytes());

            //Bytes.toBytes   将基本数据类型转成字节数组
            put.add("info".getBytes(), "age".getBytes(), Bytes.toBytes(12));

            put.add("info".getBytes(), "gender".getBytes(), "男".getBytes());

            student.put(put);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void query() {
        //获取表的实例对象
        try {
            HTableInterface student = connection.getTable("student");
            //创建gei对象
            Get get = new Get("001".getBytes());
            get.addFamily("info".getBytes());

            //查询数据
            Result result = student.get(get);

            //通过列蔟加上列获取数据，返回一个字节数组
            String name = Bytes.toString(result.getValue("info".getBytes(), "name".getBytes()));
            int age = Bytes.toInt(result.getValue("info".getBytes(), "age".getBytes()));
            String gender = Bytes.toString(result.getValue("info".getBytes(), "gender".getBytes()));

            System.out.println(name + "\t" + age + "\t" + gender);


            //获取所有单元格
            List<Cell> cells = result.listCells();

            for (Cell cell : cells) {

                String family = Bytes.toString(CellUtil.cloneFamily(cell));
                System.out.println(family);
                //获取单元格列的名称
                String qualifier = Bytes.toString(CellUtil.cloneQualifier(cell));

                if ("age".equals(qualifier)) {
                    int a = Bytes.toInt(CellUtil.cloneValue(cell));
                    System.out.println(a);
                } else {
                    String s = Bytes.toString(CellUtil.cloneValue(cell));
                    System.out.println(s);
                }
            }

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
