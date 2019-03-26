package com.shujia

import org.apache.spark.{SparkConf, SparkContext}

object Demo11Collect {
  /**
    *
    * simple  抽样算子  返回一个RDD
    */
  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setMaster("local").setAppName("app")

    val sc = new SparkContext(conf)
    val studentRDD = sc.textFile("spark/data/student.txt")

    //RDD只能使用RDD的api

    //如果studentRDD  数据量很大，会出现内存溢出 （>10G）

    /**
      * collect  将数据拉渠道Driver端，也就是程序运行的内存里面
      *
      */

    val arr = studentRDD.collect()

    studentRDD
      .map(_.split(","))
      .map(line => (line(0), line))
      .collectAsMap()
      .map(t => (t._1, t._2.toList))
      .foreach(println)


  }
}
