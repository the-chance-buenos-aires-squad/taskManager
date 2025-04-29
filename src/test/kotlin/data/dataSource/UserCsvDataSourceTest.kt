package data.dataSource

import com.google.common.truth.Truth.assertThat
import org.buinos.domain.entities.User
import org.buinos.domain.entities.UserRole
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class UserCsvDataSourceTest{



    private val dataSource = UserCsvDataSource()

    @Test
    fun `add user to userCscDayaSource`(){
        //given
        val newUser = User(id = "", username = "", password = "", role = UserRole.ADMIN, createdAt = LocalDateTime.now())

        //when
        val result = dataSource.insertItem(newUser)

        //then
        assertThat(result).isTrue()
    }


    @Test
    fun `get user by id for an existing user will return user object`(){
        //given
        val existUserId = "123"

        //when
        val resultUser = dataSource.getItemById(existUserId)

        //then
        assertThat(resultUser?.id).isEqualTo(existUserId)
    }


    @Test
    fun `get user by id for not existed user will return null`(){
        //given
        val existUserId = "123"

        //when
        val resultUser = dataSource.getItemById(existUserId)

        //then
        assertThat(resultUser).isNull()
    }



    @Test
    fun `delete user will return true if successful`(){
        //given
        val userId = " 123"


        //when
        val userIsDeleted = dataSource.deleteItem(userId)


        //then
        assertThat(userIsDeleted).isTrue()
    }

    @Test
    fun `delete user will return false if unSuccessful`(){
        //given
        val userId = " 123"


        //when
        val userIsDeleted = dataSource.deleteItem(userId)


        //then
        assertThat(userIsDeleted).isFalse()
    }



    @Test
    fun `get users will return non empty List of User`() {
        //when
        val users: List<User> = dataSource.getItems()

        //then
        assertThat(users.isNotEmpty()).isTrue()
    }

    @Test
    fun `get users from empty dataSource will return  empty List of User`() {
        //when
        val users: List<User> = dataSource.getItems()

        //then
        assertThat(users.isEmpty()).isTrue()
    }




    @Test
    fun `update user will return true when successful`(){
        //given
        val user = User(id = "", username = "old", password = "", role = UserRole.ADMIN, createdAt = LocalDateTime.now())


        //when
        val result = dataSource.updateItem(user.copy(username = "new"))

        // then
        assertThat(result).isTrue()
    }


    @Test
    fun `check the updated user properties is changed`() {
        //given
        val user =
            User(id = "userid", username = "mostafa", password = "", role = UserRole.ADMIN, createdAt = LocalDateTime.now())

        dataSource.insertItem(user)

        //when
        val updatedUserName = "falah"
        val result = dataSource.updateItem(user.copy(username = updatedUserName))
        val userFromDataSource = dataSource.getItemById("userid")
        // then
        assertThat(userFromDataSource?.username).isEqualTo(updatedUserName)
    }

}