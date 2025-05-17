package dummyData

import data.dto.UserDto
import domain.entities.UserRole
import java.util.*

object DummyUser {
    val dummyUserOne = createDummyUser(
        id = UUID.fromString("e7a1a8b0-51e2-4e61-b4f6-7c9f3e05b221"),
        username = "adminUserName",
        role = UserRole.ADMIN
    )

    val dummyUserOneDto = UserDto(
        id = dummyUserOne.id.toString(),
        username = dummyUserOne.username,
        password = "adminUserName",
        role = dummyUserOne.role,
        createdAt = dummyUserOne.createdAt.toString()
    )

    val dummyUserOneRow = listOf(
        dummyUserOne.id.toString(),
        "adminUserName",
        "adminPassword",
        UserRole.ADMIN.name,
        dummyUserOne.createdAt.toString()
    )

    val dummyUserTwo = createDummyUser(
        id = UUID.fromString("e7a1a8b0-51e2-4e61-b4f6-7c9f3e05b222"),
        username = "mateUserName",
        role = UserRole.MATE
    )


//    val userMapper = UserCsvMapper()
//
//    val dummyUserOneRow = userMapper.mapEntityToRow(dummyUserOne)
//    val dummyUserTwoRow = userMapper.mapEntityToRow(dummyUserTwo)

    const val dummyUpdatedUserOneUserName = "new user name"
//    val dummyUpdatedUserOneRow = userMapper.mapEntityToRow(dummyUserOne.copy(username = dummyUpdatedUserOneUserName))

}