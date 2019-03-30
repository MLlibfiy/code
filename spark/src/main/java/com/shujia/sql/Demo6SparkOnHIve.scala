package com.shujia.sql

import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.Row
import org.apache.spark.sql.hive.HiveContext
import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}


object Demo6SparkOnHIve {
  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setMaster("local[4]").setAppName("app")
    conf.set("spark.sql.shuffle.partitions","4")
    val sc = new SparkContext(conf)

    val sqlContext = new SQLContext(sc)



    /**
      * 操作hive的上下文对象
      * 底层是使用hive解析sql
      *
      */
    val hiveContext = new HiveContext(sc)

    import sqlContext.implicits._


    /**
      * mdn	        	string	用户手机号码
      * grid_id 		string	停留点所在电信内部网格号
      * city_id			string	业务发生城市id
      * county_id		string	停留点区县
      * duration		string	机主在停留点停留的时间长度（分钟）,lTime-eTime
      * grid_first_time	string	网格第一个记录位置点时间（秒级）
      * grid_last_time	string	网格最后一个记录位置点时间（秒级）
      * day_id			string	天分区
      */

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

    val df = hiveContext.createDataFrame(rowRDD, schema)


    //注册成一张临时表

    /**
      * sqlContext和hiveContext 注册的临时表不再同一个作用域里面
      *
      */

    df.registerTempTable("staypoint")


    /**
      * 统计每个城市停留时间最长的前十个游客
      *
      */

    //计算每个用户总的停留时间
    hiveContext
      .sql("select  mdn,city_id ,sum(duration) as sumtime from staypoint group by mdn,city_id ")
      .registerTempTable("sumtimetable")


    /**
      * spark sql 不支持  row_number 语法
      *
      */
    //    sqlContext
//      .sql("select *, row_number() over(partition by  city_id order by sumtime desc ) as rank from sumtimetable ")
//      .show()



    hiveContext
      .sql("select * from (select *, row_number() over(partition by  city_id order by sumtime desc ) as rank from sumtimetable) as a where rank<=10")
      .show(1000)

  }
}
