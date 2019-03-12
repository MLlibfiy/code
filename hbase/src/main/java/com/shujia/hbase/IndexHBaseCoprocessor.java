package com.shujia.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.coprocessor.BaseRegionObserver;
import org.apache.hadoop.hbase.coprocessor.ObserverContext;
import org.apache.hadoop.hbase.coprocessor.RegionCoprocessorEnvironment;
import org.apache.hadoop.hbase.regionserver.wal.WALEdit;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.List;

/**
 * 协处理器，运行在region server 上
 *
 */
public class IndexHBaseCoprocessor extends BaseRegionObserver {

    static HTableInterface studentIndex;

    static {
        //创建表的连接

        //在集群里面运行可以使用这种方式创建配置文件对象
        Configuration conf = HBaseConfiguration.create();

        try {
            HConnection connection = HConnectionManager.createConnection(conf);
            studentIndex = connection.getTable("student_index");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * put之前执行
     */
    @Override
    public void prePut(ObserverContext<RegionCoprocessorEnvironment> e, Put put, WALEdit edit, Durability durability) throws IOException {

        try {

        //往索引表查询索引

        /**
         * 对班级和性别建立索引
         *
         */

        String row = Bytes.toString(put.getRow());


        //多个版本
        List<Cell> cells = put.get("info".getBytes(), "clazz".getBytes());
        Cell cell = cells.get(0);
        String clazz = Bytes.toString(CellUtil.cloneValue(cell));


        List<Cell> cells1 = put.get("info".getBytes(), "gender".getBytes());
        Cell cell1 = cells1.get(0);
        String gender = Bytes.toString(CellUtil.cloneValue(cell1));


        if (!"".equals(clazz) && !"".equals(gender)) {
            //构建索引表的rowkey
            String indexRowkey = clazz + "_" + gender + "_" + row;
            Put put1 = new Put(indexRowkey.getBytes());

            put1.add("f".getBytes(),"c".getBytes(),"|".getBytes());

            studentIndex.put(put1);
        }

        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
}
