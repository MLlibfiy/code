package com.shujia.optimize

import java.sql.{Connection, DriverManager}

import com.mysql.jdbc.ConnectionGroupManager
import org.apache.spark.{SparkConf, SparkContext}

object TestForeachPartition {
  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setMaster("local").setAppName("app")

    val sc = new SparkContext(conf)

    val data = sc.textFile("spark/data/student.txt")


    /**
      * 将数据存到MySQL数据库
      *
      */


    /**
      * 所有和网络连接由关系的对象都不能序列化，不能再网络里面传输
      *
      */


    /**
      * 使用foreach  每一条数据都会创建一个jdbc 连接
      * 1、创建连接底层tcp   需要三次握手，比较耗时间
      * 2、太多了网络连接对象，会占用大量的内存
      *
      */

/*

    data.foreach(line => {

      //加载驱动
      Class.forName("com.mysql.jdbc.Driver")
      //建立连接
      val conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/student", "root", "123456")
      val stu = line.split(",")
      val sql = "insert into student values(?,?,?,?,?)"
      val stat = conn.prepareStatement(sql)

      stat.setString(1, stu(0))
      stat.setString(2, stu(1))
      stat.setInt(3, stu(2).toInt)
      stat.setString(4, stu(3))
      stat.setString(5, stu(4))

      stat.executeUpdate()
      stat.close()
      conn.close()
    })
*/


    /**
      * foreachPartition  action 算子，传入一个分区的数据
      *
      * 使用 foreachPartition  代替foreach    减少创建连接的次数
      *
      */


    data.foreachPartition(iter => {//iter  一个分区的数据，是一个迭代器，不会将数据全部拉到内存
      //加载驱动
      Class.forName("com.mysql.jdbc.Driver")
      //建立连接
      val conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/student", "root", "123456")

      //一个分区只创建一个jdbc 连接
      for (line <- iter) {
        val stu = line.split(",")
        val sql = "insert into student values(?,?,?,?,?)"
        val stat = conn.prepareStatement(sql)

        stat.setString(1, stu(0))
        stat.setString(2, stu(1))
        stat.setInt(3, stu(2).toInt)
        stat.setString(4, stu(3))
        stat.setString(5, stu(4))

        stat.executeUpdate()
        stat.close()
      }

      conn.close()


    })


  }
}
