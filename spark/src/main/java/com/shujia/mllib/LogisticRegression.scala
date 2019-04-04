package com.shujia.mllib

import org.apache.spark.mllib.classification.LogisticRegressionWithSGD
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.util.MLUtils
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object LogisticRegression {

  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("spark").setMaster("local[3]")
    val sc = new SparkContext(conf)

    //加载数据，数据已经被预处理了
    val inputData: RDD[LabeledPoint] = MLUtils.loadLibSVMFile(sc, "spark/data/人体指标.txt")

    //将数据芬格茨训练集，测试集，，
    //训练级：带入算法，训练模型
    //测试集，对模型进行测试，评估模型准确率
    val splits = inputData.randomSplit(Array(0.7, 0.3))
    val (trainingData, testData) = (splits(0), splits(1))


    /**
      * LogisticRegressionWithSGD  逻辑回归算法类
      *
      */
    val lr = new LogisticRegressionWithSGD()
    //指定是否有截距
    lr.setIntercept(true)

    //将训练集带入算法，训练模型

    //模型：  包含每一个维度（x） 的权重值（参数值）
    val model = lr.run(trainingData)


    val result = testData.map { point => {
      val label = point.label//测试集真实值
      val preLabel = model.predict(point.features)//通过模型预测的值
      Math.abs(label-preLabel)
    }}

    // result.mean()  错误率
    println("正确率=" + (1.0 - result.mean()))
    //权重值
    println(model.weights.toArray.mkString(" "))

    //截距
    println(model.intercept)

    //将训练的模型保存到hdfs
    model.save(sc,"spark/data/model")
  }
}
