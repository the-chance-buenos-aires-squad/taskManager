package di

import data.dataSource.project.ProjectDtoParser
import data.repositories.mappers.*
import org.koin.dsl.module

val mapperModule = module {
    single { UserMapper() }
    single { AuditMapper() }
    single { ProjectDtoMapper() }
    single { TaskStateMapper() }
    single { CsvTaskMapper() }
    single { ProjectDtoParser() }
}