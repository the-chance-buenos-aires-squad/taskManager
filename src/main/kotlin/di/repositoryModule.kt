package di

import data.repositories.*
import domain.repositories.*
import org.koin.dsl.module

val repositoryModule = module {
    single<AuditRepository> { AuditRepositoryImpl(auditDataSource = get(), auditDtoMapper = get()) }
    single<AuthRepository> { AuthRepositoryImpl(userRepository = get(), session = get(), md5Hash = get(), userMapper = get()) }
    single<UserRepository> { UserRepositoryImpl(userDataSource = get(), userMapper = get(), md5Hash = get(), auditRepository = get(), userSessionImpl = get()) }

    single<ProjectRepository> { ProjectRepositoryImpl(projectDataSource = get(), projectMapper = get(), userSession = get(), auditRepository = get()) }
    single<TaskStateRepository> { TaskStateRepositoryImpl(taskStateDataSource = get(), taskStateDtoMapper = get(), userSession = get(), auditRepository = get()) }
    single<TaskRepository> { TaskRepositoryImpl(taskDataSource = get(), taskMapper = get(), userSessionImpl = get(), auditRepository = get()) }
}