package di

import domain.usecases.AuthenticationUseCase
import domain.usecases.CreateUserUseCase
import org.koin.dsl.module
import presentation.Cli.auth.CreateUserCli
import presentation.Cli.auth.LoginCli
import presentation.Cli.MainCli
import presentation.Cli.dashBoard.AdminDashBoardCli
import presentation.Cli.dashBoard.MateDashBoardCli
import presentation.UiController
import java.util.*

val presentationModel = module {
    single { UiController(System.out, Scanner(System.`in`)) }
    single { CreateUserUseCase(authRepository = get(), userValidator = get()) }
    single { AuthenticationUseCase(authRepository = get(), userValidator = get()) }
    single { AdminDashBoardCli(get(),get()) }
    single { MateDashBoardCli(get()) }
    single { LoginCli(get(), get(), get(), get()) }
    single { MainCli(get(), get()) }

    single { CreateUserCli() }

}