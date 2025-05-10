package domain.usecases

import com.google.common.truth.Truth.assertThat
import domain.customeExceptions.InvalidCredentialsException
import domain.customeExceptions.UserNameEmptyException
import domain.repositories.AuthRepository
import domain.util.UserValidator
import dummyData.DummyUser
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test

class AuthenticationUseCaseTest {
    private var authRepository: AuthRepository = mockk(relaxed = true)
    private val userValidator = UserValidator()
    private val authenticationUseCase = AuthenticationUseCase(authRepository, userValidator)

    val firstUser = DummyUser.dummyUserOne

    @Test
    fun `should return user when valid credentials`() = runTest {
        //given
        coEvery { authRepository.login(firstUser.username, firstUser.password) } returns firstUser

        //when
        val result = authenticationUseCase.login(firstUser.username, "adminPassword")

        //then
        assertThat(result).isEqualTo(firstUser)

    }

    @Test
    fun `should throw InvalidCredentialsException for non-existing user`() = runTest {
        //given
        coEvery { authRepository.login(firstUser.username, firstUser.password) } returns null

        //when & then
        assertThrows<InvalidCredentialsException> {
            authenticationUseCase.login(firstUser.username, "adminPassword")
        }
    }

    @Test
    fun `should throw UserNameEmptyException for empty username`() = runTest {

        //when & then
        assertThrows<UserNameEmptyException> {
            authenticationUseCase.login(" ", "anypass")
        }
    }

    @Test
    fun `should throw InvalidCredentialsException for wrong password`() = runTest {

        //given
        coEvery { authRepository.login(firstUser.username, "wrongpass") } returns null

        //when & then
        assertThrows<InvalidCredentialsException> {
            authenticationUseCase.login(firstUser.username, "wrongpass")
        }
    }

    @Test
    fun `should throw InvalidCredentialsException when password has trailing space`() = runTest {
        // Given
        coEvery { authRepository.login(firstUser.username, "adminPassword ") } returns null

        // When & Then
        assertThrows<InvalidCredentialsException> {
            authenticationUseCase.login(firstUser.username, "adminPassword ")
        }
    }

    @Test
    fun `should return user when username has leading or trailing spaces`() = runTest {
        // Given
        val usernameWithSpaces = " ${firstUser.username}  "
        coEvery { authRepository.login(usernameWithSpaces, "adminPassword") } returns firstUser

        // When
        val result = authenticationUseCase.login(usernameWithSpaces, "adminPassword")

        // Then
        assertThat(result).isEqualTo(firstUser)
    }

    @Test
    fun `should throw InvalidCredentialsException when username case mismatch`() = runTest {
        // Given
        val caseDifferentUsername = firstUser.username.lowercase()
        coEvery { authRepository.login(caseDifferentUsername, firstUser.password) } returns null

        // When & Then
        assertThrows<InvalidCredentialsException> {
            authenticationUseCase.login(caseDifferentUsername, firstUser.password)
        }
    }

}