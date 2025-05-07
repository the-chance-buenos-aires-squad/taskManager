package di

import domain.usecases.*
import domain.usecases.project.CreateProjectUseCase
import domain.usecases.project.DeleteProjectUseCase
import domain.usecases.project.GetAllProjectsUseCase
import domain.usecases.project.UpdateProjectUseCase
import domain.usecases.task.CreateTaskUseCase
import domain.usecases.task.DeleteTaskUseCase
import domain.usecases.task.GetAllTasksUseCase
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
    single { GetTasksUseCase(get()) }
    single { AuthenticationUseCase(authRepository = get(), userValidator = get()) }
    single { GetTasksGroupedByStateUseCase(getTasksUseCase = get(), getTaskStatesUseCase = get()) }
    single { CreateTaskUseCase(taskRepository = get(), authRepository = get(), addAuditUseCase = get()) }
    single { DeleteTaskUseCase(taskRepository = get(), authRepository = get(), addAuditUseCase = get()) }
    single { GetAllTasksUseCase(taskRepository = get()) }
    single { UpdateTaskUseCase(taskRepository = get(), authRepository = get(), addAuditUseCase = get()) }
    single { CreateTaskUseCase(taskRepository = get(), addAuditUseCase = get(), authRepository = get()) }
    single { CreateUserUseCase(authRepository = get(), userValidator = get(), addAuditUseCase = get()) }
}