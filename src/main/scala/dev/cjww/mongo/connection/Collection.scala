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

package dev.cjww.mongo.connection

import org.bson.codecs.configuration.CodecRegistry
import org.mongodb.scala.{MongoClient, MongoCollection, MongoDatabase}

import scala.reflect.ClassTag

trait Collection {
  protected val mongoUri, dbName, collectionName: String

  private lazy val mongoClient: MongoClient = MongoClient(mongoUri)
  private lazy val database: MongoDatabase = mongoClient.getDatabase(dbName)

  def collection[T](implicit ct: ClassTag[T], codec: CodecRegistry): MongoCollection[T] = {
    database.withCodecRegistry(codec).getCollection[T](collectionName)
  }
}
