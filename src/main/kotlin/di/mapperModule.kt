package di

import data.dataSource.taskState.TaskStateDtoParser
import data.dataSource.user.UserDtoParser
import data.dataSource.audit.AuditDtoParser
import data.dataSource.project.ProjectDtoParser
import data.repositories.mappers.*
import data.repositories.mappers.UserDtoMapper
import org.koin.dsl.module

val mapperModule = module {
    single { AuditDtoMapper() }
    single { AuditDtoParser() }

    single { ProjectDtoMapper() }
    single { ProjectDtoParser() }

    single { TaskStateDtoMapper() }
    single { TaskStateDtoParser() }

    single { CsvTaskMapper() }

    single { UserDtoMapper() }
    single { UserDtoParser() }
}