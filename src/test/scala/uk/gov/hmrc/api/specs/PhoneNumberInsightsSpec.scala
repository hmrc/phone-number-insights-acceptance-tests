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

  val riskyPhoneNumber = "07700900001"
  val safePhoneNumber  = "07700900002"

  val invalidPayload          = "{}"
  val invalidInsightsEndpoint = s"$baseUrl/check/invalid-endpoint"

  override def beforeEach(): Unit = {
    clearWatchlistData()
    clearGraphData()
    clearCountsData()
  }

  override def afterEach(): Unit = {
    clearWatchlistData()
    clearGraphData()
    clearCountsData()
    super.afterAll()
  }

  Feature("[PNI-1]- Phone Number Insights - Check if a phone number exists/does not exist") {
    Scenario("[PNI.1.1] - Phone number exists on the watchlist, graph database and counts database") {
      Given("the watchlist & graph database is empty")
      assert(getWatchlistData.isEmpty)
      assert(getGraphData.isEmpty)
      assert(getCounts == 0)

      When(s"I add the phone number '$riskyPhoneNumber' to the watchlist, graph database and counts database")
      createWatchlistData(0, riskyPhoneNumber)
      createGraphData(1000, riskyPhoneNumber)
      createCountData(2, riskyPhoneNumber)

      And("I send a POST request to the check/insights endpoint")
      postCheckInsightsRequest(riskyPhoneNumber)

      Then(
        "the response should indicate that the number exists on the watchlist & graph database & the payload is correct"
      )
      validateRiskyNumberPayload(riskyPhoneNumber)
    }
    Scenario("[PNI.1.2] - Phone number does not exist on the watchlist & graph database") {
      Given("the watchlist & graph database is empty")
      assert(getWatchlistData.isEmpty)
      assert(getGraphData.isEmpty)

      When(s"I add the phone number '$riskyPhoneNumber' to the watchlist & graph database")
      createWatchlistData(0, riskyPhoneNumber)
      createGraphData(1000, riskyPhoneNumber)

      And("I send a POST request to the check/insights endpoint")
      postCheckInsightsRequest(safePhoneNumber)

      Then(
        s"the response should indicate that the number doesn't exist on the watchlist & graph database & the payload is correct"
      )
      validateSafeNumberPayload(safePhoneNumber)
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
  }
}
