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

import com.mongodb.client.model.IndexModel
import dev.cjww.mongo.connection.Collection
import org.bson.codecs.configuration.CodecRegistry

import scala.concurrent.Future
import scala.reflect.ClassTag


trait DatabaseRepository extends Collection {
  def indexes: Seq[IndexModel] = Seq.empty

  def ensureSingleIndex[T](index: IndexModel)(implicit ct: ClassTag[T], codec: CodecRegistry): Future[Seq[String]] = {
    collection.createIndexes(Seq(index)).toFuture()
  }
}
