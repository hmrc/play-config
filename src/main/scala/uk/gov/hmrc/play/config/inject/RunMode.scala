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

package uk.gov.hmrc.play.config.inject

import com.google.inject.Inject
import play.api.{Application, Configuration, Environment, Mode}

import scala.annotation.tailrec

trait RunMode extends uk.gov.hmrc.play.config.RunMode {

  protected def environment: Environment

  protected override def mode = environment.mode

}

class DefaultRunMode @Inject()(
  override val runModeConfiguration: Configuration,
  val environment: Environment
) extends RunMode