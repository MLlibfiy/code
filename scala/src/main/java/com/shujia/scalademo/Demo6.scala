package com.shujia.scalademo

/**
  * 在java里面函数得依赖于对象执行，通过static修饰得方法依赖于类对象来执行
  *
  */

/**
  * 在scala 里面，函数可以单独运行，不需要依赖于任何对东西
  *
  */
object Demo6 {

  def main(args: Array[String]): Unit = {


    /**
      * 定义函数
      * 通过def定义函数，函数名必须有
      * 参数可以没有，如果没有参数括号可以省略
      * 返回值类型可以不写，scala 会自动推断返回值类型,如果写了返回值，那么返回类型必须匹配，或者返回子类类型
      */
    def fun1(string: String): Unit = {
      println(s"函数内部得函数\t  $string")
    }

    /**
      * 函数调用
      * 函数名加上()  调用函数
      * 如果没有参数，括号可以省略
      *
      */
    fun1("数加")


    /**
      * 高阶函数
      * 1、以函数作为参数
      *
      */

    //函数的类型有参数的类型和返回值的类型决定

    // f 是一个参数为String 返回值为Int的函数
    def fun2(f: String => Int): Unit = {
      //调用函数
      val i = f("1,2,3,4,5,6,7,8")
      println(i)
    }


    def fun3(str: String): Int = {
      str.length
    }

    def fun4(str: String): Int = {
      var sum = 0
      //求和
      for (i <- str.split(",").toList) {
        sum = sum + i.toInt
      }
      sum
    }

    //调用fun2 需要传递一个参数为String 返回值为Int的函数
    fun2(fun3)
    fun2(fun4)

    // 匿名函数   str => str.lengthl
    //lamda表达式
    fun2(str => str.length)


    val list = "1,2,3,4,5,6,7,8".split(",").toList


    def p(str: String): Unit ={
      println(str)
    }

    //foreach  将集合里面的元素一个一个的传递给后面的函数
    list.foreach(p)

    println("="*100)
    //匿名函数  str => println(str)
    list.foreach(str => println(str))
    println("="*100)
    //对上面的简写
    list.foreach(println)



  }
}
