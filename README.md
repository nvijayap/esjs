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
$ curl -d "" 'dayrhebfmd002:8080/sjs/contexts/test-context?num-cpu-cores=4&memory-per-node=512m'
{
  "status": "CONTEXT INIT ERROR",
  "result": {
    "message": "interface akka.actor.Scheduler is not assignable from class akka.actor.LightArrayRevolverScheduler",
    "errorClass": "java.lang.ClassCastException",
```

