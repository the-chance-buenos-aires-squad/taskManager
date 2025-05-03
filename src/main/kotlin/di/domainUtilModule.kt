package di

import domain.util.UserValidator
import org.koin.dsl.module


val domainUtilModule= module {
    single { UserValidator() }
}
