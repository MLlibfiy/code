package com.shujia.scala

object Demo15Implicit {


  /**
    * 隐式转换
    *
    * 1、隐式转换方法
    * 2、隐式转换参数
    * 3、隐式转换类
    *
    */
  def main(args: Array[String]): Unit = {

    point("张三")("普通")

    //隐式转换变量，只和类型有关，和名字无关
    implicit val str:String = "implicit"
    //同一个作用域里面不能有两个类型相同的隐式转换变量
    //implicit val str1:String = "implicit1"

    point("张三")
    point("李四")
    point("王五")

  }


  /**
    * 隐式转换参数，在调用方法的时候可以不指定这个参数，
    * 自动去当前作用域里面找一个类型匹配的隐式转换变量传递进来
    *
    */
  def point(name: String)(implicit p: String): Unit = {
    println(s"$p\t$name")
  }
}
