package di

import com.github.doyaaaaaken.kotlincsv.client.CsvReader
import com.github.doyaaaaaken.kotlincsv.client.CsvWriter
import data.dataSource.project.CsvProjectDataSource
import data.dataSource.project.ProjectDataSource
import data.dataSource.user.CsvUserDataSource
import data.dataSource.user.UserDataSource
import data.dataSource.util.CsvHandler
import data.repositories.ProjectRepositoryImpl
import data.repositories.UserRepositoryImpl
import data.repositories.mappers.Mapper
import data.repositories.mappers.ProjectMapper
import data.repositories.mappers.UserMapper
import domain.entities.Project
import domain.entities.User
import domain.repositories.ProjectRepository
import domain.repositories.UserRepository
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
    single<File>(qualifier = Paths.ProjectFileQualifier) {
        File(Paths.PROJECTFILEPATH)
    }

    single<Mapper<User>> { UserMapper() }
    single<UserDataSource> { CsvUserDataSource(get(), get(Paths.UserFileQualifier)) }

    single<UserRepository> { UserRepositoryImpl(get(), get()) }

    single<Mapper<Project>> { ProjectMapper() }
    single<ProjectDataSource> { CsvProjectDataSource(get(Paths.ProjectFileQualifier), get()) }
    single<ProjectRepository> { ProjectRepositoryImpl(get(), get()) }

}

object Paths {
    /*
    when injecting the file to data source use qualifier before injecting
    @Named("UserFilePath") private val userFile: File
     */
    const val UserFilePath = "src/main/kotlin/data/resource/users_file.csv"
    val UserFileQualifier: Qualifier = named("UserFilePath")

    const val PROJECTFILEPATH = "src/main/kotlin/data/resource/projects.csv"
    val ProjectFileQualifier: Qualifier = named("ProjectFilePath")
}

object Files {
    val UserFile = File("src/main/kotlin/data/resource/users_file.csv")

}