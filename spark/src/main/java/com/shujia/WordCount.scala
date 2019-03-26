package com.shujia

import org.apache.spark.{SparkConf, SparkContext}

/**
  * spark  实现wordcount
  *
  */
object WordCount {
  def main(args: Array[String]): Unit = {
    //1、创建spark配置信息对象
    val conf = new SparkConf()
      .setMaster("local") //本地运行模式
      .setAppName("WordCount") //任务名

    //创建spark 上下文对象，可以用来读取数据
    val sc = new SparkContext(conf)

    //读取本地文件
    /**
      * spark 自己没有读取数据的方法，他用的是mr读取数据的方法
      * textFile底层实现是TextInputFormat
      * 切片规则和mr一样
      */
    val linesRDD = sc.textFile("spark/data/words.txt")

    //flatMap  算子  ，先对数据做一把map操作，再扁平化
    val flatRDD = linesRDD.flatMap(line => line.split(","))

    val tupleRDD = flatRDD.map((_, 1))

    /**
      * reduceByKey  聚合算子
      *
      * 先根据key分组，组内做什么样的操作由你来决定
      *
      *
      * x,y 代表组内前后两个元素
      *
      * reduceByKey((x, y) => x + y)   组内求和
      */
    val countRDD = tupleRDD.reduceByKey((x, y) => x + y)



    //循环遍历，触发job运行  action算子
    countRDD.foreach(t => println(s"${t._1}\t${t._2}"))


    /**
      * spark 底层就是mapreduce
      * spark 只是对mr做了封装优化，简化了spi
      *
      */

    //简写版
    sc.textFile("spark/data/words.txt") //可以指定本地路径，也可以指定hdfs路径
      .flatMap(_.split(","))
      .map((_, 1))
      //.reduceByKey((x, y) => x + y)
      //第一个_ 代表第一个参数，第二个_代表第二个参数  只能使用一次

      /**
        * reduceByKey  分区类算子，必须作用在kv格式的rdd上
        *
        */
      .reduceByKey(_ + _)
      .map(t => s"${t._1}\t${t._2}")
      //.foreach(println)//遍历
      /**
        * 保存数据到本地
        * 触发job运行
        * action 类算子
        */
      .saveAsTextFile("spark/data/output")





    //while (true){}


  }
}
