package di

import data.dataSource.audit.AuditDtoParser
import data.dataSource.project.ProjectDtoParser
import data.dataSource.task.TaskDtoParser
import data.dataSource.taskState.TaskStateDtoParser
import data.dataSource.user.UserDtoParser
import data.repositories.mappers.*
import org.koin.dsl.module

val mapperModule = module {
    single { AuditDtoMapper() }
    single { AuditDtoParser() }

    single { ProjectDtoMapper() }
    single { ProjectDtoParser() }

    single { TaskStateDtoMapper() }
    single { TaskStateDtoParser() }

    single { TaskDtoMapper() }
    single { TaskDtoParser() }

    single { UserDtoMapper() }
    single { UserDtoParser() }
}