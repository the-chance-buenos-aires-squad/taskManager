package data.repositories

import com.google.common.truth.Truth.assertThat
import data.dataSource.DataSource
import dummyData.DummyUser.testUserOne
import dummyData.DummyUser.testUserTwo
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.buinos.domain.entities.User
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class UserRepositoryImplTest {

    private val mockDataSource = mockk<DataSource<User>>()
    private lateinit var userRepository: UserRepositoryImpl


    @BeforeEach
    fun setUp() {
        userRepository = UserRepositoryImpl(mockDataSource)
    }


    @Test
    fun `should return true when adding a new user`() {
        //given
        every { mockDataSource.insertItem(testUserOne) } returns true

        //when
        val result = userRepository.insertUser(testUserOne)

        //then
        assertThat(result).isTrue()
    }

    @Test
    fun `should return user when searching by valid user id`() {
        //given
        every { mockDataSource.getItemById("1") } returns testUserOne

        //when
        val result = userRepository.getUserById("1")

        //then
        assertThat(result).isEqualTo(testUserOne)
    }

    @Test
    fun `should return null when user does not exist`() {
        //given
        every { mockDataSource.getItemById("3") } returns null

        //when
        val result = userRepository.getUserById("3")

        //then
        assertThat(result).isNull()
    }

    @Test
    fun `should return all users when retrieving users`() {
        //given
        every { mockDataSource.getItems() } returns listOf(testUserOne, testUserTwo)

        //when
        val result = userRepository.getUsers()

        //then
        assertThat(result.size).isEqualTo(2)
    }

    @Test
    fun `should call updateItem on data source when updating a user`() {
        //given
        every { mockDataSource.updateItem(testUserOne) } returns true

        //when
        userRepository.updateUser(testUserOne)

        //then
        verify(exactly = 1) { mockDataSource.updateItem(testUserOne) }
    }


    @Test
    fun `should call deleteItem on data source when deleting a user`() {
        //given
        every { mockDataSource.deleteItem("1") } returns true

        //when
        userRepository.deleteUser(testUserOne)

        //then
        verify(exactly = 1) { mockDataSource.deleteItem("1") }
    }
}