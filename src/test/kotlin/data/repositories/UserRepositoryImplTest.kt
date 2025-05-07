package data.repositories

import com.google.common.truth.Truth.assertThat
import data.dataSource.user.CsvUserDataSource
import data.repositories.mappers.userMappers.UserCsvMapper
import dummyData.DummyUser.dummyUserOne
import dummyData.DummyUser.dummyUserOneRow
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class UserRepositoryImplTest {

    private val mockDataSource = mockk<CsvUserDataSource>(relaxed = true)
    private lateinit var userRepository: UserRepositoryImpl
    private val userMapper = UserCsvMapper()

    @BeforeEach
    fun setUp() {
        userRepository = UserRepositoryImpl(mockDataSource, userMapper)
    }

    @Test
    fun `should return true when adding a new user`() {
        //given
        every { mockDataSource.addUser(any()) } returns true

        //when
        val result = userRepository.addUser(dummyUserOne)

        //then
        assertThat(result).isTrue()
    }

    @Test
    fun `should return user when searching by valid user id`() {
        //given
        every { mockDataSource.getUserById(UUID.fromString("e7a1a8b0-51e2-4e61-b4f6-7c9f3e05b221")) } returns dummyUserOneRow

        //when
        val result = userRepository.getUserById(UUID.fromString("e7a1a8b0-51e2-4e61-b4f6-7c9f3e05b221"))

        //then
        assertThat(result).isEqualTo(dummyUserOne)
    }

    @Test
    fun `should return null when user id does not exist`() {
        //given
        every { mockDataSource.getUserById(UUID.fromString("e7a1a8b0-51e2-4e61-b4f6-7c9f3e05b223")) } returns null

        //when
        val result = userRepository.getUserById(UUID.fromString("e7a1a8b0-51e2-4e61-b4f6-7c9f3e05b223"))

        //then
        assertThat(result).isNull()
    }

    @Test
    fun `should return user when searching by valid userName`() {
        //given
        every { mockDataSource.getUserByUserName("adminUserName") } returns dummyUserOneRow

        //when
        val result = userRepository.getUserByUserName("adminUserName")

        //then
        assertThat(result).isEqualTo(dummyUserOne)
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
        every { mockDataSource.getUsers() } returns listOf(dummyUserOneRow, dummyUserOneRow)

        //when
        val result = userRepository.getUsers()

        //then
        assertThat(result.size).isEqualTo(2)
    }

    @Test
    fun `should call updateItem on data source when updating a user`() {
        //given
        every { mockDataSource.updateUser(dummyUserOneRow) } returns true

        //when
        userRepository.updateUser(dummyUserOne)

        //then
        verify(exactly = 1) { mockDataSource.updateUser(dummyUserOneRow) }
    }

    @Test
    fun `should call deleteItem on data source when deleting a user`() {
        //given
        every { mockDataSource.deleteUser(UUID.fromString("e7a1a8b0-51e2-4e61-b4f6-7c9f3e05b221")) } returns true

        //when
        userRepository.deleteUser(dummyUserOne)

        //then
        verify(exactly = 1) { mockDataSource.deleteUser(UUID.fromString("e7a1a8b0-51e2-4e61-b4f6-7c9f3e05b221")) }
    }
}