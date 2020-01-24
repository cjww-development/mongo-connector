/*
 * Copyright 2020 CJWW Development
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
package com.cjwwdev.mongo

import com.cjwwdev.mongo.indexing.RepositoryIndexer
import com.cjwwdev.mongo.models.TestModel
import com.cjwwdev.mongo.models.TestModel._
import com.cjwwdev.mongo.responses.{MongoSuccessCreate, MongoSuccessDelete, MongoSuccessUpdate}
import org.mongodb.scala.model.{IndexModel, IndexOptions, Indexes}
import org.scalatest.BeforeAndAfterAll
import org.scalatestplus.play.PlaySpec
import play.api.test.{DefaultAwaitTimeout, FutureAwaits}

import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContext.Implicits
import scala.concurrent.ExecutionContext.Implicits.global

class DatabaseRepositoryISpec extends PlaySpec with FutureAwaits with DefaultAwaitTimeout with BeforeAndAfterAll {

  val testRepository: TestRepository = new TestRepository {
    override def indexes: Seq[IndexModel] = Seq(
      IndexModel(Indexes.ascending("string"), IndexOptions().background(false).unique(true)),
      IndexModel(Indexes.ascending("int"), IndexOptions().background(false).unique(true))
    )

    override implicit val ec: ExecutionContext = Implicits.global
    override protected val dbName: String = "test-db"
    override protected val mongoUri: String = "mongodb://localhost:27017"
    override protected val collectionName: String = "test-collection"
  }

  val repoIndexer: RepositoryIndexer = new RepositoryIndexer {}

  override def beforeAll(): Unit = {
    super.beforeAll()
    await(repoIndexer.ensureMultipleIndexes[TestModel](testRepository))
  }

  "create" should {
    "insert a model" in {
      val res = await(testRepository.create(TestModel("testDocId", "qwerty", 616)))
      res mustBe MongoSuccessCreate
    }
  }

  "get" should {
    "return the model associated with the provided id" in {
      val res = await(testRepository.get("testDocId"))
      res mustBe Some(TestModel("testDocId", "qwerty", 616))
    }
  }

  "update" should {
    "add the specific keys to correct document" in {
      val res = await(testRepository.update("testDocId", Map("int" -> 101)))
      res mustBe MongoSuccessUpdate

      val get = await(testRepository.get("testDocId"))
      get mustBe Some(TestModel("testDocId", "qwerty", 101))
    }
  }

  "delete" should {
    "remove the specified document from the database" in {
      val res = await(testRepository.delete("testDocId"))
      res mustBe MongoSuccessDelete

      val get = await(testRepository.get("testDocId"))
      get mustBe None
    }
  }
}
