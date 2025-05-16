package di

import auth.UserSession
import com.github.doyaaaaaken.kotlincsv.client.CsvReader
import data.dataSource.util.CsvHandler
import data.dataSource.util.hash.MD5PasswordHash
import data.dataSource.util.hash.PasswordHash
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import java.security.MessageDigest

val dataUtilModule = module {
    factory { CsvReader() }
    factory { CsvHandler(get()) }
    factory { MessageDigest.getInstance("MD5") }
    factory<PasswordHash> { MD5PasswordHash(get()) }
    single{UserSession()}
}