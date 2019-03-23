package com.shujia.scala

object Demo13Implicit {

  /**
    * 隐式转换
    * 编译时起作用
    */

  def main(args: Array[String]): Unit = {

    point("我爱数加")

    //显示转换

    point(12.toString)


    /**
      * 写在其他object中也可以，需要用的时候导入就可以了
      *
      */
    import com.shujia.scala.implicittest._

    /**
      * 隐式转换方法
      * 隐式转换只在当前作用域起作用
      *
      */
    implicit def intToString(i: Int): String = i.toString

    implicit def doubleToString(i: Double): String = i.toString

    /**
      * 同一个作用域里面不能有两个参数类型和返回值类型一样的隐式转换
      *
      * 隐式转换方法只和参数类型和返回值类型有关，和方法名无关
      *
      */
    //implicit def doubleToString2(i: Double): String = i.toString + "11"

    /**
      * 方法在调用的时候，如果类型参数的类型和方法需要的类型不匹配，
      * 编译器会去当前作用域里面找有没有这样一个隐式转换，如果有在编译的时候就会将这个方法加到调用里面
      * point(12)  => 编译  point(intToString(12))
      */

    point(12)//编译完成的样子  point(intToString(12))
    point(12.2)


  }

  def point(str: String): Unit = {
    println(str)
  }


}

object implicittest {
  /**
    * 隐式转换方法
    * 隐式转换只在当前作用域起作用
    *
    */
  implicit def intToString(i: Int): String = i.toString
}


