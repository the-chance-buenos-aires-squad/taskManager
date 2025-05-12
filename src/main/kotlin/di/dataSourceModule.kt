package di

import data.dataSource.audit.CsvAuditDataSource
import data.dataSource.audit.MongoAuditDataSource
import data.dataSource.project.MongoProjectDataSource
import data.dataSource.task.MongoTaskDataSource
import data.dataSource.taskState.MongoTaskStateDataSource
import data.dataSource.user.MongoUserDataSource
import data.repositories.dataSource.*
import di.MongoCollections.auditCollectionQualifier
import di.MongoCollections.projectCollectionQualifier
import di.MongoCollections.taskStateCollectionQualifier
import di.MongoCollections.tasksCollectionQualifier
import di.MongoCollections.userCollectionQualifier
import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.io.File

val dataSourceModule = module {

    single<File>(qualifier = Paths.UserFileQualifier) {
        File(Paths.USER_FILE_PATH)
    }
    single<File>(qualifier = Paths.ProjectFileQualifier) {
        File(Paths.PROJECT_FILE_PATH)
    }
    single<File>(qualifier = Paths.AuditFileQualifier) {
        File(Paths.AUDIT_FILE_PATH)
    }
    single<File>(qualifier = Paths.TaskStateFileQualifier) {
        File(Paths.TASK_STATE_FILE_PATH)
    }
    single<File>(qualifier = Paths.TaskFileQualifier) {
        File(Paths.TASK_FILE_PATH)
    }

//    single<AuditDataSource> { CsvAuditDataSource(csvHandler = get(), auditDtoParser = get(), file = get(Paths.AuditFileQualifier)) }
    single<AuditDataSource> { MongoAuditDataSource(auditCollection = get(auditCollectionQualifier)) }

    //    single<ProjectDataSource> { CsvProjectDataSource(file = get(Paths.ProjectFileQualifier), projectDtoParser = get() ,csvHandler = get()) }
    single<ProjectDataSource> { MongoProjectDataSource(get(projectCollectionQualifier)) }

    //single<UserDataSource> { CsvUserDataSource(csvHandler = get(), file = get(Paths.UserFileQualifier), userDtoParser = get()) }
    single<UserDataSource> { MongoUserDataSource(get(userCollectionQualifier)) }

    //    single<TaskStateDataSource> { TaskStateCSVDataSource(file = get(Paths.TaskStateFileQualifier), taskStateDtoParser = get(), csvHandler = get()) }
    single<TaskStateDataSource> { MongoTaskStateDataSource(get(taskStateCollectionQualifier)) }

    //    single<TaskDataSource> { CsvTaskDataSource(csvHandler = get(), taskDtoParser = get(), file = get(Paths.TaskFileQualifier)) }
    single<TaskDataSource> { MongoTaskDataSource(get(tasksCollectionQualifier)) }
}

object Paths {
    const val USER_FILE_PATH = "src/main/kotlin/data/resource/users_file.csv"
    val UserFileQualifier: Qualifier = named("UserFilePath")

    const val TASK_FILE_PATH = "src/main/kotlin/data/resource/tasks_file.csv"
    val TaskFileQualifier: Qualifier = named("TaskFilePath")

    const val PROJECT_FILE_PATH = "src/main/kotlin/data/resource/projects.csv"
    val ProjectFileQualifier: Qualifier = named("ProjectFilePath")

    const val AUDIT_FILE_PATH = "src/main/kotlin/data/resource/audit.csv"
    val AuditFileQualifier: Qualifier = named("AuditFile")

    const val TASK_STATE_FILE_PATH = "src/main/kotlin/data/resource/task_state.csv"
    val TaskStateFileQualifier: Qualifier = named("TaskStateFilePath")
}