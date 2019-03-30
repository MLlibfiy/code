package com.shujia.core

import org.apache.spark.{SparkConf, SparkContext}

object Demo17Parallelism {
  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setMaster("local").setAppName("app")
    conf.set("spark.default.parallelism","200")

    val sc = new SparkContext(conf)

    val scoreRDD = sc.textFile("spark/data/score.txt").map((_, 1))

    val RDD1 = scoreRDD.groupByKey()
    println(RDD1.getNumPartitions)


    val RDD2 = scoreRDD.groupByKey(10)
    println(RDD2.getNumPartitions)



  }
}
