package com.shujia

import org.apache.spark.{SparkConf, SparkContext}

object Demo10Count {
  /**
    *
    * simple  抽样算子  返回一个RDD
    */
  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setMaster("local").setAppName("app")

    val sc = new SparkContext(conf)

    val studentRDD = sc.textFile("spark/data/student.txt")

    val num = studentRDD.count()

    //统计每个年龄段的人数
    val ageNum  = studentRDD
      .map(_.split(","))
      .map(line => (line(2), line))
      .countByKey()

    println(ageNum)


  }
}
