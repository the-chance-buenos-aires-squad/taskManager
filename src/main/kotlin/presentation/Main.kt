package presentation

import di.*
import di.dataUtilModule
import di.dataSourceModule
import di.domainUtilModule
import di.mapperModule
import di.presentationModule
import di.repositoryModule
import di.useCaseModule
import di.mongoModule
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