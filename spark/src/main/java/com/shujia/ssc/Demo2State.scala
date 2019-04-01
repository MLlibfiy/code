package com.shujia.ssc

import org.apache.spark.SparkConf
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.{Duration, Durations, StreamingContext}

object Demo2State {

  /**
    * 有状态算子
    *
    * 更新key的状态
    *
    */


  def main(args: Array[String]): Unit = {


    val conf = new SparkConf().setMaster("local[2]").setAppName("state")

    val ssc = new StreamingContext(conf, Durations.seconds(5))

    //设置checkpoint地址
    ssc.checkpoint("spark/data/checkpoint")

    val socketDS = ssc.socketTextStream("node1", 8888, StorageLevel.MEMORY_AND_DISK_SER)


    /**
      * 计算历史所有的单词的数量
      *
      */


    /**
      * seq 当前batch  一个key的所有value
      * option  之前所有batch 计算的状态    泛型可以自定义,任意类型都可以,    wordcount  为int类型,
      *
      */
    val updataBykey = (seq: Seq[Int], option: Option[Int]) => {

      //当前batch  wordcount 值
      val sum = seq.sum

      /**
        * option  可能为None   当key  第一次出现的时候
        */

      //之前keu的状态
//      val count = option match {
//        case s: Some[Int] => s.get
//        case None => 0
//      }

      //如果为None  返回0
      val count = option.getOrElse(0)

      //返回当前batcch 计算的状态
      Option(sum + count)
    }

    /**
      *  updateStateByKey
      * 有状态算子,保存了之前的状态
      *
      * 使用有状态算子需要设置checkpoint 地址
      *k
      */

    socketDS
      .flatMap(_.split(" "))
      .map((_, 1))
      .updateStateByKey(updataBykey)
      .print()


    ssc.start()
    ssc.awaitTermination()
    ssc.stop()


  }
}
