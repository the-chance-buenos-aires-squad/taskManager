package di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import presentation.Cli.GetAllAuditsCli
import presentation.Cli.LoginCli
import presentation.Cli.MainCli
import presentation.Cli.projectClasses.*
import presentation.UiController
import java.util.*


val presentationModel = module {
    single { UiController(System.out, Scanner(System.`in`)) }
    single { GetAllAuditsCli(getAllAuditUseCase = get(), uiController = get()) }
    single { LoginCli(get()) }
    single { MainCli(get(), get()) }
    single { CreateProjectCli(get(), get()) }
    single { UpdateProjectCli(get(), get(), get()) }
    single { DeleteProjectCli(get(), get(), get()) }
    single { ProjectShowMenu(get()) }
    singleOf(::ProjectScreenController)
    single { MainCli(get(), get()) }
}