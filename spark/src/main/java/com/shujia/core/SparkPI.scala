package com.shujia.core

import org.apache.spark.{SparkConf, SparkContext}

import scala.math.random

object SparkPI {

  /**
    * spark 实现计算圆周率
    *
    */
  def main(args: Array[String]): Unit = {
    /**
      * local  为这个应用程序申请一一个cpu
      * local[4]  4个cpu
      *
      */

    val conf = new SparkConf().setMaster("local[4]").setAppName("PI")
    val sc = new SparkContext(conf)

    val list = 0 until 100000000


    /**
      * 序列化一个scala集合，默认一个分区,并行度为1
      *
      *
      */
    val RDD = sc.parallelize(list, 10)

    val RDD2 = RDD.map(x => {
      val x = random * 2 - 1
      val y = random * 2 - 1
      if (x * x + y * y < 1) 1 else 0
    })

    //圆内点的数量
    val num = RDD2.reduce(_ + _).toDouble

    val pi = (num / list.length) * 4

    println(pi)


  }
}
