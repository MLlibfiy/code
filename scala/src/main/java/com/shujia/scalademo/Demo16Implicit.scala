package com.shujia.scalademo

import java.io.File

import scala.io.Source


object Demo16Implicit {

  /**
    * 隐式转换类
    */

  def main(args: Array[String]): Unit = {

    val file = new File("scala/data/students.txt")
    val lines = Source.fromFile(file).getLines().toList

    val fileRead = new FileRead(file)
    fileRead.read().foreach(println)


    println("=" * 100)

    /**
      * 当前作用域里面有一个File的隐式转换
      */

    /**
      * 本来这个类的对象没有这个方法，但是可以通过隐式转换动态的增加一个方法
      *
      * 在不修改源码的情况下对类进行扩展
      */
    file.read().foreach(println)


  }

  /**
    * 隐式转换类，自动将参数类型转换成当前类的对象
    *
    */
  implicit class FileRead(file: File) {

    def read(): List[String] = {

      val list = Source.fromFile(file).getLines().toList
      list

    }
  }

}


