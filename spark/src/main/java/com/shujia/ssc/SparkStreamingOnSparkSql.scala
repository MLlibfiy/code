package com.shujia.ssc

import java.util.Properties

import kafka.serializer.StringDecoder
import org.apache.hadoop.fs.Path
import org.apache.spark.SparkConf
import org.apache.spark.sql.{SQLContext, SaveMode}
import org.apache.spark.streaming.{Durations, StreamingContext}
import org.apache.spark.streaming.kafka.KafkaUtils

object SparkStreamingOnSparkSql {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
      .setMaster("local[2]")
      .setAppName("ssc")

    val ssc = new StreamingContext(conf, Durations.seconds(5))

    //direct模式保存消费偏移量
    ssc.checkpoint("spark/data/checkpoint")

    val kafkaParams = Map("metadata.broker.list" -> "node1:9092,node2:9092,node3:9092")

    //可以指定多个topic
    val topics = Set("topic")

    val kafkaDstream = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](
      ssc,
      kafkaParams,
      topics
    )


    /**
      * //DS  --->  RDD
      * foreachRDD   不是action  算子
      *
      * 每个batch  执行一次
      *
      */


    kafkaDstream
      .map(_._2)
      .foreachRDD(rdd => {
        //使用RDD API

        /**
          * direct  模式RDD分区的数量等于topic 分区的数量
          *
          *
          */
        println(s"rdd分区数量：${rdd.getNumPartitions}")

        //rdd.map((_, 1)).reduceByKey(_ + _).foreach(println)


        //获取SparkContext对象
        val sc = rdd.context

        //创建sqlContext上下文对象
        val sqlContext = SQLContext.getOrCreate(sc)

        //设置shuffle 平行度
        sqlContext.setConf("spark.sql.shuffle.partitions", "4")


        //导入隐式转换
        import sqlContext.implicits._


        /**
          * DS  --->  RDD  -----> DF
          */

        val DF = rdd
          .map(_.split(","))
          .map(line => Student(line(0), line(1), line(2).toInt, line(3), line(4)))
          .toDF

        //注册临时表
        DF.registerTempTable("student")


        //实时统计每个班级学生的人数
        var countDF = sqlContext.sql("select clazz ,count(1) as c from student group by clazz")


        //读取之前batch  保存的状态
        val statDF = sqlContext.read
          .format("jdbc") //通过jdbc创建df
          .options(Map(
          "url" -> "jdbc:mysql://localhost:3306",
          "driver" -> "com.mysql.jdbc.Driver",
          "dbtable" -> "student.stat",
          "user" -> "root",
          "password" -> "123456"
        )).load()

        //合并当前batch计算的结果 和之前batch 计算的结果
        var unionDF = countDF.rdd.
          union(statDF.rdd)
          .map(row => (row.getAs[String]("clazz"), row.getAs[Long]("c")))
          .reduceByKey(_ + _)
          .map(t => Stat(t._1, t._2))
          .toDF()

        //对多次使用的DF  进行缓存
        unionDF = unionDF.cache()

        unionDF.show()

        val properties = new Properties()
        properties.setProperty("user", "root")
        properties.setProperty("password", "123456")

        //保存状态到mysql
        unionDF.write.mode(SaveMode.Overwrite).jdbc("jdbc:mysql://localhost:3306/student", "stat", properties)


      })


    ssc.start()
    ssc.awaitTermination()
    ssc.stop()


  }
}

case class Student(id: String, name: String, age: Int, gender: String, clazz: String)

case class Stat(clazz: String, c: Long)