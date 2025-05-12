package di

import data.dataSource.audit.AuditDtoParser
import data.dataSource.project.ProjectDtoParser
import data.dataSource.task.TaskDtoParser
import data.dataSource.taskState.TaskStateDtoParser
import data.dataSource.user.UserDtoParser
import data.repositories.mappers.*
import org.koin.dsl.module

val mapperModule = module {
    factory { AuditDtoMapper() }
    factory { AuditDtoParser() }

    factory { ProjectDtoMapper() }
    factory { ProjectDtoParser() }

    factory { TaskStateDtoMapper() }
    factory { TaskStateDtoParser() }

    factory { TaskDtoMapper() }
    factory { TaskDtoParser() }

    factory { UserDtoMapper() }
    factory { UserDtoParser() }
}