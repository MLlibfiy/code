package com.shujia.core

import org.apache.spark.{SparkConf, SparkContext}

object Demo12Reduce {
  /**
    *
    * simple  抽样算子  返回一个RDD
    */
  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setMaster("local").setAppName("app")

    val sc = new SparkContext(conf)
    val studentRDD = sc.textFile("spark/data/student.txt")

    //总人数
    val sum = studentRDD
      .map(line => 1)
      .reduce(_ + _)//全部数据一起聚合，在map端有预聚合

    println(sum)


  }
}
