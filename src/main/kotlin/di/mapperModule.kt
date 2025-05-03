package di

import data.repositories.mappers.AuditMapper
import data.repositories.mappers.ProjectMapper
import data.repositories.mappers.TaskStateMapper
import data.repositories.mappers.UserMapper
import org.koin.dsl.module

val mapperModule = module {
    single { UserMapper() }
    single { ProjectMapper() }
    single { TaskStateMapper() }
    single { AuditMapper() }
}