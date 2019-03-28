package com.shujia.scalademo

import scala.actors.Actor

/**
  * 在spark 2.0之前使用akka 实现分布式通信
  * akka 底层就是actor
  *
  */

object Demo18Actor {
  def main(args: Array[String]): Unit = {
    println("main已启动")

    val worker = new Worker()
    val master = new Master(worker)

    worker.start()
    master.start()

  }

}

//消息类
case class Message(actor: Actor, msg: String)

class Worker() extends Actor {

  override def act(): Unit = {
    println("Worker已启动")

    //接收消息

    while (true) {
      receive {
        case msg: Message => {
          println(msg.msg) //打印消息内容

          //回一个消息
          msg.actor ! "心跳消息已收到"

        }
      }
    }
  }
}

class Master(worker: Actor) extends Actor {

  override def act(): Unit = {
    println("master已启动")

    //异步发送消息给worker
    worker ! Message(this, "发送心跳请求")

    //接收woker 返回的消息
    while (true) {
      receive {
        case msg: String => println(msg)
      }
    }
  }
}