package com.shujia.optimize

import java.sql.DriverManager

import org.apache.spark.{SparkConf, SparkContext}

object TestConfig {
  def main(args: Array[String]): Unit = {


    /**
      * 参数优先级
      * 代码优先级  >   spark-submit 后面指定的优先级  >  spark 配置文件
      *
      *
      *
      */

    val conf = new SparkConf()
      .setMaster("local")
      .setAppName("app")
      .set("spark.shuffle.file.buffer", "64k") // sparrk  shuffle  过程数据落地缓存内存大小
      .set("spark.reducer.maxSizeInFlight", "96m") //reduce去map中一次最多拉去多少数据
      .set("spark.shuffle.io.maxRetries", "10") //shuffle read task从shuffle write task所在节点拉取属于自己的数据时  重试次数
      .set("spark.shuffle.io.retryWait", "60s") //shuffle read task从shuffle write task所在节点拉取属于自己的数据时  等待时间
      //两个一起调节，剩下0.2是task运行时可以使用的内存
      .set("spark.shuffle.memoryFraction", "0.4") // shuffle  内存占比
      .set("spark.storage.memoryFraction", "0.4") //  RDD持久化可以使用的内存
      .set("spark.default.parallelism", "100") //默认shuffle 并行度
    //.set("spark.shuffle.sort.bypassMergeThreshold","200")//触发bypassc机制的条件

    val sc = new SparkContext(conf)

    val data = sc.textFile("spark/data/student.txt")

    data.foreach(println)

  }
}
