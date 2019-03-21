package com.shujia.scala

/**
  * 类和对象
  *
  */
object Demo3 {
  def main(args: Array[String]): Unit = {
    val p = new Person("张三", 23)
    println(p.toString)

    println(p.name)
    println(p.age)
    p.name = "李四"
    println(p)


    //在scala代码里面使用java的类，方法都一样
    val s = new Student("张三", 23, "一班")

    println(s)

    val s1 = new Student1("王五",25,"二班")
    println(s1)



  }

}

//类的属性默认val修饰，不可变，当使用var修饰之后可以改变
class Person(var name: String, var age: Int) {
  //override 重写父类方法
  override def toString: String = s"$name\t$age"

  //如果不适用var修饰属性，可以提供接口
  def getName(): String = name

  def setName(name: String) = this.name = name

}


class Student1(name: String, age: Int, clazz: String) extends Person(name, age) {
  override def toString: String = s"$name\t$age\t$clazz"
}

