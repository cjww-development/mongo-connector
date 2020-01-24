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

package com.cjwwdev.mongo.indexing

import com.cjwwdev.mongo.DatabaseRepository
import org.slf4j.{Logger, LoggerFactory}
import org.bson.codecs.configuration.CodecRegistry

import scala.concurrent.{ExecutionContext, Future}
import scala.reflect.ClassTag

trait RepositoryIndexer {
  private val logger: Logger = LoggerFactory.getLogger(getClass)

  def ensureMultipleIndexes[T](repo: DatabaseRepository)(implicit codec: CodecRegistry, ec: ExecutionContext, ct: ClassTag[T]): Future[Seq[String]] = {
    Future.sequence(repo.indexes map repo.ensureSingleIndex).map { seq =>
      val flatSeq = seq.flatten
      flatSeq.foreach(idx => logger.info(s"[ensureMultipleIndexes] - Ensured index ${idx}"))
      if(flatSeq.size == repo.indexes.size) {
        logger.info("[ensureMultipleIndexes] - All indexes ensured")
      } else {
        logger.warn(s"[ensureMultipleIndexes] - There was a problem ensuring one or more indexes for ${repo.getClass.getCanonicalName}")
      }
      flatSeq
    }
  }
}
