package presentation

import di.*
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.getKoin
import presentation.cli.MainCli

fun main() {
    startKoin {
        modules(
            dataUtilModule,
            dataSourceModule,
            domainUtilModule,
            mapperModule,
            presentationModule,
            repositoryModule,
            useCaseModule
        )
    }
    val mainCli: MainCli = getKoin().get()
    mainCli.startCli()
}