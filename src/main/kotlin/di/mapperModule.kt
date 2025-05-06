package di

import data.repositories.mappers.*
import domain.entities.Project
import domain.entities.TaskState
import org.koin.dsl.module

val mapperModule = module {
    single { UserMapper() }
    single { AuditMapper() }
    single<Mapper<Project>> { ProjectMapper() }
    single { TaskStateMapper() }
    single { TaskMapper() }
}