package com.test.akka.streams.main

import akka.{ NotUsed, Done }
import akka.actor.ActorSystem
import akka.stream._
import akka.stream.scaladsl._
import akka.util.ByteString

import scala.concurrent._
import scala.concurrent.duration._

import java.nio.file.Paths

object Main extends App {
    implicit val system       = ActorSystem("TestStreams")
    implicit val materializer = ActorMaterializer()

    val source: Source[Int, NotUsed] = Source(1 to 100)

    // Dont
    // Run without termination as the Actor system does not terminate
    source.runForeach(i => println(i))(materializer)

    val factorials = source.scan(BigInt(1))((acc, next) => acc * next)

    val res: Future[IOResult] =
        factorials
            .map(num ⇒ ByteString(s"$num\n"))
            .runWith(FileIO.toPath(Paths.get("factorial.txt")))

    val result : Future[IOResult] =
        factorials
            .map(_.toString)
            .runWith(lineSink("factorials.txt"))

    factorials
      .zipWith(Source(0 to 100))((num, idx) ⇒ s"$idx! = $num")
      .throttle(1, 1.second)
      .take(3)
      .runForeach(println)

    // Do
    // Resolves when the stream finishes
    val done: Future[Done]  = source.runForeach(i => println(i))(materializer)

    implicit val ec = system.dispatcher
    done.onComplete(_ => system.terminate())

    def lineSink(filename: String): Sink[String, Future[IOResult]] =
        Flow[String]
            .map(s => ByteString(s + "\n"))
            .toMat(FileIO.toPath(Paths.get(filename)))(Keep.right)
}
