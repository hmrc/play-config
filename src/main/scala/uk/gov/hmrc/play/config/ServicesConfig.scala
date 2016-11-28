/*
 * Copyright 2016 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.play.config

import play.api.Configuration

trait CleanServicesConfig {

  def config: Configuration
  def env: String

  protected val rootServices = "microservice.services"
  protected lazy val services = s"$env.microservice.services"

//  @deprecated("The 'govuk-tax' is an unnecessary level of configuration please use ServicesConfig.services", "24.11.14")
  protected lazy val playServices = s"govuk-tax.$env.services"

  protected lazy val defaultProtocol =
    config.getString(s"$rootServices.protocol")
      .orElse(config.getString(s"$services.protocol"))
      .getOrElse("http")

  protected def serviceConfig(serviceName: String): Configuration =
    config.getConfig(s"$rootServices.$serviceName")
      .orElse(config.getConfig(s"$services.$serviceName"))
      .orElse(config.getConfig(s"$playServices.$serviceName"))
      .getOrElse(throw new IllegalArgumentException(s"Configuration for service $serviceName not found"))

  def url(serviceName: String) = maybeConfString(s"$serviceName.url").getOrElse(urlByParts(serviceName))

  private def urlByParts(serviceName: String) =
    List(
      Some(baseUrl(serviceName)),
      maybeConfString(s"$serviceName.path"),
      maybeConfString(s"$serviceName.version")
    )
      .flatten
      .mkString("/")

  def baseUrl(serviceName: String) = {
    val protocol = getConfString(s"$serviceName.protocol",defaultProtocol)
    val host = getConfString(s"$serviceName.host", throw new RuntimeException(s"Could not find config $serviceName.host"))
    val port = getConfInt(s"$serviceName.port", throw new RuntimeException(s"Could not find config $serviceName.port"))
    s"$protocol://$host:$port"
  }

  def getConfString(confKey: String, default: => String): String = maybeConfString(confKey).getOrElse(default)
  def maybeConfString: String => Option[String] = conf(config.getString(_, None))

  def getConfInt(confKey: String, default: => Int): Int = maybeConfInt(confKey).getOrElse(default)
  def maybeConfInt: String => Option[Int] = conf(config.getInt)

  def getConfBool(confKey: String, default: => Boolean): Boolean = maybeConfBool(confKey).getOrElse(default)
  def maybeConfBool: String => Option[Boolean] = conf(config.getBoolean)

  private def conf[T](supply: String => Option[T])(confKey: String): Option[T] = {
    supply(s"$rootServices.$confKey").
      orElse(supply(s"$services.$confKey")).
      orElse(supply(s"$playServices.$confKey"))
  }

  def getInt(key: String) = Play.configuration.getInt(key).getOrElse(throw new RuntimeException(s"Could not find config key '$key'"))

  def getString(key: String) = Play.configuration.getString(key).getOrElse(throw new RuntimeException(s"Could not find config key '$key'"))

  def getBoolean(key: String) = Play.configuration.getBoolean(key).getOrElse(throw new RuntimeException(s"Could not find config key '$key'"))
}

trait ServicesConfig extends CleanServicesConfig with RunMode {
  import play.api.Play
  import play.api.Play.current

  // RunMode provides the implementation of env: String
  override def config = Play.configuration
}
