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
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.partition.HashPartitioner;

import java.io.IOException;


/**
 * hdfs 数据导入hbase    批量导入，可以设置对各reduce 提高导入的并行度
 */
public class HdfsToHbase {


    public static class ToHbaseMap extends Mapper<LongWritable, Text, Text, Text> {
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            context.write(new Text(value.toString().split("\t")[0]), value);
        }
    }

    public static class HDFSToHbaseReducer extends TableReducer<Text, Text, NullWritable> {
        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            for (Text value : values) {
                String[] student = value.toString().split("\t");
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
    }

    /**
     * 自定义分区
     */
    public static class HDFSToHbasePartitioner extends Partitioner<Text, Text> {
        @Override
        public int getPartition(Text key, Text value, int numReduceTasks) {
            String id = key.toString();
            if (id.compareTo("15001000|") < 0) {
                return 0;
            }
            if (id.compareTo("15001001|") < 0) {
                return 1;
            }
            if (id.compareTo("15001002|") < 0) {
                return 2;
            }
            if (id.compareTo("15001003|") < 0) {
                return 3;
            }
            if (id.compareTo("15001004|") < 0) {
                return 4;
            }
            if (id.compareTo("15001005|") < 0) {
                return 5;
            }
            if (id.compareTo("15001006|") < 0) {
                return 6;
            }
            if (id.compareTo("15001007|") < 0) {
                return 7;
            }
            if (id.compareTo("15001008|") < 0) {
                return 8;
            }
            if (id.compareTo("15001009|") < 0) {
                return 9;
            }

            return 0;

        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        conf.set("hbase.zookeeper.quorum", "node1:2181,node2:2181,node3:2181");
        Job job = Job.getInstance(conf);

        job.setNumReduceTasks(10);
        job.setJarByClass(HdfsToHbase.class);
        job.setJobName("HdfsToHbase");
        job.setMapperClass(ToHbaseMap.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        job.setPartitionerClass(HDFSToHbasePartitioner.class);//设置自定义分区器


        FileInputFormat.addInputPath(job, new Path("/data/out/student/"));

        //指定reduce
        TableMapReduceUtil.initTableReducerJob("student1", HDFSToHbaseReducer.class, job, null, null, null, null, false);

        job.setOutputKeyClass(NullWritable.class);
        job.setOutputValueClass(Put.class);

        boolean b = job.waitForCompletion(true);
    }
}
