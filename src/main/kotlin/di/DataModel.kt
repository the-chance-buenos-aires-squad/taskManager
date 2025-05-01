package di

import com.github.doyaaaaaken.kotlincsv.client.CsvReader
import com.github.doyaaaaaken.kotlincsv.client.CsvWriter
import data.dataSource.CsvAuditDataSource
import data.dataSource.util.CsvHandler
import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.named
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module
import java.io.File


val dataModule = module {
    single { CsvWriter() }
    single { CsvReader() }
    single { CsvHandler(get(), get()) }


    single<File>(qualifier = Paths.UserFileQualifier) {
        File(Paths.UserFilePath)
    }
    single<File>(qualifier = Paths.AuditFileQualifier){
        File(Paths.AuditFilePath)
    }


    single {
        CsvAuditDataSource(get(Paths.AuditFileQualifier))
    }



}

object Paths {
    /*
    when injecting the file to data source use qualifier before injecting
    @Named("UserFilePath") private val userFile: File
     */
    const val UserFilePath = "src/main/kotlin/data/resource/users_file.csv"
    val UserFileQualifier: Qualifier = named("UserFilePath")

    const val AuditFilePath = "src/main/kotlin/data/resource/audit.csv"
    val AuditFileQualifier :Qualifier = named("AuditFile")



}
