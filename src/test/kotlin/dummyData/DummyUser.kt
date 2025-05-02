package dummyData

import data.repositories.mappers.UserMapper
import domain.entities.UserRole
import domain.util.MD5Hash

object DummyUser {
    val dummyUserOne = createDummyUser(
        id = "1",
        username = "adminUserName",
        password = MD5Hash.hash("adminPassword"),
        role = UserRole.ADMIN
    )
    val userMapper = UserMapper()
    val dummyUserOneRow = userMapper.mapEntityToRow(dummyUserOne)
    const val dummyUpdatedUserOneUserName = "new user name"
    val dummyUpdatedUserOneRow = userMapper.mapEntityToRow(dummyUserOne.copy(username = dummyUpdatedUserOneUserName))


    val dummyUserTwo = createDummyUser(
        id = "2",
        username = "mateUserName",
        password = MD5Hash.hash("matePassword"),
        role = UserRole.MATE
    )


}