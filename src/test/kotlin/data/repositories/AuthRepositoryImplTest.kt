package data.repositories

import com.google.common.truth.Truth.assertThat
import dummyData.DummyUser
import io.mockk.every
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test

class AuthRepositoryImplTest {

    private lateinit var authRepository: AuthRepositoryImpl
    private val testAdminUser = DummyUser.testUserOne
    private val testMateUser = DummyUser.testUserTwo

    @BeforeEach
    fun setup() {
        authRepository = AuthRepositoryImpl()
    }

    @Test
    fun `should set current user when logging in with no existing user`() {
        // when
        authRepository.login(testAdminUser)

        // then
        assertThat(testAdminUser).isEqualTo(authRepository.getCurrentUser())
    }

    @Test
    fun `should update current user when logging in with a new user`() {
        // given
        val initialUser = testAdminUser
        authRepository.login(initialUser)
        authRepository.logout()

        // when
        val newUser = testMateUser
        authRepository.login(newUser)

        // then
        assertThat(newUser).isEqualTo(authRepository.getCurrentUser())
    }

    @Test
    fun `should clear current user when logging out`() {
        // given
        authRepository.login(testMateUser)
        assertNotNull(authRepository.getCurrentUser())

        // when
        authRepository.logout()

        // then
        assertThat(authRepository.getCurrentUser()).isNull()
    }

    @Test
    fun `should do nothing when logging out with no current user`() {
        // given
        assertNull(authRepository.getCurrentUser())

        // when
        authRepository.logout()

        // Then
        assertThat(authRepository.getCurrentUser()).isNull()
    }

    @Test
    fun `should return current user when a user is logged in`() {
        // given
        authRepository.login(testMateUser)

        // when
        val result = authRepository.getCurrentUser()

        // then
        assertThat(result).isEqualTo(testMateUser)
    }

    @Test
    fun `should return null when getting current user with no logged-in user`() {
        // given
        authRepository.logout()

        // when
        val result = authRepository.getCurrentUser()

        // then
        assertThat(result).isNull()
    }

    @Test
    fun `currentUser should not be null when user logged in`() {
        // given
        authRepository.login(testMateUser)

        // when
        val currentUser = authRepository.getCurrentUser()


        // then
        assertThat(currentUser).isNotNull()
    }

    @Test
    fun `currentUser should be null when user not logged in`() {
        // given
        authRepository.logout()

        // when
        val currentUser = authRepository.getCurrentUser()

        // then
        assertThat(currentUser).isNull()
    }

}