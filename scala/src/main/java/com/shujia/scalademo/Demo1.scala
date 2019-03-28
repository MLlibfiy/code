package com.shujia.scalademo

object Demo1 {

  /**
    * def 关键字
    * main  函数名
    * args  方法的参数
    *
    * Unit  没有返回值 相当于void
    *
    */
  def main(args: Array[String]): Unit = {
    //底层调用了java的打印方法   PrintStream
    println("hello world")

    //var 定义变量
    var a = 10
    a = 20

    println(a.getClass)

    //val 常量，  不能修改，不能修改引用
    val b = 10
    //b = 20


    //算术运算
    println(a + b)
    println(a - b)
    println(a * b)
    println(a / b)
    println(a % b)

    println("=" * 100)

    //scala string 和java是同一个
    var str = "数加,科技"
    println(str.getClass)

    val arr = str.split(",")

    //mkString  把一个数组拼接成一个字符串
    // scala 任意一个序列都有这个方法
    // 隐式转换  ：  本来这个对象没有这个方法，但是scala  可以通过隐式转换动态的增加一个方法
    println(arr.mkString(","))

    val dis = "aaaaabbbb"
    //去重
    println(dis.distinct)
    println(dis.head) //取头
    println(dis.tail) //取不包含第一个元素的字符串
    println(dis.reverse) //反转

    val s = "牛逼"
    //$  相当于字符串拼接
    val str2 = s"数加$s"
    println(str2)


  }
}
