package com.shujia.ssc

import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Durations, StreamingContext}

object SparkStreamingOnKafkaOnFlume {


  def main(args: Array[String]): Unit = {

    /**
      * spark streaming  作为kafak的消费者
      *
      * Receiver  被动接收数据
      */

    val conf = new SparkConf()
      .setMaster("local[2]")
      .setAppName("ssc")
      .set("spark.streaming.receiver.writeAheadLog.enable", "true") //开启WAL机制，需要设置Checkpoint路径

    val ssc = new StreamingContext(conf, Durations.seconds(5))


    ssc.checkpoint("spark/data/checkpoint")


    //使用recevise 模式读取kafka数据
    val poinDS = KafkaUtils.createStream(
      ssc,
      "node1:2181,node2:2181,node3:2181",
      "pointgroup",
      Map("staypoint" -> 1)
    )

    poinDS.foreachRDD(rdd => {

      /**
        * recevise  模式rdd分区数量，
        * batch interval   5S
        * block interval  200 毫秒   每200毫秒生成一个block    一个block 块对应一个分区
        *
        * 5s/200ms = 25  如果数据不间断，会产生25个分区，   如果没有数据，不会产生分区
        *
        */

      println(s"rdd:分区的数量：${rdd.getNumPartitions}")
      rdd.count()
    })


    poinDS
      .map(_._2)
      .map(_.split(",")(2))
      .map((_, 1))
      .updateStateByKey((seq: Seq[Int], option: Option[Int]) => {
        Option(seq.sum + option.getOrElse(0))
      })
      .print()

    ssc.start()
    ssc.awaitTermination()
    ssc.stop()


  }

}
