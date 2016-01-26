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

trait ServicesConfig extends RunMode {
  import play.api.Play
  import play.api.Play.current

  protected lazy val rootServices = "microservice.services"
  protected lazy val services = s"$env.microservice.services"

//  @deprecated("The 'govuk-tax' is an unnecessary level of configuration please use ServicesConfig.services", "24.11.14")
  protected lazy val playServices = s"govuk-tax.$env.services"

  protected lazy val defaultProtocol =
    Play.configuration.getString(s"$rootServices.protocol")
    .getOrElse(Play.configuration.getString(s"$services.protocol")
      .getOrElse("http"))

  protected def config(serviceName: String): Configuration =
    Play.configuration.getConfig(s"$rootServices.$serviceName")
      .getOrElse(Play.configuration.getConfig(s"$services.$serviceName")
      .getOrElse(Play.configuration.getConfig(s"$playServices.$serviceName")
      .getOrElse(throw new IllegalArgumentException(s"Configuration for service $serviceName not found"))))

  def baseUrl(serviceName: String) = {
    val protocol = getConfString(s"$serviceName.protocol",defaultProtocol)
    val host = getConfString(s"$serviceName.host", throw new RuntimeException(s"Could not find config $serviceName.host"))
    val port = getConfInt(s"$serviceName.port", throw new RuntimeException(s"Could not find config $serviceName.port"))
    s"$protocol://$host:$port"
  }

  def getConfString(confKey: String, defString: => String): String = {
    Play.configuration.getString(s"$rootServices.$confKey").
      getOrElse(Play.configuration.getString(s"$services.$confKey").
      getOrElse(Play.configuration.getString(s"$playServices.$confKey").
      getOrElse(defString)))
  }

  def getConfInt(confKey: String, defInt: => Int): Int = {
    Play.configuration.getInt(s"$rootServices.$confKey").
      getOrElse(Play.configuration.getInt(s"$services.$confKey").
      getOrElse(Play.configuration.getInt(s"$playServices.$confKey").
      getOrElse(defInt)))
  }

  def getConfBool(confKey: String, defBool: => Boolean): Boolean = {
    Play.configuration.getBoolean(s"$rootServices.$confKey").
      getOrElse(Play.configuration.getBoolean(s"$services.$confKey").
      getOrElse(Play.configuration.getBoolean(s"$playServices.$confKey").
      getOrElse(defBool)))
  }
}
