package com.shujia.sql

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.hive.HiveContext

object Demo7HIveContext {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[4]").setAppName("app")
    conf.set("spark.sql.shuffle.partitions","4")
    val sc = new SparkContext(conf)
    /**
      * 操作hive的上下文对象
      * 底层是使用hive解析sql
      *
      */
    val hiveContext = new HiveContext(sc)

    hiveContext.sql("show tables").show()

    hiveContext.sql("select * from score limit 10").show()

  }
}
