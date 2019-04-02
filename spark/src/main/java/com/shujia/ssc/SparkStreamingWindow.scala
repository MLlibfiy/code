package com.shujia.ssc

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Durations, StreamingContext}

object SparkStreamingWindow {


  /**
    * 窗口函数
    * 查看最近30分钟最热门的微博，每一分钟查看一次
    *
    */


  def main(args: Array[String]): Unit = {


    val conf = new SparkConf()
      .setMaster("local[2]")
      .setAppName("ssc")

    val ssc = new StreamingContext(conf, Durations.seconds(5))

    val socketDS = ssc.socketTextStream("node1", 8888)


    /**
      * reduceByKeyAndWindow
      * 聚合函数
      * 窗口大小
      * 滑动时间
      *
      * 窗口大小和滑动时间必须是batch interval 的整数倍
      *
      */

    //    socketDS
    //      .map((_, 1))
    //      //统计最近15的数据，每5秒统计一次
    //      .reduceByKeyAndWindow(
    //      (x: Int, y: Int) => x + y,
    //      Durations.seconds(15),
    //      Durations.seconds(5))
    //      .print()


    /**
      * 优化之后的窗口操作
      */

    //需要设置checkpoint地址
    ssc.checkpoint("spark/data/checkpoint")

    socketDS
      .map((_, 1))
      //统计最近15的数据，每5秒统计一次
      .reduceByKeyAndWindow(
      (x: Int, y: Int) => x + y, //加上当前batch 部分
      (x: Int, y: Int) => x - y, //上一个窗口的结果减去多出来的部分
      Durations.seconds(15),
      Durations.seconds(5))
      .filter(_._2 != 0)
      .print()


    ssc.start()
    ssc.awaitTermination()
    ssc.stop()


  }

}
