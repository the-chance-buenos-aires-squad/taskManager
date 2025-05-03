package di

import com.github.doyaaaaaken.kotlincsv.client.CsvReader
import com.github.doyaaaaaken.kotlincsv.client.CsvWriter
import data.dataSource.user.CsvUserDataSource
import data.dataSource.user.UserDataSource
import data.dataSource.util.CsvHandler
import data.dataSource.util.PasswordHasher
import data.repositories.*
import data.repositories.mappers.*
import domain.entities.Project
import domain.entities.TaskState
import domain.repositories.*
import domain.usecases.AddAuditUseCase
import domain.usecases.GetAllAuditUseCase
import domain.util.UserValidator
import data.repositories.TaskRepositoryImpl
import data.repositories.UserRepositoryImpl
import data.repositories.mappers.Mapper
import data.repositories.mappers.UserMapper
import domain.entities.User
import domain.repositories.UserRepository
import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.io.File


val dataModule = module {
    single { CsvReader() }
    single { CsvHandler(get()) }
    single { CsvHandler(get(),get()) }
    single { CsvUserDataSource(get(),get(Paths.UserFileQualifier)) }
    single { TaskRepositoryImpl(get(),get()) }

    single { PasswordHasher() }
    single { UserValidator() }

    single<File>(qualifier = Paths.UserFileQualifier) {
        File(Paths.UserFilePath)
    }
    single<File>(qualifier = Paths.ProjectFileQualifier){
        File(Paths.PROJECTFILEPATH)
    }
    single<File>(qualifier = Paths.AuditFileQualifier) {
        File(Paths.AuditFilePath)
    }
    single<File>(qualifier = Paths.TaskStateFileQualifier) {
        File(Paths.TASK_STATE_FILE_PATH)
    }

    single { AuditMapper() }


    single<AuditDataSource> { CsvAuditDataSource(csvHandler = get(), file = get(Paths.AuditFileQualifier)) }

    single<AuditRepository> { AuditRepositoryImpl(auditDataSource = get(), auditMapper = get()) }

    single { AddAuditUseCase(auditRepository = get()) }
    single { GetAllAuditUseCase(auditRepositoryImpl = get()) }

    single { UserMapper() }
    single<UserDataSource> { CsvUserDataSource(get(), get(Paths.UserFileQualifier)) }

    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    single<UserRepository> { UserRepositoryImpl(userDataSource = get(), userMapper = get()) }


    single { ProjectMapper() }
    single<ProjectDataSource> { CsvProjectDataSource(get(Paths.ProjectFileQualifier), get()) }
    single<ProjectRepository> { ProjectRepositoryImpl(get(), get()) }

    single { TaskStateMapper() }
    single<TaskStateDataSource> { TaskStateCSVDataSource(get(Paths.TaskStateFileQualifier), get()) }
    single<TaskStateRepository> { TaskStateRepositoryImpl(get(), get()) }

}

object Paths {
    /*
    when injecting the file to data source use qualifier before injecting
    @Named("UserFilePath") private val userFile: File
     */
    const val UserFilePath = "src/main/kotlin/data/resource/users_file.csv"
    val UserFileQualifier: Qualifier = named("UserFilePath")

    const val TaskFilePath = "src/main/kotlin/data/resource/tasks_file.csv"
    val TaskFileQualifier: Qualifier = named("TaskFilePath")

    const val PROJECTFILEPATH = "src/main/kotlin/data/resource/projects.csv"
    val ProjectFileQualifier: Qualifier = named("ProjectFilePath")

    const val AuditFilePath = "src/main/kotlin/data/resource/audit.csv"
    val AuditFileQualifier: Qualifier = named("AuditFile")

    const val TASK_STATE_FILE_PATH = "src/main/kotlin/data/resource/task_state.csv"
    val TaskStateFileQualifier: Qualifier = named("TaskStateFilePath")


}
