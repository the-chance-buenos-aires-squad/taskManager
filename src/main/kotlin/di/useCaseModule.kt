package di

import domain.usecases.audit.AddAuditUseCase
import domain.usecases.audit.GetAllAuditUseCase
import domain.usecases.auth.LoginUseCase
import domain.usecases.groupingByState.GetTasksGroupedByStateUseCase
import domain.usecases.project.CreateProjectUseCase
import domain.usecases.project.DeleteProjectUseCase
import domain.usecases.project.GetAllProjectsUseCase
import domain.usecases.project.UpdateProjectUseCase
import domain.usecases.task.*
import domain.usecases.taskState.CreateTaskStateUseCase
import domain.usecases.taskState.DeleteTaskStateUseCase
import domain.usecases.taskState.EditTaskStateUseCase
import domain.usecases.taskState.GetAllTaskStatesUseCase
import domain.usecases.user.CreateUserUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val useCaseModule = module {

        // Auth & User Use Cases
        singleOf(::LoginUseCase)
        singleOf(::CreateUserUseCase)

        // Project Use Cases
        singleOf(::CreateProjectUseCase)
        singleOf(::UpdateProjectUseCase)
        singleOf(::DeleteProjectUseCase)
        singleOf(::GetAllProjectsUseCase)

        // Task State Use Cases
        singleOf(::CreateTaskStateUseCase)
        singleOf(::EditTaskStateUseCase)
        singleOf(::DeleteTaskStateUseCase)
        singleOf(::GetAllTaskStatesUseCase)

        // Audit Use Cases
        singleOf(::AddAuditUseCase)
        singleOf(::GetAllAuditUseCase)

        // Task Use Cases
        singleOf(::GetTasksUseCase)
        singleOf(::GetAllTasksUseCase)
        singleOf(::GetTasksGroupedByStateUseCase)
        singleOf(::AddTaskUseCase)
        singleOf(::DeleteTaskUseCase)
        singleOf(::UpdateTaskUseCase)
}