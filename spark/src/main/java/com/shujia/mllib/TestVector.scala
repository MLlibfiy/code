package com.shujia.mllib

import org.apache.spark.mllib.linalg.{Matrices, Matrix, Vectors}
import org.apache.spark.mllib.regression.LabeledPoint

object TestVector {

  def main(args: Array[String]): Unit = {

    /**
      * 向量
      * 对集合存储和计算做了优化
      *
      *
      */

    //稠密向量
    val denVec = Vectors.dense(1.0, 2.0, 4.0, 6.0)

    println(denVec)

    //稀疏向量
    val spaVec = Vectors.sparse(5, Array(0, 3, 4), Array(1.0, 3.0, 5.0))

    println(spaVec)


    //一条训练集数据
    val pos = LabeledPoint(1.0, Vectors.dense(1.0, 0.0, 3.0))
    println(pos)


    val neg = LabeledPoint(0.0, Vectors.sparse(3, Array(0, 2), Array(1.0, 3.0)))
    println(neg)



    //稠密矩阵
    val dm: Matrix = Matrices.dense(3, 2, Array(1.0, 3.0, 5.0, 2.0, 4.0, 6.0))


    println(dm)

    //稀疏矩阵
    val sm: Matrix = Matrices.sparse(3, 2, Array(0, 1, 3), Array(0, 2, 1), Array(9, 6, 8))


    println(sm)
  }
}
