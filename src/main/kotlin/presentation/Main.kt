package org.buinos.presentation

import org.koin.java.KoinJavaComponent.getKoin
import presentation.Cli.LoginCli
import presentation.Cli.MainCli
import presentation.UiController

fun main() {
    println("Hello World!")

    val uiController = UiController()
    val loginCli = LoginCli(uiController)
    val holderCli = MainCli(uiController, loginCli)
    holderCli.startCli()
}