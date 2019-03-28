package com.shujia.scalademo

/**
  * 偏应用函数，函数柯里化
  *
  */
object Demo13 {

  def main(args: Array[String]): Unit = {

    def fun(a: Int, b: Int, c: Int): Int = {
      a + b + c //求和
    }

    println(fun(1, 2, 3))
    println(fun(1, 2, 4))
    println(fun(1, 2, 5))

    println("=" * 100)

    /**
      * 偏应用函数
      *
      * 在调用一个函数的时候可以不指定所有参数，那么这个时候会返回一个新的函数
      * 对这个新的函数可以继续调用，指定剩下的参数即可
      *
      * 返回一个还没有完全调用的函数
      */
    val newFun = fun(1, 2, _: Int)

    println(newFun(3))
    println(newFun(4))
    println(newFun(5))


    /**
      * 函数柯里化
      *
      */

    def f1(s1: String): String => String = {
      def rf(s2: String): String = {
        s1 + " 返回值为一个函数 " + s2
      }

      rf
    }

    val f = f1("第一次调用")

    /**
      * f = def rf(s2: String): String = {
      *   "第一次调用"+ " 返回值为一个函数 " + s2
      * }
      *
      */

    println(f("2"))
    println(f("3"))
    println(f("4"))

    println(f1("第一次调用")("第二次调用"))


    /**
      * 简写，函数的返回值还是一个函数
      *
      */
    def f2(s1: String)(s2: String):  String = {
        s1 + " 返回值为一个函数 " + s2
    }

    println(f2("第一次调用")("第二次调用"))






  }
}
