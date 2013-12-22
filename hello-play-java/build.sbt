import play.Project._

name := """hello-play-java"""

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
	"org.hibernate" % "hibernate-entitymanager" % "3.6.9.Final",
	"mysql" % "mysql-connector-java" % "5.1.18",
	"org.eclipse.persistence" % "javax.persistence" % "2.0.0",
	"org.webjars" %% "webjars-play" % "2.2.0", 
	"org.webjars" % "bootstrap" % "2.3.1",
	"com.github.mumoshu" %% "play2-memcached" % "0.3.0.2",
	"com.typesafe.play" %% "play-cache" % "2.2.0" withSources
)


//appDependencies ++= Seq(
//	"org.hibernate" % "hibernate-entitymanager" % "3.6.9.Final",
//	"mysql" % "mysql-connector-java" % "5.1.18"
//)


resolvers ++= Seq(
	// "Local Maven Repository" at "file:///"+Path.userHome.absolutePath+"/.m2/repository"
    "Local Maven Repository" at "file:///e:/_shared/_repository/maven/3.x.x",
    "Spy Repository" at "http://files.couchbase.com/maven2"
)

playJavaSettings
