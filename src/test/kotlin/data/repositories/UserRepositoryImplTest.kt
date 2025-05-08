package data.repositories

import com.google.common.truth.Truth.assertThat
import data.dataSource.user.CsvUserDataSource
import data.dto.UserDto
import data.repositories.mappers.UserDtoMapper
import dummyData.DummyUser.dummyUserOne
import dummyData.DummyUser.dummyUserOneDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class UserRepositoryImplTest {

    private val mockDataSource = mockk<CsvUserDataSource>(relaxed = true)
    private lateinit var userRepository: UserRepositoryImpl
    private val userMapper = UserDtoMapper()

    @BeforeEach
    fun setUp() {
        userRepository = UserRepositoryImpl(mockDataSource, userMapper)
    }

    @Test
    fun `should return true when adding a new user`() = runTest {
        // given
        coEvery { mockDataSource.addUser(any()) } returns true

        // when
        val result = userRepository.addUser(dummyUserOne)

        // then
        assertThat(result).isTrue()
    }

    @Test
    fun `should return user when searching by valid user id`() = runTest {
        // given
        val id = UUID.fromString("e7a1a8b0-51e2-4e61-b4f6-7c9f3e05b221")
        coEvery { mockDataSource.getUserById(id) } returns dummyUserOneDto

        // when
        val result = userRepository.getUserById(id)

        // then
        assertThat(result).isEqualTo(dummyUserOne)
    }

    @Test
    fun `should return null when user id does not exist`() = runTest {
        // given
        val id = UUID.fromString("e7a1a8b0-51e2-4e61-b4f6-7c9f3e05b223")
        coEvery { mockDataSource.getUserById(id) } returns null

        // when
        val result = userRepository.getUserById(id)

        // then
        assertThat(result).isNull()
    }

    @Test
    fun `should return user when searching by valid userName`() = runTest {
        // given
        coEvery { mockDataSource.getUserByUserName("adminUserName") } returns dummyUserOneDto

        // when
        val result = userRepository.getUserByUserName("adminUserName")

        // then
        assertThat(result).isEqualTo(dummyUserOne)
    }

    @Test
    fun `should return null when userName does not exist`() = runTest {
        // given
        coEvery { mockDataSource.getUserByUserName("testUser3") } returns null

        // when
        val result = userRepository.getUserByUserName("testUser3")

        // then
        assertThat(result).isNull()
    }

    @Test
    fun `should return all users when retrieving users`() = runTest {
        // given
        coEvery { mockDataSource.getUsers() } returns listOf(dummyUserOneDto, dummyUserOneDto)

        // when
        val result = userRepository.getUsers()

        // then
        assertThat(result.size).isEqualTo(2)
        assertThat(result).containsExactly(dummyUserOne, dummyUserOne)
    }

    @Test
    fun `should call updateItem on data source when updating a user`() = runTest {
        // given
        coEvery { mockDataSource.updateUser(dummyUserOneDto) } returns true

        // when
        userRepository.updateUser(dummyUserOne)

        // then
        coVerify(exactly = 1) { mockDataSource.updateUser(dummyUserOneDto) }
    }

    @Test
    fun `should call deleteItem on data source when deleting a user`() = runTest {
        // given
        val id = UUID.fromString("e7a1a8b0-51e2-4e61-b4f6-7c9f3e05b221")
        coEvery { mockDataSource.deleteUser(id) } returns true

        // when
        userRepository.deleteUser(dummyUserOne)

        // then
        coVerify(exactly = 1) { mockDataSource.deleteUser(id) }
    }
}