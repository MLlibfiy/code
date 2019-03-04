package com.shujia.mr.pagerank;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Counter;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.Arrays;


class PageRankMapper extends Mapper<LongWritable, Text, Text, Text> {
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        //按照制表符分割
        /**
         * A	1	B	D
         * B	1	C
         * C	1	A	B
         * D	1	B	C
         */
        String[] split = value.toString().split("\t");
        String page = split[0];
        Double pagerank = Double.parseDouble(split[1]);
        //获取出链列表
        String[] outlist = Arrays.copyOfRange(split, 2, split.length);

        //1、平分之后的pr值
        Double avgPr = pagerank / outlist.length;
        for (String s : outlist) {
            Text outKey = new Text(s);
            Text outValue = new Text(avgPr.toString() + "\t*");
            //第一种数据
            context.write(outKey, outValue);
        }

        //指定分隔符拼接数组
        String outListStr = StringUtils.join(outlist, "\t");
        Text text = new Text(outListStr + "\t#");

        //每个网页的出链列表
        context.write(new Text(page), text);


    }
}

class PageRankReducer extends Reducer<Text, Text, Text, Text> {

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

        Double sum = 0.0;

        String outList = "";

        for (Text value : values) {
            String[] split = value.toString().split("\t");
            String flag = split[split.length - 1];
            //出链列表
            if ("#".equals(flag)) {
                outList = value.toString().substring(0, value.toString().length() - 2);
            } else if ("*".equals(flag)) {//pagerank值加和
                sum = sum + Double.parseDouble(split[0]);
            }
        }

        //写入到hdfs
        /**
         * A	0.5 	B	D
         * B	1.5 	C
         * C 	1.5 	A 	B
         * D 	0.5 	B 	C
         */
        context.write(key, new Text(sum.toString() + "\t" + outList));

    }
}


public class RunJob {

    public static void main(String[] args) throws Exception {

        Configuration config = new Configuration();
        FileSystem fs = FileSystem.get(config);
        Job job = Job.getInstance(config);
        job.setJarByClass(com.shujia.mr.pagerank.RunJob.class);
        job.setJobName("pagerank");
        job.setMapperClass(PageRankMapper.class);
        job.setReducerClass(PageRankReducer.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        job.setInputFormatClass(TextInputFormat.class);

        Path inputPath = new Path("E:\\第二期\\code\\mapreduce\\data\\pagerank.txt");

        FileInputFormat.addInputPath(job, inputPath);

        Path outPath = new Path("E:\\第二期\\code\\mapreduce\\data\\out");
        if (fs.exists(outPath)) {
            fs.delete(outPath, true);
        }

        FileOutputFormat.setOutputPath(job, outPath);
        boolean f = job.waitForCompletion(true);


    }

}
