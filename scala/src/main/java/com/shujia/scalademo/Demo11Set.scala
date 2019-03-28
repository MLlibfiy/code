package com.shujia.scalademo

object Demo11Set {

  def main(args: Array[String]): Unit = {

    //无序，唯一，不可变
    val set = Set(1, 2, 3, 4, 5, 6)

    //获取所有奇数
    set.filter(x => x % 2 != 0).foreach(println)

    println("=" * 100)
    set.foreach(println)
    println("=" * 100)
    println(set.max)
    println(set.min)
    println(set.sum)

    println(set.tail)
    println(set.head)
    println(set.take(2))

    println("=" * 100)
    //set 没有排序的方法，可以转成list再排序
    //链式调用，每一次调用都会返回一个新的对象
    set.toList.sortBy(x => x).foreach(println)

    val set1 = Set(1, 2, 3, 4, 5, 6)
    val set2 = Set(4, 5, 6, 7, 8, 9)

    println(set1.&(set2))//交集
    println(set1.&~(set2))//差集



    /**
      * 可变set
      */
    val set3 = scala.collection.mutable.Set[String]()
    println(set3)
    set3.add("java")
    println(set3)
    set3.add("scala")
    println(set3)




  }
}
