package com.shujia.scala

import java.io.{BufferedInputStream, BufferedReader, FileInputStream, InputStreamReader}

import scala.io.Source

object Demo8List {
  def main(args: Array[String]): Unit = {
    //不可变的集合，有序（插入顺序），不唯一
    val list = List(2, 2, 6, 4, 5, 6, 7, 8, 9)
    println(list)

    println(list.mkString("\t")) //指定分隔符构建字符串
    println(list.head) //取头
    println(list.tail) //返回不包含头的所有元素
    println(list.take(3)) //取前面多少个元素
    println(list.takeRight(3)) //取后面多少个元素

    def f(x: Int): Boolean = {
      x % 2 != 0
    }

    println(list)
    println(list.takeWhile(x => x % 2 == 0)) //遇false 跳过，不再取后面的元素
    println(list.takeWhile(f))
    //遍历
    list.foreach(print) //遍历
    println("=" * 20)
    list.foreach(x => print(x))
    println("=" * 20)
    list.foreach(x => {
      print(x)
    })
    println("=" * 20)


    println(list.distinct) //去重
    println(list.sum)
    println(list.max)
    println(list.min)
    println(list.reverse) //反转

    //sortBy  指定某个字段进行排序    默认升序
    println(list.sortBy(x => x)) //升序
    println(list.sortBy(x => -x)) //倒序


    //sortWith 指定排序规则排序
    //x  代表前一个元素
    //y  代表后一个元素
    println(list.sortWith((x, y) => x < y)) //小的排前面
    println(list.sortWith((x, y) => x > y)) //大的排前面


    //java 读取文件
    /*val inputStream = new FileInputStream("scala/data/students.txt")
    val reader = new InputStreamReader(inputStream)
    val bufferedReader = new BufferedReader(reader)
    var line = bufferedReader.readLine()
    while (line != null) {
      println(line)
      line = bufferedReader.readLine()
    }*/

    //scala 读取文件

    val source = Source.fromFile("scala/data/students.txt")
    val lines = source.getLines() //获取所有行，返回一个迭代器
    //lines.foreach(println) //循环完了 迭代器里面就没有东西了
    println("=" * 100)

    val students = lines.toList


    //链式调用
    students.take(2).foreach(println)

    //年龄升序排序
    students.sortBy(line => {
      val age = line.split(",")(2).toInt
      age
    }).foreach(println)

    println("=" * 100)

    //年龄倒序排序
    students.sortWith((x, y) => {
      val age1 = x.split(",")(2).toInt
      val age2 = y.split(",")(2).toInt
      age1 > age2
    }).foreach(println)


    //取出文科班的学生
    students
      //过滤，返回一个新的集合
      .filter(line => line.split(",")(4).startsWith("文科"))//如果传入的函数返回true保留这一样数据
      .foreach(println)


  }
}
