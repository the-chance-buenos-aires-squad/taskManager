package di

import domain.usecases.project.CreateProjectUseCase
import domain.usecases.project.DeleteProjectUseCase
import domain.usecases.project.GetAllProjectsUseCase
import domain.usecases.project.UpdateProjectUseCase
import domain.usecases.taskState.CreateTaskStateUseCase
import domain.usecases.taskState.EditTaskStateUseCase
import org.koin.dsl.module

val logicModule = module {
    single { CreateProjectUseCase(get()) }
    single { UpdateProjectUseCase(get()) }
    single { DeleteProjectUseCase(get()) }
    single { GetAllProjectsUseCase(get()) }
    single { CreateTaskStateUseCase(get()) }
    single { EditTaskStateUseCase(get()) }
    single { DeleteProjectUseCase(get()) }
    single { GetAllProjectsUseCase(get()) }
}