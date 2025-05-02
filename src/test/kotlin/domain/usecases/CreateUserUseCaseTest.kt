package domain.usecases

import com.google.common.truth.Truth.assertThat
import domain.customeExceptions.InvalidConfirmPasswordException
import domain.customeExceptions.InvalidLengthPasswordException
import domain.customeExceptions.UserAlreadyExistException
import domain.customeExceptions.UserNameEmptyException
import domain.repositories.AuthRepository
import domain.repositories.UserRepository
import domain.util.MD5Hasher
import dummyData.DummyUser
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CreateUserUseCaseTest {
    private var userRepository: UserRepository = mockk(relaxed = true)
    private var authRepository: AuthRepository = mockk(relaxed = true)
    private val createUserUseCase = CreateUserUseCase(userRepository,authRepository)
    val firstUser = DummyUser.dummyUserOne
    val secondUser = DummyUser.dummyUserTwo
    val mD5Hash = MD5Hasher()


    @Test
    fun `should create admin successfully`() {
        //given
        every { userRepository.getUserByUserName(firstUser.username) } returns null

        //when
        createUserUseCase.addUser(
            username = firstUser.username,
            password = firstUser.password,
            confirmPassword = firstUser.password,
            userRole = firstUser.role
        )

        //then
        verify(exactly = 1) {
            userRepository.addUser(
                match { user ->
                    user.username == firstUser.username &&
                            user.role == firstUser.role
                }
            )
        }

    }

    @Test
    fun `should create mate successfully`() {
        //given
        every { userRepository.getUserByUserName(secondUser.username) } returns null

        //when
        createUserUseCase.addUser(
            username = secondUser.username,
            password = secondUser.password,
            confirmPassword = secondUser.password,
            userRole = secondUser.role
        )

        //then
        verify(exactly = 1) {
            userRepository.addUser(
                match { user ->
                    user.role == secondUser.role
                }
            )
        }

    }

    @Test
    fun `should return throw if userName already exists`() {
        // given
        every { userRepository.getUserByUserName(secondUser.username) } returns secondUser

        //when & then
        assertThrows<UserAlreadyExistException> {
            createUserUseCase.addUser(secondUser.username, secondUser.password, secondUser.password, secondUser.role)
        }

    }

    @Test
    fun `should return throw if userName empty`() {
        // given
        every { userRepository.getUserByUserName(secondUser.username) } returns null

        //when & then
        assertThrows<UserNameEmptyException> {
            createUserUseCase.addUser("", secondUser.password, secondUser.password, secondUser.role)
        }

    }

    @Test
    fun `should return throw if password length less than 6 `() {
        // given
        every { userRepository.getUserByUserName(secondUser.username) } returns null

        //when & then
        assertThrows<InvalidLengthPasswordException> {
            createUserUseCase.addUser(secondUser.username, "1234", secondUser.password, secondUser.role)
        }

    }


    @Test
    fun `should return throw if password not equal confirm password `() {
        // given
        every { userRepository.getUserByUserName(secondUser.username) } returns null

        //when & then
        assertThrows<InvalidConfirmPasswordException> {
            createUserUseCase.addUser(secondUser.username, secondUser.password, "invalid-confirm", secondUser.role)
        }

    }


    @Test
    fun `should hash password using MD5 utility`() {
        // Given
        val password = "test123"
        val expectedHash = mD5Hash.hash(password)

        // When
        val actualHash = mD5Hash.hash(password)

        // Then
        assertThat(actualHash).isEqualTo(expectedHash)
    }


    @Test
    fun `created user should have hashed password`() {
        // given
        val rawPassword = "rawPassword123"
        every { userRepository.getUserByUserName(any()) } returns null

        // when
        createUserUseCase.addUser(
            username = "newUser",
            password = rawPassword,
            confirmPassword = rawPassword
        )

        // then
        verify {
            userRepository.addUser(
                match { user ->
                    user.password == mD5Hash.hash(rawPassword)
                }
            )
        }
    }

}