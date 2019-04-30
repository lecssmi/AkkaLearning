package cn.shicheng.day_01

import akka.actor.{ActorSelection, ActorSystem}
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.Await
import scala.concurrent.duration._

/**
  * 本类应该用在另外一个工程，监听不同的端口。然后才能进行通信。
  * 假设监听2553端口。
  */
object RemoteDevice1 {

  def main(args: Array[String]): Unit = {


    val system = ActorSystem()

    //获取2552端口，也就是另外一个ActorSystem里面的actor
    val select: ActorSelection = system.actorSelection("akka.tcp://default@127.0.0.1:2552/user/parentActor")


    println(select)

//引入相关的包，使用？返回future对象，得到结果
    implicit val timeout:Timeout=Timeout(3 seconds)
    val future=select ? "child fail"

    val result: String = Await.result(future,timeout.duration).asInstanceOf[String]

    println(result)



  }

}
