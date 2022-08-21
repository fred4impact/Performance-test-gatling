package simulations
import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder
import scala.concurrent.duration.DurationInt


class ValidateResponseCode extends Simulation{

  // here we configure the headers
  val  httpConfig: HttpProtocolBuilder = http.baseUrl("https://world-tech-comapanies.herokuapp.com/")
      .header("Accept", "application/json")


  // here we crete scenario and responsde check

  val scen: ScenarioBuilder = scenario("Get Company 3 requests")

    .exec(
         http("Get All Company - 1 Request").get("company")
           .check(status.is(200)))
    .pause(5)

    // second request scenario
    .exec(
      http("Get a Youtube Company - 2 Request").get("company/3")
        .check(status.in(200 to 205)))
    .pause(1,10)


    // Get all the company again

    .exec(
      http("Get All Company - 3 Request").get("company")
        .check(status.not(404), status.not(500)))
    .pause(3000.milliseconds)


  setUp(
    scen.inject(atOnceUsers(1))
  ).protocols(httpConfig)


}
