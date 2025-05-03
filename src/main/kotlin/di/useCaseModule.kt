package di

import domain.usecases.AddAuditUseCase
import domain.usecases.GetAllAuditUseCase
import domain.usecases.project.CreateProjectUseCase
import domain.usecases.project.DeleteProjectUseCase
import domain.usecases.project.GetAllProjectsUseCase
import domain.usecases.project.UpdateProjectUseCase
import domain.usecases.taskState.CreateTaskStateUseCase
import domain.usecases.taskState.EditTaskStateUseCase
import org.koin.dsl.module

val useCaseModule = module {
    single { CreateProjectUseCase(projectRepository = get()) }
    single { UpdateProjectUseCase(projectRepository = get()) }
    single { DeleteProjectUseCase(projectRepository = get()) }
    single { GetAllProjectsUseCase(projectRepository = get()) }
    single { CreateTaskStateUseCase(repository = get()) }
    single { EditTaskStateUseCase(repository = get()) }
    single { AddAuditUseCase(auditRepository = get()) }
    single { GetAllAuditUseCase(auditRepositoryImpl = get()) }
}