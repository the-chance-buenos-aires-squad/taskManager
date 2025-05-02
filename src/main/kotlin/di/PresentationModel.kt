package di

import domain.usecases.AuthenticationUseCase
import domain.usecases.CreateUserUseCase
import org.koin.dsl.module
import presentation.Cli.CreateUserCli
import presentation.Cli.LoginCli
import presentation.Cli.MainCli
import presentation.UiController
import java.util.*


val presentationModel = module {
    single { UiController(System.out, Scanner(System.`in`)) }
    single { LoginCli(get()) }
    single { MainCli(get(),get()) }

    single { CreateUserUseCase(userRepository = get(), authRepository = get()) }
    single { AuthenticationUseCase(userRepository = get(), authRepository = get()) }
    single { CreateUserCli(uiController = get(), createUserUseCase = get() , authRepository = get())}
}