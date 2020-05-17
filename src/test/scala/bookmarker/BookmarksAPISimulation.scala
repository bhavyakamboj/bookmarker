package bookmarker

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import scala.util.Random

class BookmarksAPISimulation extends Simulation {

  val httpConf = http
    .baseUrl("http://localhost:8080")
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")

  val tagFeeder = csv("data/tags.csv").random

  val getAllBookmarks = exec(http("AllBookmarks").get("/bookmarks")).pause(1)

  val getBookmarksByTag = feed(tagFeeder)
      .exec(http("Bookmarks By Tag")
        .get("/bookmarks?tag=${tag}"))
      .pause(3)

  // Now, we can write the scenario as a composition
  val scnGetAllBookmarks = scenario("Get All Bookmarks").exec(getAllBookmarks).pause(2)
  val scnGetBookmarksByTag = scenario("Get Bookmarks By Tag").exec(getBookmarksByTag).pause(2)

  setUp(
      scnGetAllBookmarks.inject(rampUsers(500) during (10 seconds)),
      scnGetBookmarksByTag.inject(rampUsers(500) during (10 seconds))
  ).protocols(httpConf)

}
