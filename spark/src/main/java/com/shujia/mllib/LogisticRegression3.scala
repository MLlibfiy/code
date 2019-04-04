package com.shujia.mllib

import org.apache.spark.mllib.classification.{LogisticRegressionWithLBFGS, LogisticRegressionWithSGD}
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.util.MLUtils
import org.apache.spark.{SparkConf, SparkContext}


object LogisticRegression3 {

  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("spark").setMaster("local[3]")
    val sc = new SparkContext(conf)
    // 解决线性不可分我们来升维,升维有代价,计算复杂度变大了
    var inputData = MLUtils.loadLibSVMFile(sc, "spark/data/线性不可分数据集.txt")

    /**
      * 二维不可分，对数据做升维
      *
      */

    inputData = inputData.map(point => {
      val lable = point.label
      val features = point.features
      //增加维度    两个维度相乘得到一个新的维度
      val f = Array(features(0), features(1), features(0) * features(1))

      LabeledPoint(lable, Vectors.dense(f))
    })

    inputData.foreach(println)

    val splits = inputData.randomSplit(Array(0.7, 0.3))

    val (trainingData, testData) = (splits(0), splits(1))

    val lr = new LogisticRegressionWithLBFGS()
    lr.setIntercept(true)
    val model = lr.run(trainingData)

    val result = testData
      .map { point => Math.abs(point.label - model.predict(point.features)) }

    println("正确率=" + (1.0 - result.mean()))
    println(model.weights.toArray.mkString(" "))
    println(model.intercept)
  }
}
