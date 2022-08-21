package simulations

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder


class companyLoadTest extends Simulation{

  // Step 1:  add the http configuration
  val httpConfig: HttpProtocolBuilder = http.baseUrl("https://world-tech-comapanies.herokuapp.com/")
    .header("Accept", "application/json")

  // Step 2:  define the scenario
  val scn: ScenarioBuilder = scenario("Test Company Perf")
    .exec(http("List All Company").get("company"))
    .pause(8)

  // Step 3 ::  Load scenario

  setUp(
     scn.inject(atOnceUsers(10))
  ).protocols(httpConfig)

}
