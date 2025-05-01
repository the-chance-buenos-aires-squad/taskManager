package di

import org.koin.dsl.module
import presentation.UiController
import java.util.*


val PresentationModel = module {
    single { UiController(System.out, Scanner(System.`in`)) }
}