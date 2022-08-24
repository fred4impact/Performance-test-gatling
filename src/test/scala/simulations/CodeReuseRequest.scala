package simulations
import io.gatling.core.Predef._
import io.gatling.core.structure.{ChainBuilder, ScenarioBuilder}
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

class CodeReuseRequest  extends Simulation{

  // create a request headers http configuration
  val requestHeader: HttpProtocolBuilder = http.baseUrl("https://world-tech-comapanies.herokuapp.com/")
    .header("Accept", "application/json")

  // define request call to be reused
  def getAllCompanies: ChainBuilder = {
    repeat(4) {
      exec(
         http("Get All Companies")
           .get("company")
           .check(status.in(200 to 304))
      )
    }
  }

  // get single company
  def getACompany: ChainBuilder = {
     repeat(2){
       exec(
         http("Get single company")
           .get("company/3")
           .check(status.is(200))
       )
     }
  }




  // Define scenario users here
  val uSer: ScenarioBuilder = scenario("List Companies")
    .exec(getAllCompanies) // request all companies
    .pause(5)
    .exec(getACompany)   // request a single company


  // setUp to inject users
  setUp(
    uSer.inject(atOnceUsers(1))
  ).protocols(requestHeader)

}


