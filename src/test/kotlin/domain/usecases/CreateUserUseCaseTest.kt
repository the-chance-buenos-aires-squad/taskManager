package domain.usecases

import com.google.common.truth.Truth.assertThat
import domain.customeExceptions.CreateUserException
import domain.customeExceptions.InvalidLengthPasswordException
import domain.customeExceptions.PasswordEmptyException
import domain.customeExceptions.UserNameEmptyException
import domain.repositories.AuthRepository
import domain.validation.UserValidator
import dummyData.DummyUser
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test

class CreateUserUseCaseTest {

    private lateinit var createUserUseCase: CreateUserUseCase
    private val authRepository: AuthRepository = mockk(relaxed = true)
    private val userValidator: UserValidator = mockk(relaxed = true)
    private val dummyUser = DummyUser.dummyUserTwo
    private var addAuditUseCase: AddAuditUseCase = mockk(relaxed = true)

    @BeforeEach
    fun setup() {
        createUserUseCase = CreateUserUseCase(authRepository, userValidator, addAuditUseCase)
    }

    @Test
    fun `should create user successfully with valid data`() = runTest {
        // given
        coEvery {
            authRepository.addUser(dummyUser.username, dummyUser.password)
        } returns dummyUser

        // when
        val result = createUserUseCase.addUser(
            dummyUser.username,
            dummyUser.password,
            dummyUser.password
        )

        // then
        assertThat(result).isEqualTo(dummyUser)
    }

    @Test
    fun `should return throw CreateUserException when Failed to create user`() = runTest {
        // given
        coEvery { authRepository.addUser(any(), any()) } returns null

        // when & then
        assertThrows<CreateUserException> {
            createUserUseCase.addUser(
                dummyUser.username,
                dummyUser.password,
                dummyUser.password
            )
        }
    }

    @Test
    fun `should return throw UserNameEmptyException if userName empty`() = runTest {
        coEvery { userValidator.validateUsername(any()) } throws UserNameEmptyException()

        assertThrows<UserNameEmptyException> {
            createUserUseCase.addUser("", dummyUser.password, dummyUser.password)
        }
    }

    @Test
    fun `should return throw PasswordEmptyException when password is empty`() = runTest {
        coEvery { userValidator.validatePassword(any(), any()) } throws PasswordEmptyException()

        assertThrows<PasswordEmptyException> {
            createUserUseCase.addUser(dummyUser.username, "", dummyUser.password)
        }
    }

    @Test
    fun `should return throw PasswordEmptyException when confirm password is empty`() = runTest {
        coEvery { userValidator.validatePassword(any(), any()) } throws PasswordEmptyException()

        assertThrows<PasswordEmptyException> {
            createUserUseCase.addUser(dummyUser.username, dummyUser.password, "")
        }
    }

    @Test
    fun `should return throw if password length less than 6`() = runTest {
        coEvery { userValidator.validatePassword(any(), any()) } throws InvalidLengthPasswordException()

        assertThrows<InvalidLengthPasswordException> {
            createUserUseCase.addUser(dummyUser.username, "1234", dummyUser.password)
        }
    }

    @Test
    fun `should return pass if password match confirm password`() = runTest {
        coEvery { userValidator.validatePassword(any(), any()) } throws InvalidLengthPasswordException()

        assertThrows<InvalidLengthPasswordException> {
            createUserUseCase.addUser(dummyUser.username, "1234", dummyUser.password)
        }
    }

    @Test
    fun `should return throw CreateUserException if user don't exist`() = runTest {
        coEvery { authRepository.addUser(any(), any()) } throws CreateUserException()

        assertThrows<CreateUserException> {
            authRepository.addUser("", "")
        }
    }
}