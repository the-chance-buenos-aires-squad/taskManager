package presentation

import com.github.doyaaaaaken.kotlincsv.client.CsvReader
import com.github.doyaaaaaken.kotlincsv.client.CsvWriter
import data.dataSource.auditDataSource.CsvAuditDataSource
import data.dataSource.user.CsvUserDataSource
import data.dataSource.user.UserDataSource
import data.dataSource.util.CsvHandler
import data.repositories.AuditRepositoryImpl
import data.repositories.UserRepositoryImpl
import data.repositories.mappers.AuditMapper
import data.repositories.mappers.Mapper
import data.repositories.mappers.UserMapper
import di.Paths
import di.dataModule
import di.presentationModel
import domain.entities.*
import domain.repositories.UserRepository
import domain.usecases.AddAuditUseCase
import domain.usecases.GetAllAuditUseCase
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.getKoin
import presentation.Cli.GetAllAuditsCli
import presentation.Cli.MainCli
import presentation.UiController
import java.io.File
import java.io.PrintStream
import java.time.LocalDateTime
import java.util.*

fun main() {
    startKoin {
        modules(
            dataModule, presentationModel
        )
    }
    val mainCli: MainCli = getKoin().get()
    mainCli.startCli()
}