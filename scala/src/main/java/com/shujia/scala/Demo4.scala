package com.shujia.scala

object Demo4 {

  def main(args: Array[String]): Unit = {
    val location = new Location(10, 5)

    println(location)

    location.move(20,50)

    location.print()
  }
}


//特征，相当于java里面的抽象类
trait Point {
  //抽象方法，子类必须实现
  def move(mx: Int, my: Int)

  //普通方法
  def print(): Unit = {
    println("普通方法")
  }
}




/**
  *
  *
  * 在scala里面继承类和实现接口都是一个关键字
  *
  * java 只能单继承，多实现
  * scala 可以多继承    继承的多个类里面不能有重复的方法
  *
  */

class Location(xc: Int, yc: Int) extends Point with java.io.Serializable {

  //: Int  强制标准变量的类型
  var x: Int = xc
  var y: Int = yc

  //当函数没有返回值的时候可以省略Unit

  override def move(mx: Int, my: Int) = {
    this.x = this.x + mx
    this.y = this.y + my

    println(s"移动后的横坐标为：$x")
    println(s"移动后的纵坐标为：$y")
  }

  override def toString: String = s"$x\t$y"
}