package simulations
import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder


class ValidateResponseBodyAndExtract extends Simulation {

  val httpConfig: HttpProtocolBuilder = http.baseUrl("https://world-tech-comapanies.herokuapp.com/")
    .header("Accept", "application/json")

  val scen: ScenarioBuilder = scenario("Validate using JSONPATH")

    // First Request:-  Validate the company_name
    .exec(
       http("Get specific company name").get("company/4")
       .check(jsonPath("$.company_name").is("Tesla")))

  // Secondly Request:  extract the ID of a company and save it to a variable called compId
  // performing correlation in Gatling

    .exec(
      http("Get all companies").get("company")
        .check(jsonPath("$[1]['id']").saveAs("compId")))

  // Third Request - use the compId variable saved from the above call
    .exec(http("Validate specific company using compId")
      .get("company/${compId}")
      .check(jsonPath("$.company_name").is("Google")))


  setUp(
    scen.inject(atOnceUsers(1))
  ).protocols(httpConfig)

}
