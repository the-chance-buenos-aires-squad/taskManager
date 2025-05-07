package data.repositories

import com.google.common.truth.Truth.assertThat
import data.dataSource.util.PasswordHash
import domain.repositories.UserRepository
import dummyData.DummyUser
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test

class AuthRepositoryImplTest {

    private lateinit var authRepository: AuthRepositoryImpl
    private var userRepository: UserRepository = mockk(relaxed = true)
    private var mD5Hasher: PasswordHash = mockk()

    private val testAdminUser = DummyUser.dummyUserOne
    private val testMateUser = DummyUser.dummyUserTwo

    @BeforeEach
    fun setup() {
        authRepository = AuthRepositoryImpl(userRepository, mD5Hasher)
    }

    @Test
    fun `should return user success when logging in with valid credentials`() = runTest{
        // given
        val username = testAdminUser.username
        val password = testAdminUser.password
        coEvery { userRepository.getUserByUserName(username) } returns testAdminUser
        every { mD5Hasher.hash(password) } returns testAdminUser.password

        // when
        val result = authRepository.login(username, password)

        // then
        assertThat(result).isEqualTo(testAdminUser)
    }

    @Test
    fun `should return null when user name not found`() = runTest{
        // given
        val username = testAdminUser.username
        val password = testAdminUser.password
        coEvery { userRepository.getUserByUserName(username) } returns null
        every { mD5Hasher.hash(password) } returns testAdminUser.password

        // when
        val result = authRepository.login(username, password)

        // then
        assertThat(result).isNull()
    }

    @Test
    fun `should return null when password not match`() = runTest{
        // given
        val username = testAdminUser.username
        val password = testAdminUser.password
        coEvery { userRepository.getUserByUserName(username) } returns testAdminUser
        every { mD5Hasher.hash(password) } returns "wrong password"

        // when
        val result = authRepository.login(username, password)

        // then
        assertThat(result).isNull()
    }

    @Test
    fun `should set current user when logging in with valid credentials`() = runTest {
        // given
        val username = testAdminUser.username
        val password = testAdminUser.password
        coEvery { userRepository.getUserByUserName(username) } returns testAdminUser
        every { mD5Hasher.hash(password) } returns testAdminUser.password

        // when
        authRepository.login(username, password)

        // then
        assertThat(authRepository.getCurrentUser()).isEqualTo(testAdminUser)
    }

    @Test
    fun `should return new user when adding user with admin logged in`() = runTest {
        // given
        coEvery { userRepository.getUserByUserName(testAdminUser.username) } returns testAdminUser
        every { mD5Hasher.hash(testAdminUser.password) } returns testAdminUser.password
        authRepository.login(testAdminUser.username, testAdminUser.password)

        coEvery { userRepository.getUserByUserName(testMateUser.username) } returns null
        every { mD5Hasher.hash(testMateUser.password) } returns testMateUser.password
        coEvery { userRepository.addUser(testMateUser) } returns true

        // when
        val result = authRepository.addUser(testMateUser.username, testMateUser.password)

        // then
        assertThat(result?.username).isEqualTo(testMateUser.username)
    }

    @Test
    fun `should return null when adding user without login`() = runTest {
        // given
        authRepository.logout()

        // when
        val result = authRepository.addUser("anyUser", "anyPass")

        // then
        assertThat(result).isNull()
    }

    @Test
    fun `should return null when mate user tries to add user`() = runTest {
        // given
        coEvery { userRepository.getUserByUserName(testMateUser.username) } returns testMateUser
        every { mD5Hasher.hash(testMateUser.password) } returns testMateUser.password
        authRepository.login(testMateUser.username, testMateUser.password)

        // when
        val result = authRepository.addUser(testMateUser.username, testMateUser.password)

        // then
        assertThat(result).isNull()
    }

    @Test
    fun `should return null when username already exists`() = runTest{
        // given

        coEvery { userRepository.getUserByUserName(testAdminUser.username) } returns testAdminUser
        every { mD5Hasher.hash(testAdminUser.password) } returns testAdminUser.password
        authRepository.login(testAdminUser.username, testAdminUser.password)

        coEvery { userRepository.getUserByUserName(testMateUser.username) } returns testMateUser
        every { mD5Hasher.hash(testMateUser.password) } returns testMateUser.password

        // when
        val result = authRepository.addUser(testMateUser.username, testMateUser.password)

        // then
        assertThat(result).isNull()
    }


    @Test
    fun `should update current user when logging in with new user`() = runTest{
        // given
        val firstUserLoggedIn = testAdminUser
        val newUserLoggedIn = testMateUser

        coEvery { userRepository.getUserByUserName(firstUserLoggedIn.username) } returns firstUserLoggedIn
        every { mD5Hasher.hash(firstUserLoggedIn.password) } returns firstUserLoggedIn.password
        authRepository.login(firstUserLoggedIn.username, firstUserLoggedIn.password)
        authRepository.logout()

        coEvery { userRepository.getUserByUserName(newUserLoggedIn.username) } returns newUserLoggedIn
        every { mD5Hasher.hash(newUserLoggedIn.password) } returns newUserLoggedIn.password

        //when
        authRepository.login(newUserLoggedIn.username, newUserLoggedIn.password)

        // then
        assertThat(authRepository.getCurrentUser()).isEqualTo(newUserLoggedIn)
    }

    @Test
    fun `should clear current user when logging out`() = runTest{
        // given
        coEvery { userRepository.getUserByUserName(testMateUser.username) } returns testMateUser
        every { mD5Hasher.hash(testMateUser.password) } returns testMateUser.password
        authRepository.login(testMateUser.username, testMateUser.password)

        // when
        authRepository.logout()

        // then
        assertThat(authRepository.getCurrentUser()).isNull()
    }


    @Test
    fun `should return current user when logged in`() = runTest{
        // given
        coEvery { userRepository.getUserByUserName(testMateUser.username) } returns testMateUser
        every { mD5Hasher.hash(testMateUser.password) } returns testMateUser.password
        authRepository.login(testMateUser.username, testMateUser.password)

        // when
        val result = authRepository.getCurrentUser()

        // then
        assertThat(result).isEqualTo(testMateUser)
    }

    @Test
    fun `should return null when no user is logged in`() = runTest{
        // when & then
        assertThat(authRepository.getCurrentUser()).isNull()
    }


}