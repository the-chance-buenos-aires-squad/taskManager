package di

import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import org.koin.dsl.module

val mongoModule = module {
    single<MongoClient> {
        MongoClient.create("mongodb+srv://hamadarayyan056:PZrnkQVEQ4awiwY9@cluster0.cgucsr8.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0")
    }

    single<MongoDatabase> {
        get<MongoClient>().getDatabase("planmate")
    }
}