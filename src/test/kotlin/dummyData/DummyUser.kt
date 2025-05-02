package dummyData

import domain.entities.UserRole
import domain.util.MD5Hasher

object DummyUser {
     val testUserOne = createDummyUser(
        id = "1",
        username = "adminUserName",
        password = MD5Hasher.hash("adminPassword"),
        role = UserRole.ADMIN
    )

     val testUserTwo = createDummyUser(
        id = "2",
        username = "mateUserName",
        password = MD5Hasher.hash("matePassword"),
        role = UserRole.MATE
    )
}