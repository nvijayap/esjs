import sbt._

object Dependencies {
  val excludeCglib = ExclusionRule(organization = "org.sonatype.sisu.inject")
  val excludeJackson = ExclusionRule(organization = "org.codehaus.jackson")
  val excludeScalaTest = ExclusionRule(organization = "org.scalatest")
  val excludeScala= ExclusionRule(organization = "org.scala-lang")
  val excludeNettyIo = ExclusionRule(organization = "io.netty", artifact= "netty-all")
  val excludeAsm = ExclusionRule(organization = "asm")
  val excludeQQ = ExclusionRule(organization = "org.scalamacros")

  // CDH 5.5.2 comes with akka 2.2.3-shaded-protobuf, which is built against com.typesafe.config-1.0.2,
  // hence we need to downgrade here
  // USED TO BE: 1.2.1
  lazy val typeSafeConfigDeps = "com.typesafe" % "config" % "1.0.2"
  lazy val yammerDeps = "com.yammer.metrics" % "metrics-core" % "2.2.0"

  lazy val yodaDeps = Seq(
    "org.joda" % "joda-convert" % "1.2",
    "joda-time" % "joda-time" % "2.2"
  )

  // CDH 5.5.2 comes with akka 2.2.3-shaded-protobuf preinstalled, so we should build against that version.
  val akkaVersion = "2.2.3-shaded-protobuf"

  // io.spray 1.2.3 pulls in a newer akka version than the one included in CDH 5.4. We neeed to downgrade
  // io.spray to 1.2.3 so we can use the akka version included in CDH 5.5.2.
  // USED TO BE: 1.3.2
  val sprayVersion = "1.2.3"

  lazy val akkaDeps = Seq(
    // Akka is provided because Spark already includes it,
    // and Spark's version is shaded so it's not safe to use this one
    "com.typesafe.akka" % "akka-actor_2.10" % "2.2.3",
    // "org.spark-project.akka" %% "akka-slf4j" % akkaVersion % "provided",
    "com.typesafe.akka" % "akka-slf4j_2.10" % "2.2.3",
    "org.slf4j" % "slf4j-log4j12" % "1.7.21",
    "io.spray" %% "spray-json" % sprayVersion,
    "io.spray" % "spray-can" % sprayVersion,
    "io.spray" % "spray-routing" % sprayVersion,
    "io.spray" % "spray-client" % sprayVersion,
    "io.spray" % "spray-servlet" % sprayVersion,
    yammerDeps
  ) ++ yodaDeps

  val mesosVersion = sys.env.getOrElse("MESOS_VERSION", "0.25.0-0.2.70.ubuntu1404")

  val sparkVersion = sys.env.getOrElse("SPARK_VERSION", "1.5.0-cdh5.5.2")
  lazy val sparkDeps = Seq(
    "org.apache.spark" %% "spark-core" % sparkVersion % "provided" excludeAll(excludeNettyIo, excludeQQ),
    // Force netty version.  This avoids some Spark netty dependency problem.
    "io.netty" % "netty-all" % "4.0.23.Final"
  )

  lazy val scalaLib = if (scala.util.Properties.versionString.split(" ")(1).startsWith("2.10"))
      Seq("org.scala-lang" % "scala-library" % "2.10.4")
    else Seq()

  lazy val sparkExtraDeps = Seq(
    "org.apache.spark" %% "spark-mllib" % sparkVersion % "provided" excludeAll(excludeNettyIo, excludeQQ),
    "org.apache.spark" %% "spark-sql" % sparkVersion % "provided" excludeAll(excludeNettyIo, excludeQQ),
    "org.apache.spark" %% "spark-streaming" % sparkVersion % "provided" excludeAll(excludeNettyIo, excludeQQ),
    "org.apache.spark" %% "spark-hive" % sparkVersion % "provided" excludeAll(excludeNettyIo, excludeQQ, excludeScalaTest)
  ) ++ scalaLib

  lazy val slickDeps = Seq(
    "com.typesafe.slick" %% "slick" % "2.1.0",
    "com.h2database" % "h2" % "1.3.170",
    "commons-dbcp" % "commons-dbcp" % "1.4"

  )

  lazy val logbackDeps = Seq(
    "ch.qos.logback" % "logback-classic" % "1.0.7"
  )

  lazy val coreTestDeps = Seq(
    "org.scalatest" %% "scalatest" % "2.2.1" % "test",
    "org.spark-project.akka" %% "akka-testkit" % akkaVersion % "test",
    "io.spray" % "spray-testkit" % sprayVersion % "test"
  )

  lazy val securityDeps = Seq(
     "org.apache.shiro" % "shiro-core" % "1.2.4"
  )

  lazy val serverDeps = apiDeps ++ yodaDeps
  lazy val apiDeps = sparkDeps :+ typeSafeConfigDeps

  val repos = Seq(
    "Typesafe Repo" at "http://repo.typesafe.com/typesafe/releases/",
    "sonatype snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/",
    "spray repo" at "http://repo.spray.io",
    "cdh repo" at "https://repository.cloudera.com/artifactory/cloudera-repos/",
    "conjars repo" at "http://conjars.org/repo/"
  )
}
