package dummyData

import domain.entities.UserRole

object DummyUser {
     val testUserOne = createDummyUser(
        id = "1",
        username = "testUser1",
        password = "testPassword1",
        role = UserRole.ADMIN
    )

     val testUserTwo = createDummyUser(
        id = "2",
        username = "testUser2",
        password = "testPassword2",
        role = UserRole.MATE
    )
}