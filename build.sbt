name := "scalafx-example"

version := "0.1"

scalaVersion := "2.13.2"

libraryDependencies += "org.scalafx" %% "scalafx" % "12.0.2-R18"

libraryDependencies += "org.scalafx" %% "scalafx-extras" % "0.3.4"

val http4sVersion = "0.21.3"

libraryDependencies ++= Seq(
  "org.http4s" %% "http4s-dsl" % http4sVersion,
  "org.http4s" %% "http4s-blaze-server" % http4sVersion,
  "org.http4s" %% "http4s-blaze-client" % http4sVersion,
  "org.http4s" %% "http4s-circe" % http4sVersion,
  "io.circe" %% "circe-generic" % "0.13.0",
  "io.circe" %% "circe-literal" % "0.13.0"
)
