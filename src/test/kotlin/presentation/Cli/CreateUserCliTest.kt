package presentation.Cli

import domain.usecases.AuthenticationUseCase
import domain.usecases.CreateUserUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifySequence
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.Cli.CreateUserCli.Companion.CONFIRM_PASSWORD_PROMPT_MESSAGE
import presentation.Cli.CreateUserCli.Companion.HEADER_MESSAGE
import presentation.Cli.CreateUserCli.Companion.PASSWORD_PROMPT_MESSAGE
import presentation.Cli.CreateUserCli.Companion.USERNAME_PROMPT_MESSAGE
import presentation.Cli.CreateUserCli.Companion.USER_ROLE_PROMPT_MESSAGE
import presentation.UiController

class CreateUserCliTest{
    private val uiController: UiController = mockk()
    private val createUserUseCase: CreateUserUseCase = mockk()
    private val authenticationUseCase: AuthenticationUseCase = mockk()
    private lateinit var createUserCli: CreateUserCli

    @BeforeEach
    fun setUp(){
        createUserCli = CreateUserCli(uiController, createUserUseCase, authenticationUseCase)
    }

    @Test
    fun `should print header and username prompt before handle username input`(){
        //when
        createUserCli.start()

        //then
        verifySequence {
            uiController.printMessage(HEADER_MESSAGE)
            uiController.printMessage(USERNAME_PROMPT_MESSAGE,true)
            uiController.readInput()
        }
    }

    @Test
    fun `display password prompt message and read new input after having username `(){
        //when
        every { uiController.readInput() } returns "username input"

        createUserCli.start()
        //then
        verifySequence {
            uiController.printMessage(PASSWORD_PROMPT_MESSAGE,true)
            uiController.readInput()
        }
    }

    @Test
    fun `display confirm password prompt and read new input `(){
        //when
        every {  uiController.readInput()} returnsMany listOf("username input","password input")

        //then
        verify {
            uiController.printMessage(CONFIRM_PASSWORD_PROMPT_MESSAGE,true)
        }

    }

    @Test
    fun `display user Role choice prompt after reading confirm password input`(){
        //when
        every { uiController.readInput() } returnsMany listOf("username input","password input","confirmPassword input")

        //then
        verify {
            uiController.printMessage(USER_ROLE_PROMPT_MESSAGE,true)
        }

    }

    @Test
    fun `call addUser after reading username, password, confirmPassword and Role`(){
        //when
        every { uiController.readInput() } returnsMany listOf("username input","password input","confirmPassword input","role input")

        //then
        verify { createUserUseCase.addUser(any(),any(),any()) }
    }


}