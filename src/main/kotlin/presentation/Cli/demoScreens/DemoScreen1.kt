package presentation.Cli.demoScreens

import presentation.Cli.*

class DemoScreen1(
    private val navigator: Navigator,
    private val uiController: UiController
) : Screen {
    override fun render() {
        uiController.printMessage("===========screen 1 ===========")
        uiController.printMessage("1. Go to screen2")
        uiController.printMessage("2. Exit")
        uiController.printMessage(":", true)

        val input = uiController.readInput()
        handleInput(input)
    }

    private fun handleInput(input: String) {
        when (input) {
            "1" -> {
                try {
                    navigator.navigate(Routes.DEMO_SCREEN2_ROUTE)
                } catch (e: RouteNotFoundException) {
                    uiController.printMessage("Navigation failed: ${e.message}")
                    render() // Show screen again for user to retry
                }
            }

            "2" -> uiController.printMessage("Exiting application...")
            else -> {
                uiController.printMessage("Invalid option. Please try again.")
                render()
            }
        }
    }

}
