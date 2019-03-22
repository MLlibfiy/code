package com.shujia.scala

object Demo9map {

  def main(args: Array[String]): Unit = {

    var map = Map("数加" -> "学院", "java" -> "scala")

    println(map("数加")) //更具key获取value，如果value不存在，报异常
    //println(map("hadoop"))
    println(map.get("数加").get) //如果key存在返回some   ,不存在返回none
    println(map.get("hadoop"))

    println("=" * 100)

    //返回所有key
    map.keys.foreach(println)
    println("=" * 100)
    map.keySet.foreach(println)
    println("=" * 100)
    //返回所有value
    map.values.foreach(println)


    /**
      * 在scala 中任何序列都可以相互转换   to
      *
      */

    //类型转换，返回一个新的对象
    map.values.toList.toIterator
    map.values.toSet
    map.values.toArray

    println("=" * 100)
    //遍历
    map.foreach(e => println(s"${e._1}\t${e._2}"))

    println("=" * 100)

    for (kv <- map) {
      val k = kv._1
      val v = kv._2
      println(s"$k\t$v")
    }

    println("=" * 100)

    for (key <- map.keys.toList) {
      val value = map(key)

      //println(key + "\t" + value)
      //println(s"$key\t$value")
      println(s"$key\t${map(key)}")

    }






  }
}
