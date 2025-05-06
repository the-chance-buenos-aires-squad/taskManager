package presentation.cli

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.assertThrows
import presentation.UiController
import presentation.cli.auth.LoginCli
import kotlin.test.Test

class MainCliTest {

    private val uiController: UiController = mockk(relaxed = true)
    private val loginCli: LoginCli = mockk(relaxed = true)
    private val mainCli: MainCli = MainCli(uiController, loginCli)

    @Test
    fun `should print welcome message`() {
        //given
        every { uiController.readInput() } returns "1" andThenThrows RuntimeException("Exit loop")

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
    fun `should start login CLI on option 1`() {

        //given
        val userInput = "1"
        every { uiController.readInput() }.returns(userInput) andThenThrows RuntimeException("Exit loop")

        //when
        assertThrows<RuntimeException> { mainCli.startCli() }

        //then
        verify {
            loginCli.start()
        }

    }

    @Test
    fun `should exit CLI on option 2`() {

        //given
        val userInput = "2"
        every { uiController.readInput() }.returns(userInput)

        //when
        mainCli.startCli()

        //then
        verify {
            uiController.printMessage("Exiting PlanMate... Goodbye!")
        }

    }

    @Test
    fun `should recall startCli on invalid input`() {

        //given
        val userInput = "3"
        every { uiController.readInput() }.returns(userInput) andThenThrows RuntimeException("Exit loop")

        //when
        assertThrows<RuntimeException> { mainCli.startCli() }

        //then
        verify {
            uiController.printMessage("Invalid option! Please try again.")
        }

    }

}