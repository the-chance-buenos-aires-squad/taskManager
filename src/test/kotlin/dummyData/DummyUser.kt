package dummyData

import domain.entities.UserRole
import domain.util.MD5Hash

object DummyUser {
     val testUserOne = createDummyUser(
        id = "1",
        username = "adminUserName",
        password = MD5Hash.hash("adminPassword"),
        role = UserRole.ADMIN
    )

     val testUserTwo = createDummyUser(
        id = "2",
        username = "mateUserName",
        password = MD5Hash.hash("matePassword"),
        role = UserRole.MATE
    )
}