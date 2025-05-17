package data.repositories

import auth.FakeUserSession
import com.google.common.truth.Truth.assertThat
import data.dataSource.util.hash.PasswordHash
import data.dto.UserDto
import data.exceptions.InvalidCredentialsException
import data.repositories.mappers.UserDtoMapper
import domain.entities.User
import domain.entities.UserRole
import domain.repositories.UserRepository
import dummyData.DummyUser
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import presentation.exceptions.UserNotLoggedInException
import java.time.LocalDateTime
import java.util.UUID
import kotlin.test.Test

class AuthRepositoryImplTest {

    private lateinit var authRepository: AuthRepositoryImpl
    private val userRepository: UserRepository = mockk()
    private val mapper: UserDtoMapper = mockk(relaxed = true)
    private val hasher: PasswordHash = mockk()
    private val session = FakeUserSession()

    @BeforeEach
    fun setup() {
        authRepository = AuthRepositoryImpl(md5Hash = hasher, userRepository = userRepository, userMapper = mapper, session = session)
    }

    @Test
    fun `should return user when login succeeds`() = runTest {
        // given
        coEvery { userRepository.getUserByUserName(adminUser.username) } returns adminUserDto
        every { mapper.toType(adminUserDto) } returns adminUser
        every { hasher.generateHash(any()) } returns hashedPassword
        // when
        val result = authRepository.login(adminUser.username, hashedPassword)

        // then
        assertThat(result).isEqualTo(adminUser)
    }

    @Test
    fun `should throw InvalidCredentialsException when the no user found by the given name`() = runTest {
        // given
        coEvery { userRepository.getUserByUserName(adminUser.username) } returns  null

        // when
        // then
        assertThrows<InvalidCredentialsException> { authRepository.login(adminUser.username, password) }
    }

    @Test
    fun `should throw InvalidCredentialsException when the given hashed password is not matching`() = runTest {
        // given
        coEvery { userRepository.getUserByUserName(adminUser.username) } returns adminUserDto
        every { mapper.toType(adminUserDto) } returns adminUser
        every { hasher.generateHash(any()) } returns wrongHashedPassword

        // when
        // then
        assertThrows<InvalidCredentialsException> { authRepository.login(adminUser.username, password) }
    }


    @Test
    fun `should call setCurrentUser to null when logout`()= runTest{
        //when
        authRepository.logout()

        assertThat(session.getCurrentUser()).isNull()
    }


    private val adminUser = User(
        id = UUID.randomUUID(),
        username = "admin",
        role = UserRole.ADMIN,
        createdAt = LocalDateTime.now()
    )

    private val password = "123456"
    private val hashedPassword = "e10adc3949ba59abbe56e057f20f883e"
    private val wrongHashedPassword = "e10adc4444baddabbe5de057f20f883e"

    private var adminUserDto = UserDto(
        id = adminUser.id.toString(),
        username = adminUser.username,
        password = hashedPassword,
        role = adminUser.role,
        createdAt = adminUser.createdAt.toString()
    )

    private val normalUser = User(
        id = UUID.fromString("00000000-0000-0000-0000-000000000002"),
        username = "mate",
        role = UserRole.MATE,
        createdAt = LocalDateTime.now()
    )
    private val normalUserDto = UserDto(
        id = normalUser.id.toString(),
        username = normalUser.username,
        password = hashedPassword,
        role = normalUser.role,
        createdAt = ""
    )

}