import sbt._

class Plugins(info: ProjectInfo) extends PluginDefinition(info) {
  val sbtIdeaRepo = "sbt-idea-repo" at "http://mpeltonen.github.com/maven/"
  val sbtIdea = "com.github.mpeltonen" % "sbt-idea-plugin" % "0.1.0"

  val codasRepo = "codahale.com" at "http://repo.codahale.com/"
  val rsync = "com.codahale" % "rsync-sbt" % "0.1.1"
}
