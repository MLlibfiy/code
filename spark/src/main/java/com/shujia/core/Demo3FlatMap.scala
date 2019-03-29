package com.shujia.core

import org.apache.spark.{SparkConf, SparkContext}

object Demo3FlatMap {
  /**
    * flatMap算子，传入一行返回d朵翰行
    * 过程：  先进性map   再对返回的序列扁平化
    *
    */
  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setMaster("local").setAppName("app")

    val sc = new SparkContext(conf)


    /**
      * 通过scala集合构建RDD
      *
      */
    val list = List("1,2", "3,4", "5,6")

    //把scala 集合序列化成一个RDD
    val RDD1 = sc.parallelize(list)

    RDD1.flatMap(_.split(",")).foreach(println)


  }
}
