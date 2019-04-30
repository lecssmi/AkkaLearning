package cn.shicheng.day_01

import akka.actor.{Actor, ActorRef, ActorSystem, Props}

/**
  * actor的创建、消息发送、层级关系
  *
  * 1.ActorSystem是外部system接口，在actor内部使用context。应该是一个对象的封装。
  * 2.手动创建的actor最多在akka://system/user/下面
  * 3.如果是在一个actor里面创建的actor，就会有层级关系。
  */
object PrintMyActorRefActor {



  def props:Props={


    Props(new PrintMyActorRefActor())

  }


}


class PrintMyActorRefActor extends  Actor{





  var start:Long =0

  override def preStart(): Unit = {


    start=System.nanoTime()
  }

  override def postStop(): Unit = super.postStop()




  override def receive: Receive = {


    case "print it"=>{


      //这里的Props是创立Actor的参数，如果是空，属性就是是默认的，对象应该也是默认的一种。
      //这里因为是在receive方法里面初始化的，说明是在一个互动的actor对象内部被创建的一个actor，所以在父类actor下面。
      val ref: ActorRef = context.actorOf(Props.empty,"second-actor")

      println(s"Second :$ref")

//      start=System.currentTimeMillis()


    }
    case "stop it"=>{

      println("life cycle:"+ (System.nanoTime()-start))
      context.stop(self)

    }



  }

}

object  MyApp {


  def main(args: Array[String]): Unit = {

      //创建系统环境
     val system = ActorSystem("testSystem")

    //指定使用某种actor类生成actor对象，返回引用
     val firstRef: ActorRef = system.actorOf(Props(new PrintMyActorRefActor),"first-actor")



    println(s"first ref $firstRef")

    //通过引用，发送消息
    firstRef ! "print it"

    firstRef ! "stop it"

    //关闭系统
    system.terminate()






  }


}