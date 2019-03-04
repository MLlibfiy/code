package com.shujia.mr.tfidf;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * c1_001,2 c2_001,1 count,10000
 * 
 * @author root
 *
 */
public class FirstReduce extends Reducer<Text, IntWritable, Text, IntWritable> {

	@Override
	protected void reduce(Text key, Iterable<IntWritable> iterable,
			Context context) throws IOException, InterruptedException {

		int sum = 0;
		for (IntWritable i : iterable) {
			sum = sum + i.get();
		}
		if (key.equals(new Text("count"))) {
			System.out.println(key.toString() + "___________" + sum);
		}
		context.write(key, new IntWritable(sum));
	}
}
