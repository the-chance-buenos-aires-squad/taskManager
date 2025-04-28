package presentation

import io.mockk.*
import org.buinos.presentation.*
import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test

class LoginScreenTest {
    // ================================
    // 1. Pure Rendering Test
    // ================================
    @Test
    fun `render prints login banner`() {
        val mockUi = mockk<UiController>(relaxed = true)
        val screen = LoginScreen(LoginUseCase(), mockUi)

        screen.render()

        verify { mockUi.printMessage("=== Login ===") }
    }

    // ================================
    // 2. Input Handling Tests
    // ================================
    @Test
    fun `valid input triggers navigation`() {
        // 1. Create strict mocks (no relaxation)
        val mockUi = mockk<UiController>(relaxed = true)
        val mockNav = mockk<Navigator>()
        val mockUseCase = mockk<LoginUseCase>()

        // 2. Define exact UI interactions
        every { mockUi.printMessage("=== Login ===") } just Runs
        every { mockUi.printMessage("Username: ", isInline = true) } just Runs
        every { mockUi.printMessage("Password: ", isInline = true) } just Runs
        every { mockUi.readInput() } returnsMany listOf("valid", "valid") // First username, then password

        // 3. Define use case behavior
        every { mockUseCase.execute("valid", "valid") } returns true

        // 4. Define expected navigation calls
        every { mockNav.push(any<MainMenuScreen>()) } just Runs

        // 5. Execute test
        val screen = LoginScreen(mockUseCase, mockUi)
        screen.render() // Trigger UI prints
        screen.handleInput(mockNav)

        // 6. Verify ALL interactions in order
        verifySequence {
            // Render phase
            mockUi.printMessage("=== Login ===")

            // HandleInput phase
            mockUi.printMessage("Username: ", isInline = true)
            mockUi.readInput()
            mockUi.printMessage("Password: ", isInline = true)
            mockUi.readInput()

            // Navigation
            mockNav.push(any<MainMenuScreen>())
        }
    }



    @Test
    fun `empty input shows error`() {
        val mockUi = mockk<UiController>(relaxUnitFun = true)
        val mockNav = mockk<Navigator>(relaxUnitFun = true)
        val mockUseCase = mockk<LoginUseCase>()

        every { mockUi.readInput() } returns ""
        every { mockUseCase.execute(any(), any()) } returns false

        LoginScreen(mockUseCase, mockUi).handleInput(mockNav)

        verify { mockUi.printMessage("Invalid credentials!") }
        verify(exactly = 0) { mockNav.push(any()) }
    }
}