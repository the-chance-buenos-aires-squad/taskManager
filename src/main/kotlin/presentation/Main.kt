package presentation

import di.*
import kotlinx.coroutines.runBlocking
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.getKoin
import presentation.cli.MainCli

fun main() = runBlocking {
    startKoin {
        modules(
            dataSourceModule,
            dataUtilModule,
            domainUtilModule,
            mapperModule,
            presentationModule,
            repositoryModule,
            useCaseModule,
            mongoModule
        )
    }
    val mainCli: MainCli = getKoin().get()
    mainCli.startCli()
}
