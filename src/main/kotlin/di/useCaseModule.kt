package di

import domain.usecases.AddAuditUseCase
import domain.usecases.AuthenticationUseCase
import domain.usecases.CreateUserUseCase
import domain.usecases.GetAllAuditUseCase
import domain.usecases.project.CreateProjectUseCase
import domain.usecases.project.DeleteProjectUseCase
import domain.usecases.project.GetAllProjectsUseCase
import domain.usecases.project.UpdateProjectUseCase
import domain.usecases.task.CreateTaskUseCase
import domain.usecases.task.DeleteTaskUseCase
import domain.usecases.task.UpdateTaskUseCase
import domain.usecases.taskState.*
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
    single { CreateTaskUseCase(taskRepository = get(), authRepository = get(), addAuditUseCase = get()) }
    single { DeleteTaskUseCase(taskRepository = get(), authRepository = get(), addAuditUseCase = get()) }
    single { UpdateTaskUseCase(taskRepository = get(), authRepository = get(), addAuditUseCase = get()) }
    single { CreateTaskUseCase(get()) }
    single { GetTasksUseCase(get()) }
    single { CreateUserUseCase(authRepository = get(), userValidator = get(), addAuditUseCase = get()) }
    single { AuthenticationUseCase(authRepository = get(), userValidator = get()) }
    single { GetTasksGroupedByStateUseCase(getTasksUseCase = get(), getTaskStatesUseCase = get()) }
}