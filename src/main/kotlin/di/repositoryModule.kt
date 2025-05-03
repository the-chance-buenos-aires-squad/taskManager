package di


import data.repositories.AuthRepositoryImpl
import data.repositories.ProjectRepositoryImpl
import data.repositories.TaskStateRepositoryImpl
import data.repositories.UserRepositoryImpl
import data.repositories.AuditRepositoryImpl
import domain.repositories.AuthRepository
import domain.repositories.ProjectRepository
import domain.repositories.TaskStateRepository
import domain.repositories.UserRepository
import domain.repositories.AuditRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<AuthRepository> { AuthRepositoryImpl(userRepository = get(), mD5Hasher = get()) }
    single<UserRepository> { UserRepositoryImpl(userDataSource = get(), userMapper = get()) }
    single<ProjectRepository> { ProjectRepositoryImpl(projectDataSource = get(), projectMapper = get()) }
    single<TaskStateRepository> { TaskStateRepositoryImpl(taskStateCSVDataSource = get(), taskStateMapper = get()) }
    single<AuditRepository> { AuditRepositoryImpl(auditDataSource = get(), auditMapper = get()) }
}