package org.buinos.presentation

import kotlin.system.exitProcess

class MainMenuScreen(
    ui: UiController,
    private val username: String
) : Screen(ui) {
    override fun render() {
        ui.printMessage("\n=== Main Menu ===")
        ui.printMessage("Welcome, $username!")
        ui.printMessage("1. Do nothing")
        ui.printMessage("2. Logout")
        ui.printMessage("3. terminate app....")
    }

    override fun handleInput(navigator: Navigator) {
        when (ui.readInput()) {
            "1" -> ui.printMessage("Doing nothing...")
            "2" -> navigator.pop()
            "3" -> exitProcess(0)
            else -> ui.printMessage("Invalid choice!")
        }
    }
}