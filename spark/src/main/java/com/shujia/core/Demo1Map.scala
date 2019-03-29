package com.shujia.core

import org.apache.spark.{SparkConf, SparkContext}

object Demo1Map {
  /**
    * map算子，传入一行返回一行
    *
    * @param args
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

    RDD1.map(l => l * 2).foreach(println)


  }
}
