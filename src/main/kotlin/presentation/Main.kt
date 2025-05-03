package presentation


import di.*
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.getKoin
import presentation.Cli.MainCli

fun main() {
    startKoin {
        modules(
            dataSourceModule,
            dataUtilModule,
            domainUtilModule,
            mapperModule,
            presentationModel,
            repositoryModule,
            useCaseModule
        )
    }
    val mainCli: MainCli = getKoin().get()
    mainCli.startCli()
}