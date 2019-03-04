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


/**
 * 枚举，相当于一个类的对象已经被定死
 */
enum Count {
    my
}


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
        //加上上一次网页的pr值
        String outListStr = split[1] + "\t" + StringUtils.join(outlist, "\t");
        Text text = new Text(outListStr + "\t#");

        //每个网页的出链列表
        context.write(new Text(page), text);


    }
}

class PageRankReducer extends Reducer<Text, Text, Text, Text> {

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

        //当前网页的pagerank值
        Double sum = 0.0;

        String lastPrAndOutList = "";

        for (Text value : values) {
            String[] split = value.toString().split("\t");
            String flag = split[split.length - 1];
            //出链列表和上一次网页的pr值
            if ("#".equals(flag)) {
                lastPrAndOutList = value.toString();
            } else if ("*".equals(flag)) {//pagerank值加和
                sum = sum + Double.parseDouble(split[0]);
            }
        }


        String[] split = lastPrAndOutList.split("\t");

        //上一次pr值
        Double lastPr = Double.parseDouble(split[0]);

        String[] outList = Arrays.copyOfRange(split, 1, split.length - 1);


        //加上阻尼系数,计算当前pr值
        Double currPr = 0.15 / 4 + 0.85 * sum;

        //取绝对值
        long abs = (long) (Math.abs(currPr - lastPr) * 1000);

        //获取累加器对象
        Counter counter = context.getCounter(Count.my);

        //累加
        /**
         * 累加所有网页pr值的差值
         */
        counter.increment(abs);

        //写入到hdfs
        /**
         * A	0.5 	B	D
         * B	1.5 	C
         * C 	1.5 	A 	B
         * D 	0.5 	B 	C
         */
        context.write(key, new Text(currPr.toString() + "\t" + StringUtils.join(outList, "\t")));

    }
}


public class RunJob {

    public static void main(String[] args) throws Exception {
        int i = 0;

        //收敛阈值
        double flag = 0.1;

        while (true) {
            i++;
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


            //后一次读取前一次的输出结果
            if (i > 1) {
                inputPath = new Path("E:\\第二期\\code\\mapreduce\\data\\out" + (i - 1));
            }

            FileInputFormat.addInputPath(job, inputPath);

            Path outPath = new Path("E:\\第二期\\code\\mapreduce\\data\\out" + i);
            if (fs.exists(outPath)) {
                fs.delete(outPath, true);
            }

            FileOutputFormat.setOutputPath(job, outPath);
            boolean f = job.waitForCompletion(true);

            //计算当前所有网页的pagerank的和上一次pagerank的差值的平均值
            //当差值的平均值小于设定的阈值之后收敛

            /**
             * 累加器
             *  在map端或者reduce端累加，在主函数里面读取的一个变量
             *
             */

            //获取累加器的值
            Counter counter = job.getCounters().findCounter(Count.my);
            long value = counter.getValue();


            //差值的平均值
            double l = value / 4000.0;

            System.out.println(l);

            //当差值的平均值小于设定的阈值后收敛
            if (l < flag) {
                break;
            }
        }
    }
}
