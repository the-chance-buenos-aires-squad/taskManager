package di

import data.repositories.mappers.*
import data.repositories.mappers.userMappers.UserDtoMapper
import domain.entities.Project
import org.koin.dsl.module

val mapperModule = module {
    single { UserDtoMapper() }
    single { AuditMapper() }
    single { ProjectMapper() }
    single { TaskStateMapper() }
    single { CsvTaskMapper() }
}