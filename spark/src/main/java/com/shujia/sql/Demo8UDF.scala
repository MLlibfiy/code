package com.shujia.sql

import org.apache.spark.sql.{Row, SQLContext}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}

object Demo8UDF {


  /**
    * 自定义函数
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


    //自定义函数 输入一行返回一行
    val getCityName = (cityId: String) => {
      //根据城市id获取城市名
      val cityName = SSXRelation.CODE_NAME.get(cityId)
      cityName
    }

    //注册自定义函数
    sqlContext.udf.register("getCityName", getCityName)


    //使用自定义函数   函数名使用
    sqlContext
      .sql("select *,getCityName(city_id) from staypoint")
      .show()


    //多个参数的自定义函数   最多可以22个参数
    val timeAndTime = (time1: String, time2: String) => {
      s"$time1-$time2"
    }

    //注册自定义函数
    sqlContext.udf.register("timeAndTime", timeAndTime)
    sqlContext
      .sql("select *,timeAndTime(grid_first_time,grid_last_time) from staypoint")
      .show()

  }
}
