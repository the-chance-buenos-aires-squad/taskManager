package presentation

import com.mongodb.kotlin.client.coroutine.MongoDatabase
import data.dataSource.user.MongoUserDataSource
import data.dto.UserDto
import di.*
import domain.entities.UserRole
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