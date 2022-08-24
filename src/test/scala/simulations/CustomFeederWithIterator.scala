package simulations

import io.gatling.core.Predef._
import io.gatling.core.structure.{ChainBuilder, ScenarioBuilder}
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

import scala.util.Random

class CustomFeederWithIterator  extends Simulation{


  //1. HTTP Configuration
  val requestHeader: HttpProtocolBuilder = http
    .baseUrl("https://world-tech-comapanies.herokuapp.com/")
    .header("Accept", "application/json")

  //2. Helper variables and methods
  var idNumbers: Iterator[Int] = (11 to 20).iterator
  val rnd = new Random()

  def randomString(length: Int): String = {
    rnd.alphanumeric.filter(_.isLetter).take(length).mkString
  }

  //3. Custom feeder mapper
  val customFeeder: Iterator[Map[String, Any]] = Iterator.continually(Map(
    "id" -> idNumbers.next(),
    "company_name" -> ("company_name-" + randomString(9)),
    "founder" -> ("founder-" + randomString(6)),
    "year_established" -> ("year_established-" + randomString(5)),
    "current_networth" -> ("current_networth-" + randomString(6)),
    "hq" -> ("hq-" + randomString(6)),
    "country" -> ("country-" + randomString(6)),
  ))


  //4. Call Definition
  def createCompany: ChainBuilder = {
    repeat(5) {
      feed(customFeeder)
        .exec(http("Create New Company")
          .post("company")
          .body(ElFileBody("bodies/postNewCompany.json")).asJson
          .check(status.is(201)))
        .pause(1)
    }
  }


  //5. Scenario Definition
  val user: ScenarioBuilder = scenario("Scenario to create new Company")
    .exec(createCompany)

  //6. Load Scenario
  setUp(
    user.inject(atOnceUsers(1))
  ).protocols(requestHeader)



}
