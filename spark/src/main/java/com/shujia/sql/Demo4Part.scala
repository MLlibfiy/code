package com.shujia.sql

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SQLContext

object Demo4Part {
  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setAppName("sql").setMaster("local")
    val sc = new SparkContext(conf)

    //创建sql 上下文对象
    val sqlContext = new SQLContext(sc)


    import sqlContext.implicits._

    val rdd = sc.textFile("spark/data/student.txt")


    //通过rdd创建df

    val df = rdd
      .map(_.split(","))
      .map(line => Student(line(0), line(1), line(2).toInt, line(3), line(4)))
      .toDF()//需要导入隐式转换  sqlContext.implicits._


    df
      .filter("gender = '男'")
      .write
      .parquet("spark/data/student/gender=男")//分区保存数据


    df
      .filter("gender = '女'")
      .write
      .parquet("spark/data/student/gender=女")




    //读取分区表
    val pdf = sqlContext.read.parquet("spark/data/student")
    pdf.printSchema()
    pdf.show()

  }
}
