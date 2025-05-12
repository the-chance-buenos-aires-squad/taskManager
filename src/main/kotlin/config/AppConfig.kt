package config

import com.typesafe.config.ConfigFactory

object AppConfig {
    private val config=ConfigFactory.load()
    val mongoUri:String= config.getString("mongo.uri")
    val mongoDatabase:String= config.getString("mongo.database")
}