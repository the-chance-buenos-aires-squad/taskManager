package di

import data.dataSource.project.ProjectDtoParser
import data.repositories.mappers.*
import data.repositories.mappers.userMappers.UserDtoMapper
import domain.entities.Project
import org.koin.dsl.module

val mapperModule = module {
    single { UserDtoMapper() }
    single { AuditMapper() }
    single { ProjectDtoMapper() }
    single { TaskStateMapper() }
    single { CsvTaskMapper() }
    single { ProjectDtoParser() }
}