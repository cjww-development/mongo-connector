/*
 * Copyright 2021 CJWW Development
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

import com.typesafe.config.ConfigFactory
import scoverage.ScoverageKeys

import scala.util.Try

val libraryName = "mongo-connector"

val btVersion: String = Try(ConfigFactory.load.getString("version")).getOrElse("0.1.0-local")

val dependencies: Seq[ModuleID] = Seq(
  "com.typesafe"           %  "config"             % "1.4.1",
  "org.mongodb.scala"      %% "mongo-scala-driver" % "4.2.3",
  "org.slf4j"              %  "slf4j-api"          % "1.7.30",
  "org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0"   % Test
)

lazy val scoverageSettings = Seq(
  ScoverageKeys.coverageExcludedPackages := "<empty>;Reverse.*;/.data/..*;views.*;models.*;.*(AuthService|BuildInfo|Routes).*",
  ScoverageKeys.coverageMinimum          := 80,
  ScoverageKeys.coverageFailOnMinimum    := false,
  ScoverageKeys.coverageHighlighting     := true
)

lazy val library = Project(libraryName, file("."))
  .settings(scoverageSettings:_*)
  .settings(
    version                              :=  btVersion,
    scalaVersion                         :=  "2.13.6",
    semanticdbEnabled                    :=  true,
    semanticdbVersion                    :=  scalafixSemanticdb.revision,
    organization                         :=  "dev.cjww.libs",
    libraryDependencies                  ++= dependencies,
    githubTokenSource                    := (if (Try(ConfigFactory.load.getBoolean("local")).getOrElse(true)) {
      TokenSource.GitConfig("github.token")
    } else {
      TokenSource.Environment("GITHUB_TOKEN")
    }),
    githubOwner                          :=  "cjww-development",
    githubRepository                     :=  libraryName,
    scalacOptions                        ++= Seq(
      "-unchecked",
      "-deprecation",
      "-Wunused"
    ),
    Test / testOptions                   +=  Tests.Argument("-oF")
  )
