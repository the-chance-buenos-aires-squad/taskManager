package org.buinos

import org.buinos.presentation.LoginScreen
import org.buinos.presentation.LoginUseCase
import org.buinos.presentation.Navigator
import org.buinos.presentation.UiController

fun main() {

    val uiController = UiController()
    val loginUseCase = LoginUseCase()

    Navigator().apply {
        push(LoginScreen(loginUseCase, uiController))
        start()
    }

}