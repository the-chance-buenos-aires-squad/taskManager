package presentation

import com.github.doyaaaaaken.kotlincsv.client.CsvReader
import com.github.doyaaaaaken.kotlincsv.client.CsvWriter
import data.dataSource.auditDataSource.CsvAuditDataSource
import data.dataSource.util.CsvHandler
import data.repositories.AuditRepositoryImpl
import data.repositories.mappers.AuditMapper
import di.Paths
import di.dataModule
import di.presentationModel
import domain.entities.ActionType
import domain.entities.Audit
import domain.entities.EntityType
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