/*
 * Copyright 2017 HM Revenue & Customs
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

import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}
import play.api.Play
import play.api.test.FakeApplication

/**
  * Created by william on 02/02/17.
  */
class ServicesConfigSpec extends WordSpecLike with Matchers with BeforeAndAfterAll {

  val config = Map(
    "microservice.services.testString" -> "hello world",
    "Test.microservice.services.devTestString" -> "hello test",
    "microservice.services.testInt" -> "1",
    "Test.microservice.services.devTestInt" -> "1",
    "microservice.services.testBool" -> "true",
    "Test.microservice.services.devTestBool" -> "true",
    "anotherInt" -> "1",
    "anotherString" -> "hello other test",
    "anotherBool" -> "false"
  )

  lazy val fakeApplication = FakeApplication(additionalConfiguration = config)

  override def beforeAll() {
    Play.start(fakeApplication)
  }

  override def afterAll() {
    Play.stop(fakeApplication)
  }

  trait Setup extends ServicesConfig

  "getConfString" should {
    "return a string from config under rootServices" in new Setup {
      override protected def app = fakeApplication

      getConfString("testString", "") shouldBe "hello world"
    }

    "return a string from config under Dev services" in new Setup {
      override protected def app = fakeApplication

      getConfString("devTestString", "") shouldBe "hello test"
    }

    "return a default string if the config can't be found" in new Setup {
      override protected def app = fakeApplication

      getConfString("notInConf", "hello default") shouldBe "hello default"
    }
  }

  "getConfInt" should {
    "return an int from config under rootServices" in new Setup {
      override protected def app = fakeApplication

      getConfInt("testInt", 0) shouldBe 1
    }

    "return an int from config under Dev services" in new Setup {
      override protected def app = fakeApplication

      getConfInt("devTestInt", 0) shouldBe 1
    }

    "return a default int if the config can't be found" in new Setup {
      override protected def app = fakeApplication

      getConfInt("notInConf", 1) shouldBe 1
    }
  }

  "getConfBool" should {
    "return a boolean from config under rootServices" in new Setup {
      override protected def app = fakeApplication

      getConfBool("testBool", defBool = false) shouldBe true
    }

    "return a boolean from config under Dev services" in new Setup {
      override protected def app = fakeApplication

      getConfBool("devTestBool", defBool = false) shouldBe true
    }

    "return a default boolean if the config can't be found" in new Setup {
      override protected def app = fakeApplication

      getConfBool("notInConf", defBool = true) shouldBe true
    }
  }

  "getInt" should {
    "return an int from config" in new Setup {
      override protected def app = fakeApplication

      getInt("anotherInt") shouldBe 1
    }

    "throw an exception if the config can't be found" in new Setup {
      override protected def app = fakeApplication

      intercept[RuntimeException](getInt("notInConf"))
        .getMessage shouldBe "Could not find config key 'notInConf'"
    }
  }

  "getString" should {
    "return a string from config" in new Setup {
      override protected def app = fakeApplication

      getString("anotherString") shouldBe "hello other test"
    }

    "throw an exception if the config can't be found" in new Setup {
      override protected def app = fakeApplication

      intercept[RuntimeException](getInt("notInConf"))
        .getMessage shouldBe "Could not find config key 'notInConf'"
    }
  }

  "getBool" should {
    "return a boolean from config" in new Setup {
      override protected def app = fakeApplication

      getBoolean("anotherBool") shouldBe false
    }

    "throw an exception if the config can't be found" in new Setup {
      override protected def app = fakeApplication

      intercept[RuntimeException](getInt("notInConf"))
        .getMessage shouldBe "Could not find config key 'notInConf'"
    }
  }

}
