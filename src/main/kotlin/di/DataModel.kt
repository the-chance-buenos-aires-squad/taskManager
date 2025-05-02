package di

import com.github.doyaaaaaken.kotlincsv.client.CsvReader
import com.github.doyaaaaaken.kotlincsv.client.CsvWriter
import data.dataSource.auditDataSource.AuditDataSource
import data.dataSource.auditDataSource.CsvAuditDataSource
import data.dataSource.util.CsvHandler
import data.repositories.AuditRepositoryImpl
import data.repositories.mappers.AuditMapper
import data.repositories.mappers.Mapper
import domain.entities.Audit
import domain.repositories.AuditRepository
import domain.usecases.AddAuditUseCase
import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.io.File


val dataModule = module {
    single { CsvWriter() }
    single { CsvReader() }
    single { CsvHandler(get(), get()) }


    single<File>(qualifier = Paths.UserFileQualifier) {
        File(Paths.UserFilePath)
    }
    single<File>(qualifier = Paths.AuditFileQualifier) {
        File(Paths.AuditFilePath)
    }

    single<Mapper<Audit>> { AuditMapper() }


    single<AuditDataSource> { CsvAuditDataSource(csvHandler = get(), file = get(Paths.AuditFileQualifier)) }

    single<AuditRepository> { AuditRepositoryImpl(auditDataSource = get(), auditMapper = get()) }

    single { AddAuditUseCase(auditRepository = get()) }


}

object Paths {
    /*
    when injecting the file to data source use qualifier before injecting
    @Named("UserFilePath") private val userFile: File
     */
    const val UserFilePath = "src/main/kotlin/data/resource/users_file.csv"
    val UserFileQualifier: Qualifier = named("UserFilePath")

    const val AuditFilePath = "src/main/kotlin/data/resource/audit.csv"
    val AuditFileQualifier: Qualifier = named("AuditFile")


}
