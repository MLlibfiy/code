package com.shujia.sql

import java.util.Properties

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.{SQLContext, SaveMode}

object Demo10Mode {
  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setMaster("local[4]").setAppName("app")
    conf.set("spark.sql.shuffle.partitions", "4")
    val sc = new SparkContext(conf)
    val sqlContext = new SQLContext(sc)

    import sqlContext.implicits._
    /**
      * 通过读取json数据创建DF
      *
      */
    val df = sqlContext.read.json("spark/data/student.json")


    /**
      * SaveMode.ErrorIfExists (default)  默认值，如果已存在，报错
      * SaveMode.Append  追加
      * SaveMode.Overwrite   覆盖
      * SaveMode.Ignore  忽略
      *
      */

    df.filter("gender='女'")
      .write
      .mode(SaveMode.Ignore)
      .parquet("spark/data/student")

    //保存为json 格式的数据
    df.write.mode(SaveMode.Overwrite).json("spark/data/student1")

    val properties = new Properties()
    properties.setProperty("user","root")
    properties.setProperty("password","123456")


    //将数据写到外部数据库
    df.write.mode(SaveMode.Append).jdbc("jdbc:mysql://localhost:3306/student","student2",properties)


  }
}
