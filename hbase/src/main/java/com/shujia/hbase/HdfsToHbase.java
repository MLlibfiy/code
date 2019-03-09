package com.shujia.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;

import java.io.IOException;


/**
 * hdfs 数据导入hbase    批量导入，可以设置对各reduce 提高导入的并行度
 */
public class HdfsToHbase {


    public static class ToHbaseMap extends Mapper<LongWritable, Text, Text, NullWritable> {
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            context.write(value, NullWritable.get());
        }
    }

    public static class HDFSToHbaseReducer extends TableReducer<Text, NullWritable, NullWritable> {
        @Override
        protected void reduce(Text key, Iterable<NullWritable> values, Context context) throws IOException, InterruptedException {
            String[] student = key.toString().split("\t");
            String id = student[0];
            String name = student[1];
            int age = Integer.parseInt(student[2]);
            String gender = student[3];
            String clazz = student[4];

            Put put = new Put(id.getBytes());
            put.add("info".getBytes(), "name".getBytes(), name.getBytes());
            put.add("info".getBytes(), "age".getBytes(), Bytes.toBytes(age));
            put.add("info".getBytes(), "gender".getBytes(), gender.getBytes());
            put.add("info".getBytes(), "clazz".getBytes(), clazz.getBytes());


            //数据写入hbase
            context.write(NullWritable.get(), put);


        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        conf.set("hbase.zookeeper.quorum", "node1:2181,node2:2181,node3:2181");
        Job job = Job.getInstance(conf);

        job.setNumReduceTasks(5);
        job.setJarByClass(HdfsToHbase.class);
        job.setJobName("HdfsToHbase");
        job.setMapperClass(ToHbaseMap.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(NullWritable.class);
        FileInputFormat.addInputPath(job, new Path("/data/out/student/"));

        //指定reduce
        TableMapReduceUtil.initTableReducerJob("student", HDFSToHbaseReducer.class, job, null, null, null, null, false);

        job.setOutputKeyClass(NullWritable.class);
        job.setOutputValueClass(Put.class);

        boolean b = job.waitForCompletion(true);
    }
}
