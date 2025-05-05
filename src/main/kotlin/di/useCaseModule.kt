package di

import domain.usecases.*
import domain.usecases.project.CreateProjectUseCase
import domain.usecases.project.DeleteProjectUseCase
import domain.usecases.project.GetAllProjectsUseCase
import domain.usecases.project.UpdateProjectUseCase
import domain.usecases.taskState.EditTaskStateUseCase
import domain.usecases.taskState.CreateTaskStateUseCase
import domain.usecases.taskState.GetAllTaskStatesUseCase
import domain.usecases.taskState.DeleteTaskStateUseCase
import domain.usecases.taskState.ExistsTaskStateUseCase
import org.koin.dsl.module

val useCaseModule = module {
    single { CreateProjectUseCase(get()) }
    single { UpdateProjectUseCase(get()) }
    single { DeleteProjectUseCase(get()) }
    single { GetAllProjectsUseCase(get()) }
    single { CreateTaskStateUseCase(get()) }
    single { EditTaskStateUseCase(get()) }
    single { AddAuditUseCase(get()) }
    single { GetAllAuditUseCase(get()) }
    single { DeleteTaskStateUseCase(get()) }
    single { GetAllTaskStatesUseCase(get()) }
    single { ExistsTaskStateUseCase(get()) }
    single { CreateTaskUseCase(get()) }
    single { GetTasksUseCase(get()) }
    single { CreateUserUseCase(authRepository = get(), userValidator = get(), addAuditUseCase = get()) }
    single { AuthenticationUseCase(authRepository = get(), userValidator = get()) }
    single { GetTasksGroupedByStateUseCase(getTasksUseCase = get(), getTaskStatesUseCase = get()) }
}