# esjs (embedded sjs)

* This repo is based on https://github.com/spark-jobserver/spark-jobserver (SJS)

* In this repo, an attempt is made to embed SJS in a Servlet Container like Tomcat or Jetty

* Here's what's working up until now ...

* SJS can be deployed to Tomcat

* SJS UI can be seen by accessing Tomcat's SJS webapp (example: http://host:8080/sjs)

* Upload jar using curl and and view the uploaded jar using curl

* And, here's the issue faced as of now ...

* Unable to submit job to transient context ; Here's the issue faced ...

```
"message": "java.lang.ClassCastException: interface akka.actor.Scheduler is not assignable from class akka.actor.LightArrayRevolverScheduler"
```

* Unable to start a new context ; Same issue as above ...

```
$ curl -d "" 'host:8080/sjs/contexts/test-context?num-cpu-cores=4&memory-per-node=512m'
{
  "status": "CONTEXT INIT ERROR",
  "result": {
    "message": "interface akka.actor.Scheduler is not assignable from class akka.actor.LightArrayRevolverScheduler",
    "errorClass": "java.lang.ClassCastException",
```

* Here's the complete stacktrace ...

```
{
  "status": "ERROR",
  "result": {
    "errorClass": "java.lang.RuntimeException",
    "cause": "interface akka.actor.Scheduler is not assignable from class akka.actor.LightArrayRevolverScheduler",
    "stack": ["akka.actor.ReflectiveDynamicAccess$$anonfun$getClassFor$1.apply(DynamicAccess.scala:69)", "akka.actor.ReflectiveDynamicAccess$$anonfun$getClassFor$1.apply(DynamicAccess.scala:66)", "scala.util.Try$.apply(Try.scala:161)", "akka.actor.ReflectiveDynamicAccess.getClassFor(DynamicAccess.scala:66)", "akka.actor.ReflectiveDynamicAccess.createInstanceFor(DynamicAccess.scala:84)", "akka.actor.ActorSystemImpl.createScheduler(ActorSystem.scala:618)", "akka.actor.ActorSystemImpl.<init>(ActorSystem.scala:541)", "akka.actor.ActorSystem$.apply(ActorSystem.scala:111)", "akka.actor.ActorSystem$.apply(ActorSystem.scala:104)", "org.apache.spark.util.AkkaUtils$.org$apache$spark$util$AkkaUtils$$doCreateActorSystem(AkkaUtils.scala:121)", "org.apache.spark.util.AkkaUtils$$anonfun$1.apply(AkkaUtils.scala:53)", "org.apache.spark.util.AkkaUtils$$anonfun$1.apply(AkkaUtils.scala:52)", "org.apache.spark.util.Utils$$anonfun$startServiceOnPort$1.apply$mcVI$sp(Utils.scala:1912)", "scala.collection.immutable.Range.foreach$mVc$sp(Range.scala:141)", "org.apache.spark.util.Utils$.startServiceOnPort(Utils.scala:1903)", "org.apache.spark.util.AkkaUtils$.createActorSystem(AkkaUtils.scala:55)", "org.apache.spark.rpc.akka.AkkaRpcEnvFactory.create(AkkaRpcEnv.scala:253)", "org.apache.spark.rpc.RpcEnv$.create(RpcEnv.scala:53)", "org.apache.spark.SparkEnv$.create(SparkEnv.scala:252)", "org.apache.spark.SparkEnv$.createDriverEnv(SparkEnv.scala:193)", "org.apache.spark.SparkContext.createSparkEnv(SparkContext.scala:277)", "org.apache.spark.SparkContext.<init>(SparkContext.scala:450)", "spark.jobserver.context.DefaultSparkContextFactory$$anon$1.<init>(SparkContextFactory.scala:53)", "spark.jobserver.context.DefaultSparkContextFactory.makeContext(SparkContextFactory.scala:53)", "spark.jobserver.context.DefaultSparkContextFactory.makeContext(SparkContextFactory.scala:48)", "spark.jobserver.context.SparkContextFactory$class.makeContext(SparkContextFactory.scala:37)", "spark.jobserver.context.DefaultSparkContextFactory.makeContext(SparkContextFactory.scala:48)", "spark.jobserver.JobManagerActor.createContextFromConfig(JobManagerActor.scala:287)", "spark.jobserver.JobManagerActor$$anonfun$wrappedReceive$1.applyOrElse(JobManagerActor.scala:109)", "scala.runtime.AbstractPartialFunction$mcVL$sp.apply$mcVL$sp(AbstractPartialFunction.scala:33)", "scala.runtime.AbstractPartialFunction$mcVL$sp.apply(AbstractPartialFunction.scala:33)", "scala.runtime.AbstractPartialFunction$mcVL$sp.apply(AbstractPartialFunction.scala:25)", "ooyala.common.akka.ActorStack$$anonfun$receive$1.applyOrElse(ActorStack.scala:33)", "scala.runtime.AbstractPartialFunction$mcVL$sp.apply$mcVL$sp(AbstractPartialFunction.scala:33)", "scala.runtime.AbstractPartialFunction$mcVL$sp.apply(AbstractPartialFunction.scala:33)", "scala.runtime.AbstractPartialFunction$mcVL$sp.apply(AbstractPartialFunction.scala:25)", "ooyala.common.akka.Slf4jLogging$$anonfun$receive$1$$anonfun$applyOrElse$1.apply$mcV$sp(Slf4jLogging.scala:26)", "ooyala.common.akka.Slf4jLogging$class.ooyala$common$akka$Slf4jLogging$$withAkkaSourceLogging(Slf4jLogging.scala:35)", "ooyala.common.akka.Slf4jLogging$$anonfun$receive$1.applyOrElse(Slf4jLogging.scala:25)", "scala.runtime.AbstractPartialFunction$mcVL$sp.apply$mcVL$sp(AbstractPartialFunction.scala:33)", "scala.runtime.AbstractPartialFunction$mcVL$sp.apply(AbstractPartialFunction.scala:33)", "scala.runtime.AbstractPartialFunction$mcVL$sp.apply(AbstractPartialFunction.scala:25)", "ooyala.common.akka.ActorMetrics$$anonfun$receive$1.applyOrElse(ActorMetrics.scala:24)", "akka.actor.ActorCell.receiveMessage(ActorCell.scala:498)", "akka.actor.ActorCell.invoke(ActorCell.scala:456)", "akka.dispatch.Mailbox.processMailbox(Mailbox.scala:237)", "akka.dispatch.Mailbox.run(Mailbox.scala:219)", "akka.dispatch.ForkJoinExecutorConfigurator$AkkaForkJoinTask.exec(AbstractDispatcher.scala:386)", "scala.concurrent.forkjoin.ForkJoinTask.doExec(ForkJoinTask.java:260)", "scala.concurrent.forkjoin.ForkJoinPool$WorkQueue.runTask(ForkJoinPool.java:1339)", "scala.concurrent.forkjoin.ForkJoinPool.runWorker(ForkJoinPool.java:1979)", "scala.concurrent.forkjoin.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:107)"],
    "causingClass": "java.lang.ClassCastException",
    "message": "java.lang.ClassCastException: interface akka.actor.Scheduler is not assignable from class akka.actor.LightArrayRevolverScheduler"
  }
}
```
