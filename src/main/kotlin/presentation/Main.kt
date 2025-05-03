package presentation

import di.dataModule
import di.logicModule
import di.presentationModel
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.getKoin
import presentation.Cli.MainCli
import presentation.Cli.projectClasses.ProjectScreenController

fun main() {
    startKoin {
        modules(
            dataModule, presentationModel, logicModule
        )
    }
    val mainCli: MainCli = getKoin().get()
    mainCli.startCli()
}