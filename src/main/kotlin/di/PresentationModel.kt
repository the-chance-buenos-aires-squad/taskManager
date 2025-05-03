package di

import domain.usecases.AuthenticationUseCase
import domain.usecases.CreateUserUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import presentation.Cli.GetAllAuditsCli
import presentation.Cli.MainCli
import presentation.Cli.auth.CreateUserCli
import presentation.Cli.auth.LoginCli
import presentation.Cli.dashBoard.AdminDashBoardCli
import presentation.Cli.dashBoard.MateDashBoardCli
import presentation.Cli.projectClasses.*
import presentation.UiController
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
    singleOf(::ProjectScreenController)
    single { MainCli(get(), get()) }
}