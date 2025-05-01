package domain.usecases

import com.google.common.truth.Truth.assertThat
import domain.customeExceptions.InvalidConfirmPasswordException
import domain.customeExceptions.InvalidLengthPasswordException
import domain.customeExceptions.UserAlreadyExistException
import domain.customeExceptions.UserNameEmptyException
import domain.repositories.UserRepository
import domain.util.MD5Hash
import dummyData.DummyUser
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CreateUserUseCaseTest {
    private var userRepository: UserRepository = mockk(relaxed = true)
    private val createUserUseCase = CreateUserUseCase(userRepository)
    val firstUser = DummyUser.testUserOne
    val secondUser = DummyUser.testUserTwo

    @Test
    fun `should create admin successfully`() {
        //given
        every { userRepository.getUserByUserName(firstUser.username) } returns null

        //when
        createUserUseCase.createUser(
            username = firstUser.username,
            password = firstUser.password,
            confirmPassword = firstUser.password,
            userRole = firstUser.role
        )

        //then
        verify(exactly = 1) {
            userRepository.insertUser(
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
        createUserUseCase.createUser(
            username = secondUser.username,
            password = secondUser.password,
            confirmPassword = secondUser.password,
            userRole = secondUser.role
        )

        //then
        verify(exactly = 1) {
            userRepository.insertUser(
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
            createUserUseCase.createUser(secondUser.username, secondUser.password, secondUser.password, secondUser.role)
        }

    }

    @Test
    fun `should return throw if userName empty`() {
        // given
        every { userRepository.getUserByUserName(secondUser.username) } returns null

        //when & then
        assertThrows<UserNameEmptyException> {
            createUserUseCase.createUser("", secondUser.password, secondUser.password, secondUser.role)
        }

    }

    @Test
    fun `should return throw if password length less than 6 `() {
        // given
        every { userRepository.getUserByUserName(secondUser.username) } returns null

        //when & then
        assertThrows<InvalidLengthPasswordException> {
            createUserUseCase.createUser(secondUser.username, "1234", secondUser.password, secondUser.role)
        }

    }


    @Test
    fun `should return throw if password not equal confirm password `() {
        // given
        every { userRepository.getUserByUserName(secondUser.username) } returns null

        //when & then
        assertThrows<InvalidConfirmPasswordException> {
            createUserUseCase.createUser(secondUser.username, secondUser.password, "invalid-confirm", secondUser.role)
        }

    }


    @Test
    fun `should hash password using MD5 utility`() {
        // Given
        val password = "test123"
        val expectedHash = MD5Hash.hash(password)

        // When
        val actualHash = MD5Hash.hash(password)

        // Then
        assertThat(actualHash).isEqualTo(expectedHash)
    }


    @Test
    fun `created user should have hashed password`() {
        // Arrange
        val rawPassword = "rawPassword123"
        every { userRepository.getUserByUserName(any()) } returns null

        // Act
        createUserUseCase.createUser(
            username = "newUser",
            password = rawPassword,
            confirmPassword = rawPassword
        )

        // Assert
        verify {
            userRepository.insertUser(
                match { user ->
                    user.password == MD5Hash.hash(rawPassword)
                }
            )
        }
    }

}