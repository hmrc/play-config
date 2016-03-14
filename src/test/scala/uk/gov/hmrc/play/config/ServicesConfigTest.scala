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

import org.scalatest.{Matchers, WordSpecLike}
import play.api.Configuration
import play.api.test.FakeApplication
import uk.gov.hmrc.play.test.WithFakeApplication

class ServicesConfigTest extends WordSpecLike with Matchers with WithFakeApplication{

  import com.typesafe.config.{Config, ConfigFactory}
  import net.ceedubs.ficus.Ficus._

  override lazy val fakeApplication = new FakeApplication{
    override lazy val initialConfiguration = Configuration(
      ConfigFactory.parseString(
        """
          |microservice {
          | services {
          |   backend-service {
          |     host = "hostname"
          |     port = 7777
          |   },
          |   backend-service-secure {
          |     protocol = "https"
          |     host = "hostname"
          |     port = 7778
          |   },
          |   key1 = "value"
          |   key2 = 2
          |   key3 = true
          | }
          |}
        """.stripMargin)
    )
  }

  val cc = new ServicesConfig {}

  "services config" should {
    "find a service in the config" in {
      cc.baseUrl("backend-service") shouldBe "http://hostname:7777"
      cc.baseUrl("backend-service-secure") shouldBe "https://hostname:7778"
      cc.baseUrlOpt("backend-service-secure") shouldBe Some("https://hostname:7778")
      cc.baseUrlOpt("non-existent") shouldBe None
      intercept[RuntimeException] { cc.baseUrl("non-existent") }

      cc.getConfStringOpt("key1") shouldBe Some("value")
      cc.getConfStringOpt("key0") shouldBe None

      cc.getConfIntOpt("key2") shouldBe Some(2)
      cc.getConfStringOpt("key0") shouldBe None

      cc.getConfBoolOpt("key3") shouldBe Some(true)
      cc.getConfStringOpt("key0") shouldBe None
    }
  }
}
