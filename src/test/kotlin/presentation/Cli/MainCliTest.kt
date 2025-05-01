package presentation.Cli

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import presentation.UiController
import kotlin.test.Test

class MainCliTest {

    private val uiController: UiController = mockk(relaxed = true)
    private val loginCli: LoginCli = mockk(relaxed = true)
    private val mainCli: MainCli = MainCli(uiController, loginCli)


    @Test
    fun `should print welcome message`() {

        //when
        mainCli.startCli()

        //then
        verify {
            uiController.printMessage(
                """
            ========================================
                     Welcome to PlanMate!        
            ========================================
            1. Login
            2. Exit
            Choose an option (1 - 2): 
            """
            )
        }

    }

    @Test
    fun `should start login CLI on option 1`() {

        //given
        val userInput = "1"
        every { uiController.readInput() }.returns(userInput)

        //when
        mainCli.startCli()

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
    fun `should start main cli when user valid input`() {

        //given
        val userInput = "3"
        every { uiController.readInput() }.returns(userInput)

        //when
        mainCli.startCli()

        //then
        verify(exactly = 1) {
            uiController.printMessage(
                """
            ========================================
                     Welcome to PlanMate!        
            ========================================
            1. Login
            2. Exit
            Choose an option (1 - 2): 
            """
            )
        }


    }

}