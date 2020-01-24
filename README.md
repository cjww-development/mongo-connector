[![Apache-2.0 license](http://img.shields.io/badge/license-Apache-brightgreen.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)
[ ![Download](https://api.bintray.com/packages/cjww-development/releases/mongo-connector/images/download.svg) ](https://bintray.com/cjww-development/releases/mongo-connector/_latestVersion)

# mongo-connector

Mechanisms to connect to a MongoDB database (scala mongo driver implementation)

To utilise this library add this to your sbt build file

```
"com.cjww-dev.libs" % "mongo-connector_2.13" % "x.x.x" 
```

| Major Version | Scala Version | Mongo Version |
|---------------|---------------|----------------
| 0.x.x         | 2.13.x        | 4.2+          |


## About
#### Configuration
Configuration for uri, database and collection is derived from the database repositories package structure.


```hocon
    package.structure {
      RepositoryClass {
        uri = ""
        database = ""
        collection = ""
      }
    }
```

#### com.cjwwdev.mongo.DatabaseRepository
Flatmapping **collection** from this trait class will grant access to mongo CRUD operations.

```scala
    class ExampleDataBaseRepository extends DatabaseRepository {
      def findById[T](id: String)(implicit codec: CodecRegistry, ct: ClassTag[T]): Future[Option[T]] = {
		collection[T].find[T](equal("_id", id)).first().toFutureOption()
      }
    }
``` 

#### com.cjwwdev.mongo.indexes.RepositoryIndexer
To ensure each of your repositories indexes are ensured you need to implement RepositoryIndexer. Provide each of your repositories
in a sequence. like so

```scala
	val repoIndexer: RepositoryIndexer = new RepositoryIndexer {
	
	}

	repoIndexer.ensureMultipleIndexes[TestModel](testRepository)
```




### License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html")
