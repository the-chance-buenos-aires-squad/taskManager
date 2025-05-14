package domain.usecases.auth

import com.google.common.truth.Truth.assertThat
import domain.repositories.AuthRepository
import dummyData.DummyUser
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test

class LoginUseCaseTest {
    private val authRepository: AuthRepository = mockk()
    private lateinit var useCase: LoginUseCase

    private val testUser = DummyUser.dummyUserOne
    private val validUsername = testUser.username
    private val validPassword = "adminPassword"

    @BeforeEach
    fun setup() {
        useCase = LoginUseCase(authRepository)

        coEvery { authRepository.login(validUsername, validPassword) } returns testUser
    }

    @Test
    fun `should call repository with correct credentials when login is called`() = runTest {
        // when
        useCase.execute(validUsername, validPassword)

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
        val result = useCase.execute(validUsername, validPassword)

        // then
        assertThat(result).isEqualTo(testUser)
    }
}