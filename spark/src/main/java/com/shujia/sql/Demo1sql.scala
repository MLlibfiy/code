package com.shujia.sql

import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}

object Demo1sql {
  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setAppName("sql").setMaster("local")
    val sc = new SparkContext(conf)

    //创建sql 上下文对象
    val sqlContext = new SQLContext(sc)

    //导入隐式转换
    import sqlContext.implicits._


    /**
      * 通过读取json数据创建DF
      *
      */
    val df = sqlContext.read.json("spark/data/student.json")


    //show 相当于action算子
    df.show()

    //打印表结构
    df.printSchema()


    //df 常用方法

    //select  转换
    df.select("name").show()

    df.select(df("name"), df("age")).show()


    //获取年龄大于22的学生
    df.filter(df("age") > 22).show()

    df.filter("age > 22").show()


    //计算每个年龄段学生的总数
    df.groupBy("age").count().show()


    /**
      *
      * 将df注册成一张临时表
      *
      */
    df.registerTempTable("student")


    //写sql
    sqlContext.sql("select age,count(1) from student group by age").show()


    /**
      * parquet  文件
      * 自带列描述
      *
      */

    df.write.format("parquet").save("spark/data/student.parquet")


    //读取parquet

    val pdf = sqlContext.read.load("spark/data/student.parquet")

    pdf.printSchema()
    pdf.show()



  }
}
