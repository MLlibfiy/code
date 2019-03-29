package com.shujia.sql

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SQLContext

object Demo3DfToRDD {
  def main(args: Array[String]): Unit = {

    /**
      * 通过RDD创建df
      *
      */

    val conf = new SparkConf().setAppName("sql").setMaster("local")
    val sc = new SparkContext(conf)

    //创建sql 上下文对象
    val sqlContext = new SQLContext(sc)


    //读取parquet

    val df = sqlContext.read.parquet("spark/data/student.parquet")


    //df 转RDD 每一行是一个ROW对象
    val rdd = df.rdd


    rdd.map(row => {

      //从row对象里面获取数据，可以通过下表获取，也可以通过列名获取

      val id = row.getAs[String]("id")
      val name = row.getAs[String]("name")
      val age = row.getAs[Long]("age")
      val gender = row.getAs[String]("gender")
      val clazz = row.getAs[String]("clazz")


      s"$id\t$name\t$age\t$gender\t$clazz\t"
    }).foreach(println)


  }
}
