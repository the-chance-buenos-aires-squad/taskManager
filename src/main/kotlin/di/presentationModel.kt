package di

import domain.usecases.AuthenticationUseCase
import domain.usecases.CreateUserUseCase
import org.koin.dsl.module
import presentation.cli.GetAllAuditsCli
import presentation.cli.MainCli
import presentation.cli.auth.CreateUserCli
import presentation.cli.auth.LoginCli
import presentation.cli.dashBoard.AdminDashBoardCli
import presentation.cli.dashBoard.MateDashBoardCli
import presentation.cli.TaskState.CreateTaskStateCli
import presentation.cli.TaskState.EditTaskStateCli
import presentation.cli.TaskState.GetAllTaskStatesCli
import presentation.cli.TaskState.TaskStateCliController
import presentation.UiController
import presentation.cli.project.CreateProjectCli
import presentation.cli.project.DeleteProjectCli
import presentation.cli.project.ProjectShowMenu
import presentation.cli.project.UpdateProjectCli
import presentation.cli.project.ProjectScreenController

import java.util.*


val presentationModel = module {
    single { UiController(System.out, Scanner(System.`in`)) }
    single { GetAllAuditsCli(getAllAuditUseCase = get(), uiController = get()) }
    single { MainCli(get(), get()) }
    single { CreateUserUseCase(authRepository = get(), userValidator = get(), addAuditUseCase = get()) }
    single { AuthenticationUseCase(authRepository = get(), userValidator = get()) }
    single { AdminDashBoardCli(get(), get()) }
    single { MateDashBoardCli(get()) }
    single { LoginCli(get(), get(), get(), get()) }
    single { MainCli(get(), get()) }

    single { CreateUserCli(uiController = get(), createUserUseCase = get()) }

    single { CreateProjectCli(get(), get()) }
    single { UpdateProjectCli(get(), get(), get()) }
    single { DeleteProjectCli(get(), get(), get()) }
    single { ProjectShowMenu(get()) }
    single { ProjectScreenController(get(),get(),get(),get(),get()) }
    single { CreateTaskStateCli(get(), get(), get(),get()) }
    single { EditTaskStateCli(get(), get(), get()) }
    single { DeleteProjectCli(get(),get(), get()) }
    single { GetAllTaskStatesCli(get(),get()) }
    single { TaskStateCliController(get(),get(),get(),get(),get(),get()) }
    single { MainCli(get(), get()) }
}