import sbt._

class JerksonProject(info: ProjectInfo) extends DefaultProject(info)
                                                with IdeaProject
                                                with maven.MavenDependencies {
  /**
   * Publish the source as well as the class files.
   */
  override def packageSrcJar = defaultJarPath("-sources.jar")
  val sourceArtifact = Artifact.sources(artifactID)
  override def packageToPublishActions = super.packageToPublishActions ++ Seq(packageSrc)
  
  lazy val publishTo = Resolver.sftp("Personal Repo",
                                     "codahale.com",
                                     "/home/codahale/repo.codahale.com/")

  /**
   * Repositories
   */
  val codaRepo = "Coda's Repo" at "http://repo.codahale.com/"

  /**
   * Dependencies
   */
  val jacksonVersion = "1.7.6"
  val jacksonCore = "org.codehaus.jackson" % "jackson-core-asl" % jacksonVersion
  val jacksonMapper = "org.codehaus.jackson" % "jackson-mapper-asl" % jacksonVersion
  val paranamer = "com.thoughtworks.paranamer" % "paranamer" % "2.3"

  /**
   * Test Dependencies
   */
  val specs = "org.scala-tools.testing" %% "specs" % "1.6.6" % "test"
  val simplespec = "com.codahale" %% "simplespec" % "0.2.0" % "test"
  val mockito = "org.mockito" % "mockito-all" % "1.8.4" % "test"
}
