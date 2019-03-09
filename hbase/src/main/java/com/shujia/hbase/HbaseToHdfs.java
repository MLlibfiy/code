package com.shujia.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * 通过mapreduce 将hbase里面的数据导入到hdfs
 */
public class HbaseToHdfs {

    /**
     * 读取hbase数据的map
     */
    public class HbaseToHdfsMap extends TableMapper<NullWritable, Text> {
        @Override
        protected void map(ImmutableBytesWritable key, Result value, Context context) throws IOException, InterruptedException {

            String id = Bytes.toString(key.get());
            //Result  一行数据
            String name = Bytes.toString(value.getValue("info".getBytes(), "name".getBytes()));
            int age = Bytes.toInt(value.getValue("info".getBytes(), "age".getBytes()));
            String gender = Bytes.toString(value.getValue("info".getBytes(), "gender".getBytes()));
            String clazz = Bytes.toString(value.getValue("info".getBytes(), "clazz".getBytes()));

            String stu = id + "\t" + name + "\t" + age + "\t" + gender + "\t" + clazz;


            context.write(NullWritable.get(), new Text(stu));


        }
    }


    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        conf.set("hbase.zookeeper.quorum", "node1:2181,node2:2181,node3:2181");
        Job job = Job.getInstance(conf);
        job.setNumReduceTasks(5);
        job.setJarByClass(HbaseToHdfs.class);
        job.setJobName("HbaseToHdfs");

        Scan scan = new Scan();
        scan.addFamily("info".getBytes());
        //初始化map 类
        TableMapReduceUtil.initTableMapperJob("student", scan, HbaseToHdfsMap.class, NullWritable.class, Text.class, job);
        //指定输出目录
        FileOutputFormat.setOutputPath(job, new Path("/data/out/student"));

        boolean b = job.waitForCompletion(true);

    }
}
