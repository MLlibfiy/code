package com.shujia.scala

object Demo2 {
  def main(args: Array[String]): Unit = {
    //条件判断
    if (true) {
    } else if (true) {
    } else {
    }

    //toList  转集合
    val arr = "1,2,3,4,5,6".split(",").toList

    //增强for循环遍历集合
    for (i <- arr) {
      println(i)
    }

    println("=" * 100)

    //函数式编程，高阶函数
    /**
      * 1、以函数作为参数
      * 2、以函数作为返回值
      */
    //以函数作为参数
    arr.foreach(println)
    println("=" * 100)
    //x => println(x) 匿名函数
    arr.foreach(x => println(x))
    println("=" * 100)

    for (i <- 1 until 10) {
      println(i)
    }

    for (i <- 1 to 10) {
      println(i)
    }

  }
}
