package com.shujia.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.HFileOutputFormat;
import org.apache.hadoop.hbase.mapreduce.KeyValueSortReducer;
import org.apache.hadoop.hbase.mapreduce.SimpleTotalOrderPartitioner;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class HdfsToHFile {

    public static class HdfsToHHFileMap extends Mapper<LongWritable, Text, ImmutableBytesWritable, KeyValue> {
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] split = value.toString().split("\t");
            ImmutableBytesWritable rowkey = new ImmutableBytesWritable(split[0].getBytes());

            KeyValue name = new KeyValue(split[0].getBytes(), "info".getBytes(), "name".getBytes(), System.currentTimeMillis(), split[1].getBytes());
            context.write(rowkey, name);

            KeyValue age = new KeyValue(split[0].getBytes(), "info".getBytes(), "age".getBytes(), System.currentTimeMillis(), Bytes.toBytes(Integer.parseInt(split[2])));
            context.write(rowkey, age);

            KeyValue gender = new KeyValue(split[0].getBytes(), "info".getBytes(), "gender".getBytes(), System.currentTimeMillis(), split[3].getBytes());
            context.write(rowkey, gender);

            KeyValue clazz = new KeyValue(split[0].getBytes(), "info".getBytes(), "clazz".getBytes(), System.currentTimeMillis(), split[4].getBytes());
            context.write(rowkey, clazz);


        }
    }


    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        conf.set("hbase.zookeeper.quorum", "node1:2181,node2:2181,node3:2181");
        Job job = Job.getInstance(conf);

        job.setOutputKeyClass(ImmutableBytesWritable.class);
        job.setOutputValueClass(KeyValue.class);

        job.setNumReduceTasks(10);
        job.setJarByClass(HdfsToHFile.class);
        job.setJobName("HdfsToHHFile");

        //hbase提供的用来对rowkey排序的reduce  字典顺序
        job.setReducerClass(KeyValueSortReducer.class);

        job.setPartitionerClass(SimpleTotalOrderPartitioner.class);

        job.setMapperClass(HdfsToHHFileMap.class);

        HFileOutputFormat.configureIncrementalLoad(job, new HTable(conf, "student"));


        FileInputFormat.addInputPath(job, new Path("/data/out/student/"));
        FileOutputFormat.setOutputPath(job, new Path("/data/hfile/student/"));

        job.waitForCompletion(true);

    }

}
