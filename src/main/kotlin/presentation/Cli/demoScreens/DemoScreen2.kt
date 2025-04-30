package presentation.Cli.demoScreens

import presentation.Cli.Navigator
import presentation.Cli.NoPreviousScreenException
import presentation.Cli.Screen
import presentation.Cli.UiController


class DemoScreen2(
    private val navigator: Navigator,
    private val uiController: UiController
) : Screen {
    private var userName = ""
    private var password = ""
    override fun render() {
        uiController.printMessage("===========screen 2 ===========")
        uiController.printMessage(message = "1-log in: ")
        uiController.printMessage(message = "2. Back to screen 1")

        val input = uiController.readInput()
        handleInput(input)
    }

    private fun handleInput(input: String) {
        when (input) {
            "1" -> {
                uiController.printMessage(message = "username:")
                userName = uiController.readInput()
                handleInput("password")
            }

            "password" -> {
                uiController.printMessage("password:")
                password = uiController.readInput()
                uiController.printMessage("log in successful:$userName navigating back to screen 1")
                handleInput("2")
            }

            "2" -> {
                try {
                    navigator.navigateBack()
                } catch (e: NoPreviousScreenException) {
                    uiController.printMessage("Cannot go back: ${e.message}")
                    render()
                }
            }

            else -> {
                uiController.printMessage(message = "Invalid option. Please try again.")
                render()
            }
        }
    }


}