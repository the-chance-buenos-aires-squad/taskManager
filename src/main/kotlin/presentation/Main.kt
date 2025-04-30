package org.buinos.presentation

import di.presentationModel
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import presentation.Cli.Navigator
import presentation.Cli.Routes

fun main() {
    startKoin {
        modules(presentationModel)
    }
    val navigator = GlobalContext.get().get<Navigator>()
    navigator.navigate(Routes.DEMO_SCREEN1_ROUTE)

}