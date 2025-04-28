package presentation

import com.google.common.truth.Truth.assertThat
import io.mockk.*
import org.buinos.presentation.*
import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test

class NavigatorTest{


    @Test
    fun `navigator manages screen stack correctly`() {
        val mockUi :UiController= mockk(relaxed = true)
        val navigator = Navigator()

        // Push two screens
        navigator.push(LoginScreen(LoginUseCase(), mockUi))
        navigator.push(MainMenuScreen(mockUi, "test"))
        assertEquals(2, navigator.getStackScreenCounts())

        // Pop one screen
        navigator.pop()
        assertEquals(1, navigator.getStackScreenCounts())
    }


    @Test
    fun `pop removes top screen from stack`() {
        val navigator = Navigator()
        val screenA = mockk<Screen>(relaxed = true)
        val screenB = mockk<Screen>(relaxed = true)

        // Push two screens: [A, B]
        navigator.push(screenA)
        navigator.push(screenB)

        // Pop B → [A]
        navigator.pop()


        assertThat(navigator.getTopScreen()).isEqualTo(screenA)
    }


    @Test
    fun `start renders the given first screen`() {
        val navigator = Navigator()
        val screen = mockk<Screen>(relaxed = true) {
            // Make the screen remove itself during input handling
            // so the navigator loop wil end
            every { handleInput(navigator) } answers {
                navigator.pop()
            }
        }

        navigator.push(screen)
        navigator.start()

        verify { screen.render() }
    }

}