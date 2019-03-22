package com.shujia.scala

object Demo12Match {
  def main(args: Array[String]): Unit = {


    /**
      * 模式匹配
      * 可以匹配基本数据类型，对象，数据类型
      * 只有有一个 case 满足条件就结束
      */

    val a = 1

    //匹配基本数据类型
    a match {
      case 1 => println("one")
      case 2 => println("two")
      case _ => println("no") //_  通配符
    }


    val dog = Dog("布丁", 3)

    //匹配对象
    dog match {
      case Dog("布丁", 3) => println("布丁")
      case Dog("花花", 2) => println("布丁")
      case _ => println("布丁")
    }


    //匹配数据类型
    def mat(o: Any) = {
      o match {
        case dog: Dog => println("这是一条狗:" + dog)
        case s: String => println("这是一个字符串" + s)
        case _ => println("我也不知道是个啥")
      }
    }

    mat(dog)
    mat("asd")
    mat(1)

    println("=" * 100)

    //match 使用   避免出现空指针异常
    val map = Map("1500100007" -> "尚孤风", "1500100009" -> "沈德昌")
    //println(map("15001000010")) 如果没有会出现异常
    val v = map.get("1500100008") match {
      case s: Some[String] => s.get //获取some 里面的值，返回
      case None => "默认值" //如果可以不存在返回一个默认重
    }
    println(v)

    println("=" * 100)
    val dogs = List(Dog("布丁", 3), Dog("花花", 2), Dog("小黑", 3))

    //取出布丁和花花
    //filter  返回true保留，返回false 过滤
    dogs
      .filter(dog => dog.name.equals("布丁") || dog.name.equals("花花"))
      .foreach(println)

    println("=" * 100)

    dogs.filter(dog => dog match {
      case Dog("布丁", 3) => true
      case Dog("花花", 2) => true
      case _ => false
    }).foreach(println)


    println("=" * 100)

    dogs.filter(_ match {
      case Dog("布丁", 3) => true
      case Dog("花花", 2) => true
      case _ => false
    }).foreach(println)


  }
}


case class Dog(name: String, age: Int)
