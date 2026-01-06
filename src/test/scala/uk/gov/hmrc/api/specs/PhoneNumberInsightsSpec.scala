/*
 * Copyright 2025 HM Revenue & Customs
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

package uk.gov.hmrc.api.specs

import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach}
import uk.gov.hmrc.api.helpers.PhoneNumberInsightsHelpers

class PhoneNumberInsightsSpec extends PhoneNumberInsightsHelpers with BeforeAndAfterEach with BeforeAndAfterAll {

  override def beforeEach(): Unit =
    deleteData()

  override def afterAll(): Unit = {
    deleteData()
    super.afterAll()
  }

  val response: Any = getWatchlistPhoneNumbers

  Feature("[PNI-1] - Test the watchlist test-only endpoint") {
    Scenario("Check if numbers exist on the phone number watchlist") {
      Given("the watchlist is empty and the test data has been created")
      assert(getWatchlistPhoneNumbers.isEmpty)
      createData(10)

      When("the phone numbers are retrieved from the watchlist")
      val phoneNumbers = getWatchlistPhoneNumbers
      assert(phoneNumbers.nonEmpty)

      And("the watchlist contains the expected number of phone numbers")
      assert(phoneNumbers.size == 14)

      Then("the expected phone numbers should exist on the watchlist")
      val presentNumbers = Seq("44798761728", "447928394728", "447927384756", "07783947887")
      presentNumbers.foreach { number =>
        assert(isPhoneNumberOnWatchlist(number))
      }
    }
  }
}
