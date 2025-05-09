package di

import data.dataSource.taskState.TaskStateDtoParser
import data.dataSource.user.UserDtoParser
import data.dataSource.audit.AuditDtoParser
import data.dataSource.project.ProjectDtoParser
import data.dataSource.user.UserDtoParser
import data.repositories.mappers.*
import data.repositories.mappers.UserDtoMapper
import org.koin.dsl.module

val mapperModule = module {
    single { UserDtoMapper() }
    single { AuditDtoMapper() }
    single { ProjectDtoMapper() }
    single { TaskStateMapper() }
    single { CsvTaskMapper() }

    single { UserDtoParser() }
    single { TaskStateDtoParser() }

    single { ProjectDtoParser() }
    single { UserDtoParser() }
    single { AuditDtoParser() }
}