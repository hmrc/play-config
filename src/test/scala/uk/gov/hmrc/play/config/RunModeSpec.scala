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

import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}
import play.api.Play
import play.api.test.FakeApplication

class RunModeSpec extends WordSpecLike with Matchers with BeforeAndAfterAll {

  lazy val fakeApplication = FakeApplication()

  override def beforeAll() {
    Play.start(fakeApplication)
  }

  override def afterAll() {
    Play.stop()
  }

  trait Setup extends RunMode

  "envPath" should {

    "return the `other` env path" in new Setup {
      override lazy val env = "Dev"

      envPath("/somePath")(other = "http://localhost") shouldBe "http://localhost/somePath"
      envPath("/somePath")(other = "http://localhost/") shouldBe "http://localhost/somePath"
      envPath("//somePath")(other = "http://localhost") shouldBe "http://localhost/somePath"
      envPath("somePath")(other = "http://localhost") shouldBe "http://localhost/somePath"
      envPath("somePath")(other = "http://localhost/") shouldBe "http://localhost/somePath"
      envPath("somePath/")(other = "http://localhost/") shouldBe "http://localhost/somePath"
      envPath()(other = "http://localhost") shouldBe "http://localhost"
    }

    "return the `prod` env path" in new Setup {
      override lazy val env = "Prod"

      envPath("/somePath")(prod = "prod") shouldBe "/prod/somePath"
      envPath("/somePath")(prod = "/prod") shouldBe "/prod/somePath"
      envPath("/somePath")(prod = "//prod") shouldBe "/prod/somePath"
      envPath("/somePath")(prod = "prod/") shouldBe "/prod/somePath"
      envPath("/somePath")(prod = "/prod/") shouldBe "/prod/somePath"
      envPath("/somePath")(prod = "//prod/") shouldBe "/prod/somePath"
      envPath("//somePath")(prod = "/prod/") shouldBe "/prod/somePath"
      envPath("somePath/")(prod = "prod") shouldBe "/prod/somePath"
      envPath()(prod = "prod") shouldBe "/prod"
    }

  }
}
