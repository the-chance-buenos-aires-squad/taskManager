package data.dataSource.user

import com.github.doyaaaaaken.kotlincsv.client.CsvReader
import com.github.doyaaaaaken.kotlincsv.client.CsvWriter
import com.google.common.truth.Truth.assertThat
import data.dataSource.util.CsvHandler
import dummyData.DummyUser.dummyUpdatedUserOneRow
import dummyData.DummyUser.dummyUserOne
import dummyData.DummyUser.dummyUserOneRow
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.File
import java.io.FileNotFoundException
import java.io.PrintStream
import java.util.UUID

//new
class CsvUserDataSourceTest {

    private lateinit var file: File
    private val csvHandler = CsvHandler(CsvReader())
    private lateinit var dataSource: CsvUserDataSource

    @BeforeEach
    fun setUp() {
        file = File.createTempFile("user_test", ".csv")
        file.writeText("")
        dataSource = CsvUserDataSource(csvHandler = csvHandler, file)
    }


    @Test
    fun `should return true when add user to userCsvDataSource`() {
        //when
        val result = dataSource.addUser(dummyUserOneRow)

        //then
        assertThat(result).isTrue()
    }

    @Test
    fun `get user by id for an existing user will return same user object`() {
        //given
        dataSource.addUser(dummyUserOneRow)

        //when
        val resultUser = dataSource.getUserById(dummyUserOne.id)

        //then
        assertThat(resultUser).isEqualTo(dummyUserOneRow)
    }

    @Test
    fun `get user by id for not existed user will return null`() {
        //when
        val resultUser = dataSource.getUserById(dummyUserOne.id)
        //then
        assertThat(resultUser).isNull()
    }


    @Test
    fun `get user by userName for an existing user will return same user object`() {
        //given
        dataSource.addUser(dummyUserOneRow)

        //when
        val resultUser = dataSource.getUserByUserName(dummyUserOne.username)

        //then
        assertThat(resultUser).isEqualTo(dummyUserOneRow)
    }

    @Test
    fun `get user by userName for not existed user will return null`() {
        //when
        val resultUser = dataSource.getUserByUserName(dummyUserOne.username)
        //then
        assertThat(resultUser).isNull()
    }

    @Test
    fun `delete user will return true if successful`() {
        //given
        dataSource.addUser(dummyUserOneRow)


        //when
        val userIsDeleted = dataSource.deleteUser(dummyUserOne.id)


        //then
        assertThat(userIsDeleted).isTrue()
    }

    @Test
    fun `delete user will return false if unSuccessful`() {
        //when
        val userIsDeleted = dataSource.deleteUser(dummyUserOne.id)

        //then
        assertThat(userIsDeleted).isFalse()
    }

    @Test
    fun `get users will return non empty List of User`() {
        //given
        dataSource.addUser(dummyUserOneRow)
        dataSource.addUser(dummyUserOneRow)
        dataSource.addUser(dummyUserOneRow)

        //when
        val users: List<List<String>> = dataSource.getUsers()

        //then
        assertThat(users.isNotEmpty()).isTrue()

    }

    @Test
    fun `get users on 3 saved users will return a list of size 3 `() {
        //given
        dataSource.addUser(dummyUserOneRow)
        dataSource.addUser(dummyUserOneRow)
        dataSource.addUser(dummyUserOneRow)

        //when
        val users: List<List<String>> = dataSource.getUsers()

        //then
        assertThat(users).hasSize(3)
    }

    @Test
    fun `get users from empty dataSource will return  empty List`() {
        //when
        val users: List<List<String>> = dataSource.getUsers()

        //then
        assertThat(users).isEmpty()
    }

    @Test
    fun `update user will return true when successful`() {
        //given
        dataSource.addUser(dummyUserOneRow)

        //when
        val result = dataSource.updateUser(dummyUpdatedUserOneRow)

        // then
        assertThat(result).isTrue()
    }

    @Test
    fun `update user should be return false when UnSuccessful`() {
        //when
        val result = dataSource.updateUser(dummyUpdatedUserOneRow)

        // then
        assertThat(result).isFalse()
    }

    @Test
    fun `should handle an exceptions come from IO`() {
        //given
        file.setReadOnly()


        val result = dataSource.addUser(dummyUserOneRow)

        assertThat(result).isFalse()
        //when&then
        //assertThrows<FileNotFoundException> { dataSource.addUser(dummyUserOneRow) }



    }

}