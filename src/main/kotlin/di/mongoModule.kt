package di

import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import org.koin.dsl.module

val mongoModule = module {
    single<MongoClient> {
        MongoClient.create("mongodb://localhost:27017")
    }

    single<MongoDatabase> {
        get<MongoClient>().getDatabase("planmate")
    }
}