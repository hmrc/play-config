/*
 * Copyright 2019 HM Revenue & Customs
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
import play.api.Mode.Mode
import play.api.{Configuration, Mode}

class RunModeSpec extends WordSpecLike with Matchers {

  trait Setup extends RunMode

  "env" should {
    "return 'Test' if Play mode is set to Test" in new Setup {
      override protected def mode: Mode = Mode.Test

      override protected def runModeConfiguration: Configuration = Configuration()

      env shouldBe "Test"
    }

    "return value from 'run.mode' property if Play mode is not Test" in new Setup {
      override protected def mode: Mode = Mode.Prod

      override protected def runModeConfiguration: Configuration = Configuration(
        "run.mode" -> "Something"
      )

      env shouldBe "Something"
    }

    "return Dev as a default if Play mode is not Test" in new Setup {
      override protected def mode = Mode.Prod

      override protected def runModeConfiguration = Configuration()

      env shouldBe "Dev"
    }

  }
  "envPath" should {

    "return the `other` env path" in new Setup {
      override protected def mode = Mode.Dev

      override protected def runModeConfiguration = Configuration()

      envPath("/somePath")(other  = "http://localhost")  shouldBe "http://localhost/somePath"
      envPath("/somePath")(other  = "http://localhost/") shouldBe "http://localhost/somePath"
      envPath("//somePath")(other = "http://localhost")  shouldBe "http://localhost/somePath"
      envPath("somePath")(other   = "http://localhost")  shouldBe "http://localhost/somePath"
      envPath("somePath")(other   = "http://localhost/") shouldBe "http://localhost/somePath"
      envPath("somePath/")(other  = "http://localhost/") shouldBe "http://localhost/somePath"
      envPath()(other             = "http://localhost")  shouldBe "http://localhost"

    }

    "return the `prod` env path" in new Setup {

      override protected def mode = Mode.Prod

      override protected def runModeConfiguration = Configuration("run.mode" -> "Prod")

      envPath("/somePath")(prod  = "prod")    shouldBe "/prod/somePath"
      envPath("/somePath")(prod  = "/prod")   shouldBe "/prod/somePath"
      envPath("/somePath")(prod  = "//prod")  shouldBe "/prod/somePath"
      envPath("/somePath")(prod  = "prod/")   shouldBe "/prod/somePath"
      envPath("/somePath")(prod  = "/prod/")  shouldBe "/prod/somePath"
      envPath("/somePath")(prod  = "//prod/") shouldBe "/prod/somePath"
      envPath("//somePath")(prod = "/prod/")  shouldBe "/prod/somePath"
      envPath("somePath/")(prod  = "prod")    shouldBe "/prod/somePath"
      envPath()(prod             = "prod")    shouldBe "/prod"
    }

  }

  "RunMode object should instantiate valid trait instance" in {
    RunMode(
      Mode.Prod,
      Configuration(
        "run.mode" -> "Something"
      )).env shouldBe "Something"
  }
}
