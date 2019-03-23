package com.shujia.scala

import scala.io.Source

object WordCount {
  /**
    * wordcount
    *
    */
  def main(args: Array[String]): Unit = {
    //1、读取数据
    val lines = Source.fromFile("scala/data/pagerank.txt").getLines().toList


    val list = List(1, 2, 3, 4, 5, 6, 7)
    //map  函数，传入一行，返回一行,可以返回任何东西
    list.map(x => x * 2).foreach(println)
    println("=" * 100)

    val f = List("1,2,3", "4,5,6")
    //flatMap  传入一行，返回多行，返回值必须是一个序列，
    //flatMap  相当于先做了一次map  操作，再将序列里面的数据扁平化
    f.flatMap(x => x.split(",")).foreach(println)

    // _  代表第一个参数，只能用一次
    f.flatMap(_.split(",")).foreach(println)

    println("=" * 100)


    lines
        .flatMap(line => line.split("\t"))
        .map(word => (word, 1)) //每一个单词后面加一个1
        .groupBy(t => t._1) //根据单词分组
        .map(t => {
        //t   (E,List((E,1), (E,1), (E,1), (E,1)))
        val word = t._1
        val sum = t._2 //每个组内的集合
          .map(x => x._2) //取出后面的1
          .sum
        s"$word\t$sum"

    }).foreach(println)


  }
}
