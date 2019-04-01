package com.shujia.ssc

import org.apache.spark.SparkConf
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Durations, StreamingContext}

object SparkStreamingOnKafkaReceiver {
  def main(args: Array[String]): Unit = {

    /**
      * spark streaming  作为kafak的消费者
      *
      *Receiver  被动接收数据
      */

    val conf = new SparkConf()
      .setMaster("local[2]")
      .setAppName("ssc")
      .set("spark.streaming.receiver.writeAheadLog.enable","true")//开启WAL机制，需要设置Checkpoint路径

    val ssc = new StreamingContext(conf, Durations.seconds(5))

    ssc.checkpoint("spark/data/checkpoint")


    val topics = Map("topic" -> 1) //topic  名称和接收数据的并行度


    /**
      * 连接kafka创建DStream
      *
      * 被动接收数据
      *
      */

    val kafkaDS = KafkaUtils.createStream(
      ssc,
      "node1:2181,node2:2181,node3:2181",
      "sparkOnkafka",//随意
      topics
    )

    val countDS = kafkaDS
      .map(_._2)//取出数据
      .flatMap(_.split(" "))
      .map((_,1))
      .reduceByKey(_+_)


    countDS.print()


    ssc.start()
    ssc.awaitTermination()
    ssc.stop()






  }
}
