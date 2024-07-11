package com.eorzitzer.sbt

import lmcoursier.CoursierConfiguration
import lmcoursier.definitions.Authentication
import sbt.Keys.{csrConfiguration, updateClassifiers, updateSbtClassifiers}
import sbt.{Def, *}
import sbt.plugins.JvmPlugin

object ResolverCredentialsPlugin extends AutoPlugin {

  override def trigger = allRequirements
  override def requires = JvmPlugin

  object autoImport {
    val resolverCredentials = settingKey[Map[String, Credentials]]("A map between resolver id to its specific credentials")
  }
  import autoImport._

  private def addResolverCredentials(cfg: CoursierConfiguration, credentialsMap: Map[String, Credentials]): CoursierConfiguration = {
    val addedCredentials: Vector[(String, Authentication)] = credentialsMap.map { t =>
      val directCredentials = Credentials.toDirect(t._2)
      t._1 -> Authentication(directCredentials.userName, directCredentials.passwd)
    }.toVector
    cfg.withAuthenticationByRepositoryId(cfg.authenticationByRepositoryId ++ addedCredentials)
  }

  lazy val baseResolverCredentialsConfiguration: Seq[Def.Setting[?]] = Seq(
    csrConfiguration := addResolverCredentials(csrConfiguration.value, resolverCredentials.value),
    updateClassifiers / csrConfiguration := addResolverCredentials((updateClassifiers / csrConfiguration).value, resolverCredentials.value),
    updateSbtClassifiers / csrConfiguration := addResolverCredentials((updateSbtClassifiers / csrConfiguration).value, resolverCredentials.value)
  )

  override lazy val projectSettings: Seq[Def.Setting[?]] = baseResolverCredentialsConfiguration

  override lazy val buildSettings: Seq[Def.Setting[?]] = Seq()

  override lazy val globalSettings: Seq[Def.Setting[?]] = Seq(
    resolverCredentials := Map()
  )
}
