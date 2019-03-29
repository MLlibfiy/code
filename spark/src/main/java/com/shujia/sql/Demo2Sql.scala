package com.shujia.sql

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SQLContext

object Demo2Sql {
  def main(args: Array[String]): Unit = {

    /**
      * 通过RDD创建df
      *
      */

    val conf = new SparkConf().setAppName("sql").setMaster("local")
    val sc = new SparkContext(conf)

    //创建sql 上下文对象
    val sqlContext = new SQLContext(sc)

    //导入隐式转换
    import sqlContext.implicits._


    val rdd = sc.textFile("spark/data/student.txt")


    //通过rdd创建df

    val df = rdd
      .map(_.split(","))
      .map(line => Student(line(0), line(1), line(2).toInt, line(3), line(4)))
      .toDF()//需要导入隐式转换  sqlContext.implicits._

    df.printSchema()
    df.show()

    df.registerTempTable("student")


    sqlContext.sql("select age,count(1) from student group by age").show()



  }

  case class Student(id: String, name: String, age: Int, gender: String, clazz: String)

}
