package di

import com.github.doyaaaaaken.kotlincsv.client.CsvReader
import data.dataSource.util.CsvHandler
import data.dataSource.util.PasswordHash
import org.koin.dsl.module

val dataUtilModule = module {
    single { CsvReader() }
    single { CsvHandler(get()) }
    single { PasswordHash() }
}