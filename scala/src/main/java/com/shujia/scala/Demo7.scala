package com.shujia.scala

/**
  * 高阶函数
  * 2、以函数作为返回值
  *
  */
object Demo7 {
  def main(args: Array[String]): Unit = {

    // String => Int  指定函数的返回值类型为一个参数类型为String 返回值类型为Int 函数
    def fun1(): String => Int = {

      def f(string: String): Int = {
        string.length
      }

      f

    }
    //返回一个函数
    val f1 = fun1()
    println(f1("数加学院"))

    //连续调用
    val length = fun1()("数加学院")
    println(length)


  }
}
