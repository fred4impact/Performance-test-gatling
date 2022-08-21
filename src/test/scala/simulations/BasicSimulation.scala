package simulations
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

import scala.concurrent.duration._

class BasicSimulation extends Simulation{

  val httpProtocol: HttpProtocolBuilder = http
    .baseUrl("http://computer-database.gatling.io") // base URL is set here
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")


   // create a scenario  // A scenario is a chain of requests and pauses

   private val scn  = scenario("Sample Test")
       .exec(http("get request").get("/"))
       .pause(7) // Note that Gatling has recorder real time pauses


   setUp(scn.inject(atOnceUsers(1)).protocols(httpProtocol))






}
