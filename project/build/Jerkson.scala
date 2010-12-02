import sbt._

class Jerkson(info: ProjectInfo) extends DefaultProject(info)
                                         with IdeaProject
                                         with rsync.RsyncPublishing {
  /**
   * Publish the source as well as the class files.
   */

  override def packageSrcJar = defaultJarPath("-sources.jar")

  val sourceArtifact = sbt.Artifact(artifactID, "src", "jar", Some("sources"), Nil, None)

  override def packageToPublishActions = super.packageToPublishActions ++ Seq(packageSrc)

  /**
   * Publish via rsync.
   */

  def rsyncRepo = "codahale.com:/home/codahale/repo.codahale.com"

  /**
   * Repositories
   */
  val codasRepo = "Coda's Repo" at "http://repo.codahale.com"

  /**
   * Dependencies
   */
  val jacksonVersion = "1.6.2"
  val jacksonCore = "org.codehaus.jackson" % "jackson-core-asl" % jacksonVersion withSources()
  val jacksonMapper = "org.codehaus.jackson" % "jackson-mapper-asl" % jacksonVersion withSources ()
  val paranamer = "com.thoughtworks.paranamer" % "paranamer" % "2.3" withSources()

  /**
   * Test Dependencies
   */
  val specs = "org.scala-tools.testing" %% "specs" % "1.6.6" % "test" withSources ()
  val simplespec = "com.codahale" %% "simplespec" % "0.2.0" % "test" withSources ()
  val mockito = "org.mockito" % "mockito-all" % "1.8.4" % "test" withSources ()
}
