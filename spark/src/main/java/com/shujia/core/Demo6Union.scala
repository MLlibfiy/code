package com.shujia.core

import org.apache.spark.{SparkConf, SparkContext}

object Demo6Union {
  /**
    *
    * simple  抽样算子  返回一个RDD
    */
  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setMaster("local").setAppName("app")

    val sc = new SparkContext(conf)

    val linesRDD = sc.textFile("spark/data/student.txt")


    //分别取出性别为男和性别为女的学生
    val RDD1 = linesRDD.filter(line => "男".equals(line.split(",")(3)))
    val RDD2 = linesRDD.filter(line => "女".equals(line.split(",")(3)))


    //union 将两个RDD 连接在一起，数据类型必须要一样
    RDD1.union(RDD2).foreach(println)



  }
}
