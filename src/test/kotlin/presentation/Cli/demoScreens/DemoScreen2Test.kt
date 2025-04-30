package presentation.Cli.demoScreens

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifySequence
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.Cli.Navigator
import presentation.Cli.UiController

class DemoScreen2Test {

    private lateinit var navigator: Navigator
    private lateinit var uiController: UiController
    private lateinit var screen2: DemoScreen2

    @BeforeEach
    fun setUp() {
        navigator = mockk(relaxed = true)
        uiController = mockk(relaxed = true)
        screen2 = DemoScreen2(navigator, uiController)
    }

    @Test
    fun `login flow - input 1 then username then password`() {
        // Simulate: 1 -> username -> password -> 2 (to navigate back)
        every { uiController.readInput() } returnsMany listOf("1", "myUser", "myPass", "2")

        screen2.render()

        verifySequence {
            uiController.printMessage("===========screen 2 ===========")
            uiController.printMessage("1-log in: ")
            uiController.printMessage("2. Back to screen 1")
            uiController.readInput() // "1"

            uiController.printMessage("username:")
            uiController.readInput() // "myUser"

            uiController.printMessage("password:")
            uiController.readInput() // "myPass"

            uiController.printMessage("log in successful:myUser navigating back to screen 1")
            navigator.navigateBack()
        }
    }

    @Test
    fun `navigate back - input 2 success`() {
        every { uiController.readInput() } returns "2"

        screen2.render()

        verify { navigator.navigateBack() }
    }


    @Test
    fun `invalid input re-renders screen`() {
        every { uiController.readInput() } returnsMany listOf("xyz", "2")

        screen2.render()

        verify {
            uiController.printMessage("Invalid option. Please try again.")
            uiController.printMessage("===========screen 2 ===========")
        }
    }

}