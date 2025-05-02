package di


//import org.example.data.CsvFileReader
//import org.example.data.CsvRecipesRepository
//import org.example.data.MemoryDataSource
//import org.example.data.RecipeParser
//import org.example.logic.RecipesRepository
import com.github.doyaaaaaken.kotlincsv.client.CsvReader
import com.github.doyaaaaaken.kotlincsv.client.CsvWriter

import data.dataSource.user.CsvUserDataSource
import data.dataSource.user.UserDataSource


import data.dataSource.util.CsvHandler
import data.repositories.UserRepositoryImpl
import data.repositories.mappers.Mapper
import data.repositories.mappers.UserMapper
import domain.entities.User
import domain.repositories.UserRepository
import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.io.File
import kotlin.io.path.Path


//
val dataModule = module {
    single { CsvWriter() }
    single { CsvReader() }
    single { CsvHandler(get(), get()) }


    single<File>(qualifier = Paths.UserFileQualifier) {
        File(Paths.UserFilePath)
    }

    single<Mapper<User>> { UserMapper() }
    single<UserDataSource> { CsvUserDataSource(get(),get(Paths.UserFileQualifier)) }

    single<UserRepository> { UserRepositoryImpl(get(),get()) }

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