package com.shujia.scala

object Demo5 {
  def main(args: Array[String]): Unit = {
    val stu: Stu1 = Stu2("张三",23)
    println(stu)

    println(Stu3("张三",23))
  }
}

//样例类
case class Stu3(var name: String, var age: Int)


class Stu1(var name: String, var age: Int) {
  override def toString: String = s"Stu[$name\t$age]"
}

/**
  * 实现不用 new 关键字创建对象
  *
  */

//伴生对象
//object  调用方法不需要new ，object 本身就是一个对象
object Stu2 {

  //对象名加 () 调用
  def apply(name: String, age: Int): Stu1 = {
    //最后一行return可以省略
    return new Stu1(name, age)
  }

  def point = println("张三")
}
