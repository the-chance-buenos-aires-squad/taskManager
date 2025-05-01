package data.dataSource

import com.google.common.truth.Truth.assertThat
import domain.entities.User
import domain.entities.UserRole
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class UserCsvDataSourceTest {
    private val dataSource = UserCsvDataSource()
    private val dummyUser = User(
        id = "1",
        username = "dummy user",
        password = "123456",
        role = UserRole.ADMIN,
        createdAt = LocalDateTime.now()
    )

    @AfterEach
    fun cleanUpFile() {
        dataSource.getUsers().forEach { dataSource.deleteUser(it.id) }
    }

    @Test
    fun `should return true when add user to userCscDayaSource`() {
        //when
        val result = dataSource.insertUser(dummyUser)

        //then
        assertThat(result).isTrue()
    }

    @Test
    fun `get user by id for an existing user will return same user object`() {
        //given
        dataSource.insertUser(dummyUser)

        //when
        val resultUser = dataSource.getUserById(dummyUser.id)

        //then
        assertThat(resultUser).isEqualTo(dummyUser)
    }

    @Test
    fun `get user by id for not existed user will return null`() {
        //when
        val resultUser = dataSource.getUserById(dummyUser.id)
        //then
        assertThat(resultUser).isNull()
    }


    @Test
    fun `get user by userName for an existing user will return same user object`() {
        //given
        dataSource.insertUser(dummyUser)

        //when
        val resultUser = dataSource.getUserByUserName(dummyUser.username)

        //then
        assertThat(resultUser).isEqualTo(dummyUser)
    }

    @Test
    fun `get user by userName for not existed user will return null`() {
        //when
        val resultUser = dataSource.getUserByUserName(dummyUser.username)
        //then
        assertThat(resultUser).isNull()
    }

    @Test
    fun `delete user will return true if successful`() {
        //given
        dataSource.insertUser(dummyUser)


        //when
        val userIsDeleted = dataSource.deleteUser(dummyUser.id)


        //then
        assertThat(userIsDeleted).isTrue()
    }

    @Test
    fun `delete user will return false if unSuccessful`() {
        //when
        val userIsDeleted = dataSource.deleteUser(dummyUser.id)

        //then
        assertThat(userIsDeleted).isFalse()
    }

    @Test
    fun `get users will return non empty List of User`() {
        //given
        dataSource.insertUser(dummyUser)
        dataSource.insertUser(dummyUser)
        dataSource.insertUser(dummyUser)

        //when
        val users: List<User> = dataSource.getUsers()

        //then
        assertThat(users.isNotEmpty()).isTrue()
    }

    @Test
    fun `get users from empty dataSource will return  empty List`() {
        //when
        val users: List<User> = dataSource.getUsers()

        //then
        assertThat(users.isEmpty()).isTrue()
    }

    @Test
    fun `update user will return true when successful`() {
        //given
        dataSource.insertUser(dummyUser)

        //when
        val result = dataSource.updateUser(dummyUser.copy(username = "new user name"))

        // then
        assertThat(result).isTrue()
    }

    @Test
    fun `update user will false true when UnSuccessful`() {
        //when
        val result = dataSource.updateUser(dummyUser.copy(username = "new user name"))

        // then
        assertThat(result).isFalse()
    }

    @Test
    fun `check the updated user properties is changed`() {
        //given
        dataSource.insertUser(dummyUser)

        //when
        val updatedUserName = "mostafa"
        dataSource.updateUser(dummyUser.copy(username = updatedUserName))
        val userFromDataSource = dataSource.getUserById(dummyUser.id)
        // then
        assertThat(userFromDataSource?.username).isEqualTo(updatedUserName)
    }

}