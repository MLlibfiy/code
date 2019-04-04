package com.shujia.mllib

import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}

import org.apache.spark.ml.clustering.KMeans

object TestKmeans {
  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setAppName("app").setMaster("local")

    val sc = new SparkContext(conf)

    val sQLContext = new SQLContext(sc)

    val dataset = sQLContext.createDataFrame(Seq(
      (1, Vectors.dense(0.0, 0.0)),
      (2, Vectors.dense(0.1, 0.1)),
      (3, Vectors.dense(0.2, 0.2)),
      (4, Vectors.dense(9.0, 9.0)),
      (5, Vectors.dense(9.1, 9.1)),
      (6, Vectors.dense(9.2, 9.2))
    )).toDF("id", "features")

    dataset.show()
    //使用kmeans  对数据进行聚类

    //构建算法
    val kmeans = new KMeans()
      .setFeaturesCol("features") //设置特征列
      .setK(2)//数据聚到几个类里面
      .setPredictionCol("prediction")


    //将数据带入算法，构建模型
    val model = kmeans.fit(dataset)

    model.clusterCenters.foreach(println)





  }
}
