package presentation.cli

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.assertThrows
import presentation.UiController
import presentation.cli.auth.LoginCli
import kotlin.test.Ignore
import kotlin.test.Test

class MainCliTest {

    private val uiController: UiController = mockk(relaxed = true)
    private val loginCli: LoginCli = mockk(relaxed = true)
    private val mainCli: MainCli = MainCli(uiController, loginCli)

    @Test
    fun `should print welcome message`() = runTest {
        //given
        coEvery { uiController.readInput() } returns "1" andThenThrows RuntimeException("Exit loop")

        //when
        assertThrows<RuntimeException> { mainCli.startCli() }

        //then
        verify {
            uiController.printMessage(
                "========================================\n" +
                        "              Welcome to PlanMate!       \n" +
                        "========================================\n" +
                        "1. Login\n" +
                        "2. Exit\n" +
                        "Choose an option (1 - 2): "
            )
        }
    }

    @Test
    fun `should start login CLI on option 1`() = runTest {
        //given
        val userInput = "1"
        coEvery { uiController.readInput() }.returns(userInput) andThenThrows RuntimeException("Exit loop")

        //when
        assertThrows<RuntimeException> { mainCli.startCli() }

        //then
        coVerify {
            loginCli.start()
        }
    }

    @Ignore
    fun `should exit CLI on option 2`() = runTest {
        //given
        val userInput = "2"
        coEvery { uiController.readInput() }.returns(userInput)

        //when
        mainCli.startCli()

        // Then
        verify {
            uiController.printMessage("Exiting PlanMate... Goodbye!")
        }
    }

    @Test
    fun `should recall startCli on invalid input`() = runTest {
        //given
        val userInput = "3"
        coEvery { uiController.readInput() }.returns(userInput) andThenThrows RuntimeException("Exit loop")

        //when
        assertThrows<RuntimeException> { mainCli.startCli() }

        //then
        verify {
            uiController.printMessage("Invalid option! Please try again.")
        }
    }
}