package di

import domain.validation.UserValidator
import org.koin.dsl.module


val domainUtilModule = module {
    single { UserValidator() }
}