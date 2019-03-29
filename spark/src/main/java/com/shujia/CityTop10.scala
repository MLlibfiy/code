package com.shujia

import org.apache.spark.{SparkConf, SparkContext}

object CityTop10 {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setMaster("local").setAppName("app")
    val sc = new SparkContext(conf)

    val data = sc.textFile("spark/data/dianxin_data")
    data
      .map(_.split(","))
      .filter(line => !"\\N".equals(line(4))) //过滤脏数据
      .map(line => (s"${line(0)},${line(2)}", line(4).toInt))//以手机号和城市id作为key,停留时间作为value
      .reduceByKey(_ + _)//计算每个游客在一个城市里面的总的停留时间
      .map(t => {
        val mdn = t._1.split(",")(0)
        val city = t._1.split(",")(1)
        val stattime = t._2

        //以城市作为key,停留时间和手机号作为value
        (city, s"$mdn,$stattime")
      }).groupByKey()//根据城市id分组
      .flatMap(line => {//使用flatMap 每个游客返回一行数据
        val city = line._1
        //取出停留时间前十的游客
        val top10 = line._2.toList.sortBy(-_.split(",")(1).toInt).take(10)

        top10.map(line => s"$line,$city")
      }).foreach(println)


  }

}
