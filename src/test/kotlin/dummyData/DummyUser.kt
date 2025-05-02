package dummyData

import data.repositories.mappers.UserMapper
import domain.entities.UserRole
import data.repositories.MD5Hasher

object DummyUser {
    val dummyUserOne = createDummyUser(
        id = "1",
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
        id = "2",
        username = "mateUserName",
        password = "matePassword",
        role = UserRole.MATE
    )


}