package data.repositories

import com.google.common.truth.Truth.assertThat
import data.dataSource.util.hash.PasswordHash
import data.dto.UserDto
import data.exceptions.FailedUserSaveException
import data.exceptions.InvalidCredentialsException
import data.exceptions.UserNameAlreadyExistException
import data.repositories.dataSource.UserDataSource
import data.repositories.mappers.UserDtoMapper
import domain.entities.User
import domain.entities.UserRole
import domain.repositories.AuditRepository
import domain.repositories.AuthRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
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
    private val authRepository: Lazy<AuthRepository> = lazyOf(mockk<AuthRepository>())
    private val auditRepository:AuditRepository = mockk()

    @BeforeEach
    fun setup() {
        userRepository = UserRepositoryImpl(userDataSource, mapper, hasher,auditRepository)
    }

    @Test
    fun `should return true when adding a new user`() = runTest {
        // given
        val input = "test_user"
        val expected = "test_user"

        coEvery { userRepository.getUserByUserName(expected) } returns null
        coEvery { hasher.generateHash(any()) } returns "hash"
        coEvery { userDataSource.addUser(any()) } returns true

        // when
        userRepository.addUser(input, "pass")

        // then
        coVerify {
            userDataSource.addUser(
                match { dto -> dto.username == expected }
            )
        }
    }

    @Test
    fun `addUser throws UserNameAlreadyExistException when username exists`() = runTest {
        coEvery { userRepository.getUserByUserName("admin_user") } returns userAdmin

        assertThrows<UserNameAlreadyExistException> {
            userRepository.addUser("admin_user", "anyPass")
        }
    }

    @Test
    fun `addUser throws FailedUserSaveException when datasource fails to add`() = runTest {
        coEvery { userRepository.getUserByUserName("new_user") } returns null
        coEvery { hasher.generateHash("pass123") } returns "hash123"
        coEvery { userDataSource.addUser(any()) } returns false

        assertThrows<FailedUserSaveException> {
            userRepository.addUser("new_user", "pass123")
        }
    }

    @Test
    fun `should return user when searching by valid user id`() = runTest {
        coEvery { userDataSource.getUserByUserName("admin_user") } returns adminDto
        coEvery { hasher.generateHash("adminPass") } returns adminDto.password
        coEvery { mapper.toEntity(adminDto) } returns userAdmin

        val result = userRepository.loginUser("admin_user", "adminPass")

        assertThat(result.role).isEqualTo(UserRole.ADMIN)
    }

    @Test
    fun `loginUser throws InvalidCredentialsException when user not found`() = runTest {
        coEvery { userDataSource.getUserByUserName("ghost") } returns null

        assertThrows<InvalidCredentialsException> {
            userRepository.loginUser("ghost", "pass")
        }
    }

    @Test
    fun `loginUser throws InvalidCredentialsException when password does not match`() = runTest {
        coEvery { userDataSource.getUserByUserName("admin_user") } returns adminDto
        coEvery { hasher.generateHash("wrongPass") } returns "incorrectHash"

       assertThrows<InvalidCredentialsException> {
            userRepository.loginUser("admin_user", "wrongPass")
        }
    }

    @Test
    fun `getUserById returns mapped user when exists`() = runTest {
        coEvery { userDataSource.getUserById(adminId.toString()) } returns adminDto
        coEvery { mapper.toEntity(adminDto) } returns userAdmin

        val result = userRepository.getUserById(adminId)

        assertThat(result?.id).isEqualTo(adminId)
    }

    @Test
    fun `should return null when userName does not exist`() = runTest {
        coEvery { userDataSource.getUserById(regularId.toString()) } returns null

        val result = userRepository.getUserById(regularId)

        assertThat(result).isNull()
    }

    @Test
    fun `should return all users when retrieving users`() = runTest {
        coEvery { userDataSource.getUsers() } returns listOf(adminDto, mateDto)
        coEvery { mapper.toEntity(adminDto) } returns userAdmin
        coEvery { mapper.toEntity(mateDto) } returns userMate

        val result = userRepository.getUsers()

        assertThat(result).containsExactly(userAdmin, userMate)
    }

    @Test
    fun `updateUser should returns true when datasource update succeeds`() = runTest {
        coEvery { mapper.fromEntity(userMate) } returns mateDto
        coEvery { userDataSource.updateUser(mateDto) } returns true

        val result = userRepository.updateUser(userMate)

        assertThat(result).isTrue()
    }

    @Test
    fun `deleteUser should returns true when datasource delete succeeds`() = runTest {
        coEvery { userDataSource.deleteUser(regularId.toString()) } returns true

        val result = userRepository.deleteUser(userMate)

        assertThat(result).isTrue()
    }

    private val now = LocalDateTime.of(2025, 5, 1, 10, 0)
    private val adminId = UUID.fromString("11111111-1111-1111-1111-111111111111")
    private val regularId = UUID.fromString("22222222-2222-2222-2222-222222222222")

    private val adminDto = UserDto(
        id = adminId.toString(),
        username = "admin_user",
        password = "482c811da5d5b4bc6d497ffa98491e38", // md5("adminPass")
        role = UserRole.ADMIN,
        createdAt = now.toString()
    )
    private val mateDto = UserDto(
        id = regularId.toString(),
        username = "regular_user",
        password = "5e884898da28047151d0e56f8dc62927", // md5("regularPass")
        role = UserRole.MATE,
        createdAt = now.toString()
    )
    private val userAdmin = User(
        id = adminId,
        username = "admin_user",
        role = UserRole.ADMIN,
        createdAt = now
    )
    private val userMate = User(
        id = regularId,
        username = "regular_user",
        role = UserRole.MATE,
        createdAt = now
    )

}