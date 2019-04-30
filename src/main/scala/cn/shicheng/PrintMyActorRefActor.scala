package cn.shicheng

import akka.actor.{Actor, ActorRef, ActorSystem, Props}

/**
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

     val system = ActorSystem("testSystem")

     val firstRef: ActorRef = system.actorOf(Props(new PrintMyActorRefActor),"first-actor")



    println(s"first ref $firstRef")

    firstRef ! "print it"

    firstRef ! "stop it"






  }


}