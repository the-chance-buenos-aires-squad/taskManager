package domain.usecases.user

import com.google.common.truth.Truth.assertThat
import data.exceptions.UserNameAlreadyExistException
import domain.entities.ActionType
import domain.entities.EntityType
import domain.repositories.UserRepository
import dummyData.DummyUser
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test

class CreateUserUseCaseTest {


    private lateinit var createUserUseCase: CreateUserUseCase
    private val userRepository: UserRepository = mockk()

    private val dummyUser = DummyUser.dummyUserTwo

    @BeforeEach
    fun setup() {
        createUserUseCase = CreateUserUseCase(userRepository)
    }


    @Test
    fun `should return the new user when adding is successful`() = runTest {
        // given
        coEvery { userRepository.addUser(any(), any()) } returns dummyUser

        //when
        val result = createUserUseCase.execute("mateUserName", "password")

        // Then
        assertThat(result).isEqualTo(dummyUser)
    }

    //    UserNameAlreadyExistException
    @Test
    fun `should throw UserNameAlreadyExistException when creation failed if the user already exists`() = runTest{
        //given
        coEvery { userRepository.addUser(any(), any()) } throws UserNameAlreadyExistException()

        //when & then
        assertThrows<UserNameAlreadyExistException> { createUserUseCase.execute("mateUserName", "password") }

    }
}