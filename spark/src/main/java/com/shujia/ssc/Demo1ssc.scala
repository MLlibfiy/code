package com.shujia.ssc

import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.{Duration, Durations, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

object Demo1ssc {
  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setMaster("local[2]").setAppName("ssc")

    /**
      * 创建spark streaming 上下文对象
      * 设置 batch interval   每隔多少秒封装一个RDD , 每隔多少秒计算一次
      *
      */

    val ssc = new StreamingContext(conf,Durations.seconds(5))

    /**
      * 监听一个端口，创建DStream
      *
      */

    /**
      * f返回一个ReceiverInputDStream  被动接收数据,所以需要启动一个job任务接收数据
      * 没有足够的资源执行任务
      * 指定master  为local[2]
      *
      */

      //在node1 上执行  nc -lk 8888     安装 nc   yum install nc

    val DS = ssc.socketTextStream("node1",8888,StorageLevel.MEMORY_AND_DISK_SER)

    //打印数据，相当于action 算子
    //DS.print()

    //计算wordcount
    DS
      .flatMap(_.split(" "))   //转换算子 ,底层就是RDD之间的转换
      .map((_,1))
      .reduceByKey(_+_)
      .print()





    ssc.start()
    ssc.awaitTermination()
    ssc.stop()


  }
}
