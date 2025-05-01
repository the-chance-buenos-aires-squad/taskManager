package dummyData

import domain.entities.UserRole
import domain.util.MD5Hash

object DummyUser {
     val testUserOne = createDummyUser(
        id = "1",
        username = "testUser1",
        password = MD5Hash.hash("testPassword1"),
        role = UserRole.ADMIN
    )

     val testUserTwo = createDummyUser(
        id = "2",
        username = "testUser2",
        password = MD5Hash.hash("testPassword2"),
        role = UserRole.MATE
    )
}