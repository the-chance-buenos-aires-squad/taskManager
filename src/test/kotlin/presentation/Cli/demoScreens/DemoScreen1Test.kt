package presentation.Cli.demoScreens

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.Cli.Navigator
import presentation.Cli.RouteNotFoundException
import presentation.Cli.Routes
import presentation.Cli.UiController

class DemoScreen1Test {

    private val navigator = mockk<Navigator>(relaxed = true)
    private val uiController = mockk<UiController>(relaxed = true)
    private lateinit var screen: DemoScreen1

    @BeforeEach
    fun setup() {
        screen = DemoScreen1(navigator, uiController)
    }

    @Test
    fun `navigates to screen 2 when input is 1`() {
        every { uiController.readInput() } returns "1"

        screen.render()

        verify { navigator.navigate(Routes.DEMO_SCREEN2_ROUTE) }
    }

    @Test
    fun `prints exit message when input is 2`() {
        every { uiController.readInput() } returns "2"

        screen.render()

        verify { uiController.printMessage("Exiting application...") }
    }

    @Test
    fun `prints invalid option message on invalid input`() {
        every { uiController.readInput() } returnsMany listOf("invalid", "2")

        screen.render()

        verify { uiController.printMessage("Invalid option. Please try again.") }
        verify(exactly = 2) { uiController.readInput() }
    }

    @Test
    fun `should catch and handle RouteNotFoundException when navigating to screen2`() {
        // Arrange: simulate input "1" (Go to screen2), then "2" to exit in rerender
        every { uiController.readInput() } returnsMany listOf("1", "2")
        every { navigator.navigate(Routes.DEMO_SCREEN2_ROUTE) } throws RouteNotFoundException("screen2 not found")

        // Act
        screen.render()

        // Assert
        verify { navigator.navigate(Routes.DEMO_SCREEN2_ROUTE) }
        verify { uiController.printMessage(match { it.contains("Navigation failed") }) }
    }


}