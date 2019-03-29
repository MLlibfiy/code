package com.shujia.core

import org.apache.spark.{SparkConf, SparkContext}

object Demo2Filter {
  /**
    * filter算子，返回true 保存数据，返回false 过滤数据
    *
    */
  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setMaster("local").setAppName("app")

    val sc = new SparkContext(conf)


    /**
      * 通过scala集合构建RDD
      *
      */
    val list = List(1, 2, 3, 4, 5, 6, 7, 8)

    //把scala 集合序列化成一个RDD
    val RDD1 = sc.parallelize(list)

    //取出偶数
    RDD1.filter(_ % 2 == 0).foreach(println)


  }
}
