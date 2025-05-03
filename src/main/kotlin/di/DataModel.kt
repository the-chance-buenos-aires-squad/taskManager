package di


import com.github.doyaaaaaken.kotlincsv.client.CsvReader
import com.github.doyaaaaaken.kotlincsv.client.CsvWriter
import data.dataSource.auditDataSource.AuditDataSource
import data.dataSource.auditDataSource.CsvAuditDataSource
import data.dataSource.user.CsvUserDataSource
import data.dataSource.user.UserDataSource
import data.dataSource.util.CsvHandler
import data.repositories.AuditRepositoryImpl
import data.repositories.UserRepositoryImpl
import data.repositories.mappers.AuditMapper
import data.repositories.mappers.Mapper
import data.repositories.mappers.UserMapper
import domain.entities.Audit
import data.repositories.AuthRepositoryImpl
import domain.repositories.AuthRepository
import domain.repositories.AuditRepository
import domain.repositories.UserRepository
import data.dataSource.util.PasswordHasher
import domain.entities.User
import domain.util.UserValidator
import domain.usecases.AddAuditUseCase
import domain.usecases.GetAllAuditUseCase
import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.io.File


//
val dataModule = module {
    single { CsvWriter() }
    single { CsvReader() }
    single { CsvHandler(get(), get()) }

    single { PasswordHasher() }
    //todo which direction
    single { UserValidator() }

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
    single { GetAllAuditUseCase(auditRepositoryImpl = get()) }

    single{ UserMapper() }
    single<UserDataSource> { CsvUserDataSource(get(),get(Paths.UserFileQualifier)) }

    single<AuthRepository> { AuthRepositoryImpl(get(),get()) }
    single<UserRepository> { UserRepositoryImpl(get(),get()) }


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

object Files {
    val UserFile = File("src/main/kotlin/data/resource/users_file.csv")

}