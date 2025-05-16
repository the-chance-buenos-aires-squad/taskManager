package data.repositories

import auth.UserSession
import com.google.common.truth.Truth.assertThat
import data.dataSource.util.hash.PasswordHash
import data.dto.UserDto
import data.exceptions.FailedUserSaveException
import data.exceptions.InvalidCredentialsException
import data.exceptions.UserMateNotAllowedException
import data.exceptions.UserNameAlreadyExistException
import data.repositories.dataSource.UserDataSource
import data.repositories.mappers.UserDtoMapper
import domain.entities.ActionType
import domain.entities.EntityType
import domain.entities.User
import domain.entities.UserRole
import domain.repositories.AuditRepository
import domain.repositories.AuthRepository
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime
import java.util.*


class UserRepositoryImplTest {
    private lateinit var userRepository: UserRepositoryImpl
    private val userDataSource: UserDataSource = mockk(relaxed = true)
    private val mapper: UserDtoMapper = mockk(relaxed = true)
    private val hasher: PasswordHash = mockk()
    private val auditRepository:AuditRepository = mockk(relaxed = true)
    private val session: UserSession = mockk()

    @BeforeEach
    fun setup() {
        userRepository = UserRepositoryImpl(
            userDataSource = userDataSource,
            userMapper = mapper,
            md5Hash = hasher,
            auditRepository = auditRepository,
            userSession = session
        )
    }

    @Test
    fun `addUser when not logged in throws UserMateNotAllowedException`() = runTest {
        coEvery { session.getCurrentUser() } returns null

        assertThrows<UserMateNotAllowedException> {
            userRepository.addUser("user", "pass")
        }
    }

    @Test
    fun `addUser when current user is MATE throws UserMateNotAllowedException`() = runTest {
        val mateUser = adminUser.copy(role = UserRole.MATE)
        coEvery { session.getCurrentUser() } returns mateUser

        assertThrows<UserMateNotAllowedException> {
            userRepository.addUser("user", "pass")
        }
    }

    @Test
    fun `addUser with existing username throws UserNameAlreadyExistException`() = runTest {
        val existingUser = "existingUser"
        every { session.getCurrentUser() } returns adminUser
        coEvery { session.runIfLoggedIn(any<suspend (User) -> Boolean>()) } coAnswers {
            val action = firstArg<suspend (User) -> Boolean>()
            action(adminUser)
        }
        coEvery { userDataSource.getUserByUserName(existingUser) } returns adminDto

        assertThrows<UserNameAlreadyExistException> {
            userRepository.addUser(existingUser, "pass")
        }
    }

    @Test
    fun `addUser successfully saves user and returns User`() = runTest {
        val userName = "newUser"
        val password = "newPass"
        val hashedPassword = "hashedPass"

        every { session.getCurrentUser() } returns adminUser
        coEvery { session.runIfLoggedIn(any<suspend (User) -> Boolean>()) } coAnswers {
            val action = firstArg<suspend (User) -> Boolean>()
            action(adminUser)
        }
        coEvery { userDataSource.getUserByUserName(userName) } returns null
        every { hasher.generateHash(password) } returns hashedPassword
        coEvery { userDataSource.addUser(any()) } returns true
        every { mapper.toEntity(any()) } returns expectedUser
        coEvery { auditRepository.addAudit(any()) } returns true

        val result = userRepository.addUser(userName, password)

        assertThat(result).isEqualTo(expectedUser)
    }

    private val now = LocalDateTime.of(2025, 5, 1, 10, 0)
    private val adminId = UUID.fromString("11111111-1111-1111-1111-111111111111")
    private val mateId = UUID.fromString("11111111-1111-1111-1111-111111111112")

    private val adminDto = UserDto(
        id = adminId.toString(),
        username = "admin_user",
        password = "482c811da5d5b4bc6d497ffa98491e38", // md5("adminPass")
        role = UserRole.ADMIN,
        createdAt = now.toString()
    )

    private val adminUser = User(
        id =mateId,
        username = "admin",
        role = UserRole.ADMIN,
        createdAt = LocalDateTime.now()
    )

    private val expectedUser = User(
        id = UUID.randomUUID(),
        username = "newUser",
        role = UserRole.MATE,
        createdAt = LocalDateTime.now()
    )
}
