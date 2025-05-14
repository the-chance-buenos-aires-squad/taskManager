package presentation.cli.auth

import data.exceptions.UserNameAlreadyExistException
import domain.usecases.user.CreateUserUseCase
import domain.validation.UserValidator
import dummyData.DummyUser
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.UiController
import presentation.exceptions.*

class CreateUserCliTest {
    private lateinit var createUserCli: CreateUserCli
    private val uiController: UiController = mockk(relaxed = true)
    private val createUserUseCase: CreateUserUseCase = mockk()
    private val userValidator : UserValidator = mockk(relaxed = true)
    private val testUser = DummyUser.dummyUserTwo

    @BeforeEach
    fun setup() {
        createUserCli = CreateUserCli(uiController, createUserUseCase,userValidator)
    }

    @Test
    fun `should create user successfully and show success message`() = runTest {
        // given
        coEvery { uiController.readInput() } returnsMany listOf(
            testUser.username,
            "matePassword",
            "matePassword"
        )
        coEvery { createUserUseCase.execute(any(), any()) } returns testUser

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
    fun `should show error when password confirmation failed`() = runTest {
        // given
        coEvery { uiController.readInput() } returnsMany listOf(
            "testUser",
            "password1",
            "password2"
        )
        val expectedException = InvalidConfirmPasswordException()
        coEvery { createUserUseCase.execute(any(), any()) } throws
                expectedException

        // when
        createUserCli.start()

        // then
        verify {
            uiController.printMessage("${expectedException.message}")
        }
    }

    @Test
    fun `should handle empty username error`() = runTest {
        // given
        coEvery { uiController.readInput() } returnsMany listOf("", "pass", "pass")
        val expectedException = UserNameEmptyException()
        coEvery { createUserUseCase.execute(any(), any()) } throws expectedException

        // when
        createUserCli.start()

        // then
        verify {
            uiController.printMessage("${expectedException.message}")
        }
    }

    @Test
    fun `should handle empty password error`() = runTest {
        // given
        coEvery { uiController.readInput() } returnsMany listOf("username", "", "pass")
        val expectedException = PasswordEmptyException()
        coEvery { createUserUseCase.execute(any(), any()) } throws expectedException

        // when
        createUserCli.start()

        // then
        verify {
            uiController.printMessage("${expectedException.message}")
        }
    }

    @Test
    fun `should handle invalid length password error`() = runTest {
        // given
        coEvery { uiController.readInput() } returnsMany listOf("username", "", "pass")
        val expectedException = InvalidLengthPasswordException()
        coEvery { createUserUseCase.execute(any(), any()) } throws expectedException

        // when
        createUserCli.start()

        // then
        verify {
            uiController.printMessage("${expectedException.message}")
        }
    }

    @Test
    fun `should handle existing username error`() = runTest {
        // given
        coEvery { uiController.readInput() } returnsMany listOf("existingUser", "pass", "pass")
        val expectedException = UserNameAlreadyExistException()
        coEvery { createUserUseCase.execute(any(), any()) } throws expectedException

        // when
        createUserCli.start()

        // then
        verify {
            uiController.printMessage("${expectedException.message}")
        }
    }

    @Test
    fun `should handle any repo exception error`() = runTest {
        // given
        coEvery { uiController.readInput() } returnsMany listOf("existingUser", "pass", "pass")
        val expectedException = Exception()
        coEvery { createUserUseCase.execute(any(), any()) } throws expectedException

        // when
        createUserCli.start()

        // then
        verify {
            uiController.printMessage("Error: ${expectedException.localizedMessage}")
        }
    }
}