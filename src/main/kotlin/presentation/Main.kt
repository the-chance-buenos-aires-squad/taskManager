package presentation

import di.dataModule
import di.presentationModel
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.getKoin
import presentation.Cli.MainCli

fun main() {
    println("Hello World!")
    startKoin {
        modules(
            dataModule, presentationModel
        )
    }

    val mainCli: MainCli = getKoin().get()
    mainCli.startCli()
}