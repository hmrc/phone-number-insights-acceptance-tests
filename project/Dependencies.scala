import sbt.*

object Dependencies {

  val test: Seq[ModuleID] = Seq(
    "uk.gov.hmrc"                  %% "api-test-runner"         % "0.10.0",
    "uk.gov.hmrc.mongo"            %% "hmrc-mongo-test-play-30" % "2.11.0",
    "org.wiremock"                  % "wiremock"                % "3.13.2",
    "com.fasterxml.jackson.module" %% "jackson-module-scala"    % "2.20.1"
  ).map(_ % Test)
}
