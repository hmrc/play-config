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

import org.scalatest.{Matchers, WordSpec}
import play.api.{Configuration, Mode}

class ModeAwareConfigurationSpec extends WordSpec with Matchers {

  "ModeAwareConfiguration" should {

    "return config property with mode prefix if it exists" in {

      ModeAwareConfiguration.apply(
        Configuration("Test.foo" -> "bar"),
        Mode.Test
      ).getString("foo") shouldBe Some("bar")

    }

    "return config property without prefix, if there is no prefixed one" in {

      ModeAwareConfiguration.apply(
        Configuration("foo" -> "bar"),
        Mode.Test
      ).getString("foo") shouldBe Some("bar")

    }

    "return config property with 'govuk-tax.{mode}' if it exists" in {

      ModeAwareConfiguration.apply(
        Configuration("govuk-tax.Test.foo" -> "bar"),
        Mode.Test
      ).getString("foo") shouldBe Some("bar")

    }

    "should prefer 'mode.' prefix over 'govuk-tax.mode.' prefix" in {

      ModeAwareConfiguration.apply(
        Configuration("Test.foo" -> "bar", "govuk-tax.Test.foo" -> "baz"),
        Mode.Test
      ).getString("foo") shouldBe Some("bar")

    }

    "should prefer 'mode.' prefix over non prefixed properties" in {

      ModeAwareConfiguration.apply(
        Configuration("Test.foo" -> "bar", "foo" -> "baz"),
        Mode.Test
      ).getString("foo") shouldBe Some("bar")

    }

  }

}
