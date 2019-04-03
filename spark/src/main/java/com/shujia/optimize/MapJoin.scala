package com.shujia.optimize

import org.apache.spark.{SparkConf, SparkContext}

object MapJoin {
  /**
    * map join
    *
    * 将小表广播，大表使用map算子
    *
    * 1、小表不能太大， 不能超过10G
    * 2、如果driver内存不足，需要手动设置  如果广播变量大小超过了driver内存大小，会出现oom
    *
    */

  def main(args: Array[String]): Unit = {


    val conf = new SparkConf().setMaster("local").setAppName("app")
    val sc = new SparkContext(conf)

    //RDD 不能广播

    val studentRDD = sc.textFile("spark/data/student.txt")

    //将数据拉去到driver端，变成一个map集合
    val stuMap = studentRDD.collect().map(s => (s.split(",")(0), s)).toMap

    //广播mapJ集合
    val broStu = sc.broadcast(stuMap)

    val scoreRDD = sc.textFile("spark/data/score.txt")

    //循环大表，通过key获取小表信息
    scoreRDD.map(s => {
      val sId = s.split(",")(0)

      //重广播变量里面获取数据
      val stuInfo = broStu.value.getOrElse(sId, "")

      stuInfo + "," + s
    }).foreach(println)

    while (true) {

    }


  }
}

