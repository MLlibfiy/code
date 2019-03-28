package com.shujia

import org.apache.spark.storage.StorageLevel
import org.apache.spark.{SparkConf, SparkContext}
object Demo13Cache {
  /**
    *
    * groupByKey   分区类算子，必须作用在kv格式的RDD上
    *
    *
    */
  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setMaster("local").setAppName("app")

    val sc = new SparkContext(conf)


    //设置checkpoint 存储地址
    sc.setCheckpointDir("spark/data/checkpoint")

    var linesRDD = sc.textFile("spark/data/student.txt")


    //多多次使用的RDD进行缓存

    //缓存之后，
    // 当第一个job执行的时候会将数据缓存起来，
    // 第二个job执行的时候就不需要重复读取数据了

    //cache  内存
    //linesRDD = linesRDD.cache()//相当于persist

    //StorageLevel.MEMORY_ONLY
    //linesRDD = linesRDD.persist()


    //MEMORY_AND_DISK_SER   先放内存内存放不下放磁盘并且序列化（压缩）
    linesRDD = linesRDD.persist(StorageLevel.MEMORY_AND_DISK_SER)

    /**
      * 将当前RDD的数据保存到hdfs ,切断RDD之间的依赖关系
      *
      * 当job执行完成会从最后一个RDD向前回溯，如果发现有一个RDD执行了checkpoint  ，
      * 会另启动一个job任务，将这个RDD的数据写入到hdfs,被标记的RDD会被计算两遍
      *
      * 为了提高速度，可以先对标记的RDD执行一把cache
      *
      *
      *
      *
      */
    linesRDD.checkpoint()




    //计算每个班级学生的总数
    linesRDD
      .map(_.split(",")(4)) //取出班级
      .map((_, 1))
      .groupByKey() //安装班级分组
      .map(g => (g._1, g._2.toList.sum)) //组内求和
      .foreach(println)


    linesRDD
      .map(_.split(",")(4)) //取出班级
      .map((_, 1))
      .reduceByKey(_ + _)
      .foreach(println)




  }
}
