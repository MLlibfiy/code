package com.shujia.optimize

import org.apache.spark.storage.StorageLevel
import org.apache.spark.{SparkConf, SparkContext}

object TestKryo {

  /**
    * 使用kryo序列化方式代替默认序列化方式（objectOutPutStream/objectInPutStream）
    * 性能提高10倍
    *
    *
    * spark  三个地方涉及到序列化
    *
    * 1、算子里面用到可外部变量
    * 2、RDD 类型为自定义类型，同时使用checkpoint 或者  使用shuffle类算子的时候会产生序列化
    *
    */
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
      .setMaster("local")
      .setAppName("app")
      //序列化方式
      .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      //知道注册序列化的类，自定义
      .set("spark.kryo.registrator", "com.shujia.optimize.MyRegisterKryo")

    val sc = new SparkContext(conf)

    sc.setCheckpointDir("spark/data/checkpoint")

    val data = sc.textFile("spark/data/student.txt")


    /**
      * 自定义对象比字符串赵勇内存更多
      * 因为自定义对象由对象头信息
      *
      */

    ,
    var stuRDD = data
      .map(_.split(","))
      .map(line => Student(line(0), line(1), line(2).toInt, line(3), line(4)))


    ///checkpoint  产生序列化
    stuRDD.checkpoint()

    //shuffle 类算子产生序列化
    stuRDD.map(s => (s.id, s)).groupByKey()


    //对RDD  持久化会产生序列化
    stuRDD = stuRDD.persist(StorageLevel.MEMORY_AND_DISK_SER)


    stuRDD.foreach(println)

  }
}

case class Student(id: String, name: String, age: Int, gender: String, clazz: String)
