package di


import com.github.doyaaaaaken.kotlincsv.client.CsvReader
import com.github.doyaaaaaken.kotlincsv.client.CsvWriter

import data.dataSource.user.CsvUserDataSource
import data.dataSource.user.UserDataSource


import data.dataSource.util.CsvHandler
import data.repositories.mappers.Mapper
import data.repositories.mappers.UserMapper
import domain.entities.User
import data.repositories.AuthRepositoryImpl
import data.repositories.UserRepositoryImpl
import domain.repositories.AuthRepository
import domain.repositories.UserRepository
import data.repositories.PasswordHasher
import domain.util.UserValidator
import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.io.File


//
val dataModule = module {
    single { CsvWriter() }
    single { CsvReader() }
    single { CsvHandler(get(), get()) }

    single { PasswordHasher() }
    //todo which direction
    single { UserValidator() }

    single<File>(qualifier = Paths.UserFileQualifier) {
        File(Paths.UserFilePath)
    }

    single<Mapper<User>> { UserMapper() }
    single<UserDataSource> { CsvUserDataSource(get(),get(Paths.UserFileQualifier)) }

    single<AuthRepository> { AuthRepositoryImpl(get(),get()) }
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