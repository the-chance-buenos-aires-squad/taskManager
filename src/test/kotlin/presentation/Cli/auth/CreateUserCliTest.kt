package presentation.Cli.auth

import domain.customeExceptions.CreateUserException
import domain.customeExceptions.InvalidConfirmPasswordException
import domain.customeExceptions.UserNameEmptyException
import domain.usecases.CreateUserUseCase
import dummyData.DummyUser
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.UiController

class CreateUserCliTest {
    private lateinit var createUserCli: CreateUserCli
    private val uiController: UiController = mockk(relaxed = true)
    private val createUserUseCase: CreateUserUseCase = mockk()
    private val testUser = DummyUser.dummyUserTwo

    @BeforeEach
    fun setup() {
        createUserCli = CreateUserCli(uiController, createUserUseCase)
    }

    @Test
    fun `should create user successfully and show success message`() {
        // given
        every { uiController.readInput() } returnsMany listOf(
            testUser.username,
            testUser.password,
            testUser.password
        )
        every { createUserUseCase.addUser(any(), any(), any()) } returns testUser

        // when
        createUserCli.start()

        // then
        verify {
            uiController.printMessage(CreateUserCli.HEADER_MESSAGE)
            uiController.printMessage(CreateUserCli.USERNAME_PROMPT_MESSAGE)
            uiController.printMessage(CreateUserCli.PASSWORD_PROMPT_MESSAGE)
            uiController.printMessage(CreateUserCli.CONFIRM_PASSWORD_PROMPT_MESSAGE)
            uiController.printMessage("add new user mate mateUserName successfully")
        }
    }

    @Test
    fun `should show error when password confirmation fails`() {
        // given
        every { uiController.readInput() } returnsMany listOf(
            "testUser",
            "password1",
            "password2"
        )
        every { createUserUseCase.addUser(any(), any(), any()) } throws
                InvalidConfirmPasswordException()

        // when
        createUserCli.start()

        // then
        verify {
            uiController.printMessage("Error: Passwords do not match!")
        }
    }

    @Test
    fun `should handle empty username error`() {
        // given
        every { uiController.readInput() } returnsMany listOf("", "pass", "pass")
        every { createUserUseCase.addUser(any(), any(), any()) } throws
                UserNameEmptyException()

        // when
        createUserCli.start()

        // then
        verify {
            uiController.printMessage("Error: Username cannot be empty !")
        }
    }

    @Test
    fun `should handle existing username error`() {
        // given
        every { uiController.readInput() } returnsMany listOf("existingUser", "pass", "pass")
        every { createUserUseCase.addUser(any(), any(), any()) } throws
                CreateUserException()

        // when
        createUserCli.start()

        // then
        verify {
            uiController.printMessage("Error: Failed to create user")
        }
    }

}