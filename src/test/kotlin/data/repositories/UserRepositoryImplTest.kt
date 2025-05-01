package data.repositories

import com.google.common.truth.Truth.assertThat
import data.dataSource.UserCsvDataSource
import dummyData.DummyUser.testUserOne
import dummyData.DummyUser.testUserTwo
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class UserRepositoryImplTest {

    private val mockDataSource = mockk<UserCsvDataSource>()
    private lateinit var userRepository: UserRepositoryImpl

    @BeforeEach
    fun setUp() {
        userRepository = UserRepositoryImpl(mockDataSource)
    }

    @Test
    fun `should return true when adding a new user`() {
        //given
        every { mockDataSource.insertUser(testUserOne) } returns true

        //when
        val result = userRepository.insertUser(testUserOne)

        //then
        assertThat(result).isTrue()
    }

    @Test
    fun `should return user when searching by valid user id`() {
        //given
        every { mockDataSource.getUserById("1") } returns testUserOne

        //when
        val result = userRepository.getUserById("1")

        //then
        assertThat(result).isEqualTo(testUserOne)
    }

    @Test
    fun `should return null when user id does not exist`() {
        //given
        every { mockDataSource.getUserById("3") } returns null

        //when
        val result = userRepository.getUserById("3")

        //then
        assertThat(result).isNull()
    }

    @Test
    fun `should return user when searching by valid userName`() {
        //given
        every { mockDataSource.getUserByUserName("adminUserName") } returns testUserOne

        //when
        val result = userRepository.getUserByUserName("adminUserName")

        //then
        assertThat(result).isEqualTo(testUserOne)
    }

    @Test
    fun `should return null when userName does not exist`() {
        //given
        every { mockDataSource.getUserByUserName("testUser3") } returns null

        //when
        val result = userRepository.getUserByUserName("testUser3")

        //then
        assertThat(result).isNull()
    }

    @Test
    fun `should return all users when retrieving users`() {
        //given
        every { mockDataSource.getUsers() } returns listOf(testUserOne, testUserTwo)

        //when
        val result = userRepository.getUsers()

        //then
        assertThat(result.size).isEqualTo(2)
    }

    @Test
    fun `should call updateItem on data source when updating a user`() {
        //given
        every { mockDataSource.updateUser(testUserOne) } returns true

        //when
        userRepository.updateUser(testUserOne)

        //then
        verify(exactly = 1) { mockDataSource.updateUser(testUserOne) }
    }

    @Test
    fun `should call deleteItem on data source when deleting a user`() {
        //given
        every { mockDataSource.deleteUser("1") } returns true

        //when
        userRepository.deleteUser(testUserOne)

        //then
        verify(exactly = 1) { mockDataSource.deleteUser("1") }
    }
}