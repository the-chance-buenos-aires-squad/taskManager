package di

import domain.usecases.AuthenticationUseCase
import domain.usecases.CreateUserUseCase
import domain.util.UserValidator
import org.koin.dsl.module
import presentation.Cli.CreateUserCli
import presentation.Cli.login.LoginCli
import presentation.Cli.MainCli
import presentation.Cli.login.AdminDashBoardCli
import presentation.Cli.login.MateDashBoardCli
import presentation.UiController
import java.util.*
import kotlin.math.sin


val presentationModel = module {
    single { UiController(System.out, Scanner(System.`in`)) }
    single { CreateUserUseCase(authRepository = get(), userValidator = get()) }
    single { AuthenticationUseCase(authRepository = get(), userValidator = get()) }
    single { AdminDashBoardCli() }
    single { MateDashBoardCli() }
    single { LoginCli(get(), get(), get(), get()) }
    single { MainCli(get(), get()) }

    single { CreateUserCli() }

}