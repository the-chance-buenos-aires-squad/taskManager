package di

import com.github.doyaaaaaken.kotlincsv.client.CsvReader
import com.github.doyaaaaaken.kotlincsv.client.CsvWriter
import data.dataSource.project.CsvProjectDataSource
import data.dataSource.project.ProjectDataSource
import data.dataSource.auditDataSource.AuditDataSource
import data.dataSource.auditDataSource.CsvAuditDataSource
import data.dataSource.taskState.TaskStateCSVDataSource
import data.dataSource.taskState.TaskStateDataSource
import data.dataSource.user.CsvUserDataSource
import data.dataSource.user.UserDataSource
import data.dataSource.util.CsvHandler
import data.repositories.ProjectRepositoryImpl
import data.repositories.AuditRepositoryImpl
import data.repositories.TaskStateRepositoryImpl
import data.repositories.UserRepositoryImpl
import data.repositories.mappers.*
import domain.entities.Project
import domain.entities.Audit
import domain.entities.TaskState
import domain.entities.User
import domain.repositories.ProjectRepository
import domain.repositories.AuditRepository
import domain.repositories.TaskStateRepository
import domain.repositories.UserRepository
import domain.usecases.AddAuditUseCase
import domain.usecases.GetAllAuditUseCase
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
    single<File>(qualifier = Paths.AuditFileQualifier) {
        File(Paths.AuditFilePath)
    }
    single<File>(qualifier = Paths.TaskStateFileQualifier) {
        File(Paths.TASK_STATE_FILE_PATH)
    }

    single<Mapper<Audit>> { AuditMapper() }


    single<AuditDataSource> { CsvAuditDataSource(csvHandler = get(), file = get(Paths.AuditFileQualifier)) }

    single<AuditRepository> { AuditRepositoryImpl(auditDataSource = get(), auditMapper = get()) }

    single { AddAuditUseCase(auditRepository = get()) }
    single { GetAllAuditUseCase(auditRepositoryImpl = get()) }

    single<Mapper<User>> { UserMapper() }
    single<UserDataSource> { CsvUserDataSource(get(), get(Paths.UserFileQualifier)) }

    single<UserRepository> { UserRepositoryImpl(get(), get()) }

    single<Mapper<Project>> { ProjectMapper() }
    single<ProjectDataSource> { CsvProjectDataSource(get(Paths.ProjectFileQualifier), get()) }
    single<ProjectRepository> { ProjectRepositoryImpl(get(), get()) }

    single<Mapper<TaskState>> { TaskStateMapper() }
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

    const val PROJECTFILEPATH = "src/main/kotlin/data/resource/projects.csv"
    val ProjectFileQualifier: Qualifier = named("ProjectFilePath")

    const val AuditFilePath = "src/main/kotlin/data/resource/audit.csv"
    val AuditFileQualifier: Qualifier = named("AuditFile")

    const val TASK_STATE_FILE_PATH = "src/main/kotlin/data/resource/task_state.csv"
    val TaskStateFileQualifier: Qualifier = named("TaskStateFilePath")


}

object Files {
    val UserFile = File("src/main/kotlin/data/resource/users_file.csv")

}