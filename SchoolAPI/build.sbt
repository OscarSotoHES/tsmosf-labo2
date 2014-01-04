name := "SchoolAPI"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  "mysql" % "mysql-connector-java" % "5.1.27",
  "com.github.mumoshu" %% "play2-memcached" % "0.3.0.2",
  "com.typesafe.play" %% "play-cache" % "2.2.0" withSources
)     

resolvers ++= Seq(
        "Local Maven Repository" at "file:///"+Path.userHome.absolutePath+"/.m2/repository",
    //"Local Maven Repository" at "file:///e:/_shared/_repository/maven/3.x.x",
    "Spy Repository" at "http://files.couchbase.com/maven2"
)

play.Project.playJavaSettings
