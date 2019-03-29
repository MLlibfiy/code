package com.shujia.core

import org.apache.spark.{SparkConf, SparkContext}

object Demo4Sample {
  /**
    *
    * simple  抽样算子  返回一个RDD
    */
  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setMaster("local").setAppName("app")

    val sc = new SparkContext(conf)

    val linesRDD = sc.textFile("spark/data/student.txt")

    /**
      * 抽样算子
      * withReplacement  是否放回
      * fraction  抽样比列
      * seed  步长
      *
      */
    val sampleRDD = linesRDD.sample(false, 0.1)

    sampleRDD.foreach(println)


  }
}
