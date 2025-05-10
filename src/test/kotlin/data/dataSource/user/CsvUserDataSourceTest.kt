package data.dataSource.user

import com.github.doyaaaaaken.kotlincsv.client.CsvReader
import com.google.common.truth.Truth.assertThat
import data.dataSource.util.CsvHandler
import data.dto.UserDto
import dummyData.DummyUser.dummyUserOne
import dummyData.DummyUser.dummyUserOneDto
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import java.util.*

class CsvUserDataSourceTest {

    private lateinit var file: File
    private val csvHandler = CsvHandler(CsvReader())
    private val parser = UserDtoParser()
    private lateinit var dataSource: CsvUserDataSource

    @BeforeEach
    fun setUp() {
        file = File.createTempFile("user_test", ".csv")
        file.writeText("")
        dataSource = CsvUserDataSource(csvHandler = csvHandler, userDtoParser = parser, file = file)
    }

    @Test
    fun `should return true when add user to userCsvDataSource`() = runTest {
        val result = dataSource.addUser(dummyUserOneDto)
        assertThat(result).isTrue()
    }

    @Test
    fun `get user by id for an existing user will return same user object`()= runTest {
        dataSource.addUser(dummyUserOneDto)
        val resultUser = dataSource.getUserById(dummyUserOneDto.id)
        assertThat(resultUser).isEqualTo(dummyUserOneDto)
    }

    @Test
    fun `get user by id for not existed user will return null`() = runTest {
        val resultUser = dataSource.getUserById(dummyUserOneDto.id)
        assertThat(resultUser).isNull()
    }

    @Test
    fun `get user by userName for an existing user will return same user object`() = runTest {
        dataSource.addUser(dummyUserOneDto)
        val users = dataSource.getUsers()
        println(users.find { it.id == dummyUserOneDto.id })
        val resultUser = dataSource.getUserByUserName(dummyUserOneDto.username)
        assertThat(resultUser).isEqualTo(dummyUserOneDto)
    }

    @Test
    fun `get user by userName for not existed user will return null`() = runTest{
        val resultUser = dataSource.getUserByUserName(dummyUserOneDto.username)
        assertThat(resultUser).isNull()
    }

    @Test
    fun `delete user will return true if successful`() = runTest {
        dataSource.addUser(dummyUserOneDto)
        val result = dataSource.deleteUser(dummyUserOne.id.toString())
        assertThat(result).isTrue()
    }

    @Test
    fun `delete user will return false if unsuccessful`() = runTest{
        val result = dataSource.deleteUser(dummyUserOneDto.id)
        assertThat(result).isFalse()
    }

    @Test
    fun `get users will return non empty List of UserDto`() = runTest {
        dataSource.addUser(dummyUserOneDto)
        dataSource.addUser(dummyUserOneDto)
        dataSource.addUser(dummyUserOneDto)
        val users: List<UserDto> = dataSource.getUsers()
        assertThat(users).isNotEmpty()
    }

    @Test
    fun `get users on 3 saved users will return a list of size 3`() = runTest {
        dataSource.addUser(dummyUserOneDto)
        dataSource.addUser(dummyUserOneDto)
        dataSource.addUser(dummyUserOneDto)
        val users: List<UserDto> = dataSource.getUsers()
        assertThat(users).hasSize(3)
    }

    @Test
    fun `get users from empty dataSource will return empty List`() = runTest{
        val users: List<UserDto> = dataSource.getUsers()
        assertThat(users).isEmpty()
    }

    @Test
    fun `update user will return true when successful`() = runTest{
        dataSource.addUser(dummyUserOneDto)
        val result = dataSource.updateUser(dummyUserOneDto.copy(username = "updated"))
        assertThat(result).isTrue()
    }

    @Test
    fun `update user will return false when user does not exist`() = runTest {
        val result = dataSource.updateUser(dummyUserOneDto)
        assertThat(result).isFalse()
    }
}