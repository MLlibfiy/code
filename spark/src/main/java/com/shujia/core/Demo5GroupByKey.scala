package com.shujia.core

import org.apache.spark.{SparkConf, SparkContext}

object Demo5GroupByKey {
  /**
    *
    * groupByKey   分区类算子，必须作用在kv格式的RDD上
    *
    *
    */
  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setMaster("local").setAppName("app")

    val sc = new SparkContext(conf)

    val linesRDD = sc.textFile("spark/data/student.txt")

    //计算每个班级学生的总数
    linesRDD
      .map(_.split(",")(4)) //取出班级
      .map((_, 1))
      .groupByKey() //安装班级分组
      .map(g => (g._1, g._2.toList.sum)) //组内求和
      .foreach(println)


    linesRDD
      .map(_.split(",")(4)) //取出班级
      .map((_, 1))
      .reduceByKey(_ + _)
      .foreach(println)


    /**
      * groupBykey 和reduceByKey的区别
      *
      * groupBykey  根据key进行分组，相当于一个mapreduce，将相同的key拿到同一个reduce里面进行操作
      *
      * reduceByKey  根据key进行聚合  ，在map端会进行预聚合，减少reduce拉取的数据量，
      *
      *
      * 如果只是聚合类的操作可以使用reduceByKey  提高执行效率
      *
      *
      *
      */


  }
}
