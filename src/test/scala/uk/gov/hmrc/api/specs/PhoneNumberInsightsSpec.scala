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

class PhoneNumberInsightsSpec extends BaseSpec with BeforeAndAfterEach with BeforeAndAfterAll {

  val watchlistPhoneNumber = "07700900001"
  val safePhoneNumber      = "07700900002"

  val invalidPayload          = "{}"
  val invalidInsightsEndpoint = s"$baseUrl/check/invalid-endpoint"

  override def beforeEach(): Unit =
    clearWatchlistData()

  override def afterEach(): Unit = {
    clearWatchlistData()
    super.afterAll()
  }

  Feature("[PNI-1]- Phone Number Insights - Check if a phone number exists/does not exist on the watchlist") {
    Scenario("[PNI.1.1] - Phone number exists on the watchlist") {
      Given("the watchlist is empty")
      assert(getWatchlistData.isEmpty)

      When(s"I add the phone number '$watchlistPhoneNumber' to the watchlist")
      createWatchlistData(0, watchlistPhoneNumber)

      And("I send a POST request to the check/insights endpoint")
      postCheckInsightsRequest(watchlistPhoneNumber)

      Then("the response should indicate that the number exists on the watchlist and the payload is correct")
      assertPhoneNumberIsOnWatchlist(watchlistPhoneNumber)
    }
    Scenario("[PNI.1.2] - Phone number does not exist on the watchlist") {
      Given("the watchlist is empty")
      assert(getWatchlistData.isEmpty)

      When(s"I add the phone number '$watchlistPhoneNumber' to the watchlist")
      createWatchlistData(0, watchlistPhoneNumber)

      And("I send a POST request to the check/insights endpoint")
      postCheckInsightsRequest(safePhoneNumber)

      Then(s"the response should indicate that the number does not exist on the watchlist and the payload is correct")
      assertPhoneNumberIsNotOnWatchlist(safePhoneNumber)
    }
  }

  Feature("[PNI-2]- Phone Number Insights - POST Invalid requests to check/insights endpoint") {
    Scenario("[PNI.2.1] - POST to check/insights with invalid payload and return a 400 HTTP response") {
      Given("the watchlist is empty")
      assert(getWatchlistData.isEmpty)

      When("a POST to check/insights endpoint with an invalid payload")
      val response = postInvalidPayloadRequest(invalidPayload)

      Then("a 400 HTTP response is returned")
      assert(response.status == 400)
      assert(response.body.contains("\"message\":\"Invalid InsightsRequest payload"))
    }
    Scenario("[PNI.2.2] - POST to check/insights endpoint with an invalid endpoint and return a 404 HTTP response") {
      Given("the watchlist is empty")
      assert(getWatchlistData.isEmpty)

      When("a POST request is sent via an invalid endpoint")
      val response = postInvalidEndpoint(invalidInsightsEndpoint)

      Then("a 404 HTTP response is returned")
      assert(response.status == 404)
      assert(response.body.contains("URI not found"))
    }
    Scenario("[PNI.2.3] - POST to check/insights with invalid credentials and return a 403 HTTP response") {
      Given("the watchlist is empty")
      assert(getWatchlistData.isEmpty)

      When("a POST request is sent using invalid credentials")
      val response = postInvalidAuthRequest(watchlistPhoneNumber)

      Then("a 403 HTTP response is returned")
      assert(response.status == 403)
      assert(response.body.contains("USER_NOT_ALLOWED"))
      assert(response.body.contains("Invalid credentials"))
    }
  }
}
