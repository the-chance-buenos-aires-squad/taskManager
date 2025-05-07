package di

import data.repositories.mappers.*
import data.repositories.mappers.userMappers.UserCsvMapper
import domain.entities.Project
import org.koin.dsl.module

val mapperModule = module {
    single { UserCsvMapper() }
    single { AuditMapper() }
    single<Mapper<Project>> { ProjectMapper() }
    single { TaskStateMapper() }
    single { CsvTaskMapper() }
}