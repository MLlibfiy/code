package com.shujia.scalademo

import scala.io.Source

object StudentDemo {
  def main(args: Array[String]): Unit = {

    //读取学生表的数据
    val student = Source.fromFile("scala/data/students.txt")
      .getLines()
      .toList
      .map(_.split(","))
      .map(line => Student3(line(0), line(1), line(2).toInt, line(3), line(4)))

    //读取分数表
    val score = Source.fromFile("scala/data/score.txt")
      .getLines()
      .toList
      .map(_.split(","))
      .map(line => Score(line(0), line(1), line(2).toInt))

    //读取科目表
    val cource = Source.fromFile("scala/data/cource.txt")
      .getLines()
      .toList
      .map(_.split(","))
      .map(line => Cource(line(0), line(1), line(2).toInt))


    /**
      * map实现三表关联
      *
      */

    val studentMap = student.map(s => (s.id, s)).toMap

    val courceMap = cource.map(c => (c.id, c)).toMap

    val stuInfo = score.map(sco => {
      //获取学生信息
      val stu = studentMap(sco.sId)
      //获取科目信息
      val cou = courceMap(sco.cId)
      s"${stu.id}\t${stu.name}\t${stu.age}\t${stu.gender}\t${stu.clazz}\t${cou.name}\t${sco.score}\t${cou.score}"
    })


    //取出年级前十学生的学号
    val top10StuId = stuInfo
      .map(_.split("\t"))
      .map(line => (line(0), line(6).toInt)) //取出学号和分数
      .groupBy(_._1) //按照学号分组
      .toList //分完组之后是一个map  不能排序，所以需要转成list
      .map(s => (s._1, s._2.map(_._2).sum)) //计算每个学生总分
      .sortBy(-_._2) //总分倒序排序
      .take(10) //取前十
      .map(_._1) //只需要学号


    //获取前十学生的基本信息
    stuInfo.filter(line => {
      val sid = line.split("\t")(0)
      top10StuId.contains(sid)
    }) //.foreach(println)


    /**
      *
      * 查询偏科最严重的前100名学生  [学号，姓名，班级，科目，分数]
      *
      */

    /**
      * 通过计算方差评估学生的偏科程度
      *
      */


    /**
      * 计算学生偏科程度
      *
      */

    // 1、对学生的分数做归一化
    val stuids = stuInfo.map(stu => {
      val stuif = stu.split("\t")
      val score = stuif(6).toDouble
      val scoreSum = stuif(7).toDouble
      val newScore = score / scoreSum
      (stuif(0), newScore)
    })
      //计算学生分数的分差
      .groupBy(_._1)
      .toList
      .map(stu => {
        val sid = stu._1
        val scores = stu._2.map(_._2)
        //归一化的分数
        val m = scores.sum / scores.length //计算学生分数的平均值

        /**
          * 计算方差
          */
        val variance = scores.map(score => Math.pow(m - score, 2)).sum / scores.length


        (sid, variance)

      }).sortBy(-_._2)
      .take(100)
      .map(_._1)


    stuInfo.filter(line => {
      val sid = line.split("\t")(0)
      stuids.contains(sid)
    }) .foreach(println)

  }


  case class Student3(id: String, name: String, age: Int, gender: String, clazz: String)

  case class Score(sId: String, cId: String, score: Int)

  case class Cource(id: String, name: String, score: Int)

}
