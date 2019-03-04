package com.shujia.mr.itemcf;

import org.apache.hadoop.conf.Configuration;

import java.util.HashMap;
import java.util.Map;

public class StartRun {

	public static void main(String[] args) {
		Configuration config = new Configuration();
		// 所有mr的输入和输出目录定义在map集合中
		Map<String, String> paths = new HashMap<String, String>();
		paths.put("Step1Input", "E:\\第一期\\大数据\\hadoop\\pageRank\\mapreduce\\data\\sam_tianchi_2014002_rec_tmall_log.csv");
		paths.put("Step1Output", "E:\\第一期\\大数据\\hadoop\\pageRank\\mapreduce\\data\\output/step1");
		paths.put("Step2Input", paths.get("Step1Output"));
		paths.put("Step2Output", "E:\\第一期\\大数据\\hadoop\\pageRank\\mapreduce\\data\\output/step2");
		paths.put("Step3Input", paths.get("Step2Output"));
		paths.put("Step3Output", "E:\\第一期\\大数据\\hadoop\\pageRank\\mapreduce\\data\\output/step3");
		paths.put("Step4Input1", paths.get("Step2Output"));
		paths.put("Step4Input2", paths.get("Step3Output"));
		paths.put("Step4Output", "E:\\第一期\\大数据\\hadoop\\pageRank\\mapreduce\\data\\output/step4");
		paths.put("Step5Input", paths.get("Step4Output"));
		paths.put("Step5Output", "E:\\第一期\\大数据\\hadoop\\pageRank\\mapreduce\\data\\output/step5");
		paths.put("Step6Input", paths.get("Step5Output"));
		paths.put("Step6Output", "E:\\第一期\\大数据\\hadoop\\pageRank\\mapreduce\\data\\output/step6");

		//去重
/*		Step1.run(config, paths);
		//计算用户评分矩阵
		Step2.run(config, paths);
		Step3.run(config, paths);
		Step4.run(config, paths);
		Step5.run(config, paths);*/
		Step6.run(config, paths);
	}

	public static Map<String, Integer> R = new HashMap<String, Integer>();
	static {
		R.put("click", 1);
		R.put("collect", 2);
		R.put("cart", 3);
		R.put("alipay", 4);
	}
}
