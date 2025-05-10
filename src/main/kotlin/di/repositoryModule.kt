package di

import data.repositories.*
import domain.repositories.*
import org.koin.dsl.module

val repositoryModule = module {
    single<AuditRepository> { AuditRepositoryImpl(auditDataSource = get(), auditDtoMapper = get()) }
    single<AuthRepository> { AuthRepositoryImpl(userRepository = get(), mD5Hasher = get()) }
    single<UserRepository> { UserRepositoryImpl(userDataSource = get(), userMapper = get()) }
    single<ProjectRepository> { ProjectRepositoryImpl(projectDataSource = get(), projectMapper = get()) }
    single<TaskStateRepository> { TaskStateRepositoryImpl(taskStateCSVDataSource = get(), taskStateDtoMapper = get()) }
    single<TaskRepository> { TaskRepositoryImpl(taskDataSource = get(), taskMapper = get()) }
}