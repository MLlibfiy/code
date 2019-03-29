package com.shujia.core

import org.apache.spark.{SparkConf, SparkContext}

import scala.io.Source

object Demo14Bro {

  /**
    * 广播变量
    */

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local").setAppName("app")

    val sc = new SparkContext(conf)


    val scoreRDD = sc.textFile("spark/data/score.txt", 10)


    val studentMap = Source
      .fromFile("spark/data/student.txt")
      .getLines()
      .toList
      .map(line => (line.split(",")(0), line))
      .toMap


    /**
      * 广播变量
      *
      */
    //广播
    val studentBro = sc.broadcast(studentMap)

    scoreRDD.map(line => {
      val strs = line.split(",")
      val stuId = strs(0)

      //获取广播变量
      val stunewMao = studentBro.value

      val stu = stunewMao(stuId)
      s"$stu,$line"
    }).foreach(println)


    /**
      *
      * 不能这个写代码，excutoer不能进行任务调度，RDD  不能在网络中传输
      */

    //scoreRDD.foreach(line=>{
    //      println(line)
    //      studentRDD.foreach(l=>{
    //        println(l)
    //      })
    //    })


  }

}
