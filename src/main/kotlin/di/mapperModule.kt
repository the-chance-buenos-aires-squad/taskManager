package di

import data.dataSource.taskState.TaskStateDtoParser
import data.dataSource.user.UserDtoParser
import data.repositories.mappers.*
import data.repositories.mappers.UserDtoMapper
import org.koin.dsl.module

val mapperModule = module {
    single { UserDtoMapper() }
    single { AuditMapper() }
    single { ProjectMapper() }
    single { TaskStateDtoMapper() }
    single { CsvTaskMapper() }

    single { UserDtoParser() }
    single { TaskStateDtoParser() }
}