package com.shujia

import org.apache.spark.{SparkConf, SparkContext}

object Demo8MapValue {
  /**
    *
    * simple  抽样算子  返回一个RDD
    */
  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setMaster("local").setAppName("app")

    val sc = new SparkContext(conf)

    val studentRDD = sc.textFile("spark/data/student.txt")

    val tupleRDD = studentRDD
      .map(_.split(","))
      .map(line => (line(0), line(2)))


    //tupleRDD.map(line => (line._1, line._2.toInt + 1))

    /**
      * mapValues 相当于map
      * 只对value操作
      *
      */

    tupleRDD
      .mapValues(_.toInt + 1)
      .foreach(println)


  }
}
