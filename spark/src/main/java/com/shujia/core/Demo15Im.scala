package com.shujia.core

import org.apache.spark.{SparkConf, SparkContext}

object Demo15Im {

  /**
    * 累加器
    *
    * 在Driver端定义，在Excutor端累加，在Driver读取
    *
    *
    */
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local").setAppName("app")

    val sc = new SparkContext(conf)

    val studentRDD = sc.textFile("spark/data/student.txt")


   /* var sum = 0
    studentRDD.foreach(line => {
      //sum  变量副本，对这个变量副本的修改，不会体现到Driver端
      sum += 1
    })
    println(sum)*/

    /**
      * 累加器
      *
      */

    //在Driver端定义累加器
    //全局变量，
    val acc = sc.accumulator(0)

    //在Excutor端累加
    studentRDD.foreach(line => {
      acc.add(1)
    })

    //在Driver端读取
    println(acc.value)

    /**
      * 累加器，在每一个Excutor中都会有一个副本（BlockManager），进行累加，最终到Driver合并成一个变量
      *
      */




  }

}
