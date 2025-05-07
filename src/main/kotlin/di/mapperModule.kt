package di

import data.dto.ProjectDto
import data.repositories.mappers.*
import domain.entities.Project
import org.koin.dsl.module

val mapperModule = module {
    single { UserMapper() }
    single { AuditMapper() }
    single<Mapper<Project,List<String>>> { CsvProjectMapper() }
    single<Mapper<Project,ProjectDto>> { MongoProjectMapper() }
    single { TaskStateMapper() }
    single { CsvTaskMapper() }
}