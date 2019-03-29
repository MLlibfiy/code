package com.shujia

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.Partitioner

object Demo16Partitioner {
  def main(args: Array[String]): Unit = {


    val conf = new SparkConf().setMaster("local").setAppName("app")

    val sc = new SparkContext(conf)

    //设置minPartitions  提高并行度或者降低
    val data = sc.textFile("spark/data/dianxin")

    //获取分区的数量
    println(data.getNumPartitions)


    /**
      *
      * repartition  有shuffle
      * repartition 相当于 coalesce(numPartitions, shuffle = true)
      *
      */

    val reRDD = data.repartition(2)
    reRDD.foreach(println)


    /**
      * coalesce  在没有shuler的情况下只能用来减少分区，不能增加分区
      *
      */
    val coRDD = data.coalesce(1)
    println("分区数量：" + coRDD.getNumPartitions)
    coRDD.foreach(println)

    /**
      * 每一个分区会产生一个小文件
      *
      */
    //reRDD.saveAsTextFile("spark/data/output")


    val RDD2 = data.map((_, 1))

    RDD2
      //使用自定义分区
      .groupByKey(new MyPartitioner(3))
      .foreach(println)


  }
}

/**
  * 自定义分区， 继承Partitioner，实现numPartitions  和getPartition
  *
  */

class MyPartitioner(num: Int) extends Partitioner {
  //新的RDD分区数量
  override def numPartitions: Int = num


  //更具key获取key所属的分区
  override def getPartition(key: Any): Int = key match {
    case null => 0
    case _ => {

      println(s"$key:${key.hashCode()}")

      //默认也是hash 分区
      key.hashCode() % numPartitions
    }
  }

}
