name := "oneDimensionalBinPacking"

version := "1.0"

scalaVersion := "2.11.12"

libraryDependencies ++= Seq("org.scalatest" %% "scalatest" % "3.0.5" % "test"
  exclude("org.scala-lang.modules", "scala-xml_2.11")
)