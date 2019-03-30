package com.shujia.sql

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.{Row, SQLContext}
import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}

object Demo9UDAF {

  /**
    * 自定义聚合函数
    *
    */

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setMaster("local[4]").setAppName("app")
    conf.set("spark.sql.shuffle.partitions", "4")
    val sc = new SparkContext(conf)
    /**
      * 操作hive的上下文对象
      * 底层是使用hive解析sql
      *
      */
    val sqlContext = new SQLContext(sc)

    val rdd = sc.textFile("spark/data/dianxin_data")
    val rowRDD = rdd
      .map(_.split(","))
      .filter(line => !"\\N".equals(line(4)))
      .map(line => Row(line(0), line(1), line(2), line(3), line(4).toInt, line(5), line(6), line(7)))


    /**
      * 通过构建列描述构建dataframe
      *
      */
    val schema = StructType(Array(
      StructField("mdn", StringType, true),
      StructField("grid_id", StringType, true),
      StructField("city_id", StringType, true),
      StructField("county_id", StringType, true),
      StructField("duration", IntegerType, true),
      StructField("grid_first_time", StringType, true),
      StructField("grid_last_time", StringType, true),
      StructField("day_id", StringType, true)
    ))

    val df = sqlContext.createDataFrame(rowRDD, schema)


    //注册成一张临时表

    /**
      * sqlContext和hiveContext 注册的临时表不再同一个作用域里面
      *
      */

    df.registerTempTable("staypoint")



    //注册自定义聚合函数
    sqlContext.udf.register("stringCount",new StringCount())


    sqlContext.sql("select city_id,stringCount(city_id) from staypoint group by  city_id").show()





  }
}
