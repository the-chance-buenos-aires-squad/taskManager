package presentation.Cli


import io.mockk.clearMocks
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class NavigatorTest {

    private lateinit var screen1: Screen
    private lateinit var screen2: Screen
    private lateinit var screenProvider: (String) -> Screen
    private lateinit var navigator: Navigator

    @BeforeEach
    fun setup() {
        screen1 = mockk(relaxed = true)
        screen2 = mockk(relaxed = true)

        val screensMap = mapOf(
            "screen1" to screen1,
            "screen2" to screen2
        )

        screenProvider = { route -> screensMap[route] ?: throw IllegalArgumentException("No screen for $route") }
        navigator = Navigator(screenProvider)
    }

    @Test
    fun `navigate to valid screen should render the screen`() {
        val result = navigator.navigate("screen1")

        verify(exactly = 1) { screen1.render() }
    }


    @Test
    fun `navigate back should go to previous screen`() {
        navigator.navigate("screen1")
        navigator.navigate("screen2")
        clearMocks(screen1, screen2) // clear first render calls

        val result = navigator.navigateBack()

        verify(exactly = 1) { screen1.render() }
        verify(exactly = 0) { screen2.render() }
    }


    @Test
    fun `navigate throws RouteNotFoundException on invalid route`() {
        assertThrows<RouteNotFoundException> { navigator.navigate(route = "invalid Route") }
    }


    @Test
    fun `navigateBack throws NoPreviousScreenException if history is empty`() {
        assertThrows<NoPreviousScreenException> { navigator.navigateBack() }
    }


}