package dummyData

import data.repositories.mappers.UserMapper
import domain.entities.UserRole
import java.util.*

object DummyUser {
    val dummyUserOne = createDummyUser(
        id = UUID.fromString("e7a1a8b0-51e2-4e61-b4f6-7c9f3e05b221"),
        username = "adminUserName",
        password = "adminPassword",
        role = UserRole.ADMIN
    )

    val dummyAdminUser = dummyUserOne.copy()
    val userMapper = UserMapper()

    val dummyUserOneRow = userMapper.mapEntityToRow(dummyUserOne)

    const val dummyUpdatedUserOneUserName = "new user name"
    val dummyUpdatedUserOneRow = userMapper.mapEntityToRow(dummyUserOne.copy(username = dummyUpdatedUserOneUserName))


    val dummyUserTwo = createDummyUser(
        id = UUID.fromString("e7a1a8b0-51e2-4e61-b4f6-7c9f3e05b222"),
        username = "mateUserName",
        password = "matePassword",
        role = UserRole.MATE
    )


}