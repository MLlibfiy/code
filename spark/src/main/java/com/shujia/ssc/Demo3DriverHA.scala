package com.shujia.ssc

import org.apache.spark.SparkConf
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.{Durations, StreamingContext}

object Demo3DriverHA {

  /**
    * 有状态算子
    *
    * 更新key的状态
    *
    */


  def main(args: Array[String]): Unit = {


    val conf = new SparkConf().setAppName("state")


    val checkpointDir = "/spark/data/checkpoint"

    /**
      *
      * 创建spark streaming 上下文对象
      *
      */


    val creatingFunc = () => {

      println("创建StreamingContext.........................")

      val ssc = new StreamingContext(conf, Durations.seconds(5))

      //设置checkpoint路径
      ssc.checkpoint(checkpointDir)

      val socketDS = ssc.socketTextStream("node1", 8888, StorageLevel.MEMORY_AND_DISK_SER)

      socketDS
        .flatMap(_.split(" "))
        .map((_, 1))
        .reduceByKey(_ + _)
        .print()


      ssc
    }


    /**
      * 创建streaming Context
      * 1\ 如果checkpointDir 有Drveri的元信息,就基于checkpointDir的元信息容错出一个Driver
      * 2  如果checkpointDir  没有数据  则调用creatingFunc  启动一个Driver
      *
      */
    val ssc = StreamingContext.getOrCreate(checkpointDir, creatingFunc)

    ssc.start()
    ssc.awaitTermination()
    ssc.stop()


  }
}
