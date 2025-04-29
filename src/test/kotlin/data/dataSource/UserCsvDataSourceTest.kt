package data.dataSource

import com.google.common.truth.Truth.assertThat
import org.buinos.domain.entities.User
import org.buinos.domain.entities.UserRole
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class UserCsvDataSourceTest{
    private val dataSource = UserCsvDataSource()
    val dummyUser = User(id = "1", username = "dummy user", password = "123456", role = UserRole.ADMIN, createdAt = LocalDateTime.now())

    @AfterEach
    fun cleanUpFile(){
        dataSource.getItems().forEach { dataSource.deleteItem(it.id) }
    }


    @Test
    fun `should return true when add user to userCscDayaSource`(){
        //when
        val result = dataSource.insertItem(dummyUser)

        //then
        assertThat(result).isTrue()
    }


    @Test
    fun `get user by id for an existing user will return same user object`(){
        //given
        dataSource.insertItem(dummyUser)

        //when
        val resultUser = dataSource.getItemById(dummyUser.id)

        //then
        assertThat(resultUser).isEqualTo(dummyUser)
    }


    @Test
    fun `get user by id for not existed user will return null`(){
        //when
        val resultUser = dataSource.getItemById(dummyUser.id)
        //then
        assertThat(resultUser).isNull()
    }



    @Test
    fun `delete user will return true if successful`(){
        //given
        dataSource.insertItem(dummyUser)


        //when
        val userIsDeleted = dataSource.deleteItem(dummyUser.id)


        //then
        assertThat(userIsDeleted).isTrue()
    }

    @Test
    fun `delete user will return false if unSuccessful`(){
        //when
        val userIsDeleted = dataSource.deleteItem(dummyUser.id)

        //then
        assertThat(userIsDeleted).isFalse()
    }



    @Test
    fun `get users will return non empty List of User`() {
        //given
        dataSource.insertItem(dummyUser)
        dataSource.insertItem(dummyUser)
        dataSource.insertItem(dummyUser)

        //when
        val users: List<User> = dataSource.getItems()

        //then
        assertThat(users.isNotEmpty()).isTrue()
    }

    @Test
    fun `get users from empty dataSource will return  empty List`() {
        //when
        val users: List<User> = dataSource.getItems()

        //then
        assertThat(users.isEmpty()).isTrue()
    }




    @Test
    fun `update user will return true when successful`(){
        //given
        dataSource.insertItem(dummyUser)


        //when
        val result = dataSource.updateItem(dummyUser.copy(username = "new user name"))

        // then
        assertThat(result).isTrue()
    }

    @Test
    fun `update user will false true when UnSuccessful`(){
        //when
        val result = dataSource.updateItem(dummyUser.copy(username = "new user name"))

        // then
        assertThat(result).isFalse()
    }


    @Test
    fun `check the updated user properties is changed`() {
        //given
        dataSource.insertItem(dummyUser)

        //when
        val updatedUserName = "mostafa"
        dataSource.updateItem(dummyUser.copy(username = updatedUserName))
        val userFromDataSource = dataSource.getItemById(dummyUser.id)
        // then
        assertThat(userFromDataSource?.username).isEqualTo(updatedUserName)
    }

}