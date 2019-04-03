package com.shujia.optimize

import org.apache.spark.{SparkConf, SparkContext}

import scala.util.Random

object DouvleReduce {

  /**
    * 双重聚合
    * 一般适用于  业务不复杂的情况
    *
    */
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local").setAppName("app")
    val sc = new SparkContext(conf)
    val lines = sc.textFile("spark/data/word")

    val wordRDD = lines.flatMap(_.split(","))

    // 对每一个key打上随机5以内前缀
    wordRDD.map(word => {
      val pix = Random.nextInt(5)
      (pix + "-" + word, 1)
    }).groupByKey() //第一次聚合
      .map(t => (t._1, t._2.toList.sum))
      .map(t => (t._1.split("-")(1), t._2)) ///去掉随机前缀
      .groupByKey() //第二次聚合
      .map(t => (t._1, t._2.toList.sum))
      .foreach(println)

    while (true) {

    }

  }
}
