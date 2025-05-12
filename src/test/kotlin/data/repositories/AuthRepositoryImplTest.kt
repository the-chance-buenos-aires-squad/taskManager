package data.repositories

import com.google.common.truth.Truth.assertThat
import data.dataSource.util.PasswordHash
import domain.entities.User
import domain.entities.UserRole
import domain.repositories.UserRepository
import dummyData.DummyUser
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID
import kotlin.test.Test

class AuthRepositoryImplTest {


    private lateinit var authRepository: AuthRepositoryImpl
    private val userRepository: UserRepository = mockk()

    @BeforeEach
    fun setup() {
        authRepository = AuthRepositoryImpl(userRepository)
    }

    @Test
    fun `should return user when login succeeds`() = runTest {
        // given
        coEvery { userRepository.loginUser(adminUser.username, any()) } returns adminUser

        // when
        val result = authRepository.login(adminUser.username, "any")

        // then
        assertThat(result).isEqualTo(adminUser)
    }

    @Test
    fun `should set current user after successful login`() = runTest {
        // given
        coEvery { userRepository.loginUser(any(), any()) } returns adminUser

        // when
        authRepository.login(adminUser.username, "any")

        // then
        assertThat(authRepository.getCurrentUser()).isEqualTo(adminUser)
    }

    @Test
    fun `should return new user when adding user`() = runTest {
        // given
        coEvery { userRepository.addUser(any(), any()) } returns normalUser

        // when
        val result = authRepository.addUser("new", "pass")

        // then
        assertThat(result).isEqualTo(normalUser)
    }

    @Test
    fun `should not change current user when adding new user`() = runTest {
        // given
        coEvery { userRepository.addUser(any(), any()) } returns normalUser

        // when
        authRepository.addUser("new", "pass")

        // then
        assertThat(authRepository.getCurrentUser()).isNull()
    }

    @Test
    fun `should clear current user after logout`() = runTest {
        // given
        coEvery { userRepository.loginUser(any(), any()) } returns adminUser
        authRepository.login("admin", "pass")

        // when
        authRepository.logout()

        // then
        assertThat(authRepository.getCurrentUser()).isNull()
    }

    @Test
    fun `should return null when no user logged in`() = runTest {
        // when
        val result = authRepository.getCurrentUser()

        // then
        assertThat(result).isNull()
    }

    @Test
    fun `should update current user on subsequent logins`() = runTest {
        // given
        coEvery { userRepository.loginUser("admin", any()) } returns adminUser
        coEvery { userRepository.loginUser("mate", any()) } returns normalUser

        // when & then 1
        authRepository.login("admin", "admin1")
        assertThat(authRepository.getCurrentUser()).isEqualTo(adminUser)

        // when & then 2
        authRepository.logout()
        assertThat(authRepository.getCurrentUser()).isNull()

        // when & then 3
        authRepository.login("mate", "mateEe1")
        assertThat(authRepository.getCurrentUser()).isEqualTo(normalUser)
    }



    private val adminUser = User(
        id = UUID.fromString("00000000-0000-0000-0000-000000000001"),
        username = "admin",
        role = UserRole.ADMIN,
        createdAt = LocalDateTime.now()
    )

    private val normalUser = User(
        id = UUID.fromString("00000000-0000-0000-0000-000000000002"),
        username = "mate",
        role = UserRole.MATE,
        createdAt = LocalDateTime.now()
    )

}