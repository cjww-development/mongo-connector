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

package dev.cjww.mongo

import dev.cjww.mongo.responses._
import org.bson.codecs.configuration.CodecRegistry
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.model.Updates._

import scala.concurrent.{ExecutionContext, Future}
import scala.reflect.ClassTag

trait TestRepository extends DatabaseRepository {
  implicit val ec: ExecutionContext

  def create[T](data: T)(implicit codec: CodecRegistry, ct: ClassTag[T]): Future[MongoCreateResponse] = {
    collection[T].insertOne(data).toFuture().map {
      _ => MongoSuccessCreate
    }
  }

  def get[T](id: String)(implicit codec: CodecRegistry, ct: ClassTag[T]): Future[Option[T]] = {
    collection[T].find[T](equal("_id", id)).first().toFutureOption()
  }

  def update[T](id: String, data: Map[String, Any])(implicit codec: CodecRegistry, ct: ClassTag[T]): Future[MongoUpdatedResponse] = {
    val bsonUpdate = data.collect({ case (k, v) => set(k, v) }).toSeq
    collection[T].updateOne(equal("_id", id), bsonUpdate).toFuture().map {
      _ => MongoSuccessUpdate
    }
  }

  def delete[T](id: String)(implicit codec: CodecRegistry, ct: ClassTag[T]): Future[MongoDeleteResponse] = {
    collection[T].deleteOne(equal("_id", id)).toFuture().map {
      _ => MongoSuccessDelete
    }
  }
}
