package com.shujia.scala

import scala.actors.Actor

object Demo17Actor {
  def main(args: Array[String]): Unit = {
    println("main启动")
    //创建线程对象
    val worker = new more()
    //启动线程
    worker.start()

    //发送消息给actor
    /**
      * 异步发送消息，不需要返回结果
      * 对方有没有接收到，不做任何处理
      *
      */
    worker ! 1
    worker ! 2
    worker ! "数加"


    /**
      * 同步发送消息
      * 需要一个响应
      * 返回一个可以被调用的方法
      */

    val result = worker !! "同步发消息"
    val r = result()
    println(r)


  }
}

/**
  * 继承actor 实现多线程
  *
  */
class more() extends Actor {
  /**
    * 当但与run方法
    */
  override def act(): Unit = {
    println("scala 多线程")

    //消息系统
    //接收消息
    while (true) {
      receive {
        case 1 => println("one")
        case 2 => println("two")
        case "同步发消息" => {
          println("接收了同步消息")

          //回一个消息
          sender ! "消息已经结束到"
        }
        case _ => println("no")
      }
    }
  }
}
