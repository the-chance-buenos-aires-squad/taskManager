package di

import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import presentation.UiController
import presentation.cli.audit.GetAllAuditsCli
import presentation.cli.MainCli
import presentation.cli.auth.CreateUserCli
import presentation.cli.auth.LoginCli
import presentation.cli.dashBoard.AdminDashBoardCli
import presentation.cli.dashBoard.MateDashBoardCli
import presentation.cli.helper.ProjectCliHelper
import presentation.cli.helper.TaskStateCliHelper
import presentation.cli.project.*
import presentation.cli.task.CreateTaskCli
import presentation.cli.task.DeleteTaskCli
import presentation.cli.task.UpdateTaskCli
import presentation.cli.task.ViewSwimlanesCLI
import presentation.cli.taskState.*
import java.util.*


val presentationModule = module {
    // I/O
    single { UiController(System.out, Scanner(System.`in`)) }

    // Auth
    singleOf(::LoginCli)
    factoryOf(::CreateUserCli)

    // Dashboard
    factoryOf(::AdminDashBoardCli)
    factoryOf(::MateDashBoardCli)

    // Project
    factoryOf(::ProjectShowMenu)
    factoryOf(::ProjectCliHelper)
    factoryOf(::TaskStateCliHelper)
    factoryOf(::CreateProjectCli)
    factoryOf(::UpdateProjectCli)
    factoryOf(::DeleteProjectCli)
    factoryOf(::GetAllProjectsCli)
    factoryOf(::ProjectScreenController)

    // Task
    factoryOf(::CreateTaskCli)
    factoryOf(::UpdateTaskCli)
    factoryOf(::DeleteTaskCli)
    factoryOf(::ViewSwimlanesCLI)

    // Task State
    factoryOf(::CreateTaskStateCli)
    factoryOf(::EditTaskStateCli)
    factoryOf(::DeleteTaskStateCli)
    factoryOf(::GetAllTaskStatesCli)
    factoryOf(::TaskStateInputHandler)
    factoryOf(::TaskStateCliController)

    // Audit
    factoryOf(::GetAllAuditsCli)

    // Entry point
    factoryOf(::MainCli)
}