package di

import domain.usecases.AuthenticationUseCase
import domain.usecases.CreateUserUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import presentation.cli.GetAllAuditsCli
import presentation.cli.MainCli
import presentation.cli.auth.CreateUserCli
import presentation.cli.auth.LoginCli
import presentation.cli.dashBoard.AdminDashBoardCli
import presentation.cli.dashBoard.MateDashBoardCli

import presentation.UiController
import presentation.cli.projectClasses.ProjectShowMenu
import presentation.cli.projectClasses.CreateProjectCli
import presentation.cli.projectClasses.UpdateProjectCli
import presentation.cli.projectClasses.DeleteProjectCli
import presentation.cli.projectClasses.GetAllProjectsCli
import presentation.cli.projectClasses.ProjectScreenController
import presentation.cli.TaskState.CreateTaskStateCli
import presentation.cli.TaskState.EditTaskStateCli
import presentation.cli.TaskState.GetAllTaskStatesCli
import presentation.cli.TaskState.TaskStateCliController
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

    single { ProjectShowMenu(get()) }
    single { CreateProjectCli(get(), get()) }
    single { UpdateProjectCli(get(), get(), get()) }
    single { DeleteProjectCli(get(), get(), get()) }
    single { GetAllProjectsCli(get(),get()) }
    singleOf(::ProjectScreenController)
    single { CreateTaskStateCli(get(), get(), get(),get()) }
    single { EditTaskStateCli(get(), get(), get()) }
    single { GetAllTaskStatesCli(get(),get()) }
    singleOf(::TaskStateCliController)

    singleOf(::MainCli)
}