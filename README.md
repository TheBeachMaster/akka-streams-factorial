# Akka-Streams Example - (Factorials)

## Running 
 + Configure SBT as per [the docs](https://www.scala-sbt.org/1.x/docs/Installing-sbt-on-Linux.html)
 + Navigate to project root directory and run
    - `cd $WHERE-PROJECT-IS-CLONED`
    - ```shell 
        $ sbt clean 
        $ sbt compile
        $ sbt run 
      ``` 
 + Possible errors 
    - Sbt not installed propely
    - Packages not installed poperly (`rm -fr ~/.sbt`)
    - User does not have permission to create files in current directory
    

## Reference 
[Akka Docs](https://doc.akka.io/docs/akka/2.5/stream/stream-quickstart.html)
