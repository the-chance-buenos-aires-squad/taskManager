package domain.usecases

import com.google.common.truth.Truth.assertThat
import domain.customeExceptions.InvalidCredentialsException
import domain.customeExceptions.UserNameEmptyException
import domain.repositories.UserRepository
import domain.util.MD5Hash
import dummyData.DummyUser
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test

class AuthenticationUseCaseTest {
    private var userRepository: UserRepository = mockk(relaxed = true)
    private val authUseCase = AuthenticationUseCase(userRepository)
    val firstUser = DummyUser.dummyUserOne

    @Test
    fun `should return user when valid credentials`() {
        //given
        every { userRepository.getUserByUserName(firstUser.username) } returns firstUser

        //when
        val result = authUseCase.login(firstUser.username, "adminPassword")

        //then
        assertThat(result).isEqualTo(firstUser)

    }

    @Test
    fun `should throw InvalidCredentialsException for non-existing user`() {
        //given
        every { userRepository.getUserByUserName(firstUser.username) } returns null

        //when & then
        assertThrows<InvalidCredentialsException> {
            authUseCase.login(firstUser.username, "adminPassword")
        }
    }

    @Test
    fun `should throw UserNameEmptyException for empty username`() {

        //when & then
        assertThrows<UserNameEmptyException> {
            authUseCase.login(" ", "anypass")
        }
    }

    @Test
    fun `should throw InvalidCredentialsException for wrong password`() {

        //given
        every { userRepository.getUserByUserName(firstUser.username) } returns firstUser

        //when & then
        assertThrows<InvalidCredentialsException> {
            authUseCase.login(firstUser.username, "wrongpass")
        }
    }

    @Test
    fun `should throw InvalidCredentialsException when password has trailing space`() {
        // Given
        every { userRepository.getUserByUserName(firstUser.username) } returns firstUser

        // When & Then
        assertThrows<InvalidCredentialsException> {
            authUseCase.login(firstUser.username, "adminPassword ")
        }
    }

    @Test
    fun `should return user when username has leading or trailing spaces`() {
        // Given
        val usernameWithSpaces = " ${firstUser.username}  "
        every { userRepository.getUserByUserName(firstUser.username) } returns firstUser

        // When
        val result = authUseCase.login(usernameWithSpaces, "adminPassword")

        // Then
        assertThat(result).isEqualTo(firstUser)
    }


    @Test
    fun `should throw InvalidCredentialsException when username case mismatch`() {
        // Given
        val caseDifferentUsername = firstUser.username.lowercase()
        every { userRepository.getUserByUserName(caseDifferentUsername) } returns null

        // When & Then
        assertThrows<InvalidCredentialsException> {
            authUseCase.login(caseDifferentUsername, "adminPassword")
        }
    }

    @Test
    fun `should return user when password has special characters`() {
        // Given
        val complexPassword = "P@ssw0rd!123"
        val complexPasswordUser = firstUser.copy(password = MD5Hash.hash(complexPassword))
        every { userRepository.getUserByUserName(complexPasswordUser.username) } returns complexPasswordUser

        // When
        val result = authUseCase.login(complexPasswordUser.username, complexPassword)

        // Then
        assertThat(result).isEqualTo(complexPasswordUser)
    }

}