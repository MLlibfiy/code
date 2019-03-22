package com.shujia.scala

object Demo10Tuple {
  def main(args: Array[String]): Unit = {

    //元祖，不可变的序列，，可以通过下表获取元素
    val t = (1,2,3,4,5)

    println(t._1)
    println(t._2)
    println(t._4)
    println(t._5)
    //println(t._6)//编译时异常，避免在运行出现数组下标越界




  }
}
