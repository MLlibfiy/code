package com.shujia.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;

import java.io.IOException;

/**
 * 二级索引（空间换时间）  mapreduce 实现
 * 1、数据表的数据量已经很大，无法瞒住业务的查询需求，可以使用mapreduced对
 * 常用的列建立索引
 */
public class HbaseIndex {

    /**
     * 读取hbase数据的map
     */
    public static class HbaseIndexMap extends TableMapper<Text, NullWritable> {
        @Override
        protected void map(ImmutableBytesWritable key, Result value, Context context) throws IOException, InterruptedException {

            String id = Bytes.toString(key.get());
            //Result  一行数据
            String name = Bytes.toString(value.getValue("info".getBytes(), "name".getBytes()));
            int age = Bytes.toInt(value.getValue("info".getBytes(), "age".getBytes()));
            String gender = Bytes.toString(value.getValue("info".getBytes(), "gender".getBytes()));
            String clazz = Bytes.toString(value.getValue("info".getBytes(), "clazz".getBytes()));

            String stu = clazz + "_" + id;

            context.write(new Text(stu), NullWritable.get());

        }
    }

    /**
     * 数据写入hbase reduce
     */

    public static class HbaseIndexReduce extends TableReducer<Text, NullWritable, NullWritable> {
        @Override
        protected void reduce(Text key, Iterable<NullWritable> values, Context context) throws IOException, InterruptedException {
            String s = key.toString();

            Put put = new Put(s.getBytes());

            //不存数据，只需要rowkey
            put.add("f".getBytes(), "c".getBytes(), "|".getBytes());

            context.write(NullWritable.get(),put);

        }
    }

    public static void main(String[] args) throws Exception {

        Configuration conf = new Configuration();
        conf.set("hbase.zookeeper.quorum", "node1:2181,node2:2181,node3:2181");
        Job job = Job.getInstance(conf);

        job.setNumReduceTasks(10);
        job.setJarByClass(HbaseIndex.class);
        job.setJobName("HbaseIndex");

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(NullWritable.class);

        Scan scan = new Scan();
        scan.addFamily("info".getBytes());

        //初始化map 类
        TableMapReduceUtil.initTableMapperJob("student", scan, HbaseIndexMap.class, Text.class, NullWritable.class, job);

        //指定reduce
        TableMapReduceUtil.initTableReducerJob("student_index", HbaseIndexReduce.class, job, null, null, null, null, false);

        job.setOutputKeyClass(NullWritable.class);
        job.setOutputValueClass(Put.class);

        boolean b = job.waitForCompletion(true);
    }

}
