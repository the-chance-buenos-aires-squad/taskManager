package di


import com.github.doyaaaaaken.kotlincsv.client.CsvReader
import com.github.doyaaaaaken.kotlincsv.client.CsvWriter

import data.dataSource.UserCsvDataSource


import data.dataSource.util.CsvHandler
import data.repositories.AuthRepositoryImpl
import domain.repositories.AuthRepository
import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.io.File


//
val dataModule = module {
    single { CsvWriter() }
    single { CsvReader() }
    single { CsvHandler(get(), get()) }


    single<File>(qualifier = Paths.UserFileQualifier) {
        File(Paths.UserFilePath)
    }

    single { UserCsvDataSource(Files.UserFile,get()) }

    single<AuthRepository> { AuthRepositoryImpl() }


}

object Paths {
    /*
    when injecting the file to data source use qualifier before injecting
    @Named("UserFilePath") private val userFile: File
     */
    const val UserFilePath = "src/main/kotlin/data/resource/users_file.csv"
    val UserFileQualifier: Qualifier = named("UserFilePath")
}

object Files {
    val UserFile = File("src/main/kotlin/data/resource/users_file.csv")

}