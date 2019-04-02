package com.shujia.ssc

import kafka.serializer.StringDecoder
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Durations, StreamingContext}

object SparkStreamingOnKafkaDirect {
  def main(args: Array[String]): Unit = {
    /**
      * spark streaming
      *
      * direct  主动拉去数据
      */

    val conf = new SparkConf()
      .setMaster("local[2]")
      .setAppName("ssc")

    val ssc = new StreamingContext(conf, Durations.seconds(5))

    //direct模式保存消费偏移量
    ssc.checkpoint("spark/data/checkpoint")


    /**
      * 使用direct方式创建DStream
      *
      */

    val kafkaParams = Map("metadata.broker.list" -> "node1:9092,node2:9092,node3:9092")

    //可以指定多个topic
    val topics = Set("topic")

    val kafkaDstream = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](
      ssc,
      kafkaParams,
      topics
    )



    val countDS = kafkaDstream
      .map(_._2)
      .flatMap(_.split(" "))
      .map((_, 1))
      .reduceByKey(_ + _)

    countDS.print()


    ssc.start()
    ssc.awaitTermination()
    ssc.stop()


  }
}
