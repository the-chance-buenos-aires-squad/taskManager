package di

import data.dataSource.project.ProjectDtoParser
import data.dataSource.user.UserDtoParser
import data.repositories.mappers.*
import data.repositories.mappers.userMappers.UserDtoMapper
import data.repositories.mappers.UserDtoMapper
import org.koin.dsl.module

val mapperModule = module {
    single { UserDtoMapper() }
    single { AuditDtoMapper() }
    single { ProjectMapper() }
    single { AuditMapper() }
    single { ProjectDtoMapper() }
    single { TaskStateMapper() }
    single { CsvTaskMapper() }

    single { ProjectDtoParser() }
    single { UserDtoParser() }
}