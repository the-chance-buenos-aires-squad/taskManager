package data.dataSource.user

import com.google.common.truth.Truth.assertThat
import data.dto.UserDto
import dummyData.DummyUser
import dummyData.DummyUser.dummyUserOneDto
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class MongoUserDataSourceTest {

    private  var dataSource: FakeMongoUserDataSource = FakeMongoUserDataSource()
    private  var testUser: UserDto = dummyUserOneDto


    @Test
    fun `addUser should return true and allow retrieval by id`() = runTest {
        val added = dataSource.addUser(testUser)
        assertThat(added).isTrue()

        val result = dataSource.getUserById(UUID.fromString(testUser._id))
        assertThat(result).isEqualTo(testUser)
    }

    @Test
    fun `getUserByUserName should return the correct user`() = runTest {
        dataSource.addUser(testUser)

        val result = dataSource.getUserByUserName(testUser.username)
        assertThat(result).isNotNull()
        assertThat(result!!.username).isEqualTo(testUser.username)
    }

    @Test
    fun `deleteUser should remove the user`() = runTest {
        dataSource.addUser(testUser)
        val deleted = dataSource.deleteUser(UUID.fromString(testUser._id))
        assertThat(deleted).isTrue()

        val result = dataSource.getUserById(UUID.fromString(testUser._id))
        assertThat(result).isNull()
    }

    @Test
    fun `getUsers should return all users`() = runTest {
        val user2 = testUser.copy(_id = UUID.randomUUID().toString(), username = "second")
        dataSource.addUser(testUser)
        dataSource.addUser(user2)

        val result = dataSource.getUsers()
        assertThat(result).hasSize(2)
        assertThat(result).containsExactly(testUser, user2)
    }

    @Test
    fun `updateUser should modify the existing user`() = runTest {
        dataSource.addUser(testUser)

        val updatedUser = testUser.copy(username = "updatedUser")
        val updated = dataSource.updateUser(updatedUser)
        assertThat(updated).isTrue()

        val result = dataSource.getUserById(UUID.fromString(testUser._id))
        assertThat(result?.username).isEqualTo("updatedUser")
    }
}