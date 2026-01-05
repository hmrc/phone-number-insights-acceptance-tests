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

package uk.gov.hmrc.api.helpers

import org.scalatest.Assertion
import org.scalatest.matchers.should.Matchers.*
import play.api.libs.json.Json
import play.api.libs.ws.StandaloneWSResponse
import uk.gov.hmrc.api.specs.BaseSpec

import scala.concurrent.Await
import scala.concurrent.duration.*

class PhoneNumberInsightsHelpers extends BaseSpec with HttpClientHelper {

  val testOnlyEndpoint           = s"$baseUrl/test-only/watchlist/data"
  val testOnlyEndpointDeleteData = s"$testOnlyEndpoint/delete"
  val testOnlyEndpointCreateData = s"$testOnlyEndpoint/create"

  def deleteData(): Assertion = {
    val deleteDataFromEndpoint =
      Await.result(delete(testOnlyEndpointDeleteData), 20.seconds)
    val responseBody           = deleteDataFromEndpoint.body
    responseBody should include regex "Deleted \\d+ watchlist phone numbers"
  }

  def createData(numberOfGeneratedPhoneNumbers: Int): Unit = {
    val request =
      s"""{
     |  "generatedEntries":{
     |    "numberOfEntries": $numberOfGeneratedPhoneNumbers
     |   },
     |  "manualEntries":{
     |    "phoneNumbers":[
     |      "44798761728",
     |      "447928394728",
     |      "447927384756",
     |      "07783947887"
     |    ]
     |   }
     |}""".stripMargin

    val createPhoneNumberInsightsTestOnlyData: StandaloneWSResponse =
      Await.result(
        post(testOnlyEndpointCreateData, request, headers: _*),
        20.seconds
      )
    val responseBody                                                = createPhoneNumberInsightsTestOnlyData.body
    responseBody                                   should include regex "Created \\d+ watchlist phone numbers"
    createPhoneNumberInsightsTestOnlyData.status shouldBe 200
  }

  def getWatchlistPhoneNumbers: Seq[String] = {
    val response = Await.result(
      get(testOnlyEndpoint, headers: _*),
      10.seconds
    )
    val body = if (response.status == 200 && response.body.trim.nonEmpty) response.body else "{}"
    val json = Json.parse(body)
    (json \\ "phoneNumbersOnWatchlistEntries").asOpt[Seq[String]].getOrElse(Seq.empty)
  }

  def isPhoneNumberOnWatchlist(phoneNumber: String): Boolean =
    getWatchlistPhoneNumbers.contains(phoneNumber)
}
