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

import com.typesafe.config.ConfigFactory
import org.scalatest.{Matchers, WordSpecLike}
import play.api.Configuration

class ServicesConfigTest extends WordSpecLike with Matchers {


  val sc = new CleanServicesConfig {
    override val config = new Configuration(ConfigFactory.parseString(
        """
          |microservice {
          |  services {
          |    versioned {
          |      host = localhost
          |      port = 8080
          |      path = this/that
          |      version = 0.1.0
          |    }
          |    overridden {
          |      url = "https://some-server.org:654/foo/bar"
          |      host = localhost
          |      port = 8080
          |      path = this/that
          |      version = 0.1.0
          |    }
          |  }
          |}
          |Prod {
          |  microservice {
          |    services {
          |      specific {
          |        host = localhost
          |        port = 8090
          |      }
          |      optional {
          |        enabled = true
          |      }
          |      sized {
          |        count = 42
          |      }
          |    }
          |  }
          |}
        """.stripMargin))

    override val env = "Prod"
  }

  "services config" should {
    "say that versioned service contains the full url" in {
      sc.url("versioned") shouldBe "http://localhost:8080/this/that/0.1.0"
    }

    "say that overridden service contains only the url field" in {
      sc.url("overridden") shouldBe "https://some-server.org:654/foo/bar"
    }

    "say that service in the env specific part of the config contains the url" in {
      sc.url("specific") shouldBe "http://localhost:8090"
    }

    "read a boolean" in {
      sc.getConfBool("optional.enabled", false) shouldBe true
    }

    "read a boolean defult" in {
      sc.getConfBool("optional.thingied", false) shouldBe false
    }

    "read an integer" in {
      sc.getConfInt("sized.count", 0) shouldBe 42
    }
  }
}
