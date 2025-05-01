package domain.usecases

import com.google.common.truth.Truth.assertThat
import domain.customeExceptions.InvalidCredentialsException
import domain.customeExceptions.UserNameEmptyException
import domain.repositories.UserRepository
import dummyData.DummyUser
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test

class AuthenticationUseCaseTest {
    private var userRepository: UserRepository = mockk(relaxed = true)
    private val authUseCase = AuthenticationUseCase(userRepository)
    val firstUser = DummyUser.testUserOne

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

}