package cn.shicheng.day_01

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.util.Timeout

import scala.concurrent.Await

import scala.concurrent.Await
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.duration._



/**
  * 容错、异常捕获
  *
  * 1.actor如果失败，会由它的上一级重启。
  * 2.sender()函数能够调用发送该消息的上一个actor的ref。
  */
object FailureHander {




  def main(args: Array[String]): Unit = {

    //如果环境不给名称，默认default
    val system = ActorSystem()


    //actor的名称必须符合命名规范，毕竟是用要用作URL拼接
    val parent: ActorRef = system.actorOf(SupervisingActor.props,"parentActor")

    //同一个名字的actor必须单例，就算是同一种actor，不同对象之间也要不同名称。后面的数字可以忽略。
//    val parent2: ActorRef = system.actorOf(SupervisingActor.props,"parentActor")

    println(s"parent $parent")


    implicit val timeout=Timeout(3 seconds)


    val future=parent    ? "child fail"

    val result: String = Await.result(future,timeout.duration).asInstanceOf[String]

    println(result)










//    system.terminate()




  }

}

object SupervisedActor{


  def props:Props={

    Props(new SupervisedActor)
  }
}

object SupervisingActor{


  def props:Props={

    Props(new SupervisingActor)
  }

}


class SupervisingActor extends  Actor{


  val chind=context.actorOf(SupervisedActor.props,"chindActor")

  println(s"child ref $chind")






  override def preStart(): Unit = {

    println("parent start")
  }

  override def postStop(): Unit = {


    println("parent stop")
  }

  override def receive: Receive = {


    case "child fail"=>chind ! "fail now"

          sender() ! "Ok got it "


    case "ok"=> println("my child is done ")

    case e:RuntimeException=>println("my child has encountered a exception , the msg is :"+e.getMessage)

  }
}
class SupervisedActor extends Actor{





  override def preStart(): Unit = {

    println("child start")
  }

  override def postStop(): Unit = {

    println("child stop")
  }

  override def receive: Receive = {


    case "fail now"=>{

      sender() ! new RuntimeException("I failed when i got the message")

    }



  }
}




