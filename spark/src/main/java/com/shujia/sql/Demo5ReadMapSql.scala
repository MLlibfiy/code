package com.shujia.sql

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SQLContext

object Demo5ReadMapSql {
  def main(args: Array[String]): Unit = {

    /**
      * 通过RDD创建df
      *
      */

    val conf = new SparkConf().setAppName("sql").setMaster("local")
    val sc = new SparkContext(conf)

    //创建sql 上下文对象
    val sqlContext = new SQLContext(sc)


    val mysqlDF = sqlContext
      .read
      .format("jdbc")//通过jdbc创建df
      .options(Map(
        "url" -> "jdbc:mysql://localhost:3306",
        "driver" -> "com.mysql.jdbc.Driver",
        "dbtable" -> "student.student",
        "user" -> "root",
        "password" -> "123456"
      )).load()


    mysqlDF.show()

  }
}
