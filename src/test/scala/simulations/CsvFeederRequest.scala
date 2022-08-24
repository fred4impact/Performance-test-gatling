package simulations

import io.gatling.core.Predef._
import io.gatling.core.structure.{ChainBuilder, ScenarioBuilder}
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder
import io.gatling.core.feeder.BatchableFeederBuilder

class CsvFeederRequest extends Simulation {
  // we are trying to read from csv file here

  // create a request headers http configuration
  val requestHeader: HttpProtocolBuilder = http.baseUrl("https://world-tech-comapanies.herokuapp.com/")
    .header("Accept", "application/json")


  //2. Import CSV file
  val csvFeeder: BatchableFeederBuilder[String]#F = csv("data/data.csv").circular

   // Define the request
  // Call Definition
  def getSpecificCompanyWithCsvFeeder: ChainBuilder = {
    repeat(10) {
      feed(csvFeeder)
        .exec(
          http("Get Specific company") // reading the csv file
            .get("company/${id}")
            .check(jsonPath("$.company_name").is("${company_name}"))
            .check(status.in(200 to 304))
        )
    }
  }

  //4. user Scenario Definition here
  val user: ScenarioBuilder = scenario("CSV Scenario")
    .exec(getSpecificCompanyWithCsvFeeder)

  //5. Load  User Scenario
  setUp(
    user.inject(atOnceUsers(1))
  ).protocols(requestHeader)






}// end of code
