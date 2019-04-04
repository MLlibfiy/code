package com.shujia.optimize

import java.util

import org.apache.spark.broadcast.Broadcast
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import scala.util.Random

object DoubleJoinScala {
  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName("app").setMaster("local")
    val sc = new SparkContext(conf)
    val dataList1 = List(
      ("hello", 1),
      ("shujia", 2),
      ("shujia", 3),
      ("shujia", 1),
      ("shujia", 1))

    val dataList2 = List(
      ("hello", 100),
      ("hello", 99),
      ("shujia", 88),
      ("shujia", 66))

    val RDD1: RDD[(String, Int)] = sc.parallelize(dataList1)
    val RDD2: RDD[(String, Int)] = sc.parallelize(dataList2)

    val sampleRDD: RDD[(String, Int)] = RDD1.sample( false, 1.0)

    //skewedKey  导致数据倾斜的key   shujia
    val skewedKey: String = sampleRDD.map(x => (x._1, 1))
      .reduceByKey(_ + _)
      .map(x => (x._2, x._1))
      .sortByKey(ascending = false)
      .take(1)(0)._2



    //导致数据倾斜key的RDD
    val skewedRDD1: RDD[(String, Int)] = RDD1.filter(tuple => {
      tuple._1.equals(skewedKey)
    })

    val commonRDD1: RDD[(String, Int)] = RDD1.filter(tuple => {
      !tuple._1.equals(skewedKey)
    })

    val skewedRDD2: RDD[(String, Int)] = RDD2.filter(tuple => {
      tuple._1.equals(skewedKey)
    })

    val commonRDD2: RDD[(String, Int)] = RDD2.filter(tuple => {
      !tuple._1.equals(skewedKey)
    })

    val prefixSkewedRDD1: RDD[(String, Int)] = skewedRDD1.map(tuple => {
      val random = new Random()
      val prefix: Int = random.nextInt(2)
      (prefix + "_" + tuple._1, tuple._2)
    })

    val expandSkewedRDD2: RDD[(String, Int)] = skewedRDD2.flatMap(tuple => {
      val list = new util.ArrayList[(String, Int)]
      for (i <- 0 until 2) {
        list.add((i + "_" + tuple._1, tuple._2))
      }
      import scala.collection.JavaConversions._

      //将java集合转换成scala 集合
      list.toList
    })

    //数据倾斜的RDD  join
      val resultRDD1: RDD[(String, (Int, Int))] = prefixSkewedRDD1.join(expandSkewedRDD2).map(tuple => {
      //去掉随机前缀
      (tuple._1.split("_")(1), tuple._2)
    })

    //没有数据倾斜的RDD  正常join
    val resultRDD2: RDD[(String, (Int, Int))] = commonRDD1.join(commonRDD2)

    //将两个结果拼接
    resultRDD1.union(resultRDD2)
      .foreach(println)

  }
}
