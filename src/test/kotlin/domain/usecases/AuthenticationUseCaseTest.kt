package domain.usecases

import com.google.common.truth.Truth.assertThat
import domain.entities.User
import domain.repositories.AuthRepository
import domain.validation.UserValidator
import dummyData.DummyUser
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test

class AuthenticationUseCaseTest {
    private val authRepository: AuthRepository = mockk()
    private val userValidator: UserValidator = mockk()
    private lateinit var useCase: AuthenticationUseCase

    private val testUser = DummyUser.dummyUserOne
    private val validUsername = testUser.username
    private val validPassword = "adminPassword"

    @BeforeEach
    fun setup() {
        useCase = AuthenticationUseCase(authRepository, userValidator)

        coEvery { authRepository.login(validUsername, validPassword) } returns testUser
    }

    @Test
    fun `should call repository with correct credentials when login is called`() = runTest {
        // when
        useCase.login(validUsername, validPassword)

        // then
        coVerify(exactly = 1) {
            authRepository.login(
                username = validUsername,
                password = validPassword
            )
        }
    }

    @Test
    fun `should return user from repository when login is successful`() = runTest {
        // when
        val result = useCase.login(validUsername, validPassword)

        // then
        assertThat(result).isEqualTo(testUser)
    }
}