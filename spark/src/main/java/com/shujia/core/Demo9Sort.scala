package com.shujia.core

import org.apache.spark.{SparkConf, SparkContext}

object Demo9Sort {
  /**
    *
    * simple  抽样算子  返回一个RDD
    */
  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setMaster("local").setAppName("app")

    val sc = new SparkContext(conf)

    val studentRDD = sc.textFile("spark/data/student.txt")
      .map(line => (line.split(",")(0), line)) //转成kv格式，以学号作为key

    val scoreRDD = sc.textFile("spark/data/score.txt")
      .map(line => (line.split(",")(0), line)) //转成kv格式，以学号作为key


    //join  分区类算子，必须作用在kv格式的RDD上
    val joinRDD = studentRDD.join(scoreRDD)


    //计算学生总分
    //(1500100430,(1500100430,屈佑运,22,男,文科五班,   1500100430,1000002,65))
    val stuScoSumRDD = joinRDD
      .map(_._2)
      .map(t => (t._1, t._2.split(",")(2).toInt)) //以学生信息作为key，以分数作为v
      .reduceByKey(_ + _)
      .map(t => s"${t._1},${t._2}")




    //取前十的学生

    //ascending  排序方式，默认升序
    stuScoSumRDD
      .sortBy(line => line.split(",")(5).toInt, ascending = false)
      .take(10)
      .foreach(println)


  }
}
