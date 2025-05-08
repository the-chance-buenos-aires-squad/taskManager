package di

import data.repositories.mappers.*
import data.repositories.mappers.UserDtoMapper
import org.koin.dsl.module

val mapperModule = module {
    single { UserDtoMapper() }
    single { AuditDtoMapper() }
    single { ProjectMapper() }
    single { TaskStateMapper() }
    single { CsvTaskMapper() }
}